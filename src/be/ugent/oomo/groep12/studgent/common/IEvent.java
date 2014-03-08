/**
 * 
 */
package be.ugent.oomo.groep12.studgent.common;

import java.util.Date;

/**
 * Interface for the Event.
 *
 */
public interface IEvent {
	
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
	 * Returns the Name for the Event
	 * @return Name for the Event as String
	 */
	public String getName();
	
	/**
	 * Sets the name for the Event
	 * @param name the name for the Event
	 */
	public void setName(String name);
	
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
	 * Returns summary of the Event
	 * @return Summary of the Event as String
	 */
	public String toString();
}
