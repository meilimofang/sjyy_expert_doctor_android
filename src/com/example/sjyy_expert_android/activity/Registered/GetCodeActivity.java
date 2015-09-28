package com.example.sjyy_expert_android.activity.Registered;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.registered.CheckCodeRequest;
import com.example.sjyy_expert_android.entity.registered.CheckPhoneRequest;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.ShareUtil;


/***
 * 类描述：获取验证码
 * @author 海洋
 */
public class GetCodeActivity extends BaseActivity {
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private final static int SUCCESS = 1;
	private final static int SUCCESS_CODE = 2;
	private CheckCodeRequest beancode;
	private BaseRespons base;
	private CheckPhoneRequest bean;
	private TextView tv_getcode_phone;
	private EditText et_getcode_code;
	private Button btn_getcode_next, bt_getcode_getcode;
	private String phone;
	private MyCount myCount;
	private String code;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null && msg.obj instanceof BaseRespons) {

					base = (BaseRespons) msg.obj;
					if (base.resultCode.equals("1000")) {
						Intent intent=new Intent();
						intent.putExtra("phone", phone);
						intent.setClass(GetCodeActivity.this, SettingPasswordActivity.class);
						startActivity(intent);
						finish();
					} else {
						showToast(base.resultInfo);
					}
				} else {
					showToast(msg.obj + "");
				}

				break;

			case SUCCESS_CODE:
				if (msg.obj != null && msg.obj instanceof BaseRespons) {
					base = (BaseRespons) msg.obj;
					if (base.resultCode.equals("1000")) {
						showToast("获取验证码成功");
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

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getcode_activity);
		initview();
	}

	private void initview() {
		Intent intent=getIntent();
		phone=intent.getStringExtra("phone");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		tv_title_content.setText("注册");

		tv_getcode_phone = (TextView) findViewById(R.id.tv_getcode_phone);
		tv_getcode_phone.setText(phone);
		et_getcode_code = (EditText) findViewById(R.id.et_getcode_code);
		btn_getcode_next = (Button) findViewById(R.id.btn_getcode_next);
		bt_getcode_getcode = (Button) findViewById(R.id.bt_getcode_getcode);
		btn_getcode_next.setOnClickListener(this);
		bt_getcode_getcode.setOnClickListener(this);
		myCount = new MyCount(60000, 1000);
		myCount.start();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		// 下一步
		case R.id.btn_getcode_next:
			code = et_getcode_code.getText().toString().trim();
			if (code.equals("")) {
				showToast("请输入验证码");
			} else {
				new checkCodeThread().start();
			}
			break;
		// 获取验证码
		case R.id.bt_getcode_getcode:
				myCount.start();
				new getCodeThread().start();
			
			break;

		default:
			break;
		}
	}

	public class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			bt_getcode_getcode.setText("   获取验证码   ");
			bt_getcode_getcode.setClickable(true);
			bt_getcode_getcode
					.setBackgroundResource(R.drawable.button_background);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			bt_getcode_getcode.setClickable(false);
			bt_getcode_getcode.setText("   (" + millisUntilFinished / 1000
					+ ")" + "重新获取   ");
			bt_getcode_getcode
					.setBackgroundResource(R.drawable.button_background_selector);
		}

	}

	class getCodeThread extends Thread {
		@Override
		public void run() {
			bean = new CheckPhoneRequest();
			bean.telephone=phone;
			bean.codeType="0";
			bean.userId = ShareUtil.getAccountId(GetCodeActivity.this);
			Object data = NetRequestEngine.getcode(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS_CODE;
			handler.sendMessage(message);

		}

	}

	class checkCodeThread extends Thread {
		@Override
		public void run() {
			beancode=new CheckCodeRequest();
			beancode.code=code;
			beancode.telephone=phone;
			beancode.userId=ShareUtil.getAccountId(GetCodeActivity.this);
			Object data = NetRequestEngine.checkcode(beancode);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}
	}
}
