package be.ugent.oomo.groep12.studgent.view;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import be.ugent.oomo.groep12.studgent.activity.POIDetailActivity;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.POIDataSource;
import be.ugent.oomo.groep12.studgent.utilities.LocationUtil;

import com.google.android.gms.maps.model.LatLng;

public class OverlayView extends FrameLayout implements OnClickListener,
		LocationListener {

	private int screenWidth;
	private static int fov = 70;
	private static int range = 1000;
	private static int amount = 15;

	private Location devLoc;
	private ArrayList<POIView> pois;

	public OverlayView(Context context) {
		super(context);
		init(context);
	}

	public OverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public OverlayView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		screenWidth = context.getResources().getDisplayMetrics().widthPixels;

		// Set device location as static, for now
		//devLoc = LocationUtil.getLocationFromLatLng(new LatLng(51.05389, 3.705));
		// Get current location from last known network location
		devLoc = ((LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		// Pois
		updatePois();
	}

	public void updateOverlay(int az) {
		for (POIView v : pois) {
			float bearing = devLoc.bearingTo(LocationUtil
					.getLocationFromLatLng(v.getPoi().getLocation()));

			float t = ((az - bearing) + 180) % 360 - 180;

			if (t > fov) {
				// Do not draw, because poi is outside fov
				v.setVisibility(View.INVISIBLE);
			} else {
				float offset = ((az - bearing) + 180) % 360 - 180;
				offset = (offset / fov) * screenWidth;
				v.setTranslationX(-offset + (screenWidth/2 - v.getMinWidth()/2));
				// Hackish way to force visibility with a SurfaceView beneath this view
				v.setVisibility(View.VISIBLE);
				v.requestLayout();
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (((POIView) v).getPoi() != null) {
			Intent intent = new Intent(getContext(), POIDetailActivity.class);
			intent.putExtra("poi", ((POIView) v).getPoi());
			getContext().startActivity(intent);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// Save new location
		devLoc = location;
		updatePois();
		//Log.d("Hmm", devLoc.toString());
	}

	private ArrayList<POIView> getPoiList(float range) {
		ArrayList<POIView> list = new ArrayList<POIView>();
		// Fetch POI data
		Map<Integer, IPointOfInterest> data = POIDataSource.getInstance()
				.getLastItems();
		// Only keep those within range
		for (Map.Entry<Integer, IPointOfInterest> poi : data.entrySet()) {
			if (devLoc.distanceTo(LocationUtil.getLocationFromLatLng(poi
					.getValue().getLocation())) <= range) {
				list.add(new POIView(getContext(), poi.getValue()));
			}
		}
		// Sort for distance
		// Collections.sort(list);
		// Limit the amount of POIs we will show
		if (list.size() > amount) {
			list = new ArrayList<POIView>(list.subList(0, amount));
		}
		return list;
	}

	private void updatePois() {
		// Clear current pois
		this.removeAllViews();
		// Redraw with updated poi list
		pois = getPoiList(range);
		pois.add(new POIView(getContext(), new PointOfInterest(999999, "Noorden", "", new LatLng(179, 3))));
		for (POIView v : pois) {
			this.addView(v);
			// Make view clickable
			v.setOnClickListener(this);
			v.setClickable(true);
		}
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
