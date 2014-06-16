package com.xoxo.backend;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import android.util.Log;

public class Statuses {
	public static XMPPConnection connection = MaintainConnection.connection;
	public static String friendReasons;

	public static void setStatus(String reasons) {
		VCard status = new VCard();

		status.setField("reasons", reasons);
		status.setField("jid", connection.getUser());
		try {
			status.save(connection);
		} catch (XMPPException e) {
			Log.i("an error occured while trying to save the vcard",
					e.toString());
		}
		Log.i("status saved ", "the status has been saved");
	}

	public static String getFriendStatus(String userJid) {
		VCard status = new VCard();

		try {
			status.load(connection, userJid);

		} catch (XMPPException e) {
			Log.i("an error occured loading friend's vcard", e.toString());
		}
		friendReasons = status.getField("reasons");
		status = null;
		
		return friendReasons;
	}
}
