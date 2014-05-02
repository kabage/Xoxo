package com.xoxo;

import java.io.IOException;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.xoxo.backend.Statuses;

public class SetStatusActivity extends SherlockActivity {

	EditText etStatus;
	ImageButton bSetStatus;
	String status;
	ArrayList<String> statuses;
	ListView listView;
	SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_status_activity);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setUp();
		populateList();

	}

	@SuppressWarnings("unchecked")
	private void populateList() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.lvListItems);

		statuses = new ArrayList<String>();

		try {
			statuses = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("statuses",
							ObjectSerializer.serialize(new ArrayList<String>())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		statuses.add("Free meals");
		statuses.add("Awesome movie references");
		statuses.add("Kiss and hugs");
		statuses.add("Cool gear");
		statuses.add("Great taste in music");
		statuses.add("Funniness");
		statuses.add("Reading awesome books");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.status_list_item,
				R.id.tvStatusExample, statuses);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Statuses.setStatus(statuses.get(position));
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.toast,
						(ViewGroup) findViewById(R.id.toast_layout_root));
				TextView text = (TextView) layout.findViewById(R.id.text);
				text.setText("Status set!");
				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(layout);
				toast.show();
				finish();
			}
		});

	}

	private void setUp() {
		// TODO Auto-generated method stub
		sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);
		etStatus = (EditText) findViewById(R.id.etStatus);
		bSetStatus = (ImageButton) findViewById(R.id.bSetStatus);
		bSetStatus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				status = etStatus.getText().toString();
				saveStatus(status);
				if (etStatus.length() > 0) {
					Statuses.setStatus(status);
					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.toast,
							(ViewGroup) findViewById(R.id.toast_layout_root));
					TextView text = (TextView) layout.findViewById(R.id.text);
					text.setText("Status set!");
					Toast toast = new Toast(getApplicationContext());
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(layout);
					toast.show();
					finish();
				} else {
					etStatus.setError("Please say something...");
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void saveStatus(String status) {
		// TODO Auto-generated method stub

		ArrayList<String> statusesArray;
		statusesArray = new ArrayList<String>();

		try {
			statusesArray = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("statuses",
							ObjectSerializer.serialize(new ArrayList<String>())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		statusesArray.add(status);

		try {
			sp.edit()
					.putString("statuses",
							ObjectSerializer.serialize(statusesArray)).commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		finish();
	}

}
