package com.lee.xqq.control;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import com.lee.xqq.item.ChatItem;

/**
 * 缓存、后台数据全部可以放这里
 * 
 */
public class CacheUtils {

	private static CacheUtils instance;
	private Map<String, List<ChatItem>> chatHistoryMap;
	private Deque<Message> receives;
	private Map<String, Presence> presenceMap;

	private CacheUtils() {
		super();
		// TODO Auto-generated constructor stub
		chatHistoryMap = new HashMap<String, List<ChatItem>>(1000);
		receives = new LinkedBlockingDeque<Message>(1000);
		presenceMap = new HashMap<String, Presence>(1000);
	}

	public static CacheUtils getInstance() {
		if (instance == null)
			instance = new CacheUtils();
		return instance;
	}

	public void putPresence(String key, Presence presence) {
		presenceMap.put(key, presence);
	}

	public Presence getPresence(String key) {
		return presenceMap.get(key);
	}

	public void putChatHistory(String user) {
		if (chatHistoryMap.get(user) == null)
			chatHistoryMap.put(user, new ArrayList<ChatItem>(100));
	}

	public List<ChatItem> getChatHistory(String user) {
		if (chatHistoryMap.containsKey(user))
			return chatHistoryMap.get(user);
		else
			putChatHistory(user);// 放一个新的列表
		return chatHistoryMap.get(user);
	}

	public void addMessage(Message message) {
		synchronized (receives) {
			receives.add(message);
		}
	}

	public List<Message> popAllMessage() {
		List<Message> msgs = new ArrayList<Message>();
		synchronized (receives) {
			while (receives.peek() != null)
				msgs.add(receives.pop());
			return msgs;
		}
	}
}
