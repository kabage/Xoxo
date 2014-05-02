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

public class ViewFriendPoints {
	public static int friendPoints = 0;
	static XMPPConnection connect = MaintainConnection.connection;
	static MultiUserChat muc;
	static String points;
	public static ArrayList<String> pointsArray = new ArrayList<String>();

	public static void viewPoints(String jid) {

		String friendNumber = jid.replaceAll("@candr.com", "").replaceAll(
				"/Smack", "");
		Log.i(" number", friendNumber);
		// if(muc.isJoined()==false){}
		muc = new MultiUserChat(connect, friendNumber + "@conference.candr.com");

		try {
			muc.join(connect.getUser());
		} catch (XMPPException e) {
			Log.e("error logging in to view friends's channnel", e.toString());
		}

		muc.addMessageListener(new PacketListener() {

			@Override
			public void processPacket(Packet pack) {
				Message msg = (Message) pack;

				if (msg != null) {
					try {
						JSONObject job = new JSONObject(msg.getBody());
						Log.i("received json data", msg.getBody());
						points = job.getString("points");

					} catch (JSONException e) {
						Log.e("an error occured parsing text to: json ",
								e.toString());
					}
					friendPoints += Integer.valueOf(points);

					Log.i("logging friend points xo",
							msg.getBody() + "....from" + msg.getFrom()
									+ "...number of points"
									+ String.valueOf(friendPoints));
				}

			}
		});

	}

	
}
