package com.lee.xqq.control;

import android.content.Intent;

public class XMsgConst {

	private static final int X_PRESENCE = 1000;
	/** 好友申请 **/
	public static final int X_PRESENCE_SUBSCRIBE = X_PRESENCE + 1;
	/** 同意添加好友 **/
	public static final int X_PRESENCE_SUBSCRIBED = X_PRESENCE + 2;
	/** 拒绝添加好友 **/
	public static final int X_PRESENCE_UNSUBSCRIBE = X_PRESENCE + 3;
	/** 删除好友 **/
	public static final int X_PRESENCE_UNSUBSCRIBED = X_PRESENCE + 4;
	/** 好友下线 **/
	public static final int X_PRESENCE_UNAVAILABLE = X_PRESENCE + 5;
	/** 好友上线 **/
	public static final int X_PRESENCE_ONLINE = X_PRESENCE + 6;

	public static final String X_ACTION = "action";
	public static final String X_PACKET_TYPE_MESSAGE = "message";
	public static final String X_PACKET_TYPE_PRESENCE = "presence";
	public static final String X_DEFAULT_GROUP = "Friends";

	public static final boolean checkType(Intent intent, String value) {
		String type = intent.getStringExtra(X_ACTION);
		return checkType(type, value);
	}

	public static final boolean checkType(String type, String value) {
		if (type == null || value == null)
			return false;
		return type.equals(value);
	}

	public static final String getUserName(String account) {
		if (account.indexOf("@") == -1)
			return account;
		return account.substring(0, account.indexOf("@"));
	}

}
