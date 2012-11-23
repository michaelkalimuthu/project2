package com.cs413.walker.items;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.Location;

public class EnergyBar implements Portable {
	private int addEnergy;
	String name;
	Location currentLocation;
	Actor actor;
	
	public EnergyBar(int addEnergy, Location currentLocation){
		this.addEnergy = addEnergy;
		this.currentLocation = currentLocation;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void useItem() {
		getActor().addEnergy(addEnergy);
	}

	@Override
	public void pickUp(Actor actor) {
		setActor(actor);
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

}
