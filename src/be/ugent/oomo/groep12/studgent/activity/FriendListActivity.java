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
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import be.ugent.oomo.groep12.studgent.adapter.CalenderAdapter;
import be.ugent.oomo.groep12.studgent.adapter.FriendAdapter;
import be.ugent.oomo.groep12.studgent.common.Friend;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.CalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.CalendarEventDataSource;
import be.ugent.oomo.groep12.studgent.data.FriendListDataSource;

public class FriendListActivity extends Activity implements AdapterView.OnItemClickListener {
	
	protected ICalendarEvent[] event_data;
	protected FriendAdapter adapter;
	protected ListView friend_list_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_friendlist);
		
		friend_list_view = (ListView) findViewById(R.id.friends_list);
		friend_list_view.setOnItemClickListener(this);
		
        /*View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
        event_list_view.addHeaderView(header);*/
		
		// create adapter with empty list and attach custom item view
        adapter = new FriendAdapter(this, R.layout.friend_list_item, new ArrayList<Friend>());
        
        friend_list_view.setAdapter(adapter);
        new AsyncFriendListViewLoader().execute(adapter);
        
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events, menu);
		return true;
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

	
	private class AsyncFriendListViewLoader extends AsyncTask<FriendAdapter, Void, ArrayList<Friend>> {
	    private final ProgressDialog dialog = new ProgressDialog(FriendListActivity.this);

	    @Override
	    protected void onPostExecute(ArrayList<Friend> result) {            
	        super.onPostExecute(result);
	        
	        dialog.dismiss();
	        //adapter.setItemList(result);
	        adapter.clear();
	        for(Friend friend: result){
	        	adapter.add(friend);
	        }
	        friend_list_view.setAdapter(adapter);
	        adapter.notifyDataSetChanged();
	    }

	    @Override
	    protected void onPreExecute() {        
	        super.onPreExecute();
	        dialog.setMessage(getString(R.string.load_eventlist));
	        dialog.show();            
	    }

		@Override
		protected ArrayList<Friend> doInBackground(FriendAdapter... params) {
			//adp = params[0];
	        try {
	        	Map<Integer, Friend> events = FriendListDataSource.getInstance().getLastItems();
	        	return new ArrayList<Friend>(events.values());
	        }
	        catch(Throwable t) {
	            t.printStackTrace();
	        }
	        return null;
		}
	}

}
