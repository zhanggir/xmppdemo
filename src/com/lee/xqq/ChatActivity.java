package com.lee.xqq;

import java.util.List;

import org.jivesoftware.smack.packet.Message;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lee.xqq.adapter.ChatAdapter;
import com.lee.xqq.base.BaseActivity;
import com.lee.xqq.control.XMsgConst;
import com.lee.xqq.item.ChatItem;

public class ChatActivity extends BaseActivity implements
		OnItemLongClickListener {

	protected ChatAdapter chatAdapter;
	protected ListView chatView;
	protected String user;
	private EditText editText;
	private List<ChatItem> historyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setContentView(R.layout.activity_chat);
		editText = (EditText) findViewById(R.id.input);
		Button button = (Button) findViewById(R.id.option);
		button.setBackgroundResource(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		TextView title = (TextView) findViewById(R.id.title_text);
		user = getIntent().getStringExtra("user");
		title.setText(user);
		chatView = (ListView) findViewById(R.id.list_chat);
		historyList = cacheUtils.getChatHistory(user);
		chatAdapter = new ChatAdapter(this, historyList,
				R.layout.chat_item_layout);
		chatView.setAdapter(chatAdapter);
		getCacheMsgs();
		chatView.setSelection(chatAdapter.getCount() - 1);
		chatView.setOnItemLongClickListener(this);
	}

	private void getCacheMsgs() {
		List<Message> messages = cacheUtils.popAllMessage();
		for (Message message : messages) {
			chatAdapter.clear();
			historyList.add(new ChatItem(message, false));
			chatAdapter.addAll(historyList);
			chatAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.option:
			finish();
			break;
		case R.id.send:
			String msg = editText.getText().toString();
			dataUtils.sendMessage(user, msg);
			addMsgToAdapter(user, msg);
			editText.setText("");
			break;
		}
	}

	private void addMsgToAdapter(String to, String msg) {
		ChatItem item = new ChatItem(dataUtils.getUser(), user, msg, true);
		historyList.add(item);
		chatAdapter.add(item);
		chatAdapter.notifyDataSetChanged();
	}

	@Override
	public void receiver(Intent intent) {
		// TODO Auto-generated method stub
		super.receiver(intent);
		if (XMsgConst.checkType(intent, XMsgConst.X_PACKET_TYPE_MESSAGE)) {
			getCacheMsgs();
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		ChatItem item = chatAdapter.getItem(position);
		copyOptionDialog(item);
		return false;
	}

	private void copyOptionDialog(final ChatItem item) {
		final Dialog dialog = showDialog(R.layout.dialog_chat_option_layout,
				true);
		final Button copy = (Button) dialog.findViewById(R.id.copy_btn);
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (v == copy) {
					ClipboardManager copy = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					copy.setText(item.getMsg());
				}
			}

		};
		copy.setOnClickListener(listener);
	}

}
