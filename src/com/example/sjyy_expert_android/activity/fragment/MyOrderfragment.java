package com.example.sjyy_expert_android.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.example.sjyy_expert_android.MyApplication;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.activity.order.AppointmentOrderDetailActivity;
import com.example.sjyy_expert_android.activity.order.OrderDetailActivity;
import com.example.sjyy_expert_android.activity.order.RefuseActivity;
import com.example.sjyy_expert_android.activity.patient.SeeCaseDetailActivity;
import com.example.sjyy_expert_android.entity.BaseRequest;
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.loadmore.LoadMoreXuBean;
import com.example.sjyy_expert_android.entity.loadmore.LoadMoreYueBean;
import com.example.sjyy_expert_android.entity.orderBean.ChangeOrderDetail;
import com.example.sjyy_expert_android.entity.orderBean.MyOrderBean;
import com.example.sjyy_expert_android.entity.orderBean.MyVisitList;
import com.example.sjyy_expert_android.entity.orderBean.XuQiuBean;
import com.example.sjyy_expert_android.entity.patient.PatientList;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.CollapsibleTextView;
import com.example.sjyy_expert_android.util.CustomProgressDialog;
import com.example.sjyy_expert_android.util.ShareUtil;
import com.example.sjyy_expert_android.util.XListView;
import com.example.sjyy_expert_android.util.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 类描述：我的订单
 * 
 * @author 海洋
 */
