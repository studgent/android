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
import android.annotation.SuppressLint;
import android.util.Log;
import be.ugent.oomo.groep12.studgent.common.CalendarEvent;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.utilities.CurlUtil;
import be.ugent.oomo.groep12.studgent.utilities.JSONUtil;

/**
 * DataSource provider for calendar items.
 * 
 * Using a singleton design pattern
 *
 */

@SuppressLint("UseSparseArrays")
public class CalendarEventDataSource implements IDataSource {
	
	private static CalendarEventDataSource instance = null;
	protected static Map<Integer, ICalendarEvent> items;
	
	protected CalendarEventDataSource() {
      // Exists only to defeat instantiation.
	}
	
	public static CalendarEventDataSource getInstance() {
		if (instance == null) {
			instance = new CalendarEventDataSource();
		}
		return instance;
	}

	public Map<Integer, ICalendarEvent> getLastItems() {
		if (items == null ) {
			populateList();
		}
		return items;
	}
	
	protected void populateList(){
		// populate the list
		items = new HashMap<Integer,ICalendarEvent>();
		
		try {
			Log.i("retrieving resource", "cal");
			// populate with 'cal' resource
			// 'cal/all' returns all items
			// 'cal' returns a filtered list
			String apidata =  CurlUtil.get("cal");
			JSONArray calendar_items = new JSONArray(apidata);
			for (int i = 0; i < calendar_items.length(); i++) {
				JSONObject item = calendar_items.getJSONObject(i);
				CalendarEvent cal_event = parseEvent(item);
				items.put(cal_event.getId(), cal_event);
			}
			Log.i("items", ""+items.size());
		} catch (CurlException e) {
			Log.e("error retrieving calendar", e.getLocalizedMessage());
		} catch (JSONException e){
			Log.e("error parsing json", e.getLocalizedMessage());
		} catch (ParseException e){
			Log.e("error parsing date", e.getLocalizedMessage());
		}

	}
	
	@SuppressLint("SimpleDateFormat")
	protected CalendarEvent parseEvent(JSONObject item) throws JSONException, ParseException{
		CalendarEvent cal_event;
		int id = item.optInt("id",0);
		String type = JSONUtil.optString(item, "type"),
			   name = JSONUtil.optString(item, "name"),
			   details = JSONUtil.optString(item, "details"),
			   description = JSONUtil.optString(item, "description"),
			   contact = JSONUtil.optString(item, "contact"),
			   street = JSONUtil.optString(item, "street"),
			   number = JSONUtil.optString(item, "number"),
			   phone = JSONUtil.optString(item, "phone"),
			   email = JSONUtil.optString(item, "email"),
			   uri = JSONUtil.optString(item, "uri"),
			   image = JSONUtil.optString(item, "image"),
			   prices = JSONUtil.optString(item, "prices");
		
		//todo fix default date value
		Date date_from = item.optString("date_from").equals("null") ? 
				new Date():
				new SimpleDateFormat("yyyy-MM-dd").parse(item.optString("date_from"));
		Date date_to = item.optString("date_from").equals("null") ? 
				new Date():
				new SimpleDateFormat("yyyy-MM-dd").parse(item.optString("date_to"));
		
		Double latitude = item.optDouble("latitude", 0.0),
			   longitude = item.optDouble("longitude", 0.0);
		LatLng location = new LatLng(latitude,longitude);
		
		cal_event = new CalendarEvent(id, type, name, date_from, date_to, details, description, contact, phone, email, uri, image, prices, street, number, location);
		return cal_event;
	}
	
	@Override
	public ICalendarEvent getDetails(int id) {
		return items.get(id);
	}

	
	public void delete(){
		items = null;
	}
	

}
