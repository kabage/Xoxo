package com.xoxo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;

public class PointsSentCustomDialog extends Dialog implements
		android.view.View.OnClickListener {

	public SherlockActivity a;
	public Dialog d;
	public Button positive;
	public String sayMore;
	public TextView tvPointsSent, tvSendPointsTo, tvPointsFor;
	public String points, sendPointsTo, pointsFor;

	public PointsSentCustomDialog(SherlockActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.a = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature((int) Window.FEATURE_NO_TITLE);
		setContentView(R.layout.points_sent_custom_dialog);

		tvPointsSent = (TextView) findViewById(R.id.tvPointsSent);
		tvPointsSent.setText(points);
		tvSendPointsTo = (TextView) findViewById(R.id.tvSendPointsTo);
		tvSendPointsTo.setText(sendPointsTo);
		tvPointsFor = (TextView) findViewById(R.id.tvPointsFor);

		if (pointsFor != null) {
			tvPointsFor.setText("for " + pointsFor);
		}

		positive = (Button) findViewById(R.id.bOk);
		positive.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bOk:
			dismiss();
			break;
		}
	}

}
