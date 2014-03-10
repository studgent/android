package be.ugent.oomo.groep12.studgent.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class CalendarEventDataSource implements IDataSource {
	
	private static CalendarEventDataSource instance = null;
	protected static List<ICalendarEvent> items;
	
	protected CalendarEventDataSource() {
      // Exists only to defeat instantiation.
	}
	public static CalendarEventDataSource getInstance() {
		if (instance == null) {
			instance = new CalendarEventDataSource();
		}
		return instance;
	}

	public List<ICalendarEvent> getLastItems() {
		if (items == null /* || last updated < now - 1 day ago */ ) { // check if information missing or outdated
			// populate the list
			items = new ArrayList<ICalendarEvent>();
			
			
			//TODO: retrieve the actual data
			Location loc = new Location("passive");
			loc.setLatitude(0.0);
			loc.setLongitude(0.0);
			PointOfInterest poi = new PointOfInterest( loc );
			
			CalendarEvent cal_event = new CalendarEvent( "Gentse Feesten", new Date(), poi);
			for (int i= 0; i< 20; i++) {
				items.add( cal_event );
			}
		}
		
		// return the list
		return items;
	}

}
