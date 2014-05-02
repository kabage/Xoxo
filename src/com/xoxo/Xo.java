package com.xoxo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.xoxo.backend.ViewFriendPoints;

public class Xo extends SherlockActivity implements DialModel.Listener {

	String pointsDescription = null;
	int currentNick, modifiedNick;
	String jid, name, status;
	Intent i;
	boolean isNetwork;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xo);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setUp();
		ViewFriendPoints.viewPoints(jid);
	}

	private void setUp() {
		// TODO Auto-generated method stub
		DialView dial = (DialView) findViewById(R.id.dial);
		dial.getModel().addListener(this);
		Bundle b = getIntent().getExtras();
		jid = b.getString("friendjid");
		name = b.getString("friendName");
		status = b.getString("status");

	}

	@Override
	public void onDialPositionChanged(DialModel sender, int nicksChanged) {
		TextView text = (TextView) findViewById(R.id.tvPointsTo);
		currentNick = sender.getCurrentNick();
		modifyNickCount();
		if (modifiedNick == 0) {
			text.setText(name + " gets no points");
		} else if (modifiedNick == 1) {
			text.setText(name + " gets a point");
		} else {
			text.setText(name + " gets " + modifiedNick + " points");
		}
	}

	private void modifyNickCount() {
		// TODO Auto-generated method stub
		modifiedNick = currentNick - 4;
		if (modifiedNick < 0) {
			modifiedNick = currentNick + 7;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.xo_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.mFriendDetails:
			isNetwork = CheckForNetwork
					.checkNetworkConnection(getApplicationContext());
			if (isNetwork == true) {
				FriendDetailsCustomDialog fdcDialog = new FriendDetailsCustomDialog(
						this);

				fdcDialog.name = "About " + name;
				fdcDialog.points = String
						.valueOf(ViewFriendPoints.friendPoints);
				fdcDialog.status = status;

				fdcDialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(Color.TRANSPARENT));
				fdcDialog.show();
			} else {
				InternetAlertDialog.showDialog(Xo.this);
			}
			break;
		case R.id.mSendPoints:
			isNetwork = CheckForNetwork
					.checkNetworkConnection(getApplicationContext());
			if (isNetwork == true) {
				i = new Intent(getApplicationContext(), SayMoreActivity.class);
				Bundle b = new Bundle();
				b.putString("points", String.valueOf(modifiedNick));
				b.putString("jid", jid);
				b.putString("name", name);
				i.putExtras(b);
				startActivity(i);
			} else {
				InternetAlertDialog.showDialog(Xo.this);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		finish();
	}

}
