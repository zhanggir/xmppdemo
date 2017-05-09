package com.lee.xqq;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lee.xqq.base.BaseFragment;
import com.lee.xqq.fragment.HomeFragment;
import com.lee.xqq.fragment.LoginFragmen;
import com.lee.xqq.fragment.MenuFragment;
import com.lee.xqq.fragment.MenuFragment.MenuListOnItemClickListener;
import com.lee.xqq.fragment.RegisterFragment;
import com.lee.xqq.fragment.SettingFragment;
import com.lee.xqq.sliding.SlidingActivity;
import com.lee.xqq.sliding.SlidingMenu;

/**
 * 该Demo仅在Android4.0版本及以上设备才能正常运行
 * 
 * @author leehom
 * 
 */
public class MainActivity extends SlidingActivity implements
		MenuListOnItemClickListener {

	private static SlidingMenu mSlidingMenu;
	private static BaseFragment curFragment;
	private static MenuFragment menuFragment;
	private static MainActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.frame_content);
		setBehindContentView(R.layout.frame_menu);
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setShadowDrawable(R.drawable.drawer_shadow);// 设置阴影图片
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width); // 设置阴影图片的宽度
		int value = getWindowManager().getDefaultDisplay().getWidth() >> 1;
		mSlidingMenu.setBehindOffset(value);
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		curFragment = LoginFragmen.getFragmen();
		menuFragment = new MenuFragment();
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.menu, menuFragment);
		fragmentTransaction.replace(R.id.content, curFragment);
		fragmentTransaction.commit();
		switchFragment(curFragment, 1);
	}

	@Override
	public void selectItem(int position, String title) {
		// update the main content by replacing fragments
		BaseFragment fragment = null;
		switch (position) {
		case 0:
			fragment = HomeFragment.getFragment();
			break;
		case 1:
			fragment = LoginFragmen.getFragmen();
			break;
		case 2:
			fragment = RegisterFragment.getFragment();
			break;
		case 3:
			fragment = SettingFragment.getFragment();
			break;
		case 4:
			showExitDialog();
			break;
		}
		switchFragment(fragment, position);
	}

	private void showExitDialog() {
		final Dialog dialog = showDialog(R.layout.dialog_exit_layout, true);
		final Button ok = (Button) dialog.findViewById(R.id.ok);
		final Button cancel = (Button) dialog.findViewById(R.id.cancel);
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (v == ok) {
					exit();
				}
			}

		};
		ok.setOnClickListener(listener);
		cancel.setOnClickListener(listener);
	}

	public static BaseFragment getCurFragment() {
		return curFragment;
	}

	public static void switchFragment(BaseFragment fragment, int index) {
		if (fragment != null) {
			curFragment = fragment;
			FragmentManager fragmentManager = activity.getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content, fragment)
					.commit();
			mSlidingMenu.showContent();
			menuFragment.setItemChecked(index, true);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		String tip = getString(R.string.string_again_touch_exit_message);
		doubleExit(tip, 3000);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		if (getCurFragment() != null)
			getCurFragment().onClick(view);
		if (view.getId() == R.id.option) {
			mSlidingMenu.showMenu();
		}
	}

	@Override
	public void receiver(Intent intent) {
		// TODO Auto-generated method stub
		super.receiver(intent);
		if (isCurrentBroadcast(intent)) {
			if (getCurFragment() != null)
				getCurFragment().receiver(intent);
		}
	}

}
