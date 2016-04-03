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
public class SettingItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.hrbeu.mobilesafe";
	private TextView tvTitle;
	private TextView tvDisc;
	private CheckBox cbStatus;
	private String mTitle;
	private String mDescOn;
	private String mDescOff;

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 根据属性名称获取属性的值
		mTitle = attrs.getAttributeValue(NAMESPACE, "title");
		mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
		mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");

		initView();
		
		// int attributeCount = attrs.getAttributeCount();
		// for (int i = 0; i < attributeCount; i++) {
		// String attributeName = attrs.getAttributeName(i);
		// String attributeValue = attrs.getAttributeValue(i);
		// System.out.println(attributeName + "=" + attributeValue);
		// }
	}

	public SettingItemView(Context context) {
		super(context);
		initView();
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		// 将定义好的布局文件设置给当前的SettingItemView
		View.inflate(getContext(), R.layout.view_setting_item, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDisc = (TextView) findViewById(R.id.tv_disc);
		cbStatus = (CheckBox) findViewById(R.id.cb_status);
		// 设置标题
		setTitle(mTitle);
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setDisc(String disc) {
		tvDisc.setText(disc);
	}

	/**
	 * 返回勾选状态
	 * 
	 * @return 勾选状态
	 */
	public boolean isChecked() {
		return cbStatus.isChecked();
	}

	/**
	 * 设定勾选状态
	 * 
	 * @param check
	 */
	public void setChecked(boolean check) {
		cbStatus.setChecked(check);
		// 根据选择的状态，更新文本描述
		if (check) {
			setDisc(mDescOn);
		} else {
			setDisc(mDescOff);

		}
	}

}
