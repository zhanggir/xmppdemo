package com.lee.xqq.base;

import android.content.Context;
import android.content.Intent;

public class BroadSender {

	private static Context context;
	public static final String TARGET = "target";
	public static final String ACTION_RECEIVER = "action.globle.receiver";

	public static void init(Context context) {
		BroadSender.context = context;
	}

	// 向目标activity发送广播
	public static void sendBroadcast(Class<?> clazz, String... params) {
		if (clazz == null)
			return;
		String target = clazz.getName();
		Intent intent = new Intent(ACTION_RECEIVER);
		intent.putExtra(TARGET, target);
		if (params.length % 2 == 0) {
			for (int i = 0; i < params.length; i += 2)
				intent.putExtra(params[i], params[i + 1]);
		}
		context.sendBroadcast(intent);
	}

}
