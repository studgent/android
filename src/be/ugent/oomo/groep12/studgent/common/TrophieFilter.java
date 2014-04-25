package be.ugent.oomo.groep12.studgent.common;

import java.util.ArrayList;
import java.util.List;

import android.widget.Filter;
import be.ugent.oomo.groep12.studgent.adapter.TrophieAdapter;

public class TrophieFilter extends Filter{

	private TrophieAdapter trophieAdapter;
	
	public TrophieFilter(TrophieAdapter trophieAdapter){
		this.trophieAdapter=trophieAdapter;
	}

	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		constraint = constraint.toString().toLowerCase();
	    FilterResults result = new FilterResults();
	    List<Trophie> filterList = new ArrayList<Trophie>();
	    if (constraint != null && constraint.toString().length() > 0) {
	    	List<Trophie> orginalList = new ArrayList<Trophie>(trophieAdapter.getItemList());

	        for (Trophie p : orginalList) {
	            if (p.getName().toLowerCase().contains(constraint))
	                filterList.add(p);
	        }
	        result.values = filterList;
	        result.count = filterList.size();

	    } else {

	        result.values = trophieAdapter.getItemList();
	        result.count = trophieAdapter.getItemList().size();

	    }
	    return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {
		ArrayList<Trophie> fitems = (ArrayList<Trophie>) results.values;
		trophieAdapter.clear();
		trophieAdapter.setItemList(fitems);
	}
}
