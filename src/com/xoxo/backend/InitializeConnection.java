package com.xoxo.backend;

import android.content.Context;
import android.os.Handler;

public class InitializeConnection {

	public static void initialize(Context context, Handler hand) {
		if (MaintainConnection.connection == null) {
			MaintainConnection.connect(context, hand);

		} else {
			if (hand != null) {
				hand.sendEmptyMessage(0);
			}

		}

	}
}
