package com.xoxo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.xoxo.backend.RosterRetrieval;

public class PointReasons extends SherlockActivity {

	ArrayList<String> reasonsArray, friendNameArray, receivedPointsArray;
	ArrayList<TextListItem> reasonItem;
	TextView tvNoListItem;
	TextItemAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.point_reasons);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setUp();
		getData();
		populateListView();
	}

	private void setUp() {
		// TODO Auto-generated method stub
		tvNoListItem = (TextView) findViewById(R.id.tvNoListItem);
	}

	@SuppressWarnings("unchecked")
	private void getData() {
		// TODO Auto-generated method stub
		SharedPreferences sp = getSharedPreferences(getPackageName(),
				MODE_PRIVATE);

		reasonsArray = new ArrayList<String>();
		friendNameArray = new ArrayList<String>();
		receivedPointsArray = new ArrayList<String>();

		try {
			reasonsArray = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("reasons",
							ObjectSerializer.serialize(new ArrayList<String>())));
			friendNameArray = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("friends",
							ObjectSerializer.serialize(new ArrayList<String>())));

			receivedPointsArray = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("points",
							ObjectSerializer.serialize(new ArrayList<String>())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Collections.reverse(receivedPointsArray);
		Collections.reverse(reasonsArray);
		Collections.reverse(friendNameArray);

	}

	private void populateListView() {
		// TODO Auto-generated method stub
		ListView listView = (ListView) findViewById(R.id.lvListItems);
		reasonItem = new ArrayList<TextListItem>();
		ArrayList<String> jids = RosterRetrieval.jids;
		ArrayList<String> names = RosterRetrieval.names;
		ArrayList<String> userNames = new ArrayList<String>();

		for (int i = 0; i < friendNameArray.size(); i++) {
			String jid = friendNameArray.get(i).replaceAll("/Smack", "");
			int namePosition = jids.indexOf(jid);
			Log.i("logging the position of jid", String.valueOf(namePosition));
			String userName = names.get(namePosition);
			userNames.add(userName);

		}

		if (receivedPointsArray.size() == 0) {
			tvNoListItem.setText("You have no history :-(");
		}

		for (int i = 0; i < reasonsArray.size(); i++) {
			String reason = reasonsArray.get(i);
			if (reason.length() > 30) {
				reason = reason.substring(0, 30) + "...";
			}
			reasonItem.add(new TextListItem(userNames.get(i), reason,
					receivedPointsArray.get(i)));
		}

		adapter = new TextItemAdapter(getApplicationContext(),
				R.layout.text_list_item, reasonItem);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						NotificationView.class);

				JSONObject object = new JSONObject();

				try {
					object.put("from", friendNameArray.get(arg2));
					object.put("reasons", reasonsArray.get(arg2));
					object.put("points", receivedPointsArray.get(arg2));
				} catch (JSONException e) {
					Log.e("error creating json object ", e.toString());
				}

				Bundle b = new Bundle();
				b.putString("notificationData", object.toString());
				i.putExtras(b);
				startActivity(i);
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
		adapter.notifyDataSetChanged();
	}

}
