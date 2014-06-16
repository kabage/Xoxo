package com.xoxo.backend;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ListView;

import com.actionbarsherlock.view.ActionMode;
import com.xoxo.Dashboard;
import com.xoxo.IconListItemAdapter;

public class loadingImage extends AsyncTask<String, Void, String> {
	ArrayList<String> friendNames;
	ArrayList<String> Statuses;
	static Handler handler;
	Context context;
	public static ListView listView;
	Intent i;
	public static IconListItemAdapter adapter = Dashboard.adapter;
	int friendActionItemPosition;
	ActionMode mMode;

	@Override
	protected String doInBackground(String... params) {

		friendNames = RosterRetrieval.retrieve();
		return null;

	}

	@Override
	protected void onPostExecute(String result) {

		super.onPostExecute(result);
		Dashboard.handler.sendEmptyMessage(0);

	}

}