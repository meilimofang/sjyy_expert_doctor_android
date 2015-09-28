package com.example.sjyy_expert_android.activity.Registered;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.registered.CheckPhoneRequest;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.ShareUtil;



/***
 * 类描述：注册
 * 
 * @author 海洋
 */
public class RegisteredActivity extends BaseActivity {
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;

	private Button btn_registered_next;
	private TextView et_registered_phone;
	private String phone;

	private CheckPhoneRequest bean;
	private BaseRespons base;
	private final static int SUCCESS = 1;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null && msg.obj instanceof BaseRespons) {

					base = (BaseRespons) msg.obj;
					if (base.resultCode.equals("1000")) {
						Intent intent = new Intent();
						intent.putExtra("phone", phone);
						intent.setClass(RegisteredActivity.this,
								GetCodeActivity.class);
						startActivity(intent);
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
		setContentView(R.layout.registered_activity);

		initview();
	}

	private void initview() {
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_right.setVisibility(View.INVISIBLE);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_left.setText("");
		tv_title_content.setText("注册");

		btn_registered_next = (Button) findViewById(R.id.btn_registered_next);
		et_registered_phone = (TextView) findViewById(R.id.et_registered_phone);
		btn_registered_next.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {

		// 下一步
		case R.id.btn_registered_next:
			phone = et_registered_phone.getText().toString().trim();
			if (phone.equals("") || phone.length() < 11) {
				showToast("手机号码格式不正确");
			} else {
				startProgressDialog();
				new checkPhoneThread().start();
			}
			break;
		// 忘记密码
		case R.id.btn_title_left:
			finish();
			break;
		default:
			break;
		}
	}

	class checkPhoneThread extends Thread {
		@Override
		public void run() {
			bean = new CheckPhoneRequest();
			bean.telephone = phone;
			bean.userId = ShareUtil.getAccountId(RegisteredActivity.this);
			Object data = NetRequestEngine.checkphone(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);

		}
	}

}
