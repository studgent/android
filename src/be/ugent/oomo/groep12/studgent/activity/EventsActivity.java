package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.R.layout;
import be.ugent.oomo.groep12.studgent.R.menu;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import be.ugent.oomo.groep12.studgent.adapter.CalenderAdapter;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.CalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.CalendarEventDataSource;

public class EventsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
                R.anim.animation_leave);
		setContentView(R.layout.activity_events);
		
		ListView event_list_view = (ListView) findViewById(R.id.events_list);
		
		List<ICalendarEvent> events = CalendarEventDataSource.getInstance().getLastItems();
		ICalendarEvent[] event_data = (ICalendarEvent[]) events.toArray(new ICalendarEvent[events.size()]);

		// add to list view using an adapter
		/*ArrayAdapter<ICalendarEvent> adapter = new ArrayAdapter<ICalendarEvent>(this,
				android.R.layout.simple_list_item_1, events);
		event_list_view.setAdapter(adapter);*/
		
		
	       
        CalenderAdapter adapter = new CalenderAdapter(this, 
                R.layout.calendar_list_item, event_data);
        
         
        /*View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
        listView1.addHeaderView(header);*/
        
        event_list_view.setAdapter(adapter);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events, menu);
		return true;
	}
	
	

}
