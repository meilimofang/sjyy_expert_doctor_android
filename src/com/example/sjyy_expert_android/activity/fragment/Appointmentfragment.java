package com.example.sjyy_expert_android.activity.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SlidingPaneLayout.LayoutParams;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.activity.LoginActivity;
import com.example.sjyy_expert_android.activity.doctor.GoToAppointmentActivity;
import com.example.sjyy_expert_android.entity.Common;
import com.example.sjyy_expert_android.entity.appointment.AppointmentRequest;
import com.example.sjyy_expert_android.entity.appointment.AppointmentRespons;
import com.example.sjyy_expert_android.entity.appointment.Doctor;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.CircularImage;
import com.example.sjyy_expert_android.util.CollapsibleTextView;
import com.example.sjyy_expert_android.util.CustomProgressDialog;
import com.example.sjyy_expert_android.util.ShareUtil;
import com.example.sjyy_expert_android.util.XListView;
import com.example.sjyy_expert_android.util.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 类描述：在线预约列表
 * 
 * @author 海洋
 */
public class Appointmentfragment extends Fragment implements OnClickListener,IXListViewListener {
	private CustomProgressDialog progressDialog = null;
	private LinearLayout ll_chose_area, ll_chose_department;
	private TextView tv_chose_area, tv_chose_department;
	private XListView lv_online_booking;
	private PopupWindow areapopupWindow;
	private Context context;
	private View areaview;
	private ListView lv_area;
	private AreaAdapter areaAdapter;
	private PersonAdapter personAdapter;
	private ListView lv_person;
	private Boolean shenlong = true;
	private Boolean xuanzequyu = true;
	private int itemid = -1;
	private int itemids = -1;
	public String serviceTypeId;
	private int departmentId;// 第二个筛选条件ID
	private final static int SUCCESS = 1;
	private final static int SUCCESSTWO = 2;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options;
	private AppointmentRequest apprequest;
	private AppointmentRespons apprespons;
	private Integer currentPage;
	private onlineAdapter onlineadapter;
	private boolean LOADMORE = true;
	private AppointmentRespons ceshi;
	private LinearLayout ll_comm_bg_loadingfail, ll_app_zhuti,ll_common_nodata,ll_zhutier;
	private ImageView iv_comm_bg_loadingfail;
	private TextView tv_comm_bg_loadingfail;
	private String serviceProjectId;
	private AppointmentRespons loadMore;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				ll_comm_bg_loadingfail.setVisibility(View.GONE);
				ll_app_zhuti.setVisibility(View.VISIBLE);
				ll_common_nodata.setVisibility(View.GONE);
				ll_zhutier.setVisibility(View.VISIBLE);
				if (msg.obj != null && msg.obj instanceof AppointmentRespons) {

					ceshi = (AppointmentRespons) msg.obj;
					if (ceshi.resultCode.equals("1000")) {
//						test(ceshi.doctordatas);
						initdatas();
						// goNewActivity(MainActivity.class);
					} else {

						ll_common_nodata.setVisibility(View.GONE);
						ll_zhutier.setVisibility(View.GONE);
						ll_app_zhuti.setVisibility(View.GONE);
						ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
						tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
					}
				} else {
					ll_common_nodata.setVisibility(View.GONE);
					ll_zhutier.setVisibility(View.GONE);
					ll_app_zhuti.setVisibility(View.GONE);
					ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
					tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
				}
				break;
			case SUCCESSTWO:
				if (msg.obj != null && msg.obj instanceof AppointmentRespons) {
					
					loadMore=(AppointmentRespons) msg.obj;
					if(loadMore.totalCount==apprespons.doctordatas.size()){
						Toast.makeText(getActivity(),"无更多数据！", 0).show();
					}else{
						if (loadMore.resultCode.equals("1000")) {
							for(Doctor doc:loadMore.doctordatas){
								ceshi.doctordatas.add(doc);
							}
							loadinitdatas();
						} else {
							Toast.makeText(getActivity(), msg.obj+"", 0).show();
						}
					}
					
				} else {
					Toast.makeText(getActivity(), msg.obj+"", 0).show();
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
		View view = inflater.inflate(R.layout.appointment_fragment, container,
				false);
		currentPage = 1;
		Common.AREA_SELECTED = -1;
		Common.DETAIL_SELECTED = -1;
		Common.CATEGORY_SELECTED = -1;
		context = getActivity();
		ll_chose_area = (LinearLayout) view.findViewById(R.id.ll_chose_area);
		ll_chose_department = (LinearLayout) view
				.findViewById(R.id.ll_chose_department);
		tv_chose_area = (TextView) view.findViewById(R.id.tv_chose_area);
		tv_chose_department = (TextView) view
				.findViewById(R.id.tv_chose_department);
		lv_online_booking = (XListView) view
				.findViewById(R.id.lv_online_booking);
		lv_online_booking.setDivider(null);
		lv_online_booking.setPullLoadEnable(true);
		lv_online_booking.setXListViewListener(this);
		lv_online_booking.setPullRefreshEnable(true);
		ll_chose_area.setOnClickListener(this);
		ll_chose_department.setOnClickListener(this);

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.img_no_net_pic)
				.showImageForEmptyUri(R.drawable.img_no_net_pic)
				.cacheInMemory(true).cacheOnDisc(true).build();
		ll_common_nodata=(LinearLayout) view.findViewById(R.id.ll_common_nodata);
		
