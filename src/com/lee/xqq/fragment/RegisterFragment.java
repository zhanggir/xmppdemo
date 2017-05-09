package com.lee.xqq.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lee.xqq.MainActivity;
import com.lee.xqq.R;
import com.lee.xqq.base.BaseFragment;
import com.lee.xqq.base.MLog;

public class RegisterFragment extends BaseFragment {

	private static RegisterFragment fragment;
	EditText userName;
	EditText userPwd;
	EditText userPwdConfirm;

	public static RegisterFragment getFragment() {
		if (fragment == null)
			fragment = new RegisterFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_register);
		userName = (EditText) findViewById(R.id.username);
		userPwd = (EditText) findViewById(R.id.userpwd);
		userPwdConfirm = (EditText) findViewById(R.id.userpwd_confirm);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		String pwd = userPwd.getText().toString();
		String pwdc = userPwdConfirm.getText().toString();
		if (pwd.equals(pwdc)) {
			String name = userName.getText().toString();
			if (dataUtils.register(name, pwd)) {
				getBaseActivity().putString("username", name);
				getBaseActivity().putString("userpwd", pwd);
				MainActivity.switchFragment(LoginFragmen.getFragmen(), 1);
			}
		} else {
			MLog.makeText("两次密码输入不一致");
		}
	}

}
