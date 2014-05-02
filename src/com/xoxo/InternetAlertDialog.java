package com.xoxo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class InternetAlertDialog {

	public static void showDialog(Context context) {
		new AlertDialog.Builder(context)
				.setTitle("No Internet :-(")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(
						"This sucks but you'll need an internet connection to continue")
				.setPositiveButton("Alright",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
	}

}