		ll_comm_bg_loadingfail = (LinearLayout) view
				.findViewById(R.id.ll_comm_bg_loadingfail);
		ll_zhutier=(LinearLayout) view.findViewById(R.id.ll_zhutier);
		ll_comm_bg_loadingfail.setVisibility(View.INVISIBLE);
		iv_comm_bg_loadingfail = (ImageView) view
				.findViewById(R.id.iv_comm_bg_loadingfail);
		tv_comm_bg_loadingfail = (TextView) view
				.findViewById(R.id.tv_comm_bg_loadingfail);
		ll_common_nodata=(LinearLayout) view.findViewById(R.id.ll_common_nodata);
		iv_comm_bg_loadingfail.setOnClickListener(this);
		ll_comm_bg_loadingfail.setVisibility(View.GONE);
		ll_common_nodata.setVisibility(View.GONE);
		ll_zhutier.setVisibility(View.VISIBLE);
		ll_app_zhuti = (LinearLayout) view.findViewById(R.id.ll_app_zhuti);
		startProgressDialog();
		new onlineThread().start();
		return view;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ll_chose_area:
			showAreaWindow(v);
			break;

		case R.id.ll_chose_department:
			showAreaWindow(v);
			break;

		case R.id.iv_comm_bg_loadingfail:
			startProgressDialog();
			new onlineThread().start();
			break;
		default:
			break;
		}

	}

	// 区域的弹窗
	private void showAreaWindow(View parent) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		if (areapopupWindow == null || xuanzequyu) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			areaview = layoutInflater.inflate(R.layout.area_list, null);

			lv_area = (ListView) areaview.findViewById(R.id.lv_area);
			// 加载数据
			lv_area.setDivider(null);
			areaAdapter = new AreaAdapter(context);
			lv_area.setAdapter(areaAdapter);
			lv_person = (ListView) areaview.findViewById(R.id.lv_person);
			// 加载数据
			lv_person.setDivider(null);
			if (shenlong) {
				lv_person.setAdapter(null);
			} else {
				lv_person.setAdapter(personAdapter);
			}

			// 创建一个PopuWidow对象

			areapopupWindow = new PopupWindow(areaview, windowManager
					.getDefaultDisplay().getWidth(), LayoutParams.WRAP_CONTENT);

		}

		areapopupWindow.showAsDropDown(parent, 0, 0);
		areapopupWindow.setFocusable(true);
		areapopupWindow.update();

		areapopupWindow.getContentView().setOnTouchListener(
				new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						areapopupWindow.setFocusable(false);
						areapopupWindow.dismiss();
						// ll_fufutwo.setVisibility(View.GONE);
						return true;
					}
				});

		// 设置允许在外点击消失
		areapopupWindow.setOutsideTouchable(true);

		areapopupWindow.setBackgroundDrawable(new BitmapDrawable());

		areapopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

				// ll_fufutwo.setVisibility(View.GONE);

			}
		});

		lv_area.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				itemid = position;
				Common.CATEGORY_SELECTED = position;
				Common.AREA_SELECTED = 0;
				Common.DETAIL_SELECTED = 0;
				itemids = 0;
