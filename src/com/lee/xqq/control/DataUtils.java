package com.lee.xqq.control;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.lee.xqq.ChatActivity;
import com.lee.xqq.MainActivity;
import com.lee.xqq.SplashActivity;
import com.lee.xqq.base.BroadSender;
import com.lee.xqq.base.MLog;
import com.lee.xqq.item.XUserItem;
import com.lee.xqq.net.HttpResult;
import com.lee.xqq.net.HttpUtil;
import com.lee.xqq.util.CachedThreadPool;

/**
 * 所有的数据控制，逻辑可以在这里实现
 * 
 */
public class DataUtils implements ConnectionListener,
		ConnectionCreationListener, PacketListener {

	private static DataUtils instance;
	private Connection connection;
	private static String host;
	private static int port;
	private String user;

	private DataUtils() {
	}

	public void setHost(String host, int port) {
		DataUtils.host = host;
		DataUtils.port = port;
	}

	public static DataUtils getInstance() {
		if (instance == null)
			instance = new DataUtils();
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}

	public String getUser() {
		return user;
	}

	public void requestBaidu() {
		CachedThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtil httpUtil = HttpUtil.requestText(
						"http://www.baidu.com", "GET");
				HttpResult result = httpUtil.getResponse();
				if (result.getCode() == 200) {
					// 向界面发送接收广播
				}
			}
		});
	}

	public void initXMPP() {
		CachedThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				connectXmppServer();
			}
		});
	}

	private void connectXmppServer() {
		ConnectionConfiguration config = new ConnectionConfiguration(host, port);
		/** 是否启用安全验证 */
		config.setSASLAuthenticationEnabled(false);
		config.setReconnectionAllowed(true);// 断线重连
		/** 是否启用调试 */
		// config.setDebuggerEnabled(true);
		/** 创建connection链接 */
		try {
			Log.e("TAG", "-------------");
			Connection.addConnectionCreationListener(this);
			configure(ProviderManager.getInstance());
			connection = new XMPPConnection(config);
			/** 建立连接 */
			connection.connect();
			connection.addConnectionListener(this);
			PacketTypeFilter filter = new PacketTypeFilter(Packet.class);
			connection.addPacketListener(this, filter);
		} catch (XMPPException e) {
			e.printStackTrace();
			MLog.makeText("服务器连接失败");
		}
	}

	/**
	 * Android读不到/META-INF下的配置文件，需要手工配置。
	 * 
	 * @param pm
	 */
	private void configure(ProviderManager pm) {
		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());
		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());
		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());
		// Chat State
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());
		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());
		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());
		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());
		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());
		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());
		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());
		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());
		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
			e.printStackTrace();
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());
		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());
		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());
		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());
		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());
		// Privacy
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
		pm.addIQProvider("command", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.SessionExpiredError());
	}

	public boolean register(String account, String password) {
		Registration reg = new Registration();
		reg.setType(IQ.Type.SET);
		reg.setTo(connection.getServiceName());
		reg.setUsername(account);// 注意这里createAccount注册时，参数是username，不是jid，是“@”前面的部分。
		reg.setPassword(password);
		reg.addAttribute("android", "geolo_createUser_android");// 这边addAttribute不能为空，否则出错。所以做个标志是android手机创建的吧！！！！！
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				reg.getPacketID()), new PacketTypeFilter(IQ.class));
		PacketCollector collector = connection.createPacketCollector(filter);
		connection.sendPacket(reg);
		IQ result = (IQ) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		if (result == null) {
			MLog.makeText("连接超时");
		} else if (result.getType() == IQ.Type.RESULT) {
			MLog.makeText("注册成功");
			return true;
		} else {
			MLog.makeText(result.getError().toString());
		}
		return false;
	}

	public boolean login(String userName, String userPwd) {
		try {
			connection.login(userName, userPwd);
			user = userName + "@" + connection.getServiceName();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		MLog.makeText("登录失败");
		return false;
	}

	public void changePassword(String pwd) {
		try {
			connection.getAccountManager().changePassword(pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更改用户状态
	 */
	public void setPresence(int code) {
		Presence presence = null;
		switch (code) {
		case 0:
			presence = new Presence(Presence.Type.available);
			connection.sendPacket(presence);
			MLog.makeText("设置在线");
			break;
		case 1:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.chat);
			connection.sendPacket(presence);
			MLog.makeText("设置Q我吧");
			break;
		case 2:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.dnd);
			connection.sendPacket(presence);
			MLog.makeText("设置忙碌");
			break;
		case 3:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.away);
			connection.sendPacket(presence);
			MLog.makeText("设置离开");
			break;
		case 4:
			Roster roster = connection.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				presence = new Presence(Presence.Type.unavailable);
				presence.setPacketID(Packet.ID_NOT_AVAILABLE);
				presence.setFrom(connection.getUser());
				presence.setTo(entry.getUser());
				connection.sendPacket(presence);
			}
			// 向同一用户的其他客户端发送隐身状态
			presence = new Presence(Presence.Type.unavailable);
			presence.setPacketID(Packet.ID_NOT_AVAILABLE);
			presence.setFrom(connection.getUser());
			presence.setTo(StringUtils.parseBareAddress(connection.getUser()));
			connection.sendPacket(presence);
			MLog.makeText("设置隐身");
			break;
		case 5:
			presence = new Presence(Presence.Type.unavailable);
			connection.sendPacket(presence);
			MLog.makeText("设置离线");
			break;
		default:
			break;
		}
	}

	public void logoutAccount() {
		try {
			connection.getAccountManager().deleteAccount();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Collection<RosterGroup> getGroups() {
		return connection.getRoster().getGroups();
	}

	public List<RosterEntry> getEntriesByGroup(String groupName) {
		List<RosterEntry> Entrieslist = new ArrayList<RosterEntry>();
		RosterGroup rosterGroup = connection.getRoster().getGroup(groupName);
		Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();
		Iterator<RosterEntry> i = rosterEntry.iterator();
		while (i.hasNext()) {
			Entrieslist.add(i.next());
		}
		return Entrieslist;
	}

	public Drawable getUserImage(String user) {
		ByteArrayInputStream bais = null;
		try {
			VCard vcard = new VCard();
			// 加入这句代码，解决No VCard for
			ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",
					new org.jivesoftware.smackx.provider.VCardProvider());
			if (user == "" || user == null || user.trim().length() <= 0) {
				return null;
			}
			vcard.load(connection, user + "@" + connection.getServiceName());
			if (vcard == null || vcard.getAvatar() == null)
				return null;
			bais = new ByteArrayInputStream(vcard.getAvatar());
			BitmapDrawable drawable = new BitmapDrawable(bais);
			bais.close();
			return drawable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<RosterEntry> getAllEntries() {
		List<RosterEntry> Entrieslist = new ArrayList<RosterEntry>();
		Collection<RosterEntry> rosterEntry = connection.getRoster()
				.getEntries();
		Iterator<RosterEntry> i = rosterEntry.iterator();
		while (i.hasNext()) {
			Entrieslist.add(i.next());
		}
		return Entrieslist;
	}

	public boolean addGroup(String groupName) {
		try {
			connection.getRoster().createGroup(groupName);
			MLog.makeText("创建成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 添加好友 无分组
	 * 
	 * @param userName
	 * @param name
	 * @return
	 */
	public boolean addUser(String userName, String name) {
		try {
			String account = userName + "@" + connection.getServiceName();
			connection.getRoster().createEntry(account, name, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean addUserWithName(String userName, String name,
			Presence.Type type) {
		return addUserWithName(userName, name, XMsgConst.X_DEFAULT_GROUP, type);
	}

	/**
	 * 添加好友 有分组
	 * 
	 * @param userName
	 * @param name
	 * @param groupName
	 * @return
	 */
	public boolean addUserWithName(String userName, String name,
			String groupName, Presence.Type type) {
		try {
			Presence subscription = new Presence(type);
			subscription.setTo(userName);
			userName += "@" + connection.getServiceName();
			connection.sendPacket(subscription);
			if (type == Type.subscribed)
				connection.getRoster().createEntry(userName, name,
						new String[] { groupName });
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean removeUser(String userName) {
		try {
			RosterEntry entry = null;
			if (userName.contains("@"))
				entry = connection.getRoster().getEntry(userName);
			else
				entry = connection.getRoster().getEntry(
						userName + "@" + connection.getServiceName());
			if (entry == null)
				entry = connection.getRoster().getEntry(userName);
			connection.getRoster().removeEntry(entry);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<XUserItem> searchUsers(String userName) {
		List<XUserItem> results = new ArrayList<XUserItem>();
		try {
			UserSearchManager usm = new UserSearchManager(connection);
			String serverDomain = "search." + connection.getServiceName();
			Form searchForm = usm.getSearchForm(serverDomain);
			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("Username", true);
			answerForm.setAnswer("search", userName);
			ReportedData data = usm.getSearchResults(answerForm, serverDomain);
			Iterator<Row> it = data.getRows();
			Row row = null;
			XUserItem user = null;
			while (it.hasNext()) {
				row = it.next();
				String uName = row.getValues("Username").next().toString();
				String Name = row.getValues("Name").next().toString();
				String Email = row.getValues("Email").next().toString();
				user = new XUserItem(uName, Name, Email);
				results.add(user);
			}
			return results;
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}

	public void sendMessage(String toJID, String message) {
		// TODO Auto-generated method stub
		final Message newMessage = new Message(toJID, Message.Type.chat);
		newMessage.setBody(message);
		connection.sendPacket(newMessage);
	}

	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub
		MLog.makeText("连接已关闭");
	}

	@Override
	public void connectionClosedOnError(Exception arg0) {
		// TODO Auto-generated method stub
		MLog.makeText("连接关闭异常");
		arg0.printStackTrace();
	}

	@Override
	public void reconnectingIn(int arg0) {
		// TODO Auto-generated method stub
		MLog.makeText("reconnectingIn:" + arg0);
	}

	@Override
	public void reconnectionFailed(Exception arg0) {
		// TODO Auto-generated method stub
		MLog.makeText("连接失败");
		arg0.printStackTrace();
	}

	@Override
	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub
		MLog.makeText("重连成功");
	}

	@Override
	public void connectionCreated(Connection arg0) {
		// TODO Auto-generated method stub
		BroadSender.sendBroadcast(SplashActivity.class);
	}

	@Override
	public void processPacket(Packet packet) {
		// TODO Auto-generated method stub
		if (packet instanceof Message) {
			Message msg = (Message) packet;
			CacheUtils.getInstance().addMessage(msg);
			BroadSender.sendBroadcast(ChatActivity.class, "action",
					XMsgConst.X_PACKET_TYPE_MESSAGE);
		} else if (packet instanceof Presence) {
			Presence presence = (Presence) packet;
			String from = presence.getFrom();// 发送方
			String to = presence.getTo();// 接收方
			Type type = presence.getType();
			int typeValue = 0;
			if (type.equals(Presence.Type.subscribe)) {
				// 好友申请
				typeValue = XMsgConst.X_PRESENCE_SUBSCRIBE;
			} else if (type.equals(Presence.Type.subscribed)) {
				// 同意添加好友
				typeValue = XMsgConst.X_PRESENCE_SUBSCRIBED;
			} else if (type.equals(Presence.Type.unsubscribe)) {
				// 拒绝添加好友
				typeValue = XMsgConst.X_PRESENCE_UNSUBSCRIBE;
			} else if (type.equals(Presence.Type.unsubscribed)) {
				// 删除好友
				typeValue = XMsgConst.X_PRESENCE_UNSUBSCRIBED;
			} else if (type.equals(Presence.Type.unavailable)) {
				// 好友下线
				typeValue = XMsgConst.X_PRESENCE_UNAVAILABLE;
				from = processFrom(from);
				CacheUtils.getInstance().putPresence(from, presence);
			} else {
				// 好友上线
				typeValue = XMsgConst.X_PRESENCE_ONLINE;
				from = processFrom(from);
				CacheUtils.getInstance().putPresence(from, presence);
			}
			String[] params = new String[] { "action",
					XMsgConst.X_PACKET_TYPE_PRESENCE, "type",
					String.valueOf(typeValue), "from", from, "to", to };
			BroadSender.sendBroadcast(MainActivity.class, params);
		}
	}

	/**
	 * 去掉多余的服务器字符串
	 * 
	 * @param from
	 * @return
	 */
	private String processFrom(String from) {
		int endPos = from.lastIndexOf("/");
		if (endPos != -1)
			from = from.substring(0, endPos);
		return from;
	}

}
