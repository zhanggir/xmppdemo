package com.lee.xqq.item;

import com.lee.xqq.base.BaseItem;

public class XUserItem extends BaseItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7265336570653062594L;
	String Username;
	String Name;
	String Email;

	public XUserItem(String username, String name, String email) {
		super();
		Username = username;
		Name = name;
		Email = email;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

}