//				cityId = apprespons.apponitmentdatas.get(position).departmentId;
				tv_chose_area.setText(apprespons.apponitmentdatas.get(position).department);
				personAdapter = new PersonAdapter(context);
				lv_person.setAdapter(personAdapter);
				// orderadapter.notifyDataSetChanged();
				areaAdapter.notifyDataSetChanged();
				personAdapter.notifyDataSetChanged();
				shenlong = false;
			}

		});

		lv_person.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Boolean erbao = true;
				if (areapopupWindow != null) {
					areapopupWindow.dismiss();
					// ll_fufu.setVisibility(View.GONE);
					// ll_fufutwo.setVisibility(View.GONE);
				}
				itemids = 0;
				itemids = position;
				Common.AREA_SELECTED = position;
				Common.DETAIL_SELECTED = -1;
				departmentId = apprespons.apponitmentdatas
						.get(Common.CATEGORY_SELECTED).departmentList
						.get(position).departmentId;
				currentPage=1;
				// tv_chose_department.setText(apprespons.appointmentdatas
				// .get(Common.CATEGORY_SELECTED).datas.get(position).serviceProjectDesc);
				startProgressDialog();
				new onlineThread().start();
				areaAdapter.notifyDataSetChanged();
				personAdapter.notifyDataSetChanged();
				areapopupWindow.dismiss();
				LOADMORE = false;
			}
		});

	}

	// 区域的adapter
	private class AreaAdapter extends BaseAdapter {

		private Context context;

		public AreaAdapter(Context context) {

			this.context = context;

		}

		@Override
		public int getCount() {
			return apprespons.apponitmentdatas.size();
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
			TextView tv_area;
			LinearLayout ll_area;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.area_list_item, null);
				holder = new ViewHolder();

				convertView.setTag(holder);

				holder.tv_area = (TextView) convertView
						.findViewById(R.id.tv_area);
				holder.ll_area = (LinearLayout) convertView
						.findViewById(R.id.ll_area);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_area
					.setText(apprespons.apponitmentdatas.get(position).department);
			if (Common.CATEGORY_SELECTED == position) {

				holder.ll_area.setBackgroundColor(Color.parseColor("#ffffff"));
			} else {

				holder.ll_area.setBackgroundColor(Color.parseColor("#f7f7f7"));
			}

			return convertView;
		}
	}

	// 客户的adapter
	private class PersonAdapter extends BaseAdapter {

		private Context context;

		public PersonAdapter(Context context) {

			this.context = context;

		}

		@Override
		public int getCount() {
			return apprespons.apponitmentdatas.get(itemid).departmentList.size();
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
			TextView tv_person;
			LinearLayout ll_person;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.person_list_item, null);
				holder = new ViewHolder();

				convertView.setTag(holder);

				holder.tv_person = (TextView) convertView
						.findViewById(R.id.tv_person);
				holder.ll_person = (LinearLayout) convertView
						.findViewById(R.id.ll_person);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_person
					.setText(apprespons.apponitmentdatas.get(itemid).departmentList
							.get(position).department);
			return convertView;
		}
	}

	class onlineAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return apprespons.doctordatas.size();
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
			TextView tv_online_name, tv_online_appointment, tv_online_address,tv_online_dep;
			CollapsibleTextView tv_online_professionalField;
			Button  bt_online_details;
			CircularImage iv_online_head;
			ImageView iv_online_zuo, iv_online_hui, iv_online_shu;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.online_booking_item, null);
				holder = new ViewHolder();

				convertView.setTag(holder);

				holder.tv_online_name = (TextView) convertView
						.findViewById(R.id.tv_online_name);
				holder.tv_online_appointment = (TextView) convertView
						.findViewById(R.id.tv_online_appointment);
				holder.tv_online_address = (TextView) convertView
						.findViewById(R.id.tv_online_address);
				holder.tv_online_dep=(TextView) convertView.findViewById(R.id.tv_online_dep);
				holder.tv_online_professionalField = (CollapsibleTextView) convertView
						.findViewById(R.id.tv_online_professionalField);
				holder.bt_online_details = (Button) convertView
						.findViewById(R.id.bt_online_details);
				holder.iv_online_head = (CircularImage) convertView
						.findViewById(R.id.iv_online_head);
				holder.iv_online_zuo = (ImageView) convertView
						.findViewById(R.id.iv_online_zuo);
				holder.iv_online_hui = (ImageView) convertView
						.findViewById(R.id.iv_online_hui);
				holder.iv_online_shu = (ImageView) convertView
						.findViewById(R.id.iv_online_shu);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_online_name
					.setText(apprespons.doctordatas.get(position).doctorName);
			holder.tv_online_appointment.setText(apprespons.doctordatas
					.get(position).qualification);
			holder.tv_online_address.setText(apprespons.doctordatas
					.get(position).hospitalName);
			holder.tv_online_professionalField.setDesc(
					apprespons.doctordatas.get(position).professionalField,
					BufferType.NORMAL);
			holder.tv_online_dep.setText(apprespons.doctordatas.get(position).departmentName);
			imageLoader.displayImage(
					apprespons.doctordatas.get(position).iconUrl,
					holder.iv_online_head, options);

			// for(Integer i
			// :apprespons.doctordatas.get(position).serviceTypeIdList){
			// if(i==1){
			// holder.iv_online_zuo.setVisibility(View.VISIBLE);
			// }else if(i==2){
			// holder.iv_online_hui.setVisibility(View.VISIBLE);
			// }else if(i==3){
			// holder.iv_online_shu.setVisibility(View.VISIBLE);
			// }
			// }

