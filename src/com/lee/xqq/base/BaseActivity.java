package com.lee.xqq.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.lee.xqq.control.CacheUtils;
import com.lee.xqq.control.DataUtils;
import com.lee.xqq.util.TimerDialog;

public class BaseActivity extends Activity {

	private BaseHandler handler;
	private BaseBroadcastReceiver receiver;
	protected static SharedPreferences preferences;
	private static final String DBNAME = "info";
	private TimerDialog dialogTimer;
	// 后台逻辑，数据操作对象
	protected DataUtils dataUtils;
	// 后台数据缓存操作对象
	protected CacheUtils cacheUtils;
	private static boolean exit = false;
	private long lastTime;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		onCreate(savedInstanceState, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	protected void onCreate(Bundle savedInstanceState, int requestedOrientation) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(requestedOrientation);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		handler = new BaseHandler(this);
		if (preferences == null)
			preferences = getSharedPreferences(DBNAME, MODE_PRIVATE);
		dataUtils = DataUtils.getInstance();
		cacheUtils = CacheUtils.getInstance();
	}

	/**
	 * 初始数据库
	 * 
	 * @param name
	 */
	public void initSharedPreferences(String name) {
		preferences = null;
		preferences = getSharedPreferences(name, MODE_PRIVATE);
	}

	public void putString(String key, String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void putInt(String key, int value) {
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public String getString(String key, String defValue) {
		return preferences.getString(key, defValue);
	}

	public int getInt(String key, int defValue) {
		return preferences.getInt(key, defValue);
	}

	public void startActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}

	/**
	 * 
	 * @param cls
	 * @param strings
	 *            长度为双数，key和value
	 */
	public void startActivity(Class<?> cls, String... strings) {
		Intent intent = new Intent(this, cls);
		for (int i = 0; i < strings.length; i += 2)
			intent.putExtra(strings[i], strings[i + 1]);
		startActivity(intent);
	}

	public static boolean isExit() {
		return exit;
	}

	public void exit() {
		exit(this);
	}

	public static void exit(Activity activity) {
		exit = true;
		activity.finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (exit) {
			finish();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		unregisterGlobleReceiver();
		receiver = new BaseBroadcastReceiver(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		unregisterGlobleReceiver();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void unregisterGlobleReceiver() {
		if (receiver != null)
			receiver.unregisterReceiver();
		receiver = null;
	}

	protected void dismissProgressDialog() {
		if (dialogTimer != null)
			dialogTimer.dismiss();
		dialogTimer = null;
	}

	public TimerDialog showTimerDialog(int layoutID, boolean cancelble) {
		return showDialog(layoutID, cancelble, Gravity.CENTER);
	}

	public TimerDialog showDialog(int layoutID, boolean cancelble,
			int gravity) {
		return showTimerDialog(layoutID, cancelble, gravity, null);
	}

	public TimerDialog showTimerDialog(int layoutID, boolean cancelble,
			int gravity, OnCancelListener cancelListener) {
		dismissProgressDialog();
		Builder build = new AlertDialog.Builder(this);
		Dialog dialog = build.create();
		dialog.show();
		dialog.setCancelable(cancelble);
		dialog.setCanceledOnTouchOutside(cancelble);
		dialog.getWindow().setGravity(gravity);
		dialog.setContentView(layoutID);
		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		if (cancelListener != null)
			dialog.setOnCancelListener(cancelListener);
		dialogTimer = new TimerDialog(dialog);
		return dialogTimer;
	}

	public Dialog showDialog(int layoutID, boolean cancelble) {
		return showDialog(layoutID, cancelble, Gravity.CENTER, null);
	}

	public Dialog showDialog(int layoutID, boolean cancelble, int gravity,
			OnCancelListener cancelListener) {
		Builder build = new AlertDialog.Builder(this);
		Dialog dialog = build.create();
		dialog.show();
		dialog.setCancelable(cancelble);
		dialog.setCanceledOnTouchOutside(cancelble);
		dialog.getWindow().setGravity(gravity);
		dialog.setContentView(layoutID);
		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		if (cancelListener != null)
			dialog.setOnCancelListener(cancelListener);
		return dialog;
	}

	public String getName() {
		return getClass().getName();
	}

	public BaseHandler getHandler() {
		return handler;
	}

	public void dispatchMessage(Message msg) {
	}

	/**
	 * 界面数据通知更新函数
	 * 
	 * @param msg
	 */
	public void handleMessage(Message msg) {
	}

	/**
	 * 是否为当前activity要接收的广播
	 * 
	 * @param intent
	 * @return
	 */
	public boolean isCurrentBroadcast(Intent intent) {
		return intent.getStringExtra(BroadSender.TARGET).equals(getName());
	}

	/**
	 * 非activity数据通知更新函数,不支持程序后台运行
	 */
	public void receiver(Intent intent) {
	}

	/**
	 * 默认按钮点击事件
	 * 
	 * @param view
	 */
	public void onClick(View view) {
	}

	/**
	 * 双击退出程序
	 * 
	 * @param tip
	 *            提示内容
	 * @param nTime
	 *            双击间隔事件
	 */
	public void doubleExit(String tip, int nTime) {
		// TODO Auto-generated method stub
		long now = System.currentTimeMillis();
		if (now - lastTime < nTime) {
			exit();
		} else {
			lastTime = now;
			MLog.makeText(tip);
		}
	}

}
