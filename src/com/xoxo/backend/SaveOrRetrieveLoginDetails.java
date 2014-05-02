package com.xoxo.backend;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SaveOrRetrieveLoginDetails {

	static String USERNAME;
	static String PASSWORD;
	static String NAME;
	static Map<String, String> login;

	public static void storeUpRegistrationDetails(Context context,
			String PHONE_NUMBER, String PASSWORD, String NAME) {

		SharedPreferences prefs = context.getSharedPreferences(
				"RegistrationState", Context.MODE_PRIVATE);
		Editor mEditor = prefs.edit();
		mEditor.putString("regStatus", "registered");
		mEditor.putString("USERNAME", PHONE_NUMBER);
		mEditor.putString("PASSWORD", PASSWORD);
		mEditor.putString("NAME", NAME);
		mEditor.commit();
		Log.i("successfuly stored up registration details", "success");

	}

	public static Map<String, String> retriveLoginDetails(Context context) {

		try {
			SharedPreferences prefs = context.getSharedPreferences(
					"RegistrationState", Context.MODE_PRIVATE);
			USERNAME = prefs.getString("USERNAME", "");
			PASSWORD = prefs.getString("PASSWORD", "");
			NAME = prefs.getString("NAME", "");
			login = new HashMap<String, String>();
			login.put("USERNAME", USERNAME);
			login.put("PASSWORD", PASSWORD);
			login.put("NAME", NAME);
			Log.i("login details successfuly retrieved", "success");

		} catch (Exception e) {
			Log.e("error retrieving loginDetails", e.toString());
		}

		return login;
	}

}
