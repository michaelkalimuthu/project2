package com.cs413.walker.locations;

public class Water extends DefaultLocation {
	@Override
	public boolean canAddActor(){
		return false;
	}

	public Water() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Water(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
