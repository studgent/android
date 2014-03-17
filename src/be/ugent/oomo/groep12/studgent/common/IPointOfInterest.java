package be.ugent.oomo.groep12.studgent.common;

import com.google.android.gms.maps.model.LatLng;


/**
 * Interface for the Poiny of Interest.
 *
 */
public interface IPointOfInterest extends IData {
	
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
	 * Returns street of Point of Interest
	 * @return street as string
	 */
	public String getStreet();
	
	/**
	 * Sets street for Point of Interest
	 * @param street
	 */
	public void setStreet(String street);
	
	/**
	 * Return street number 
	 * @return number as string
	 */
	public String getNumber();
	
	/**
	 * Sets street number
	 * @param number as string
	 */
	public void setNumber(String number);

	/**
	 * Returns the location for the Point of Interest
	 * @return Location for the Point of Interest as android.location.Location
	 */
	public LatLng getLocation();
	
	/**
	 * Sets location for the Point of Interest
	 * @param location the locations as android.location.Location for the Point of Interest
	 */
	public void setLocation(LatLng location);
	
	/**
	 * Returns summary of the Point of Interest
	 * @return Summary of the Point of Interest as String
	 */
	public String toString();
}
