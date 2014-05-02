package com.xoxo;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TextItemAdapter extends ArrayAdapter<TextListItem> {
	private ArrayList<TextListItem> objects;

	public TextItemAdapter(Context context, int textViewResourceId,
			ArrayList<TextListItem> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.text_list_item, null);
		}

		TextListItem i = objects.get(position);
		if (i != null) {

			TextView tvPointsReason = (TextView) v
					.findViewById(R.id.tvPointsReason);
			TextView tvFriendName = (TextView) v
					.findViewById(R.id.tvFriendName);
			TextView tvPointsReceived = (TextView) v
					.findViewById(R.id.tvPointsReceived);

			if (tvPointsReason != null) {
				tvPointsReason.setText(i.getPointsReason());
			}
			if (tvFriendName != null) {
				tvFriendName.setText(i.getFriendName());
			}
			if (tvPointsReceived != null) {
				tvPointsReceived.setText(i.getPointsRecieved());
			}

		}
		return v;
	}
}