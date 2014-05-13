package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.google.android.gms.maps.model.LatLng;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.adapter.POIAdapter;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.POIDataSource;
import be.ugent.oomo.groep12.studgent.utilities.LayoutUtil;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;
import be.ugent.oomo.groep12.studgent.utilities.MenuUtil;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CheckInActivity extends Activity implements AdapterView.OnItemClickListener {

	

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	protected PointOfInterest[] poi_data;
	protected POIAdapter adapter;
	protected ListView poi_list_view;
	protected SharedPreferences sharedPreferences;
	protected boolean comesFromCheckInDetailActivity = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("CheckInActivity: onCreate");
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_checkin);
		
		poi_list_view = (ListView) findViewById(R.id.poi_list);
		poi_list_view.setOnItemClickListener(this);
		
        /*View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
        event_list_view.addHeaderView(header);*/
		
		// create adapter with empty list and attach custom item view
        adapter = new POIAdapter(this, R.layout.poi_list_item, new ArrayList<PointOfInterest>());
        
        poi_list_view.setAdapter(adapter);

		//sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //sharedPreferences = this.getSharedPreferences()(Context.MODE_PRIVATE);
        
        if (LoginUtility.getInstance().isLoggedIn() == false) {
			Toast.makeText(this, "Log in om in te checken!", Toast.LENGTH_SHORT).show();
			onBackPressed();
		}else{
			new AsyncFriendListViewLoader().execute(adapter);
	        
		}
        
        
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
	
	@Override
	protected void onResume(){
		super.onResume();
        	
	}

	
	@Override
	public void onItemClick(AdapterView<?> parent, View item, int position, long rowID) {
		PointOfInterest poi = this.adapter.getItem(position);
		Intent intent = new Intent(this, CheckInDetailsActivity.class);
		intent.putExtra("poi", poi);
		comesFromCheckInDetailActivity = true;
		startActivity(intent);
	}

	
	private class AsyncFriendListViewLoader extends AsyncTask<POIAdapter, Void, ArrayList<IPointOfInterest>> {
	    private final ProgressDialog dialog = new ProgressDialog(CheckInActivity.this);

	    @Override
	    protected void onPostExecute(ArrayList<IPointOfInterest> result) {            
	        super.onPostExecute(result);
	        
	        dialog.dismiss();
	        //adapter.setItemList(result);
	        adapter.clear();
	        for(IPointOfInterest poi: result){
	        	LatLng currentPosotion = new LatLng(51.032052, 3.701968); //<-------------------- nog wijzigen
	            double distance = distFrom(poi.getLocation().latitude, poi.getLocation().longitude, currentPosotion.latitude, currentPosotion.longitude);
	            if(distance<=getResources().getInteger(R.integer.max_distance_to_checkin))// we zoeken alles in straal van 50 meter
	            	adapter.add((PointOfInterest) poi);
	        }
	        poi_list_view.setAdapter(adapter);
	        adapter.notifyDataSetChanged();
			LayoutUtil.updateListViewHeight(poi_list_view);
	    }

	    @Override
	    protected void onPreExecute() {        
	        super.onPreExecute();
	        dialog.setMessage(getString(R.string.load_POIlist));
	        dialog.show();            
	    }

		@Override
		protected ArrayList<IPointOfInterest> doInBackground(POIAdapter... params) {
			//adp = params[0];
	        try {
	        	Map<Integer, IPointOfInterest> events = POIDataSource.getInstance().getLastItems();
	        	return new ArrayList<IPointOfInterest>(events.values());
	        }
	        catch(Throwable t) {
	            t.printStackTrace();
	        }
	        return null;
		}
	}
	
	private double distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 3958.75;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    int meterConversion = 1609;

	    return (double) (dist * meterConversion);
	}
		
	private void closeActivity(){
		this.finish();
	}
	
}
