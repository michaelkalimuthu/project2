package com.cs413.walker.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.walker.R;

public class Cloud extends Activity {
	static final String URL = "http://api.openkeyval.org/";
	static String PLAYER_NAME = "";
	JSONObject json;
	static String PLAYER_INFO = "";
	static int INIT_ENERGY, INIT_CAPACITY, INIT_LIVES, INIT_RATE;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.cloud);

		Intent i = getIntent();

		INIT_ENERGY = i.getIntExtra("energy", 1);
		INIT_CAPACITY = i.getIntExtra("inventory", 1);
		INIT_LIVES = i.getIntExtra("lives", 1);
		INIT_RATE = i.getIntExtra("difficulty", 1);
		PLAYER_NAME = i.getExtras().getString("playername");

		final Button save_button = (Button) findViewById(R.id.save);
		final Button look_button = (Button) findViewById(R.id.look);
		final Button start_button = (Button) findViewById(R.id.start);
		final EditText player_name = (EditText) findViewById(R.id.player_id);
		final TextView player_info = (TextView) findViewById(R.id.player_info);

		PLAYER_INFO = "Energy: " + INIT_ENERGY + "\nInventory: "
				+ INIT_CAPACITY + "\nLives: " + INIT_LIVES + "\nDifficulty: "
				+ INIT_RATE;

		player_name.setText(PLAYER_NAME);
		player_info.setText(PLAYER_INFO);
		// new Read().execute("text");
		save_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new postPlayer().execute();

			}
		});

		look_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				PLAYER_NAME = player_name.getText().toString();
				new getPlayer().execute();

			}
		});

		start_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				analyzePlayer(PLAYER_INFO);

				Intent walker_activity = new Intent(getApplicationContext(),
						WalkerActivity.class);
				walker_activity.putExtra("difficulty", INIT_RATE);
				walker_activity.putExtra("lives", INIT_LIVES);
				walker_activity.putExtra("energy", INIT_ENERGY);
				walker_activity.putExtra("inventory", INIT_CAPACITY);
				walker_activity.putExtra("playername", player_name.getText()
						.toString());

				startActivityForResult(walker_activity, 0);
			}
		});

	}

	public void alert(String s) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Error");
		dialog.setMessage(s);
		dialog.setNeutralButton("Cool", null);
		dialog.create().show();
	}

	class postPlayer extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;

		// url to create new answer

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Cloud.this);
			pDialog.setMessage("Saving player information..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating answer
		 * */
		@Override
		protected String doInBackground(String... args) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(URL);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(PLAYER_NAME, PLAYER_INFO));
				// UrlEncodedFormEntity ent = new
				// UrlEncodedFormEntity(params,
				// HTTP.UTF_8);
				// post.setEntity(ent);
				post.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse responsePOST = client.execute(post);
				HttpEntity resEntity = responsePOST.getEntity();
				if (resEntity != null) {
					Log.i("RESPONSE", EntityUtils.toString(resEntity));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			// alert(result);
		}

	}

	class getPlayer extends AsyncTask<String, String, String> {

		private final String TAG_SUCCESS = null;
		private final String TAG_SUBMITTED = null;
		private ProgressDialog pDialog;
		JSONParser jsonParser = new JSONParser();
		int sucess, submitted;

		// url to create new answer

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Cloud.this);
			pDialog.setMessage("Looking for player information..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating answer
		 * */
		@Override
		protected String doInBackground(String... args) {

			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(URL + PLAYER_NAME);
				HttpResponse responseGet = client.execute(get);
				HttpEntity resEntityGet = responseGet.getEntity();
				if (resEntityGet != null) {
					PLAYER_INFO = EntityUtils.toString(resEntityGet);
					// do something with the response
					Log.i("GET RESPONSE", EntityUtils.toString(resEntityGet));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			TextView player_info = (TextView) findViewById(R.id.player_info);
			player_info.setText(PLAYER_INFO);
		}

	}

	public void analyzePlayer(String s) {
		String delimiter = "\n";
		String[] array = s.split(delimiter);
		String[] player_info = new String[4];
		for (int i = 0; i < array.length; i++) {
			String[] small_array = array[i].split(":");
			player_info[i] = small_array[1].trim();
		}

		INIT_ENERGY = Integer.parseInt(player_info[0]);
		INIT_CAPACITY = Integer.parseInt(player_info[1]);
		INIT_LIVES = Integer.parseInt(player_info[2]);
		INIT_RATE = Integer.parseInt(player_info[3]);

	}
}
