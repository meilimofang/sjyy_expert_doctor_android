package com.example.sjyy_expert_android.activity.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.orderBean.AppointmentOrderDetailRepson;
import com.example.sjyy_expert_android.entity.orderBean.AppointmentOrderDetailState;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.NoScrollListView;
import com.example.sjyy_expert_android.util.ShareUtil;

/***
 * 类描述：我预约查看订单轨迹
 * 
 * @author 海洋
 */
public class AppointmentOrderDetailActivity extends BaseActivity {

	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private final static int SUCCESS = 1;

	private AppointmentOrderDetailState bean;
	private AppointmentOrderDetailRepson base;

	private NoScrollListView lv_appointment_state;
	private String orderId;
	private orderStateAdapter adapter;
	private LinearLayout ll_comm_bg_loadingfail, ll_order_zhuti;
	private ImageView iv_comm_bg_loadingfail;
	private TextView tv_comm_bg_loadingfail;
	private TextView tv_doctor_appointmentdetail_number,
			tv_doctor_appointmentdetail_name,
			tv_doctor_appointmentdetail_content, tv_appointmentdetail_state,
			tv_appointmentdetail_time,tv_appointmentdetail_ordertime;

	private LinearLayout ll_appointment_doctorinfo, ll_appointment_hosinfo;
	private TextView tv_appointment_orderdetail_name,tv_appointment_orderdetail_hos,
			tv_appointment_orderdetail_dep, tv_appointment_orderdetail_area,
			tv_appointment_orderdetail_qui, tv_appointment_orderdetail_hosname,
			tv_appointment_orderdetail_contactPerson,
			tv_appointment_orderdetail_address;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				ll_comm_bg_loadingfail.setVisibility(View.GONE);
				ll_order_zhuti.setVisibility(View.VISIBLE);
				if (msg.obj != null
						&& msg.obj instanceof AppointmentOrderDetailRepson) {

					base = (AppointmentOrderDetailRepson) msg.obj;
					if (base.resultCode.equals("1000")) {

						initdata();
					} else {
						ll_order_zhuti.setVisibility(View.GONE);
						ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
						tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
					}
				} else {
					ll_order_zhuti.setVisibility(View.GONE);
					ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
					tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
				}

				break;

