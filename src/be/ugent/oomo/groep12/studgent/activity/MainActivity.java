package be.ugent.oomo.groep12.studgent.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.utilities.LayoutUtil;

import com.crashlytics.android.Crashlytics;

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
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
}
