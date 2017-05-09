package com.lee.xqq.item;

import org.jivesoftware.smack.packet.Message;

import com.lee.xqq.base.BaseItem;

public class ChatItem extends BaseItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6244938910868248462L;
	String from;
	String to;
	String msg;
	boolean self;

	public ChatItem(String from, String to, String msg, boolean self) {
		super();
		this.from = from;
		this.to = to;
		this.msg = msg;
		this.self = self;
	}

	public ChatItem(Message message, boolean self) {
		this(message.getFrom(), message.getTo(), message.getBody(), self);
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSelf() {
		return self;
	}

	public void setSelf(boolean self) {
		this.self = self;
	}

}
