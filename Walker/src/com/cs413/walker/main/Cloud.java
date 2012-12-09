package com.cs413.walker.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.walker.R;

public class Cloud extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.cloud);

		Intent i = getIntent();

		int INIT_ENERGY = i.getIntExtra("energy", 1);
		int INIT_CAPACITY = i.getIntExtra("inventory", 1);
		int INIT_LIVES = i.getIntExtra("lives", 1);
		int INIT_RATE = i.getIntExtra("difficulty", 1);
		String PLAY_NAME = i.getExtras().getString("playername");

		final Button save_button = (Button) findViewById(R.id.save);
		final Button look_button = (Button) findViewById(R.id.look);
		final Button start_button = (Button) findViewById(R.id.start);
		final EditText player_name = (EditText) findViewById(R.id.player_id);
		final TextView player_info = (TextView) findViewById(R.id.player_info);

		player_name.setText(PLAY_NAME);
		player_info.setText("Energy: " + INIT_ENERGY + "\nInventory: "
				+ INIT_CAPACITY + "\nLives: " + INIT_LIVES + "\nDifficulty:+ "
				+ INIT_RATE);

	}

}
