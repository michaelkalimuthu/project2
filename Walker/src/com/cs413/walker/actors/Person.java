package com.cs413.walker.actors;

import java.util.HashSet;

import com.cs413.walker.items.Portable;
import com.cs413.walker.locations.Location;

public class Person extends AbstractActor implements Actor {
	HashSet<ActorListener> listeners;

	public Person(String name, Location location, int health, int energy,
			int lives, int capacity) {
		super(name, location, health, energy, lives, capacity);
		listeners = new HashSet<ActorListener>();
	}

	@Override
	public boolean move(Location newLocation) {
		if (newLocation.canAddActor() && energy > 0) {
			location = newLocation;
			energy -= 1;
			for (ActorListener listener : listeners) {
				listener.moved();
			}
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return super.getName();
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
