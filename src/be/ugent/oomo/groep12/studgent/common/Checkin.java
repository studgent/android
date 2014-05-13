package be.ugent.oomo.groep12.studgent.common;

import android.os.Parcel;
import android.os.Parcelable;

public class Checkin implements IData {
	protected int id;
	protected String user_name;
	protected String poi_name;
	protected String message;
	
	public Checkin(int id, String user_name, String poi_name, String message){
		this.id=id;
		this.user_name=user_name;
		this.poi_name=poi_name;
		this.message=message;
	}
	
	public Checkin(Parcel in) {
		this.id = in.readInt();
		this.user_name = in.readString();
		this.poi_name = in.readString();
		this.message = in.readString();
	}


	@Override
	public void setName(String name) {
		this.message=message;
		
	}
	@Override
	public String getName() {
		return this.message;
	}
	@Override
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id=id;
	}
	public String getUserName() {
		return this.user_name;
	}
	public void setUserName(String user_name) {
		this.user_name=user_name;
	}
	public String getPoiName() {
		return this.poi_name;
	}
	public void setPoiName(String poi_name) {
		this.poi_name=poi_name;
	}
	public String getMessage() {
		return this.message;
	}
	public void setMessage(String message) {
		this.message=message;
	}
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.user_name);
		dest.writeString(this.poi_name);
		dest.writeString(this.message);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	    public Checkin createFromParcel(Parcel in) {
	        return new Checkin(in);
	    }
	
	    public Checkin[] newArray(int size) {
	        return new Checkin[size];
	    }
	};
	
}

