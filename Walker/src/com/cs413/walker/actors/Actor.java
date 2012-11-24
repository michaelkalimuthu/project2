package com.cs413.walker.actors;

import java.util.ArrayList;

import com.cs413.walker.items.Portable;
import com.cs413.walker.locations.Location;

/***
 * Actor interface. Includes movement of actors
 * @author michaelkalimuthu
 *
 */
public interface Actor {
	/**
	 * Moves actor from one cell to another
	 */
	public void move(Location newLocation);
	
	/**
	 * returns actor's Location
	 */
	public Location getLocation();
	
	String toString();
	
	public String getName();
	
	public int getHealth();
	public int getEnergy();
	public int getCoins();
	
	public void addCoins(int addCoins);
	public void addHealth(int addHealth);
	public void addEnergy(int addEnergy);
	
	public void addItems(Portable item);
	public void useItem(Portable item);
	public ArrayList<Portable> getItems();
	
	public void addListeners(PersonListener listener);

}
