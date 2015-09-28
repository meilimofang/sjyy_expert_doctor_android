package com.example.sjyy_expert_android.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.choosebean.ChooseHospitalBean;



/***
 * 类描述：选择区域
 * 
 * @author 海洋
 */
public class HospitalChooseCityActivity extends BaseActivity {
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private ListView lv_chose_city;
	private dataAdapter adapter;
	private int pos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_city_activity);
		initview();
	}

	private void initview() {
		Intent intent = getIntent();
		pos = intent.getIntExtra("position", 0);
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setText("");
		btn_title_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		tv_title_content.setText("选择区域");
		lv_chose_city = (ListView) findViewById(R.id.lv_chose_city);
		adapter = new dataAdapter();
		lv_chose_city.setAdapter(adapter);
		lv_chose_city.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ChooseHospitalBean.secondName = application.regis.areaList
						.get(pos).sonAreaList.get(position).areaName;
				ChooseHospitalBean.secondId = application.regis.areaList
						.get(pos).sonAreaList.get(position).areaId;
				ChooseHospitalBean.ifTwoIntent=true;
				Intent intent = new Intent();
				intent.setClass(HospitalChooseCityActivity.this,
						PerfectHospitalInfoActivity.class);
				startActivity(intent);
			}
		});
	}

	class dataAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return application.regis.areaList.get(pos).sonAreaList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		class ViewHolder {
			TextView tv_list_item;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(HospitalChooseCityActivity.this)
						.inflate(R.layout.listview_item, null);
				holder = new ViewHolder();
				holder.tv_list_item = (TextView) convertView
						.findViewById(R.id.tv_list_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_list_item
					.setText(application.regis.areaList.get(pos).sonAreaList
							.get(position).areaName);

			return convertView;
		}

	}

}
