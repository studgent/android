package be.ugent.oomo.groep12.studgent.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.location.Location;
import be.ugent.oomo.groep12.studgent.common.CalendarEvent;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;

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
		
		//TODO: retrieve the actual data
		Location loc = new Location("passive");
		loc.setLatitude(0.0);
		loc.setLongitude(0.0);
		PointOfInterest poi = new PointOfInterest(1,"Gent", loc );
		for (int i= 0; i< 20; i++) {
			CalendarEvent cal_event = new CalendarEvent(i, "Gentse Feesten", new Date(), poi);
			items.put(cal_event.getId(), cal_event);
		}
	}
	
	@Override
	public ICalendarEvent getDetails(int id) {
		return items.get(id);
	}
	
	

}
