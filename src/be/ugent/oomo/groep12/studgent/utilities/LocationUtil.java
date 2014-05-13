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

public class LocationUtil implements LocationListener  {
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
	public static Location getLocationFromLatLng(LatLng l) {
		Location loc = new Location("StudGent");
		loc.setLatitude(l.latitude);
		loc.setLongitude(l.longitude);
		loc.setTime(new Date().getTime());
		return loc;
	}
	public static LatLng getLatLngFromLocation(Location l) {
		LatLng newLatLng = new LatLng(l.getLatitude(), l.getLongitude());
		return newLatLng;
	}
	
	
	//GPS functions
	private static LocationManager locationManager;
	private static final long MIN_TIME = 400;
	private static final float MIN_DISTANCE = 1000;
	private static Context context;
	private static LocationUtil instance;
	private static iDistanceUpdatedListener listenerDistance;
	private static iLocationChangedListener listenerLocation;
	
	protected LocationUtil() {
		if (locationManager == null) {
			locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		}
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME, MIN_DISTANCE, this);
	} 
	
	
	public static LocationUtil getInstance(Context con){
		context = con;
		if (instance==null){
			instance = new LocationUtil();
		}
		return instance;
	}
	
	public void registerDistanceUpdatedListener(iDistanceUpdatedListener listener){
		 this.listenerDistance = listener;
		 new AsyncTask<Void, Void, Void>(){
		        @Override
		        protected Void doInBackground(Void... params) {
		        	try {
						QuizQuestionsDataSource.getInstance().getLastItems();
					} catch (DataSourceException e) {
						// TODO Auto-generated catch block
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
	
	public void registerLocationUpdatedListener(iLocationChangedListener listener){
		this.listenerLocation = listener;
		Location loc = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria() , false));
		if (loc != null && loc.getLatitude() != 0 && loc.getLongitude() != 0 )
			listener.locationIsChanged( loc	);
	}
	
	
	
	public void  onPause(){
		locationManager.removeUpdates(this);
	}
	
	public void onResume(){
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME, MIN_DISTANCE, this);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
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
					// TODO Auto-generated catch block
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
