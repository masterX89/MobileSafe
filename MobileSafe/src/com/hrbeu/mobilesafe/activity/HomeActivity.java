package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.hrbeu.mobilesafe.R;

/**
 * Ö÷Ò³Ãæ
 * 
 * @author Hankai Xia
 * 
 */
public class HomeActivity extends Activity {

	private GridView gvHome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		gvHome = (GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(null);
	}

	class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return null;
		}

	}
}
