package com.example.sjyy_expert_android.activity.Registered;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.activity.user.PerfectDoctorInfoActivity;
import com.example.sjyy_expert_android.entity.registered.RegisterResponse;
import com.example.sjyy_expert_android.entity.registered.SettingPasswordRequest;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.EncryptMD5;
import com.example.sjyy_expert_android.util.PreferenceConstans;
import com.example.sjyy_expert_android.util.ShareUtil;


/***
 * 类描述：填写新密码
 * @author 海洋
 */
public class SettingPasswordActivity extends BaseActivity {
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private final static int SUCCESS = 1;
	private RegisterResponse base;
	private SettingPasswordRequest bean;
	private EditText et_setpass_password;
	private Button btn_setpass_next;

	private String password;
	private String phone;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null && msg.obj instanceof RegisterResponse) {

					base = (RegisterResponse) msg.obj;
					if (base.resultCode.equals("1000")) {
						showToast("注册成功!");
						ShareUtil.putIntShare(SettingPasswordActivity.this,
								PreferenceConstans.SP_ACCOUNTID,
								Integer.parseInt(base.userId));
						ShareUtil.putStringShare(SettingPasswordActivity.this,
								PreferenceConstans.SP_USERNAME, phone);
						ShareUtil.putStringShare(SettingPasswordActivity.this,
								PreferenceConstans.SP_PASSWORD, password);
						application.regis=base;
						goNewActivity(PerfectDoctorInfoActivity.class);
						finish();
					} else {
						showToast(base.resultInfo);
					}
				} else {
					showToast(msg.obj + "");
				}

				break;

			default:
				break;
			}
			stopProgressDialog();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingpassword_activity);
		initview();
	}

	private void initview() {
		Intent intent = getIntent();
		phone = intent.getStringExtra("phone");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_right.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		tv_title_content.setText("注册");

		et_setpass_password = (EditText) findViewById(R.id.et_setpass_password);
		btn_setpass_next = (Button) findViewById(R.id.btn_setpass_next);
		btn_setpass_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		// 完成
		case R.id.btn_setpass_next:
			password = et_setpass_password.getText().toString().trim();
			if (password.equals("") && password.length() < 6) {
				showToast("请设置密码为6-20位字符");
			} else {
				startProgressDialog();
				new setPassWordThread().start();
			}
			break;

		default:
			break;
		}
	}

	class setPassWordThread extends Thread {
		@Override
		public void run() {
			bean = new SettingPasswordRequest();
			bean.password = EncryptMD5.MD5(password, "");
			bean.telephone = phone;
			bean.role="1";
			bean.areaId="1";
			bean.userId = ShareUtil.getAccountId(SettingPasswordActivity.this);
			Object data = NetRequestEngine.setpassword(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);

		}
	}
}
