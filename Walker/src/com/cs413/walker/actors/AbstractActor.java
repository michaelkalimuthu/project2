package com.cs413.walker.actors;

import com.cs413.walker.locations.Location;

public abstract class AbstractActor implements Actor{
	
	protected Location location;
	
	protected String name;
	
	protected int health, energy, coins;
	
	public AbstractActor(String name, Location location, int health, int energy){
		this.name = name;
		this.location = location;
		this.health = health;
		this.energy = energy;
		coins = 0;
	}
	
	@Override
	public int getCoins() {
		return coins;
	}

	@Override
	public void addCoins(int addCoins) {
		coins += addCoins;
	}

	@Override
	public void addHealth(int addHealth) {
		health += addHealth;
	}

	@Override
	public void addEnegery(int addEnergy) {
		energy += addEnergy;
	}

	@Override
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
	
	@Override
	public String toString(){
		return this.name;
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public int getEnergy() {
		return energy;
	}
	
	

}
