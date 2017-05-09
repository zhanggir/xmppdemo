package com.lee.xqq.fragment;

import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.lee.xqq.ChatActivity;
import com.lee.xqq.R;
import com.lee.xqq.adapter.FriendAdapter;
import com.lee.xqq.adapter.SearchAdapter;
import com.lee.xqq.base.BaseFragment;
import com.lee.xqq.base.MLog;
import com.lee.xqq.control.CacheUtils;
import com.lee.xqq.control.XMsgConst;
import com.lee.xqq.item.XUserItem;

public class HomeFragment extends BaseFragment implements OnChildClickListener {

	private static HomeFragment fragment;
	private ExpandableListView friendView;
	private FriendAdapter friendAdapter;
	private Collection<RosterGroup> groups;

	private EditText keyword;

	public static HomeFragment getFragment() {
		if (fragment == null)
			fragment = new HomeFragment();
		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_home);
		keyword = (EditText) findViewById(R.id.keyword);
		friendView = (ExpandableListView) findViewById(R.id.friends);
		friendView.setOnChildClickListener(this);
		setTitle(dataUtils.getUser());
		if (groups == null)
			groups = dataUtils.getGroups();
		friendAdapter = new FriendAdapter(getActivity(), groups,
				R.layout.group_layout, R.layout.child_layout);
		friendView.setAdapter(friendAdapter);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		friendAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view.getId() == R.id.search) {
			String userName = keyword.getText().toString();
			List<XUserItem> list = dataUtils.searchUsers(userName);
			showSearchResult(list);
		} else if (view.getId() == R.id.add) {
			Object obj = view.getTag();
			if (obj instanceof XUserItem) {
				XUserItem item = (XUserItem) obj;
				if (dataUtils.addUserWithName(item.getUsername(),
						item.getName(), XMsgConst.X_DEFAULT_GROUP,
						Type.subscribed))
					MLog.makeText(item.getUsername() + "添加成功");
			}
		}
	}

	private void showSearchResult(List<XUserItem> xUserItems) {
		final Dialog dialog = getBaseActivity().showDialog(
				R.layout.search_layout, true, Gravity.BOTTOM, null);
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				dm.heightPixels >> 1);
		ListView listView = (ListView) dialog.findViewById(R.id.list_search);
		SearchAdapter searchAdapter = new SearchAdapter(getBaseActivity(),
				xUserItems, R.layout.search_item_layout);
		listView.setAdapter(searchAdapter);
	}

	@Override
	public void receiver(Intent intent) {
		// TODO Auto-generated method stub
		super.receiver(intent);
		if (XMsgConst.checkType(intent, XMsgConst.X_PACKET_TYPE_PRESENCE)) {
			int pType = Integer.parseInt(intent.getStringExtra("type"));
			String from = intent.getStringExtra("from");
			switch (pType) {
			case XMsgConst.X_PRESENCE_SUBSCRIBE:
				showSubscribeDialog(XMsgConst.getUserName(from));
				break;
			case XMsgConst.X_PRESENCE_UNSUBSCRIBE:
				showSubscribeDialog(XMsgConst.getUserName(from));
				break;
			default:
				groups = dataUtils.getGroups();
				handler.sendEmptyMessage(0);
				break;
			}
		}
	}

	private void showSubscribeDialog(final String from) {
		final Dialog dialog = getBaseActivity().showDialog(
				R.layout.dialog_newsub_layout, true);
		TextView textView = (TextView) dialog.findViewById(R.id.text);
		StringBuffer buffer = new StringBuffer();
		buffer.append("提示信息\n\n");
		buffer.append("收到用户:");
		buffer.append(from);
		buffer.append("的好友申请");
		textView.setText(buffer);
		final Button ok = (Button) dialog.findViewById(R.id.ok);
		final Button cancel = (Button) dialog.findViewById(R.id.cancel);
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dataUtils.addUserWithName(from, from, v == ok ? Type.subscribed
						: Type.unsubscribe);
				dialog.dismiss();
			}
		};
		ok.setOnClickListener(listener);
		cancel.setOnClickListener(listener);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		friendAdapter.getGroups().clear();
		friendAdapter.getGroups().addAll(groups);
		friendAdapter.notifyDataSetChanged();
		return super.handleMessage(msg);
	}

	public static Presence getPresence(String from) {
		return CacheUtils.getInstance().getPresence(from);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		String user = friendAdapter.getChild(groupPosition, childPosition)
				.getUser();
		getBaseActivity().startActivity(ChatActivity.class, "user", user);
		return false;
	}

}