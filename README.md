Project 2b Writeup
-------------------------

Group Members:

Mike Kalimuthu
Ron Giambalvo
Shell Yan

Extra Credit Features:

1) Vibrations onTouch for accessible locations only. 

2) Sound effects play based on player
actions: 
	* Footsteps upon movement to accessible cell * Elevator upon movement up or down
	levels
	* Up & Down buttons light up based on player's current level location

3) Player Stats Box appears near the top of the screen which displays important player stats.
This will be more important for the next iteration of the app for project 2c.

Design Decisions
--------------------------

We made the deliberate decision to not include 'Up' & 'Down' menus, and consolidate these
into 1 menu at the bottom of the screen. This decision was made because with such limited
screen space on a mobile device, it made little sense to take up that space with 2 menus
when 1 would suffice.

We did not include a menu for restarting or quitting the game, although this will be an
option in the next iteration of the app in project 2c. This was discussed and ultimately
the group decided that without an objective to the game yet, there was little need to
allow a player to save their progress (current location).

Single responsibility principle:

During the design phase of the project, we followed the guidelines very carefully to
encapsulate the attributes and behaviors of the key components of the app (Actors, Locations and Items.
This pattern simplified the development process because we are able to write each class
to perform what the actual instances of the class will "do" in the app without making any 
1 class to large with broad behaviors.

For instance, an Actor has a narrowly aligned behavior of moving into and out of rooms on 
each level. The Actor  is not responsible for much more than its location on a level, with
the formation of Locations into levels is handled by other classes.


Interface segregation principle & Factoring Out:

We observed these principle by ensuring that no interface in the app was too large and that
all of the interface method stubs are "of interest" to the classes that implement them, i.e.
can be implemented by many classes. These two principles greatly benefited us during the 
development process because we will be able to utilize these interfaces in future iterations
of the app without having to re-code much if any at all.

For instance, the Actor interface is succinct enough to be used by both Player classes and 
(soon to be used in the next project) Enemy classes because both Players and Enemies will 
have the ability to move around the level, whereas if a larger more-broad interface was created
with other method stubs such as attach and defend, we could not utilize the interface for 
an Actor such as a harmless creature who will never attack. 

Singleton:

We used the Singleton principle with our Neighbor class because we wanted 
and instance to be available to whole application, but limite the instance to 1.
This made sense when we wanted a particular location's opposite location. We used this 
to map the relationships between each location and create full levels in a grid. This helped us
during the development process since we had originally planned on defining relationships 
between neighboring location cells via numbers.

We had a unique situation in that two of the group members joined with a third member after their
partner dropped the class. While we both had similar designs in mind, and had completed 
project 2a similarly, we had to spend time merging the two designs into one. Once everyone was
on the same page, we chose to utilize the XP practice of pair programming. We met outside of 
class and took turns coding the project while the others wrote on the whiteboard, researched
online for how to implement various elements into the app, etc. Once the class hierarchy was 
establish, the majority of the time was spent working on the views.

We spent considerable time drawing out the components of the app. In particular, the terrain view
was particularly difficult to create, and we tried to conceptualize it while drawing 
on whiteboards. After we had a barebones version of the app functioning, we used less 
diagraming and more coding and running the app. This was helpful because debugging the app
on our mobile devices actually lead to many ideas (some we submitted for extra credit)
that we incorporated into the overall design. Ex: After testing the app by moving the player
from one cell to another, we felt there was a need for something to signal to the player
he had moved into a new room other than by a text notification. We decided vibrations would
be a good addition. 

How to Balance Backend vs. Frontend Work:

We avoided the issue of backend vs. frontend (one being more gratifying to work on for some)
by working on this as a group in person rather than delegate particular parts to certain members. The test could and should have been written 
first, but due to our group circumstances in that we formed only weeks ago, it was difficult 
to have the same test cases work with our newly merged code. We did write passing tests however,
but just not early enough in the process.



--------project2a uml------
For the modeling part of this project, I decided to have an abstract class for actors. From this class, I've made a Player class and a Creature class. The player class will be the player that the user controls. The creature class will be a non playable character and will have a defining property of "friendly". If friendly is true, this creature will be harmless. It will not be able to use the attack method from the abstract class. The Player class will be able to have items (from the Item interface). I chose an interface instead of an abstract class because all items will need to define their own methods. For now I only have three types of items; weapons, food and special items. The Weapon class will add attackPower to the Player The Food class will add health to the player. The SpecialItem class will give the Player special abilities (to be decided later based on what will eventually be assigned). The Player can pickup, drop or eat (Food) items. The Items have in common a few things. They are all marked as a specific color and they have a location. The color will be permanent, but location can change from being picked up, moved around and dropped. Players will be related to the Items because they can pick them up and use them. 

Actors will have a Location. This association is not permanent because Actors will be able to move around. For now, I have set up Location to be a set of individual Cells. Cells in view will be displayed, while Cells not in view will be hidden. The relationship between Cells is permanent because its location next to one another will not change. Each cell can contain: Actors, Items and Fixtures. Fixtures, for now, mostly are for display. I can't really assign them any properties without knowing what they are meant for. I did, however, assign them to have effects on a cell. I'm not exactly sure what these effects will be, since I'm still waiting to see what Dr. Laufer wants them to do. Each Cell also will affect the Player. For instance, Water might kill the actor (and end the game) and Swamp may stop the Player's movement. 

