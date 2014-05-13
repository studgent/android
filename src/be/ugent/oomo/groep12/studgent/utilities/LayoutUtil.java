package be.ugent.oomo.groep12.studgent.utilities;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
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
	     ListAdapter myListAdapter = myListView.getAdapter();
	     if (myListAdapter == null) {            
	              return;
	     }
	    //get listview height
	    int totalHeight = 0;
	    int adapterCount = myListAdapter.getCount();
	    for (int size = 0; size < adapterCount ; size++) {
	        View listItem = myListAdapter.getView(size, null, myListView);
	        listItem.measure(0, 0);
	        totalHeight += listItem.getMeasuredHeight();
	    }
	    //Change Height of ListView 
	    ViewGroup.LayoutParams params = myListView.getLayoutParams();
	    params.height = totalHeight + (myListView.getDividerHeight() * (adapterCount - 1));
	    myListView.setLayoutParams(params);
	}
}
