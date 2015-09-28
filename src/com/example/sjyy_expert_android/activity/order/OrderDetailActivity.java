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
import com.example.sjyy_expert_android.entity.orderBean.OrderState;
import com.example.sjyy_expert_android.entity.orderBean.OrderStateRespons;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.NoScrollListView;
import com.example.sjyy_expert_android.util.ShareUtil;

/***
 * 类描述：我的需求查看订单轨迹
 * 
 * @author 海洋
 */
public class OrderDetailActivity extends BaseActivity {

	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private final static int SUCCESS = 1;

	private OrderState bean;
	private OrderStateRespons base;

	private NoScrollListView lv_order_state;
	private String orderId;
	private orderStateAdapter adapter;
	private LinearLayout ll_comm_bg_loadingfail, ll_order_zhuti;
	private ImageView iv_comm_bg_loadingfail;
	private TextView tv_comm_bg_loadingfail;
	private TextView tv_orderdetail_state, tv_orderdetail_time,
			tv_doctor_orderdetail_budgetprice, tv_doctor_orderdetail_problem,
			tv_doctor_orderdetail_content;
	private TextView tv_doctor_orderdetail_name, tv_orderdetail_dep,
			tv_orderdetail_area, tv_doctor_orderdetail_qui,
			tv_doctor_orderdetail_hosname, tv_orderdetail_contactPerson,
			tv_orderdetail_address;
	private TextView tv_doctor_orderdetail_areaName,
			tv_doctor_orderdetail_hospitalName,
			tv_doctor_orderdetail_departmentName,
			tv_doctor_orderdetail_qualificationName,
			tv_doctor_orderdetail_isBusinessTripString,tv_orderdetail_ordertime;
	private LinearLayout ll_doctorinfo, ll_hosinfo;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				ll_comm_bg_loadingfail.setVisibility(View.GONE);
				ll_order_zhuti.setVisibility(View.VISIBLE);
				if (msg.obj != null && msg.obj instanceof OrderStateRespons) {

					base = (OrderStateRespons) msg.obj;
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
		setContentView(R.layout.order_detail_activity);
		initview();
		initdatas();
	}

	private void initdatas() {
		ll_comm_bg_loadingfail.setVisibility(View.GONE);
		startProgressDialog();
		new dataThread().start();

	}

	private void initview() {
		ll_doctorinfo = (LinearLayout) findViewById(R.id.ll_doctorinfo);
		ll_hosinfo = (LinearLayout) findViewById(R.id.ll_hosinfo);
		ll_order_zhuti = (LinearLayout) findViewById(R.id.ll_order_zhuti);
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
		tv_orderdetail_ordertime=(TextView) findViewById(R.id.tv_orderdetail_ordertime);
		tv_orderdetail_state = (TextView) findViewById(R.id.tv_orderdetail_state);
		tv_orderdetail_time = (TextView) findViewById(R.id.tv_orderdetail_time);
		tv_doctor_orderdetail_budgetprice = (TextView) findViewById(R.id.tv_doctor_orderdetail_budgetprice);
		tv_doctor_orderdetail_problem = (TextView) findViewById(R.id.tv_doctor_orderdetail_problem);
		tv_doctor_orderdetail_content = (TextView) findViewById(R.id.tv_doctor_orderdetail_content);
		lv_order_state = (NoScrollListView) findViewById(R.id.lv_order_state);
		lv_order_state.setDivider(null);

		tv_doctor_orderdetail_name = (TextView) findViewById(R.id.tv_doctor_orderdetail_name);
		tv_orderdetail_dep = (TextView) findViewById(R.id.tv_orderdetail_dep);
		tv_orderdetail_area = (TextView) findViewById(R.id.tv_orderdetail_area);
		tv_doctor_orderdetail_qui = (TextView) findViewById(R.id.tv_doctor_orderdetail_qui);

		tv_doctor_orderdetail_hosname = (TextView) findViewById(R.id.tv_doctor_orderdetail_hosname);
		tv_orderdetail_contactPerson = (TextView) findViewById(R.id.tv_orderdetail_contactPerson);
		tv_orderdetail_address = (TextView) findViewById(R.id.tv_orderdetail_address);
		
		
		tv_doctor_orderdetail_areaName=(TextView) findViewById(R.id.tv_doctor_orderdetail_areaName);
		tv_doctor_orderdetail_hospitalName=(TextView) findViewById(R.id.tv_doctor_orderdetail_hospitalName);
		tv_doctor_orderdetail_departmentName=(TextView) findViewById(R.id.tv_doctor_orderdetail_departmentName);
		tv_doctor_orderdetail_qualificationName=(TextView) findViewById(R.id.tv_doctor_orderdetail_qualificationName);
		tv_doctor_orderdetail_isBusinessTripString=(TextView) findViewById(R.id.tv_doctor_orderdetail_isBusinessTripString);
		
		
		
		
	}

	class dataThread extends Thread {

		@Override
		public void run() {
			bean = new OrderState();
			bean.userId = ShareUtil.getAccountId(OrderDetailActivity.this);
			bean.demandId = orderId;
			Object data = NetRequestEngine.getOrderState(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}
	}

	private class orderStateAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return base.demandDetailList.size();
		}

