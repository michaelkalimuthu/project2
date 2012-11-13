package com.cs413.walker.actors;

import com.cs413.walker.locations.Location;

public class Person extends AbstractActor implements Actor {

	
	public Person(String name, Location location){
		super(name, location);
	}
	
	@Override
	public void move(Location newLocation) {
		if (newLocation.canAddActor())
			location = newLocation;
	}
	
	public String getName(){
		return super.getName();
	}

}
