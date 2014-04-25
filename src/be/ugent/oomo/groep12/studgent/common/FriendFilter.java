package be.ugent.oomo.groep12.studgent.common;

import java.util.ArrayList;
import java.util.List;

import android.widget.Filter;
import be.ugent.oomo.groep12.studgent.adapter.FriendAdapter;

public class FriendFilter extends Filter{

	private FriendAdapter friendAdapter;
	
	public FriendFilter(FriendAdapter friendAdapter){
		this.friendAdapter=friendAdapter;
	}

	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		constraint = constraint.toString().toLowerCase();
	    FilterResults result = new FilterResults();
	    List<Friend> filterList = new ArrayList<Friend>();
	    if (constraint != null && constraint.toString().length() > 0) {
	    	List<Friend> orginalList = new ArrayList<Friend>(friendAdapter.getItemList());

	        for (Friend p : orginalList) {
	            if (p.getName().toLowerCase().contains(constraint))
	                filterList.add(p);
	        }
	        result.values = filterList;
	        result.count = filterList.size();

	    } else {

	        result.values = friendAdapter.getItemList();
	        result.count = friendAdapter.getItemList().size();

	    }
	    return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {
		ArrayList<Friend> fitems = (ArrayList<Friend>) results.values;
		friendAdapter.clear();
		friendAdapter.setItemList(fitems);
	}
}
