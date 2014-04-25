package be.ugent.oomo.groep12.studgent.common;

import java.util.ArrayList;
import java.util.List;

import be.ugent.oomo.groep12.studgent.adapter.POIAdapter;
import android.widget.Filter;

public class POIFilter extends Filter{

	private POIAdapter poiAdapter;
	
	public POIFilter(POIAdapter poiAdapter){
		this.poiAdapter=poiAdapter;
	}
	
	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		constraint = constraint.toString().toLowerCase();
	    FilterResults result = new FilterResults();
	    List<PointOfInterest> filterList = new ArrayList<PointOfInterest>();
	    if (constraint != null && constraint.toString().length() > 0) {
	    	List<PointOfInterest> orginalList = new ArrayList<PointOfInterest>(poiAdapter.getItemList());

	        for (PointOfInterest p : orginalList) {
	            if (p.getName().toLowerCase().contains(constraint))
	                filterList.add(p);
	        }
	        result.values = filterList;
	        result.count = filterList.size();

	    } else {

	        result.values = poiAdapter.getItemList();
	        result.count = poiAdapter.getItemList().size();

	    }
	    return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {
		ArrayList<PointOfInterest> fitems = (ArrayList<PointOfInterest>) results.values;
		poiAdapter.clear();
	    poiAdapter.setItemList(fitems);
		
	}

}
