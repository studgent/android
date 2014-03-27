package be.ugent.oomo.groep12.studgent.utilities;

import org.json.*;

import com.google.android.gms.maps.model.LatLng;

import be.ugent.oomo.groep12.studgent.exception.CurlException;


import android.util.Log;

public class LocationUtil {
	public static LatLng getLatLongFromAddress(String youraddress) throws CurlException {
	    String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
	                  youraddress + "&sensor=false";
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
	        return address;
	    } catch (JSONException e) {
	        e.printStackTrace();
	    }
	    throw new CurlException("No address found");
	}
	
	
}
