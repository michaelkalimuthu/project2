package com.cs413.walker.actors;

import java.util.ArrayList;
import java.util.HashSet;

import com.cs413.walker.items.Portable;
import com.cs413.walker.locations.Location;

public class Pudge extends AbstractMonster implements Actor {
	HashSet<ActorListener> listeners;

	public Pudge(String name, Location location, int health, int damage) {
		super(name, location, health, damage);
		location.addActor(this);
		listeners = new HashSet<ActorListener>();
	}

	@Override
	public boolean move(Location newLocation) {
		location.releaseActor(this);
		location = newLocation;
		newLocation.addActor(this);
		for (ActorListener listener : listeners) {
			listener.moved();
		}
		return true;

	}

	@Override
	public Boolean addItems(ArrayList<Portable> list) {

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

	@Override
	public int getRate() {
		// TODO Auto-generated method stub
		return 0;
	}
	

	@Override
	public void removeListeners(ActorListener listener) {
		if (listeners.contains(listener)){
			listeners.remove(listener);
		}
		
	}
	
	@Override
	public void attacked(int damage){
		super.attacked(damage);
		if (health <= 0){
			health = 0;
			for (ActorListener listener : listeners) {
				listener.death();
			}
		}
	}

}
