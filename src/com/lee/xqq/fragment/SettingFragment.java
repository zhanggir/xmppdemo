package com.lee.xqq.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lee.xqq.MainActivity;
import com.lee.xqq.R;
import com.lee.xqq.base.BaseActivity;
import com.lee.xqq.base.BaseFragment;

public class SettingFragment extends BaseFragment {

	private static SettingFragment fragment;
	EditText host;
	EditText port;

	public static SettingFragment getFragment() {
		if (fragment == null)
			fragment = new SettingFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_setting);
		host = (EditText) findViewById(R.id.host);
		port = (EditText) findViewById(R.id.port);
		BaseActivity activity = getBaseActivity();
		String host = activity.getString("host", "www.ithtw.com");
		int port = activity.getInt("port", 5222);
		this.host.setText(host);
		this.port.setText(String.valueOf(port));
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		String host = this.host.getText().toString();
		int port = Integer.parseInt(this.port.getText().toString());
		BaseActivity activity = getBaseActivity();
		activity.putString("host", host);
		activity.putInt("port", port);
		this.host.setText(host);
		this.port.setText(String.valueOf(port));
		MainActivity.switchFragment(LoginFragmen.getFragmen(), 1);
		dataUtils.setHost(host, port);
		dataUtils.initXMPP();
	}

}
