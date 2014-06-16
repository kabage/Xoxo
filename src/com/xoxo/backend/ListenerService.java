package com.xoxo.backend;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

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

			PacketFilter packetFilter = new MessageTypeFilter(Message.Type.chat);

			connect.addPacketListener(new PacketListener() {

				@Override
				public void processPacket(Packet packet) {
					// TODO Auto-generated method stub
					Message msg = (Message) packet;
					NotificationAndCaching.notification(msg,
							ListenerService.this, 0);

				}
			}, packetFilter);

		}
	}

}
