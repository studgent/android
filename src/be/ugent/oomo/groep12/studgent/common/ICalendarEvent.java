/**
 * 
 */
package be.ugent.oomo.groep12.studgent.common;

import java.util.Date;

/**
 * Interface for the Event.
 *
 */
public interface ICalendarEvent extends IData {
	
	/**
	 * Returns the Date for the Event
	 * @return Date for the Event as java.util.Date
	 */
	public Date getDate();
	
	/**
	 * Sets the date for the Event
	 * @param date the date as java.util.date for the Event
	 */
	public void setDate(Date date);
	
	/**
	 * Returns the detailed information for the Event
	 * @return Detailed information for the Event as String
	 */
	public String getDetails();
	
	/**
	 * Sets the detailed information for the Event
	 * @param details the details for the Event
	 */
	public void setDetails(String details);
	
	
	/**
	 * Returns the detailed information for the Event
	 * @return Detailed information for the Event as String
	 */
	public IPointOfInterest getLocation();
	
	/**
	 * Sets the detailed information for the Event
	 * @param details the details for the Event
	 */
	public void setLocation(IPointOfInterest location);
	
	
	/**
	 * Returns summary of the Event
	 * @return Summary of the Event as String
	 */
	public String toString();
}
