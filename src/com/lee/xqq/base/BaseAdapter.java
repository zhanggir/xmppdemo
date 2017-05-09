package com.lee.xqq.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 实现这个适配器模板会少很多代码
 * @author leehom
 * @param <T>
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

	protected List<T> list;
	protected Context context;
	protected LayoutInflater inflater;
	private int layoutID;

	public BaseAdapter(Context context, T[] ts, int layoutID) {
		super();
		this.context = context;
		this.list = new ArrayList<T>();
		this.layoutID = layoutID;
		Collections.addAll(list, ts);
		this.inflater = LayoutInflater.from(context);
	}

	public BaseAdapter(Context context, List<T> list, int layoutID) {
		super();
		this.layoutID = layoutID;
		this.context = context;
		this.list = new ArrayList<T>();
		this.list.addAll(list);
		this.inflater = LayoutInflater.from(context);
	}

	public Resources getResources() {
		return context.getResources();
	}

	public synchronized void add(T object) {
		list.add(object);
	}

	public synchronized void addAll(Collection<? extends T> collection) {
		list.addAll(collection);
	}

	public synchronized void remove(T object) {
		list.remove(object);
	}

	public synchronized void insert(T object, int index) {
		list.add(index, object);
	}

	public synchronized void clear() {
		list.clear();
	}

	public synchronized void sort(Comparator<? super T> comparator) {
		Collections.sort(list, comparator);
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(layoutID, null);
			holder = new ViewHolder(convertView);
			initView(holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setViewValue(holder, position);
		return convertView;
	}

	public List<T> getList() {
		return list;
	}
	
	/**
	 * 向ViewHolder类里面添加View
	 * @param holder
	 */
	public abstract void initView(ViewHolder holder);

	/**
	 * 从ViewHolder获取对应ID的View设置其值
	 * @param holder
	 * @param t 数据对象
	 */
	public abstract void setViewValue(ViewHolder holder, int position);

}
