/**
 * 
 */
package be.ugent.oomo.groep12.studgent.common;

import java.util.Date;

/**
 * Interface for the Event.
 *
 */
@SuppressWarnings("rawtypes")
public interface ICalendarEvent extends IData, Comparable {

	public String getType();
	public void setType(String type);

	
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
	

	public String getDescription();
	public void setDescription(String description);
	
	
	/**
	 * Returns the Date for the Event
	 * @return Date for the Event as java.util.Date
	 */
	public Date getFromDate();
	
	
	/**
	 * Sets the date for the Event
	 * @param date the date as java.util.date for the Event
	 */
	public void setFromDate(Date date);
	
	/**
	 * Returns the Date for the Event
	 * @return Date for the Event as java.util.Date
	 */
	public Date getToDate();
	
	/**
	 * Sets the date for the Event
	 * @param date the date as java.util.date for the Event
	 */
	public void setToDate(Date date);

	/**
	 * Returns contact name
	 * @return Contact as String
	 */
	public String getContact();
	
	/**
	 * Sets contact name
	 * @param contact
	 */
	public void setContact(String contact);
	
	/**
	 * Returns phone number
	 * @return Phone number as string
	 */
	public String getPhone();
	
	/**
	 * Sets phone number
	 * @param phone
	 */
	public void setPhone(String phone);
	
	/**
	 * Returns email address as string
	 * @return email address
	 */
	public String getEmail();
	
	/**
	 * Sets email address
	 * @param email
	 */
	public void setEmail(String email);
	
	/**
	 * Return uri/website as string
	 * @return uri
	 */
	public String getUri();
	
	/**
	 * Sets uri
	 * @param uri
	 */
	public void setUri(String uri);
	
	/**
	 * Returns location of image
	 * @return image location as string
	 */
	public String getImage();
	
	/**
	 * Sets image location
	 * @param image
	 */
	public void setImage(String image);
	
	/**
	 * Returns prices
	 * @return prices as json string
	 */
	public String getPrices();
	
	/**
	 * Sets prices
	 * @param prices as json string
	 */
	public void setPrices(String prices);
	
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
