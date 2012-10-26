For the modeling part of this project, I decided to have an abstract class for actors. From this class, I've made a Player class and a Creature class. The player class will be the player that the user controls. The creature class will be a non playable character and will have a defining property of "friendly". If friendly is true, this creature will be harmless. It will not be able to use the attack method from the abstract class. The Player class will be able to have items (from the Item interface). I chose an interface instead of an abstract class because all items will need to define their own methods. For now I only have three types of items; weapons, food and special items. The Weapon class will add attackPower to the Player The Food class will add health to the player. The SpecialItem class will give the Player special abilities (to be decided later based on what will eventually be assigned). The Player can pickup, drop or eat (Food) items. The Items have in common a few things. They are all marked as a specific color and they have a location. The color will be permanent, but location can change from being picked up, moved around and dropped. Players will be related to the Items because they can pick them up and use them. 

Actors will have a Location. This association is not permanent because Actors will be able to move around. For now, I have set up Location to be a set of individual Cells. Cells in view will be displayed, while Cells not in view will be hidden. The relationship between Cells is permanent because its location next to one another will not change. Each cell can contain: Actors, Items and Fixtures. Fixtures, for now, mostly are for display. I can't really assign them any properties without knowing what they are meant for. I did, however, assign them to have effects on a cell. I'm not exactly sure what these effects will be, since I'm still waiting to see what Dr. Laufer wants them to do. Each Cell also will affect the Player. For instance, Water might kill the actor (and end the game) and Swamp may stop the Player's movement. 
