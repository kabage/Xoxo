package com.xoxo.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Broadcast extends BroadcastReceiver {

	Handler handler;

	@Override
	public void onReceive(final Context context, Intent intent) {
		final int DO_UPDATE_TEXT = 0;
		final int DO_THAT = 1;
		// on receive method
		handler = new Handler() {
			public void handleMessage(Message msg) {
				final int what = msg.what;
				switch (what) {
				case DO_UPDATE_TEXT:

					Intent in = new Intent(context, ListenerService.class);
					context.startService(in);

					Log.i("message received", "message received ");
					break;
				case DO_THAT:
					// doThat();
					break;
				}
			}
		};
		InitializeConnection.initialize(context, handler);

	}
}
