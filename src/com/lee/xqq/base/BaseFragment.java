package com.lee.xqq.base;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lee.xqq.R;
import com.lee.xqq.control.CacheUtils;
import com.lee.xqq.control.DataUtils;

public abstract class BaseFragment extends Fragment {

	private View mView;
	protected DataUtils dataUtils;
	protected CacheUtils cacheUtils;
	protected Handler handler;
	private TextView titleText;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataUtils = DataUtils.getInstance();
		cacheUtils = CacheUtils.getInstance();
		handler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				// TODO Auto-generated method stub
				return BaseFragment.this.handleMessage(msg);
			}
		});
	}

	public void setContentView(int resID) {
		mView = getActivity().getLayoutInflater().inflate(resID, null);
		titleText = (TextView) findViewById(R.id.title_text);
	}

	public void setTitle(String title) {
		if (titleText != null)
			titleText.setText(title);
	}

	public final View findViewById(int id) {
		return mView.findViewById(id);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return mView;
	}

	public BaseActivity getBaseActivity() {
		return (BaseActivity) getActivity();
	}

	public abstract void onClick(View view);

	public boolean handleMessage(Message msg) {
		return false;
	}

	public void receiver(Intent intent) {
	}

}
