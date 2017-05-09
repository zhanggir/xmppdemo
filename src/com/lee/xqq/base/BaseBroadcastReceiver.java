package com.lee.xqq.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BaseBroadcastReceiver extends BroadcastReceiver {

	private BaseActivity activity;

	public BaseBroadcastReceiver(BaseActivity activity) {
		super();
		this.activity = activity;
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadSender.ACTION_RECEIVER);
		activity.registerReceiver(this, filter);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String target = intent.getStringExtra(BroadSender.TARGET);
		if (activity != null && target.equals(activity.getName()))
			activity.receiver(intent);
	}

	public void unregisterReceiver() {
		if (activity != null)
			activity.unregisterReceiver(this);
	}

}
