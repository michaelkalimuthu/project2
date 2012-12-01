package com.cs413.walker.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
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

import com.cs413.walker.actors.Actor;
import com.cs413.walker.actors.Person;
import com.cs413.walker.actors.ActorListener;
import com.cs413.walker.actors.Pudge;
import com.cs413.walker.items.Coin;
import com.cs413.walker.items.EnergyBar;
import com.cs413.walker.items.Food;
import com.cs413.walker.items.Portable;
import com.cs413.walker.locations.DefaultLocation;
import com.cs413.walker.locations.Location;
import com.cs413.walker.locations.Neighbor;
import com.cs413.walker.locations.Water;
import com.cs413.walker.view.GridCell;
import com.cs413.walker.view.TerrainView;
import com.example.walker.R;

public class WalkerActivity extends Activity {
	private static final String TAG = "Activity";

	private static int INIT_HEALTH = 100;
	private static int INIT_ENERGY = 10;
	private static int INIT_LIVES = 3;
	private static int INIT_CAPACITY = 1;
	private static int INIT_RATE = 1;

	HashMap<Integer, ArrayList<Location>> levels;
	ArrayList<Location> one;
	ArrayList<Location> two;
	ArrayList<Location> three;
	HashMap<Integer, GridCell> gridMap;
	ArrayList<Integer> movingOptions;
	HashMap<Integer, ArrayList<Actor>> monsters;
	ArrayList<Actor> levelOne;

	CountDownTimer movingTimer;
	ActorListener personListener, monsterListener;

	Actor player, monster;

	SoundPool sp;
	int footsteps;
	int elevator;

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

		// Example of receiving parameter having key value as 'email'
		// and storing the value in a variable named myemail
		String myemail = i.getStringExtra("email");

		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		one = new ArrayList<Location>(); // level one
		two = new ArrayList<Location>(); // level two
		three = new ArrayList<Location>();
		levels = new HashMap<Integer, ArrayList<Location>>();
		
		monsters = new HashMap<Integer, ArrayList<Actor>>();
		
		levelOne = new ArrayList<Actor>();

		gridMap = new HashMap<Integer, GridCell>();
		movingOptions = new ArrayList<Integer>();

		setUpGame();

		// SoundPool object allows up to 3 simultaneous sounds to be played.0
		// represents normal audio quality.
		sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		footsteps = sp.load(this, R.raw.footsteps, 1); // links footsteps
														// variable to audio
														// clip in raw folder
		elevator = sp.load(this, R.raw.elevator, 1);

		setContentView(R.layout.activity_walker);

		final TerrainView view = new TerrainView(this, levels, player);

		personListener = new ActorListener() {

			@Override
			public void pickedUpItem() {
				view.invalidate();
			}

			@Override
			public void moved() {
				view.invalidate();
			}

		};
		player.addListeners(personListener);
		
		monsterListener = new ActorListener(){

			@Override
			public void pickedUpItem() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void moved() {
				for (Location loc: player.getLocation().getNeighbors().values()){
					if (loc.getActors().contains(this)){
						view.invalidate();
						view.notify("near");
					}
					
				}
				
			}
			
		};
		monster.addListeners(monsterListener);
		
		OnTouchListener listener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					float clickX = event.getX();
					float clickY = event.getY();
					gridMap = view.getGridMap(); // get the grid
					movingOptions = view.getMovingOptions(); // get moving
																// options

					GridCell down = gridMap.get(TerrainView.DOWN); // get down
																	// button
					GridCell up = gridMap.get(TerrainView.UP); // get up button
					GridCell inventory = gridMap.get(TerrainView.INVENTORY);

