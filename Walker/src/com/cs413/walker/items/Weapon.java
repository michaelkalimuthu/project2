package com.cs413.walker.items;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.Location;

public class Weapon extends AbstractItem {
	
	
	String name;
	Location currentLocation;
	Actor actor;
	
	public Weapon(String name, int volume){
		super(name, volume);
		
	}


	// engaging a Weapon during battle will protect a Player from death
	@Override
	public void useItem() {
	getActor().setArmed(true);
		
	}

	
	@Override
	public String toString(){
		return "Equip " + name;
	}
	
	@Override
	public int getValue(){
		
		return super.getVolume();
	}

	
}



