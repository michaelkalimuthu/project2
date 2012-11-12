package com.cs413.walker.actors;

import com.cs413.walker.locations.Location;

/***
 * Actor interface. Includes movement of actors
 * @author michaelkalimuthu
 *
 */
public interface Actor {
	/**
	 * Moves actor from one cell to another
	 */
	public void move(Location newLocation);
	
	/**
	 * returns actor's Location
	 */
	public Location getLocation();
	
	String toString();

}
