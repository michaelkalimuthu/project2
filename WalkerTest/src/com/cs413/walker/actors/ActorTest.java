package com.cs413.walker.actors;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.actors.Person;
import com.cs413.walker.locations.DefaultLocation;
import com.cs413.walker.locations.Water;

public class ActorTest extends TestCase{
	
	Actor actor;
	

	@Before
	public void setUp() throws Exception {
		actor = new Person("name", new DefaultLocation("location"));
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	
	public void testMove(){
		actor.move(new DefaultLocation("newLocation"));
		assertNotSame("location", actor.getLocation().getName());
		assertEquals("newLocation", actor.getLocation().getName());
		actor.move(new Water("waterLocation"));
		assertNotSame("waterLocation", actor.getLocation().getName());
		assertEquals("newLocation", actor.getLocation().getName());
		assertEquals("name", actor.getName());
	}

}
