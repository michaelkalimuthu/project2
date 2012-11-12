package com.cs413.walker.view;

import com.cs413.walker.locations.Location;

public class GridCell {
	private int left;
	private int right;
	private int bottom;
	private int top;
	
	private Location location;
	
	public GridCell(int left, int right, int bottom, int top) {
		this.setLeft(left);
		this.setRight(right);
		this.setBottom(bottom);
		this.setTop(top);
		//this.setLocation(location);
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