		@Override
		public Object getItem(int position) {
			return base.demandDetailList.get(position);
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
				convertView = LayoutInflater.from(OrderDetailActivity.this)
						.inflate(R.layout.order_state_item, null);
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

			if (base.demandDetailList.size() == 1) {

				// 当只有一行状态时
				if (!base.demandDetailList.get(position).cancelReason
						.equals("")
						&& base.demandDetailList.get(position).cancelReason != null) {
					holder.tv_user_state.setText(base.demandDetailList
							.get(position).operateUser
							+ " "
							+ base.demandDetailList.get(position).operateStr
							+ " "
							+ base.demandDetailList.get(position).cancelReason);
				} else {
					holder.tv_user_state.setText(base.demandDetailList
							.get(position).operateUser
							+ " "
							+ base.demandDetailList.get(position).operateStr);
				}

				holder.tv_state_time.setText(base.demandDetailList
						.get(position).createTimeString);
				holder.tv_user_state.setTextColor(Color.parseColor("#4dd0c8"));
				holder.tv_state_time.setTextColor(Color.parseColor("#4dd0c8"));
				holder.v_top.setVisibility(View.INVISIBLE);
				holder.iv_point.setImageResource(R.drawable.icon_point_orange);
				holder.v_bottom.setVisibility(View.INVISIBLE);

			} else if (position + 1 == base.demandDetailList.size()) {

				// 当前为列表最后一行时
				if (!base.demandDetailList.get(position).cancelReason
						.equals("")
						&& base.demandDetailList.get(position).cancelReason != null) {
					holder.tv_user_state.setText(base.demandDetailList
							.get(position).operateUser
							+ " "
							+ base.demandDetailList.get(position).operateStr
							+ " "
							+ base.demandDetailList.get(position).cancelReason);
				} else {
					holder.tv_user_state.setText(base.demandDetailList
							.get(position).operateUser
							+ " "
							+ base.demandDetailList.get(position).operateStr);
				}

				holder.tv_state_time.setText(base.demandDetailList
						.get(position).createTimeString);
				holder.tv_user_state.setTextColor(Color.parseColor("#adadad"));
				holder.tv_state_time.setTextColor(Color.parseColor("#adadad"));
				holder.v_top.setVisibility(View.VISIBLE);
				holder.iv_point.setImageResource(R.drawable.icon_point_gray);
				holder.v_bottom.setVisibility(View.INVISIBLE);

			} else if (position == 0) {

				// 当前为列表第一行时
				if (!base.demandDetailList.get(position).cancelReason
						.equals("")
						&& base.demandDetailList.get(position).cancelReason != null) {
					holder.tv_user_state.setText(base.demandDetailList
							.get(position).operateUser
							+ " "
							+ base.demandDetailList.get(position).operateStr
							+ " "
							+ base.demandDetailList.get(position).cancelReason);
				} else {
					holder.tv_user_state.setText(base.demandDetailList
							.get(position).operateUser
							+ " "
							+ base.demandDetailList.get(position).operateStr);
				}

				holder.tv_state_time.setText(base.demandDetailList
						.get(position).createTimeString);
				holder.tv_user_state.setTextColor(Color.parseColor("#4dd0c8"));
				holder.tv_state_time.setTextColor(Color.parseColor("#4dd0c8"));
				holder.v_top.setVisibility(View.INVISIBLE);
				holder.iv_point.setImageResource(R.drawable.icon_point_orange);
				holder.v_bottom.setVisibility(View.VISIBLE);

			} else {

				// 中间显示样式
				holder.tv_user_state.setText(base.demandDetailList
						.get(position).operateUser
						+ " "
						+ base.demandDetailList.get(position).operateStr);
				holder.tv_state_time.setText(base.demandDetailList
						.get(position).createTimeString);
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
		lv_order_state.setAdapter(adapter);
		tv_orderdetail_state.setText(base.demand.demandStatusStr);
		tv_orderdetail_time.setText(base.demand.demandTimeString);
		tv_doctor_orderdetail_budgetprice
				.setText(base.demand.demandPrice + "元");
		tv_doctor_orderdetail_problem.setText(base.demand.demandTitle);
		tv_doctor_orderdetail_content.setText(base.demand.demandDesc);
		tv_doctor_orderdetail_areaName.setText(base.demand.areaName);
		tv_doctor_orderdetail_hospitalName.setText(base.demand.hospitalName);
		tv_doctor_orderdetail_departmentName.setText(base.demand.departmentName);
		tv_doctor_orderdetail_qualificationName.setText(base.demand.qualificationName);
		tv_doctor_orderdetail_isBusinessTripString.setText(base.demand.isBusinessTripString);
		tv_orderdetail_ordertime.setText(base.demand.createTimeString);
		
		
		if (base.sourceType == 1) {
			ll_doctorinfo.setVisibility(View.VISIBLE);
			ll_hosinfo.setVisibility(View.GONE);
			tv_doctor_orderdetail_name.setText(base.source.doctorName);
			tv_orderdetail_dep.setText(base.source.departmentName);
			tv_orderdetail_area.setText(base.source.areaString);
			tv_doctor_orderdetail_qui.setText(base.source.qualification);
		} else if (base.sourceType == 2) {
			ll_doctorinfo.setVisibility(View.GONE);
			ll_hosinfo.setVisibility(View.VISIBLE);
			tv_doctor_orderdetail_hosname.setText(base.source.hospitalName);
			tv_orderdetail_contactPerson.setText(base.source.contactPerson);
			tv_orderdetail_address.setText(base.source.address);
		}
		
	}
}
