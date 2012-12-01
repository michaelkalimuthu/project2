package com.cs413.walker.actors;

import java.util.ArrayList;

import com.cs413.walker.items.Portable;
import com.cs413.walker.locations.Location;

/***
 * Actor interface. Includes movement of actors
 * 
 * @author michaelkalimuthu
 * 
 */
public interface Actor {
	/**
	 * Moves actor from one cell to another
	 * 
	 * @return
	 */
	public boolean move(Location newLocation);

	/**
	 * returns actor's Location
	 */
	public Location getLocation();

	@Override
	String toString();

	public String getName();

	public int getLives();

	public int getHealth();

	public int getEnergy();

	public int getCoins();

	public int getArmor();

	public int getDamage();

	public int getCapacity();

	public void addCoins(int addCoins);

	public void addHealth(int addHealth);

	public void addEnergy(int addEnergy);

	public Boolean addItems(Portable item);

	public void useItem(Portable item);

	public ArrayList<Portable> getItems();

	public void addListeners(ActorListener listener);

	public int attack(Actor actor);

}
