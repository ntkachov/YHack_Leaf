package com.dutchrudder.leaf;

import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.getpebble.android.kit.PebbleKit;

public class MainActivity extends Activity {

	private PebbleKit.PebbleDataReceiver dataReceiver;
	private PebbleKit.PebbleAckReceiver ackReceiver;
	private PebbleKit.PebbleNackReceiver nackReceiver;
	
	private MessageManager messageManager;
	
	private Thread messageThread = null;

	private static final UUID TODO_LIST_UUID = UUID
			.fromString("39b9d7d5-87b1-4950-a41e-1219fc1bfdf4");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		messageManager = new MessageManager(getApplicationContext(), TODO_LIST_UUID);
		PebbleKit.startAppOnPebble(getApplicationContext(), TODO_LIST_UUID);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
		if(messageThread == null){
			new Thread(messageManager).start();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(messageThread != null){
			messageThread.stop();
		}
	}
	
	@Override
    protected void onPause() {
        super.onPause();

        // Always deregister any Activity-scoped BroadcastReceivers when the Activity is paused
        if (dataReceiver != null) {
            unregisterReceiver(dataReceiver);
            dataReceiver = null;
        }

        if (ackReceiver != null) {
            unregisterReceiver(ackReceiver);
            ackReceiver = null;
        }

        if (nackReceiver != null) {
            unregisterReceiver(nackReceiver);
            nackReceiver = null;
        }
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		
		final Handler handler = new Handler();
		dataReceiver = new DataReceiver(TODO_LIST_UUID, new Handler());
		
        PebbleKit.registerReceivedDataHandler(getApplicationContext(), dataReceiver);

        ackReceiver = new PebbleKit.PebbleAckReceiver(TODO_LIST_UUID) {
            @Override
            public void receiveAck(final Context context, final int transactionId) {
                messageManager.notifyAckReceivedAsync();
            }
        };

        PebbleKit.registerReceivedAckHandler(this, ackReceiver);


        nackReceiver = new PebbleKit.PebbleNackReceiver(TODO_LIST_UUID) {
            @Override
            public void receiveNack(final Context context, final int transactionId) {
                messageManager.notifyNackReceivedAsync();
            }
        };

        PebbleKit.registerReceivedNackHandler(this, nackReceiver);
	}

}
