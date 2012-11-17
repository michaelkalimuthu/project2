package com.cs413.walker.items;

import com.cs413.walker.locations.Location;

/**
 * A interface for portable items
 * 
 * @author shell
 */
public interface Portable {
	void setName(String name);

	String getName();

	void useItem(Location location);

	void dropItem(Location location);
}
