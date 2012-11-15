package com.cs413.walker.actors;

import junit.framework.TestCase;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.actors.Person;
import com.cs413.walker.locations.DefaultLocation;

public class PersonTest extends TestCase {

	Actor person;
	
	protected void setUp() throws Exception {
	
		person = new Person("Hal",new DefaultLocation("home"));
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetName(){
		assertEquals("Hal",person.getName());
	}
	
}
