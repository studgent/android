package be.ugent.oomo.groep12.studgent.common;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;



/**
 * Generic PointOfInterest class implementing IPointOfInterest
 *
 */
public class PointOfInterest implements IPointOfInterest {
	
	protected int id;
	protected String name;
	protected String details;
	protected String street;
	protected String number;
	protected LatLng location;
	protected String url;

	public PointOfInterest(int id, String name, String details, String street, String number, LatLng location){
		this(id, name, details, location);
		this.street = street;
		this.number = number;
	}
	

	public PointOfInterest(int id, String name, String details, LatLng location){
		this.id = id;
		this.name = name;
		this.details = details;
		this.location = location;
	}


	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDetails() {
		return details;
	}

	@Override
	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public String getStreet() {
		return this.street;
	}

	@Override
	public void setStreet(String street) {
		this.street = street;
	}

	@Override
	public String getNumber() {
		return this.number;
	}

	@Override
	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public LatLng getLocation() {
		return location;
	}

	@Override
	public void setLocation(LatLng location) {
		this.location = location;
	}



	// Parcelable
	
	/**
	 * Constructor for parcelable
	 * Constructs CalendarEvent from Parcel object
	 * @param in
	 */
	public PointOfInterest(Parcel in){
		this.id = in.readInt();
		this.name = in.readString();
		this.details = in.readString();
		this.street = in.readString();
		this.number = in.readString();
		this.location = new LatLng(in.readDouble(),in.readDouble());
	}
	

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.name);
		dest.writeString(this.details);
		dest.writeString(this.street);
		dest.writeString(this.number);
		dest.writeDouble(this.location.latitude);
		dest.writeDouble(this.location.longitude);
	}
	

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	    public PointOfInterest createFromParcel(Parcel in) {
	        return new PointOfInterest(in);
	    }
	
	    public PointOfInterest[] newArray(int size) {
	        return new PointOfInterest[size];
	    }
	};

	
	@Override
	public void setUrl(String url) {
		// TODO Auto-generated method stub
		this.url=url;
	}


	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return url;
	}
	
	

}
