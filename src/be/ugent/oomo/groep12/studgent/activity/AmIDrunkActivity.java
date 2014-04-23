package be.ugent.oomo.groep12.studgent.activity;

import be.ugent.oomo.groep12.studgent.R;

import be.ugent.oomo.groep12.studgent.R.anim;
import be.ugent.oomo.groep12.studgent.R.id;
import be.ugent.oomo.groep12.studgent.R.layout;
import be.ugent.oomo.groep12.studgent.R.menu;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Build;
import android.os.CountDownTimer;

public class AmIDrunkActivity extends Activity implements SensorEventListener {
	
	
	/**
	 * Keep track of X, Y and Z co�rdinates and keep a total for Z.
	 */
	private float mLastX, mLastY, mLastZ, mTotalZ;
	private long mTime;
	
	/**
	 * To check if initialized, if not, set initial values
	 */
	private boolean mInitialized; 
	private boolean mCompleted;
	
	private static final float NS2S = 1.0f / 1000000000.0f;
	
	/**
	 * Sensor and sensormanager
	 */
	private SensorManager mSensorManager; 
	private Sensor mAccelerometer; 
	
	/**
	 * NOISE: to eliminate sensor noise (false positives)
	 */
	private final float NOISE = (float) 0.35;
	
	/**
	 * DEVIATION: max allowed deviation
	 * if exceeds: user can be considered drunk
	 */
	private final float DEVIATION = (float) 1.9;
	
	/**
	 * Distance to complete in the Z direction
	 */
	private final float DISTANCE = (float) 300.0;
	
	
	/**
	 * Amount of times user deviated too much
	 */
	private int mDeviated_count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_am_idrunk);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		mInitialized = false;
		mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	//  ignored
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.am_idrunk, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_am_idrunk,
					container, false);
			return rootView;
		}
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		final float dT = (event.timestamp - mTime) * NS2S;
		TextView tvX= (TextView)findViewById(R.id.x_axis);
		TextView tvY= (TextView)findViewById(R.id.y_axis);
		TextView tvZ= (TextView)findViewById(R.id.z_axis);
		TextView tvZtotal= (TextView)findViewById(R.id.z_total);
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mTotalZ = 0;
			mTime = event.timestamp;
			tvX.setText("0.0");
			tvY.setText("0.0");
			tvZ.setText("0.0");
			tvZtotal.setText("0.0");
			mInitialized = true;
		} else if (!mCompleted) {
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			
			if (deltaX < DEVIATION) deltaX = (float)0.0;
			if (deltaY < DEVIATION) deltaY = (float)0.0;
			if (deltaZ < NOISE) deltaZ = (float)0.0;
			//mTotalZ += mLastZ - z;
			//if ( deltaX > NOISE && deltaY > NOISE && deltaZ > NOISE ) {
			mTotalZ += deltaZ * dT * dT; // distance
			//}
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			if(deltaX > 0 || deltaY > 0){
				mDeviated_count++;
			}
			tvX.setText(Float.toString(deltaX));
			tvY.setText(Float.toString(deltaY));
			tvZ.setText(Float.toString(deltaZ));
			tvZtotal.setText(Float.toString(mTotalZ));
			if (mTotalZ >= DISTANCE) {
				mCompleted = true;
			}
		}
		if (mCompleted){
			hideSensorTable();
			boolean drunk = mDeviated_count > 0 ? true : false;
			onDrunkResult(drunk);
		}
	}
	
	protected void hideSensorTable() {
		TableLayout sensortable = (TableLayout)findViewById(R.id.am_i_drunk_sensor_table);
		sensortable.removeAllViews();
		sensortable.setVisibility(View.GONE);
		
	}
	
	protected void onDrunkResult(boolean drunk) {
		ImageView img = (ImageView) findViewById(R.id.am_i_drunk_result_image);
		TextView text = (TextView)findViewById(R.id.am_i_drunk_result_text);
		
		// set image to drunk or sober
		img.setImageResource(drunk ? R.drawable.drunk : R.drawable.sober);
		text.setText(drunk ? "You are drunk!\n New checkins disabled for 24h." : "Still sober! \n Party on and keep checking in!");
		
		TableLayout resulttable = (TableLayout)findViewById(R.id.am_i_drunk_result_table);
		resulttable.setVisibility(View.VISIBLE);
	}

}
