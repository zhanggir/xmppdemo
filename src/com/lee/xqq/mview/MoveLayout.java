package com.lee.xqq.mview;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class MoveLayout extends LinearLayout {

	private final static int SPEED = 15;
	private int MAX_VALUE = 0;

	public MoveLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		MAX_VALUE = b - t;
	}

	public void doMove() {
		LayoutParams lp = (LayoutParams) getLayoutParams();
		if (lp.topMargin >= 0)
			new AsynMove().execute(new Integer[] { -SPEED });
		else if (lp.topMargin <= -lp.height)
			new AsynMove().execute(new Integer[] { SPEED });
	}

	private class AsynMove extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			int times = 0;
			if (MAX_VALUE % Math.abs(params[0]) == 0)
				times = MAX_VALUE / Math.abs(params[0]);
			else
				times = MAX_VALUE / Math.abs(params[0]) + 1;

			for (int i = 0; i < times; i++) {
				publishProgress(params);
				sleep(Math.abs(params[0]));
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			LayoutParams lp = (LayoutParams) getLayoutParams();
			if (values[0] < 0)
				lp.topMargin = Math.max(lp.topMargin + values[0], -MAX_VALUE);
			else
				lp.topMargin = Math.min(lp.topMargin + values[0], MAX_VALUE);
			setLayoutParams(lp);
		}

		private void sleep(long time) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
