package be.ugent.oomo.groep12.studgent.data;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.maps.model.LatLng;

import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;

public class POIDataSource implements IDataSource {

	private static POIDataSource instance = null;
	protected static Map<Integer, PointOfInterest> items;
	
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
	public Map<Integer, PointOfInterest> getLastItems() {
		// TODO Auto-generated method stub
		items = new HashMap<Integer, PointOfInterest>();
		items.put(1, new PointOfInterest(1, "test 1", "Details", new LatLng(51.05389,3.705)));
		items.put(2, new PointOfInterest(1, "test 2", "Details", new LatLng(51.05489,3.705)));
		items.put(3, new PointOfInterest(1, "test 3", "Details", new LatLng(51.05889,3.705)));
		items.put(4, new PointOfInterest(1, "test 4", "Details", new LatLng(51.05089,3.705)));
		
		
		
		return items;
	}

	@Override
	public IData getDetails(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
