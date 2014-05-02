package com.xoxo.backend;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;

import android.util.Log;

public class RosterRetrieval {

	static XMPPConnection connect;
	public static ArrayList<String> userFriends = new ArrayList<String>();
	public static ArrayList<String> names = new ArrayList<String>();
	public static ArrayList<String> statuses = new ArrayList<String>();
	public static ArrayList<String> jids = new ArrayList<String>();

	public static ArrayList<String> retrieve() {

		connect = MaintainConnection.connection;
		userFriends = retrieveRosterOfRegisteredUsers(connect);

		return userFriends;

	}

	public static ArrayList<String> retrieveRosterOfRegisteredUsers(
			XMPPConnection connect) {

		// method retrieves a list of registered roster entries
		Roster roster = connect.getRoster();

		Collection<RosterEntry> entries = roster.getEntries();
		for (RosterEntry entry : entries) {
			String name = entry.getName();
			String jid = entry.getUser();

			if (CheckUserRegistration.check(jid) == true) {
				if (names.contains(name) == false) {
					names.add(name);
				}
				if (jids.contains(jid) == false) {
					jids.add(jid);
				}
				Log.i("the not unregistered", entry.getName() + entry.getUser());
			}

		}
		retrieveStatuses();

		return names;
	}

	public static void retrieveStatuses() {
		for (int i = 0; i < jids.size(); i++) {
			String status = Statuses.getFriendStatus(jids.get(i));

			if (status != null) {
				statuses.add(status);
			} else {
				statuses.add("no status available");
			}

		}
	}

	public void connectionTermination() {
		connect.disconnect();
	}

	
}
