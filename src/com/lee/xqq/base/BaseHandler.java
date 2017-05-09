package com.lee.xqq.base;

import android.os.Handler;
import android.os.Message;

public class BaseHandler extends Handler {

	private BaseActivity activity;

	public BaseHandler(BaseActivity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void dispatchMessage(Message msg) {
		// TODO Auto-generated method stub
		super.dispatchMessage(msg);
		if (activity != null)
			activity.dispatchMessage(msg);
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		if (activity != null)
			activity.handleMessage(msg);
	}

}
