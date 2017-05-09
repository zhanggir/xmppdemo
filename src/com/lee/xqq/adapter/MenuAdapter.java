package com.lee.xqq.adapter;

import java.util.List;

import android.content.Context;

import com.lee.xqq.R;
import com.lee.xqq.base.BaseAdapter;
import com.lee.xqq.base.ViewHolder;
import com.lee.xqq.item.MenuItem;

public class MenuAdapter extends BaseAdapter<MenuItem> {

	public MenuAdapter(Context context, List<MenuItem> list, int layoutID) {
		super(context, list, layoutID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initView(ViewHolder holder) {
		// TODO Auto-generated method stub
		holder.addView(R.id.menu_ico);
		holder.addView(R.id.menu_title);
	}

	@Override
	public void setViewValue(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		MenuItem item = getItem(position);
		holder.getTextView(R.id.menu_title).setText(item.getTitle());
		holder.getImageView(R.id.menu_ico).setImageResource(item.getIconID());
	}

}
