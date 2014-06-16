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
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.VCardProvider;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

public class Register {

	public static void registerUser(Context context, String USERNAME,
			String PHONE_NUMBER, String PASSWORD, Handler myHandler) {

		Registration reg = new Registration(context, USERNAME, PHONE_NUMBER,
				PASSWORD, myHandler);
		reg.execute();

	} 

}

class Registration extends AsyncTask<String, Void, String> {

	static ArrayList<String> theNames = new ArrayList<String>();
	static ArrayList<String> theNumbers = new ArrayList<String>();
	static XMPPConnection connection;
	String USERNAME, PHONE_NUMBER, PASSWORD;
	Context context;
	static Map<String, String> newRegAttrMap = new HashMap<String, String>();
	Handler handler;

	public Registration(Context myContext, String myUSERNAME,
			String myPHONE_NUMBER, String myPASSWORD, Handler myHandler) {
		handler = myHandler;
		context = myContext;
		USERNAME = myUSERNAME;
		PHONE_NUMBER = myPHONE_NUMBER;
		PASSWORD = myPASSWORD;

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
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
			String path = System.getProperty("javax.net.ssl.trustStore");
			if (path == null)
				path = System.getProperty("java.home") + File.separator + "etc"
						+ File.separator + "security" + File.separator
						+ "cacerts.bks";
			conf.setTruststorePath(path);
		}
		connection = new XMPPConnection(conf);

		ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",
				new VCardProvider()); // get point and
										// last tag
		ProviderManager pm = ProviderManager.getInstance();
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());
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
	}

	@Override
	protected String doInBackground(String... params) {

		CreateRoster.getContacts(context);
		theNames = CreateRoster.contactNameArray;

		theNumbers = CreateRoster.contactNumberArray;
		Log.i("thecontacts off ", theNames.toString() + theNumbers.toString());
		try {
			connection.connect();
		} catch (XMPPException e) {
			Log.e("an error occured when trying to connnect to the server ",
					e.toString());
		}
		return null;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		registerThePerson(connection, USERNAME, PHONE_NUMBER, PASSWORD);
		try {
			connection.login(PHONE_NUMBER, PASSWORD);
		} catch (XMPPException e1) {
			Log.e("error occured logging in the server ", e1.toString());
		}
		if (connection.isAuthenticated()) {
			for (int i = 0; i < theNames.size(); i++) {
				try {
					connection.getRoster().createEntry(
							theNumbers.get(i) + "@candr.com", theNames.get(i),
							null);
				} catch (XMPPException e) {
					Log.i("error creating roster ", e.toString());
				}
			}

			registerPersonAsGroup(connection, PHONE_NUMBER);
			connection.disconnect();
			handler.sendEmptyMessage(0);

		}

	}

	public static void registerThePerson(XMPPConnection connection,
			String USERNAME, String PHONE_NUMBER, String PASSWORD) {

		newRegAttrMap.put("name", USERNAME);

		newRegAttrMap.put("email", null);
		try {
			connection.getAccountManager().createAccount(PHONE_NUMBER,
					PASSWORD, newRegAttrMap);

		} catch (XMPPException e) {
			Log.e("error in registration", e.toString());
			e.printStackTrace();
		}

	}

	public static void registerPersonAsGroup(XMPPConnection connection,
			String PHONE_NUMBER) {

		MultiUserChat muc = new MultiUserChat(connection, PHONE_NUMBER
				+ "@conference.candr.com");
		try {
			muc.create(PHONE_NUMBER);
		} catch (XMPPException e) {
			Log.e("errror occcured registering person as a group", e.toString());
		}

		Form submitform = null;
		try {
			
			submitform = muc.getConfigurationForm().createAnswerForm();
		} catch (XMPPException e) {
			Log.e("an error ocurred getting config form", e.toString());
		}
		submitform.setAnswer("muc#roomconfig_publicroom", true);
		try {
			muc.sendConfigurationForm(submitform);
		} catch (XMPPException e) {
			Log.e("an error occured sending the configuration form ",
					e.toString());
		}

	}

}