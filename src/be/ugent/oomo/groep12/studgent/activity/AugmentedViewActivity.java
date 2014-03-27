package be.ugent.oomo.groep12.studgent.activity;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.view.OverlayView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

public class AugmentedViewActivity extends Activity implements
SurfaceHolder.Callback,
SensorEventListener {
	// Camera
	private Camera camera;
	private boolean inPreview;
	private SurfaceHolder holder;
	private boolean cameraConfigured;

	// Compass
	private SensorManager sensorManager;
	private Sensor magnetometer;
	private Sensor accelerometer;

	// Layout elements
	private SurfaceView surfaceView;
	private TextView textView;
	private OverlayView overlayView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set landscape orientation
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_augmented);

		// Layout binding
		surfaceView = (SurfaceView) findViewById(R.id.arview);
		textView = (TextView) findViewById(R.id.artext);
		overlayView = (OverlayView) findViewById(R.id.aroverlay);

		// Compass
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		// Video
		holder = surfaceView.getHolder();
		holder.addCallback(this);
	}

	@Override
	public void onResume() {
		super.onResume();

		// Camera preview
		camera = Camera.open();
		startPreview();

		// Compass
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
		
		// Overlay
		overlayView.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onPause() {
		// Stop camera preview and release camera
		if (inPreview) {
			camera.stopPreview();
		}
		camera.release();
		camera = null;
		inPreview = false;

		// Compass
		sensorManager.unregisterListener(this);

		super.onPause();
	}

	private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}

		return (result);
	}

	private void initPreview(int width, int height) {
		if (camera != null && holder.getSurface() != null) {
			try {
				camera.setPreviewDisplay(holder);
			} catch (Throwable t) {
				Log.e("PreviewDemo-surfaceCallback",
						"Exception in setPreviewDisplay()", t);
			}

			if (!cameraConfigured) {
				Camera.Parameters parameters = camera.getParameters();
				Camera.Size size = getBestPreviewSize(width, height, parameters);

				if (size != null) {
					parameters.setPreviewSize(size.width, size.height);
					camera.setParameters(parameters);
					cameraConfigured = true;
				}
			}
		}
	}

	private void startPreview() {
		if (cameraConfigured && camera != null) {
			camera.startPreview();
			inPreview = true;
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.augmented, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.switch_to_map:
			Intent intent = new Intent(this, POIMapviewActivity.class);
			startActivity(intent);
			return true;
		}
		return false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		initPreview(width, height);
		startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
	
	float[] mGravity;
	float[] mGeomagnetic;

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			mGravity = event.values;
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			mGeomagnetic = event.values;
		if (mGravity != null && mGeomagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
			if (success) {
				
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				
				float azimuthInRadians = orientation[0];
				float azimuthInDegrees = (float) Math.toDegrees(azimuthInRadians);
				// Compensate for landscape orientation
				azimuthInDegrees += 90f;
				
				if (azimuthInDegrees < 0.0f) {
				    azimuthInDegrees += 360.0f;
				}
				
				int azimuth = Math.round(azimuthInDegrees);
				
				textView.setText("Az: " + Float.toString(azimuth) + " degrees");
				
				overlayView.updateOverlay(azimuth);
			}
		}
				
	}
}
