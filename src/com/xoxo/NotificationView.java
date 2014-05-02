package com.xoxo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.xoxo.backend.RosterRetrieval;

public class NotificationView extends SherlockActivity implements
		OnClickListener {

	public SherlockActivity a;
	public Dialog d;
	public Button positive;
	public String sayMore, senderName;
	public TextView tvRecievedPoints, tvPointsFrom, tvPointsDescription;
	public String receivedPoints, pointsFrom, pointsDescription;
	JSONObject dataObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature((int) Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notification_view);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getBundle();
		setUp();

	}

	private void getBundle() {
		// TODO Auto-generated method stub
		Bundle b = getIntent().getExtras();
		String data = b.getString("notificationData");
		try {
			dataObject = new JSONObject(data);
		} catch (JSONException e) {
			Log.e("an error occured converting data to json", e.toString());
		}
		try {
			pointsFrom = dataObject.getString("from");
			String jid = pointsFrom.replaceAll("/Smack", "");
			int position = RosterRetrieval.jids.indexOf(jid);
			senderName = RosterRetrieval.names.get(position);
			pointsDescription = dataObject.getString("reasons");
			receivedPoints = dataObject.getString("points");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setUp() {
		// TODO Auto-generated method stub
		tvRecievedPoints = (TextView) findViewById(R.id.tvReceivedPoints);
		tvRecievedPoints.setText(receivedPoints);
		tvPointsFrom = (TextView) findViewById(R.id.tvPointsFrom);
		tvPointsFrom.setText("from " + senderName);
		tvPointsDescription = (TextView) findViewById(R.id.tvPointsDescription);
		tvPointsDescription.setText("for " + pointsDescription);

		positive = (Button) findViewById(R.id.bBack);
		positive.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bBack:
			closeNotificationView();
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		closeNotificationView();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void closeNotificationView() {
		if (Dashboard.open==true) {
			finish();
		} else {
			Intent intent = new Intent(NotificationView.this, Dashboard.class);
			startActivity(intent);
		}
	}
}
