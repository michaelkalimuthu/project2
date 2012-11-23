package com.cs413.walker.actors;

import com.cs413.walker.locations.Location;

public class Person extends AbstractActor implements Actor {

	
	public Person(String name, Location location, int health, int energy){
		super(name, location, health, energy);
	}
	
	@Override
	public void move(Location newLocation) {
		if (newLocation.canAddActor() && energy > 0){
			location = newLocation;
			energy -= 1;
		}
	}
	
	public String getName(){
		return super.getName();
	}

}
