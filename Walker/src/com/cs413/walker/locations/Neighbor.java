package com.cs413.walker.locations;

public enum Neighbor {
	NORTH, SOUTH, EAST, WEST, ABOVE, BELOW;
	
	static Neighbor getOpposite(Neighbor n){
		if (n.equals(EAST))
			return WEST;
		if (n.equals(WEST))
			return EAST;
		if (n.equals(NORTH))
			return SOUTH;
		if (n.equals(SOUTH))
			return NORTH;
		if (n.equals(ABOVE))
			return BELOW;
		if (n.equals(BELOW))
			return ABOVE;
		return n;
	}
}
