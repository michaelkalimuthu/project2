package com.cs413.walker.items;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.Location;

public class Coin extends AbstractItem {
	int value;
	String name;
	int volume;
	
	public Coin(int value){
		super("Coin", 0);
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
	@Override
	public String toString(){
		return getName() + " x" + getValue();
	}

	@Override
	public int getVolume() {
		return volume;
	}



}
