package com.cs413.walker.items;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.Location;

public class Weapon extends AbstractItem {
	
	
	String name;
	Location currentLocation;
	Actor actor;
	int value;
	
	public Weapon(String name, int volume, int value){
		super(name, volume);
		this.value = value;
		
	}

	// picking up a Weapon adds it to inventory which will
	// protect a Player from death when encountered by Monster
	@Override
	public void useItem() {
	getActor().setArmed(true);
		
	}

	
	@Override
	public String toString(){
		return "Equip " + super.toString() + value;
	}
	
	@Override
	public int getValue(){
		
		return super.getVolume();
	}

	
}



