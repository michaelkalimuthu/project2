package com.cs413.walker.items;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.Location;

public class Food implements Portable {
	private int addHealth;
	String name;
	Location currentLocation;
	Actor actor;
	
	public Food(int addHealth, Location currentLocation){
		this.addHealth = addHealth;
		this.currentLocation = currentLocation;
	}


	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void useItem() {
		getActor().addHealth(addHealth);
	}

	@Override
	public void dropItem(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Actor getActor() {
		return actor;
	}

	@Override
	public void setActor(Actor actor) {
		this.actor = actor;
		
	}


	@Override
	public void pickUp(Actor actor) {
		setActor(actor);
		
	}

}
