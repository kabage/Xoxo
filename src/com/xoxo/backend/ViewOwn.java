package com.xoxo.backend;

import java.util.ArrayList;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ViewOwn {

	public static int myPoints = 0;
	static XMPPConnection connect = MaintainConnection.connection;
	static MultiUserChat muc;
	static String points, from;
	public static ArrayList<String> reasonsList = new ArrayList<String>();
	public static ArrayList<String> pointList = new ArrayList<String>();
	public static ArrayList<String> senders = new ArrayList<String>();

	public static int viewPoints() {

		String ownNumber = connect.getUser().replaceAll("@candr.com", "")
				.replaceAll("/Smack", "");
		Log.i("own number", ownNumber);
		muc = new MultiUserChat(connect, ownNumber + "@conference.candr.com");

		try {
			muc.join(connect.getUser());
			
		} catch (XMPPException e) {
			Log.e("error logging in to view own channnel", e.toString());
		}

		muc.addMessageListener(new PacketListener() {

			@Override
			public void processPacket(Packet pack) {
				Message msg = (Message) pack;
				try {
					if (msg != null) {
						JSONObject job = new JSONObject(msg.getBody());
						points = job.getString("points");

					}

				} catch (JSONException e) {
					Log.e("an error occured parsing text to json ",
							e.toString());
				}

				myPoints += Integer.valueOf(points);

				Log.i("logging own message in view own points", msg.getBody()
						+ "....from" + msg.getFrom() + "...number of points"
						+ String.valueOf(myPoints));

			}
		});
		return myPoints;
	}

	public void leaveChannel() {

		muc.leave();
	}
}
