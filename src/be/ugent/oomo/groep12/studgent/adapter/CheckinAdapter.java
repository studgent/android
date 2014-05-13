package be.ugent.oomo.groep12.studgent.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.adapter.FriendAdapter.FriendItemHolder;
import be.ugent.oomo.groep12.studgent.common.Checkin;
import be.ugent.oomo.groep12.studgent.common.Friend;
import be.ugent.oomo.groep12.studgent.common.FriendFilter;

public class CheckinAdapter extends ArrayAdapter<Checkin> {
    static class CheckinItemHolder
    {
        TextView message;
        TextView location;
        TextView owner;    }
	
    Context context; 
    int layoutResourceId;    
    List<Checkin> data = new ArrayList<Checkin>();
    

	public CheckinAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
	}
	public CheckinAdapter(Context context, int layoutResourceId, List<Checkin> objects) {
		super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = objects;
	}
	

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CheckinItemHolder holder = null;
        
        if (row == null) {
        	//System.out.println("ruw==null");
        	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new CheckinItemHolder();
            holder.message = (TextView)row.findViewById(R.id.checkin_item_message); 
            holder.location = (TextView)row.findViewById(R.id.checkin_item_location);
            holder.owner = (TextView)row.findViewById(R.id.checkin_item_owner);
            
            row.setTag(holder);
        } else {
            holder = (CheckinItemHolder)row.getTag();
        }
        
        Checkin checkin_item = data.get(position);
        //System.out.println("Friend friend_item: "+data.get(position).getFirstName());
        
        holder.message.setText(Html.fromHtml(checkin_item.getMessage()) );
        holder.location.setText( Html.fromHtml(checkin_item.getPoiName()) );
        holder.owner.setText( Html.fromHtml(checkin_item.getUserName()) );
        
        return row;
    }
    


    public List<Checkin> getItemList() {
        return data;
    }
 
    public void setItemList(List<Checkin>  itemList) {
        this.data = itemList;
    }
	@Override
	public void clear() {
		super.clear();
	}
}
