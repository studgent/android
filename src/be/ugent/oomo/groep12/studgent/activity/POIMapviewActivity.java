package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.adapter.CalenderAdapter;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.CalendarEventDataSource;
import be.ugent.oomo.groep12.studgent.data.POIDataSource;
import be.ugent.oomo.groep12.studgent.utilities.PlayServicesUtil;


public class POIMapviewActivity extends Activity implements OnInfoWindowClickListener {
	
	protected Map<String, IPointOfInterest> marker_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,R.anim.animation_leave);
		setContentView(R.layout.activity_poi_mapview);
		marker_data = new HashMap<String, IPointOfInterest>();
		
		
		FragmentManager fmanager = getFragmentManager();
		MapFragment map = (MapFragment)(fmanager.findFragmentById(R.id.mapFullscreen));
	
		String noPlayServices = "Google Play Services not found, map will not be shown."; 
		if (PlayServicesUtil.hasPlayServices(this, noPlayServices)){
			loadPOIs(map.getMap());
		}		
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
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.05389,3.705), 16));
		new AsyncPOILoader().execute(map);
		
		map.setOnInfoWindowClickListener( this);
	}


	public void openAugmentedViewActivity(View view) {
		openAugmentedViewActivity();
	}

	public void openAugmentedViewActivity() {
		Intent intent = new Intent(this, AugmentedViewActivity.class);
		startActivity(intent);
	}



	@Override
	public void onInfoWindowClick(Marker marker) {
		IPointOfInterest poi = marker_data.get(marker.getId());
		Log.i("clicked on marker", marker.getId() + '-' + poi.getName());
		Intent intent = new Intent(this, POIDetailActivity.class);
		intent.putExtra("poi", poi);
		startActivity(intent);
	}
	

	private class AsyncPOILoader  extends AsyncTask<GoogleMap, Integer, GoogleMap> {
		protected Map<Integer, IPointOfInterest> data;
		
	  	@Override
		protected GoogleMap doInBackground(GoogleMap... params) {
	  		GoogleMap map = params[0];		
	  		data = POIDataSource.getInstance().getLastItems();
	  		return map;
		}
	  	
	  	 protected void onPostExecute(GoogleMap map) {
	  		for (Map.Entry<Integer, IPointOfInterest> poi : data.entrySet())
	 		{
	 			
	 		 	Marker marker = map.addMarker(new MarkerOptions()
							 	                .title(poi.getValue().getName())
							 	                .snippet(poi.getValue().getDetails() + "\n" + poi.getValue().getUrl())
							 	                .position(poi.getValue().getLocation())
							 	                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
							 	                .flat(true));
	 		 	marker_data.put(marker.getId(), poi.getValue());
	 		}
	  		
	   }
	}

}






