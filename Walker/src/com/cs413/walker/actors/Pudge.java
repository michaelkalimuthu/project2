package com.cs413.walker.actors;

import java.util.HashSet;

import com.cs413.walker.items.Portable;
import com.cs413.walker.locations.Location;

public class Pudge extends AbstractMonster implements Actor {
	HashSet<PersonListener> listeners;

	public Pudge(String name, Location location, int health, int energy,
			int lives) {
		super(name, location, health, energy, lives);
		listeners = new HashSet<PersonListener>();
	}

	@Override
	public boolean move(Location newLocation) {
		return false;

	}

	@Override
	public void addItems(Portable item) {
		super.addItems(item);
		for (PersonListener listener : listeners) {
			listener.pickedUpItem();
		}
	}

	@Override
	public void useItem(Portable item) {
		super.useItem(item);
		for (PersonListener listener : listeners) {
			listener.pickedUpItem();
		}
	}

	@Override
	public void addListeners(PersonListener listener) {
		listeners.add(listener);
	}
	

	

}
