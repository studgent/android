package be.ugent.oomo.groep12.studgent.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.common.CalendarFilter;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;

public class CalenderAdapter extends ArrayAdapter<ICalendarEvent> implements Filterable {
    static class CalendarItemHolder
    {
        TextView name;
        TextView day_of_month;
        TextView month;
        TextView year;
        TextView location;
    }
	
    Context context; 
    int layoutResourceId;    
    List<ICalendarEvent> data = new ArrayList<ICalendarEvent>();
    private Filter mFilter;
    

    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new CalendarFilter(this);
        }
        return mFilter;
    }

	public CalenderAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
	}
	public CalenderAdapter(Context context, int layoutResourceId, List<ICalendarEvent> objects) {
		super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = objects;
	}
	

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CalendarItemHolder holder = null;
        
        if (row == null) {
        	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new CalendarItemHolder();
            holder.name = (TextView)row.findViewById(R.id.calendar_item_name);
            holder.month = (TextView)row.findViewById(R.id.calendar_item_month);
            holder.day_of_month = (TextView)row.findViewById(R.id.calendar_item_date);
            holder.year = (TextView)row.findViewById(R.id.calendar_item_year);
            holder.location = (TextView)row.findViewById(R.id.calendar_item_location);
            
            row.setTag(holder);
        } else {
            holder = (CalendarItemHolder)row.getTag();
        }
        
        ICalendarEvent calendar_item = data.get(position);
        SimpleDateFormat month = new SimpleDateFormat("MMM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        
        holder.name.setText(Html.fromHtml("" +  calendar_item.getName()));
        holder.month.setText( Html.fromHtml("" +  month.format(calendar_item.getFromDate())) );
        holder.day_of_month.setText( Html.fromHtml("" +  day.format(calendar_item.getFromDate())) );
        holder.year.setText( Html.fromHtml("" +  year.format(calendar_item.getFromDate())) );
        holder.location.setText( Html.fromHtml("" +  calendar_item.getLocation().getName()) );
        
        return row;
    }
    


    public List<ICalendarEvent> getItemList() {
        return data;
    }
 
    public void setItemList(List<ICalendarEvent>  itemList) {
        this.data = itemList;
    }
	@Override
	public void clear() {
		super.clear();
	}

	
	


}
