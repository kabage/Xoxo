package com.xoxo;

import java.io.IOException;

import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.xoxo.backend.RateFriend;

public class SayMoreActivity extends SherlockActivity {

	EditText etSayMore;
	TextView tvSayMore;
	ImageView ivDancingSkullMan;
	Button bDone;
	String jid, points, name, pointsDescription;
	LinearLayout layout;
	private GifAnimationDrawable dancingSkullMan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.say_more_activity);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getBundle();
		setUp();

	}

	private void getBundle() {
		// TODO Auto-generated method stub
		Bundle b = getIntent().getExtras();
		jid = b.getString("jid");
		points = b.getString("points");
		name = b.getString("name");
	}

	private void setUp() {
		// TODO Auto-generated method stub
		layout = (LinearLayout) findViewById(R.id.lvSayMoreActivityLayout);
		try {
			dancingSkullMan = new GifAnimationDrawable(getResources()
					.openRawResource(R.raw.dancing_skull_man));
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		etSayMore = (EditText) findViewById(R.id.etSayMore);
		tvSayMore = (TextView) findViewById(R.id.tvSayMore);
		ivDancingSkullMan = (ImageView) findViewById(R.id.ivDancingSkullMan);
		ivDancingSkullMan.setVisibility(View.INVISIBLE);
		if (points.equals("0")) {
			tvSayMore.setText("No points for...");
		} else if (points.equals("1")) {
			tvSayMore.setText("This point is for...");
		} else {
			tvSayMore.setText("These " + points + " points are for...");
		}
		bDone = (Button) findViewById(R.id.bDone);
		bDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pointsDescription = etSayMore.getText().toString();
				if (pointsDescription.length() > 0) {
					RateFriend.rate(jid, points, pointsDescription);
					PointsSentCustomDialog pscDialog = new PointsSentCustomDialog(
							SayMoreActivity.this);
					pscDialog.points = points;
					pscDialog.sendPointsTo = "points to " + name;
					pscDialog.pointsFor = pointsDescription;

					pscDialog.getWindow().setBackgroundDrawable(
							new ColorDrawable(Color.TRANSPARENT));
					pscDialog.show();
					layout.setBackgroundColor(Color.WHITE);
					tvSayMore.setText("Oh yeah!");
					tvSayMore.setTextColor(Color.rgb(109, 39, 137));
					tvSayMore.setTextSize(40);
					etSayMore.setFocusable(false);
					etSayMore.setVisibility(View.INVISIBLE);

					ivDancingSkullMan.setVisibility(View.VISIBLE);

					ivDancingSkullMan.getLayoutParams().height = 400;
					ivDancingSkullMan.getLayoutParams().width = 400;
					ivDancingSkullMan.setImageDrawable(dancingSkullMan);

					dancingSkullMan.setVisible(true, true);

					bDone.setVisibility(View.INVISIBLE);
				} else {
					etSayMore.setError("Please say something...");
				}
			}
		});
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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// RateFriend.leaveNotificationChannel();
		finish();
	}

}
