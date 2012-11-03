package com.cs413.walker.main;

import com.example.walker.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WalkerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_walker, menu);
        return true;
    }
}
