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
import com.example.sjyy_expert_android.activity.user.PerfectDoctorInfoActivity;
import com.example.sjyy_expert_android.entity.choosebean.ChooseDepartmentBean;
/***
 * 类描述：选择疾病
 * 
 * @author 海洋
 */
public class ChooseDiseaseActivity extends BaseActivity {
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private ListView lv_chose_disease;
	private dataAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_disease_activity);
		initview();
	}

	private void initview() {

		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		btn_title_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title_content.setText("选择医院");
		lv_chose_disease = (ListView) findViewById(R.id.lv_chose_disease);
		adapter = new dataAdapter();
		lv_chose_disease.setAdapter(adapter);
		lv_chose_disease.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ChooseDepartmentBean.fistName = application.regis.departmentList
						.get(ChooseDepartmentBean.position).departmentList.get(position).department;
				ChooseDepartmentBean.fistId =  application.regis.departmentList
						.get(ChooseDepartmentBean.position).departmentList.get(position).departmentId;
				ChooseDepartmentBean.ifIntent = true;
				Intent intent = new Intent();
				intent.setClass(ChooseDiseaseActivity.this,
						PerfectDoctorInfoActivity.class);
				startActivity(intent);
			}
		});
	}

	class dataAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return application.regis.departmentList
					.get(ChooseDepartmentBean.position).departmentList
					.size();
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
				convertView = LayoutInflater.from(ChooseDiseaseActivity.this)
						.inflate(R.layout.listview_item, null);
				holder = new ViewHolder();
				holder.tv_list_item = (TextView) convertView
						.findViewById(R.id.tv_list_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_list_item
					.setText(application.regis.departmentList
							.get(ChooseDepartmentBean.position).departmentList.get(position).department);

			return convertView;
		}

	}

}
