package com.xoxo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.tjeannin.apprate.AppRate;
import com.xoxo.backend.InitializeConnection;
import com.xoxo.backend.ListenerService;
import com.xoxo.backend.RosterRetrieval;

public class Dashboard extends SherlockActivity {

	ListView listView;
	AlertDialog.Builder builder;
	AppRate rate;
	Intent i;
	static public Handler myHandler;
	String status;
	ArrayList<String> cachedNames, cachedJids, cachedStatuses;
	SharedPreferences sp;
	boolean isNetwork;

	public static boolean active = false;
	public static boolean open = false;
	boolean cached = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		open = true;
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.dashboard);

		getSupportActionBar().setHomeButtonEnabled(true);
		setSupportProgressBarIndeterminateVisibility(false);

		isNetwork = CheckForNetwork
				.checkNetworkConnection(getApplicationContext());
		if (isNetwork == true) {
			cached = checkForCachedData();
			if (cached == true) {
				populateListView();
			}
			createHandler();
			rateApp();
		} else {
			InternetAlertDialog.showDialog(Dashboard.this);
		}

	}

	@SuppressWarnings("unchecked")
	private boolean checkForCachedData() {
		// TODO Auto-generated method stub
		sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);
		cachedNames = new ArrayList<String>();
		cachedJids = new ArrayList<String>();
		cachedStatuses = new ArrayList<String>();

		try {
			cachedNames = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("names",
							ObjectSerializer.serialize(new ArrayList<String>())));
			cachedJids = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("jids",
							ObjectSerializer.serialize(new ArrayList<String>())));
			cachedStatuses = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("statuses",
							ObjectSerializer.serialize(new ArrayList<String>())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (cachedNames.size() > 0 && cachedJids.size() > 0
				&& cachedStatuses.size() > 0) {
			cached = true;

		} else {
			cached = false;

		}

		return cached;
	}

	@SuppressWarnings("unchecked")
	private void populateListView() {
		// TODO Auto-generated method stub
		sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);
		ListView listView = (ListView) findViewById(R.id.lvListItems);

		cachedNames = new ArrayList<String>();
		cachedJids = new ArrayList<String>();
		cachedStatuses = new ArrayList<String>();

		try {
			cachedNames = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("names",
							ObjectSerializer.serialize(new ArrayList<String>())));
			cachedJids = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("jids",
							ObjectSerializer.serialize(new ArrayList<String>())));
			cachedStatuses = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("statuses",
							ObjectSerializer.serialize(new ArrayList<String>())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<IconListItem> userFriend = new ArrayList<IconListItem>();

		for (int i = 0; i < cachedNames.size(); i++) {
			userFriend.add(new IconListItem(cachedNames.get(i), cachedStatuses
					.get(i), randomHexInt(), randomHexInt(), randomHexInt()));
		}

		IconListItemAdapter adapter = new IconListItemAdapter(
				getApplicationContext(), R.layout.icon_list_item, userFriend);

		listView.setAdapter(adapter);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		active = true;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		active = true;
	}

	private void createHandler() {
		setSupportProgressBarIndeterminateVisibility(true);

		if (ListenerService.serviceStarted == true) {
			configureList();
		} else {
			final int DO_UPDATE_TEXT = 0;
			final int DO_THAT = 1;

			myHandler = new Handler() {
				public void handleMessage(Message msg) {
					final int what = msg.what;
					switch (what) {
					case DO_UPDATE_TEXT:
						configureList();

						Intent in = new Intent(Dashboard.this,
								ListenerService.class);
						startService(in);

						Log.i("message received", "message received ");
						break;
					case DO_THAT:
						// doThat();
						break;
					}
				}
			};
			InitializeConnection.initialize(Dashboard.this, myHandler);
		}

	}

	private void configureList() {

		ListView list = (ListView) findViewById(R.id.lvListItems);
		loadingImage l = new loadingImage(Dashboard.this, list);
		l.execute();
		setSupportProgressBarIndeterminateVisibility(false);
	}

	private void rateApp() {
		// TODO Auto-generated method stub
		builder = new AlertDialog.Builder(Dashboard.this);
		rate = new AppRate(Dashboard.this);
		builder.setTitle("Rate Xoxo!")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("Please rate the app. Tell us what you think.")
				.setPositiveButton("Yes", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent i = new Intent(
								Intent.ACTION_VIEW,
								Uri.parse("https://play.google.com/store/apps/details?=com.xoxo"));
						startActivity(i);
						AppRate.reset(Dashboard.this);
						rate.setMinDaysUntilPrompt(120);
					}
				}).setNeutralButton("Later", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						AppRate.reset(Dashboard.this);
						rate.setMinDaysUntilPrompt(3);
					}
				}).setNegativeButton("No", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						AppRate.reset(Dashboard.this);
						rate.setMinDaysUntilPrompt(10);
					}
				});

		rate.setShowIfAppHasCrashed(false).setMinLaunchesUntilPrompt(20)
				.setCustomDialog(builder).init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.dashboard_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			i = new Intent(getApplicationContext(), About.class);
			startActivity(i);
			break;
		case R.id.mPoints:
			isNetwork = CheckForNetwork
					.checkNetworkConnection(getApplicationContext());
			if (isNetwork == true) {
				i = new Intent(getApplicationContext(), Points.class);
				startActivity(i);
			} else {
				InternetAlertDialog.showDialog(Dashboard.this);
			}

			break;
		case R.id.mStatus:
			isNetwork = CheckForNetwork
					.checkNetworkConnection(getApplicationContext());
			if (isNetwork == true) {
				i = new Intent(getApplicationContext(), SetStatusActivity.class);
				startActivity(i);
			} else {
				InternetAlertDialog.showDialog(Dashboard.this);
			}
			break;
		case R.id.mRefresh:
			isNetwork = CheckForNetwork
					.checkNetworkConnection(getApplicationContext());
			if (isNetwork == true) {
				refreshRoster();
			} else {
				InternetAlertDialog.showDialog(Dashboard.this);
			}
			break;
		case R.id.mShareApp:
			i = new Intent(android.content.Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(android.content.Intent.EXTRA_TEXT,
					"Join me on Xoxo! https://play.google.com/store/apps/details?=com.xoxo");
			startActivity(Intent.createChooser(i, "Share using..."));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {

		// TODO Auto-generated method stub
		super.onDestroy();
		open = false;
		cacheData();

	}

	@SuppressWarnings("unchecked")
	public void cacheData() {
		SharedPreferences sp = getSharedPreferences(getPackageName(),
				MODE_PRIVATE);
		cachedNames = new ArrayList<String>();
		cachedJids = new ArrayList<String>();
		cachedStatuses = new ArrayList<String>();

		try {
			cachedNames = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("names",
							ObjectSerializer.serialize(new ArrayList<String>())));
			cachedJids = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("jids",
							ObjectSerializer.serialize(new ArrayList<String>())));
			cachedStatuses = (ArrayList<String>) ObjectSerializer
					.deserialize(sp.getString("statuses",
							ObjectSerializer.serialize(new ArrayList<String>())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cachedNames = RosterRetrieval.names;
		cachedJids = RosterRetrieval.jids;
		cachedStatuses = RosterRetrieval.statuses;

		try {
			sp.edit()
					.putString("names", ObjectSerializer.serialize(cachedNames))
					.commit();
			sp.edit().putString("jids", ObjectSerializer.serialize(cachedJids))
					.commit();
			sp.edit()
					.putString("statuses",
							ObjectSerializer.serialize(cachedStatuses))
					.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void refreshRoster() {

		Intent intent = getIntent();
		finish();
		startActivity(intent);
		loadingImage.refreshList();
	}

	private int randomHexInt() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int hexadecimal = rand.nextInt(255);
		return hexadecimal;
	}

}

class loadingImage extends AsyncTask<String, Void, String> {
	ArrayList<String> friendNames;
	ArrayList<String> Statuses;
	static Handler handler;
	Context context;
	static ListView listView;
	Intent i;
	static IconListItemAdapter adapter;
	int friendActionItemPosition;
	ActionMode mMode;

	public loadingImage(Context mcontext, ListView list) {

		context = mcontext;
		listView = list;

	}

	@Override
	protected String doInBackground(String... params) {

		friendNames = RosterRetrieval.retrieve();

		return null;

	}

	@Override
	protected void onPostExecute(String result) {

		super.onPostExecute(result);

		ArrayList<IconListItem> userFriend = new ArrayList<IconListItem>();
		final ArrayList<String> names = RosterRetrieval.names;
		final ArrayList<String> jids = RosterRetrieval.jids;
		final ArrayList<String> statuses = RosterRetrieval.statuses;

		for (int i = 0; i < names.size(); i++) {
			String status = statuses.get(i);
			if (status.length() > 30) {
				status = status.substring(0, 30) + "...";
			}
			userFriend.add(new IconListItem(names.get(i), status,
					randomHexInt(), randomHexInt(), randomHexInt()));
		}

		adapter = new IconListItemAdapter(context, R.layout.icon_list_item,
				userFriend);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				i = new Intent(context, Xo.class);
				Bundle b = new Bundle();
				b.putString("friendjid", jids.get(arg2));
				b.putString("friendName", names.get(arg2));
				b.putString("status", statuses.get(arg2));
				i.putExtras(b);
				context.startActivity(i);
			}
		});

	}

	private int randomHexInt() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int hexadecimal = rand.nextInt(255);
		return hexadecimal;
	}

	public static void refreshList() {

		final int DO_UPDATE_TEXT = 0;
		final int DO_THAT = 1;

		handler = new Handler() {
			public void handleMessage(Message msg) {
				final int what = msg.what;
				switch (what) {
				case DO_UPDATE_TEXT:
					listView.invalidateViews();
					adapter.notifyDataSetChanged();
					Log.i("message receivedin refresh", "message received ");
					break;
				case DO_THAT:
					// doThat();
					break;
				}
			}
		};
		handler.sendEmptyMessage(0);

	}

}
