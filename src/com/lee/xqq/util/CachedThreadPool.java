package com.lee.xqq.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程 线程池优点
 * 
 */
public class CachedThreadPool {

	private static ExecutorService service;
	private static CachedThreadPool pool;

	private CachedThreadPool() {
		super();
		// TODO Auto-generated constructor stub
	}

	private static void init() {
		if (service == null)
			service = Executors.newCachedThreadPool();
		if (pool == null)
			pool = new CachedThreadPool();
	}

	public static void execute(Runnable runnable) {
		init();
		if (runnable instanceof TaskThread) {
			TaskThread thread = (TaskThread) runnable;
			thread.setCachedThreadPool(pool);
		}
		service.execute(runnable);
	}

	public static ExecutorService getService() {
		init();
		return service;
	}
}
