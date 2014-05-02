package com.xoxo.backend;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RateFriend {
	static XMPPConnection connection = MaintainConnection.connection;
	static MultiUserChat muc;
	static int friendPoints = 0;
	static String friendNumber;
	static int numberOfPoints;
	static JSONObject receivedData;
	static JSONObject data;

	public static void rate(String jid, String points, String extraWords) {

		data = new JSONObject();
		try {
			data.put("points", points);
			data.put("reasons", extraWords);
		} catch (JSONException e1) {
			Log.e("error occured putting data in json", e1.toString());
		}

		joinChannel(jid);
		try {
			muc.sendMessage(data.toString());
		} catch (XMPPException e) {
			Log.e("error occured sending points", e.toString());
		}
		sendExtraWords(jid, connection, data.toString());

	}

	public static void leaveChannel() {
		muc.leave();

	}

	public static void joinChannel(String jid) {
		friendNumber = jid.replaceAll("@candr.com", "");
		muc = new MultiUserChat(connection, friendNumber
				+ "@conference.candr.com");

		try {
			muc.join(connection.getUser());
		} catch (XMPPException e1) {
			Log.e("an error occured when trying to join friend channel",
					e1.toString());
		}

	}

	public static void sendExtraWords(String jid, XMPPConnection connection,
			String extraWords) {

		
		Message msg = new Message(jid, Message.Type.chat);
		msg.setFrom(connection.getUser());
		msg.setBody(data.toString());
		connection.sendPacket(msg);
	}
}
