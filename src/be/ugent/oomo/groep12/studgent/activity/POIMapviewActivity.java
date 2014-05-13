package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.adapter.CalenderAdapter;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.CalendarEventDataSource;
import be.ugent.oomo.groep12.studgent.data.POIDataSource;
import be.ugent.oomo.groep12.studgent.utilities.MenuUtil;
import be.ugent.oomo.groep12.studgent.utilities.PlayServicesUtil;

public class POIMapviewActivity extends Activity implements
		OnInfoWindowClickListener, ActionBar.OnNavigationListener,
		LocationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	protected Map<String, IPointOfInterest> marker_data;
	protected MapFragment mapFragment;
	protected GoogleMap map;
	private LocationManager locationManager;
	private static final long MIN_TIME = 400;
	private static final float MIN_DISTANCE = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_poi_mapview);


		marker_data = new HashMap<String, IPointOfInterest>();

		FragmentManager fmanager = getFragmentManager();
		mapFragment = (MapFragment) (fmanager
				.findFragmentById(R.id.mapFullscreen));
		map = mapFragment.getMap();
		if(map != null) {
			map.setMyLocationEnabled(true);
		}
		locationManager = (LocationManager)
		getSystemService(Context.LOCATION_SERVICE);
		//LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER and
		//LocationManager.PASSIVE_PROVIDER
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		MIN_TIME, MIN_DISTANCE, this);

		String noPlayServices = "Google Play Services not found, map will not be shown.";
		if (PlayServicesUtil.hasPlayServices(this, noPlayServices)) {
			loadPOIs();
		} else {
			setNavigation();
		}
	}

	// START switchbar

	protected void setNavigation() {
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		String[] entries;

		// Check whether augmented can be used
		SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Sensor magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		if (accelerometerSensor != null && magnetometerSensor != null) {
		    // Device has Accelerometer and Magnetometer
			entries = new String[] { "Map", "Lijst", "Augmented" };
		} else {
			entries = new String[] { "Map", "Lijst" };
		}
		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, entries), this);
		actionBar.setSelectedNavigationItem(0);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		Log.i("selected ", "" + itemPosition + '-' + itemId);
		// keep correct item in this activity
		getActionBar().setSelectedNavigationItem(0);
		switch (itemPosition) {
		case 1:
			openPOIListActivity();
			return true;
		case 2:
			openAugmentedViewActivity();
			return true;
		default:
			return false;
		}
	}

	// END switchbar


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {

		return MenuUtil.PrepareMenu(this, menu);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    return MenuUtil.OptionsItemSelected(this, item);
	}
	

	private void openPOIListActivity() {
		Intent intent = new Intent(this, POIListActivity.class);
		intent.putExtra("filter", false);
		startActivity(intent);
	}

	private void loadPOIs() {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.05389,
				3.725), 15));
		new AsyncPOILoader().execute(map);

		map.setOnInfoWindowClickListener(this);
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

	private class AsyncPOILoader extends
			AsyncTask<GoogleMap, Integer, GoogleMap> {
		protected Map<Integer, IPointOfInterest> data;

		@Override
		protected GoogleMap doInBackground(GoogleMap... params) {
			GoogleMap map = params[0];
			data = POIDataSource.getInstance().getLastItems();
			return map;
		}

		protected void onPostExecute(GoogleMap map) {

			setNavigation();
			for (Map.Entry<Integer, IPointOfInterest> poi : data.entrySet()) {

				Marker marker = map
						.addMarker(new MarkerOptions()
								.title("" + Html.fromHtml( poi.getValue().getName() ).toString() )
								.snippet("" + Html.fromHtml(
										poi.getValue().getDetails() + "\n"
												+ poi.getValue().getUrl()).toString())
								.position(poi.getValue().getLocation()) 
								.icon(BitmapDescriptorFactory
										.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
								.flat(true));
				marker_data.put(marker.getId(), poi.getValue());
			}

		}
	}

	// implementation LocationListener
	@Override
	public void onLocationChanged(Location location) {
		LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,
				10);
		map.animateCamera(cameraUpdate);
		locationManager.removeUpdates(this);
	}
	
	@Override
	public void onPause() {
		
		locationManager.removeUpdates(this);
		super.onPause();
	}
	
	@Override
	public void onResume(){
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
		super.onResume();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

}
