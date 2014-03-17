package be.ugent.oomo.groep12.studgent.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.maps.model.LatLng;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;
import be.ugent.oomo.groep12.studgent.common.CalendarEvent;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.utilities.CurlUtil;

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
		if (items == null /* || last updated < now - 1 day ago */ ) { // check if information missing or outdated
			populateList();
		}
		// return the list
		return items;
	}
	
	protected void populateList(){
		// populate the list
		items = new HashMap<Integer,ICalendarEvent>();
		
		// example code for retrieving data from api
		try {
			String apidata =  CurlUtil.get("cal");
			
			Log.i("retrieving calendar",apidata);
		} catch (CurlException e) {
			Log.e("error retrieving calendar", e.getLocalizedMessage());
		}
		// end example*/
		
		LatLng loc = new LatLng(51.0500, 3.7333);
		for (int i= 0; i< 20; i++) {
			PointOfInterest poi = new PointOfInterest(i,"Gent", loc );
			CalendarEvent cal_event = new CalendarEvent(i, "Gentse "+ i +" Feesten", new Date(), poi);
			items.put(cal_event.getId(), cal_event);
		}
	}
	
	@Override
	public ICalendarEvent getDetails(int id) {
		return items.get(id);
	}
	
	

}
