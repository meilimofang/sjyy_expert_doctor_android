package com.example.sjyy_expert_android.activity.order;

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
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.orderBean.RefuseRequest;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.ShareUtil;


/***
 * 类描述：医生填写拒绝原因
 * 
 * @author 海洋
 */
public class RefuseActivity extends BaseActivity {

	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private final static int SUCCESS = 1;

	private RefuseRequest bean;
	private BaseRespons base;

	private EditText et_refuse;
	private Button btn_refuse_commit;
	private String refuse, orderId;
	private int refuseId;
	private int orderStatus;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null && msg.obj instanceof BaseRespons) {

					base = (BaseRespons) msg.obj;
					if (base.resultCode.equals("1000")) {

						showToast("已成功取消!");
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
		setContentView(R.layout.refuse_activity);
		initview();
	}

	private void initview() {
		Intent intent = getIntent();
		orderId = intent.getStringExtra("orderId");
		orderStatus=intent.getIntExtra("orderStatus", 0);
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		tv_title_content.setText("拒绝接诊");
		btn_refuse_commit = (Button) findViewById(R.id.btn_refuse_commit);
		btn_refuse_commit.setOnClickListener(this);
		et_refuse = (EditText) findViewById(R.id.et_refuse);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_refuse_commit:
			refuse=et_refuse.getText().toString();
			if(!refuse.equals("")){
				startProgressDialog();
				new dataThread().start();
			}else{
				showToast("请填写拒绝原因");
			}
			
			break;
		case R.id.rl_change_refuse:
			break;
		default:
			break;
		}
	}

	class dataThread extends Thread {
		@Override
		public void run() {
			bean = new RefuseRequest();
			bean.demandStatus = orderStatus;
			bean.cancelReason = refuse;
			bean.demandId = orderId;
			bean.userId = ShareUtil.getAccountId(RefuseActivity.this);
			Object data = NetRequestEngine.OrderOperate(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}
	}

}
