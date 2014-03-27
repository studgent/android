package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.crashlytics.android.Crashlytics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.adapter.CalenderAdapter;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.data.CalendarEventDataSource;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.utilities.CurlUtil;
import be.ugent.oomo.groep12.studgent.utilities.LayoutUtil;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;


public class MainActivity extends Activity {
	

	protected Button button_neighbourhood;
	protected Button button_events;
	protected Button button_challenges;
	protected Button button_friends;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Enable crash logs
		 */
		Crashlytics.start(this);
		setContentView(R.layout.activity_main);
		setButtons();
	}
	
	
	protected void setButtons(){
		// get the buttons and set them to protected members
		button_neighbourhood = (Button) findViewById(R.id.button_neighbourhood);
		button_events = (Button) findViewById(R.id.button_events);
		button_challenges = (Button) findViewById(R.id.button_challenges);
		button_friends = (Button) findViewById(R.id.button_friends);

		// enable on touch effect
		LayoutUtil.buttonEffect(button_neighbourhood);
		LayoutUtil.buttonEffect(button_events);
		LayoutUtil.buttonEffect(button_challenges);
		LayoutUtil.buttonEffect(button_friends);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case R.id.login:
		    	openLoginActivity();
		        return true;
		    case R.id.info:
		    	Toast.makeText(MainActivity.this, LoginUtility.getInstance().getToken(), Toast.LENGTH_LONG).show();
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
	
	
	
	public void openEventsActivity(View view) {
		Intent intent = new Intent(this, EventsActivity.class);
		startActivity(intent);
	}
	
	public void openPOIMapviewActivity(View view) {
		Intent intent = new Intent(this, POIMapviewActivity.class);
		startActivity(intent);
	}
	
	public void openAugmentedViewActivity(View view) {
		Intent intent = new Intent(this, AugmentedViewActivity.class);
		startActivity(intent);
	}
	public void openFriendListActivity(View view){
		System.out.println("openFriendListActivity");
		Intent intent = new Intent(this, FriendListActivity.class);
		startActivity(intent);
	}
	
	public void openLoginActivity(){
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	
}
