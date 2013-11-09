package com.dutchrudder.leaf.service;

import java.util.UUID;

import com.dutchrudder.leaf.DataReceiver;
import com.dutchrudder.leaf.MessageManager;
import com.dutchrudder.leaf.R;
import com.getpebble.android.kit.PebbleKit;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class LeafService extends Service {
	
	private PebbleKit.PebbleDataReceiver dataReceiver;
	private PebbleKit.PebbleAckReceiver ackReceiver;
	private PebbleKit.PebbleNackReceiver nackReceiver;
	
	private MessageManager messageManager;
	
	private Thread messageThread = null;
	
	private int id = PEBBLE_UUID.hashCode();
	private Notification notification;

	private static final UUID PEBBLE_UUID = UUID
			.fromString("39b9d7d5-87b1-4950-a41e-1219fc1bfdf4");

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		notification = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentText(getText(R.string.notification_content_text)).setTicker(getText(R.string.notification_ticket_text))
		.setContentTitle(getText(R.string.notification_content_title))
		.build();
		
		messageManager = new MessageManager(getApplicationContext(), PEBBLE_UUID);
		PebbleKit.startAppOnPebble(getApplicationContext(), PEBBLE_UUID);
		
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(messageThread == null){
			new Thread(messageManager).start();
		}
			final Handler handler = new Handler();
		dataReceiver = new DataReceiver(PEBBLE_UUID, new Handler());
		
        PebbleKit.registerReceivedDataHandler(getApplicationContext(), dataReceiver);

        ackReceiver = new PebbleKit.PebbleAckReceiver(PEBBLE_UUID) {
            @Override
            public void receiveAck(final Context context, final int transactionId) {
                messageManager.notifyAckReceivedAsync();
            }
        };

        PebbleKit.registerReceivedAckHandler(this, ackReceiver);


        nackReceiver = new PebbleKit.PebbleNackReceiver(PEBBLE_UUID) {
            @Override
            public void receiveNack(final Context context, final int transactionId) {
                messageManager.notifyNackReceivedAsync();
            }
        };

        PebbleKit.registerReceivedNackHandler(this, nackReceiver);
		super.onDestroy();
		startForeground(id, notification);
		return super.onStartCommand(intent, flags, startId);
		
	}
	
	@Override
	public void onDestroy() {
		if(messageThread != null){
			messageThread.stop();
		}
		
	
	}
}
