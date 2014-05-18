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
	static MultiUserChat mucPoints;
	static MultiUserChat mucNotification;
	static int friendPoints = 0;
	static String friendNumber;
	static int numberOfPoints;
	static JSONObject receivedData;
	static JSONObject data;

	public static void rate(String jid, String points, String extraWords) {

		sendChatMessage(jid);
		data = new JSONObject();
		try {
			data.put("points", points);
			data.put("reasons", extraWords);
		} catch (JSONException e1) {
			Log.e("error occured putting data in json", e1.toString());
		}

		joinPointChannel(jid);
		try {
			Message msg = new Message() {
			};
			msg.setProperty("readState", false);
			msg.setBody(data.toString());

			mucPoints.sendMessage(msg);
		} catch (XMPPException e) {
			Log.e("error occured sending points", e.toString());
		}

	}

	public static void leaveChannel() {
		mucPoints.leave();

	}

	public static void joinPointChannel(String jid) {
		friendNumber = jid.replaceAll("@candr.com", "");
		mucPoints = new MultiUserChat(connection, friendNumber
				+ "@conference.candr.com");

		try {
			mucPoints.join(connection.getUser());
		} catch (XMPPException e1) {
			Log.e("an error occured when trying to join friend channel",
					e1.toString());
		}

	}

	public static void sendChatMessage(String jid) {
		Message message = new Message(jid, Message.Type.chat);
		message.setBody("a message sent offline");
		message.setFrom(connection.getUser());

		connection.sendPacket(message);

	}

}
