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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.dutchrudder.leaf.listAdapter.ContactListItem;
import com.dutchrudder.leaf.listAdapter.ExpandableListAdapter;
import com.dutchrudder.leaf.service.LeafService;

public class MainActivity extends Activity {

	private List<ContactListItem> contactItems;
	private ExpandableListView expListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		new Thread(new Runnable() {
			public void run() {
				if (sendFirst()) {
					/*try {
					//	Thread.sleep(5000);
				//	} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/	
					String userInfo = sendSecond();
					System.out.println(userInfo);
				}
			}
		}).start();
		return super.onOptionsItemSelected(item);
	}
	
	public boolean sendFirst() {

		try {
			URL u = new URL(
					String.format("http://54.215.16.181/send-time?&timeStamp=0&userId=mchiang&userInfo=Marco_Chiang"));

			HttpURLConnection urlConnection = (HttpURLConnection) u
					.openConnection();
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(urlConnection.getInputStream(),
								"UTF-8"));
				String json = "";
				String line = "";
				while ((line = reader.readLine()) != null) {
					json += line;
				}

				JSONObject jsonObject = new JSONObject(json);
				System.out.println(jsonObject);
				if (jsonObject.get("message").equals("success")) {
					return true;
				}

			} finally {
				urlConnection.disconnect();
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return false;
	}

	public String sendSecond() {

		try {
			URL u = new URL(
					String.format("http://54.215.16.181/match?&timeStamp=0&userId=test"));

			HttpURLConnection urlConnection = (HttpURLConnection) u
					.openConnection();
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(urlConnection.getInputStream(),
								"UTF-8"));
				String json = "";
				String line = "";
				while ((line = reader.readLine()) != null) {
					json += line;
				}

				JSONObject jsonObject = new JSONObject(json);
				if (jsonObject.get("message").equals("success")) {
					return jsonObject.getString("userInfo");
				}

			} finally {
				urlConnection.disconnect();
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return null;
	}


}
