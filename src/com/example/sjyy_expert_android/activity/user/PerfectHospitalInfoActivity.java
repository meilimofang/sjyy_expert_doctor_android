package com.example.sjyy_expert_android.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.MainActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.BaseRequest;
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.choosebean.ChooseHospitalBean;
import com.example.sjyy_expert_android.entity.choosebean.MyInformationfragmentBean;
import com.example.sjyy_expert_android.entity.hospatil.Hospatil;
import com.example.sjyy_expert_android.entity.user.PerfectHospitalInfoBean;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.ShareUtil;

public class PerfectHospitalInfoActivity extends BaseActivity {
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;

	private final static int SUCCESS = 1;
	private PerfectHospitalInfoBean bean;
	private BaseRespons base;
	private BaseRequest beanrequest;
	private Hospatil baserespons;
	private TextView tv_perfect_hos_area;
	private EditText et_perfect_hos_name, et_perfect_hos_person,
			et_perfect_hos_address, et_perfect_hos_phone,
			et_perfect_hos_summary, et_perfect_hos_number;
	private String name;
	private String tel;
	private String address;
	private String area;
	private LinearLayout ll_perfect_hos_area;
	private final static int SUCCESSTWO = 2;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null && msg.obj instanceof BaseRespons) {
					base = (BaseRespons) msg.obj;
					if (base.resultCode.equals("1000")) {
						showToast("信息完善成功！");
						goNewActivity(MainActivity.class);
						finish();
					} else {
						showToast(base.resultInfo);
					}
				} else {
					showToast(msg.obj + "");
				}

				break;

			case SUCCESSTWO:

				if (msg.obj != null && msg.obj instanceof Hospatil) {

					baserespons = (Hospatil) msg.obj;
					if (baserespons.resultCode.equals("1000")) {

						application.regis = baserespons;
						showview();
					} else {
						showToast(baserespons.resultInfo);
					}
				} else {
					showToast(msg.obj + "");
				}

				MyInformationfragmentBean.ifIntent = false;
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
		setContentView(R.layout.perfect_info_activity);
		initview();
	}

	private void initview() {
		tv_perfect_hos_area = (TextView) findViewById(R.id.tv_perfect_hos_area);
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_right.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("保存");
		tv_title_content.setText("个人信息");
		ll_perfect_hos_area = (LinearLayout) findViewById(R.id.ll_perfect_hos_area);
		et_perfect_hos_name = (EditText) findViewById(R.id.et_perfect_hos_name);
		et_perfect_hos_person = (EditText) findViewById(R.id.et_perfect_hos_person);
		et_perfect_hos_address = (EditText) findViewById(R.id.et_perfect_hos_address);
		et_perfect_hos_phone = (EditText) findViewById(R.id.et_perfect_hos_phone);
		et_perfect_hos_summary = (EditText) findViewById(R.id.et_perfect_hos_summary);
		et_perfect_hos_number = (EditText) findViewById(R.id.et_perfect_hos_number);
		ll_perfect_hos_area.setOnClickListener(this);
		if (MyInformationfragmentBean.ifIntent) {
			startProgressDialog();
			new initdataThread().start();
		}
	}

	private String hos_name, hos_person, hos_address, hos_phone, hos_summary,
			hos_number;

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.ll_perfect_hos_area:
			goNewActivity(HospitalChooseAreaActivity.class);
			break;
		case R.id.btn_title_right:
			area = tv_perfect_hos_area.getText().toString();
			hos_name = et_perfect_hos_name.getText().toString();
			hos_person = et_perfect_hos_person.getText().toString();
			hos_address = et_perfect_hos_address.getText().toString();
			hos_phone = et_perfect_hos_phone.getText().toString();
			hos_summary = et_perfect_hos_summary.getText().toString();
			hos_number = et_perfect_hos_number.getText().toString();
			if (!area.equals("区域")) {
				if (!hos_name.equals("")) {
					if (!hos_person.equals("")) {

						if (!hos_address.equals("")) {

							if (!hos_phone.equals("")) {
								startProgressDialog();
								new dataThread().start();
							} else {
								showToast("请输入移动电话");
							}
						} else {
							showToast("请输入地址");
						}
					} else {
						showToast("请输入联系人");
					}

				} else {
					showToast("请输入医院名字");
				}
			} else {
				showToast("请选择区域");
			}
			break;

		default:
			break;
		}
	}

	class dataThread extends Thread {

		@Override
		public void run() {
			bean = new PerfectHospitalInfoBean();
			bean.areaId = ChooseHospitalBean.secondId;
			bean.address = hos_address;
			bean.contactPerson = hos_person;
			bean.hospitalName = hos_name;
			bean.mobileTel = hos_phone;
			bean.landlineTel = hos_number;
			bean.summary = hos_summary;
			bean.userId = ShareUtil
					.getAccountId(PerfectHospitalInfoActivity.this);
			Object data = NetRequestEngine.completeHospitalAccountInfo(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}
	}

	class initdataThread extends Thread {
		@Override
		public void run() {
			super.run();
			beanrequest = new BaseRequest();
			beanrequest.userId = ShareUtil
					.getAccountId(PerfectHospitalInfoActivity.this);
			Object data = NetRequestEngine.getHospatilPersonInfo(beanrequest);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESSTWO;
			handler.sendMessage(message);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (ChooseHospitalBean.ifTwoIntent) {
			tv_perfect_hos_area.setText(ChooseHospitalBean.secondName);
		}
	}

	private void showview() {
		tv_perfect_hos_area.setText(baserespons.areaString);
		et_perfect_hos_name.setText(baserespons.hospitalName);
		et_perfect_hos_person.setText(baserespons.contactPerson);
		et_perfect_hos_address.setText(baserespons.address);
		et_perfect_hos_phone.setText(baserespons.mobileTel);
		et_perfect_hos_summary.setText(baserespons.summary);
		et_perfect_hos_number.setText(baserespons.landlineTel);
	}

}
