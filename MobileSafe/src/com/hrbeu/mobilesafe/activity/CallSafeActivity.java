package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
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
public class CallSafeActivity extends Activity {

	private ListView listView;
	private List<BlackNumberInfo> blackNumberInfos;
	private LinearLayout llpb;
	private BlackNumberDao dao;
	private CallSafeAdper adaper;
	/**
	 * 开始的位置
	 */
	private int mStartIndex = 0;
	/**
	 * 每一页展示20条数据
	 */
	private int maxCount = 20;

	/**
	 * 页面的总个数
	 */
	private int totalPage;
	private int totalNumber;
	private CallSafeAdper callSafeAdper;

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
			if (adaper == null) {
				adaper = new CallSafeAdper(blackNumberInfos, CallSafeActivity.this);
				listView.setAdapter(adaper);
			} else {
				adaper.notifyDataSetChanged();
			}
		}
	};

	private void initData() {

		dao = new BlackNumberDao(CallSafeActivity.this);
		totalNumber = dao.getTotalNumber();
		new Thread() {
			@Override
			public void run() {
				if (blackNumberInfos == null) {
					blackNumberInfos = dao.findPar2(mStartIndex, maxCount);
				} else {
					//后面的数据追加到blackNumberInfos，防止黑名单被覆盖
					blackNumberInfos.addAll(dao.findPar2(mStartIndex, maxCount));
				}
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	/**
	 * 添加黑名单
	 *
	 * @param view
	 */
	public void addBlackNumber(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View dialogView = View.inflate(this, R.layout.dialog_add_black_number, null);
		final EditText etNumber = (EditText) dialogView.findViewById(R.id.et_number);
		final CheckBox cbPhone = (CheckBox) dialogView.findViewById(R.id.cb_phone);
		final CheckBox cbSms = (CheckBox) dialogView.findViewById(R.id.cb_sms);
		Button btnOK = (Button) dialogView.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String strNumber = etNumber.getText().toString().trim();
				if (TextUtils.isEmpty(strNumber)) {
					Toast.makeText(CallSafeActivity.this, "请输入黑名单号码", Toast.LENGTH_SHORT).show();
					return;
				}

				String mode = "";
				if (cbPhone.isChecked() && cbSms.isChecked()) {
					mode = "1";
				} else if (cbPhone.isChecked()) {
					mode = "2";
				} else if (cbSms.isChecked()) {
					mode = "3";
				} else {
					Toast.makeText(CallSafeActivity.this, "请勾选拦截模式", Toast.LENGTH_SHORT).show();
					return;
				}
				BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
				blackNumberInfo.setNumber(strNumber);
				blackNumberInfo.setMode(mode);
				blackNumberInfos.add(0, blackNumberInfo);
				//添加号码和拦截模式添加到数据库
				dao.add(strNumber, mode);
				if (adaper == null) {
					callSafeAdper = new CallSafeAdper(blackNumberInfos, CallSafeActivity.this);
					listView.setAdapter(callSafeAdper);
				} else {
					adaper.notifyDataSetChanged();
				}
				dialog.dismiss();
			}
		});
		dialog.setView(dialogView);
		dialog.show();
	}

	private void initUI() {
		llpb = (LinearLayout) findViewById(R.id.ll_pb);
		//展示加载的圆圈
		llpb.setVisibility(View.VISIBLE);
		listView = (ListView) findViewById(R.id.list_view);
		//设置listView的滚动监听
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			//状态改变时回调此方法

			/**
			 *
			 * @param view
			 * @param scrollState 滚动的状态
			 */
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				/**
				 * AbsListView.OnScrollListener.SCROLL_STATE_IDLE 闲置状态
				 * AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 手指触摸的状态
				 * AbsListView.OnScrollListener.SCROLL_STATE_FLING 惯性状态
				 */
				switch (scrollState) {
					case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
						//获取到最后一条显示的数据
						int lastVisiblePosition = listView.getLastVisiblePosition();
						System.out.println("lastVisiblePosition:" + lastVisiblePosition);
						if (lastVisiblePosition == blackNumberInfos.size() - 1) {
							//加载更多的数据，更改加载数据的开始位置
							mStartIndex += maxCount;
							if (lastVisiblePosition >= totalNumber) {
								Toast.makeText(CallSafeActivity.this, "已经没有数据了", Toast.LENGTH_SHORT).show();
								return;
							}
						}

						initData();
						break;

				}

			}

			//listView滚动时调用此方法
			//实时调用，手指触摸屏幕就调用
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});
	}

	private class CallSafeAdper extends MyBaseAdapter<BlackNumberInfo> {
		public CallSafeAdper(List lists, Context mContext) {
			super(lists, mContext);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(CallSafeActivity.this, R.layout.item_call_safe, null);
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
						Toast.makeText(CallSafeActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
						lists.remove(info);
						//刷新界面
						adaper.notifyDataSetChanged();
					} else {
						Toast.makeText(CallSafeActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
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

}
