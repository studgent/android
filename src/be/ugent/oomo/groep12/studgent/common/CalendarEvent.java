package be.ugent.oomo.groep12.studgent.common;

import java.util.Date;

import com.google.android.gms.maps.model.LatLng;


import android.location.Location;

/**
 * Generic Event class implementing IEvent
 *
 */

public class CalendarEvent implements ICalendarEvent {
	protected int id;
	protected String name;
	protected String type;
	protected Date from_date;
	protected Date to_date;
	protected String details;
	protected String description;
	protected String contact;
	protected String phone;
	protected String email;
	protected String uri;
	protected String image;
	protected String prices;
	protected IPointOfInterest poi;
	
	public CalendarEvent(int id, 
						 String type,
						 String name,
						 Date from_date,
						 Date to_date,
						 String details,
						 String description,
						 String contact,
						 String phone,
						 String email,
						 String uri,
						 String image,
						 String prices,
						 String street,
						 String number,
						 LatLng loc){
		this.id = id;
		this.type = type;
		this.name = name;
		this.from_date = from_date;
		this.to_date = to_date;
		this.details = details;
		this.description = description;
		this.contact = contact;
		this.phone = phone;
		this.email = email;
		this.uri = uri;
		this.image = image;
		this.poi = new PointOfInterest(id,name, details, street, number, loc );
	}
	
	@Override
	public int getId(){
		return id;
	}

	@Override
	public Date getFromDate() {
		return from_date;
	}

	@Override
	public void setFromDate(Date date) {
		this.from_date = date;		
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDetails() {
		return details;
	}

	@Override
	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public IPointOfInterest getLocation() {
		return poi;
	}

	@Override
	public void setLocation(IPointOfInterest location) {
		this.poi = poi;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public void setType(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
		
	}

	@Override
	public Date getToDate() {
		return this.to_date;
	}

	@Override
	public void setToDate(Date date) {
		this.to_date = date;
		
	}

	@Override
	public String getContact() {
		return this.contact;
	}

	@Override
	public void setContact(String contact) {
		this.contact = contact;
	}

	@Override
	public String getPhone() {
		return this.phone;
	}

	@Override
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getUri() {
		return this.uri;
	}

	@Override
	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public String getImage() {
		return this.image;
	}

	@Override
	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String getPrices() {
		return this.prices;
	}

	@Override
	public void setPrices(String prices) {
		this.prices = prices;
	}
	
	

}
