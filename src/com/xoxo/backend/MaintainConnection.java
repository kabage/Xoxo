package com.xoxo.backend;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

public class MaintainConnection extends AsyncTask<String, Void, String> {
	static Thread t;
	public static XMPPConnection connection;
	public static String auserName, auserNumber;
	Context context;
	Handler hand;
	static Map<String, String> newRegAttrMap = new HashMap<String, String>();

	public MaintainConnection(final Context mcontext, final Handler mhand) {

		context = mcontext;
		hand = mhand;

	}

	public static void registerThePerson(XMPPConnection connection,
			String USERNAME, String PHONE_NUMBER, String PASSWORD) {

		newRegAttrMap.put("name", PHONE_NUMBER);
		newRegAttrMap.put("email", null);
		try {
			connection.getAccountManager().createAccount(PHONE_NUMBER,
					"password", newRegAttrMap);
			connection.login(PHONE_NUMBER, "password");

		} catch (XMPPException e) {
			Log.e("error in registration", e.toString());
			e.printStackTrace();
		}

	}

	public static void disconnnectTheSession() {

		connection.disconnect();
	}

	@Override
	protected String doInBackground(String... params) {
		
		try {
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
		} catch (ClassNotFoundException e3) {
			Log.e("class not found extception maintain connnection 75",
					e3.toString());
		}
		System.setProperty("smack.debugEnabled", "true");
		SASLAuthentication.supportSASLMechanism("PLAIN", 0);
		XMPPConnection.DEBUG_ENABLED = true;
		ConnectionConfiguration conf = new ConnectionConfiguration(
				ConnectionManager.HOST, ConnectionManager.PORT,
				ConnectionManager.SERVICE);
		conf.setReconnectionAllowed(true); // new change to enable automatic
											// reconnection
		conf.setSendPresence(false);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			conf.setTruststoreType("AndroidCAStore");
			conf.setTruststorePassword(null);
			conf.setTruststorePath(null);
			conf.setSASLAuthenticationEnabled(true); // latest change
		} else {
			conf.setTruststoreType("BKS");
			String path = System.getProperty("javax.net.ssl.trustStore");
			if (path == null)
				path = System.getProperty("java.home") + File.separator + "etc"
						+ File.separator + "security" + File.separator
						+ "cacerts.bks";
			conf.setTruststorePath(path);
		}
		connection = new XMPPConnection(conf);

		ProviderManager pm = ProviderManager.getInstance();
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#rooms",
				new DiscoverItemsProvider());
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());

		try {
			connection.connect();
			connection.addConnectionListener(new ConnectionListener() {

				@Override
				public void reconnectionSuccessful() {
					// TODO Auto-generated method stub

				}

				@Override
				public void reconnectionFailed(Exception e) {
					// TODO Auto-generated method stub
					try {
						connection.connect();
					} catch (XMPPException e2) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void reconnectingIn(int seconds) {
					// TODO Auto-generated method stub

				}

				@Override
				public void connectionClosedOnError(Exception e) {
					// TODO Auto-generated method stub
					try {
						connection.connect();
					} catch (XMPPException e1) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void connectionClosed() {
					// TODO Auto-generated method stub
					try {
						connection.connect();
					} catch (XMPPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			String username = SaveOrRetrieveLoginDetails.retriveLoginDetails(
					context).get("USERNAME");
			String password = SaveOrRetrieveLoginDetails.retriveLoginDetails(
					context).get("PASSWORD");
			Log.i("logging the retrieved password", password + username);
			connection.login(username, password);
		} catch (Exception ex) {
			connection = null;
			Log.e("error in connection to server or in login", ex.toString());
		}
		Log.i("successful connection to the server ", "success");

		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (hand != null) {
			hand.sendEmptyMessage(0);
		}
	}

}
