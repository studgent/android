package be.ugent.oomo.groep12.studgent.common;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Trophie implements IData {
	protected int id;
	protected String name;
	protected int points;
	
	public Trophie(int id, String name, int points){
		this.name=name;
		this.points=points;
		this.id=id;
	}
	
	public Trophie(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
		this.points = in.readInt();
		//hier nog iets voor de foto...
	}


	@Override
	public void setName(String name) {
		this.name=name;
		
	}
	@Override
	public String getName() {
		return this.name;
	}
	public void setPoints(int points){
		this.points=points;
	}
	public int getPoints(){
		return this.points;
	}
	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.name);
		dest.writeInt(this.points);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	    public Trophie createFromParcel(Parcel in) {
	        return new Trophie(in);
	    }
	
	    public Trophie[] newArray(int size) {
	        return new Trophie[size];
	    }
	};
	
}
