package com.xoxo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.xoxo.backend.Register;
import com.xoxo.backend.SaveOrRetrieveLoginDetails;

public class InitializeUser extends SherlockActivity {

	String phoneNumber;
	Handler myHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.initialize_user);
		setSupportProgressBarIndeterminateVisibility(true);

		getData();
		registerUser();
	}

	private void registerUser() {
		// TODO Auto-generated method stub
		createNewUser(); // Create a new user table
		SharedPreferences sp = getSharedPreferences(getPackageName(),
				MODE_PRIVATE);
		sp.edit().putBoolean("registered", true).commit();

		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast,
				(ViewGroup) findViewById(R.id.toast_layout_root));
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText("Registration done");
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();

	}

	private void createNewUser() {// createVcard(connection, USERNAME);
		// TODO Auto-generated method stub

		final int DO_UPDATE_TEXT = 0;
		final int DO_THAT = 1;
		myHandler = new Handler() {
			public void handleMessage(Message msg) {
				final int what = msg.what;
				switch (what) {
				case DO_UPDATE_TEXT:
					
					Intent i = new Intent(getApplicationContext(),
							Dashboard.class);
					startActivity(i);

					break;
				case DO_THAT:
					// doThat();
					break;
				}
			}
		};
		Register.registerUser(InitializeUser.this, "apersonname", phoneNumber,
				"password", myHandler);
		SaveOrRetrieveLoginDetails.storeUpRegistrationDetails(
				InitializeUser.this, phoneNumber, "password", "personNick");

	}

	private void getData() {
		// TODO Auto-generated method stub
		Bundle b = getIntent().getExtras();
		phoneNumber = b.getString("phoneNumber");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
