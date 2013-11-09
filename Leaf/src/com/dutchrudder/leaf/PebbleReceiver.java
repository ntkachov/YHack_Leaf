package com.dutchrudder.leaf;

import java.util.UUID;

import android.content.Context;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.getpebble.android.kit.util.PebbleDictionary;

public class PebbleReceiver extends PebbleDataReceiver{

	protected PebbleReceiver(UUID subscribedUuid) {
		super(subscribedUuid);
		
	}

	@Override
	public void receiveData(Context context, int transactionId, PebbleDictionary data) {
		Log.d("Leaf", "GOT DATA FROM THE PEBBLE   !!");
	}

}
