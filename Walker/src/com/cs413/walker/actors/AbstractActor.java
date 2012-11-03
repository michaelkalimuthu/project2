package com.cs413.walker.actors;

import com.cs413.walker.locations.Location;

public class AbstractActor{
	
	private Location location;
	
	private String name;
	
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
