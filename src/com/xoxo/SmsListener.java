package com.xoxo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsListener extends BroadcastReceiver {

	String strMsgSrc, strMsgBody;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		try {
			if (extras != null) {
				Object[] smsextras = (Object[]) extras.get("pdus");
				for (int i = 0; i < smsextras.length; i++) {
					SmsMessage smsmsg = SmsMessage
							.createFromPdu((byte[]) smsextras[i]);
					strMsgBody = smsmsg.getMessageBody().toString();
					strMsgSrc = smsmsg.getOriginatingAddress();
					if (strMsgBody.trim().startsWith("+")) {
						if (strMsgSrc.trim().equals(strMsgBody.trim())) {
							Intent finish = new Intent(context,
									InitializeUser.class);
							Bundle b = new Bundle();
							b.putString("phoneNumber", strMsgSrc);
							finish.putExtras(b);
							finish.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(finish);
						}
					}
				}
				abortBroadcast();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}