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
import com.example.sjyy_expert_android.entity.choosebean.ChooseTitle;
import com.example.sjyy_expert_android.entity.choosebean.ChooseTitleBean;


/***
 * 类描述：选择职称
 * 
 * @author 海洋
 */
public class ChooseTitleActivity extends BaseActivity {
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private ListView lv_chose_title;
	private dataAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_title_activity);
		initview();
	}

	private void initview() {
		ChooseTitleBean.list=application.regis.qualificationList;
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		tv_title_content.setText("选择职称");
		btn_title_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		lv_chose_title=(ListView) findViewById(R.id.lv_chose_title);
		adapter=new dataAdapter();
		lv_chose_title.setAdapter(adapter);
		
		lv_chose_title.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ChooseTitle.fistName=ChooseTitleBean.list.get(position).qualification;
				ChooseTitle.fistId=ChooseTitleBean.list.get(position).qualificationId;
				ChooseTitle.ifIntent=true;
				Intent intent=new Intent();
				intent.setClass(ChooseTitleActivity.this, PerfectDoctorInfoActivity.class);
				startActivity(intent);
			}
		});
		
	}

	class dataAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return ChooseTitleBean.list.size();
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
				convertView = LayoutInflater.from(ChooseTitleActivity.this).inflate(
						R.layout.listview_item, null);
				holder = new ViewHolder();
				holder.tv_list_item=(TextView)convertView.findViewById(R.id.tv_list_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_list_item.setText(ChooseTitleBean.list.get(position).qualification);
			return convertView;
		}

	}
	
}
