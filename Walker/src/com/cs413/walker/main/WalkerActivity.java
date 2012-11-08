package com.cs413.walker.main;

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
import com.cs413.walker.view.TerrainView;
import com.example.walker.R;

public class WalkerActivity extends Activity {
	private static final String TAG = "Activity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        

        
        setContentView(R.layout.activity_walker);
        Location l = new DefaultLocation();
        
        Actor player = new Person("player", l);
        final TerrainView view = new TerrainView(this, l);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_walker, menu);
        return true;
    }
}
