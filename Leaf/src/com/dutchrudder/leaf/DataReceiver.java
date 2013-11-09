package com.dutchrudder.leaf;

import java.util.UUID;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.dutchrudder.leaf.listAdapter.ContactListItem;
import com.dutchrudder.leaf.listAdapter.ExpandableListAdapter;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.getpebble.android.kit.util.PebbleDictionary;

public class DataReceiver extends PebbleDataReceiver {

	private Handler handler;
	private Context context;
	private final UUID id;

	public DataReceiver(UUID subscribedUuid, Handler handler) {
		super(subscribedUuid);
		id = subscribedUuid;
		this.handler = handler;
	}

	@Override
	public void receiveData(Context context, int transactionId, PebbleDictionary data) {
		handler.post(new DataRunner(context, transactionId, data));
		this.context = context;
	}

	private class DataRunner implements Runnable {

		private int transactionId;
		private Context context;
		private PebbleDictionary data;

		public DataRunner(Context context, int transactionID, final PebbleDictionary data) {
			this.data = data;
			this.context = context;
			this.transactionId = transactionID;

		}

		@Override
		public void run() {
			PebbleKit.sendAckToPebble(context, transactionId);
			if (!data.iterator().hasNext()) {
				return;
			}
			if (data.getUnsignedInteger(4) == 5) {
				
				ServerTask servTask = new ServerTask();
				servTask.execute();
					
				
			}
		}
	}
	
	public class ServerTask extends AsyncTask<Void, Void, Void>{
		
		String uname = null;

		@Override
		protected Void doInBackground(Void... params) {
			if (ServerManager.sendFirst()) {
					String[] userInfo = ServerManager.sendSecond();
					if (userInfo != null) {
						Log.d("Leaf", userInfo[0]);
						boolean good = true;
						for(ContactListItem item : ExpandableListAdapter.contactList ){
							if(item.name.equals(userInfo[0])){
								good = false;
							}
						}
						if(good){
							ExpandableListAdapter.contactList.add(new ContactListItem(userInfo[1],
									null, userInfo[2], userInfo[3]));
							uname = userInfo[0];
						}
					}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			MainActivity.refresh(uname);	
	
		}
		
	}

}
