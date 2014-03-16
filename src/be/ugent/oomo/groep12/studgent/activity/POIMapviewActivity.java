package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.POIDataSource;


public class POIMapviewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_mapview);
		
		
		
		FragmentManager fmanager = getFragmentManager();
		MapFragment map = (MapFragment)(fmanager.findFragmentById(R.id.mapFullscreen));
	
		loadPOIs(map.getMap());
	}
	
	
	
	
	private void loadPOIs(GoogleMap map){
		
		Map<Integer, PointOfInterest> data= POIDataSource.getInstance().getLastItems();
		
		for (Map.Entry<Integer, PointOfInterest> poi : data.entrySet())
		{
			
			//PointOfInterest poi = new PointOfInterest(new LatLng(51.05389,3.705));
			//poi.setName("test marker");
			
		    //map.setMyLocationEnabled(true);
	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(poi.getValue().getLocation(), 16));

	        map.addMarker(new MarkerOptions()
	                .title("Ghent (" + poi.getValue().getId() +")")
	                .snippet(poi.getValue().getName())
	                .position(poi.getValue().getLocation()));
		} 
	}
	
	
}
