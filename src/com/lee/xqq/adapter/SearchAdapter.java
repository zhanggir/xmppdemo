package com.lee.xqq.adapter;

import java.util.List;

import android.content.Context;

import com.lee.xqq.R;
import com.lee.xqq.base.BaseAdapter;
import com.lee.xqq.base.ViewHolder;
import com.lee.xqq.item.XUserItem;

public class SearchAdapter extends BaseAdapter<XUserItem> {

	public SearchAdapter(Context context, List<XUserItem> list, int layoutID) {
		super(context, list, layoutID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initView(ViewHolder holder) {
		// TODO Auto-generated method stub
		holder.addView(R.id.name_text);
		holder.addView(R.id.add);
	}

	@Override
	public void setViewValue(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		XUserItem item = getItem(position);
		holder.getTextView(R.id.name_text).setText(item.getUsername());
		holder.getButton(R.id.add).setTag(item);
	}

}
