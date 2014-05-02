package com.xoxo.backend;

import java.util.ArrayList;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.util.Log;

public class RegisterAllChannels {
	public static MultiUserChat muc;
	public static ArrayList<String> numberOfPoints = new ArrayList<String>();
	static String ownChannel;

	public static void registerChannels(ArrayList<String> jids) {

		XMPPConnection connection = MaintainConnection.connection;
		ownChannel = connection.getUser().replaceAll("@candr.com", "")
				.replaceAll("/Smack", "")
				+ "@conference.candr.com";
		for (String jid : jids) {
			String friendNumber = jid.replaceAll("@candr.com", "");
			muc = new MultiUserChat(connection, friendNumber
					+ "@conference.candr.com");
			try {
				muc.join(connection.getUser());

			} catch (XMPPException e) {
				Log.i("error joining friends channnel", e.toString());
			}
			PacketFilter filter = new MessageTypeFilter(Message.Type.groupchat);
			connection.addPacketListener(new PacketListener() {

				@Override
				public void processPacket(Packet arg0) {
					// TODO Auto-generated method stub
					Message msg = (Message) arg0;
					String roomName = muc.getRoom();
					ArrayList<String> sourceList = new ArrayList<String>();
					sourceList.add(roomName);

					String messageString = msg.getBody();
					

				}
			}, filter);
		}

	}
}
