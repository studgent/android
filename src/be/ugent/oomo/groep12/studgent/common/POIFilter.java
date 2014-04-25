package be.ugent.oomo.groep12.studgent.common;

import java.util.ArrayList;
import java.util.List;

import be.ugent.oomo.groep12.studgent.adapter.POIAdapter;
import android.widget.Filter;

public class POIFilter extends Filter{

	private POIAdapter poiAdapter;
	private ArrayList<PointOfInterest> originalData;
	
	public POIFilter(POIAdapter poiAdapter){
		this.poiAdapter=poiAdapter;
		originalData=new ArrayList<PointOfInterest>(poiAdapter.getItemList());
	}
	
	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		constraint = constraint.toString().toLowerCase();
	    FilterResults result = new FilterResults();
	    List<PointOfInterest> filterList = new ArrayList<PointOfInterest>();
	    if (constraint != null && constraint.toString().length() > 0) {
	        for (PointOfInterest p : originalData) {
	            if (p.getName().toLowerCase().contains(constraint))
	                filterList.add(p);
	        }
	        result.values = filterList;
	        result.count = filterList.size();

	    } else {

	        result.values = originalData;
	        result.count = originalData.size();

	    }
	    return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {
		ArrayList<PointOfInterest> fitems = (ArrayList<PointOfInterest>) results.values;
		poiAdapter.clear();
		for (PointOfInterest p : fitems) {
			poiAdapter.add(p);
		}
		poiAdapter.notifyDataSetChanged();
		poiAdapter.notifyDataSetInvalidated();
	}

}
