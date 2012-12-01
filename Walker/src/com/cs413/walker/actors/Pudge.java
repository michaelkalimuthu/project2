package com.cs413.walker.actors;

import java.util.HashSet;

import com.cs413.walker.items.Portable;
import com.cs413.walker.locations.Location;

public class Pudge extends AbstractMonster implements Actor {
	HashSet<ActorListener> listeners;

	public Pudge(String name, Location location, int health, int energy,
			int lives) {
		super(name, location, health, energy, lives);
		location.addActor(this);
		listeners = new HashSet<ActorListener>();
	}

	@Override
	public boolean move(Location newLocation) {
		location.releaseActor(this);
		location = newLocation;
		newLocation.addActor(this);
		for (ActorListener listener : listeners){
			listener.moved();
		}
		return true;

	}

	@Override
	public Boolean addItems(Portable item) {

		if (items.size() < capacity) {
			items.add(item);
			item.setActor(this);
			for (ActorListener listener : listeners) {
				listener.pickedUpItem();
			}
			return true;
		}
		return false;

	}

	@Override
	public void useItem(Portable item) {
		super.useItem(item);
		for (ActorListener listener : listeners) {
			listener.pickedUpItem();
		}
	}

	@Override
	public void addListeners(ActorListener listener) {
		listeners.add(listener);
	}

}
