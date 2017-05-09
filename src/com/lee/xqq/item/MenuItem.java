package com.lee.xqq.item;

import com.lee.xqq.base.BaseItem;

public class MenuItem extends BaseItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8579290551013322265L;
	int id;
	String title;
	int iconID;

	public MenuItem(String title) {
		super();
		this.title = title;
	}

	public MenuItem(int id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public MenuItem(String title, int iconID) {
		super();
		this.title = title;
		this.iconID = iconID;
	}

	public MenuItem(int id, String title, int iconID) {
		super();
		this.id = id;
		this.title = title;
		this.iconID = iconID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIconID() {
		return iconID;
	}

	public void setIconID(int iconID) {
		this.iconID = iconID;
	}

}
