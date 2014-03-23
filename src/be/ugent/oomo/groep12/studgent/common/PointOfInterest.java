package be.ugent.oomo.groep12.studgent.common;

import com.google.android.gms.maps.model.LatLng;



/**
 * Generic PointOfInterest class implementing IPointOfInterest
 *
 */
public class PointOfInterest implements IPointOfInterest {
	
	protected int id;
	protected String name;
	protected String details;
	protected String street;
	protected String number;
	protected LatLng location;

	public PointOfInterest(int id, String name, String details, String street, String number, LatLng location){
		this(id, name, details, location);
		this.street = street;
		this.number = number;
	}
	

	public PointOfInterest(int id, String name, String details, LatLng location){
		this.id = id;
		this.name = name;
		this.details = details;
		this.location = location;
	}


	@Override
	public int getId() {
		return id;
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
	public String getStreet() {
		return this.street;
	}

	@Override
	public void setStreet(String street) {
		this.street = street;
	}

	@Override
	public String getNumber() {
		return this.number;
	}

	@Override
	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public LatLng getLocation() {
		return location;
	}

	@Override
	public void setLocation(LatLng location) {
		this.location = location;
	}

}
