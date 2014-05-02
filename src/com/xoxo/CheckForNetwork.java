package com.xoxo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckForNetwork {

	static boolean haveConnectedWifi = false;
	static boolean haveConnectedMobile = false;
	static boolean isNetwork = false;

	public static boolean checkNetworkConnection(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}

		if (haveConnectedMobile == true || haveConnectedWifi == true) {
			isNetwork = true;
		}

		return isNetwork;
	}
}