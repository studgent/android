package be.ugent.oomo.groep12.studgent.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.common.POIFilter;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;

import com.google.android.gms.maps.model.LatLng;

public class POIAdapter extends ArrayAdapter<PointOfInterest> implements Filterable{
    static class POIItemHolder{
    	TextView name;
    	TextView streetAndNumber;
    	TextView distance;
    	TextView unit;
        ImageView category_image;
    }
	
    Context context; 
    int layoutResourceId;    
    List<PointOfInterest> data = new ArrayList<PointOfInterest>();
    private Filter mFilter;
    
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new POIFilter(this);
        }
        return mFilter;
    }
    
    public PointOfInterest getItem(int pos){
    	return this.data.get(pos);
    }

	public POIAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
	}
	public POIAdapter(Context context, int layoutResourceId, List<PointOfInterest> objects) {
		super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = objects;
	}
	
	public List<PointOfInterest> getItemList(){
		return data;
	}
	
	

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        POIItemHolder holder = null;
        
        if (row == null) {
        	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new POIItemHolder();
            holder.category_image = (ImageView)row.findViewById(R.id.poi_list_item_category_image);
            holder.name = (TextView)row.findViewById(R.id.poi_item_name);
            holder.streetAndNumber = (TextView)row.findViewById(R.id.poi_item_steet);
            holder.distance = (TextView)row.findViewById(R.id.poi_item_distance);
            holder.unit = (TextView)row.findViewById(R.id.poi_item_unit);
            
            row.setTag(holder);
        } else {
            holder = (POIItemHolder)row.getTag();
        }
        
        PointOfInterest POI_item = data.get(position);
        //calculate distance. temponary work with hard point.
        //LatLng currentPosotion = new LatLng(51.032052, 3.701968); //<-------------------- nog wijzigen
        //double distance = distFrom(POI_item.getLocation().latitude, POI_item.getLocation().longitude, currentPosotion.latitude, currentPosotion.longitude);
        double distance = POI_item.getDistance();
        String unit = "m";
        if(distance>=1000.0){
        	distance=distance/1000.0;
        	unit = "km";
        }
        //de category_image moet nog gedaan worden
        Spanned name =Html.fromHtml((String) "" +  POI_item.getName());
        holder.name.setText(name);
        if ( POI_item != null && !( POI_item.getStreet() == null || (POI_item.getStreet().equals("")) ) ) {
        	holder.streetAndNumber.setText(Html.fromHtml("" +  POI_item.getStreet()+" "+POI_item.getNumber()) );
        } else if (POI_item != null) {
        	if (POI_item.getCheckins() == 0) {
            	holder.streetAndNumber.setText("Nog geen checkins.");
        	} else {
            	holder.streetAndNumber.setText("Al " + POI_item.getCheckins() + " checkins.");
        	}
        } else {
        	holder.streetAndNumber.setText("");
        }
        holder.distance.setText( Html.fromHtml("" +  String.valueOf(round(distance, 2))) );
        holder.unit.setText( Html.fromHtml("" +  unit) );
        
        return row;
    }
    
 
    public void setItemList(List<PointOfInterest>  itemList) {
        this.data = itemList;
    }
	@Override
	public void clear() {
		super.clear();
	}

	
	private double distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 3958.75;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    int meterConversion = 1609;

	    return (double) (dist * meterConversion);
	}
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	
}
