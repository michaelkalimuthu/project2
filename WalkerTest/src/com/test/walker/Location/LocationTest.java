/**
 * 
 */
package com.test.walker.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import com.cs413.walker.locations.DefaultLocation;
import com.cs413.walker.locations.Location;
import com.cs413.walker.locations.Neighbor;

import junit.framework.TestCase;

/**
 * @author michaelkalimuthu
 *
 */
public class LocationTest extends TestCase {
	Location center, north, south, east, west, above, below;



	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		center = new DefaultLocation("0");
		north = new DefaultLocation("1");
		south = new DefaultLocation("2");
		east = new DefaultLocation("3");
		west = new DefaultLocation("4");
		above = new DefaultLocation("5");
		below = new DefaultLocation("6");
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	HashMap<Neighbor, Location> getDefaultMap(){
		HashMap map = new HashMap<Neighbor, Location>();
		map.put(Neighbor.NORTH, north);
		map.put(Neighbor.SOUTH, south);
		map.put(Neighbor.EAST, east);
		map.put(Neighbor.WEST, west);
		map.put(Neighbor.ABOVE, above);
		map.put(Neighbor.BELOW, below);
		
		return map;
	}
	
	public void testAddNeighbor(){
		center.addNeighbor(Neighbor.NORTH, north);
		center.addNeighbor(Neighbor.SOUTH, south);
		center.addNeighbor(Neighbor.EAST, east);
		center.addNeighbor(Neighbor.WEST, west);
		center.addNeighbor(Neighbor.ABOVE, above);
		center.addNeighbor(Neighbor.BELOW, below);
		
		
		
		HashMap<Neighbor, Location> centerDefaultMap = getDefaultMap();
		
		
		assertEquals(centerDefaultMap, center.getNeighbors());
		
		HashMap<Neighbor, Location> southMap = south.getNeighbors();
		
		assertEquals(center, southMap.get(Neighbor.NORTH));
		
		Location incompatible = new DefaultLocation("7");
		
		center.addNeighbor(Neighbor.NORTH, incompatible);
		
		assertEquals(north, center.getNeighbors().get(Neighbor.NORTH));
	}

}
