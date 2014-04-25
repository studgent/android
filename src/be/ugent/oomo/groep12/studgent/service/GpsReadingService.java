package be.ugent.oomo.groep12.studgent.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import be.ugent.oomo.groep12.studgent.utilities.App;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

public class GpsReadingService extends Service implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private LocationRequest mLocationRequest;
	private LocationClient mLocationClient;

	public GpsReadingService() {

	}

	/*
	 * Called before service onStart method is called.All Initialization part
	 * goes here
	 */
	@Override
	public void onCreate() {
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setInterval(App.UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest
				.setFastestInterval(App.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		mLocationClient = new LocationClient(getApplicationContext(), this,
				this);
		mLocationClient.connect();
	}

	/*
	 * You need to write the code to be executed on service start. Sometime due
	 * to memory congestion DVM kill the running service but it can be restarted
	 * when the memory is enough to run the service again.
	 */

	@Override
	public void onStart(Intent intent, int startId) {
		int start = Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/*
	 * Overriden method of the interface
	 * GooglePlayServicesClient.OnConnectionFailedListener . called when
	 * connection to the Google Play Service are not able to connect
	 */

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			//try {
				// Start an Activity that tries to resolve the error

				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			//} catch (IntentSender.SendIntentException e) {
				// Log the error
			//	e.printStackTrace();
			//}
		} else {
			// If no resolution is available, display a dialog to the user with
			// the error.
			Log.i("info", "No resolution is available");
		}
	}

	/*
	 * This is overriden method of interface
	 * GooglePlayServicesClient.ConnectionCallbacks which is called when
	 * locationClient is connecte to google service. You can receive GPS reading
	 * only when this method is called.So request for location updates from this
	 * method rather than onStart()
	 */
	@Override
	public void onConnected(Bundle arg0) {
		Log.i("info", "Location Client is Connected");
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
		Log.i("info", "Service Connect status :: " + isServicesConnected());
	}

	@Override
	public void onDisconnected() {
		Log.i("info", "Location Client is Disconnected");
	}

	/*
	 * Overrriden method of interface LocationListener called when location of
	 * gps device is changed. Location Object is received as a parameter. This
	 * method is called when location of GPS device is changed
	 */
	@Override
	public void onLocationChanged(Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();

		Log.i("info", "Latitude :: " + latitude);
		Log.i("info", "Longitude :: " + longitude);
	}

	/**
	 * Verify that Google Play services is available before making a request.
	 * 
	 * @return true if Google Play services is available, otherwise false
	 */
	private boolean isServicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(GpsReadingService.this);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Called when Sevice running in backgroung is stopped. Remove location
	 * upadate to stop receiving gps reading
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("info", "Service is destroyed");
		mLocationClient.removeLocationUpdates(this);
		super.onDestroy();
	}
}