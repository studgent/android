package be.ugent.oomo.groep12.studgent.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.common.Friend;

public class FriendAdapter extends ArrayAdapter<Friend> {
    static class FriendItemHolder
    {
    	ImageView image;
        TextView name;
        TextView location;
    }
	
    Context context; 
    int layoutResourceId;    
    List<Friend> data = new ArrayList<Friend>();

	public FriendAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
	}
	public FriendAdapter(Context context, int layoutResourceId, List<Friend> objects) {
		super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = objects;
	}
	

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FriendItemHolder holder = null;
        
        if (row == null) {
        	//System.out.println("ruw==null");
        	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new FriendItemHolder();
            holder.name = (TextView)row.findViewById(R.id.friend_item_name); 
            holder.location = (TextView)row.findViewById(R.id.friend_item_location);
            holder.image = (ImageView)row.findViewById(R.id.friend_item_image);
            row.setTag(holder);
        } else {
            holder = (FriendItemHolder)row.getTag();
        }
        
        Friend friend_item = data.get(position);
        //System.out.println("Friend friend_item: "+data.get(position).getFirstName());
        
        holder.name.setText(friend_item.getFirstName()+" "+friend_item.getLastName());
        holder.location.setText( friend_item.getLocation() );
        //holder.image.setImageBitmap(friend_item.getPhoto()); //nog geen foto
        
        return row;
    }
    


    public List<Friend> getItemList() {
        return data;
    }
 
    public void setItemList(List<Friend>  itemList) {
        this.data = itemList;
    }
	@Override
	public void clear() {
		super.clear();
	}

	
	


}
