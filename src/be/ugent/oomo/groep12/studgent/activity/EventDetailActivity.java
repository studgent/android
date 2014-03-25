package be.ugent.oomo.groep12.studgent.activity;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.R.layout;
import be.ugent.oomo.groep12.studgent.R.menu;
import be.ugent.oomo.groep12.studgent.common.CalendarEvent;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.utilities.LocationUtil;
import be.ugent.oomo.groep12.studgent.utilities.PlayServicesUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;


public class EventDetailActivity extends Activity {
	
	protected ICalendarEvent[] event_data;
	protected ICalendarEvent selected_event;
	protected TableLayout table_view;
	protected LayoutParams row_layout;
	protected LayoutParams table_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
                R.anim.animation_leave);
		setContentView(R.layout.activity_event_detail);
		Bundle b = getIntent().getExtras();
        Integer id = (Integer) b.get("id");
        //CalendarEvent item = (CalendarEvent) CalendarEventDataSource.getInstance().getDetails(id);
        // getting parcelable is faster than using datasource.
        selected_event = (CalendarEvent) getIntent().getParcelableExtra("calendarItem");

        // get the table from view
        table_view = (TableLayout) findViewById(R.id.calendar_detail_table);
        // set some default data for table and row layout
        row_layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        table_layout = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        loadEvent();
        
        // load map as last, load it asynchronously
        String noPlayServices = "Google Play Services not found, map will not be shown.";
        if (PlayServicesUtil.hasPlayServices(this, noPlayServices)){
        	new AsyncMapLoader().execute(selected_event.getLocation());
        }
	}
	
	@SuppressWarnings("deprecation")
	private void loadEvent(/*ICalendarEvent event*/){
		setTitle(selected_event.getName());
		TextView location = (TextView) findViewById(R.id.calendar_detail_location);
		location.setText( selected_event.getLocation().getStreet() 
						+ " " 
						+ selected_event.getLocation().getNumber() );

		
		TableRow tr = new TableRow(this);
		tr.setLayoutParams(row_layout);
		TextView info = new TextView(this);
		info.setSingleLine(false); 
		info.setEllipsize(null);
		info.setText(selected_event.getDescription());
		info.setLayoutParams(row_layout);
		tr.addView(info);
		table_view.addView(tr, table_layout);

	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_detail, menu);
		return true;
	}
	
	
	private class AsyncMapLoader extends AsyncTask<IPointOfInterest, Void, LatLng> {
	    private final ProgressDialog dialog = new ProgressDialog(EventDetailActivity.this);

	    @Override
	    protected void onPostExecute(LatLng result) {            
	        super.onPostExecute(result);
	        
	        if(result != null){ // if we found cošrdinates, update the PoI
	        	selected_event.getLocation().setLocation(result);
	        } else if (selected_event.getLocation().getLocation().latitude == 0.0 ||
	        		   selected_event.getLocation().getLocation().longitude == 0.0) {
	        	// no valid cošrdinates in the PoI
	        	String message = "Kon geen cošrdinaten vinden, map wordt niet getoond.";
	        	Toast.makeText(EventDetailActivity.this, message, Toast.LENGTH_LONG).show();
	        	dialog.dismiss();
	        	return;
	        }
	        
	        // create new row
	        TableRow tr = new TableRow(EventDetailActivity.this);
			tr.setLayoutParams(row_layout);
			
			RelativeLayout rl = new RelativeLayout(EventDetailActivity.this);
			rl.setLayoutParams(row_layout);
			tr.addView(rl);
			// inflate map fragment in row
	        View map_view = (View)getLayoutInflater().inflate(R.layout.fragment_map, rl);
	        
	        // add row to table_view
	        table_view.addView(tr, table_layout);
	
	        MapFragment map_fragment = (MapFragment) ((Activity)map_view.getContext()).getFragmentManager().findFragmentById(R.id.map);
	        GoogleMap map = map_fragment.getMap();
	        dialog.dismiss();
	        
	        //map.setMyLocationEnabled(true);
	        
	        // offset on location latitude (to show infowindow from marker)
	        LatLng maplocation = new LatLng(selected_event.getLocation().getLocation().latitude + 0.00024, 
	        								selected_event.getLocation().getLocation().longitude);
	        
	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(maplocation, 18));

	        Log.i("LatLng (w/o offset):", 
	        		"(" + selected_event.getLocation().getLocation().latitude + 
	        		" ; " + selected_event.getLocation().getLocation().longitude + ")"
	        	);
	        Log.i("LatLng (with offset):", 
	        		"(" + maplocation.latitude + 
	        		" ; " + maplocation.longitude + ")"
	        	);
	        String snippet = selected_event.getLocation().getStreet() != null ? 
	        		selected_event.getLocation().getStreet() + " " + selected_event.getLocation().getNumber()
	        		: "";
	        Marker marker = map.addMarker(new MarkerOptions()
	        								.title(selected_event.getName())
	        								.snippet(snippet)
	        								.position(selected_event.getLocation().getLocation())
	        								.icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN) )
	        							 );
	        marker.showInfoWindow();
	    }

	    @Override
	    protected void onPreExecute() {        
	        super.onPreExecute();
	        dialog.setMessage(getString(R.string.load_map));
	        dialog.show();            
	    }

	    @Override
	    protected LatLng doInBackground(IPointOfInterest... params) {
	    	PointOfInterest poi = (PointOfInterest) params[0];
			if(poi.getLocation().latitude == 0.0 || poi.getLocation().latitude == 0.0) {
		    	String locationname = "";
				try {
					locationname = poi.getStreet() + "+" + poi.getNumber() + "+Gent";
					LatLng location = LocationUtil.getLatLongFromAddress(locationname);
					if(location != null){
						return location;
					} 
					// try with different location info
					location = LocationUtil.getLatLongFromAddress(poi.getName());
					if(location != null){
						return location;
					} 
				} catch (CurlException e) {
					Log.e("Geocoder exception", locationname);
					e.printStackTrace();
				}
			}
	        return null;
	    }
	}

}
