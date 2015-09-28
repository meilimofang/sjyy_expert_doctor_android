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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjyy_expert_android.MyApplication;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.activity.LoginActivity;
import com.example.sjyy_expert_android.activity.about.AboutSjyyActivity;
import com.example.sjyy_expert_android.activity.about.BindingSalesActivity;
import com.example.sjyy_expert_android.activity.about.CallSalesActivity;
import com.example.sjyy_expert_android.activity.about.SeriousMissionActivity;
import com.example.sjyy_expert_android.activity.about.ServiceActivity;
import com.example.sjyy_expert_android.activity.about.SettingActivity;
import com.example.sjyy_expert_android.activity.user.PerfectDoctorInfoActivity;
import com.example.sjyy_expert_android.entity.choosebean.ChooseDepartmentBean;
import com.example.sjyy_expert_android.entity.choosebean.ChooseHospitalBean;
import com.example.sjyy_expert_android.entity.choosebean.ChooseOccupationBean;
import com.example.sjyy_expert_android.entity.choosebean.ChooseServiceProductsBean;
import com.example.sjyy_expert_android.entity.choosebean.ChooseTitle;
import com.example.sjyy_expert_android.entity.choosebean.MyInformationfragmentBean;
import com.example.sjyy_expert_android.entity.login.LoginRequest;
import com.example.sjyy_expert_android.entity.registered.RegisterResponse;
import com.example.sjyy_expert_android.entity.user.UserInfo;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.CircularImage;
import com.example.sjyy_expert_android.util.CustomProgressDialog;
import com.example.sjyy_expert_android.util.PreferenceConstans;
import com.example.sjyy_expert_android.util.ShareUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 类描述：个人信息
 * 
 * @author 海洋
 */
public class MyInformationfragment extends Fragment implements OnClickListener {

	private final static int SUCCESS = 1;
	private CustomProgressDialog progressDialog = null;
	private LoginRequest bean;
	private UserInfo base;

	private CircularImage iv_user_icon;
	private TextView tv_user_name;
	// private Button bt_my_yuyue, bt_my_demand;
	private LinearLayout ll_userinfo, ll_user_setting, ll_user_service,
			ll_user_about, ll_user_service_phone, ll_serious_mission;

	protected ImageLoader imageLoader;
	private DisplayImageOptions options;
	private LinearLayout ll_comm_bg_loadingfail, ll_myinfo_zhuti;
	private ImageView iv_comm_bg_loadingfail;
	private TextView tv_comm_bg_loadingfail;
	private ImageView iv_mine_renzheng;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				ll_comm_bg_loadingfail.setVisibility(View.GONE);
				ll_myinfo_zhuti.setVisibility(View.VISIBLE);
				if (msg.obj != null && msg.obj instanceof UserInfo) {

					base = (UserInfo) msg.obj;
					if (base.resultCode.equals("1000")) {
						showview();

					} else if (base.resultCode.equals("998")) {
						MyInformationfragmentBean.ifIntent = false;
						MyApplication.regis = (RegisterResponse) msg.obj;
						Intent intent = new Intent();
						intent.setClass(getActivity(),
								PerfectDoctorInfoActivity.class);
						startActivity(intent);
						Toast.makeText(getActivity(), base.resultInfo, 0)
								.show();
					} else {
						ll_myinfo_zhuti.setVisibility(View.GONE);
						ll_comm_bg_loadingfail.setVisibility(View.VISIBLE);
						tv_comm_bg_loadingfail.setText("加载失败，点击重新加载");
					}
				} else {
					ll_myinfo_zhuti.setVisibility(View.GONE);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mine_activity, container, false);
		initview();
		iv_user_icon = (CircularImage) view.findViewById(R.id.iv_user_icon);
		tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
		// bt_my_yuyue = (Button) view.findViewById(R.id.bt_my_yuyue);
		// bt_my_demand = (Button) view.findViewById(R.id.bt_my_demand);
		// bt_my_yuyue.setText("我的预约");
		// bt_my_demand.setText("我的需求");
		ll_userinfo = (LinearLayout) view.findViewById(R.id.ll_userinfo);
		ll_user_setting = (LinearLayout) view
				.findViewById(R.id.ll_user_setting);
		ll_user_service = (LinearLayout) view
				.findViewById(R.id.ll_user_service);
		ll_user_service_phone = (LinearLayout) view
				.findViewById(R.id.ll_user_service_phone);
		ll_user_about = (LinearLayout) view.findViewById(R.id.ll_user_about);
		ll_serious_mission = (LinearLayout) view
				.findViewById(R.id.ll_serious_mission);
		// bt_my_yuyue.setOnClickListener(this);
		// bt_my_demand.setOnClickListener(this);
		ll_userinfo.setOnClickListener(this);
		ll_user_service.setOnClickListener(this);
		ll_user_about.setOnClickListener(this);
		ll_user_service_phone.setOnClickListener(this);
		ll_user_setting.setOnClickListener(this);
		ll_serious_mission.setOnClickListener(this);
		ll_comm_bg_loadingfail = (LinearLayout) view
				.findViewById(R.id.ll_comm_bg_loadingfail);
		ll_comm_bg_loadingfail.setVisibility(View.INVISIBLE);
		iv_comm_bg_loadingfail = (ImageView) view
				.findViewById(R.id.iv_comm_bg_loadingfail);
		tv_comm_bg_loadingfail = (TextView) view
				.findViewById(R.id.tv_comm_bg_loadingfail);
		iv_comm_bg_loadingfail.setOnClickListener(this);
		ll_myinfo_zhuti = (LinearLayout) view
				.findViewById(R.id.ll_myinfo_zhuti);
		iv_mine_renzheng = (ImageView) view.findViewById(R.id.iv_mine_renzheng);

