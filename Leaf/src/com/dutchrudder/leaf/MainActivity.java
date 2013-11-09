package com.dutchrudder.leaf;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.dutchrudder.leaf.listAdapter.ContactListItem;
import com.dutchrudder.leaf.listAdapter.ExpandableListAdapter;
import com.dutchrudder.leaf.service.LeafService;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

public class MainActivity extends Activity {
	
	public static final String FTUE = "ftue";

	public static final String USERNAME = "username";

	private List<ContactListItem> contactItems;
	public static ExpandableListView expListView;
	public static ExpandableListAdapter listAdapter;

	public static SharedPreferences sharedPrefs;
	
	Handler handler = new Handler();
	public static Context appcontext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		appcontext = getApplicationContext();
		super.onCreate(savedInstanceState);
		sharedPrefs = this.getSharedPreferences("leaf", MODE_WORLD_READABLE);
		if(sharedPrefs.contains(FTUE)){
			
			setContentView(R.layout.activity_main);
			Intent startService = new Intent(this, LeafService.class);
			startService(startService);
		
			ExpandableListView expListView;
		
			// get the listview
			expListView = (ExpandableListView) findViewById(R.id.contactListView);
 
			// preparing list data
			prepareListData();
 
			listAdapter = new ExpandableListAdapter(this, contactItems);
 
			// setting list adapter
			expListView.setAdapter(listAdapter);
		} else {
			launchSignup();
		}
	}

	private void prepareListData() {
		this.contactItems = new ArrayList<ContactListItem>();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_settings){
			sharedPrefs.edit().remove(FTUE).commit();
			launchSignup();
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void launchSignup(){
		Intent signin = new Intent(this, SignUp.class);
		startActivity(signin);
	}

	public static void refresh(String uname) {
		listAdapter.notifyDataSetChanged();
		/*PebbleDictionary data = new PebbleDictionary();
		data.addString(0, uname);
		PebbleKit.sendDataToPebble(appcontext, LeafService.PEBBLE_UUID ,data );*/
	}

	
}
