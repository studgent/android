package be.ugent.oomo.groep12.studgent.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import be.ugent.oomo.groep12.studgent.activity.POIDetailActivity;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.POIDataSource;
import be.ugent.oomo.groep12.studgent.utilities.LocationUtil;
import be.ugent.oomo.groep12.studgent.utilities.iLocationChangedListener;

import com.google.android.gms.maps.model.LatLng;

public class OverlayView extends FrameLayout implements OnClickListener,
		iLocationChangedListener {

	private int screenWidth;
	private static int fov = 70;
	private static int range = 2000;
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

	/*
	 * This method sets some variables at View creation
	 * 
	 * @param context The current application context
	 */
	private void init(Context context) {
		screenWidth = context.getResources().getDisplayMetrics().widthPixels;
	}

	/*
	 * This method updates all the POIViews currently on the overlay with a new bearing
	 * and makes sure the Views are drawn on the correct location relative to true North.
	 * 
	 * @param az The value describing the current device azimuth (heading).
	 */
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
				v.setTranslationX(-offset
						+ (screenWidth / 2 - v.getMinWidth() / 2));
				// Hackish way to force visibility with a SurfaceView beneath
				// this view
				v.setVisibility(View.VISIBLE);
				v.requestLayout();
			}
		}
	}

	/*
	 * Implementation of onClickListener to handle the click events of the POIViews.
	 * 
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (((POIView) v).getPoi() != null) {
			Intent intent = new Intent(getContext(), POIDetailActivity.class);
			intent.putExtra("poi", ((POIView) v).getPoi());
			getContext().startActivity(intent);
		}
	}

	/*
	 * This functions fetches the POIs and creates the POIView objects to be
	 * drawn. It then sorts the POIs and limits the amount that will be drawn.
	 * 
	 * @param range The maximum range a POI can be to be drawn on screen.
	 * @return An ArrayList of POIViews in range, limited to a specific amount
	 */
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
		Collections.sort(list);
		// Limit the amount of POIs we will show
		if (list.size() > amount) {
			list = new ArrayList<POIView>(list.subList(0, amount));
		}
		return list;
	}
	
	/*
	 * This method is used to update the POIs that are drawn when the device location changes
	 */
	private void updatePois() {
		// Clear current pois
		this.removeAllViews();
		// Redraw with updated poi list
		pois = getPoiList(range);
		pois.add(new POIView(getContext(), new PointOfInterest(999999,
				"Noorden", "", new LatLng(179, 3))));
		for (POIView v : pois) {
			this.addView(v);
			// Make view clickable
			v.setOnClickListener(this);
			v.setClickable(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.utilities.iLocationChangedListener#locationIsChanged(android.location.Location)
	 */
	@Override
	public void locationIsChanged(Location loc) {
		// Save new location
		devLoc = loc;
		updatePois();
		// Log.d("Hmm", devLoc.toString());

	}
}
