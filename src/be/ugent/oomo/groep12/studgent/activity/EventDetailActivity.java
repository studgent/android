package be.ugent.oomo.groep12.studgent.activity;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.R.layout;
import be.ugent.oomo.groep12.studgent.R.menu;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
		setContentView(R.layout.activity_event_detail);
		Bundle b = getIntent().getExtras();
        Integer id = (Integer) b.get("id");

        // Get a handle to the Map Fragment
        ViewGroup vg = (ViewGroup) findViewById(R.id.event_detail_layout);
        View map_view = (View)getLayoutInflater().inflate(R.layout.fragment_map, vg);

        MapFragment map_fragment = (MapFragment) ((Activity)map_view.getContext()).getFragmentManager().findFragmentById(R.id.map);
        
        GoogleMap map = map_fragment.getMap();
        LatLng ghent = new LatLng(51.0500, 3.7333);

        //map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ghent, 13));

        map.addMarker(new MarkerOptions()
                .title("Ghent (" + id +")")
                .snippet("Gentse feesten.")
                .position(ghent));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_detail, menu);
		return true;
	}

}
