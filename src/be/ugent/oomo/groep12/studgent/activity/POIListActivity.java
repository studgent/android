package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.adapter.FriendAdapter;
import be.ugent.oomo.groep12.studgent.adapter.POIAdapter;
import be.ugent.oomo.groep12.studgent.common.Friend;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.FriendListDataSource;
import be.ugent.oomo.groep12.studgent.data.POIDataSource;

public class POIListActivity extends Activity implements 
	AdapterView.OnItemClickListener,
	ActionBar.OnNavigationListener{
	

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	protected PointOfInterest[] poi_data;
	protected POIAdapter adapter;
	protected ListView poi_list_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_poi_list);
		setNavigation();
		
		poi_list_view = (ListView) findViewById(R.id.poi_list);
		poi_list_view.setOnItemClickListener(this);
		
        /*View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
        event_list_view.addHeaderView(header);*/
		
		// create adapter with empty list and attach custom item view
        adapter = new POIAdapter(this, R.layout.poi_list_item, new ArrayList<PointOfInterest>());
        
        poi_list_view.setAdapter(adapter);
        new AsyncFriendListViewLoader().execute(adapter);
        
	}
	

	// START switchbar
	
	protected void setNavigation() {
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	
		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								"Map",
								"Lijst",
								"Augmented", }), this);
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


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case R.id.poi_open_augmented:
		    	openAugmentedViewActivity();
		        return true;
		    case R.id.poi_open_mapview:
		    	openMapviewActivity();
		    	return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
	
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
	public void onItemClick(AdapterView<?> parent, View item, int position, long rowID) {
		System.out.println("geklikt op vriend");
		/*
		Intent intent = new Intent(this, EventDetailActivity.class);
		intent.putExtra("id", adapter.getItem(position).getId());
		intent.putExtra("calendarItem", adapter.getItem(position));
		startActivity(intent);
		*/
	}

	
	private class AsyncFriendListViewLoader extends AsyncTask<POIAdapter, Void, ArrayList<IPointOfInterest>> {
	    private final ProgressDialog dialog = new ProgressDialog(POIListActivity.this);

	    @Override
	    protected void onPostExecute(ArrayList<IPointOfInterest> result) {            
	        super.onPostExecute(result);
	        
	        dialog.dismiss();
	        //adapter.setItemList(result);
	        adapter.clear();
	        for(IPointOfInterest poi: result){
	        	adapter.add((PointOfInterest) poi);
	        }
	        poi_list_view.setAdapter(adapter);
	        adapter.notifyDataSetChanged();
	    }

	    @Override
	    protected void onPreExecute() {        
	        super.onPreExecute();
	        dialog.setMessage(getString(R.string.load_eventlist));
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
	

}
