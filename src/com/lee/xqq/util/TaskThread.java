package com.lee.xqq.util;

import android.app.Activity;

public abstract class TaskThread implements Runnable {

	private boolean cancel;
	private Activity activity;
	private CachedThreadPool cachedThreadPool;

	public TaskThread() {
		super();
	}

	public TaskThread(Activity activity) {
		super();
		setActivity(activity);
	}

	public Activity getActivity() {
		return activity;
	}

	/**
	 * 设置activity，activity被finish线程自动结束 如果activity为null，后台执行完任务才结束线程
	 * 
	 * @param activity
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public CachedThreadPool getCachedThreadPool() {
		return cachedThreadPool;
	}

	public void setCachedThreadPool(CachedThreadPool cachedThreadPool) {
		this.cachedThreadPool = cachedThreadPool;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			process();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isCancel() {
		return cancel;
	}

	/**
	 * 取消线程
	 */
	public void cancel() {
		this.cancel = true;
	}

	/**
	 * 开始执行任务
	 */
	public void start() {
		if (cachedThreadPool == null)
			CachedThreadPool.execute(this);
	}

	/**
	 * 当前线程是否离开当前activity或者被用户取消
	 * 
	 * @return
	 */
	public boolean isThreadFinish() {
		if (isCancel() || (activity != null && activity.isFinishing()))
			return true;
		return false;
	}

	public void slee(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public abstract void process() throws Exception;

}
