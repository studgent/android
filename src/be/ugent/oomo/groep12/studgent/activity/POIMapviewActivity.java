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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.POIDataSource;


public class POIMapviewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
                R.anim.animation_leave);
		setContentView(R.layout.activity_poi_mapview);
		
		
		
		FragmentManager fmanager = getFragmentManager();
		MapFragment map = (MapFragment)(fmanager.findFragmentById(R.id.mapFullscreen));
	
		loadPOIs(map.getMap());
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poi, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.poi_open_augmented:
	    	openAugmentedViewActivity();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
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


	public void openAugmentedViewActivity(View view) {
		openAugmentedViewActivity();
	}

	public void openAugmentedViewActivity() {
		Intent intent = new Intent(this, AugmentedViewActivity.class);
		startActivity(intent);
	}
	
}
