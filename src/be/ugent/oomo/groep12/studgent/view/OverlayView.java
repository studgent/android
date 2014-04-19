package be.ugent.oomo.groep12.studgent.view;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.utilities.LocationUtil;

import com.google.android.gms.maps.model.LatLng;

public class OverlayView extends FrameLayout {

	private int screenWidth;
	private int screenHeight;
	private static int fov = 80;

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
		screenHeight = context.getResources().getDisplayMetrics().heightPixels;

		// Set device location as static, for now
		devLoc = LocationUtil.getLocationFromLatLng(new LatLng(50.848602,
				3.175140));

		// Add some dummy data
		pois = new ArrayList<POIView>();
		POIView t = new POIView(context, new PointOfInterest(10, "Noorden", "",
				new LatLng(179, 0)));
		pois.add(t);
		t = new POIView(context, new PointOfInterest(10, "Delta Lights", "",
				new LatLng(50.844924, 3.176591)));
		pois.add(t);
		for (POIView v : pois) {
			this.addView(v);
		}
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
				v.setTranslationX(-offset);
				// Hackish way to force visibility with a SurfaceView beneath
				// this view
				v.setVisibility(View.VISIBLE);
				v.requestLayout();
			}
		}
	}
}
