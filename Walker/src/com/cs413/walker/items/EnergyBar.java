package com.cs413.walker.items;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.Location;

public class EnergyBar extends AbstractItem {
	private int addEnergy;
	String name;
	Location currentLocation;
	Actor actor;
	
	public EnergyBar(int addEnergy, String name, int volume){
		super(name,volume);
		this.addEnergy = addEnergy;
		
	}

	@Override
	public void useItem() {
		getActor().addEnergy(addEnergy);
	}

	
	@Override
	public String toString(){
		return super.toString() + " " + addEnergy;
	}
	@Override
	public int getValue(){
		return addEnergy;
	}

}
