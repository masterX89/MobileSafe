package com.hrbeu.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/**
 * 获取焦点的TextView
 * 
 * @author Hankai Xia
 * 
 */
public class FocusedTextView extends TextView {
	/**
	 * 有style样式时使用此方法
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 有属性时使用此方法
	 * 
	 * @param context
	 * @param attrs
	 */
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 用代码new对象时使用此方法
	 * 
	 * @param context
	 */
	public FocusedTextView(Context context) {
		super(context);
	}

	/**
	 * 表示有没有获取焦点，跑马灯要运行，首先调用此函数判断是否有焦点，是true时，跑马灯才会运行
	 */
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return true;
	}
}
