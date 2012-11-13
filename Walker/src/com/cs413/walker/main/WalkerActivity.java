package com.cs413.walker.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.actors.Person;
import com.cs413.walker.locations.DefaultLocation;
import com.cs413.walker.locations.Location;
import com.cs413.walker.locations.Neighbor;
import com.cs413.walker.locations.Water;
import com.cs413.walker.view.GridCell;
import com.cs413.walker.view.TerrainView;
import com.example.walker.R;

public class WalkerActivity extends Activity {
	private static final String TAG = "Activity";

	HashMap<Integer, ArrayList<Location>> levels;
	ArrayList<Location> one;
	ArrayList<Location> two;
	ArrayList<Location> three;
	HashMap<Integer, GridCell> gridMap;
	ArrayList<Integer> movingOptions;

	Actor player;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		one = new ArrayList<Location>();
		two = new ArrayList<Location>();
		levels = new HashMap<Integer, ArrayList<Location>>();

		gridMap = new HashMap<Integer, GridCell>();
		movingOptions = new ArrayList<Integer>();
		setUpGame();



		setContentView(R.layout.activity_walker);
		Location l = new DefaultLocation();


		final TerrainView view = new TerrainView(this, levels, player);



		OnTouchListener listener = new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					float clickX = event.getX();
					float clickY = event.getY();
					gridMap = view.getGridMap();
					movingOptions = view.getMovingOptions();

					GridCell down = gridMap.get(TerrainView.DOWN);
					GridCell up = gridMap.get(TerrainView.UP);
					
					Log.d(TAG, String.valueOf(view.isCanGoDown()));
					Log.d(TAG, String.valueOf(view.isCanGoUp()));

					if (view.isCanGoDown() && clickX >= down.getLeft() && clickX <=down.getRight()  //if down button pressed
							&& clickY<=down.getTop() && clickY >= down.getBottom()){
						player.move(player.getLocation().getNeighbors().get(Neighbor.BELOW));
						view.changeLevel(-1);
						view.notify(player.getLocation(), player);
						view.invalidate();
					} else if(view.isCanGoUp() && clickX >= up.getLeft() && clickX <=up.getRight()  //if up button pressed
							&& clickY<=up.getTop() && clickY >= up.getBottom()){
						player.move(player.getLocation().getNeighbors().get(Neighbor.ABOVE));
						view.changeLevel(1);
						view.notify(player.getLocation(), player);
						view.invalidate();
					} else{

						for (Map.Entry<Integer, GridCell> cell : gridMap.entrySet()){

							if (clickX >= cell.getValue().getLeft() && clickX <=cell.getValue().getRight()
									&& clickY>=cell.getValue().getTop() && clickY <= cell.getValue().getBottom()){
								for (Map.Entry<Location, Integer> mapping : view.getMapping().entrySet()){
									if (mapping.getValue() == cell.getKey() && movingOptions.contains(mapping.getValue())){
										player.move(mapping.getKey());
										view.notify(mapping.getKey(), player);
										view.invalidate();
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
	}



	private void setUpGame(){
		Location loc = null;

		for (int i = 0; i < 45; i++){
			if (i == 22){
				loc = new Water(String.valueOf(i));
				one.add(loc);
			} else{
				loc = new DefaultLocation(String.valueOf(i));
				one.add(loc);
			}
		}

		int j = 45;

		for (int i = 0; i<45; i++){
			
			if (i == 25 || i == 12 || i == 4){
				loc = new Water(String.valueOf(j++));
				two.add(loc);
			} else{
				loc = new DefaultLocation(String.valueOf(j++));
				two.add(loc);
			}
		}

		for (int i = 0; i<45; i++){
			if (i <= 39){
				one.get(i).addNeighbor(Neighbor.SOUTH, one.get(i + 5));
				two.get(i).addNeighbor(Neighbor.SOUTH, two.get(i + 5));
			}
			if (i % 5 == 0){
				one.get(i).addNeighbor(Neighbor.EAST, one.get(i+1));
				two.get(i).addNeighbor(Neighbor.EAST, two.get(i+1));
			} else if ((i + 1) % 5 == 0){
				one.get(i).addNeighbor(Neighbor.WEST, one.get(i - 1));
				two.get(i).addNeighbor(Neighbor.WEST, two.get(i - 1));
			} else{
				one.get(i).addNeighbor(Neighbor.EAST, one.get(i + 1));
				two.get(i).addNeighbor(Neighbor.EAST, two.get(i + 1));
				one.get(i).addNeighbor(Neighbor.WEST, one.get(i - 1));
				two.get(i).addNeighbor(Neighbor.WEST, two.get(i - 1));
			}
		}

		one.get(32).addNeighbor(Neighbor.ABOVE, two.get(5));
		
		Log.d(TAG, "below " + two.get(5).getNeighbors().get(Neighbor.BELOW));

		player = new Person("Player", one.get(12));

		levels.put(1, one);
		levels.put(2, two);

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_walker, menu);
		return true;
	}
}
