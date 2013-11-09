package com.dutchrudder.leaf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.dutchrudder.leaf.listAdapter.ContactListItem;
import com.dutchrudder.leaf.listAdapter.ExpandableListAdapter;
import com.dutchrudder.leaf.service.LeafService;

public class MainActivity extends Activity {
	
	public static final String FTUE = "ftue";

	private List<ContactListItem> contactItems;
	private ExpandableListView expListView;

	public static SharedPreferences sharedPrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPrefs = this.getSharedPreferences("leaf", MODE_WORLD_READABLE);
		if(sharedPrefs.contains(FTUE)){
			setContentView(R.layout.activity_main);
			Intent startService = new Intent(this, LeafService.class);
			startService(startService);
		
			ExpandableListAdapter listAdapter;
			ExpandableListView expListView;
		
			// get the listview
			expListView = (ExpandableListView) findViewById(R.id.contactListView);
 
			// preparing list data
			prepareListData();
 
			listAdapter = new ExpandableListAdapter(this, contactItems);
 
			// setting list adapter
			expListView.setAdapter(listAdapter);
		} else {
			Intent signin = new Intent(this, SignUp.class);
		}
	}

	private void prepareListData() {
		this.contactItems = new ArrayList<ContactListItem>();
		this.contactItems.add(new ContactListItem("Me", null, "Me Me", "yfb"));
		this.contactItems.add(new ContactListItem("You", null, "Me Me", "yfb"));
		this.contactItems.add(new ContactListItem("Him", null, "Me Me", "yfb"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		return super.onOptionsItemSelected(item);
	}
	
	
}
