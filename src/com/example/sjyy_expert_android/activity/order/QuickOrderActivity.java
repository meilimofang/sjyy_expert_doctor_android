package com.example.sjyy_expert_android.activity.order;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.MainActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.login.LoginRequest;
import com.example.sjyy_expert_android.entity.login.LoginRespons;
import com.example.sjyy_expert_android.http.NetRequestEngine;



public class QuickOrderActivity extends BaseActivity{
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	
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
		setContentView(null);
		initview();
	}

	private void initview() {
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_right.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("注册");
		tv_title_content.setText("私家医院");
	}
	
	class dataThread extends Thread {

		@Override
		public void run() {
			bean = new LoginRequest();
			Object data = NetRequestEngine.login(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}

	}
}
