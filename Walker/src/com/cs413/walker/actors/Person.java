package com.cs413.walker.actors;

import com.cs413.walker.locations.Location;

public class Person extends AbstractActor implements Actor {
	private String name;
	private Location location;
	
	public Person(String name, Location location){
		this.name = name;
		this.location = location;
	}
	
	@Override
	public void move(Location newLocation) {
		if (newLocation.canAddActor())
			this.location = newLocation;
	}

}
