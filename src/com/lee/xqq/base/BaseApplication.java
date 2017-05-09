package com.lee.xqq.base;


import android.app.Application;
import android.os.Handler;

public class BaseApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		BroadSender.init(getApplicationContext());
		MLog.init(getApplicationContext(), new Handler());
	}

}