			default:
				break;
			}
			stopProgressDialog();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appointment_order_detail_activity);
		initview();
		initdatas();
	}

	private void initdatas() {
		ll_comm_bg_loadingfail.setVisibility(View.GONE);
		startProgressDialog();
		new dataThread().start();

	}

	private void initview() {
		ll_order_zhuti = (LinearLayout) findViewById(R.id.ll_appointment_zhu);
		ll_comm_bg_loadingfail = (LinearLayout) findViewById(R.id.ll_comm_bg_loadingfail);
		ll_comm_bg_loadingfail.setVisibility(View.INVISIBLE);
		iv_comm_bg_loadingfail = (ImageView) findViewById(R.id.iv_comm_bg_loadingfail);
		tv_comm_bg_loadingfail = (TextView) findViewById(R.id.tv_comm_bg_loadingfail);
		iv_comm_bg_loadingfail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initdatas();
			}
		});
		Intent intent = getIntent();
		orderId = intent.getStringExtra("orderId");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_title_left.setText("");
		btn_title_right.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		tv_title_content.setText("查看详情");
		tv_doctor_appointmentdetail_number = (TextView) findViewById(R.id.tv_doctor_appointmentdetail_number);
		tv_doctor_appointmentdetail_name = (TextView) findViewById(R.id.tv_doctor_appointmentdetail_name);
		tv_appointmentdetail_state = (TextView) findViewById(R.id.tv_appointmentdetail_state);
		tv_appointmentdetail_time = (TextView) findViewById(R.id.tv_appointmentdetail_time);
		tv_doctor_appointmentdetail_content = (TextView) findViewById(R.id.tv_doctor_appointmentdetail_content);
		lv_appointment_state = (NoScrollListView) findViewById(R.id.lv_appointment_state);
		lv_appointment_state.setDivider(null);
		tv_appointmentdetail_ordertime=(TextView) findViewById(R.id.tv_appointmentdetail_ordertime);
		ll_appointment_doctorinfo = (LinearLayout) findViewById(R.id.ll_appointment_doctorinfo);
		ll_appointment_hosinfo = (LinearLayout) findViewById(R.id.ll_appointment_hosinfo);
		
		
		tv_appointment_orderdetail_name=(TextView) findViewById(R.id.tv_appointment_orderdetail_name);
		tv_appointment_orderdetail_dep=(TextView) findViewById(R.id.tv_appointment_orderdetail_dep);
		tv_appointment_orderdetail_area=(TextView) findViewById(R.id.tv_appointment_orderdetail_area);
		tv_appointment_orderdetail_qui=(TextView) findViewById(R.id.tv_appointment_orderdetail_qui);
		
		tv_appointment_orderdetail_hosname=(TextView) findViewById(R.id.tv_appointment_orderdetail_hosname);
		tv_appointment_orderdetail_contactPerson=(TextView) findViewById(R.id.tv_appointment_orderdetail_contactPerson);
		tv_appointment_orderdetail_address=(TextView) findViewById(R.id.tv_appointment_orderdetail_address);
		tv_appointment_orderdetail_hos=(TextView) findViewById(R.id.tv_appointment_orderdetail_hos);
		
		
		
		
	}

	class dataThread extends Thread {

		@Override
		public void run() {
			bean = new AppointmentOrderDetailState();
			bean.userId = ShareUtil
					.getAccountId(AppointmentOrderDetailActivity.this);
			bean.orderId = orderId;
			Object data = NetRequestEngine.getOrderDetail(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}
	}

	private class orderStateAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return base.details.size();
		}

		@Override
		public Object getItem(int position) {
			return base.details.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			TextView tv_user_state, tv_state_time;
			View v_bottom, v_top;
			ImageView iv_point;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(
						AppointmentOrderDetailActivity.this).inflate(
						R.layout.order_state_item, null);
				holder = new ViewHolder();
				holder.tv_user_state = (TextView) convertView
						.findViewById(R.id.tv_user_state);
				holder.tv_state_time = (TextView) convertView
						.findViewById(R.id.tv_state_time);
				holder.v_bottom = convertView.findViewById(R.id.v_bottom);
				holder.v_top = convertView.findViewById(R.id.v_top);
				holder.iv_point = (ImageView) convertView
						.findViewById(R.id.iv_point);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (base.details.size() == 1) {

				// 当只有一行状态时
				if (!base.details.get(position).operateDesc.equals("")
						&& base.details.get(position).operateDesc != null) {
					holder.tv_user_state
							.setText(base.details.get(position).operateUser
									+ " "
									+ base.details.get(position).operateStr
									+ " "
									+ base.details.get(position).operateDesc);
				} else {
					holder.tv_user_state
							.setText(base.details.get(position).operateUser
									+ " "
									+ base.details.get(position).operateStr);
				}

				holder.tv_state_time
						.setText(base.details.get(position).createTimeString);
				holder.tv_user_state.setTextColor(Color.parseColor("#4dd0c8"));
				holder.tv_state_time.setTextColor(Color.parseColor("#4dd0c8"));
				holder.v_top.setVisibility(View.INVISIBLE);
				holder.iv_point.setImageResource(R.drawable.icon_point_orange);
				holder.v_bottom.setVisibility(View.INVISIBLE);

			} else if (position + 1 == base.details.size()) {

				// 当前为列表最后一行时
				if (!base.details.get(position).operateDesc.equals("")
						&& base.details.get(position).operateDesc != null) {
					holder.tv_user_state
							.setText(base.details.get(position).operateUser
									+ " "
									+ base.details.get(position).operateStr
									+ " "
									+ base.details.get(position).operateDesc);
				} else {
					holder.tv_user_state
							.setText(base.details.get(position).operateUser
									+ " "
									+ base.details.get(position).operateStr);
				}

				holder.tv_state_time
						.setText(base.details.get(position).createTimeString);
				holder.tv_user_state.setTextColor(Color.parseColor("#adadad"));
				holder.tv_state_time.setTextColor(Color.parseColor("#adadad"));
				holder.v_top.setVisibility(View.VISIBLE);
				holder.iv_point.setImageResource(R.drawable.icon_point_gray);
				holder.v_bottom.setVisibility(View.INVISIBLE);

			} else if (position == 0) {

				// 当前为列表第一行时
				if (!base.details.get(position).operateDesc.equals("")
						&& base.details.get(position).operateDesc != null) {
					holder.tv_user_state
							.setText(base.details.get(position).operateUser
									+ " "
									+ base.details.get(position).operateStr
									+ " "
									+ base.details.get(position).operateDesc);
				} else {
					holder.tv_user_state
							.setText(base.details.get(position).operateUser
									+ " "
									+ base.details.get(position).operateStr);
				}

				holder.tv_state_time
						.setText(base.details.get(position).createTimeString);
				holder.tv_user_state.setTextColor(Color.parseColor("#4dd0c8"));
				holder.tv_state_time.setTextColor(Color.parseColor("#4dd0c8"));
				holder.v_top.setVisibility(View.INVISIBLE);
				holder.iv_point.setImageResource(R.drawable.icon_point_orange);
				holder.v_bottom.setVisibility(View.VISIBLE);

			} else {

				// 中间显示样式
				holder.tv_user_state
						.setText(base.details.get(position).operateUser + " "
								+ base.details.get(position).operateStr+
								" "
								+ base.details.get(position).operateDesc
								);
				holder.tv_state_time
						.setText(base.details.get(position).createTimeString);
				holder.tv_user_state.setTextColor(Color.parseColor("#adadad"));
				holder.tv_state_time.setTextColor(Color.parseColor("#adadad"));
				holder.v_bottom.setVisibility(View.VISIBLE);
				holder.v_top.setVisibility(View.VISIBLE);
				holder.iv_point.setImageResource(R.drawable.icon_point_gray);
				holder.v_bottom.setVisibility(View.VISIBLE);
			}
			return convertView;
		}
	}

	private void initdata() {
		adapter = new orderStateAdapter();
		lv_appointment_state.setAdapter(adapter);
		tv_doctor_appointmentdetail_number.setText(base.orderInfo.orderNumber);
		tv_doctor_appointmentdetail_name.setText(base.orderInfo.ordererName);
		tv_appointmentdetail_state.setText(base.orderInfo.orderStatusString);
		tv_appointmentdetail_time.setText(base.orderInfo.bespeakTimeString);
		tv_appointmentdetail_ordertime.setText(base.orderInfo.createTimeString);
		tv_doctor_appointmentdetail_content
				.setText(base.orderInfo.bespeakRequire);
		if(base.sourceType==1){
			ll_appointment_doctorinfo.setVisibility(View.VISIBLE);
			ll_appointment_hosinfo.setVisibility(View.GONE);
			tv_appointment_orderdetail_name.setText(base.source.doctorName);
			tv_appointment_orderdetail_hos.setText(base.source.hospitalName);
			tv_appointment_orderdetail_dep.setText(base.source.departmentName);
			tv_appointment_orderdetail_qui.setText(base.source.qualification);
			tv_appointment_orderdetail_area.setText(base.source.areaString);
		}else if(base.sourceType==2){
			ll_appointment_doctorinfo.setVisibility(View.GONE);
			ll_appointment_hosinfo.setVisibility(View.VISIBLE);
			tv_appointment_orderdetail_hosname.setText(base.source.hospitalName);
			tv_appointment_orderdetail_contactPerson.setText(base.source.contactPerson);
			tv_appointment_orderdetail_address.setText(base.source.address);
		}
	}
}
