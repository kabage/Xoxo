package com.xoxo.backend;

import java.io.IOException;
import java.util.ArrayList;

import org.jivesoftware.smack.packet.Message;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.xoxo.NotificationView;
import com.xoxo.ObjectSerializer;
import com.xoxo.R;

public class NotificationAndCaching {

	static JSONObject data;
	static String name;
	static String reason;
	static String points;
	static Notification not = null;
	static String pointsMessage;
	static String mPoints = null;

	static String notificationSub;

	String ownNumber;

	public static void notification(Message msg, Context context,
			int messageCount) {

		try {
			data = new JSONObject(msg.getBody());
		} catch (JSONException e) {
			Log.e("error occurred getting json from message", e.toString());
		}

		@SuppressWarnings("static-access")
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(context, NotificationView.class);
		Bundle bundle = new Bundle();
		try {
			data.put("from", msg.getFrom());
		} catch (JSONException e) {
			Log.e("error occured adding from messagedata to json", e.toString());
		}
		bundle.putString("notificationData", data.toString());
		intent.putExtras(bundle);
		PendingIntent pIntent = PendingIntent
				.getActivity(context, 0, intent, 0);

		NotificationCompat.Builder n = new NotificationCompat.Builder(context);

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
			if (messageCount > 1) {
				not = n.setContentTitle("Points").setContentText(

				"points from " + String.valueOf(messageCount) + "friends")
						.setSmallIcon(R.drawable.xoxo_logo)
						.setContentIntent(pIntent).setAutoCancel(true).build();

			} else {
				not = n.setContentTitle(mPoints)
						.setContentText("from " + senderName)
						.setSmallIcon(R.drawable.xoxo_logo)
						.setContentIntent(pIntent).setAutoCancel(true).build();
			}

		} catch (Exception e) {
			Log.e("error occured could not generate", e.toString());
		}

		notificationManager.notify(634125, not);
		createArrayLists(msg, context);

	}

	@SuppressWarnings("unchecked")
	public static void createArrayLists(Message msg, Context context) {
		// TODO Auto-generated method stub
		@SuppressWarnings("static-access")
		SharedPreferences sp = context.getSharedPreferences(
				context.getPackageName(), context.MODE_PRIVATE);

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
}
