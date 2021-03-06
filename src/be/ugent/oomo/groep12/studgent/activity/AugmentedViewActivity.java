package be.ugent.oomo.groep12.studgent.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.utilities.LocationUtil;
import be.ugent.oomo.groep12.studgent.view.OverlayView;

public class AugmentedViewActivity extends Activity implements
		SurfaceHolder.Callback, SensorEventListener {
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
	private OverlayView overlayView;

	/*
	 * Sets up the sensors, camera and gps for use in Augmented Reality
	 * 
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (!LocationUtil.isGPSEnabled(this)){
			Toast.makeText(this, "GPS is uitgeschakeld! Sommige functies zijn onbruikbaar.", Toast.LENGTH_SHORT).show();
			this.finish();
		}

		// Set landscape orientation if not already
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		setContentView(R.layout.activity_augmented);

		// Layout binding
		surfaceView = (SurfaceView) findViewById(R.id.arview);
		overlayView = (OverlayView) findViewById(R.id.aroverlay);

		// Compass
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		// GPS
		LocationUtil.getInstance(this).registerLocationUpdatedListener(overlayView);

		// Video
		holder = surfaceView.getHolder();
		holder.addCallback(this);
	}

	/*
	 * Resumes sensors, camera and gps
	 * 
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();

		// Camera preview
		camera = Camera.open();
		startPreview();

		// Compass
		sensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(this, magnetometer,
				SensorManager.SENSOR_DELAY_GAME);

		// GPS
		LocationUtil.getInstance(this).onResume();
	}
	
	/*
	 * Pauses camera, sensors and gps
	 * 
	 * (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
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

		// GPS
		LocationUtil.getInstance(this).onPause();

		super.onPause();
	}

	/*
	 * Makes sure the camera is matching the right screen width and height
	 */
	private Camera.Size getBestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
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

	/*
	 * Initializes the camera live preview
	 */
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

	/*
	 * Starts the live camera preview
	 */
	private void startPreview() {
		if (cameraConfigured && camera != null) {
			camera.startPreview();
			inPreview = true;
		}
	}

	/*
	 * Sets up and starts the camera when the SurfaceView is ready
	 * 
	 * (non-Javadoc)
	 * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
	 */
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

	/*
	 * Processes (low-pass filter) the sensor data and calculates the compass heading.
	 * The new compass azimuth gets passed to the OverlayView to update the POIs drawn.
	 * 
	 * (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {

		// Smoothing the sensor data to lessen jitter
		float gravFilter = 0.33334f;
		float magFilter = 0.4f;

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			if (mGravity != null) {
				mGravity[0] = (mGravity[0] * 2 + event.values[0]) * gravFilter;
				mGravity[1] = (mGravity[1] * 2 + event.values[1]) * gravFilter;
				mGravity[2] = (mGravity[2] * 2 + event.values[2]) * gravFilter;
			} else {
				mGravity = event.values;
			}
		}
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			if (mGeomagnetic != null) {
				mGeomagnetic[0] = (mGeomagnetic[0] * 1.5f + event.values[0])
						* magFilter;
				mGeomagnetic[1] = (mGeomagnetic[1] * 1.5f + event.values[1])
						* magFilter;
				mGeomagnetic[2] = (mGeomagnetic[2] * 1.5f + event.values[2])
						* magFilter;
			} else {
				mGeomagnetic = event.values;
			}
		}

		// Process the data
		if (mGravity != null && mGeomagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
					mGeomagnetic);
			if (success) {

				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);

				float azimuthInRadians = orientation[0];
				float azimuthInDegrees = (float) Math
						.toDegrees(azimuthInRadians);
				// Compensate for landscape orientation
				azimuthInDegrees += 90f;

				// Compensate for negative compass values
				if (azimuthInDegrees < 0.0f) {
					azimuthInDegrees += 360.0f;
				}

				// Convert from float to int
				int azimuth = Math.round(azimuthInDegrees);

				// Call method to update the AR Overlay
				overlayView.updateOverlay(azimuth);
			}
		}

	}
}
