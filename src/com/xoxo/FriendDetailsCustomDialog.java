package com.xoxo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;

public class FriendDetailsCustomDialog extends Dialog implements
		android.view.View.OnClickListener {

	public SherlockActivity a;
	public Dialog d;
	public Button positive;
	public String sayMore;
	public TextView tvFriendName, tvFriendPoints, tvFriendStatus;
	public String name, points, status;

	public FriendDetailsCustomDialog(SherlockActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.a = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature((int) Window.FEATURE_NO_TITLE);
		setContentView(R.layout.friend_details_custom_dialog);

		tvFriendName = (TextView) findViewById(R.id.tvFriendName);
		tvFriendName.setText(name);
		tvFriendPoints = (TextView) findViewById(R.id.tvFriendPoints);
		tvFriendPoints.setText(points);
		tvFriendStatus = (TextView) findViewById(R.id.tvFriendStatus);
		tvFriendStatus.setText(status);

		positive = (Button) findViewById(R.id.bBack);
		positive.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bBack:
			dismiss();
			break;
		}
	}

}