		initdata();
		return view;
	}

	private void initdata() {
		ll_comm_bg_loadingfail.setVisibility(View.GONE);
		startProgressDialog();
		new dataThread().start();
	}

	private void showview() {
		imageLoader.displayImage(base.iconUrl, iv_user_icon, options);
		tv_user_name.setText(base.name);
		ShareUtil.putIntShare(getActivity(), PreferenceConstans.SP_ROLE,
				base.role);
		if (base.certification == 1) {
			iv_mine_renzheng.setBackgroundResource(R.drawable.yirenzheng);
		} else if (base.certification == 0) {
			iv_mine_renzheng.setBackgroundResource(R.drawable.weirenzheng);
		}
	}

	private void initview() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.img_no_net_pic)
				.showImageForEmptyUri(R.drawable.img_no_net_pic)
				.cacheInMemory(true).cacheOnDisc(true).build();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// // 我的预约
		// case R.id.bt_my_yuyue:
		// String password = ShareUtil.getPasswordContent(getActivity());
		// if (!password.equals("")) {
		// //医生
		// if(ShareUtil.getRole(getActivity()) == 1){
		// Intent intent = new Intent();
		// intent.setClass(getActivity(), DoctorMyReservationActivity.class);
		// startActivity(intent);
		// //医院
		// }else if(ShareUtil.getRole(getActivity()) == 2){
		// Intent intent = new Intent();
		// intent.setClass(getActivity(), MyAppointmentActivity.class);
		// startActivity(intent);
		// }
		// } else {
		// Intent intent = new Intent();
		// intent.setClass(getActivity(), LoginActivity.class);
		// startActivity(intent);
		// }
		//
		// break;
		// // 我的需求
		// case R.id.bt_my_demand:
		// String password2 = ShareUtil.getPasswordContent(getActivity());
		// if (!password2.equals("")) {
		// if (ShareUtil.getRole(getActivity()) == 1) {
		// Intent intent = new Intent();
		// intent.setClass(getActivity(), MyCallActivity.class);
		// startActivity(intent);
		// } else if (ShareUtil.getRole(getActivity()) == 2) {
		// Intent intent = new Intent();
		// intent.setClass(getActivity(), DoctorNeedsActivity.class);
		// startActivity(intent);
		// }
		//
		// } else {
		// Intent intent = new Intent();
		// intent.setClass(getActivity(), LoginActivity.class);
		// startActivity(intent);
		// }
		//
		// break;
		// 个人信息
		case R.id.ll_userinfo:
			String passwordtwo = ShareUtil.getPasswordContent(getActivity());
			if (!passwordtwo.equals("")) {
				ChooseHospitalBean.ifIntent = false;
				ChooseOccupationBean.ifIntent = false;
				ChooseDepartmentBean.ifIntent = false;
				ChooseTitle.ifIntent = false;
				ChooseServiceProductsBean.ifIntent = false;
				MyInformationfragmentBean.ifIntent = true;
				Intent intent2 = new Intent();
				intent2.setClass(getActivity(), PerfectDoctorInfoActivity.class);
				startActivity(intent2);
			} else {
				Intent intent4 = new Intent();
				intent4.setClass(getActivity(), LoginActivity.class);
				startActivity(intent4);
			}

			break;
		// 失败
		case R.id.iv_comm_bg_loadingfail:
			initdata();
			break;
		// 专属客服
		case R.id.ll_user_service:
			if (base.isHasEmployee == 0) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), BindingSalesActivity.class);
				startActivity(intent);
			} else if (base.isHasEmployee == 1) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), CallSalesActivity.class);
				intent.putExtra("employeeName", base.employeeName);
				intent.putExtra("employeeTel", base.employeeTel);
				startActivity(intent);

			}

			break;
		case R.id.ll_user_about:
			Intent intent3 = new Intent();
			intent3.setClass(getActivity(), AboutSjyyActivity.class);
			startActivity(intent3);
			break;

		case R.id.ll_user_setting:
			Intent intent4 = new Intent();
			intent4.setClass(getActivity(), SettingActivity.class);
			startActivity(intent4);
			break;

		// 失败
		case R.id.ll_user_service_phone:
			Intent intent5 = new Intent();
			intent5.setClass(getActivity(), ServiceActivity.class);
			startActivity(intent5);
			break;
		// 失败
		case R.id.ll_serious_mission:
			Intent intent9 = new Intent();
			intent9.setClass(getActivity(), SeriousMissionActivity.class);
			startActivity(intent9);
			break;
		default:
			break;
		}
	}

	class dataThread extends Thread {

		@Override
		public void run() {
			bean = new LoginRequest();
			bean.userId = ShareUtil.getAccountId(getActivity());
			Object data = NetRequestEngine.getPersonInfo(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
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

	@Override
	public void onResume() {
		super.onResume();
		initdata();
	}
}
