package com.dutchrudder.leaf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_reg);
		
		EditText firstname = (EditText)findViewById(R.id.firstName);
		EditText lastname = (EditText)findViewById(R.id.lastName);
		EditText facebook = (EditText)findViewById(R.id.facebookName);
		Button signup = (Button)findViewById(R.id.signup);
		
		final Activity activityContext = this;
		signup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.sharedPrefs.edit().putBoolean(MainActivity.FTUE, true).commit();
				Intent mainIntent = new Intent(activityContext, MainActivity.class);
				startActivity(mainIntent);
			}
		});
		
	}

}
