package com.cs413.walker.actors;

import java.util.ArrayList;

import com.cs413.walker.items.Portable;
import com.cs413.walker.locations.Location;

public abstract class AbstractActor implements Actor{
	
	protected Location location;
	
	protected String name;
	
	protected int health, energy, coins, lives;
	
	protected ArrayList<Portable> items;
	
	public AbstractActor(String name, Location location, int health, int energy, int lives){
		this.name = name;
		this.location = location;
		this.health = health;
		this.energy = energy;
		this.lives = lives;
		coins = 0;
		items = new ArrayList<Portable>();
	}
	
	@Override
	public void addItems(Portable item) {
		items.add(item);
		item.setActor(this);
		
	}

	@Override
	public ArrayList<Portable> getItems() {
		return items;
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
	public void addEnergy(int addEnergy) {
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
	
	@Override
	public void useItem(Portable item){
		if (getItems().contains(item)){
			item.useItem();
			getItems().remove(item);
		}
	}
	

}
