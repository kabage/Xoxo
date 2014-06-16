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
	static MultiUserChat mucPoints = ViewFriendPoints.muc;
	static MultiUserChat mucNotification;
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
		sendChatMessage(jid);
		joinPointChannel(jid);
		try {
			mucPoints.sendMessage(data.toString());

		} catch (XMPPException e) {
			Log.e("an error occured sending points ", data.toString());
		}

	}

	public static void leaveChannel() {
		mucPoints.leave();

	}

	public static void joinPointChannel(String jid) {
		friendNumber = jid.replaceAll("@candr.com", "")
				.replaceAll("/Smack", "");

	}

	public static void sendChatMessage(String jid) {
		Message message = new Message(jid, Message.Type.chat);
		message.setBody(data.toString());
		message.setFrom(connection.getUser());

		try {
			connection.getChatManager().createChat(jid, null)
					.sendMessage(message);
		} catch (XMPPException e) {
			Log.e("error occured sending message ", e.toString());
		}

	}
}
