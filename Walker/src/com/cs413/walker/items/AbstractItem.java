package com.cs413.walker.items;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.Location;

public abstract class AbstractItem implements Portable {
	String name;
	Actor actor;
	int volume; // the amount of space an item fills
	
	public AbstractItem(String name, int volume){
		this.name = name;
		this.volume = volume;
	}
	
	public int getVolume(){
		return volume;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	
	public void useItem() {}

	@Override
	public void dropItem(Location location) {
		
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
	@Override
	public String toString(){
		return getName() + " weight=" + volume + ": +";
	}


}
