package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * 设置引导页的基类，不需要在清单文件中注册，因为不需要界面展示
 *
 * @author Hankai Xia
 */
public abstract class BaseSetupActivity extends Activity {

    private GestureDetector mDetector;
    public SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        // 手势识别器
        mDetector = new GestureDetector(this, new SimpleOnGestureListener() {

            /**
             * 监听手势滑动事件 e1表示滑动的起点，e2表示滑动终点 velocityX表示水平速度，velocityY表示垂直速度
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {
                // 判断纵向滑动幅度是否过大
                if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
                    Toast.makeText(BaseSetupActivity.this, "不能这个样子滑动哦~",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (Math.abs(velocityX) < 100) {
                    Toast.makeText(BaseSetupActivity.this, "你滑的太慢啦~",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
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
     * 展示上一页，子类必须实现
     */
    public abstract void showPreviousPage();

    /**
     * 展示下一页，子类必须实现
     */
    public abstract void showNextPage();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 委托手势识别器处理触摸事件
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
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

}
