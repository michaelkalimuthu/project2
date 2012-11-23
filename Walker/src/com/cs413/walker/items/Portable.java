package com.cs413.walker.items;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.Location;

/**
 * A interface for portable items
 * 
 * @author shell
 */
public interface Portable {
	void setName(String name);

	String getName();

	void useItem();
	void pickUp(Actor actor);
	void dropItem(Location location);
	
	Actor getActor();
	void setActor(Actor actor);
	
}
