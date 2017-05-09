package com.lee.xqq.system;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.lee.xqq.util.CachedThreadPool;

public class LocalService extends Service implements Runnable {

	private AppBinder binder;
	private boolean running;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		if (binder == null)
			binder = new AppBinder();
		return binder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		running = true;
		CachedThreadPool.execute(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public class AppBinder extends Binder {
		LocalService getService() {
			return LocalService.this;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (running) {
			}
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
