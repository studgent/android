package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class POIListActivity extends Activity implements AdapterView.OnItemClickListener {
	
	protected PointOfInterest[] poi_data;
	protected POIAdapter adapter;
	protected ListView poi_list_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_poi_list);
		
		poi_list_view = (ListView) findViewById(R.id.poi_list);
		poi_list_view.setOnItemClickListener(this);
		
        /*View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
        event_list_view.addHeaderView(header);*/
		
		// create adapter with empty list and attach custom item view
        adapter = new POIAdapter(this, R.layout.poi_list_item, new ArrayList<PointOfInterest>());
        
        poi_list_view.setAdapter(adapter);
        new AsyncFriendListViewLoader().execute(adapter);
        
	}


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
