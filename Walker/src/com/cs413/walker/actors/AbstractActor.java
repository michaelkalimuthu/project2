package com.cs413.walker.actors;

import com.cs413.walker.locations.Location;

public abstract class AbstractActor{
	
	protected Location location;
	
	protected String name;
	
	public AbstractActor(String name, Location location){
		this.name = name;
		this.location = location;
	}
	
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public Location getLocation(){
		return location;
	}
	public void setLocation(Location location){
		this.location = location;
	}
	
	

}
