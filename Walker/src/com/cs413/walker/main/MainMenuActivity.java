package com.cs413.walker.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.cs413.walker.actors.Person;
import com.example.walker.R;

/**
 * Main screen for the game.
 * 
 * @author shell
 * 
 */
public class MainMenuActivity extends Activity {
	// Initializing variables
	Person player = new Person(null, null, 0, 0, 0, 0);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		int GAME_IS_ON = getIntent().getIntExtra("GAME_IS_ON", 0);
		if (GAME_IS_ON == 1) {
			findViewById(R.id.resume).setEnabled(true);
		}

		final Button play_button = (Button) findViewById(R.id.play);
		final Button resume_button = (Button) findViewById(R.id.resume);

		final NumberPicker difficulty = (NumberPicker) findViewById(R.id.difficulty);
		difficulty.setMaxValue(5);
		difficulty.setMinValue(1);
		difficulty.setValue(1);

		final NumberPicker lives = (NumberPicker) findViewById(R.id.lives);
		lives.setMaxValue(100);
		lives.setMinValue(1);
		lives.setValue(3);

		final NumberPicker inventory = (NumberPicker) findViewById(R.id.inventory);
		inventory.setMaxValue(100);
		inventory.setMinValue(1);
		inventory.setValue(5);
		final NumberPicker energy = (NumberPicker) findViewById(R.id.energy);
		energy.setMaxValue(100);
		energy.setMinValue(1);
		energy.setValue(20);

		final Intent walker_activity = new Intent(getApplicationContext(),
				WalkerActivity.class);

		final EditText play_name = (EditText) findViewById(R.id.playername);

		// new game button
		play_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				walker_activity.putExtra("difficulty", difficulty.getValue());
				walker_activity.putExtra("lives", lives.getValue());
				walker_activity.putExtra("energy", energy.getValue());
				walker_activity.putExtra("inventory", inventory.getValue());
				walker_activity.putExtra("playername", play_name.getText()
						.toString());

				startActivityForResult(walker_activity, 0);

			}
		});

		resume_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	//
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // TODO Auto-generated method stub
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// if (requestCode == RESULT_OK) {
	//
	// }
	// }

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}