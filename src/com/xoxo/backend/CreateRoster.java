package com.xoxo.backend;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class CreateRoster {

	public static ArrayList<String> contactNameArray;
	public static ArrayList<String> contactNumberArray;
	static String contactName;
	static String contactNumber;
	static PhoneNumberUtil phoneUtil;
	static PhoneNumber phoneNumber;

	public static void getContacts(Context context) {
		// // TODO Auto-generated method stub
		contactNameArray = new ArrayList<String>();
		contactNumberArray = new ArrayList<String>();
		ContentResolver cr = context.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				contactName = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				contactNameArray.add(contactName);
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					while (pCur.moveToNext()) {
						contactNumber = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						contactNumberArray.add(contactNumber);
					}
					pCur.close();
				}
			}
		}
		formatPhoneNumbers(context);
	}

	private static void formatPhoneNumbers(Context context) {
		// TODO Auto-generated method stub
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String locale = tm.getSimCountryIso().toUpperCase();

		for (int i = 0; i < contactNumberArray.size(); i++) {
			phoneUtil = PhoneNumberUtil.getInstance();

			contactNumber = contactNumberArray.get(i).trim();
			contactNumber.replaceAll("[\\s+\\-()]", "");

			try {
				phoneNumber = phoneUtil.parse(contactNumber, locale);
			} catch (NumberParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			contactNumber = phoneUtil.format(phoneNumber,
					PhoneNumberFormat.E164);

			contactNumberArray.set(i, contactNumber);

		}
		Log.i("list of phone number arrays ", contactNumberArray.toString());
		Log.i("list of name  arrays ", contactNameArray.toString());
	}
}
