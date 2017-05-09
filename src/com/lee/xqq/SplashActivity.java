package com.lee.xqq;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.lee.xqq.base.BaseActivity;

public class SplashActivity extends BaseActivity {

	ImageView load_process;
	AnimationDrawable drawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		load_process = (ImageView) findViewById(R.id.load_process);
		drawable = (AnimationDrawable) load_process.getBackground();

		initSharedPreferences("xmpp");
		String host = getString("host", "192.168.0.166");
		int port = getInt("port", 5222);
		dataUtils.setHost(host, port);
		dataUtils.initXMPP();// 初始XMPP连接
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!drawable.isRunning())
			drawable.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		drawable.stop();
	}

	@Override
	public void receiver(Intent intent) {
		// TODO Auto-generated method stub
		super.receiver(intent);
		if (isCurrentBroadcast(intent)) {
			// 收到连接成功广播
			startActivity(MainActivity.class);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.exit(0);
	}
}
