package com.lee.xqq.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.lee.xqq.R;
import com.lee.xqq.base.BaseAdapter;
import com.lee.xqq.base.ViewHolder;
import com.lee.xqq.item.ChatItem;

public class ChatAdapter extends BaseAdapter<ChatItem> {

	public ChatAdapter(Context context, List<ChatItem> list, int layoutID) {
		super(context, list, layoutID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initView(ViewHolder holder) {
		// TODO Auto-generated method stub
		holder.addView(R.id.left_text);
		holder.addView(R.id.right_text);
		holder.addView(R.id.left_chat_layout);
		holder.addView(R.id.right_chat_layout);
	}

	@Override
	public void setViewValue(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		if (getItem(position).isSelf()) {
			holder.getView(R.id.left_chat_layout).setVisibility(View.GONE);
			holder.getView(R.id.right_chat_layout).setVisibility(View.VISIBLE);
			holder.getTextView(R.id.right_text).setText(
					getItem(position).getMsg());
		} else {
			holder.getView(R.id.left_chat_layout).setVisibility(View.VISIBLE);
			holder.getView(R.id.right_chat_layout).setVisibility(View.GONE);
			holder.getTextView(R.id.left_text).setText(
					getItem(position).getMsg());
		}
	}
}
