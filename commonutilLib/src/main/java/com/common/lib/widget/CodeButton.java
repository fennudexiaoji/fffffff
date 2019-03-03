package com.common.lib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.common.lib.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
/**
 * 验证码的时间统计
 */
public class CodeButton extends TextView implements OnClickListener {
	private long lenght = 60 * 1000;// 倒计时长度,这里给了默认60秒
	private String textafter = "重新发送(";
	private String textAfterString=")";
	private String textbefore = "获取验证码";
	private final String TIME = "time";
	private final String CTIME = "ctime";
	private OnClickListener mOnclickListener;
	private Timer t;
	private TimerTask tt;
	private long time;
	public static Map<String, Long> map=new HashMap<>();

	public CodeButton(Context context) {
		super(context);
		setOnClickListener(this);

	}

	public CodeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	@SuppressLint("HandlerLeak")
	Handler han = new Handler() {
		public void handleMessage(android.os.Message msg) {
			CodeButton.this.setText(textafter+time / 1000+textAfterString );
			time -= 1000;
			if (time < 0) {
				CodeButton.this.setEnabled(true);
				CodeButton.this.setText(textbefore);
				CodeButton.this.setTextColor(CodeButton.this.getContext().getResources().getColor(R.color.blue));
				CodeButton.this.setBackground(CodeButton.this.getContext().getResources().getDrawable(R.drawable.bg_code_true));
				clearTimer();
			}else{
				CodeButton.this.setEnabled(false);
				CodeButton.this.setBackground(CodeButton.this.getContext().getResources().getDrawable(R.drawable.bg_code));
				CodeButton.this.setTextColor(CodeButton.this.getContext().getResources().getColor(R.color.line));
			}
		};
	};

	private void initTimer() {
		time = lenght;
		t = new Timer();
		tt = new TimerTask() {
			@Override
			public void run() {
				han.sendEmptyMessage(0x01);
			}
		};
	}

	private void clearTimer() {
		if (tt != null) {
			tt.cancel();
			tt = null;
		}
		if (t != null)
			t.cancel();
		t = null;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		if (l instanceof CodeButton) {
			super.setOnClickListener(l);
		} else
			this.mOnclickListener = l;
	}

	@Override
	public void onClick(View v) {
		if (mOnclickListener != null)
			mOnclickListener.onClick(v);
		initTimer();
		this.setText(textafter+time / 1000+textAfterString);
		this.setEnabled(false);
		t.schedule(tt, 0, 1000);
	}

	/**
	 * 和activity的onDestroy()方法同步
	 */
	public void onDestroy() {
		if (map == null)
		map = new HashMap<>();
		map.put(TIME, time);
		map.put(CTIME, System.currentTimeMillis());
		clearTimer();
		Log.e("yung", "onDestroy");

	}

	/**
	 * 和activity的onCreate()方法同步
	 */
	public void onCreate(Bundle bundle) {
		Log.e("yung", map + "");
		if (map == null)
			return;
		if (map.size() <= 0)// 这里表示没有上次未完成的计时
			return;
		long time = System.currentTimeMillis() - map.get(CTIME)
				- map.get(TIME);
		map.clear();
		if (time > 0)
			return;
		else {
			initTimer();
			this.time = Math.abs(time);
			t.schedule(tt, 0, 1000);
			this.setText(textafter+time+textAfterString);
			this.setEnabled(false);
		}
	}

	/** * 设置计时时候显示的文本 */
	public CodeButton setTextAfter(String text1) {
		this.textafter = text1;
		return this;
	}

	/** * 设置点击之前的文本 */
	public CodeButton setTextBefore(String text0) {
		this.textbefore = text0;
		this.setText(textbefore);
		return this;
	}

	/**
	 * 设置到计时长度
	 *
	 * @param lenght
	 *            时间 默认毫秒
	 * @return
	 */
	public CodeButton setLenght(long lenght) {
		this.lenght = lenght;
		return this;
	}

	public long getButtonTime(){
		return time;
	}

	public void restart(){
		time=0;
	}
}