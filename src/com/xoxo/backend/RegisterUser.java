package com.xoxo.backend;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.VCardProvider;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

public class RegisterUser {

	static Thread t;
	public static XMPPConnection connection;
	public static String auserName, auserNumber;

	static ArrayList<String> theNames = new ArrayList<String>();
	static ArrayList<String> theNumbers = new ArrayList<String>();
	static Map<String, String> newRegAttrMap = new HashMap<String, String>();
	static MultiUserChat muc;

	public static void register(final Context context, final String USERNAME,
			final String PHONE_NUMBER, final String PASSWORD) {

		t = new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();
				// SmackAndroid.init(context);
				System.setProperty("smack.debugEnabled", "true");
				SASLAuthentication.supportSASLMechanism("PLAIN", 0);
				XMPPConnection.DEBUG_ENABLED = true;
				ConnectionConfiguration conf = new ConnectionConfiguration(
						ConnectionManager.HOST, ConnectionManager.PORT,
						ConnectionManager.SERVICE);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
					conf.setTruststoreType("AndroidCAStore");
					conf.setTruststorePassword(null);
					conf.setTruststorePath(null);
				} else {
					conf.setTruststoreType("BKS");
					String path = System
							.getProperty("javax.net.ssl.trustStore");
					if (path == null)
						path = System.getProperty("java.home") + File.separator
								+ "etc" + File.separator + "security"
								+ File.separator + "cacerts.bks";
					conf.setTruststorePath(path);
				}
				connection = new XMPPConnection(conf);

				ProviderManager.getInstance().addIQProvider("vCard",
						"vcard-temp", new VCardProvider()); // get point and
															// last tag
				ProviderManager pm = ProviderManager.getInstance();
				pm.addIQProvider("query",
						"http://jabber.org/protocol/disco#info",
						new DiscoverInfoProvider());
				pm.addExtensionProvider("x", "jabber:x:data",
						new DataFormProvider());
				pm.addExtensionProvider("x",
						"http://jabber.org/protocol/muc#user",
						new MUCUserProvider());
				pm.addIQProvider("query",
						"http://jabber.org/protocol/muc#admin",
						new MUCAdminProvider());
				pm.addIQProvider("query",
						"http://jabber.org/protocol/muc#owner",
						new MUCOwnerProvider());
				pm.addIQProvider("query",
						"http://jabber.org/protocol/disco#items",
						new DiscoverItemsProvider());
				pm.addIQProvider("query",
						"http://jabber.org/protocol/muc#rooms",
						new DiscoverItemsProvider());
				pm.addExtensionProvider("x", "jabber:x:roster",
						new RosterExchangeProvider());
				pm.addIQProvider("query", "jabber:iq:private",
						new PrivateDataManager.PrivateDataIQProvider());
				pm.addIQProvider("sharedgroup",
						"http://www.jivesoftware.org/protocol/sharedgroup",
						new SharedGroupsInfo.Provider());

				try {
					CreateRoster.getContacts(context);
					theNames = CreateRoster.contactNameArray;

					theNumbers = CreateRoster.contactNumberArray;
					Log.i("thecontacts off ",
							theNames.toString() + theNumbers.toString());
					connection.connect();
					// Class.forName("org.jivesoftware.smack.ReconnectionManager");
					registerThePerson(connection, USERNAME, PHONE_NUMBER,
							PASSWORD);

					for (int i = 0; i < theNames.size(); i++) {
						try {
							connection.getRoster().createEntry(
									theNumbers.get(i), theNames.get(i), null);
						} catch (XMPPException e) {
							Log.i("error creating roster ", e.toString());
						}
					}

					Log.i("usernmae and password are", PHONE_NUMBER + PASSWORD);
					if (connection.isAuthenticated() == false) {
						connection.login(PHONE_NUMBER, "password");

						theNames = CreateRoster.contactNameArray;
						CreateRoster.getContacts(context);

						theNumbers = CreateRoster.contactNumberArray;
						for (int i = 0; i < theNames.size(); i++) {
							try {
								connection.getRoster().createEntry(
										theNumbers.get(i), theNames.get(i),
										null);
							} catch (XMPPException e) {
								Log.i("error creating roster ", e.toString());
							}
						}

					}

				} catch (Exception ex) {
					connection = null;
					Log.e("error in registration or vcard creation",
							ex.toString());
				}
				// connection.disconnect();
			}
		});
		t.start();

	}

	public static void registerThePerson(XMPPConnection connection,
			String USERNAME, String PHONE_NUMBER, String PASSWORD) {

		newRegAttrMap.put("name", USERNAME);

		newRegAttrMap.put("email", null);
		try {
			connection.getAccountManager().createAccount(PHONE_NUMBER,
					PASSWORD, newRegAttrMap);
			if (connection.isAuthenticated() == false) {
				connection.login(PHONE_NUMBER, PASSWORD);
				registerPersonAsGroup(connection, PHONE_NUMBER);
				registerNotificationSub(connection, PHONE_NUMBER);

			}
		} catch (XMPPException e) {
			Log.e("error in registration", e.toString());
			e.printStackTrace();
		}
		// connection.disconnect();

	}

	public static void createVcard(XMPPConnection connection, String username) {

		VCard vCard = new VCard();
		vCard.setJabberId(connection.getUser());
		vCard.setField("points", "0");

		try {
			vCard.save(connection);
		} catch (XMPPException e) {

			Log.e("error saving the users's vcard", e.toString());
		}

	}

	public static void registerPersonAsGroup(XMPPConnection connection,
			String PHONE_NUMBER) {

		MultiUserChat muc = new MultiUserChat(connection, PHONE_NUMBER
				+ "@conference.candr.com");
		try {

			muc.create(PHONE_NUMBER);
			muc.join(connection.getUser());
		} catch (XMPPException e) {
			Log.e("error creating the room", e.toString());
		}

		if (muc != null && muc.isJoined()) {
			muc.leave();
		}

	}

	public static void disconnnectTheSession() {
		connection.disconnect();
	}

	public static void registerNotificationSub(XMPPConnection connection,
			String PHONE_NUMBER) {

		MultiUserChat muc = new MultiUserChat(connection, PHONE_NUMBER
				+ "notification" + "@conference.candr.com");
		try {

			muc.create(PHONE_NUMBER);
			muc.join(connection.getUser());
		} catch (XMPPException e) {
			Log.e(" error creating the notification sub", e.toString());
		}

		if (muc != null && muc.isJoined()) {
			muc.leave();
		}

	}

}