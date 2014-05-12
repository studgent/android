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
import be.ugent.oomo.groep12.studgent.utilities.JSONUtil;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;

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
		
		String type = JSONUtil.optString(item,"type"),
			   name = JSONUtil.optString(item,"name"),
			   details = JSONUtil.optString(item,"details"),
			   uri = JSONUtil.optString(item,"uri"),
			   cafeplan_uri = JSONUtil.optString(item,"cafeplan_uri");
			  
		Double latitude = item.optDouble("latitude", 0.0),
			   longitude = item.optDouble("longitude", 0.0);
		LatLng location = new LatLng(latitude,longitude);
		
		String street = null;
		String streetNumber= null;
		
		
		poi  = new PointOfInterest(id, name, details, street , streetNumber , location );
		if (uri != null){
			poi.setUrl(uri);	
		}else{
			poi.setUrl("http://www.cafeplan.be" + cafeplan_uri);
		}
		return poi;
	}

	@Override
	public IData getDetails(int id) {
		return null;
	}

	public boolean checkin(PointOfInterest poi) {

		int userID = LoginUtility.getInstance().getId();
		
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("token", LoginUtility.getInstance().getToken() );
		postData.put("longitude", String.valueOf(poi.getLocation().longitude));
		postData.put("latitude", String.valueOf(poi.getLocation().latitude) );
		postData.put("message", "" );
		
		boolean added = false;
		//update online	
		//http://studgent.ahlun.be/user/{user_id}/checkin/{poi_id}
		String resource = "user/" + 
						  userID + 
						  "/checkin/" + 
						  poi.getId();
		try {
			String apidata =  CurlUtil.post(resource, postData);
			JSONObject items = new JSONObject(apidata);
			String message = items.getString("status");
			if ( message.equalsIgnoreCase("OK") ){
				added = true;
			} else { // somethings wrong or user removed from list
				added = false;
			}
		} catch (CurlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return added;
	}

	
	public void delete(){
		items = null;
	}

}
