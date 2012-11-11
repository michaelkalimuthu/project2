package com.cs413.walker.main;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.cs413.walker.view.TerrainView;
import com.example.walker.R;

public class WalkerActivity extends Activity {
	private static final String TAG = "Activity";
	
	HashMap<Integer, ArrayList<Location>> levels;
	ArrayList<Location> one;
	ArrayList<Location> two;
	ArrayList<Location> three;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        one = new ArrayList<Location>();
        levels = new HashMap<Integer, ArrayList<Location>>();
        
        setUpGame();
        

        
        setContentView(R.layout.activity_walker);
        Location l = new DefaultLocation();
        
        Actor player = new Person("player", l);
        final TerrainView view = new TerrainView(this, l, levels);
        view.setActor(player);
        
        
		OnTouchListener listener = new OnTouchListener(){
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
		            float x = event.getX();
		            float y = event.getY();
		            view.setPlaceHolderX(x);
		            view.setPlaceHolderY(y);
		            view.invalidate();
		            
		            Log.d(TAG, String.valueOf(x) + " " + String.valueOf(y));
		            return true;
		        }
				//Log.d(TAG, "FALSE");
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
    	
    	for (int i = 0; i<44; i++){
    		if (i < 39)
    			one.get(i).addNeighbor(Neighbor.SOUTH, one.get(i + 5));
    		if (i % 5 == 0){
    			one.get(i).addNeighbor(Neighbor.EAST, one.get(i+1));
    		} else if ((i + 1) % 5 == 0){
    			one.get(i).addNeighbor(Neighbor.WEST, one.get(i - 1));
    		} else{
    			one.get(i).addNeighbor(Neighbor.EAST, one.get(i + 1));
    			one.get(i).addNeighbor(Neighbor.WEST, one.get(i - 1));
    		}
    	}	
    	
    	
    	levels.put(1, one);
    	
    }
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_walker, menu);
        return true;
    }
}
