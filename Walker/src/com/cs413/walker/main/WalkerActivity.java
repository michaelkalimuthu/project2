package com.cs413.walker.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.cs413.walker.actors.AbstractMonster;
import com.cs413.walker.actors.Actor;
import com.cs413.walker.actors.ActorListener;
import com.cs413.walker.actors.Person;
import com.cs413.walker.actors.Pudge;
import com.cs413.walker.items.Armor;
import com.cs413.walker.items.Coin;
import com.cs413.walker.items.Energy;
import com.cs413.walker.items.Food;
import com.cs413.walker.items.Portable;
import com.cs413.walker.items.Weapon;
import com.cs413.walker.locations.DefaultLocation;
import com.cs413.walker.locations.Location;
import com.cs413.walker.locations.Neighbor;
import com.cs413.walker.locations.Water;
import com.cs413.walker.view.GridCell;
import com.cs413.walker.view.TerrainView;
import com.example.walker.R;

public class WalkerActivity extends Activity {
	private static final String TAG = "Activity";

	//These are the game's attributes which are configurable at startup screen via a menu
	private static int INIT_HEALTH = 100;
	private static int INIT_ENERGY = 10;
	private static int INIT_LIVES = 1;
	private static int INIT_CAPACITY = 1;
	private static int INIT_RATE;
	private static String PLAY_NAME = "";
	
	HashMap<Integer, ArrayList<Location>> levels;
	ArrayList<Location> one;
	ArrayList<Location> two;
	ArrayList<Location> three;
	ArrayList<Location> four;

	HashMap<Integer, GridCell> gridMap;
	ArrayList<Integer> movingOptions;
	HashMap<Integer, Actor> monsters;
	ArrayList<Actor> levelOne;

	CountDownTimer movingTimer;
	ActorListener personListener, monsterListener, chaseListener;

	Actor player, monster, monster2, boss;
	AbstractMonster tempMonster;

	SoundPool sp;
	int footsteps;
	int elevator;
	int growl;
	
	Location winningLocation;

