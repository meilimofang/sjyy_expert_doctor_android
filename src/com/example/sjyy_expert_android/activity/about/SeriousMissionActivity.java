package com.example.sjyy_expert_android.activity.about;


import java.io.File;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.contans.UrlConstans;
import com.example.sjyy_expert_android.entity.BaseRequest;
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.workphoto.UpLoadPhoto;
import com.example.sjyy_expert_android.entity.workphoto.WorkPhotoBean;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.ShareUtil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class SeriousMissionActivity extends BaseActivity {
	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;
	private final static int SUCCESS = 1;
	private final static int SUCCESSTWO = 2;
	private BaseRequest bean;
	private WorkPhotoBean base;
	private UpLoadPhoto upload;
	private BaseRespons respons;
	private ImageView iv_upload_image;
	private Button bt_serious_renzheng, btn_upload_ima;
	private TextView tv_upload;
	HttpUtils utils;
	BitmapUtils bitUtils;
	private String realPath;
	String path;// 从相册或者照相机拍照的图片生成地址
	private static final int PHOTO_CAPTURE = 0x11;// 拍照
	private static final int PHOTO_RESULT = 0x12;// 结果
	Builder dialog;// 加载框
	private static String IMG_PATH = Environment.getExternalStorageDirectory()
			.toString()
			+ java.io.File.separator
			+ "sjyy"
			+ java.io.File.separator + "photo" + java.io.File.separator;// 照片存放地址
	private File file;
	private String iconUrl;
	private String zijiname = "";
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null && msg.obj instanceof WorkPhotoBean) {
					base = (WorkPhotoBean) msg.obj;
					if (base.resultCode.equals("1000")) {
						bitUtils.display(iv_upload_image, base.workPhotoUrl);
						if(base.workPhotoStatus==0){
							tv_upload.setVisibility(View.VISIBLE);
							btn_upload_ima.setVisibility(View.VISIBLE);
							iv_upload_image.setEnabled(true);
							bt_serious_renzheng.setBackgroundResource(R.drawable.weirenzheng);
						}else if(base.workPhotoStatus==1){
							iv_upload_image.setEnabled(false);
							tv_upload.setVisibility(View.GONE);
							bt_serious_renzheng.setBackgroundResource(R.drawable.yirenzheng);
							btn_upload_ima.setVisibility(View.GONE);
						}
					} else {
						
					}
				} else {
					showToast(msg.obj + "");
				}

				break;
			case SUCCESSTWO:
				if (msg.obj != null && msg.obj instanceof BaseRespons) {
					respons = (BaseRespons) msg.obj;
					if (respons.resultCode.equals("1000")) {
						showToast("上传成功,请耐心等待审核！");
						finish();
					} else {
						showToast(respons.resultInfo+"");
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
		setContentView(R.layout.serious_mission_activity);
		initview();
		initdata();
	}

	private void initdata() {
		startProgressDialog();
		new dataThread().start();
	}

	private void initview() {
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("");
		tv_title_content.setText("实名认证");
		iv_upload_image = (ImageView) findViewById(R.id.iv_upload_image);
		bt_serious_renzheng = (Button) findViewById(R.id.bt_serious_renzheng);
		btn_upload_ima = (Button) findViewById(R.id.btn_upload_ima);
		tv_upload=(TextView) findViewById(R.id.tv_upload);
		iv_upload_image.setOnClickListener(this);
		btn_upload_ima.setOnClickListener(this);
		File filePath = new File(IMG_PATH);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		utils = new HttpUtils();
		bitUtils = new BitmapUtils(this);
	}

	class dataThread extends Thread {

		@Override
		public void run() {
			bean = new BaseRequest();
			bean.userId=ShareUtil.getAccountId(SeriousMissionActivity.this);
			Object data = NetRequestEngine.getDoctorWorkPhoto(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}
	}
	class uploadThread extends Thread {

		@Override
		public void run() {
			upload = new UpLoadPhoto();
			upload.userId=ShareUtil.getAccountId(SeriousMissionActivity.this);
			upload.workPhoto=iconUrl;
			Object data = NetRequestEngine.uploadDoctorWorkPhoto(upload);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESSTWO;
			handler.sendMessage(message);
		}
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.iv_upload_image:
			photoseclect();
			dialog.show();
			break;
		case R.id.btn_upload_ima:
			startProgressDialog();
			new uploadThread().start();
			break;
		default:
			break;
		}
	}
	
	// 图片选取
	private void photoseclect() {
		dialog = new AlertDialog.Builder(this).setPositiveButton("相机",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 调用相册
						// 用时间戳作为图片名
						path = IMG_PATH + System.currentTimeMillis() + ".jpg";
//						getimage(path);
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(new File(path)));
						startActivityForResult(intent, PHOTO_CAPTURE);
					}
				}).setNegativeButton("相册",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						path = IMG_PATH + System.currentTimeMillis() + ".jpg";
						Intent intent = new Intent(Intent.ACTION_PICK);
						// intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("image/*");
						intent.putExtra("crop", "true");
						intent.putExtra("scale", true);
						intent.putExtra("return-data", false);
						intent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(new File(path)));
						intent.putExtra("outputFormat",
								Bitmap.CompressFormat.JPEG.toString());
						intent.putExtra("noFaceDetection", true); // no face
																	// detection
						startActivityForResult(intent, PHOTO_RESULT);
					}
				});
	}
	
	// 调用系统剪切图片
		public void startPhotoCrop(Uri uri) {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("scale", true);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
			intent.putExtra("return-data", false);
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", true); // no face detection
			startActivityForResult(intent, PHOTO_RESULT);
		}

		@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);

			if (requestCode == PHOTO_CAPTURE && resultCode == RESULT_OK) {
				startPhotoCrop(Uri.fromFile(new File(path)));// 对获取的图片进行剪切
				return;
			}
			if (requestCode == PHOTO_RESULT && resultCode == RESULT_OK) {
				file = new File(path);
				// 传入文件
				RequestParams params = new RequestParams();
				// 传入文件
				params.addBodyParameter("pic", new File(path));
				utils.send(
						HttpRequest.HttpMethod.POST,
						UrlConstans.PICTURE_URL+"uploadify/upload",
						params, new RequestCallBack<String>() {
							@Override
							public void onFailure(HttpException arg0, String arg1) {
								Toast.makeText(SeriousMissionActivity.this,
										"头像上传失败，请重新选择上传", 1).show();
							}

							@Override
							public void onSuccess(ResponseInfo<String> responseInfo) {
								String str = responseInfo.result;
								iconUrl = str.substring(
										str.indexOf("fileName") + 11,
										str.indexOf("fileName") + 51);
								bitUtils.display(iv_upload_image, path);
							}
						});
			}
		
		}
	
	
	
}
