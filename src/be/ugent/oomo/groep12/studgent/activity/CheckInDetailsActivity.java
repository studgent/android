package be.ugent.oomo.groep12.studgent.activity;

import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.FriendListDataSource;
import be.ugent.oomo.groep12.studgent.data.POIDataSource;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.utilities.LocationUtil;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;
import be.ugent.oomo.groep12.studgent.utilities.PlayServicesUtil;

public class CheckInDetailsActivity extends Activity implements
		OnInfoWindowClickListener {

	private PointOfInterest poi;
	protected TableLayout table_view;
	protected LayoutParams row_layout;
	protected LayoutParams table_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		poi = getIntent().getParcelableExtra("poi");
		setContentView(R.layout.activity_checkin_details);
		

		// get the table from view
		table_view = (TableLayout) findViewById(R.id.poi_detail_table);
		// set some default data for table and row layout
		row_layout = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT);
		table_layout = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT);

		TextView txtLocation = (TextView) findViewById(R.id.poi_detail_location);
		TextView txtDetail = (TextView) findViewById(R.id.poi_detail_summary);
		TextView txtUrl = (TextView) findViewById(R.id.poi_detail_uri);

		txtLocation.setVisibility(View.INVISIBLE);
		txtDetail.setVisibility(View.INVISIBLE);
		txtUrl.setVisibility(View.INVISIBLE);

		if (poi.getName() != null) {
			setTitle(poi.getName());
		}

		if (poi.getStreet() != null || poi.getNumber() != null) {
			txtLocation.setText(poi.getStreet() + " " + poi.getNumber());
			txtLocation.setVisibility(View.VISIBLE);
		} else if (poi.getName() != null) {
			txtLocation.setText(poi.getName());
			txtLocation.setVisibility(View.VISIBLE);
		}

		if (poi.getDetails() != null) {
			txtDetail.setText(poi.getDetails().replace(';', '\n'));
			txtDetail.setVisibility(View.VISIBLE);
		}

		if (poi.getUrl() != null) {
			txtUrl.setText(poi.getUrl());
			txtUrl.setVisibility(View.VISIBLE);
		}

		// load map as last, load it asynchronously
		String noPlayServices = "Google Play Services not found, map will not be shown.";
		if (PlayServicesUtil.hasPlayServices(this, noPlayServices)) {
			new AsyncMapLoader().execute(poi);
		}

	}

	public void checkIn(View view) {
		checkIn();
	}

	private class AsyncCheckin extends AsyncTask<Integer, Void, Boolean> {

	    @Override
	    protected void onPostExecute(Boolean result) {            
	        super.onPostExecute(result);
	    }

		@Override
		protected Boolean doInBackground(Integer... params) {
			try {
				boolean result = POIDataSource.getInstance().checkin(poi);
	        	return result;
	        }
	        catch(Throwable t) {
	            t.printStackTrace();
				return false;
	        }
		}
	}
	
	protected void checkIn() {
		// finish checkin. This means storing the key value pare wich remambers
		// the time, location and POIid of the last checkin
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"LastCheckin", Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("lat", String.valueOf(poi.getLocation().latitude));
		editor.putString("lon", String.valueOf(poi.getLocation().longitude));
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		editor.putLong("date", now.getTime());
		editor.putString("name", poi.getName());
		editor.commit();
		
		new AsyncCheckin().execute();
		this.finish();
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		checkIn();
	}

	private class AsyncMapLoader extends
			AsyncTask<IPointOfInterest, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(
				CheckInDetailsActivity.this);

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
				Toast.makeText(CheckInDetailsActivity.this, message,
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
			TableRow tr = new TableRow(CheckInDetailsActivity.this);
			tr.setLayoutParams(row_layout);

			RelativeLayout rl = new RelativeLayout(CheckInDetailsActivity.this);
			rl.setLayoutParams(row_layout);
			tr.addView(rl);
			// inflate map fragment in row
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
			map.setOnInfoWindowClickListener(CheckInDetailsActivity.this);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(maplocation, 18));

			Log.i("LatLng (w/o offset):", "(" + poi.getLocation().latitude
					+ " ; " + poi.getLocation().longitude + ")");
			Log.i("LatLng (with offset):", "(" + maplocation.latitude + " ; "
					+ maplocation.longitude + ")");
			String snippet = poi.getStreet() != null ? poi.getStreet() + " "
					+ poi.getNumber() : "";
			Marker marker = map.addMarker(new MarkerOptions()
					.title(poi.getName())
					.snippet(snippet)
					.position(poi.getLocation())
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
			marker.showInfoWindow();
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
					Log.e("Reverse geocoder exception", location);
					e.printStackTrace();
				}
			}
			return null;
		}
	}

}
