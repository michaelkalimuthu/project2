package com.cs413.walker.locations;

import java.util.ArrayList;
import java.util.HashMap;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.items.Portable;
import com.cs413.walker.items.Reward;

/***
 * Location interface that requires adding neighbors
 * @author michaelkalimuthu
 *
 */
public interface Location {
	/**
	 * add neighbor to location
	 * @param neighbor - enumerated type for cardinal directions and above/below
	 * @param location - location that will be added to the defined neighbor
	 */
	void addNeighbor(Neighbor neighbor, Location location);
	/**
	 * get a list of neighbors
	 * @return array of Location neighbors
	 */
	HashMap<Neighbor, Location> getNeighbors();
	/**
	 * Allow an actor to enter
	 */
	boolean addActor(Actor actor);
	/**
	 * Allow actor to leave
	 */
	void releaseActor();
	/**
	 * Sets neighbor to be opposite from addNeighbor's location.
	 * Called in addNeighbor.
	 * Used to automatically set the neighbor of the Location that is being added
	 * Example: Location 1 sets Location 2 as its neighbor to the NORTH. addNeighbor will
	 * then automatically set Location 1 to be SOUTH of Location 2
	 * @param location - Location that is added in addNeighbor
	 * @param opposite - Location that calls addNeighbor
	 * @param neighbor - location's Neighbor to determine opposite's Neighbor 
	 */
	void setOppositeNeighbor(Location location, Location opposite, Neighbor neighbor);
	
	boolean canAddActor();
	
	String getName();
	
	String toString();
	
	ArrayList<Portable> getItems();
	void addItem(Portable item);
	
	ArrayList<Reward> getRewards();
	void addRewards(Reward reward);
	

}
