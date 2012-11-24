package com.cs413.walker.items;

public class Coin implements Reward {
	int value;
	
	public Coin(int value){
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
	@Override
	public String toString(){
		return "Coin " + getValue();
	}



}
