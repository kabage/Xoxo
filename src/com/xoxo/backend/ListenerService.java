package com.xoxo.backend;

import java.io.IOException;
import java.util.ArrayList;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.xoxo.NotificationView;
import com.xoxo.ObjectSerializer;
import com.xoxo.R;

public class ListenerService extends Service {

	XMPPConnection connect = MaintainConnection.connection;
	JSONObject data;
	String name, reason, points;
	Notification not = null;
	String pointsMessage;
	String mPoints = null;
	public static boolean serviceStarted = false;

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

	public void notification(Message msg) {

		try {
			data = new JSONObject(msg.getBody());
		} catch (JSONException e) {
			Log.e("error occurred getting json from message", e.toString());
		}

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Intent intent = new Intent(ListenerService.this, NotificationView.class);
		Bundle bundle = new Bundle();
		try {
			data.put("from", msg.getFrom());
		} catch (JSONException e) {
			Log.e("error occured adding from messagedata to json", e.toString());
		}
		bundle.putString("notificationData", data.toString());
		intent.putExtras(bundle);
		PendingIntent pIntent = PendingIntent.getActivity(ListenerService.this,
				0, intent, 0);

		NotificationCompat.Builder n = new NotificationCompat.Builder(
				ListenerService.this);

		try {
			mPoints = data.getString("points");
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (mPoints.equals("0")) {
			pointsMessage = "No points!";
		} else if (mPoints.equals("1")) {
			pointsMessage = "1 point!";
		} else {
			pointsMessage = mPoints + " points!";
		}
		try {

			String jid = data.getString("from").replaceAll("/Smack", "");
			int position = RosterRetrieval.jids.indexOf(jid);
			String senderName = RosterRetrieval.names.get(position);
			not = n.setContentTitle(pointsMessage)
					.setContentText("from " + senderName)
					.setSmallIcon(R.drawable.xoxo_logo)
					.setContentIntent(pIntent).setAutoCancel(true).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		notificationManager.notify(06755, not);
	}

	@SuppressWarnings("unchecked")
	private void createArrayLists(Message msg) {
		// TODO Auto-generated method stub
		SharedPreferences sp = getSharedPreferences(getPackageName(),
				MODE_PRIVATE);

		try {
			name = data.getString("from");
			reason = data.getString("reasons");
			points = data.getString("points");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			Log.e("Failiure to get data from createArrayLists", e1.toString());
		}

		ArrayList<String> reasonsArray, friendNameArray, receivedPointsArray;
		reasonsArray = new ArrayList<String>();
		friendNameArray = new ArrayList<String>();
		receivedPointsArray = new ArrayList<String>();

		try {
			reasonsArray = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("reasons",
							ObjectSerializer.serialize(new ArrayList<String>())));
			friendNameArray = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("friends",
							ObjectSerializer.serialize(new ArrayList<String>())));

			receivedPointsArray = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("points",
							ObjectSerializer.serialize(new ArrayList<String>())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		friendNameArray.add(name);
		reasonsArray.add(reason);
		receivedPointsArray.add(points);

		try {
			sp.edit()
					.putString("reasons",
							ObjectSerializer.serialize(reasonsArray)).commit();
			sp.edit()
					.putString("friends",
							ObjectSerializer.serialize(friendNameArray))
					.commit();
			sp.edit()
					.putString("points",
							ObjectSerializer.serialize(receivedPointsArray))
					.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void receiveData() {

		ViewOwn.viewPoints();
		Log.i("exectuion active", "exection active ");

		PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
		connect.addPacketListener(new PacketListener() {

			@Override
			public void processPacket(Packet pack) {
				// TODO Auto-generated method stub
				Message msg = (Message) pack;

				Log.i("message received in service", msg.getBody() + "...."
						+ "from" + msg.getFrom() + ".....to" + msg.getTo());

				notification(msg);
				createArrayLists(msg);
			}
		}, filter);

	}

}
