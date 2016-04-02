package view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FocusedTextView extends TextView {
	/**
	 * ��style��ʽ��ʱ���ߴ˷���
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * ������ʱ�ߴ˷���
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
