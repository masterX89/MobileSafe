package com.hrbeu.mobilesafe.view;

import com.hrbeu.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 设置中心的自定义組合控件
 * 
 * @author Hankai Xia
 * 
 */
public class SettingClickView extends RelativeLayout {

	private TextView tvTitle;
	private TextView tvDisc;

	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView();

	}

	public SettingClickView(Context context) {
		super(context);
		initView();
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		// 将定义好的布局文件设置给当前的SettingClickView
		View.inflate(getContext(), R.layout.view_setting_click, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDisc = (TextView) findViewById(R.id.tv_disc);
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setDesc(String desc) {
		tvDisc.setText(desc);
	}

}
