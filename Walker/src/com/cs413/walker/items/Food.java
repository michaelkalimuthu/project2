package com.cs413.walker.items;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.Location;

public class Food extends AbstractItem {
	private int addHealth;
	String name;
	Location currentLocation;
	Actor actor;
	
	public Food(int addHealth, String name, int volume){
		super(name, volume);
		this.addHealth = addHealth;
	}


	@Override
	public void useItem() {
		getActor().addHealth(addHealth);
	}
	@Override
	public int getValue(){
		return addHealth;
	}
	
	@Override
	public String toString(){
		return super.toString() + " " + addHealth;
	}

}
