package com.cs413.walker.locations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.items.Portable;

/***
 * This class will be used to create locations for the terrain of the 
 * main application
 * @author michaelkalimuthu
 *
 */
public class DefaultLocation implements Location{
	/*
	 * This name will be unique. Most likely will name all locations with after a number
	 * to keep track of the terrain easier.
	 */
	String name;
	HashMap<Neighbor, Location> map;
	List<Actor> actors;
	LocationAbility ability;
	ArrayList<Portable> items;
	
	int tile;
	
	/**
	 * Default constructor. Names the location randomly after a number through 0 and 50000.
	 */
	public DefaultLocation(){
		name = Integer.toString( (int) (Math.random() * 50000));
		map = new HashMap<Neighbor, Location>();
		actors = new ArrayList<Actor>();
		items = new ArrayList<Portable>();
	}
	/**
	 * Creates a location
	 * @param name
	 */
	public DefaultLocation(String name){
		this.name = name;
		map = new HashMap<Neighbor, Location>();
		actors = new ArrayList<Actor>();
		items = new ArrayList<Portable>();
	}
	
	@Override
	public void addNeighbor(Neighbor neighbor, Location location) {
		if (!map.containsKey(neighbor)){
			map.put(neighbor, location);
			setOppositeNeighbor(location, this, neighbor);
		}
	}

	@Override
	public HashMap<Neighbor, Location> getNeighbors() {		
		return map;
	}

	@Override
	public boolean addActor(Actor actor) {
		if (canAddActor()){
			actors.add(actor);
			return true;
		} return false;
	}
	@Override
	public String getName(){
		return name;
	}
	
	@Override
	public boolean canAddActor(){
		return true;
	}

	@Override
	public void releaseActor() {
		if (actors.contains(this) && ability.isExitLocation()){
			actors.remove(this);
		}
		
	}
	
	@Override
	public void setOppositeNeighbor(Location location, Location opposite, Neighbor neighbor){
		location.getNeighbors().put(Neighbor.getOpposite(neighbor), opposite);
	}
	
	@Override
	public String toString(){
		return this.name;
	}
	@Override
	public ArrayList<Portable> getItems() {
		return items;
	}
	@Override
	public void addItem(Portable item) {
		items.add(item);	
	}

	

}
