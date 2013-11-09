package com.dutchrudder.leaf;

import java.text.RuleBasedCollator;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUp extends Activity {
	Activity activityContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_reg);

		final EditText firstname = (EditText) findViewById(R.id.firstName);
		final EditText lastname = (EditText) findViewById(R.id.lastName);
		final EditText facebook = (EditText) findViewById(R.id.facebookName);
		final EditText username = (EditText) findViewById(R.id.username);

		final Button signup = (Button) findViewById(R.id.signup);

		activityContext = this;
		signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String uname = username.getText().toString();
		final String first = firstname.getText().toString();
		final String fb = facebook.getText().toString();
		final String fullname = lastname.getText().toString();
				final TextView view = (TextView) findViewById(R.id.errorText);
				if (!checkValues(first, fullname, fb, uname)) {
					view.setText("Username and firstname cannot be blank");
					view.setVisibility(View.VISIBLE);
				} else {
					ServerManager.ServerTask(new Runnable() {
						
						@Override
						public void run() {
							ServerManager.sendUserReg(uname, first, fullname, fb);
						}
					}, new Runnable() {
						
						@Override
						public void run() {
							finish_intent(uname);
						}
					}, new Runnable() {
						
						@Override
						public void run() {
							view.setText("Couldn't connect to server");
						view.setVisibility(View.VISIBLE);
						}
					});
				}
			}
		});

	}


	private boolean checkValues(String firstname, String lastname, String facebook, String username) {
		if (firstname == null || firstname.isEmpty()) {
			return false;
		}
		if (username == null || username.isEmpty()) {
			return false;
		}
		return true;
	}
	
	private void finish_intent(String username){
		Intent mainIntent = new Intent(activityContext, MainActivity.class);
		startActivity(mainIntent);
		MainActivity.sharedPrefs.edit().putBoolean(MainActivity.FTUE, true).commit();
		MainActivity.sharedPrefs.edit().putString(MainActivity.USERNAME, username).commit();
	}

}
