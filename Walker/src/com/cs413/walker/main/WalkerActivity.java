package com.cs413.walker.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

import com.cs413.walker.locations.DefaultLocation;
import com.cs413.walker.locations.Location;
import com.cs413.walker.view.TerrainView;
import com.example.walker.R;

public class WalkerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_walker);
        Location l = new DefaultLocation();
        TerrainView view = new TerrainView(this, l);
        setContentView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_walker, menu);
        return true;
    }
}
