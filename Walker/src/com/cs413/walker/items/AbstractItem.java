package com.cs413.walker.items;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.Location;

public abstract class AbstractItem implements Portable {
	String name;
	Actor actor;
	
	public AbstractItem(String name){
		this.name = name;
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
		return getName() ;
	}

}
