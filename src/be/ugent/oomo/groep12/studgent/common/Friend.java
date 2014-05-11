package be.ugent.oomo.groep12.studgent.common;

import java.util.Date;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Friend implements IData {
	protected int id;
	protected boolean following;
	protected String lastName;
	protected String firstName;
	protected String email;
	protected String description;
	protected String phone;
	protected int score;
	protected Bitmap photo;
	
	public Friend(int id, boolean following, String firstname, String lastname, String email, String description, String phone, int score) {
		this.id = id;
		this.following = following;
		this.firstName = firstname;
		this.lastName = lastname;
		this.email = email;
		this.description = description;
		this.phone = phone;
		this.score = score;
	}
	
	public Friend(Parcel in) {
		this.id = in.readInt();
		this.following =  ( in.readInt() == 1 ) ? true : false;
		this.firstName = in.readString();
		this.lastName = in.readString();
		this.email = in.readString();
		this.description = in.readString();
		this.phone = in.readString();
		this.score = in.readInt();
	}


	public void setLastName(String lastName){
		this.lastName=lastName;
	}
	public void setFirstName(String firstName){
		this.firstName=firstName;
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
	public Bitmap getPhoto(){
		return this.photo;
	}
	public String getDescription(){
		return this.description;
	}

	public int getScore(){
		return this.score;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeInt( this.following ? 1 : 0);
		dest.writeString(this.firstName);
		dest.writeString(this.lastName);
		dest.writeString(this.email);
		dest.writeString(this.description);
		dest.writeString(this.phone);
		dest.writeInt(this.score);
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
	
	public boolean isFollowing(){
		return this.following;
	}
	
	@Override
	public String toString() {
		return this.getName();
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