					if (downButton(view, clickX, clickY, down)) {
						if (player.move(player.getLocation().getNeighbors()
								.get(Neighbor.BELOW))) { // move player
							view.changeLevel(-1); // let view know level is
													// changing
							view.notify(player.getLocation(), player); // tell
																		// view
																		// to
																		// notify
							sp.play(elevator, 1, 1, 0, 0, 1);
						} // play elevator sound
						else {
							view.notify("You can not go there!");
						}

					} else if (upButton(view, clickX, clickY, up)) {
						if (player.move(player.getLocation().getNeighbors()
								.get(Neighbor.ABOVE))) {
							sp.play(elevator, 1, 1, 0, 0, 1); // play elevator
																// sound
							view.changeLevel(1);
							view.notify(player.getLocation(), player);
						} else {
							view.notify("You can not go there!");
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
									if (mapping.getValue() == cell.getKey()  && movingOptions.contains(mapping.getValue())) {
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
											view.notify("You can not go there!");
										}

									}
								}
							}
						}
					}
					invalidateOptionsMenu();
					return true;
				}
				invalidateOptionsMenu();
				return false;
			}

			private boolean centerButton(TerrainView view, float clickX,
					float clickY, GridCell inventory) {
				return (clickX >= inventory.getLeft()
						&& clickX <= inventory.getRight() // if inventory button
															// pressed
						&& clickY <= inventory.getTop() && clickY >= inventory
						.getBottom());
			}

			private boolean upButton(TerrainView view, float clickX,
					float clickY, GridCell up) {
				return (view.isCanGoUp() && clickX >= up.getLeft()
						&& clickX <= up.getRight() // if up button pressed
						&& clickY <= up.getTop() && clickY >= up.getBottom());
			}

			private boolean downButton(TerrainView view, float clickX,
					float clickY, GridCell down) {
				return (view.isCanGoDown() && clickX >= down.getLeft()
						&& clickX <= down.getRight() // if down button pressed
						&& clickY <= down.getTop() && clickY >= down
						.getBottom());
			}

		};

		view.setOnTouchListener(listener);

		setContentView(view);
	}

	private void setUpGame() {
		Location loc = null;

		for (int i = 0; i < 45; i++) {
			if (i == 22) {
				loc = new Water(String.valueOf(i));
				one.add(loc);
			} else {
				loc = new DefaultLocation(String.valueOf(i));
				one.add(loc);
			}
		}

		int j = 45;

		for (int i = 0; i < 45; i++) {

			if (i == 25 || i == 12 || i == 4) {
				loc = new Water(String.valueOf(j++));
				two.add(loc);
			} else {
				loc = new DefaultLocation(String.valueOf(j++));
				two.add(loc);
			}
		}

		for (int i = 0; i < 45; i++) {
			if (i == 5 || i == 6 || i == 7 || i == 8 || i == 9) {
				three.add(new Water(String.valueOf(j++)));
			} else {
				three.add(new DefaultLocation(String.valueOf(j++)));
			}
		}

		for (int i = 0; i < 45; i++) {
			if (i <= 39) {
				one.get(i).addNeighbor(Neighbor.SOUTH, one.get(i + 5));
				two.get(i).addNeighbor(Neighbor.SOUTH, two.get(i + 5));
				three.get(i).addNeighbor(Neighbor.SOUTH, three.get(i + 5));
			}
			if (i % 5 == 0) {
				one.get(i).addNeighbor(Neighbor.EAST, one.get(i + 1));
				two.get(i).addNeighbor(Neighbor.EAST, two.get(i + 1));
				three.get(i).addNeighbor(Neighbor.EAST, three.get(i + 1));
			} else if ((i + 1) % 5 == 0) {
				one.get(i).addNeighbor(Neighbor.WEST, one.get(i - 1));
				two.get(i).addNeighbor(Neighbor.WEST, two.get(i - 1));
				three.get(i).addNeighbor(Neighbor.WEST, three.get(i - 1));
			} else {
				one.get(i).addNeighbor(Neighbor.EAST, one.get(i + 1));
				two.get(i).addNeighbor(Neighbor.EAST, two.get(i + 1));
				three.get(i).addNeighbor(Neighbor.EAST, three.get(i + 1));
				one.get(i).addNeighbor(Neighbor.WEST, one.get(i - 1));
				two.get(i).addNeighbor(Neighbor.WEST, two.get(i - 1));
				three.get(i).addNeighbor(Neighbor.WEST, three.get(i - 1));
			}
		}

		one.get(32).addNeighbor(Neighbor.ABOVE, two.get(5)); // 32's above
																// neighbor is
																// now 5 and
																// vice versa

		two.get(13).addNeighbor(Neighbor.ABOVE, three.get(0)); // 13's above
																// neighbor is
																// now 0 and
																// vice versa

		Log.d(TAG, "below " + two.get(5).getNeighbors().get(Neighbor.BELOW));

		player = new Person("Player", one.get(12), INIT_HEALTH, INIT_ENERGY,
				INIT_LIVES, INIT_CAPACITY);
		Portable food = new Food(10, "bread", 1);
		Portable energy = new EnergyBar(2, "energybar", 1);

		one.get(14).addItem(energy);
		one.get(12).addItem(food);
		one.get(12).addItem(new EnergyBar(4, "energybar", 1));
		one.get(12).addRewards(new Coin(3));

		levels.put(1, one);
		levels.put(2, two);
		levels.put(3, three);

		monster = new Pudge("Monster", one.get(17), 5, 5, 5);
		
		levelOne.add(monster);
		monsters.put(1, levelOne);
	    movingTimer(1);
	    
	   // movingTimer.start();

	}

	public void movingTimer(final int level) {
		final ArrayList<Actor> levelMonsters = monsters.get(level);

		movingTimer = new CountDownTimer(5000, 1000) {
			int min = 0;
			int max = TerrainView.MAX_CELLS;

			@Override
			public void onFinish() {
				for (Actor m : levelMonsters) {
					int choice = (int) (Math.random() * 4);
					switch (choice) {
					case 0:
						if (choice - 1 > min) {
							m.move(m.getLocation().getNeighbors()
									.get(Neighbor.WEST));
						}
						break;
					case 1:
						if (choice + 1 < max) {
							m.move(m.getLocation().getNeighbors()
									.get(Neighbor.EAST));
						}
						break;
					case 2:
						if (choice + 5 < max) {
							m.move(m.getLocation().getNeighbors()
									.get(Neighbor.SOUTH));
						}
						break;
					case 3:
						if (choice - 5 > min) {
							m.move(m.getLocation().getNeighbors()
									.get(Neighbor.NORTH));
						}
						break;
					}
					Log.d(TAG, "MOVED " + m.getLocation().getName());
				}
				restartTimer(level);
				
			}


			@Override
			public void onTick(long millisUntilFinished) {
				Log.d(TAG, "ONE SECOND");
			}

		};

	}
	
	public void restartTimer(int level){
		if (monsters.get(level).size() > 0){
			movingTimer.cancel(); //prevents timer from overlapping 
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

	@Override
	protected void onPause() {
		super.onPause();
		movingTimer.cancel();     // stop the monster moving timer
	}

	@Override
	protected void onResume() {
		super.onResume();
		movingTimer.start();    // start the monster moving timer
	}

}