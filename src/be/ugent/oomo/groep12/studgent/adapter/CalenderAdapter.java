package be.ugent.oomo.groep12.studgent.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;

public class CalenderAdapter extends ArrayAdapter<ICalendarEvent> {
    static class CalendarItemHolder
    {
        TextView name;
        TextView day_of_month;
        TextView month;
        TextView location;
    }
	
    Context context; 
    int layoutResourceId;    
    List<ICalendarEvent> data = new ArrayList<ICalendarEvent>();

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
            holder.location = (TextView)row.findViewById(R.id.calendar_item_location);
            
            row.setTag(holder);
        } else {
            holder = (CalendarItemHolder)row.getTag();
        }
        
        ICalendarEvent calendar_item = data.get(position);
        SimpleDateFormat month = new SimpleDateFormat("MMM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        
        holder.name.setText(calendar_item.getName());
        holder.month.setText( month.format(calendar_item.getDate()) );
        holder.day_of_month.setText( day.format(calendar_item.getDate()) );
        holder.location.setText( calendar_item.getLocation().getName() );
        
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
