package com.cs413.walker.actors;

import java.util.ArrayList;

import com.cs413.walker.items.Coin;
import com.cs413.walker.items.Portable;
import com.cs413.walker.items.Weapon;
import com.cs413.walker.locations.Location;

public class Person extends AbstractActor implements Actor {
	ArrayList<ActorListener> listeners;

	int initEnergy;
	int initHealth;

	public Person(String name, Location location, int health, int energy,
			int lives, int capacity, int rate) {
		super(name, location, health, energy, lives, capacity, rate);
		initEnergy = energy;
		initHealth = health;
		listeners = new ArrayList<ActorListener>();
	}

	@Override
	public boolean move(Location newLocation) {
		if (newLocation.canAddActor() && (getEnergy() - rate) >= 0) {
			location = newLocation;
			energy -= 1 * rate;

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
	public Boolean addItems(ArrayList<Portable> list) {
		for (Portable item : list) {
			if (item instanceof Coin) {
				addCoins(item.getValue());
				getLocation().getItems().remove(item);
			} else if (item instanceof Weapon) {
				addDamagePoints(item.getValue());
				getLocation().getItems().remove(item);
				additionalCapacity += item.getVolume();
			}

			else if (getCurrentCapacity() + item.getVolume() <= getCapacity()) {
				items.add(item);
				item.setActor(this);
				getLocation().getItems().remove(item);
			}
		}
		for (ActorListener listener : listeners) {
			listener.pickedUpItem();
		}
		return true;

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
	public void addCoins(int addCoins) {
		super.addCoins(addCoins);
		for (ActorListener listener : listeners) {
			listener.pickedUpItem();
		}
	}

	@Override
	public void removeListeners(ActorListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}

	}

	@Override
	public void attacked(int damage) {
		super.attacked(damage);
		if (health <= 0 && lives > 0) {
			health = initHealth;
			lives -= 1;
		} else if (health <= 0 && lives == 0) {
			health = 0;
			for (ActorListener listener : listeners) {
				listener.death();
			}
		}
	}

}
