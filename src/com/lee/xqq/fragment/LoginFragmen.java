package com.lee.xqq.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lee.xqq.MainActivity;
import com.lee.xqq.R;
import com.lee.xqq.base.BaseFragment;

public class LoginFragmen extends BaseFragment {

	private static LoginFragmen fragmen;
	EditText userName;
	EditText userPwd;

	public static LoginFragmen getFragmen() {
		if (fragmen == null)
			fragmen = new LoginFragmen();
		return fragmen;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_login);
		userName = (EditText) findViewById(R.id.username);
		userPwd = (EditText) findViewById(R.id.userpwd);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String name = getBaseActivity().getString("username", "");
		String pwd = getBaseActivity().getString("userpwd", "");
		userName.setText(name);
		userPwd.setText(pwd);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.login:
			String name = userName.getText().toString();
			String pwd = userPwd.getText().toString();
			if (dataUtils.login(name, pwd)) {
				getBaseActivity().putString("username", name);
				getBaseActivity().putString("userpwd", pwd);
				setTitle(dataUtils.getUser());
				MainActivity.switchFragment(HomeFragment.getFragment(), 0);
			}
			break;
		}
	}

}
