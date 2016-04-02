package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/**
 * ��ȡ�����TextView
 * 
 * @author Hankai Xia
 * 
 */
public class FocusedTextView extends TextView {
	/**
	 * ��style��ʽ��ʱ���ô˷���
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * ������ʱ�ô˷���
	 * 
	 * @param context
	 * @param attrs
	 */
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * �ô���new����ʱ���ô˷���
	 * 
	 * @param context
	 */
	public FocusedTextView(Context context) {
		super(context);
	}

	/**
	 * ��ʾ��û�л�ȡ���� �����Ҫ���У����ȵ��õ��ô˺����ж��Ƿ��н��㣬��true����Ч��
	 */
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return true;
	}
}
