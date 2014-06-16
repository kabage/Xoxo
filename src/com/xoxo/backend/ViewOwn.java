package com.xoxo.backend;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class ViewOwn {

	public static int myPoints = 0;
	static XMPPConnection connect = MaintainConnection.connection;
	static MultiUserChat muc;
	static String points, from;
	public static ArrayList<String> reasonsList = new ArrayList<String>();
	public static ArrayList<String> pointList = new ArrayList<String>();
	public static ArrayList<String> senders = new ArrayList<String>();
	static ArrayList<Message> messages = new ArrayList<Message>();
	static int messageCount = 0;
	static Iterator<Message> messageiterator;

	public static int viewPoints(final Context context) {

		if (connect != null) {

			OfflineMessageManager manager = new OfflineMessageManager(connect);

			try {
				messageCount = manager.getMessageCount();
			} catch (XMPPException e1) {
				Log.e("an error occured when getting the offline message count",
						e1.toString());
			}

			try {
				messageiterator = manager.getMessages();
			} catch (XMPPException e2) {
				Log.e("an error occured getting offfline message as",
						e2.toString());
			}

			while (messageiterator.hasNext()) {
				Message message = messageiterator.next();
				NotificationAndCaching.notification(message, context,
						messageCount);

				String body = message.getBody();
				Log.i("the retrieved offline messages ", body);
			}

			try {
				manager.deleteMessages();
			} catch (XMPPException e2) {
				Log.e("an error occured when deleting offline messages ",
						e2.toString());
			}

			Presence presence = new Presence(Type.available,
					"my very new status", 1, Mode.available);

			connect.sendPacket(presence);

			String ownNumber = connect.getUser().replaceAll("@candr.com", "")
					.replaceAll("/Smack", "");
			Log.i("own number", ownNumber);

			@SuppressWarnings("deprecation")
			Date date = new Date(0, 1, 1);
			DiscussionHistory history = new DiscussionHistory();
			history.setSince(date);

			muc = new MultiUserChat(connect, ownNumber
					+ "@conference.candr.com");

			try {
				muc.join(connect.getUser(), null, history, 20000);

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

					Log.i("logging own message in view own points",
							msg.getBody() + "....from" + msg.getFrom()
									+ "...number of points"
									+ String.valueOf(myPoints));

				}
			});
		}
		return myPoints;
	}

	public void leaveChannel() {

		muc.leave();
	}
}
