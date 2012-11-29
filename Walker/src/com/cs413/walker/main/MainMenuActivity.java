package com.cs413.walker.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.walker.R;

public class MainMenuActivity extends Activity {
	// Initializing variables
	EditText inputName;
	EditText inputEmail;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		Button play_button = (Button) findViewById(R.id.play);

		// Listening to button event
		play_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent walker_activity = new Intent(getApplicationContext(),
						WalkerActivity.class);
				startActivity(walker_activity);
			}
		});
	}
}