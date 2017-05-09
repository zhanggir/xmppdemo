package com.lee.xqq.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lee.xqq.R;
import com.lee.xqq.adapter.MenuAdapter;
import com.lee.xqq.base.BaseFragment;
import com.lee.xqq.item.MenuItem;

public class MenuFragment extends BaseFragment implements OnItemClickListener {

	protected MenuListOnItemClickListener mCallback;
	protected int selected = -1;
	protected MenuAdapter menuAdapter;
	protected ListView menuListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_menu);
		List<MenuItem> list = new ArrayList<MenuItem>();
		list.add(new MenuItem("首页", R.drawable.ic_home));
		list.add(new MenuItem("登录", R.drawable.ic_pages));
		list.add(new MenuItem("注册", R.drawable.ic_people));
		list.add(new MenuItem("设置", R.drawable.ic_photos));
		list.add(new MenuItem("退出", R.drawable.ic_whats_hot));
		menuAdapter = new MenuAdapter(getActivity(), list, R.layout.item_menu);
		menuListView = (ListView) findViewById(R.id.list_menu);
		menuListView.setAdapter(menuAdapter);
		menuListView.setOnItemClickListener(this);
		setItemChecked(1, true);
	}

	public void setItemChecked(int position, boolean value) {
		if (menuListView != null)
			menuListView.setItemChecked(position, value);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mCallback != null)
			mCallback.selectItem(position, menuAdapter.getItem(position)
					.getTitle());
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (MenuListOnItemClickListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	public interface MenuListOnItemClickListener {

		public void selectItem(int position, String title);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

	}
}
