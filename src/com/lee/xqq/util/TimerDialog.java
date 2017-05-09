package com.lee.xqq.util;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;

public class TimerDialog {

	private Timer timer;
	public static final int DEFAULT_TIME_OUT = 15000;
	private Dialog dialog;
	private DialogTimeOutListner timeOutListner;

	public TimerDialog(Dialog dialog) {
		this(dialog, DEFAULT_TIME_OUT);
	}

	/**
	 * 
	 * @param dialog
	 * @param time
	 *            milliseconds
	 */
	public TimerDialog(Dialog dialog, int time) {
		super();
		this.dialog = dialog;
		timer = new Timer();
		timer.schedule(task, time);
	}

	public void setTimeOutListner(DialogTimeOutListner timeOutListner) {
		this.timeOutListner = timeOutListner;
	}

	public void dismiss() {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
		dialog = null;
		if (task != null)
			task.cancel();
		task = null;
		if (timer != null)
			timer.cancel();
		timer = null;
	}

	public Dialog getDialog() {
		return dialog;
	}

	private TimerTask task = new TimerTask() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();
			if (timeOutListner != null)
				timeOutListner.dialogTimeOut(dialog);
			timer.cancel();
		}
	};

	public interface DialogTimeOutListner {
		public void dialogTimeOut(Dialog dialog);
	}

}
