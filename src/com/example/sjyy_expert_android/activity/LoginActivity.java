package com.example.sjyy_expert_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.MainActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.activity.Registered.RegisteredActivity;
import com.example.sjyy_expert_android.entity.login.LoginFinshBean;
import com.example.sjyy_expert_android.entity.login.LoginRequest;
import com.example.sjyy_expert_android.entity.login.LoginRespons;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.EncryptMD5;
import com.example.sjyy_expert_android.util.PreferenceConstans;
import com.example.sjyy_expert_android.util.ShareUtil;

/***
 * 类描述：登录
 * 
 * @author 海洋
 */
public class LoginActivity extends BaseActivity {

	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private EditText et_login_name, et_login_password;
	private Button btn_login;
	private TextView tv_forget;
	private String password, name;
	private final static int SUCCESS = 1;
	private LoginRequest bean;
	private LoginRespons base;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null && msg.obj instanceof LoginRespons) {
					base = (LoginRespons) msg.obj;
					if (base.resultCode.equals("1000")) {
						ShareUtil.putIntShare(LoginActivity.this,
								PreferenceConstans.SP_ACCOUNTID,
								Integer.parseInt(base.userId));
						ShareUtil.putStringShare(LoginActivity.this,
								PreferenceConstans.SP_USERNAME, name);
						ShareUtil.putStringShare(LoginActivity.this,
								PreferenceConstans.SP_PASSWORD, password);
						ShareUtil.putIntShare(LoginActivity.this,
								PreferenceConstans.SP_IFUSERINFO,
								base.completeStatus);
						goNewActivity(MainActivity.class);
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		initdata();
		initview();

	}

	private void initdata() {

		String username = ShareUtil.getUserNameContent(this);
		String password = ShareUtil.getPasswordContent(this);
		if (!username.equals("") && !password.equals("")) {
			goNewActivity(MainActivity.class);
			finish();
		}

	}

	private void initview() {

		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_right.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_right.setText("注册");
		tv_title_content.setText("私家医院");
		et_login_name = (EditText) findViewById(R.id.et_login_name);
		et_login_password = (EditText) findViewById(R.id.et_login_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		tv_forget = (TextView) findViewById(R.id.tv_forget);
		tv_forget.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		if (!ShareUtil.getUserNameContent(this).equals("")) {
			et_login_name.setText(ShareUtil.getUserNameContent(this));
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 注册
		case R.id.btn_title_right:
			goNewActivity(RegisteredActivity.class);
			finish();
			break;
		// 登陆
		case R.id.btn_login:
			gologin();
			break;
		// 忘记密码
		case R.id.tv_forget:
			// goNewActivity(ForgetPasswordActivity.class);

			break;

		default:
			break;
		}
	}

	private void gologin() {
		name = et_login_name.getText().toString().trim();
		password = et_login_password.getText().toString().trim();
		if (name.equals("")) {
			showToast("用户名不能为空");

		} else if (password.equals("")) {
			showToast("密码不能为空");
		} else {
			startProgressDialog();
			new dataThread().start();
		}
	}

	class dataThread extends Thread {

		@Override
		public void run() {
			bean = new LoginRequest();
			bean.username = name;
			bean.password = EncryptMD5.MD5(password, "");
			Object data = NetRequestEngine.login(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(LoginFinshBean.if_finsh){
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
}
