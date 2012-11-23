package com.cs413.walker.items;

import java.util.ArrayList;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.DefaultLocation;
import com.cs413.walker.locations.Location;

/**
 * Breadcrumb class will be used in future iterations of application for in-game
 * items
 * 
 * @author rgiambalvo
 * 
 */
public class Breadcrumb implements Portable {
	String name;
	Location currentLocation;
	ArrayList<Location> locationHolder;

	/**
	 * Default constructor.
	 */
	public Breadcrumb() {
		name = Integer.toString((int) (Math.random() * 50000));
		currentLocation = new DefaultLocation();

	}

	/**
	 * Creates a Breadcrumb
	 * 
	 * @param name
	 */
	public Breadcrumb(String name, Location location) {
		this.name = name;
		currentLocation = location;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void useItem() {
	//	currentLocation = location;
	//	locationHolder.add(location);
		drawDots();
		// TODO Auto-generated method stub

	}

	@Override
	public void dropItem(Location location) {
		currentLocation = location;

		// TODO Auto-generated method stub

	}

	public void drawDots() {

	}

	@Override
	public Actor getActor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setActor(Actor actor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pickUp(Actor actor) {
		// TODO Auto-generated method stub
		
	}

}
