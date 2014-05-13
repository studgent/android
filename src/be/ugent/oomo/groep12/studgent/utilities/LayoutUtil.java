package be.ugent.oomo.groep12.studgent.utilities;

import java.util.ArrayList;
import java.util.Map;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.activity.POIListActivity;
import be.ugent.oomo.groep12.studgent.adapter.POIAdapter;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.POIDataSource;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;

public class LayoutUtil {
	
	/**
	 * Adds a stylish effect to a button
	 * @param button : the button
	 */
	public static void buttonEffect(View button){
	    button.setOnTouchListener(new OnTouchListener() {

	        public boolean onTouch(View v, MotionEvent event) {
	            switch (event.getAction()) {
	                case MotionEvent.ACTION_DOWN: {
	                    v.getBackground().setColorFilter(Color.parseColor("#bed774"), PorterDuff.Mode.SRC_ATOP);
	                    v.invalidate();
	                    break;
	                }
	                case MotionEvent.ACTION_UP: {
	                    v.getBackground().clearColorFilter();
	                    v.invalidate();
	                    break;
	                }
	            }
	            return false;
	        }
	    });
	}
}
