package view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FocusedTextView extends TextView {
	/**
	 * 有style样式的时候走此方法
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 有属性时走此方法
	 * 
	 * @param context
	 * @param attrs
	 */
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FocusedTextView(Context context) {
		super(context);
	}

}
