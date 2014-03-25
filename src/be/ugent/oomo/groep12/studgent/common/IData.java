package be.ugent.oomo.groep12.studgent.common;

import android.os.Parcelable;

public interface IData extends Parcelable {
	public int getId();
	
	/**
	 * Returns the name for the object
	 * @return Name for the object as String
	 */
	public String getName();
	
	/**
	 * Sets the name for the object
	 * @param name the name for the object
	 */
	public void setName(String name);
}
