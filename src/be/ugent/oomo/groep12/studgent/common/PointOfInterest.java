package be.ugent.oomo.groep12.studgent.common;

import android.location.Location;

/**
 * Generic PointOfInterest class implementing IPointOfInterest
 *
 */
public class PointOfInterest implements IPointOfInterest {
	
	protected int id;
	protected String name;
	protected String details;
	protected Location location;

	public PointOfInterest(int id, String name, String details, Location location){
		this.id = id;
		this.name = name;
		this.details = details;
		this.location = location;
	}

	public PointOfInterest(int id, String name, Location location){
		this.id = id;
		this.name = name;
		this.location = location;
	}

	public PointOfInterest(Location location){
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
	public Location getLocation() {
		return location;
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
	}

}
