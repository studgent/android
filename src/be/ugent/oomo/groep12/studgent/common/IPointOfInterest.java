package be.ugent.oomo.groep12.studgent.common;

import android.location.Location;

/**
 * Interface for the Poiny of Interest.
 *
 */
public interface IPointOfInterest {
	
	/**
	 * Returns the name for the Point of Interest
	 * @return Name for the Point of Interest as String
	 */
	public String getName();
	
	/**
	 * Sets the name for the Point of Interest
	 * @param name the name for the Point of Interest
	 */
	public void setName(String name);
	
	/**
	 * Returns the detailed information for the Point of Interest
	 * @return Detailed information for the Point of Interest as String
	 */
	public String getDetails();
	
	/**
	 * Sets detailed information for the Point of Interest
	 * @param details the detailed information for the Point of Interest
	 */
	public void setDetails(String details);

	/**
	 * Returns the location for the Point of Interest
	 * @return Location for the Point of Interest as android.location.Location
	 */
	public Location getLocation();
	
	/**
	 * Sets location for the Point of Interest
	 * @param location the locations as android.location.Location for the Point of Interest
	 */
	public void setLocation(Location location);
	
	/**
	 * Returns summary of the Point of Interest
	 * @return Summary of the Point of Interest as String
	 */
	public String toString();
}
