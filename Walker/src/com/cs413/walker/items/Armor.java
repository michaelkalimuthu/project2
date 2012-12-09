package com.cs413.walker.items;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.Location;

/*
 * Armor class contains method for actor to add health after using armor item
 */

public class Armor extends AbstractItem{
	
	private int addHealth;
	String name;
	int value;
	Location currentLocation;
	Actor actor;
	
	public Armor(int addHealth, String name, int volume){
		super(name, volume);
		this.addHealth = addHealth;
		value = addHealth;
	}


	// using Armor adds to an actor's health attribute
	@Override
	public void useItem() {
		getActor().addHealth(addHealth);
	}

	
	@Override
	public String toString(){
		return super.toString() + addHealth;
	}
	@Override
	public int getValue(){
		return addHealth;
	}

}
