package com.dutchrudder.leaf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.getpebble.android.kit.util.PebbleDictionary;

public class DataReceiver extends PebbleDataReceiver {

	private Handler handler;
	private Context context;

	public DataReceiver(UUID subscribedUuid, Handler handler) {
		super(subscribedUuid);
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
			 if(data.getUnsignedInteger(4) == 5){
				 new Thread(new Runnable() {
						public void run() {
							if (ServerManager.sendFirst()) {
								/*try {
								//	Thread.sleep(5000);
							//	} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}*/	
								String userInfo = ServerManager.sendSecond();
								System.out.println(userInfo);
							}
						}
					}).start();
			 }
		} 
	}
	




}
