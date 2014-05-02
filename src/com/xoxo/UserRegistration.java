package com.xoxo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;

public class UserRegistration extends SherlockActivity {

	EditText etPhoneNumber;
	String phoneNumber;
	Button bRegisterUser;
	boolean valid = false;
	boolean haveConnectedWifi = false;
	boolean haveConnectedMobile = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.user_registration);
		setSupportProgressBarIndeterminateVisibility(false);

		setUp();
	}

	private void getData() {
		// TODO Auto-generated method stub
		phoneNumber = etPhoneNumber.getText().toString().trim();

		if (phoneNumber.startsWith("+") && phoneNumber.length() > 10) {
			etPhoneNumber.setError(null);
			valid = true;
		} else {
			etPhoneNumber
					.setError("Please enter a valid phone number,  start with your country code");
			valid = false;
		}
	}

	private boolean checkNetworkConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	private void setUp() {
		// TODO Auto-generated method stub
		etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
		bRegisterUser = (Button) findViewById(R.id.bRegisterUser);
		bRegisterUser.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getData();
				checkNetworkConnection();
				if (haveConnectedMobile == true || haveConnectedWifi == true) {
					if (valid == true) {
						new AlertDialog.Builder(UserRegistration.this)
								.setTitle("Confirmation")
								.setIcon(android.R.drawable.ic_dialog_alert)
								.setMessage(
										"Silk will now send a confirmation text message which may be charged by your service provider")
								.setPositiveButton("Got it",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												confirmPhoneNumber();
											}
										}).show();
					}
				} else {
					new AlertDialog.Builder(UserRegistration.this)
							.setTitle("No Internet")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setMessage(
									"You'll need an internet connection to register")
							.setPositiveButton("Alright",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();
				}
			}
		});
	}

	private void confirmPhoneNumber() {
		// TODO Auto-generated method stub
		setSupportProgressBarIndeterminateVisibility(true);
		try {
			final SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phoneNumber, null, phoneNumber, null, null);
			startReciever();

		} catch (Exception e) {
			// TODO: handle exception
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.toast,
					(ViewGroup) findViewById(R.id.toast_layout_root));
			TextView text = (TextView) layout.findViewById(R.id.text);
			text.setText("Woops! Something went wrong. Please try again.");
			Toast toast = new Toast(getApplicationContext());
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setView(layout);
			toast.show();
		}
	}

	private void startReciever() {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		Bundle b = new Bundle();
		i.putExtras(b);
		i.setClass(getApplicationContext(), SmsListener.class);
		this.sendBroadcast(i);

		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast,
				(ViewGroup) findViewById(R.id.toast_layout_root));
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText("Confirming your number...");
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
