package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.adapter.POIAdapter;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.common.QuizQuestion;
import be.ugent.oomo.groep12.studgent.data.POIDataSource;
import be.ugent.oomo.groep12.studgent.data.QuizQuestionsDataSource;
import be.ugent.oomo.groep12.studgent.exception.DataSourceException;
import be.ugent.oomo.groep12.studgent.utilities.LayoutUtil;
import be.ugent.oomo.groep12.studgent.utilities.MenuUtil;

public class POIListActivity extends Activity implements
		AdapterView.OnItemClickListener, ActionBar.OnNavigationListener,
		TextWatcher, LocationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	protected PointOfInterest[] poi_data;
	protected POIAdapter adapter;
	protected ListView poi_list_view;
	protected EditText inputSearch;
	protected boolean filterByDistance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_poi_list);
		setNavigation();
		filterByDistance = false;
		filterByDistance = getIntent().getExtras().getBoolean("filter");
		System.out.println("filteren?----------" + filterByDistance);
		// hide keyboard on start activity
		this.getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		poi_list_view = (ListView) findViewById(R.id.poi_list);
		inputSearch = (EditText) findViewById(R.id.searchPOI_EditText);
		inputSearch.addTextChangedListener(this);

		poi_list_view.setOnItemClickListener(this);

		/*
		 * View header =
		 * (View)getLayoutInflater().inflate(R.layout.listview_header_row,
		 * null); event_list_view.addHeaderView(header);
		 */

		// start GPS
		startGPS();

		// create adapter with empty list and attach custom item view

		adapter = new POIAdapter(this, R.layout.poi_list_item,
				new ArrayList<PointOfInterest>());
		poi_list_view.setAdapter(adapter);
		new AsyncPOIListViewLoader().execute(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return MenuUtil.PrepareMenu(this, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuUtil.OptionsItemSelected(this, item);
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
		Sensor accelerometerSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Sensor magnetometerSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
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
		actionBar.setSelectedNavigationItem(1);
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
		getActionBar().setSelectedNavigationItem(1);
		switch (itemPosition) {
		case 0:
			openMapviewActivity();
			return true;
		case 2:
			openAugmentedViewActivity();
			return true;
		default:
			return false;
		}
	}

	// END switchbar

	public void openAugmentedViewActivity(View view) {
		openAugmentedViewActivity();
	}

	public void openAugmentedViewActivity() {
		Intent intent = new Intent(this, AugmentedViewActivity.class);
		startActivity(intent);
	}

	public void openMapviewActivity(View view) {
		openMapviewActivity();
	}

	public void openMapviewActivity() {
		Intent intent = new Intent(this, POIMapviewActivity.class);
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View item, int position,
			long rowID) {
		PointOfInterest poi = this.adapter.getItem(position);
		Intent intent = new Intent(this, POIDetailActivity.class);
		intent.putExtra("poi", poi);
		if (filterByDistance)
			intent.putExtra("parentIsCheckInActivity", true);
		startActivity(intent);
	}

	private class AsyncPOIListViewLoader extends
			AsyncTask<POIAdapter, Void, ArrayList<IPointOfInterest>> {
		private final ProgressDialog dialog = new ProgressDialog(
				POIListActivity.this);

		@Override
		protected void onPostExecute(ArrayList<IPointOfInterest> result) {
			super.onPostExecute(result);

			updateLocation(null);

			dialog.dismiss();
			// adapter.setItemList(result);
			adapter.clear();
			for (IPointOfInterest poi : result) {
				if (filterByDistance) {
					if (poi.getDistance() <= getResources().getInteger(
							R.integer.max_distance_to_checkin))
						adapter.add((PointOfInterest) poi);
				} else
					adapter.add((PointOfInterest) poi);
			}
			poi_list_view.setAdapter(adapter);
			renewListGui();
			LayoutUtil.updateListViewHeight(poi_list_view);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage(getString(R.string.load_POIlist));
			dialog.show();
		}

		@Override
		protected ArrayList<IPointOfInterest> doInBackground(
				POIAdapter... params) {
			// adp = params[0];
			try {
				Map<Integer, IPointOfInterest> pois = POIDataSource
						.getInstance().getLastItems();
				return new ArrayList<IPointOfInterest>(pois.values());
			} catch (Throwable t) {
				t.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		adapter.getFilter().filter(s);

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	// -------------GPS---------------------------
	LocationManager locationManager;
	private static final long MIN_TIME = 400;
	private static final float MIN_DISTANCE = 1000;

	@Override
	public void onPause() {
		locationManager.removeUpdates(this);
		super.onPause();
	}

	@Override
	public void onResume() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME, MIN_DISTANCE, this);
		super.onResume();
	}

	public void startGPS() {
		if (locationManager == null) {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME, MIN_DISTANCE, this);
	}

	public void updateLocation(Location location) {
		if (location == null) {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			location = locationManager.getLastKnownLocation(locationManager
					.getBestProvider(new Criteria(), false));
		}

		if (location != null && location.getLatitude() != 0
				&& location.getLongitude() != 0) {
			Collection<IPointOfInterest> pois = POIDataSource.getInstance()
					.getLastItems().values();
			for (IPointOfInterest p : pois) {
				PointOfInterest p2 = (PointOfInterest) p;
				float distance = location
						.distanceTo(p2.getLocationAsLocation());
				p2.setDistance(distance);
			}
			renewListGui();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		updateLocation(location);

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	// update LIST
	private void renewListGui() {
		adapter.sort(new Comparator<PointOfInterest>() {
			@Override
			public int compare(PointOfInterest lhs, PointOfInterest rhs) {
				if (lhs.getDistance() != rhs.getDistance()) {
					if (rhs.getDistance() > lhs.getDistance()) {
						return -1;
					} else {
						return 1;
					}
				}
				return 0;
			}
		});
		adapter.notifyDataSetChanged();
	}

	

}
