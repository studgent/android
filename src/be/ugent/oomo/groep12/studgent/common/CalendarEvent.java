package be.ugent.oomo.groep12.studgent.common;

import java.util.Date;

import android.location.Location;

/**
 * Generic Event class implementing IEvent
 *
 */
public class CalendarEvent implements ICalendarEvent {
	protected int id;
	protected String name;
	protected Date date;
	protected String details;
	protected IPointOfInterest poi;

	public CalendarEvent(int id, String name, Date date){
		this.id = id;
		this.name = name;
		this.date = date;
	}
	public CalendarEvent(int id, String name, Date date, IPointOfInterest poi){
		this.id = id;
		this.name = name;
		this.date = date;
		this.poi = poi;
	}
	
	@Override
	public int getId(){
		return id;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public void setDate(Date date) {
		this.date = date;		
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
	
	

}
