package com.lee.xqq.base;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class MLog {

	public static boolean debug = true;
	private static final String TAG = MLog.class.getName();
	private static Context context;
	private static Handler handler;

	public static void init(Context context, Handler handler) {
		MLog.context = context;
		MLog.handler = handler;
	}

	public static void logInfo(String text) {
		if (debug)
			Log.i(TAG, text);
	}

	public static void logInfo(String tag, String text) {
		if (debug)
			Log.i(tag, text);
	}

	public static void makeText(final StringBuffer text){
		makeText(text.toString());
	}
	
	public static void makeText(final String text) {
		if (context == null || handler == null)
			return;
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(context, text, Toast.LENGTH_LONG).show();
			}
		});
	}
}
