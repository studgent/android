package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import android.widget.AdapterView;
import java.util.Date;
import java.util.List;
import java.util.Map;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.R.layout;
import be.ugent.oomo.groep12.studgent.R.menu;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import be.ugent.oomo.groep12.studgent.adapter.CalenderAdapter;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.CalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.CalendarEventDataSource;

public class EventsActivity extends Activity implements AdapterView.OnItemClickListener {
	
	protected ICalendarEvent[] event_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
                R.anim.animation_leave);
		setContentView(R.layout.activity_events);
		
		ListView event_list_view = (ListView) findViewById(R.id.events_list);
		event_list_view.setOnItemClickListener(this);
		
        /*View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
        event_list_view.addHeaderView(header);*/
		
		// Retrieve the data from DataSource
		Map<Integer, ICalendarEvent> events = CalendarEventDataSource.getInstance().getLastItems();
		
		// Transform to array for adapter
		event_data = (ICalendarEvent[]) events.values().toArray(new ICalendarEvent[events.size()]);
		
		// add to list view using an adapter
        CalenderAdapter adapter = new CalenderAdapter(this, R.layout.calendar_list_item, event_data);
        
        event_list_view.setAdapter(adapter);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events, menu);
		return true;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View item, int position, long rowID) {
		Intent intent = new Intent(this, EventDetailActivity.class);
		intent.putExtra("id", event_data[position].getId());
		startActivity(intent);
		
	}

	
	

}
