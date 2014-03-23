package be.ugent.oomo.groep12.studgent.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.R.layout;
import be.ugent.oomo.groep12.studgent.R.menu;
import be.ugent.oomo.groep12.studgent.adapter.CalenderAdapter;
import be.ugent.oomo.groep12.studgent.common.CalendarEvent;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.CalendarEventDataSource;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.utilities.App;
import be.ugent.oomo.groep12.studgent.utilities.LocationUtil;
import be.ugent.oomo.groep12.studgent.utilities.PlayServicesUtil;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.app.Activity;
import android.os.Bundle;

public class EventDetailActivity extends Activity {
	
	protected ICalendarEvent[] event_data;
	protected ICalendarEvent selected_event;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
                R.anim.animation_leave);
		setContentView(R.layout.activity_event_detail);
		Bundle b = getIntent().getExtras();
        Integer id = (Integer) b.get("id");
        //CalendarEvent item = (CalendarEvent) CalendarEventDataSource.getInstance().getDetails(id);
        selected_event = (CalendarEvent) getIntent().getParcelableExtra("calendarItem");
        String noPlayServices = "Google Play Services not found, map will not be shown.";
        if (PlayServicesUtil.hasPlayServices(this, noPlayServices)){
        	//setMap(selected_event.getLocation());
        	new AsyncMapLoader().execute(selected_event.getLocation());
        }

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
	        
	        dialog.dismiss();
	        if(result != null){
	        	selected_event.getLocation().setLocation(result);
	        }
	        // Get a handle to the Map Fragment
	        ViewGroup vg = (ViewGroup) findViewById(R.id.event_detail_layout);
	        View map_view = (View)getLayoutInflater().inflate(R.layout.fragment_map, vg);
	
	        MapFragment map_fragment = (MapFragment) ((Activity)map_view.getContext()).getFragmentManager().findFragmentById(R.id.map);
	        
	        GoogleMap map = map_fragment.getMap();
	
	        //map.setMyLocationEnabled(true);
	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(selected_event.getLocation().getLocation(), 18));
	        Log.i("LatLng:", selected_event.getLocation().getLocation().latitude + "." + selected_event.getLocation().getLocation().longitude);
	        Marker marker = map.addMarker(new MarkerOptions().title(selected_event.getName())
	        												 .snippet(selected_event.getLocation().getStreet() + " " + selected_event.getLocation().getNumber())
	        												 .position(selected_event.getLocation().getLocation()));
	        marker.showInfoWindow();
	    }

	    @Override
	    protected void onPreExecute() {        
	        super.onPreExecute();
	        dialog.setMessage(getString(R.string.load_eventlist));
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
				} catch (CurlException e) {
					Log.e("Geocoder exception", locationname);
					e.printStackTrace();
				}
			}
	        return null;
	    }
	}

}