public class MyOrderfragment extends Fragment implements OnClickListener,
		IXListViewListener {

	private MyApplication application;
	private XListView lv_seeking;
	private CustomProgressDialog progressDialog = null;
	private final static int SUCCESS = 1;
	private final static int SUCCESSTWO = 2;
	private final static int SUCCESSTHREE = 3;
	private final static int SUCCESSFOUR = 4;
	private final static int SUCCESSFIVE = 5;
	private BaseRequest bean;
	private MyOrderBean base;
	private XuQiuBean xuqiubean;
	private BaseRespons respons;
	private MyOrderBean loadMorebase;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options;
	private LinearLayout ll_comm_bg_loadingfail, ll_mycall_zhuti,ll_common_nodata;
	private ImageView iv_comm_bg_loadingfail;
	private TextView tv_comm_bg_loadingfail;
	private onlineAdapter yueAdapter;
	private xuqiuAdapter xuAdapter;
	private ChangeOrderDetail request;
	private Button bt_my_needs, bt_my_reservation;
	private Integer orderStatus, orderId;
	private boolean type=true;
	private LoadMoreXuBean loadmoreXuqiu;
	private LoadMoreYueBean loadmoreYueqiu;
	private Integer currentPageXu;
	private Integer currentPageYue;
	private XuQiuBean loadxuqiubean;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case SUCCESS:
				ll_comm_bg_loadingfail.setVisibility(View.GONE);
				ll_common_nodata.setVisibility(View.GONE);
				ll_mycall_zhuti.setVisibility(View.VISIBLE);
				if (msg.obj != null && msg.obj instanceof MyOrderBean) {
					base = (MyOrderBean) msg.obj;
					if (base.resultCode.equals("1000")) {
						initadapter();
					} else {
						ll_common_nodata.setVisibility(View.GONE);
						ll_mycall_zhuti.setVisibility(View.GONE);
						ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
						tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
					}

				} else {
					ll_common_nodata.setVisibility(View.GONE);
					ll_mycall_zhuti.setVisibility(View.GONE);
					ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
					tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
				}

				break;

			case SUCCESSTWO:
				ll_common_nodata.setVisibility(View.GONE);
				ll_comm_bg_loadingfail.setVisibility(View.GONE);
				ll_mycall_zhuti.setVisibility(View.VISIBLE);
				if (msg.obj != null && msg.obj instanceof BaseRespons) {
					respons = (BaseRespons) msg.obj;
					if (respons.resultCode.equals("1000")) {
						initdata();
					} else {
						ll_common_nodata.setVisibility(View.GONE);
						ll_mycall_zhuti.setVisibility(View.GONE);
						ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
						tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
					}

				} else {
					ll_common_nodata.setVisibility(View.GONE);
					ll_mycall_zhuti.setVisibility(View.GONE);
					ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
					tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
				}

				break;

			case SUCCESSTHREE:
				ll_common_nodata.setVisibility(View.GONE);
				ll_comm_bg_loadingfail.setVisibility(View.GONE);
				ll_mycall_zhuti.setVisibility(View.VISIBLE);
				if (msg.obj != null && msg.obj instanceof XuQiuBean) {
					xuqiubean = (XuQiuBean) msg.obj;
					if (xuqiubean.resultCode.equals("1000")) {
						initadapters();
					} else {
						ll_common_nodata.setVisibility(View.GONE);
						ll_mycall_zhuti.setVisibility(View.GONE);
						ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
						tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
					}

				} else {
					ll_common_nodata.setVisibility(View.GONE);
					ll_mycall_zhuti.setVisibility(View.GONE);
					ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
					tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
				}

				break;
			case SUCCESSFOUR:
				if (msg.obj != null && msg.obj instanceof MyOrderBean) {

					loadMorebase = (MyOrderBean) msg.obj;
					if (loadMorebase.orderTotalCount == base.orderList
							.size()) {
						Toast.makeText(getActivity(), "无更多数据！", 0).show();
					} else {

						for (PatientList vi : loadMorebase.orderList) {
							base.orderList.add(vi);
						}

						loadYuInitData();
					}
					

				} else {
					Toast.makeText(getActivity(), msg.obj + "", 0).show();
				}
				break;
			case SUCCESSFIVE:
				if (msg.obj != null && msg.obj instanceof XuQiuBean) {
					
					loadxuqiubean = (XuQiuBean) msg.obj;
					if (loadxuqiubean.demandTotalCount == xuqiubean.demandList
							.size()) {
						Toast.makeText(getActivity(), "无更多数据！", 0).show();
					} else {

						for (MyVisitList vi : loadxuqiubean.demandList) {
							xuqiubean.demandList.add(vi);
						}

						loadXuInitData();
					}
					
				} else {
					Toast.makeText(getActivity(), msg.obj + "", 0).show();
				}

				break;
			default:
				break;
			}
			stopProgressDialog();
			onLoad();
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_order_fragment, container,
				false);
		currentPageXu=1;
		currentPageYue=1;
		ll_comm_bg_loadingfail = (LinearLayout) view
				.findViewById(R.id.ll_comm_bg_loadingfail);
		ll_comm_bg_loadingfail.setVisibility(View.INVISIBLE);
		iv_comm_bg_loadingfail = (ImageView) view
				.findViewById(R.id.iv_comm_bg_loadingfail);
		tv_comm_bg_loadingfail = (TextView) view
				.findViewById(R.id.tv_comm_bg_loadingfail);
		ll_common_nodata=(LinearLayout) view.findViewById(R.id.ll_common_nodata);
		iv_comm_bg_loadingfail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				initdata();
			}
		});
		ll_mycall_zhuti = (LinearLayout) view
				.findViewById(R.id.ll_mycall_zhuti);
		lv_seeking = (XListView) view.findViewById(R.id.lv_seeking);
		lv_seeking.setPullLoadEnable(true);
		lv_seeking.setXListViewListener(this);
		lv_seeking.setPullRefreshEnable(true);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.img_no_net_pic)
				.showImageForEmptyUri(R.drawable.img_no_net_pic)
				.cacheInMemory(true).cacheOnDisc(true).build();
		bt_my_needs = (Button) view.findViewById(R.id.bt_my_needs);
		bt_my_reservation = (Button) view.findViewById(R.id.bt_my_reservation);
		// 我的需求
		bt_my_needs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				type=true;
				initdata();
				bt_my_needs.setBackgroundResource(R.drawable.xuqiuliebiao);
				bt_my_reservation
						.setBackgroundResource(R.drawable.yuyueliebiao_click);
			}
		});
		// 我的预约
		bt_my_reservation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				type=false;
				initdatas();
				bt_my_needs
						.setBackgroundResource(R.drawable.xuqiuliebiao_click);
				bt_my_reservation
						.setBackgroundResource(R.drawable.yuyueliebiao);
			}
		});

		initdata();
		return view;
	}

	private void initdata() {
		ll_comm_bg_loadingfail.setVisibility(View.GONE);
		startProgressDialog();
		new xuqiuThread().start();
	}

	private void initdatas() {
		ll_comm_bg_loadingfail.setVisibility(View.GONE);
		startProgressDialog();
		new yuyueThread().start();
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onRefresh() {
		initdata();
		if (type) {
			currentPageXu=1;
			initdata();
		} else {
			currentPageYue=1;
			initdatas();
		}
	}

	@Override
	public void onLoadMore() {

		if (type) {// 需求
			startProgressDialog();
			currentPageXu++;
			new loadMorexuqiuThread().start();
		} else {// 预约
			startProgressDialog();
			currentPageYue++;
			new loadMoreyuyueThread().start();

		}
	}
	
	

	// /我的预约
	class onlineAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return base.orderList.size();
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
			TextView tv_patient_name,tv_patient_ordertime, tv_patient_suggest, tv_patient_problem,
					tv_patient_orderstate, tv_patient_time;
			Button bt_patient_details,bt_patient_see_case;
			// CircularImage iv_my_appointment_icon;
			ImageView iv_my_appointment_zuo, iv_my_appointment_shu,
					iv_my_appointment_hui;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.my_appointmen_item, null);
				holder = new ViewHolder();
				holder.tv_patient_name = (TextView) convertView
						.findViewById(R.id.tv_patient_name);
				holder.tv_patient_ordertime = (TextView) convertView
						.findViewById(R.id.tv_patient_ordertime);
				holder.tv_patient_suggest = (TextView) convertView
						.findViewById(R.id.tv_patient_suggest);
				holder.tv_patient_problem = (TextView) convertView
						.findViewById(R.id.tv_patient_problem);
				holder.tv_patient_orderstate = (TextView) convertView
						.findViewById(R.id.tv_patient_orderstate);
				holder.tv_patient_time = (TextView) convertView
						.findViewById(R.id.tv_patient_time);
				holder.bt_patient_details = (Button) convertView
						.findViewById(R.id.bt_patient_details);
				holder.bt_patient_see_case = (Button) convertView
						.findViewById(R.id.bt_patient_see_case);
				// holder.iv_my_appointment_icon = (CircularImage) convertView
				// .findViewById(R.id.iv_my_appointment_icon);
				holder.iv_my_appointment_zuo = (ImageView) convertView
						.findViewById(R.id.iv_my_appointment_zuo);
				holder.iv_my_appointment_shu = (ImageView) convertView
						.findViewById(R.id.iv_my_appointment_shu);
				holder.iv_my_appointment_hui = (ImageView) convertView
						.findViewById(R.id.iv_my_appointment_hui);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_patient_name
					.setText(base.orderList.get(position).ordererName);
			holder.tv_patient_problem
					.setText(base.orderList.get(position).bespeakRequire);
			holder.tv_patient_orderstate
					.setText(base.orderList.get(position).orderStatusString);
			holder.tv_patient_time
					.setText(base.orderList.get(position).bespeakTimeString);
			// imageLoader.displayImage(
			// base.orderList.get(position).iconUrl,
			// holder.iv_my_appointment_icon, options);
			holder.tv_patient_ordertime.setText(base.orderList.get(position).createTimeString);
			if (base.orderList.get(position).serviceTypeId == 1) {
				holder.iv_my_appointment_zuo.setVisibility(View.VISIBLE);
			} else {
				holder.iv_my_appointment_zuo.setVisibility(View.GONE);
			}

			if (base.orderList.get(position).serviceTypeId == 2) {
				holder.iv_my_appointment_hui.setVisibility(View.VISIBLE);
			} else {
				holder.iv_my_appointment_hui.setVisibility(View.GONE);
			}

			if (base.orderList.get(position).serviceTypeId == 3) {
				holder.iv_my_appointment_shu.setVisibility(View.VISIBLE);
			} else {
				holder.iv_my_appointment_shu.setVisibility(View.GONE);
			}
			
			if(base.orderList.get(position).hasPatientInfo==0){
				holder.bt_patient_see_case.setVisibility(View.GONE);
			}else{
				holder.bt_patient_see_case.setVisibility(View.VISIBLE);
			}
			
			//查看病例
			holder.bt_patient_see_case.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MyApplication.if_demand=false;
					Intent intent=new Intent();
					intent.setClass(getActivity(), SeeCaseDetailActivity.class);
					intent.putExtra("caseId", base.orderList.get(position).orderId);
					startActivity(intent);
				}
			});
			// 查看详情
			holder.bt_patient_details.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(getActivity(),
							AppointmentOrderDetailActivity.class);
					intent.putExtra("orderId",
							base.orderList.get(position).orderId + "");
					startActivity(intent);

				}
			});

			return convertView;
		}

	}

	// 我的需求
	class xuqiuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return xuqiubean.demandList.size();
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
			TextView  tv_doctor_order_time,
					tv_doctor_order_budgetprice, tv_doctor_order_state,
					tv_doctor_order_yuyue, tv_doctor_order_problem;
			Button bt_doctor_order_details, bt_doctor_order_refuse,
					bt_doctor_order_start,bt_doctor_order_see_case;
			CollapsibleTextView tv_doctor_order_content;
			ImageView iv_doctor_needs_zuo, iv_doctor_needs_hui,
					iv_doctor_needs_shu;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.doctor_needs_item, null);
				holder = new ViewHolder();
				holder.tv_doctor_order_time = (TextView) convertView
						.findViewById(R.id.tv_doctor_order_time);
				holder.tv_doctor_order_problem = (TextView) convertView
						.findViewById(R.id.tv_doctor_order_problem);
				holder.tv_doctor_order_state = (TextView) convertView
						.findViewById(R.id.tv_doctor_order_state);
				holder.bt_doctor_order_details = (Button) convertView
						.findViewById(R.id.bt_doctor_order_details);
				holder.bt_doctor_order_refuse = (Button) convertView
						.findViewById(R.id.bt_doctor_order_refuse);
				holder.bt_doctor_order_see_case = (Button) convertView
						.findViewById(R.id.bt_doctor_order_see_case);
				holder.bt_doctor_order_start = (Button) convertView
						.findViewById(R.id.bt_doctor_order_start);
				holder.tv_doctor_order_yuyue = (TextView) convertView
						.findViewById(R.id.tv_doctor_order_yuyue);
				holder.tv_doctor_order_content = (CollapsibleTextView) convertView
						.findViewById(R.id.tv_doctor_order_content);
				holder.tv_doctor_order_budgetprice = (TextView) convertView
						.findViewById(R.id.tv_doctor_order_budgetprice);
				holder.iv_doctor_needs_hui = (ImageView) convertView
						.findViewById(R.id.iv_doctor_needs_hui);
				holder.iv_doctor_needs_zuo = (ImageView) convertView
						.findViewById(R.id.iv_doctor_needs_zuo);
				holder.iv_doctor_needs_shu = (ImageView) convertView
						.findViewById(R.id.iv_doctor_needs_shu);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_doctor_order_time.setText(xuqiubean.demandList
					.get(position).createTimeString);
			holder.tv_doctor_order_problem.setText(xuqiubean.demandList
					.get(position).demandTitle);
			holder.tv_doctor_order_yuyue.setText(xuqiubean.demandList
					.get(position).demandTimeString);
			holder.tv_doctor_order_state.setText(xuqiubean.demandList
					.get(position).demandStatusStr);
			holder.tv_doctor_order_content.setDesc(
					xuqiubean.demandList.get(position).demandDesc,
					BufferType.NORMAL);
			holder.tv_doctor_order_budgetprice.setText(xuqiubean.demandList
					.get(position).demandPrice + "元");
			if (xuqiubean.demandList.get(position).demandStatus != 102
					&& xuqiubean.demandList.get(position).demandStatus != 6
					&& xuqiubean.demandList.get(position).demandStatus != 3&&
					xuqiubean.demandList.get(position).demandStatus != 4) {
				holder.bt_doctor_order_refuse.setVisibility(View.VISIBLE);
			} else {
				holder.bt_doctor_order_refuse.setVisibility(View.GONE);
			}

			if (xuqiubean.demandList.get(position).serviceTypeId == 1) {
				holder.iv_doctor_needs_zuo.setVisibility(View.VISIBLE);
			} else {
				holder.iv_doctor_needs_zuo.setVisibility(View.GONE);
			}
			if (xuqiubean.demandList.get(position).serviceTypeId == 2) {
				holder.iv_doctor_needs_hui.setVisibility(View.VISIBLE);
			} else {
				holder.iv_doctor_needs_hui.setVisibility(View.GONE);
			}

			if (xuqiubean.demandList.get(position).serviceTypeId == 3) {
				holder.iv_doctor_needs_shu.setVisibility(View.VISIBLE);
			} else {
				holder.iv_doctor_needs_shu.setVisibility(View.GONE);
			}

			if (xuqiubean.demandList.get(position).demandStatus == 2) {
				holder.bt_doctor_order_start.setVisibility(View.VISIBLE);
				holder.bt_doctor_order_start.setText("接诊");
			} else if (xuqiubean.demandList.get(position).demandStatus == 5) {
				holder.bt_doctor_order_start.setVisibility(View.VISIBLE);
				holder.bt_doctor_order_start.setText("就诊");
			} else if (xuqiubean.demandList.get(position).demandStatus == 6) {
				holder.bt_doctor_order_start.setVisibility(View.GONE);
			}

			if (xuqiubean.demandList.get(position).demandStatus != 2
					&& xuqiubean.demandList.get(position).demandStatus != 5) {
				holder.bt_doctor_order_start.setVisibility(View.GONE);
			}

			if(xuqiubean.demandList.get(position).hasPatientInfo==0){
				holder.bt_doctor_order_see_case.setVisibility(View.GONE);
			}else{
				holder.bt_doctor_order_see_case.setVisibility(View.VISIBLE);
			}
			
			holder.bt_doctor_order_see_case.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MyApplication.if_demand=true;
					Intent intent=new Intent();
					intent.setClass(getActivity(), SeeCaseDetailActivity.class);
					intent.putExtra("caseId", xuqiubean.demandList.get(position).demandId);
					startActivity(intent);
					
					
				}
			});
			holder.bt_doctor_order_start
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (xuqiubean.demandList.get(position).demandStatus == 2) {
								startProgressDialog();
								orderStatus = 5;
								orderId = xuqiubean.demandList.get(position).demandId;
								new jiezhenThread().start();
							} else if (xuqiubean.demandList.get(position).demandStatus == 5) {
								startProgressDialog();
								orderStatus = 6;
								orderId = xuqiubean.demandList.get(position).demandId;
								new jiuzhenThread().start();
							}

						}
					});

			// 查看详情
			holder.bt_doctor_order_details
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setClass(getActivity(),
									OrderDetailActivity.class);
							intent.putExtra("orderId",
									xuqiubean.demandList.get(position).demandId
											+ "");
							startActivity(intent);

						}
					});

			// 拒绝接诊
			holder.bt_doctor_order_refuse
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setClass(getActivity(), RefuseActivity.class);
							intent.putExtra("orderId",
									xuqiubean.demandList.get(position).demandId
											+ "");
							intent.putExtra("orderStatus", 4);
							startActivity(intent);

						}
					});
			return convertView;
		}

	}

	private void onLoad() {

		lv_seeking.stopRefresh();
		lv_seeking.stopLoadMore();
		lv_seeking.setRefreshTime("刚刚");
	}

	public void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(getActivity());

		}

		progressDialog.show();
	}

	public void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	private void initadapter() {
		bt_my_needs.setBackgroundResource(R.drawable.xuqiuliebiao_click);
		bt_my_reservation.setBackgroundResource(R.drawable.yuyueliebiao);
		if (base.orderTotalCount != 0) {
			yueAdapter = new onlineAdapter();
			lv_seeking.setAdapter(yueAdapter);
		} else {
			ll_common_nodata.setVisibility(View.VISIBLE);
			ll_comm_bg_loadingfail.setVisibility(View.GONE);
			ll_mycall_zhuti.setVisibility(View.GONE);
		}
	}

	private void initadapters() {
		bt_my_needs.setBackgroundResource(R.drawable.xuqiuliebiao);
		bt_my_reservation.setBackgroundResource(R.drawable.yuyueliebiao_click);
		if (xuqiubean.demandTotalCount != 0) {
			xuAdapter = new xuqiuAdapter();
			lv_seeking.setAdapter(xuAdapter);
		} else {
			ll_common_nodata.setVisibility(View.VISIBLE);
			ll_comm_bg_loadingfail.setVisibility(View.GONE);
			ll_mycall_zhuti.setVisibility(View.GONE);
		}
	}

	// @Override
	// public void onResume() {
	// super.onResume();
	// initdata();
	// }

	class jiezhenThread extends Thread {
		@Override
		public void run() {
			request = new ChangeOrderDetail();
			request.userId = ShareUtil.getAccountId(getActivity());
			request.demandStatus = orderStatus;
			request.demandId = orderId;
			Object data = NetRequestEngine.admissionDemand(request);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESSTWO;
			handler.sendMessage(message);
		}
	}

	class jiuzhenThread extends Thread {
		@Override
		public void run() {
			request = new ChangeOrderDetail();
			request.userId = ShareUtil.getAccountId(getActivity());
			request.demandStatus = orderStatus;
			request.demandId = orderId;
			Object data = NetRequestEngine.visDemand(request);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESSTWO;
			handler.sendMessage(message);
		}
	}

	class xuqiuThread extends Thread {
		@Override
		public void run() {
			loadmoreXuqiu = new LoadMoreXuBean();
			loadmoreXuqiu.userId = ShareUtil.getAccountId(getActivity());
			loadmoreXuqiu.currentPage=1;
			Object data = NetRequestEngine.getHospitalDemandLists(loadmoreXuqiu);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESSTHREE;
			handler.sendMessage(message);
		}

	}

	class yuyueThread extends Thread {
		@Override
		public void run() {
			loadmoreYueqiu = new LoadMoreYueBean();
			loadmoreYueqiu.userId = ShareUtil.getAccountId(getActivity());
			loadmoreYueqiu.currentPage=1;
			Object data = NetRequestEngine.getDoctorOrderList(loadmoreYueqiu);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}
	}

	class loadMorexuqiuThread extends Thread {
		@Override
		public void run() {
			loadmoreXuqiu = new LoadMoreXuBean();
			loadmoreXuqiu.userId = ShareUtil.getAccountId(getActivity());
			loadmoreXuqiu.currentPage = currentPageXu;
			Object data = NetRequestEngine.getHospitalDemandLists(loadmoreXuqiu);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESSFIVE;
			handler.sendMessage(message);
		}

	}

	class loadMoreyuyueThread extends Thread {
		@Override
		public void run() {
			loadmoreYueqiu = new LoadMoreYueBean();
			loadmoreYueqiu.userId = ShareUtil.getAccountId(getActivity());
			loadmoreYueqiu.currentPage = currentPageYue;
			Object data = NetRequestEngine
					.getDoctorOrderList(loadmoreYueqiu);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESSFOUR;
			handler.sendMessage(message);
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		if(type){
			initdata();
		}else{
			initdatas();
		}
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(!hidden){
			if(type){
				initdata();
			}else{
				initdatas();
			}
		}
	}
	
	private void loadXuInitData() {
		xuAdapter.notifyDataSetChanged();
		onLoad();
	}
	private void loadYuInitData() {
		yueAdapter.notifyDataSetChanged();
		onLoad();
	}
}
