package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;

import android.widget.AdapterView;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.R.layout;
import be.ugent.oomo.groep12.studgent.R.menu;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import be.ugent.oomo.groep12.studgent.adapter.CalenderAdapter;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.CalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.CalendarEventDataSource;
import be.ugent.oomo.groep12.studgent.utilities.MenuUtil;

public class EventsActivity extends Activity implements AdapterView.OnItemClickListener, TextWatcher {
	
	protected ICalendarEvent[] event_data;
	protected CalenderAdapter adapter;
	protected ListView event_list_view;
	private EditText inputSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_events);
		
		event_list_view = (ListView) findViewById(R.id.events_list);
		event_list_view.setOnItemClickListener(this);
		inputSearch = (EditText) findViewById(R.id.searchEvents_EditText);
		inputSearch.addTextChangedListener(this);
		
		// hide keyboard on start activity
	    this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		// create adapter with empty list and attach custom item view
        adapter = new CalenderAdapter(this, R.layout.calendar_list_item, new ArrayList<ICalendarEvent>());
        
        event_list_view.setAdapter(adapter);
        new AsyncListViewLoader().execute(adapter);
        
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
	public void onItemClick(AdapterView<?> parent, View item, int position, long rowID) {
		Intent intent = new Intent(this, EventDetailActivity.class);
		intent.putExtra("id", adapter.getItem(position).getId());
		intent.putExtra("calendarItem", adapter.getItem(position));
		startActivity(intent);
		
	}

	
	private class AsyncListViewLoader extends AsyncTask<CalenderAdapter, Void, ArrayList<ICalendarEvent>> {
	    private final ProgressDialog dialog = new ProgressDialog(EventsActivity.this);

	    @Override
	    protected void onPostExecute(ArrayList<ICalendarEvent> result) {            
	        super.onPostExecute(result);
	        
	        dialog.dismiss();
	        //adapter.setItemList(result);
	        Collections.sort(result);
	        adapter.clear();
	        for(ICalendarEvent event: result){
	        	adapter.add(event);
	        }
	        event_list_view.setAdapter(adapter);
	        adapter.notifyDataSetChanged();
	    }

	    @Override
	    protected void onPreExecute() {        
	        super.onPreExecute();
	        dialog.setMessage(getString(R.string.load_eventlist));
	        dialog.show();            
	    }

	    @Override
	    protected ArrayList<ICalendarEvent> doInBackground(CalenderAdapter... params) {
	    	//adp = params[0];
	        try {
	        	Map<Integer, ICalendarEvent> events = CalendarEventDataSource.getInstance().getLastItems();
	        	return new ArrayList<ICalendarEvent>(events.values());
	        }
	        catch(Throwable t) {
	            t.printStackTrace();
	        }
	        return null;
	    }
	}

	// Implements TextWatcher
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

		
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		adapter.getFilter().filter(s);
	}


	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

}
