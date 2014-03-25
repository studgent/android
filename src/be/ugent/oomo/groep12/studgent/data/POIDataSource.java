package be.ugent.oomo.groep12.studgent.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.util.Log;
import be.ugent.oomo.groep12.studgent.common.CalendarEvent;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.utilities.CurlUtil;

public class POIDataSource implements IDataSource {

	private static POIDataSource instance = null;
	protected static Map<Integer, IPointOfInterest> items;
	
	protected POIDataSource() {
      // Exists only to defeat instantiation.
	}
	
	public static POIDataSource getInstance() {
		if (instance == null) {
			instance = new POIDataSource();
		}
		return instance;
	}

	
	@Override
	public Map<Integer, IPointOfInterest> getLastItems() {
		if (items == null ) {
			populateList();
		}
		return items;
	}
	
	private void populateList(){
		items = new HashMap<Integer,IPointOfInterest>();
		
		try {
			String apidata =  CurlUtil.get("poi");
			JSONArray poi_items = new JSONArray(apidata);
			for (int i = 0; i < poi_items.length(); i++) {
				JSONObject item = poi_items.getJSONObject(i);
				PointOfInterest point = parse(item);
				items.put(point.getId(), point);
			}
			
		} catch (CurlException e) {
			Log.e("error retrieving calendar", e.getLocalizedMessage());
		} catch (JSONException e){
			Log.e("error parsing json", e.getLocalizedMessage());
		} catch (ParseException e){
			Log.e("error parsing date", e.getLocalizedMessage());
		}
	}
	
	protected PointOfInterest parse(JSONObject item) throws JSONException, ParseException{
		PointOfInterest poi;
		int id = item.optInt("id",0);
		String type = item.optString("type",""),
			   name = item.optString("name", ""),
			   details = item.optString("details", ""),
			   uri = item.optString("uri", ""),
			   cafeplan_uri = item.optString("cafeplan_uri", "");
			  
		Double latitude = item.optDouble("latitude", 0.0),
			   longitude = item.optDouble("longitude", 0.0);
		LatLng location = new LatLng(latitude,longitude);
		
		String street = "Dummy straat";
		String streetNumber="3";
		
		
		poi  = new PointOfInterest(id, name, details, street , streetNumber , location );
		if (uri != null){
			poi.setUrl(uri);	
		}else{
			poi.setUrl(cafeplan_uri);
		}
		return poi;
	}

	@Override
	public IData getDetails(int id) {
		return null;
	}

}
