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
import be.ugent.oomo.groep12.studgent.common.Friend;
import be.ugent.oomo.groep12.studgent.common.FriendFilter;

public class FriendAdapter extends ArrayAdapter<Friend> implements Filterable {
    static class FriendItemHolder
    {
    	ImageView image;
        TextView name;
        TextView location;
        ImageButton follow;
    }
	
    Context context; 
    int layoutResourceId;    
    List<Friend> data = new ArrayList<Friend>();
    private Filter mFilter;
    
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new FriendFilter(this);
        }
        return mFilter;
    }

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
            holder.follow = (ImageButton)row.findViewById(R.id.status_button);
            
            row.setTag(holder);
        } else {
            holder = (FriendItemHolder)row.getTag();
        }
        
        Friend friend_item = data.get(position);
        //System.out.println("Friend friend_item: "+data.get(position).getFirstName());
        
        holder.name.setText(Html.fromHtml(friend_item.getFirstName()+" "+friend_item.getLastName()) );
        holder.location.setText( Html.fromHtml(friend_item.getScore() + " points") );
        
        holder.follow.setFocusable(false);
        holder.follow.setFocusableInTouchMode(false);
        
        if ( friend_item.isFollowing() ) {
        	holder.follow.setBackgroundResource(R.drawable.check);
        	holder.follow.setContentDescription("check");
        } else {
        	holder.follow.setBackgroundResource(R.drawable.add_friend);
        	holder.follow.setContentDescription("nothing");
        }
        holder.follow.setTag(friend_item.getId());
        
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
	
	public void setFollowStatus(int userID, boolean follow){
		int i=0;
		while(i<data.size() && data.get(i).getId()!=userID){
			i++;
		}
		if(i!=data.size()){
			data.get(i).setFollowing(follow);
			this.notifyDataSetChanged();
			this.notifyDataSetInvalidated();
		}
	}
}
