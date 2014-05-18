package com.xoxo.backend;

import org.jivesoftware.smack.XMPPConnection;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ListenerService extends Service {

	XMPPConnection connect = MaintainConnection.connection;

	public static boolean serviceStarted = false;

	String ownNumber;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		receiveData();
		serviceStarted = true;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {

				receiveData();
			}
		});
		t.start();

	}

	public void receiveData() {
		if (connect != null) {
			ViewOwn.viewPoints(ListenerService.this);

		}
	}
}
