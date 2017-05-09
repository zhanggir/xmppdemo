package com.lee.xqq.base;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {

	SparseArray<View> array;
	View parent;

	public ViewHolder(View parent) {
		this.parent = parent;
		array = new SparseArray<View>();
	}

	public void addView(int id) {
		array.append(id, parent.findViewById(id));
	}

	public View getView(int id) {
		return array.get(id);
	}

	public Button getButton(int id) {
		return (Button) getView(id);
	}

	public TextView getTextView(int id) {
		return (TextView) getView(id);
	}

	public ImageView getImageView(int id) {
		return (ImageView) getView(id);
	}

	public ImageButton getImageButton(int id) {
		return (ImageButton) getView(id);
	}

	public View getParent() {
		return parent;
	}

	public void setParent(View parent) {
		this.parent = parent;
	}

}
