package com.dutchrudder.leaf;

import java.util.UUID;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.getpebble.android.kit.util.PebbleDictionary;

public class DataReceiver extends PebbleDataReceiver {

	private Handler handler;

	protected DataReceiver(UUID subscribedUuid, Handler handler) {
		super(subscribedUuid);
		this.handler = handler;
	}

	@Override
	public void receiveData(Context context, int transactionId, PebbleDictionary data) {
		handler.post(new DataRunner(context, transactionId, data));
	}

	private class DataRunner implements Runnable {
		
		private int transactionId;
		private Context context;
		private PebbleDictionary data;
		
		public DataRunner(Context context, int transactionID,  final PebbleDictionary data){
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
			 
			 
			 Log.d("Leaf", data.getUnsignedInteger(4) + "");
		} 
	}

}
