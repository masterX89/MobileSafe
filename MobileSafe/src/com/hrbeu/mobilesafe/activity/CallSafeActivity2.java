package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrbeu.mobilesafe.R;
import com.hrbeu.mobilesafe.adapter.MyBaseAdapter;
import com.hrbeu.mobilesafe.bean.BlackNumberInfo;
import com.hrbeu.mobilesafe.db.dao.BlackNumberDao;

import java.util.List;

/**
 * @author Hankai Xia
 */
public class CallSafeActivity2 extends Activity {

	private ListView listView;
	private List<BlackNumberInfo> blackNumberInfos;
	private LinearLayout llpb;
	/**
	 * 当前页面
	 */
	private int mCurrentPageNumber = 0;
	/**
	 * 每一页展示20条数据
	 */
	private int mPageSize = 20;
	private TextView tvPageNumber;
	/**
	 * 页面的总个数
	 */
	private int totalPage;
	private EditText etPageNumber;
	private BlackNumberDao dao;
	private CallSafeAdper adaper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_safe);
		initUI();
		initData();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			llpb.setVisibility(View.INVISIBLE);
			tvPageNumber.setText(mCurrentPageNumber + "/" + (totalPage - 1));
			adaper = new CallSafeAdper(blackNumberInfos, CallSafeActivity2.this);
			listView.setAdapter(adaper);
		}
	};

	private void initData() {

		new Thread() {
			@Override
			public void run() {
				dao = new BlackNumberDao(CallSafeActivity2.this);
				totalPage = dao.getTotalNumber() / mPageSize;
				//blackNumberInfos = dao.findAll();
				blackNumberInfos = dao.findPar(mCurrentPageNumber, mPageSize);
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	private void initUI() {
		llpb = (LinearLayout) findViewById(R.id.ll_pb);
		//展示加载的圆圈
		llpb.setVisibility(View.VISIBLE);
		listView = (ListView) findViewById(R.id.list_view);
		tvPageNumber = (TextView) findViewById(R.id.tv_page_number);
		etPageNumber = (EditText) findViewById(R.id.et_page_number);
	}

	private class CallSafeAdper extends MyBaseAdapter<BlackNumberInfo> {
		public CallSafeAdper(List lists, Context mContext) {
			super(lists, mContext);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(CallSafeActivity2.this, R.layout.item_call_safe, null);
				holder = new ViewHolder();
				holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
				holder.tvMode = (TextView) convertView.findViewById(R.id.tv_mode);
				holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvNumber.setText(lists.get(position).getNumber());
			String mode = lists.get(position).getMode();
			if (mode.equals("1")) {
				holder.tvMode.setText("拦截电话+短信");
			} else if (mode.equals("2")) {
				holder.tvMode.setText("电话拦截");
			} else if (mode.equals("2")) {
				holder.tvMode.setText("短信拦截");
			}
			final BlackNumberInfo info = lists.get(position);
			holder.ivDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String number = info.getNumber();
					boolean result = dao.delete(number);
					if (result) {
						Toast.makeText(CallSafeActivity2.this, "删除成功", Toast.LENGTH_SHORT).show();
						lists.remove(info);
						//刷新界面
						adaper.notifyDataSetChanged();
					} else {
						Toast.makeText(CallSafeActivity2.this, "删除失败", Toast.LENGTH_SHORT).show();
					}
				}
			});

			return convertView;
		}
	}

	static class ViewHolder {
		TextView tvNumber;
		TextView tvMode;
		ImageView ivDelete;
	}

	/**
	 * 上一页
	 *
	 * @param view
	 */
	public void prePage(View view) {
		if (mCurrentPageNumber <= 0) {
			Toast.makeText(this, "已经是第一页了", Toast.LENGTH_SHORT).show();
			return;
		}
		mCurrentPageNumber--;
		initData();
	}

	/**
	 * 下一页
	 *
	 * @param view
	 */
	public void nextPage(View view) {
		//判断当前的页码不能大于总页数
		if (mCurrentPageNumber >= (totalPage - 1)) {
			Toast.makeText(this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
			return;
		}
		mCurrentPageNumber++;
		initData();
	}

	/**
	 * 跳转
	 *
	 * @param view
	 */
	public void jump(View view) {
		String strPageNumber = etPageNumber.getText().toString().trim();
		if (TextUtils.isEmpty(strPageNumber)) {
			Toast.makeText(this, "请输入正确的页码", Toast.LENGTH_SHORT).show();
		} else {
			int number = Integer.parseInt(strPageNumber);
			if (number >= 0 && number <= (totalPage - 1)) {
				mCurrentPageNumber = number;
				initData();
			} else {
				Toast.makeText(this, "请输入正确的页码", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
