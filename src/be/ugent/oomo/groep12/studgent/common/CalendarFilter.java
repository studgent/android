package be.ugent.oomo.groep12.studgent.common;

import java.util.ArrayList;
import java.util.List;

import android.widget.Filter;
import be.ugent.oomo.groep12.studgent.adapter.CalenderAdapter;


public class CalendarFilter extends Filter{

	private CalenderAdapter calendarAdapter;
	private ArrayList<ICalendarEvent> originalData;
	
	public CalendarFilter(CalenderAdapter calendarAdapter){
		this.calendarAdapter=calendarAdapter;
		originalData=new ArrayList<ICalendarEvent>(calendarAdapter.getItemList());
	}

	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		constraint = constraint.toString().toLowerCase();
	    FilterResults result = new FilterResults();
	    List<ICalendarEvent> filterList = new ArrayList<ICalendarEvent>();
	    if (constraint != null && constraint.toString().length() > 0) {

	        for (ICalendarEvent p : originalData) {
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
		ArrayList<ICalendarEvent> fitems = (ArrayList<ICalendarEvent>) results.values;
		calendarAdapter.clear();
		for (ICalendarEvent p : fitems) {
			calendarAdapter.add(p);
		}
		calendarAdapter.notifyDataSetChanged();
		calendarAdapter.notifyDataSetInvalidated();
	}
}