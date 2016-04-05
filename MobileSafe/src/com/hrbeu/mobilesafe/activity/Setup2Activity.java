package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import com.hrbeu.mobilesafe.R;

/**
 * 第二个设置向导页
 * 
 * @author Hankai Xia
 * 
 */
public class Setup2Activity extends Activity {

	private GestureDetector mDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		// 手势识别器
		mDetector = new GestureDetector(this, new SimpleOnGestureListener() {

			/**
			 * 监听手势滑动事件 e1表示滑动的起点，e2表示滑动终点 velocityX表示水平速度，velocityY表示垂直速度
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// 向右滑动，上一页
				if (e2.getRawX() - e1.getRawX() > 200) {
					showPreviousPage();
					return true;
				}
				// 向左滑动，下一页
				if (e1.getRawX() - e2.getRawX() > 200) {
					showNextPage();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}

	/**
	 * 展示上一页
	 */
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
	}

	/**
	 * 展示下一页
	 */
	public void showNextPage() {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	/**
	 * 点击按钮到下一页
	 * 
	 * @param view
	 */
	public void next(View view) {
		showNextPage();
	}

	/**
	 * 点击按钮到上一页
	 * 
	 * @param view
	 */
	public void previous(View view) {
		showPreviousPage();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 委托手势识别器处理触摸事件
		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
