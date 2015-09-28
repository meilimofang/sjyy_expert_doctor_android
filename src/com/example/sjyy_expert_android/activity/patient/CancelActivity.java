package com.example.sjyy_expert_android.activity.patient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.orderBean.CancelRequest;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.ShareUtil;



/***
 * 类描述：患者取消预约界面
 * 
 * @author 海洋
 */
public class CancelActivity extends BaseActivity {

	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content,tv_cancel;
	private final static int SUCCESS = 1;

	private CancelRequest bean;
	private BaseRespons base;

	private RelativeLayout rl_cancel_refuse;
	private EditText et_cancel;
	private Button btn_cancel_commit;
	private String refuse, orderId;
	private int refuseId;
	private String refuseType;
	private String[] provinces = new String[] { "病情已经好转", "已在附近就医", "预约时间冲突", "其他" };
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
					showToast(msg.obj+"");
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
		setContentView(R.layout.cancel_activity);
		initview();
	}

	private void initview() {
		Intent intent = getIntent();
		orderId = intent.getStringExtra("orderId");
		tv_cancel=(TextView) findViewById(R.id.tv_cancel);
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		tv_title_content.setText("取消预约");
		btn_cancel_commit = (Button) findViewById(R.id.btn_cancel_commit);
		btn_cancel_commit.setOnClickListener(this);
		rl_cancel_refuse = (RelativeLayout) findViewById(R.id.rl_cancel_refuse);
		rl_cancel_refuse.setOnClickListener(this);
		et_cancel = (EditText) findViewById(R.id.et_cancel);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_title_left:

			break;

		case R.id.btn_cancel_commit:
			refuseType = tv_cancel.getText().toString().trim();
			refuse = et_cancel.getText().toString();
			if (refuseType.equals("拒绝原因")) {
				showToast("请选择取消原因");
			} else {
				if (refuseType.equals("病情已经好转")) {
					refuseId = 0;
				} else if (refuseType.equals("已在附近就医")) {
					refuseId = 1;
				} else if (refuseType.equals("预约时间冲突")) {
					refuseId = 2;
				} else if (refuseType.equals("其他")) {
					refuseId = 3;
				}
				new dataThread().start();
			}

			break;
		case R.id.rl_cancel_refuse:
			showListDialog();
			break;
		default:
			break;
		}
	}

	class dataThread extends Thread {
		@Override
		public void run() {
			bean = new CancelRequest();
			bean.orderStatus = 100;
			bean.cancelType = refuseId;
			bean.cancelContent = refuse;
			bean.orderId = orderId;
			bean.userId = ShareUtil.getAccountId(CancelActivity.this);
			Object data = NetRequestEngine.patientOrderCancel(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}
	}

	private void showListDialog()

	{

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("请选择原因");

		/**
		 * 
		 * 1、public Builder setItems(int itemsId, final OnClickListener
		 * 
		 * listener) itemsId表示字符串数组的资源ID，该资源指定的数组会显示在列表中。 2、public Builder
		 * 
		 * setItems(CharSequence[] items, final OnClickListener listener)
		 * 
		 * items表示用于显示在列表中的字符串数组
		 */

		builder.setItems(provinces, new DialogInterface.OnClickListener()

		{

			@Override
			public void onClick(DialogInterface dialog, int which)

			{

				switch (which) {
				case 0:
					tv_cancel.setText("病情已经好转");
					break;
				case 1:
					tv_cancel.setText("已在附近就医");
					break;
				case 2:
					tv_cancel.setText("预约时间冲突");
					break;
				case 3:
					tv_cancel.setText("其他");
					break;
				default:
					break;
				}

			}

		});

		builder.create().show();

	}

}