	@Override
	public void onBackPressed() {
		// super.onBackPresse ();
		final Intent MainMenuActivity = new Intent(getApplicationContext(),
				MainMenuActivity.class);

		MainMenuActivity.putExtra("GAME_IS_ON", 1);
		// setResult(RESULT_OK, i);
		startActivityForResult(MainMenuActivity, 0);
		// finish();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();

		INIT_ENERGY = i.getIntExtra("energy", 1);
		INIT_CAPACITY = i.getIntExtra("inventory", 1);
		INIT_LIVES = i.getIntExtra("lives", 1);
		INIT_RATE = i.getIntExtra("difficulty", 1);
		PLAY_NAME = i.getExtras().getString("playername");
		// Example of receiving parameter having key value as 'email'
		// and storing the value in a variable named myemail
		// String myemail = i.getStringExtra("email");

		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		one = new ArrayList<Location>(); // level one
		two = new ArrayList<Location>(); // level two
		three = new ArrayList<Location>(); //level three
		four = new ArrayList<Location>(); //level four
		levels = new HashMap<Integer, ArrayList<Location>>();
		
		

		monsters = new HashMap<Integer, Actor>();

		levelOne = new ArrayList<Actor>();

		gridMap = new HashMap<Integer, GridCell>();
		movingOptions = new ArrayList<Integer>();

		setUpGame();

		// SoundPool object allows up to 3 simultaneous sounds to be played where 
		// 0 represents normal audio quality.
		sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		footsteps = sp.load(this, R.raw.footsteps, 1); // links footsteps
		// variable to audio clip in raw folder
		elevator = sp.load(this, R.raw.elevator, 1);
		growl = sp.load(this, R.raw.growl, 1);

		setContentView(R.layout.activity_walker);

		final TerrainView view = new TerrainView(this, levels, player);
		personListener = new ActorListener() {

			@Override
			public void pickedUpItem() {
				view.invalidate();
			}

			@Override
			public void death() {
				view.alert("dead");
			}
 
			@Override
			public void moved() {
				boolean won = false;
				if (player.getLocation().equals(winningLocation)){
					won = true;
					AlertDialog.Builder alert = new AlertDialog.Builder(
							view.getContext());
					alert.setTitle("You have won! You collected " + player.getCoins() + " coins!");
					alert.setCancelable(false).setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									final Intent MainMenuActivity = new Intent(
											getApplicationContext(),
											MainMenuActivity.class);

									MainMenuActivity.putExtra("GAME_IS_ON",
											0);
									startActivityForResult(
											MainMenuActivity, 0);
								}
							});
					alert.show();
								
				}
				if (player.getEnergy() == 0 || player.getHealth() == 0
						|| player.getEnergy() < player.getRate()) {
					if (player.getLives() - 1 > 0) {
						// create a new intent with minus one life
						AlertDialog.Builder alert = new AlertDialog.Builder(
								view.getContext());
						alert.setTitle("You are dead!");
						alert.setCancelable(false).setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										Intent walker_activity = getIntent();
										finish();
										walker_activity.putExtra("difficulty",
												player.getRate());
										walker_activity.putExtra("lives",
												player.getLives() - 1);
										walker_activity.putExtra("energy",
												INIT_ENERGY);
										walker_activity.putExtra("inventory",
												INIT_CAPACITY);
										walker_activity.putExtra("playername",
												player.getName().toString());

										startActivity(walker_activity);
									}
								});
						alert.show();
					} else {
						AlertDialog.Builder alert = new AlertDialog.Builder(
								view.getContext());
						alert.setTitle("Game Over.");
						alert.setCancelable(false).setPositiveButton(
								"Return to Main Menu",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										final Intent MainMenuActivity = new Intent(
												getApplicationContext(),
												MainMenuActivity.class);

										MainMenuActivity.putExtra("GAME_IS_ON",
												0);
										startActivityForResult(
												MainMenuActivity, 0);
									}
								});
						alert.show();

					}
				}
				if (monsters.get(view.getLevel()) != null
						&& !((AbstractMonster) monsters.get(view.getLevel()))
								.isChasing()) {
					chase(view);
				}
				view.invalidate(); // always invalidate when player moves

				if (player.getLocation().getItems().size() > 0) {
					itemsPresent(view);
				}

			}

		};
		player.addListeners(personListener);

		monsterListener = new ActorListener() {

			@Override
			public void pickedUpItem() {
				// TODO Auto-generated method stub

			}

			//Called when player kills monster, which removes monster instance from the game
			@Override
			public void death() {

				monsters.remove(monsters.get(view.getLevel()));
				view.invalidate();

				player.removeListeners(chaseListener);
				view.alert("You've killed the monster!");
				player.getLocation().getActors().clear();

			}

			@Override
			public void moved() {
				// TerrainView.monsterLocation =
				// monster.getLocation().getName();
				// view.invalidate();
				if (monsters.get(view.getLevel()) != null
						&& !((AbstractMonster) monsters.get(view.getLevel()))
								.isChasing()) {
					chase(view);
				}

			}

		};
		monster.addListeners(monsterListener);
		monster2.addListeners(monsterListener);
		boss.addListeners(monsterListener);
		

		chaseListener = new ActorListener() {
			@Override
			public void pickedUpItem() {
				// TODO Auto-generated method stub

			}

			@Override
			public void death() {
				// todo
			}

			@Override
			public void moved() {
				monsters.get(view.getLevel()).move(player.getLocation());
				monsters.get(view.getLevel()).attack(player);
				player.attack(monsters.get(view.getLevel()));

			}

		};

		OnTouchListener listener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					float clickX = event.getX();
					float clickY = event.getY();
					gridMap = view.getGridMap(); // get the grid
					movingOptions = view.getMovingOptions(); // see available spaces to move

					GridCell down = gridMap.get(TerrainView.DOWN); // get down
					// button
					GridCell up = gridMap.get(TerrainView.UP); // get up button
					GridCell inventory = gridMap.get(TerrainView.INVENTORY);

					if (downButton(view, clickX, clickY, down)) {
						if (checkToRemoveMonster(player.getLocation()
								.getNeighbors().get(Neighbor.BELOW))) {
							player.removeListeners(chaseListener);
						}
						if (player.move(player.getLocation().getNeighbors()
								.get(Neighbor.BELOW))) {
							change(view, -1);
						} // play elevator sound
						else {
							view.notify("You cannot go there!");
						}

					} else if (upButton(view, clickX, clickY, up)) {
						if (checkToRemoveMonster(player.getLocation()
								.getNeighbors().get(Neighbor.ABOVE))) {
							player.removeListeners(chaseListener);
						}
						if (player.move(player.getLocation().getNeighbors()
								.get(Neighbor.ABOVE))) {

							change(view, 1);

						} else {
							view.notify("You cannot go there!");
						}

					} else if (centerButton(view, clickX, clickY, inventory)) {
						view.showInventory();
					} else {

						for (Map.Entry<Integer, GridCell> cell : gridMap
								.entrySet()) {

							if (clickX >= cell.getValue().getLeft()
									&& clickX <= cell.getValue().getRight()
									&& clickY >= cell.getValue().getTop()
									&& clickY <= cell.getValue().getBottom()) {
								for (Map.Entry<Location, Integer> mapping : view
										.getMapping().entrySet()) {
									if (mapping.getValue() == cell.getKey()
											&& movingOptions.contains(mapping
													.getValue())) {
										if (player.move(mapping.getKey())) {
											sp.play(footsteps, 1, 1, 0, 0, 1); // play
											// footsteps
											// sound
											// onTouch
											// of
											// accessible
											// space
											view.notify(mapping.getKey(),
													player);
										} else {
											view.notify("You cannot go there!");
										}

									}
								}
							}
						}
					}

					return true;
				}

				return false;
			}

		};

		view.setOnTouchListener(listener);

		setContentView(view);

		if (player.getLocation().getItems().size() > 0) {
			itemsPresent(view);
		}
	}

	protected boolean checkToRemoveMonster(Location location) {
		if (location != null) {
			return true;
		}
		return false;
	}

	/*
	 * Game setup includes levels being comprised of locations
	 */
	private void setUpGame() {
		Location loc = null;
		//level one set up
		for (int i = 0; i < 45; i++) {
			if (i == 22 || i == 1) {
				loc = new Water(String.valueOf(i));
				one.add(loc);
			} else {
				loc = new DefaultLocation(String.valueOf(i));
				one.add(loc);
			}
		}

		int j = 45;
		//level two set up
		for (int i = 0; i < 45; i++) {

			if (i == 25 || i == 12 || i == 4) {
				loc = new Water(String.valueOf(j++));
				two.add(loc);
			} else {
				loc = new DefaultLocation(String.valueOf(j++));
				two.add(loc);
			}
		}
		//level three set up
		for (int i = 0; i < 45; i++) {
			if (i == 5 || i == 6 || i == 7 || i == 8 || i == 9) {
				three.add(new Water(String.valueOf(j++)));
			} else {
				three.add(new DefaultLocation(String.valueOf(j++)));
			}
		}
		//level four set up
		for (int i = 0; i < 45; i++) {
			if (i == 15 || i == 26 || i == 7 || i == 3 || i == 29) {
				four.add(new Water(String.valueOf(j++)));
			} else {
				four.add(new DefaultLocation(String.valueOf(j++)));
			}
		}
		
		//configure neighbors
		configureNeighbors(one);
		configureNeighbors(two);
		configureNeighbors(three);
		configureNeighbors(four);
		
		//winning location is set to level four, tile 4
		winningLocation = four.get(4);

		/* Above and below neighbors used to create three-dimensional game map 
		 * and allow movement up and down if accessible 
		 */
		one.get(32).addNeighbor(Neighbor.ABOVE, two.get(5)); // 32's above
		// neighbor is
		// now 5 and
		// vice versa

		two.get(13).addNeighbor(Neighbor.ABOVE, three.get(0)); // 13's above
		// neighbor is
		// now 0 and
		// vice versa
		three.get(4).addNeighbor(Neighbor.ABOVE, four.get(36));

		Log.d(TAG, "below " + two.get(5).getNeighbors().get(Neighbor.BELOW));

		
		// Create player based on values passed from Main Menu screen which allows configuration
		player = new Person(PLAY_NAME, one.get(12), INIT_HEALTH, INIT_ENERGY,
				INIT_LIVES, INIT_CAPACITY, INIT_RATE);
		
		// Create game items and define their location on game map
		
		//food and energy items
		Portable food = new Food(10, "bread", 1);
		Portable energy = new Energy(2, "energybar", 1);
		one.get(14).addItem(energy);
		one.get(10).addItem(new Energy(5, "5 Hour Energy", 1));
		one.get(5).addItem(new Energy(12, "Red Bull", 1));
		two.get(6).addItem(new Energy(15, "Monster", 1));
		three.get(2).addItem(new Energy(25, "Coffee", 1));
		four.get(22).addItem(new Energy(20, "Espresso", 1));
		
		one.get(2).addItem(food);
		one.get(12).addItem(new Food(3, "cereal", 2));
		one.get(30).addItem(new Food(3, "brownie", 1));
		two.get(3).addItem(new Food(5, "pizza", 3));
		four.get(10).addItem(new Food(10, "bagels", 4));
		four.get(20).addItem(new Food(15, "fruit", 3));
		
		
		//weapons
		one.get(3).addItem(new Weapon("Axe", 5, 2));
		one.get(1).addItem(new Weapon("Sword", 2, 5));
		two.get(15).addItem(new Weapon("Shotgun", 7, 5));
		three.get(3).addItem(new Weapon("Bazooka", 10, 7));
		four.get(22).addItem(new Weapon("Chainsaw", 7, 5));
		
		//armor
		two.get(32).addItem(new Armor(25, "Shield", 10));

		//coins
		one.get(7).addItem(new Coin(3));
		one.get(33).addItem(new Coin(12));
		one.get(4).addItem(new Coin(52));
		one.get(5).addItem(new Coin(1));
		one.get(30).addItem(new Coin(1));
		one.get(36).addItem(new Coin(1));
		one.get(25).addItem(new Coin(3));
		one.get(14).addItem(new Coin(1));
		two.get(6).addItem(new Coin(1));
		two.get(1).addItem(new Coin(25));
		two.get(15).addItem(new Coin(12));
		two.get(38).addItem(new Coin(50));
		two.get(9).addItem(new Coin(1));
		two.get(22).addItem(new Coin(1));
		four.get(1).addItem(new Coin(1));
		four.get(10).addItem(new Coin(12));
		four.get(11).addItem(new Coin(15));
		four.get(21).addItem(new Coin(1));
		four.get(31).addItem(new Coin(2));
		four.get(17).addItem(new Coin(3));
		four.get(8).addItem(new Coin(5));

		// add levels to game map
		levels.put(1, one);
		levels.put(2, two);
		levels.put(3, three);
		levels.put(4, four);

		// add monsters and define their starting location
		monster = new Pudge("Monster", one.get(26), 5, INIT_RATE);
		monster2 = new Pudge("Monster2", two.get(42), 10, INIT_RATE);
		boss = new Pudge("Boss", four.get(5), 30, 15);
		monsters.put(1, monster);
		monsters.put(2, monster2);
		monsters.put(4, boss);
		
		movingTimer(1, 4);

		// movingTimer.start();

	}
	//sets up neighbors
	private void configureNeighbors(ArrayList<Location> level) {
		for (int i = 0; i < 45; i++) {
			if (i <= 39) {
				level.get(i).addNeighbor(Neighbor.SOUTH, level.get(i + 5));
			}
			if (i % 5 == 0) {
				level.get(i).addNeighbor(Neighbor.EAST, level.get(i + 1));
			} else if ((i + 1) % 5 == 0) {
				level.get(i).addNeighbor(Neighbor.WEST, level.get(i - 1));
			} else {
				level.get(i).addNeighbor(Neighbor.EAST, level.get(i + 1));
				level.get(i).addNeighbor(Neighbor.WEST, level.get(i - 1));
			}
		}
		
	}

	public void movingTimer(final int level, int rate) {
		final Actor currentMonster = monsters.get(level);

		movingTimer = new CountDownTimer(5000, 5000) {
			int min = 0;
			int max = TerrainView.MAX_CELLS;

			@Override
			public void onFinish() {
				if (currentMonster != null) {
					int choice = (int) (Math.random() * 4);

					switch (choice) {
					case 0:
						if (currentMonster.getLocation().getNeighbors()
								.get(Neighbor.WEST) != null) {
							currentMonster.move(currentMonster.getLocation()
									.getNeighbors().get(Neighbor.WEST));

						}
						break;
					case 1:
						if (currentMonster.getLocation().getNeighbors()
								.get(Neighbor.EAST) != null) {
							currentMonster.move(currentMonster.getLocation()
									.getNeighbors().get(Neighbor.EAST));

						}
						break;
					case 2:
						if (currentMonster.getLocation().getNeighbors()
								.get(Neighbor.SOUTH) != null) {
							currentMonster.move(currentMonster.getLocation()
									.getNeighbors().get(Neighbor.SOUTH));

						}
						break;
					case 3:
						if (currentMonster.getLocation().getNeighbors()
								.get(Neighbor.NORTH) != null) {
							currentMonster.move(currentMonster.getLocation()
									.getNeighbors().get(Neighbor.NORTH));

						}
						break;
					}
					Log.d(TAG, "MOVED "
							+ currentMonster.getLocation().getName());

					restartTimer(level);
				}
			}

			@Override
			public void onTick(long millisUntilFinished) {
				// Log.d(TAG, "ONE SECOND");
			}

		};

	}

	public void restartTimer(int level) {
		if (monsters.get(level) != null) {
			movingTimer.cancel(); // prevents timer from overlapping
			movingTimer.start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_walker, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.use_item:
			// Intent intent = new Intent("com.cs413.walker.main.USE_ITEM");
			// Bundle bundle = getIntent().getExtras();
			// intent.putExtras(bundle);
			//
			// startActivity(intent);
			// break;
			return true;

		case R.id.new_game:
			Intent intent = getIntent();
			finish();
			startActivity(intent);
			return true;
		}
		return true;

	}

	public void chase(TerrainView view) {
		for (Location loc : player.getLocation().getNeighbors().values()) {
			if (loc == player.getLocation().getNeighbors().get(Neighbor.ABOVE)
					|| loc == player.getLocation().getNeighbors()
							.get(Neighbor.BELOW)) {
				continue;
			}
			// Log.d(TAG, String.valueOf(loc.getActors().size()));
			if (loc.getActors().contains(monsters.get(view.getLevel()))) {
				tempMonster = (AbstractMonster) monsters.get(view.getLevel());
				tempMonster.setChasing(true);
				player.addListeners(chaseListener);
				movingTimer.cancel();
				Log.d(TAG, "MONSTER HERE");
				view.invalidate();
				sp.play(growl, 1, 1, 0, 0, 1);
				view.alert("Monster has spotted you! It will now chase and attack you!");
			}
		}
	}

	public void change(TerrainView view, int way) {
		player.removeListeners(chaseListener);
		sp.play(elevator, 1, 1, 0, 0, 1); // play elevator sound
		movingTimer.cancel();
		view.changeLevel(way);
		movingTimer(view.getLevel(), 4);
		((AbstractMonster)monsters.get(view.getLevel())).setChasing(false);
		movingTimer.start();
		view.notify(player.getLocation(), player);
		view.invalidate();
	}

	public boolean centerButton(TerrainView view, float clickX, float clickY,
			GridCell inventory) {
		return (clickX >= inventory.getLeft() && clickX <= inventory.getRight() // if
																				// inventory
																				// button
				// pressed
				&& clickY <= inventory.getTop() && clickY >= inventory
				.getBottom());
	}

	public boolean upButton(TerrainView view, float clickX, float clickY,
			GridCell up) {
		return (view.isCanGoUp() && clickX >= up.getLeft()
				&& clickX <= up.getRight() // if up button pressed
				&& clickY <= up.getTop() && clickY >= up.getBottom());
	}

	public boolean downButton(TerrainView view, float clickX, float clickY,
			GridCell down) {
		return (view.isCanGoDown() && clickX >= down.getLeft()
				&& clickX <= down.getRight() // if down button pressed
				&& clickY <= down.getTop() && clickY >= down.getBottom());
	}

	@Override
	protected void onPause() {
		super.onPause();
		movingTimer.cancel(); // stop the monster moving timer
	}

	@Override
	protected void onResume() {
		super.onResume();
		movingTimer.start(); // start the monster moving timer
	}

	public void itemsPresent(final TerrainView view) {

		final ArrayList<Portable> mSelectedItems = new ArrayList<Portable>();
		// Where we track the selected items
		String[] arr = new String[player.getLocation().getItems().size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = player.getLocation().getItems().get(i).toString();
		}

		CharSequence[] list = new CharSequence[arr.length];
		for (int i = 0; i < arr.length; i++) {
			list[i] = arr[i];
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

		// Set the dialog title
		builder.setTitle("Pick up item?")
				// Specify the list array, the items to be selected by default
				// (null for none),
				// and the listener through which to receive callbacks when
				// items are selected
				.setMultiChoiceItems(list, null,
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								if (isChecked) {
									// If the user checked the item, add it to
									// the selected items
									mSelectedItems.add(player.getLocation()
											.getItems().get(which));
								}
							}
						})
				.setPositiveButton("Done",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								player.addItems(mSelectedItems);
								// player.addItems(mSelectedItems.get(id+1));
								// player.getLocation().getItems().remove(id+1);
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								Log.d(TAG, "Cancelled");
							}
						});
		builder.show();
	}

}