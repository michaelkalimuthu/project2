package com.cs413.walker.actors;

import java.util.HashSet;

import com.cs413.walker.items.Portable;
import com.cs413.walker.locations.Location;

public class Person extends AbstractActor implements Actor {
	HashSet<PersonListener> listeners;
	
	public Person(String name, Location location, int health, int energy, int lives){
		super(name, location, health, energy, lives);
		listeners = new HashSet<PersonListener>(); 
	}
	
	@Override
	public void move(Location newLocation) {
		if (newLocation.canAddActor() && energy > 0){
			location = newLocation;
			energy -= 1;
			for (PersonListener listener : listeners){
				listener.moved();
			}
		}
	}
	
	public String getName(){
		return super.getName();
	}
	@Override
	public void addItems(Portable item) {
		super.addItems(item);
		for (PersonListener listener : listeners){
			listener.pickedUpItem();
		}	
	}
	@Override
	public void useItem(Portable item){
		super.useItem(item);
		for (PersonListener listener : listeners){
			listener.pickedUpItem();
		}
	}
	
	
	public void addListeners(PersonListener listener){
		listeners.add(listener);
	}

}
