package be.ugent.oomo.groep12.studgent.common;

import java.util.Date;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Friend implements IData {
	protected int id;
	protected String lastName;
	protected String firstName;
	protected Gender gender;
	protected String location;
	protected Bitmap photo;
	
	public Friend(int id, String lastName, String firstName, Gender gender, String location){
		this.lastName=lastName;
		this.firstName=firstName;
		this.gender=gender;
		this.location=location;
	}
	
	public Friend(Parcel in) {
		this.id = in.readInt();
		this.firstName = in.readString();
		this.lastName = in.readString();
		this.location = in.readString();
		//protected IPointOfInterest this.poi;
	}


	public void setLastName(String lastName){
		this.lastName=lastName;
	}
	public void setFirstName(String firstName){
		this.firstName=firstName;
	}
	public void setGender(Gender gender){
		this.gender=gender;
	}
	public void setLocation(String location){
		this.location=location;
	}
	public void setPhoto(Bitmap photo){
		this.photo=photo;
	}
	public String getLastName(){
		return this.lastName;
	}
	public String getFirstName(){
		return this.firstName;
	}
	public String getGender(){
		return this.gender.toString();
	}
	public String getLocation(){
		return this.location;
	}
	public Bitmap getPhoto(){
		return this.photo;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.firstName);
		dest.writeString(this.lastName);
		dest.writeString(this.location);
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.firstName+" "+this.lastName;
	}

	@Override
	public void setName(String name) {
		this.lastName=name; //niet correct
		
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	    public Friend createFromParcel(Parcel in) {
	        return new Friend(in);
	    }
	
	    public Friend[] newArray(int size) {
	        return new Friend[size];
	    }
	};
	
}
