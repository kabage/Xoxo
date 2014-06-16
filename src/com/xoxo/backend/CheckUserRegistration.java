package com.xoxo.backend;

import java.util.Iterator;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;

import android.util.Log;

public class CheckUserRegistration {

	static Form searchForm;
	public static boolean rosterEntryState = false;
	public static XMPPConnection connection = MaintainConnection.connection;
	static Form answerForm;

	public static boolean check(String jid) {

		String phoneNumber = jid.replaceAll("@candr.com", "").replaceAll(
				"/Smack", "");

		if (connection != null) {
			UserSearchManager search = new UserSearchManager(connection);
			try {
				searchForm = search.getSearchForm("search."
						+ connection.getServiceName());
			} catch (XMPPException e) {
				Log.e("an error occured when trying to get search form server ",
						e.toString());
			}
			try {
				answerForm = searchForm.createAnswerForm();
				answerForm.setAnswer("Username", true);
				answerForm.setAnswer("search", phoneNumber);
			} catch (Exception e) {
				Log.e("an error occured when creating the answer form ",
						e.toString());
			}

			org.jivesoftware.smackx.ReportedData data = null;
			try {
				data = search.getSearchResults(answerForm, "search."
						+ connection.getServiceName());
			} catch (XMPPException e) {
				Log.e("an error occured when tring to get errors", e.toString());
			}

			if (data != null) {
				if (data.getRows() != null) {
					Iterator<Row> iterate = data.getRows();

					if (iterate.hasNext()) {
						rosterEntryState = true;
					} else {
						rosterEntryState = false;
					}
				}
			}

		}
		return rosterEntryState;

	}
}
