package com.cs413.walker.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.actors.Person;
import com.cs413.walker.actors.PersonListener;
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

	private final static int INIT_HEALTH = 100;
	private final static int INIT_ENERGY = 10;
	private final static int INIT_LIVES = 3;

	HashMap<Integer, ArrayList<Location>> levels;
	ArrayList<Location> one;
	ArrayList<Location> two;
	ArrayList<Location> three;
	HashMap<Integer, GridCell> gridMap;
	ArrayList<Integer> movingOptions;

	PersonListener personListener;

	Actor player;

	SoundPool sp;
	int footsteps;
	int elevator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		one = new ArrayList<Location>(); // level one
		two = new ArrayList<Location>(); // level two
		three = new ArrayList<Location>();
		levels = new HashMap<Integer, ArrayList<Location>>();

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

		personListener = new PersonListener() {

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

					} else if (upButton(view, clickX, clickY, up)) {
						if (player.move(player.getLocation().getNeighbors()
								.get(Neighbor.ABOVE))) {
							sp.play(elevator, 1, 1, 0, 0, 1); // play elevator
																// sound
							view.changeLevel(1);
							view.notify(player.getLocation(), player);
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
				INIT_LIVES);
		Portable food = new Food(10, "bread");
		Portable energy = new EnergyBar(2, "energybar");

		one.get(14).addItem(energy);
		one.get(12).addItem(food);
		one.get(12).addItem(new EnergyBar(4, "energybar"));
		one.get(12).addRewards(new Coin(3));

		levels.put(1, one);
		levels.put(2, two);
		levels.put(3, three);

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
			Intent intent = new Intent("com.cs413.walker.main.USE_ITEM");
			Bundle bundle = getIntent().getExtras();
			intent.putExtras(bundle);

			startActivity(intent);
			break;
		}
		return true;
	}

}
