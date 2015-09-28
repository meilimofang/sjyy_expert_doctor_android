package com.example.sjyy_expert_android.activity.choose;

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
 * 类描述：选择城市
 * 
 * @author 海洋
 */
public class ChooseAreaActivity extends BaseActivity {
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private ListView lv_chose_area;
	private dataAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_area_activity);
		initview();
	}

	private void initview() {
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		btn_title_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title_content.setText("选择城市");
		lv_chose_area=(ListView) findViewById(R.id.lv_chose_area);
		adapter=new dataAdapter();
		lv_chose_area.setAdapter(adapter);
		lv_chose_area.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ChooseHospitalBean.fistName=application.regis.areaList.get(position).areaName;
				ChooseHospitalBean.fistId=application.regis.areaList.get(position).areaId;
				Intent intent=new Intent();
				intent.setClass(ChooseAreaActivity.this, ChooseCityActivity.class);
				intent.putExtra("position", position);
				startActivity(intent);
				
				
			}
			
			
		});
	}

	class dataAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return application.regis.areaList.size();
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
				convertView = LayoutInflater.from(ChooseAreaActivity.this).inflate(
						R.layout.listview_item, null);
				holder = new ViewHolder();
				holder.tv_list_item=(TextView)convertView.findViewById(R.id.tv_list_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_list_item.setText(application.regis.areaList.get(position).areaName);
			
			return convertView;
		}

	}
	
}
