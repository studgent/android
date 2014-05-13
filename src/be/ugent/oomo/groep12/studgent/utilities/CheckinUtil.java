package be.ugent.oomo.groep12.studgent.utilities;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;

public class CheckinUtil {

	public CheckinUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Checks if the user is allowed to check in on the POI.
	 * @param context : the activity 
	 * @param poi	: The Point Of Interest where the current user wants to check-in
	 * @param sharedPreferences: Local memory where the last check-in is saved
	 * @param currentLocation: The current location of the user
	 * @return a empty string if the check-in is allowed. Otherwise a textual respresentation of the error will be given
	 */
	public static String checkInAllowed(Context context, IPointOfInterest poi, SharedPreferences sharedPreferences, Location currentLocation) {
		//check if user is logged in
		if (LoginUtility.getInstance().isLoggedIn() == false)
			return "gebruiker is niet ingelogd";
		
        //checking if poi is in area
		double distance = poi.getDistance();
        if(distance > context.getResources().getInteger(R.integer.max_distance_to_checkin))
        	return "poi is te ver";
        
        //we know now that the poi is in your area
        //check if you previous checkin was in in the you area
        double lat = Double.parseDouble(sharedPreferences.getString("lat", "-1.0"));
    	double lon = Double.parseDouble(sharedPreferences.getString("lon", "-1.0"));
    	Location previousCheckin = new Location("dummy");
    	previousCheckin.setLatitude(lat);
    	previousCheckin.setLongitude(lon);
    	
    	if (currentLocation==null){
    		return "Geen GPS signaal!";
    	}
        distance = previousCheckin.distanceTo( currentLocation );
        if(distance > context.getResources().getInteger(R.integer.max_distance_to_checkin))
        	return ""; // checkinis allowed
        
        //we know now that the last checkin was in you earea
        //check if last login was in the checkin_time
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        Date lastCheckin = new Date(sharedPreferences.getLong("date", now.getTime()));
        long verschil = now.getTime()-lastCheckin.getTime(); //in milliseconds
        if(verschil!=0 && verschil < context.getResources().getInteger(R.integer.checkin_time))
        	return "je bent nog ingelogd in poi in uw buurt";
        
        //checkin is allowed
        return "";
	}

}