//			if (apprespons.doctordatas.get(position).h) {
//				holder.iv_online_hui.setVisibility(View.VISIBLE);
//			} else {
//				holder.iv_online_hui.setVisibility(View.GONE);
//			}
//			if (apprespons.doctordatas.get(position).z) {
//				holder.iv_online_zuo.setVisibility(View.VISIBLE);
//			} else {
//				holder.iv_online_zuo.setVisibility(View.GONE);
//			}
//			if (apprespons.doctordatas.get(position).s) {
//				holder.iv_online_shu.setVisibility(View.VISIBLE);
//			} else {
//				holder.iv_online_shu.setVisibility(View.GONE);
//			}


			holder.bt_online_details.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String password = ShareUtil
							.getPasswordContent(getActivity());
					if (!password.equals("")) {
						Intent intent = new Intent();
						intent.setClass(context, GoToAppointmentActivity.class);
						intent.putExtra("doctorId",
								apprespons.doctordatas.get(position).doctorId);
						intent.putExtra("serverAccountId",
								apprespons.doctordatas.get(position).accountId);
						intent.putExtra("departmentId", departmentId);
						startActivity(intent);
					} else {
						Intent intent = new Intent();
						intent.setClass(context, LoginActivity.class);
						startActivity(intent);
					}
				}
			});
			return convertView;
		}

	}

	class onlineThread extends Thread {
		@Override
		public void run() {
			apprequest = new AppointmentRequest();
			apprequest.userId = ShareUtil.getAccountId(context);
			apprequest.departmentId = departmentId;
			Object data = NetRequestEngine.getonline(apprequest);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}
	}

	private void initdatas() {
		apprespons = ceshi;
		if (apprespons.totalCount == 0) {
			lv_online_booking.setAdapter(null);
			ll_comm_bg_loadingfail.setVisibility(View.GONE);
			ll_common_nodata.setVisibility(View.VISIBLE);
			ll_zhutier.setVisibility(View.GONE);
		} else {
			if (LOADMORE) {
				onlineadapter = new onlineAdapter();
				lv_online_booking.setAdapter(onlineadapter);
			} else {
				lv_online_booking.setAdapter(onlineadapter);
				onlineadapter.notifyDataSetChanged();
			}
		}

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

//	@Override
//	public void onResume() {
//		super.onResume();
//		startProgressDialog();
//		new onlineThread().start();
//	}

//	public static void test(List<Doctor> list) {
//		if (list != null && list.size() > 0) {
//			for (Doctor base : list) {
//				List<Integer> serviceTypeIdList = base.serviceTypeIdList;
//				if (serviceTypeIdList != null && serviceTypeIdList.size() > 0) {
//					base.h = serviceTypeIdList.contains(2);
//					base.z = serviceTypeIdList.contains(1);
//					base.s = serviceTypeIdList.contains(3);
//				}
//			}
//		}
//	}

	@Override
	public void onRefresh() {
		startProgressDialog();
		new onlineThread().start();
		LOADMORE=false;
		
	}

	@Override
	public void onLoadMore() {
		startProgressDialog();
		currentPage++;
		new loadMoreThread().start();
	}
	private void onLoad() {

		lv_online_booking.stopRefresh();
		lv_online_booking.stopLoadMore();
		lv_online_booking.setRefreshTime("刚刚");
	}
	
	
	class loadMoreThread extends Thread{
		@Override
		public void run() {
			super.run();
			apprequest = new AppointmentRequest();
			apprequest.userId = ShareUtil.getAccountId(context);
			apprequest.departmentId = departmentId;
			apprequest.currentPage=currentPage;
			Object data = NetRequestEngine.getonline(apprequest);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESSTWO;
			handler.sendMessage(message);
			
			
		}
	}
	private void loadinitdatas() {
		apprespons = ceshi;
		onlineadapter.notifyDataSetChanged();
		onLoad();
	}
}
