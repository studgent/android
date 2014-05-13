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
	
	public static void updateListViewHeight(ListView myListView) {
		new AsyncPOIListViewLoader().execute(myListView);
	}
	


	protected static class AsyncPOIListViewLoader extends
			AsyncTask<ListView, Void, ViewGroup.LayoutParams> {
		
		private ListView myListView;

		@Override
		protected void onPostExecute(ViewGroup.LayoutParams result) {
			super.onPostExecute(result);
		    myListView.setLayoutParams(result);
		}


		@Override
		protected ViewGroup.LayoutParams doInBackground(
				ListView... params) {
			myListView = params[0];
		     ListAdapter myListAdapter = myListView.getAdapter();
		     if (myListAdapter != null) {
			    //get listview height
			    int totalHeight = 0;
			    int adapterCount = myListAdapter.getCount();
			    for (int size = 0; size < adapterCount ; size++) {
			        View listItem = myListAdapter.getView(size, null, myListView);
			        listItem.measure(0, 0);
			        totalHeight += listItem.getMeasuredHeight();
			    }
			    //Change Height of ListView 
			    ViewGroup.LayoutParams layoutparams = myListView.getLayoutParams();
			    layoutparams.height = totalHeight + (myListView.getDividerHeight() * (adapterCount - 1));
			    return layoutparams;
		    } else {
		    	return myListView.getLayoutParams();
		    }
		}
	}
}
