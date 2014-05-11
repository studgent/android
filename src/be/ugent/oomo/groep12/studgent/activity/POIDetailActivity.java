package be.ugent.oomo.groep12.studgent.activity;

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
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.utilities.LocationUtil;
import be.ugent.oomo.groep12.studgent.utilities.PlayServicesUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class POIDetailActivity   extends Activity implements OnInfoWindowClickListener {

	private PointOfInterest poi;
	protected TableLayout table_view;
	protected LayoutParams row_layout;
	protected LayoutParams table_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		poi = getIntent().getParcelableExtra("poi");
		setContentView(R.layout.activity_poi_detail);


        // get the table from view
        table_view = (TableLayout) findViewById(R.id.poi_detail_table);
        // set some default data for table and row layout
        row_layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        table_layout = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        
		//TextView txtTitle =   (TextView)findViewById(R.id.poi_detail_location);
		TextView txtLocation = (TextView)findViewById(R.id.poi_detail_location);
		TextView txtDetail =  (TextView)findViewById(R.id.poi_detail_summary);
		TextView txtUrl = (TextView)findViewById(R.id.poi_detail_uri);
		
		//txtTitle.setVisibility(View.INVISIBLE);
		txtDetail.setVisibility(View.INVISIBLE);
		txtLocation.setVisibility(View.INVISIBLE);
		txtUrl.setVisibility(View.INVISIBLE);
		
		if (poi.getName() != null){
			// txtTitle.setText(poi.getName());
			// txtTitle.setVisibility(View.VISIBLE);
			setTitle(poi.getName());
		}
		
		if (poi.getDetails() != null){
			txtDetail.setText(poi.getDetails().replace(';', '\n') );
			txtDetail.setVisibility(View.VISIBLE);
		}
		
		if (poi.getStreet() != null || poi.getNumber() != null){
			txtLocation.setText(poi.getStreet() + " " + poi.getNumber());
			txtLocation.setVisibility(View.VISIBLE);
		}
		
		if (poi.getUrl() != null ) {
			txtUrl.setText(Html.fromHtml( "<a href='" + poi.getUrl() + "'>" + poi.getUrl() + "</a>" ));	
			txtUrl.setVisibility(View.VISIBLE);
		}
	
		// load map as last, load it asynchronously
        String noPlayServices = "Google Play Services not found, map will not be shown.";
        if (PlayServicesUtil.hasPlayServices(this, noPlayServices)){
        	new AsyncMapLoader().execute(poi);
        }
	}
	
	
	 public void navigateTo(View view) {
			String uri = "geo:" + poi.getLocation().latitude + ","
				+ poi.getLocation().longitude;
				
				try {
					startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
				} catch(ActivityNotFoundException e){
				String message = "Navigatie kan niet geopend worden.";
				         Toast.makeText(POIDetailActivity.this, message, Toast.LENGTH_LONG).show();
				}
	 }
	 


		@Override
		public void onInfoWindowClick(Marker marker) {
			Log.i("clicked on marker", marker.getId() );
			String uri = "geo:" + poi.getLocation().latitude + "," 
								+ poi.getLocation().longitude 
								+ "?q=" + poi.getStreet().replace(" ", "+")
								+ "+" + poi.getNumber();
			try {
				startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
			} catch(ActivityNotFoundException e){
				String message = "Maps-applicatie kan niet geopend worden.";
	        	Toast.makeText(POIDetailActivity.this, message, Toast.LENGTH_LONG).show();
				Log.e("Could not open Maps", marker.getId() );
			}
		}
		
		private class AsyncMapLoader extends AsyncTask<IPointOfInterest, Void, LatLng> {
		    private final ProgressDialog dialog = new ProgressDialog(POIDetailActivity.this);

		    @Override
		    protected void onPostExecute(LatLng result) {            
		        super.onPostExecute(result);
		        
		        if(result != null){ // if we found cošrdinates, update the PoI
		        	poi.setLocation(result);
		        } else if (poi.getLocation().latitude == 0.0 ||
		        		poi.getLocation().longitude == 0.0) {
		        	// no valid cošrdinates in the PoI
		        	String message = "Kon geen cošrdinaten vinden, map wordt niet getoond.";
		        	Toast.makeText(POIDetailActivity.this, message, Toast.LENGTH_LONG).show();
		        	dialog.dismiss();
		        	return;
		        }
		        
		        // create new row
		        TableRow tr = new TableRow(POIDetailActivity.this);
				tr.setLayoutParams(row_layout);
				
				RelativeLayout rl = new RelativeLayout(POIDetailActivity.this);
				rl.setLayoutParams(row_layout);
				tr.addView(rl);
				// inflate map fragment in row
		        View map_view = (View)getLayoutInflater().inflate(R.layout.fragment_map, rl);
		        
		        // add row to table_view
		        table_view.addView(tr, table_layout);
		
		        MapFragment map_fragment = (MapFragment) ((Activity)map_view.getContext()).getFragmentManager().findFragmentById(R.id.map);
		        GoogleMap map = map_fragment.getMap();
		        dialog.dismiss();
		        
		        //map.setMyLocationEnabled(true);
		        
		        // offset on location latitude (to show infowindow from marker)
		        LatLng maplocation = new LatLng(poi.getLocation().latitude + 0.00024, 
		        								poi.getLocation().longitude);
		        map.setOnInfoWindowClickListener(POIDetailActivity.this);
		        map.moveCamera(CameraUpdateFactory.newLatLngZoom(maplocation, 18));

		        Log.i("LatLng (w/o offset):", 
		        		"(" + poi.getLocation().latitude + 
		        		" ; " + poi.getLocation().longitude + ")"
		        	);
		        Log.i("LatLng (with offset):", 
		        		"(" + maplocation.latitude + 
		        		" ; " + maplocation.longitude + ")"
		        	);
		        String snippet = poi.getStreet() != null ? 
		        		poi.getStreet() + " " + poi.getNumber()
		        		: "";
		        Marker marker = map.addMarker(new MarkerOptions()
		        								.title(poi.getName())
		        								.snippet(snippet)
		        								.position(poi.getLocation())
		        								.icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN) )
		        							 );
		        marker.showInfoWindow();
		    }

		    @Override
		    protected void onPreExecute() {        
		        super.onPreExecute();
		        dialog.setMessage(getString(R.string.load_map));
		        dialog.show();            
		    }

		    @Override
		    protected LatLng doInBackground(IPointOfInterest... params) {
		    	PointOfInterest poi = (PointOfInterest) params[0];
				if(poi.getLocation().latitude == 0.0 || poi.getLocation().latitude == 0.0) {
			    	String locationname = "";
					try {
						locationname = poi.getStreet() + "+" + poi.getNumber() + "+Gent";
						LatLng location = LocationUtil.getLatLongFromAddress(locationname);
						if(location != null){
							return location;
						} 
						// try with different location info
						location = LocationUtil.getLatLongFromAddress(poi.getName());
						if(location != null){
							return location;
						} 
					} catch (CurlException e) {
						Log.e("Geocoder exception", locationname);
						e.printStackTrace();
					}
				}
		        return null;
		    }
		}

	 
	
}