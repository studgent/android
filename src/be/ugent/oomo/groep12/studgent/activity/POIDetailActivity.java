package be.ugent.oomo.groep12.studgent.activity;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.common.QuizQuestion;
import be.ugent.oomo.groep12.studgent.data.POIDataSource;
import be.ugent.oomo.groep12.studgent.data.QuizQuestionsDataSource;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.exception.DataSourceException;
import be.ugent.oomo.groep12.studgent.utilities.LocationUtil;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;
import be.ugent.oomo.groep12.studgent.utilities.MenuUtil;
import be.ugent.oomo.groep12.studgent.utilities.PlayServicesUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class POIDetailActivity extends Activity implements
		OnInfoWindowClickListener, LocationListener {

	private PointOfInterest poi;
	protected SharedPreferences sharedPreferences;
	protected TableLayout table_view;
	protected LayoutParams row_layout;
	protected LayoutParams table_layout;
	protected Location currentLocation;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		poi = getIntent().getParcelableExtra("poi");
		setContentView(R.layout.activity_poi_detail);

		// get the table from view
		table_view = (TableLayout) findViewById(R.id.poi_detail_table);
		// set some default data for table and row layout
		row_layout = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT);
		table_layout = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT);

		// TextView txtTitle = (TextView)findViewById(R.id.poi_detail_location);
		TextView txtLocation = (TextView) findViewById(R.id.poi_detail_location);
		TextView txtDetail = (TextView) findViewById(R.id.poi_detail_summary);
		TextView txtUrl = (TextView) findViewById(R.id.poi_detail_uri);

		// txtTitle.setVisibility(View.INVISIBLE);
		txtDetail.setVisibility(View.INVISIBLE);
		txtLocation.setVisibility(View.INVISIBLE);
		txtUrl.setVisibility(View.INVISIBLE);

		if (poi.getName() != null) {
			// txtTitle.setText(poi.getName());
			// txtTitle.setVisibility(View.VISIBLE);
			setTitle(Html.fromHtml("" +  poi.getName()) );
		}

		if (poi.getDetails() != null) {
			txtDetail.setText(Html.fromHtml("" +  poi.getDetails() ) );
			txtDetail.setVisibility(View.VISIBLE);
		}

		if (poi.getStreet() != null || poi.getNumber() != null) {
			txtLocation.setText(poi.getStreet() + " " + poi.getNumber());
			txtLocation.setVisibility(View.VISIBLE);
		} else if (poi.getName() != null) {
			txtLocation.setText(Html.fromHtml("" +  poi.getName()));
			txtLocation.setVisibility(View.VISIBLE);
		}

		if (poi.getUrl() != null) {
			txtUrl.setText(Html.fromHtml("<a href='" + poi.getUrl() + "'>"
					+ poi.getUrl() + "</a>"));
			txtUrl.setVisibility(View.VISIBLE);
		}

		// load map as last, load it asynchronously
		String noPlayServices = "Google Play Services not found, map will not be shown.";
		if (PlayServicesUtil.hasPlayServices(this, noPlayServices)) {
			new AsyncMapLoader().execute(poi);
		}
		sharedPreferences = this.getSharedPreferences("LastCheckin", Context.MODE_PRIVATE);
	
		//start GPS
		startGPS();
		renewDistanceGui();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {

		return MenuUtil.PrepareMenu(this, menu);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    return MenuUtil.OptionsItemSelected(this, item);
	}
	

	public void navigateTo(View view) {
		String uri = "geo:" + poi.getLocation().latitude + ","
				+ poi.getLocation().longitude + "?q="
				+ poi.getStreet().replace(" ", "+") + "+" + poi.getNumber();

		try {
			startActivity(new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse(uri)));
		} catch (ActivityNotFoundException e) {
			String message = "Navigatie kan niet geopend worden.";
			Toast.makeText(POIDetailActivity.this, message, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		Log.i("clicked on marker", marker.getId());
		String uri = "geo:" + poi.getLocation().latitude + ","
				+ poi.getLocation().longitude + "?q="
				+ poi.getStreet().replace(" ", "+") + "+" + poi.getNumber();
		try {
			startActivity(new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse(uri)));
		} catch (ActivityNotFoundException e) {
			String message = "Maps-applicatie kan niet geopend worden.";
			Toast.makeText(POIDetailActivity.this, message, Toast.LENGTH_LONG)
					.show();
			Log.e("Could not open Maps", marker.getId());
		}
	}

	private class AsyncMapLoader extends
			AsyncTask<IPointOfInterest, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(
				POIDetailActivity.this);

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result != null) { // if we found cošrdinates, update the PoI
				poi.setStreet(result);
			}
			if (poi.getLocation().latitude == 0.0
					|| poi.getLocation().longitude == 0.0) {
				// no valid cošrdinates in the PoI
				String message = "Kon geen cošrdinaten vinden, map wordt niet getoond.";
				Toast.makeText(POIDetailActivity.this, message,
						Toast.LENGTH_LONG).show();
				dialog.dismiss();
				return;
			}

			if (poi.getStreet() != null && poi.getNumber() == null) {
				// if number was null, assume address was incomplete, re-enter
				// street info
				TextView txtLocation = (TextView) findViewById(R.id.poi_detail_location);
				txtLocation.setText(poi.getStreet());
				txtLocation.setVisibility(View.VISIBLE);
			}

			// create new row
			TableRow tr = new TableRow(POIDetailActivity.this);
			tr.setLayoutParams(row_layout);

			RelativeLayout rl = new RelativeLayout(POIDetailActivity.this);
			rl.setLayoutParams(row_layout);
			tr.addView(rl);
			// inflate map fragment in row
			try {
					View map_view = (View) getLayoutInflater().inflate(
					R.layout.fragment_map, rl);
			

				// add row to table_view
				table_view.addView(tr, table_layout);
	
				MapFragment map_fragment = (MapFragment) ((Activity) map_view
						.getContext()).getFragmentManager().findFragmentById(
						R.id.map);
				GoogleMap map = map_fragment.getMap();
				dialog.dismiss();
	
				// map.setMyLocationEnabled(true);
	
				// offset on location latitude (to show infowindow from marker)
				LatLng maplocation = new LatLng(
						poi.getLocation().latitude + 0.00024,
						poi.getLocation().longitude);
				map.setOnInfoWindowClickListener(POIDetailActivity.this);
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(maplocation, 18));
	
				Log.i("LatLng (w/o offset):", "(" + poi.getLocation().latitude
						+ " ; " + poi.getLocation().longitude + ")");
				Log.i("LatLng (with offset):", "(" + maplocation.latitude + " ; "
						+ maplocation.longitude + ")");
				String snippet = poi.getStreet() != null ? poi.getStreet() + " "
						+ poi.getNumber() : "";
				Marker marker = map.addMarker(new MarkerOptions()
						.title(Html.fromHtml("" +  poi.getName()).toString() )
						.snippet(Html.fromHtml("" +  snippet).toString())
						.position(poi.getLocation())
						.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
				marker.showInfoWindow();
			} catch (InflateException e){
				Log.e("Inflate error (emulator?)", "emulator");
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage(getString(R.string.load_map));
			dialog.show();
		}

		@Override
		protected String doInBackground(IPointOfInterest... params) {
			PointOfInterest poi = (PointOfInterest) params[0];
			if (poi.getStreet() == null || poi.getStreet().equals("")
					|| poi.getNumber() == null || poi.getNumber().equals("")) {
				String location = null;
				try {
					location = LocationUtil.getAddressFromLatLng(poi
							.getLocation());
					Log.i("poi street:", location + "");
					if (location != null) {
						return location;
					}
				} catch (CurlException e) {
					Log.e("Reverse geocoder exception", ""+location);
					e.printStackTrace(); 
				}
			}
			return null;
		}
		
	}
	public void check_In(View view) {
		checkIn();
	}

	private class AsyncCheckin extends AsyncTask<String, Void, Boolean> {

	    @Override
	    protected void onPostExecute(Boolean result) {            
	        super.onPostExecute(result);
	    }

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				String message = params[0];
				boolean result = POIDataSource.getInstance().checkin(poi, message);
	        	return result;
	        }
	        catch(Throwable t) {
	            t.printStackTrace();
				return false;
	        }
		}
	}
	
	protected void checkIn() {
		
		String checkinPossible = checkInAllowed();
		if(checkinPossible.equals("")){
			ceckInDialog();
		}
		else{
			if(checkinPossible.equals("Je bent nog ingelogd in poi in uw buurt")){
				checkinNotAllowedDiagram();
			}
			if(checkinPossible.equals("POI is te ver")){
				checkinPOIIsToFareDiagram();
			}
			if(checkinPossible.equals("Gebruiker is niet ingelogd")){
				Toast.makeText(this, "Log in om in te checken!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private String checkInAllowed() {
		//check if user is logged in
		if (LoginUtility.getInstance().isLoggedIn() == false)
			return "gebruiker is niet ingelogd";
		
        //checking if poi is in area
		double distance = poi.getDistance();
        if(distance>getResources().getInteger(R.integer.max_distance_to_checkin))
        	return "poi is te ver";
        
        //we know now that the poi is in your area
        //check if you previous checkin was in in the you area
        double lat = Double.parseDouble(sharedPreferences.getString("lat", "-1.0"));
    	double lon = Double.parseDouble(sharedPreferences.getString("lon", "-1.0"));
    	Location previousCheckin = new Location("dummy");
    	previousCheckin.setLatitude(lat);
    	previousCheckin.setLongitude(lon);
    	
        distance = previousCheckin.distanceTo( currentLocation );
        if(distance>getResources().getInteger(R.integer.max_distance_to_checkin))
        	return ""; // checkinis allowed
        
        //we know now that the last checkin was in you earea
        //check if last login was in the checkin_time
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        Date lastCheckin = new Date(sharedPreferences.getLong("date", now.getTime()));
        long verschil = now.getTime()-lastCheckin.getTime(); //in milliseconds
        if(verschil!=0 && verschil<getResources().getInteger(R.integer.checkin_time))
        	return "je bent nog ingelogd in poi in uw buurt";
        
        //checkin is allowed
        return "";
        
	}
	
	private void checkinNotAllowedDiagram() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this); //de this slaat op de ouder
	
		alertDialogBuilder.setTitle(getString(R.string.still_checked_in_title));
		alertDialogBuilder.setMessage("Je bent nog steeds ingechecked in "+sharedPreferences.getString("name", "fout")+
				".\n wacht nog effen, of zet een stapje, voordat je terug inchecked.");
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("ok",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			  });
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
		
	}
	
	private void ceckInDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this); //de this slaat op de ouder
	
		alertDialogBuilder.setTitle(getString(R.string.still_checked_in_title));
		
		final EditText input = new EditText(this); 
		input.setText("shout!");
		alertDialogBuilder.setView(input);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("CheckIn",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
					//alles van inchecken doen
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("lat", String.valueOf(poi.getLocation().latitude));
					editor.putString("lon", String.valueOf(poi.getLocation().longitude));
					Calendar c = Calendar.getInstance();
					Date now = c.getTime();
					editor.putLong("date", now.getTime());
					editor.putString("name", poi.getName());
					editor.commit();
					String[] params = new String[1];
					params[0]=input.getText().toString();
					new AsyncCheckin().execute(params);
					dialog.cancel();
				}
			  });
		alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
				
			}
		  });
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
		
	}
	
	private void checkinPOIIsToFareDiagram(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this); //de this slaat op de ouder
		
		alertDialogBuilder.setTitle(getString(R.string.still_checked_in_title));
		alertDialogBuilder.setMessage(poi.getName()+" is te ver om in te loggen");
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("ok",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			  });
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	

	
	//------------GPS---------------
	LocationManager locationManager;
	private static final long MIN_TIME = 400;
	private static final float MIN_DISTANCE = 1000;
	@Override
	public void onPause(){
		locationManager.removeUpdates(this);
		super.onPause();
	}
	
	@Override
	public void onResume(){
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME, MIN_DISTANCE, this);	
		super.onResume();
	}
	
	public void renewDistanceGui(){
		TextView distance = (TextView) findViewById(R.id.poi_detail_distance);
		if (poi.getDistance() > 1000){
			distance.setText( Math.round(poi.getDistance()/1000) + " km");
		}else{
			distance.setText( Math.round(poi.getDistance()) + " m");
		}
		
	}

	public void startGPS(){
		if (locationManager==null){
			locationManager = (LocationManager)
				getSystemService(Context.LOCATION_SERVICE);
		}
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		MIN_TIME, MIN_DISTANCE, this);			
	}	
	public void updateLocation(Location location){
		if (location == null){
			  locationManager = (LocationManager)
						getSystemService(Context.LOCATION_SERVICE);
			   location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria() , false));       
		}
		
		if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0 ){
			Collection<IPointOfInterest> pois =  POIDataSource.getInstance().getLastItems().values();
			for( IPointOfInterest  p : pois ){
					PointOfInterest p2 = (PointOfInterest) p;
					float distance = location.distanceTo(p2.getLocationAsLocation());
					p2.setDistance(distance);
					
					if (p2.getId() == poi.getId()){
						poi.setDistance(distance);
					}
			}
			currentLocation = location;
			renewDistanceGui();
		}					
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		updateLocation(location);
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}