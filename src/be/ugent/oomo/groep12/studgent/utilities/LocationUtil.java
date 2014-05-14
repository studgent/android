package be.ugent.oomo.groep12.studgent.utilities;

import java.util.Collection;
import java.util.Date;

import org.json.*;

import com.google.android.gms.maps.model.LatLng;

import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.common.QuizQuestion;
import be.ugent.oomo.groep12.studgent.data.POIDataSource;
import be.ugent.oomo.groep12.studgent.data.QuizQuestionsDataSource;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.exception.DataSourceException;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
/**
 * Utility class that provides location-related info
 * Contains static functions to retrieve addresses from geocode and vice versa
 * Also implements LocationListener as intermediary to hold the last fetched location
 * 
 */
public class LocationUtil implements LocationListener  {
	
	/**
	 * Function to resolve a streetadres to his lat/lon coordinates
	 * @param youraddress : String: the address
	 * @return	: LatLng : Location of the address
	 * @throws CurlException : 	Resolve server not found
	 */
	public static LatLng getLatLongFromAddress(String youraddress) throws CurlException {
	    String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
	                  youraddress.replace(' ', '+') + "&sensor=false";
	    String response = CurlUtil.getRaw(uri, false);

	    JSONObject jsonObject = new JSONObject();
	    try {
	        jsonObject = new JSONObject(response);

	        Double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lng");

	        Double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lat");

	        Log.d("latitude", "" + lat);
	        Log.d("longitude", "" + lng);
	        return new LatLng(lat,lng);
	    } catch (JSONException e) {
	        e.printStackTrace();
	    }
	    throw new CurlException("No location found");
	}
	
	/**
	 * Find the corresponding address from a LatLng location
	 * @param location: LatLng: the location
	 * @return : String: The address that responds to the location
	 * @throws CurlException	Server not found 
	 */
	public static String getAddressFromLatLng(LatLng location) throws CurlException {
	    String uri = "http://maps.google.com/maps/api/geocode/json?latlng=" +
	                  location.latitude + ',' + location.longitude + "&sensor=false";
	    String response = CurlUtil.getRaw(uri, false);

	    JSONObject jsonObject = new JSONObject();
	    try {
	        jsonObject = new JSONObject(response);

	        jsonObject = ((JSONArray)jsonObject.get("results")).getJSONObject(0);
	        String address = JSONUtil.optString(jsonObject, "formatted_address");
	        Log.i("LocationUtil", address);
	        return address;
	    } catch (JSONException e) {
	        e.printStackTrace();
	    }
	    throw new CurlException("No address found");
	}
	
	/**
	 * Converts a LatLng to a location
	 * @param l: LatLng object
	 * @return : corresponding location object
	 */
	public static Location getLocationFromLatLng(LatLng l) {
		Location loc = new Location("StudGent");
		loc.setLatitude(l.latitude);
		loc.setLongitude(l.longitude);
		loc.setTime(new Date().getTime());
		return loc;
	}
	
	/**
	 * Converts a Location  to a latLng
	 * @param l: Location object
	 * @return : corresponding LatLng object
	 */
	public static LatLng getLatLngFromLocation(Location l) {
		LatLng newLatLng = new LatLng(l.getLatitude(), l.getLongitude());
		return newLatLng;
	}
	
	
	public static boolean isGPSEnabled(Context context){
		LocationManager service = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = service
		  .isProviderEnabled(LocationManager.GPS_PROVIDER);
		return enabled;
	}
	
	//---------------------------GPS functions----------------------------------------------------
	//Parameters and global field for the GPS utility
	private static LocationManager locationManager;
	private static final long MIN_TIME = 400;
	private static final float MIN_DISTANCE = 1000;
	private static Context context;
	private static LocationUtil instance;
	private static IDistanceUpdatedListener listenerDistance;
	private static ILocationChangedListener listenerLocation;
	
	/**
	 * Singleton design pattern constructor. 
	 * Enables the locationManager to send updates to this class
	 */
	protected LocationUtil() {
		if (locationManager == null) {
			locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		}
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME, MIN_DISTANCE, this);
	} 
	
	/**
	 * Singleton getInstance, returns the singleton object of this type
	 * @param con: the context of the running activity. (is always equal to this)
	 * @return	: 	The LocationUtil object
	 */
	public static LocationUtil getInstance(Context con){
		context = con;
		if (instance==null){
			instance = new LocationUtil();
		}
		return instance;
	}
	
	/**
	 * Register the callback for the DistanceUpdate event
	 * This function will also execute a callback with the last known GPS data
	 * @param listener	: The object which will be called when a distanceUpdate event occure
	 */
	public void registerDistanceUpdatedListener(IDistanceUpdatedListener listener){
		 LocationUtil.listenerDistance = listener;
		 new AsyncTask<Void, Void, Void>(){
		        @Override
		        protected Void doInBackground(Void... params) {
		        	try {
						QuizQuestionsDataSource.getInstance().getLastItems();
					} catch (DataSourceException e) {
						e.printStackTrace();
					}
		        	POIDataSource.getInstance().getLastItems().values();
					return null;
		        }
		        
		        @Override
		        protected void onPostExecute(Void param) {
		        	onLocationChanged(locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria() , false)));
			    }
		        
		    }.execute();
			
	}
	
	/**
	 * Register the callback for the LocationUpdated event
	 * This function will also execute a callback with the last known GPS data
	 * @param listener	: The object which will be called when a LocationUpdated event occure
	 */
	public void registerLocationUpdatedListener(ILocationChangedListener listener){
		LocationUtil.listenerLocation = listener;
		Location loc = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria() , false));
		if (loc != null && loc.getLatitude() != 0 && loc.getLongitude() != 0 )
			listener.locationIsChanged( loc	);
	}
	
	/**
	 * This function must be called when the activity goes to the pause state
	 */
	public void  onPause(){
		locationManager.removeUpdates(this);
	}
	
	/**
	 * This function reactivates the GPS after an activity resume
	 */
	public void onResume(){
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME, MIN_DISTANCE, this);
	}
	
	
	/**
	 * This function is called by the LocationManager when new GPS data is available.
	 * It will update the distances to every POI and QuizQuestion from the datasource 
	 * and it wil also call the responding callbacks when a listener is given
	 * @param location: Location: The new location
	 * return : void
	 */
	@Override
	public void onLocationChanged(Location location) {
		if (location == null) {
			locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			location = locationManager.getLastKnownLocation(locationManager
					.getBestProvider(new Criteria(), false));
		}

		if (location != null && location.getLatitude() != 0
				&& location.getLongitude() != 0) {
			if (listenerDistance != null){
				Collection<IPointOfInterest> pois = POIDataSource.getInstance()
						.getLastItems().values();
				for (IPointOfInterest p : pois) {
					PointOfInterest p2 = (PointOfInterest) p;
					float distance = location
							.distanceTo(p2.getLocationAsLocation());
					p2.setDistance(distance);
				}
				try {
					Collection<QuizQuestion> questions =  QuizQuestionsDataSource.getInstance().getLastItems().values();
					for( QuizQuestion q : questions ){
							float distance = location.distanceTo(q.getLocationAsLocation()) / 1000;
							q.setDistance(distance);
					}
				} catch (DataSourceException e) {
					e.printStackTrace();
				}			
				listenerDistance.distanceIsUpdated();
			}
			if (listenerLocation != null){
				if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0 )
					listenerLocation.locationIsChanged(location);
			}
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}
	
}
