package com.xoxo.backend;

import android.os.AsyncTask;
import android.util.Log;

import com.xoxo.Dashboard;

public class RefreshStatuses extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		RosterRetrieval.retrieve();
		return null;

	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Log.i("elements in collection", RosterRetrieval.names.toString());

		Dashboard.handler.sendEmptyMessage(0);

	}

}
