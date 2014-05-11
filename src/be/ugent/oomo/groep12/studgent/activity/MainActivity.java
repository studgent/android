package be.ugent.oomo.groep12.studgent.activity;

import com.crashlytics.android.Crashlytics;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.utilities.LayoutUtil;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;


public class MainActivity extends Activity {

	public static final String PREFS_NAME = "preferences";

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
		
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String email = settings.getString("email", null);
		int id = settings.getInt("id", 0);
		String token = settings.getString("token", null);
		if ( email != null && !email.equals("") && 
			 id != 0 && 
			 token != null && !token.equals("") ) {
			LoginUtility.AutoLogin(email, id, token);
		}
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
	public boolean onPrepareOptionsMenu (Menu menu) {

		// change login text
		MenuItem login_item = menu.findItem(R.id.login);
		if ( LoginUtility.isLoggedIn() ) {
			login_item.setTitle( getString(R.string.login) + "(" + LoginUtility.getEmail() + ")");
		} else {
			login_item.setTitle( getString(R.string.login) + "(" + LoginUtility.getEmail() + ")");
		}
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
	
	public void openChallengeActivity(View view){
		Intent intent = new Intent(this, ChallengeActivity.class);
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
