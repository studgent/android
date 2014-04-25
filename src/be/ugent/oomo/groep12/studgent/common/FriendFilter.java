package be.ugent.oomo.groep12.studgent.common;

import java.util.ArrayList;
import java.util.List;

import android.widget.Filter;
import be.ugent.oomo.groep12.studgent.adapter.FriendAdapter;

public class FriendFilter extends Filter{

	private FriendAdapter friendAdapter;
	private ArrayList<Friend> originalData;
	
	public FriendFilter(FriendAdapter friendAdapter){
		this.friendAdapter=friendAdapter;
		originalData=new ArrayList<Friend>(friendAdapter.getItemList());
	}

	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		constraint = constraint.toString().toLowerCase();
	    FilterResults result = new FilterResults();
	    List<Friend> filterList = new ArrayList<Friend>();
	    if (constraint != null && constraint.toString().length() > 0) {

	        for (Friend p : originalData) {
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
		ArrayList<Friend> fitems = (ArrayList<Friend>) results.values;
		friendAdapter.clear();
		for (Friend p : fitems) {
			friendAdapter.add(p);
		}
		friendAdapter.notifyDataSetChanged();
		friendAdapter.notifyDataSetInvalidated();
	}
}
