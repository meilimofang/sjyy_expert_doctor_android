package com.example.sjyy_expert_android.activity.user;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjyy_expert_android.BaseActivity;
import com.example.sjyy_expert_android.MainActivity;
import com.example.sjyy_expert_android.R;
import com.example.sjyy_expert_android.activity.choose.ChooseAreaActivity;
import com.example.sjyy_expert_android.activity.choose.ChooseCategoryActivity;
import com.example.sjyy_expert_android.activity.choose.ChooseDepartmentActivity;
import com.example.sjyy_expert_android.activity.choose.ChooseTitleActivity;
import com.example.sjyy_expert_android.contans.UrlConstans;
import com.example.sjyy_expert_android.entity.BaseRequest;
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.choosebean.ChooseDepartmentBean;
import com.example.sjyy_expert_android.entity.choosebean.ChooseHospitalBean;
import com.example.sjyy_expert_android.entity.choosebean.ChooseOccupationBean;
import com.example.sjyy_expert_android.entity.choosebean.ChooseServiceTypeBean;
import com.example.sjyy_expert_android.entity.choosebean.ChooseTitle;
import com.example.sjyy_expert_android.entity.choosebean.MyInformationfragmentBean;
import com.example.sjyy_expert_android.entity.user.DoctorUserInfo;
import com.example.sjyy_expert_android.entity.user.PerfectUserInfoBean;
import com.example.sjyy_expert_android.http.NetRequestEngine;
import com.example.sjyy_expert_android.util.PreferenceConstans;
import com.example.sjyy_expert_android.util.ShareUtil;
import com.example.sjyy_expert_android.view.SwitchButton;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PerfectDoctorInfoActivity extends BaseActivity {

	private Button btn_title_left, btn_title_right;
	private TextView tv_title_content;

	private final static int SUCCESS = 1;
	private final static int SUCCESSTWO = 2;
	private PerfectUserInfoBean bean;
	private BaseRespons base;
	private BaseRequest beanrequest;
	private DoctorUserInfo baserespons;
	private String username, phone, skill, background;
	private SwitchButton sb_if_visual;
	protected ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageView iv_perfect_icon;
	private EditText et_perfect_name, et_perfect_phone, et_perfect_skill,
			et_perfect_background;
	private TextView tv_perfect_area, tv_perfect_department,
			tv_perfect_professiona, tv_perfect_disease, tv_perfect_service,
			tv_perfect_serviceconect;
	private LinearLayout ll_perfect_area, ll_perfect_department,
			ll_perfect_professiona, ll_perfect_service;
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
	private int if_visual;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case SUCCESS:
				if (msg.obj != null && msg.obj instanceof BaseRespons) {

					base = (BaseRespons) msg.obj;
					if (base.resultCode.equals("1000")) {

						ShareUtil.putIntShare(PerfectDoctorInfoActivity.this,
								PreferenceConstans.SP_IFUSERINFO, 1);
						if (MyInformationfragmentBean.ifIntent) {
							showToast("更新成功!");
						} else {
							showToast("注册成功!");
						}

						goNewActivity(MainActivity.class);
						finish();
					} else {
						showToast(base.resultInfo);
					}
				} else {
					showToast(msg.obj + "");
				}
				MyInformationfragmentBean.ifIntent = false;
				break;

			case SUCCESSTWO:

				if (msg.obj != null && msg.obj instanceof DoctorUserInfo) {

					baserespons = (DoctorUserInfo) msg.obj;
					if (baserespons.resultCode.equals("1000")) {

						application.regis = baserespons;
						showview();
					} else {
						showToast(baserespons.resultInfo);
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
		setContentView(R.layout.perfect_userinfo_activity);
		if_visual = 2;
		initview();
		initImageLoader();

	}

	private void initview() {

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.img_no_net_pic)
				.showImageForEmptyUri(R.drawable.img_no_net_pic)
				.cacheInMemory(true).cacheOnDisc(true).build();
		File filePath = new File(IMG_PATH);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		utils = new HttpUtils();
		bitUtils = new BitmapUtils(this);
		sb_if_visual = (SwitchButton) findViewById(R.id.sb_if_visual);
		sb_if_visual.setChecked(true);
		sb_if_visual.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					if_visual = 2;// 展示
				} else {
					if_visual = 1;// 不展示
				}
			}
		});
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		tv_title_content = (TextView) findViewById(R.id.tv_title_content);
		btn_title_left.setOnClickListener(this);
		btn_title_right.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setBackgroundResource(R.drawable.back);
		btn_title_right.setText("保存");
		tv_title_content.setText("完善信息");
		iv_perfect_icon = (ImageView) findViewById(R.id.iv_perfect_icon);

		et_perfect_name = (EditText) findViewById(R.id.et_perfect_name);
		et_perfect_phone = (EditText) findViewById(R.id.et_perfect_phone);
		et_perfect_skill = (EditText) findViewById(R.id.et_perfect_skill);
		et_perfect_background = (EditText) findViewById(R.id.et_perfect_background);
		tv_perfect_serviceconect = (TextView) findViewById(R.id.tv_perfect_serviceconect);
		tv_perfect_area = (TextView) findViewById(R.id.tv_perfect_area);
		tv_perfect_department = (TextView) findViewById(R.id.tv_perfect_department);
		tv_perfect_professiona = (TextView) findViewById(R.id.tv_perfect_professiona);
		tv_perfect_service = (TextView) findViewById(R.id.tv_perfect_service);
		// tv_disease = (TextView) findViewById(R.id.tv_disease);
		// tv_disease.setText("");
		ll_perfect_area = (LinearLayout) findViewById(R.id.ll_perfect_area);
		ll_perfect_department = (LinearLayout) findViewById(R.id.ll_perfect_department);
		ll_perfect_professiona = (LinearLayout) findViewById(R.id.ll_perfect_professiona);
		// ll_perfect_disease = (LinearLayout)
		// findViewById(R.id.ll_perfect_disease);
		ll_perfect_service = (LinearLayout) findViewById(R.id.ll_perfect_service);
		// ll_perfect_disease.setOnClickListener(this);
		ll_perfect_area.setOnClickListener(this);
		ll_perfect_department.setOnClickListener(this);
		ll_perfect_professiona.setOnClickListener(this);
		iv_perfect_icon.setOnClickListener(this);
		ll_perfect_service.setOnClickListener(this);
		if (MyInformationfragmentBean.ifIntent) {

			new initdataThread().start();
			startProgressDialog();
		}

	}

	private void showview() {
		ChooseOccupationBean.role = baserespons.role;
		imageLoader.displayImage(baserespons.iconUrl, iv_perfect_icon, options);
		et_perfect_name.setText(baserespons.name);
		et_perfect_phone.setText(baserespons.tel);
		tv_perfect_area.setText(baserespons.hospital);
		tv_perfect_department.setText(baserespons.department);
		tv_perfect_professiona.setText(baserespons.qualification);
		et_perfect_skill.setText(baserespons.professionField);
		et_perfect_background.setText(baserespons.practiceExperience);
		// ChooseServiceProductsBean.ChooseServiceIdlist =
		// baserespons.diseaseIdsList;
		// ChooseServiceProductsBean.ChooseServiceProductslist =
		// baserespons.diseaseList;
		ChooseServiceTypeBean.ChooseServiceName = baserespons.serviceTypeNameList;
		ChooseServiceTypeBean.ChooseServiceNameId = baserespons.serviceTypeIdList;

		// String text = "";
		// for (String str :
		// ChooseServiceProductsBean.ChooseServiceProductslist) {
		// text = text + str + ",";
		// tv_disease.setText(text);
		// }

		String texts = "";
		for (String strs : ChooseServiceTypeBean.ChooseServiceName) {
			texts = texts + strs + ",";
			tv_perfect_serviceconect.setText(texts);
		}
		if (baserespons.visbility == 2) {
			sb_if_visual.setChecked(true);
		} else if (baserespons.visbility == 1) {
			sb_if_visual.setChecked(false);
		}

	}

	class dataThread extends Thread {
		@Override
		public void run() {
			bean = new PerfectUserInfoBean();
			bean.iconUrl = iconUrl;
			bean.name = username;
			bean.tel = phone;
			bean.hospitalId = ChooseHospitalBean.thirdId;
			bean.departmentId = ChooseDepartmentBean.fistId;
			bean.qualification = ChooseTitle.fistId;
			bean.professionalField = skill;
			bean.practiceExperience = background;
			bean.visbility = if_visual;
			// bean.diseaseIdList =
			// ChooseServiceProductsBean.ChooseServiceIdlist;
			bean.userId = ShareUtil
					.getAccountId(PerfectDoctorInfoActivity.this);
			bean.serviceTypeIdList = ChooseServiceTypeBean.ChooseServiceNameId;
			Object data = NetRequestEngine.completeAccountInfo(bean);
			Message message = Message.obtain();
			message.obj = data;
			message.what = SUCCESS;
			handler.sendMessage(message);
		}

	}

	class initdataThread extends Thread {

		@Override
		public void run() {
			beanrequest = new BaseRequest();
			beanrequest.userId = ShareUtil
					.getAccountId(PerfectDoctorInfoActivity.this);
			Object data = NetRequestEngine.getDoctorPersonInfo(beanrequest);
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
		case R.id.btn_title_right:
			// if (iconUrl==null) {
			if (!et_perfect_name.getText().toString().equals("")) {
				if (!et_perfect_phone.getText().toString().equals("")) {
					if (!tv_perfect_area.getText().toString().equals("")) {

						if (!tv_perfect_department.getText().toString()
								.equals("")) {

							if (!tv_perfect_professiona.getText().toString()
									.equals("")) {
								username = et_perfect_name.getText().toString();
								phone = et_perfect_phone.getText().toString();
								skill = et_perfect_skill.getText().toString();
								background = et_perfect_background.getText()
										.toString();
								new dataThread().start();
								startProgressDialog();
							} else {
								showToast("请填写职称!");
							}
						} else {
							showToast("请填写科室!");
						}

					} else {
						showToast("请填写医院!");
					}
				} else {
					showToast("请填写电话!");
				}
			} else {
				showToast("请填写姓名!");
			}
			// } else {
			// showToast("请选择头像!");
			// }
			break;
		case R.id.btn_title_left:
			finish();
			break;
		// 选择头像
		case R.id.iv_perfect_icon:
			photoseclect();
			dialog.show();
			break;
		// 选择城市
		case R.id.ll_perfect_area:
			goNewActivity(ChooseAreaActivity.class);
			break;
		// 选择科室
		case R.id.ll_perfect_department:
			goNewActivity(ChooseDepartmentActivity.class);
			break;
		// 选择职称
		case R.id.ll_perfect_professiona:
			goNewActivity(ChooseTitleActivity.class);
			break;
		// // 选择疾病
		// case R.id.ll_perfect_disease:
		// goNewActivity(ChooseServiceItemsActivity.class);
		// break;
		// 选择服务项目
		case R.id.ll_perfect_service:
			goNewActivity(ChooseCategoryActivity.class);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_CAPTURE && resultCode == RESULT_OK) {
			startPhotoCrop(Uri.fromFile(new File(path)));// 对获取的图片进行剪切
			return;
		}
		if (requestCode == PHOTO_RESULT && resultCode == RESULT_OK) {
			file = new File(path);
			// 传入文件
			Bitmap imageBitmap = BitmapFactory.decodeFile(path);
			Bitmap bit=comp(imageBitmap);
			realPath=IMG_PATH + System.currentTimeMillis()+"1" + ".jpg";
			saveBitmap(bit, realPath);
			RequestParams params = new RequestParams();
			// 传入文件
			params.addBodyParameter("pic", new File(realPath));
			utils.send(HttpRequest.HttpMethod.POST, UrlConstans.PICTURE_URL
					+ "uploadify/upload", params,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Toast.makeText(PerfectDoctorInfoActivity.this,
									"头像上传失败，请检查您的网络连接是否正确", 1).show();
						}

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							String str = responseInfo.result;
							iconUrl = str.substring(
									str.indexOf("fileName") + 11,
									str.indexOf("fileName") + 51);
							bitUtils.display(iv_perfect_icon, realPath);
						}
					});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (ChooseHospitalBean.ifIntent) {
			tv_perfect_area.setText(ChooseHospitalBean.thirdName);
		}
		if (ChooseDepartmentBean.ifIntent) {
			tv_perfect_department.setText(ChooseDepartmentBean.fistName);
		}

		if (ChooseTitle.ifIntent) {
			tv_perfect_professiona.setText(ChooseTitle.fistName);
		}

		// if (ChooseServiceProductsBean.ifIntent) {
		// String text = "";
		// for (String str :
		// ChooseServiceProductsBean.ChooseServiceProductslist) {
		// text = text + str + ",";
		// }
		// tv_disease.setText(text);
		// }else{
		// tv_disease.setText("");
		// }

		if (ChooseServiceTypeBean.ifIntent) {
			String texts = "";
			for (String str : ChooseServiceTypeBean.ChooseServiceName) {
				texts = texts + str + ",";
			}
			tv_perfect_serviceconect.setText(texts);
		}
	}

	/** 保存方法 */
	public void saveBitmap(Bitmap bm, String pathString) {
		Log.e("haiyang", "保存图片");
		File f = new File(pathString);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			Log.i("haiyang", "已经保存");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	float hh;
	float ww;
	private Bitmap comp(Bitmap image) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		if(w<h){
			hh = 70f;// 这里设置高度为800f
			ww = w*hh/h;// 这里设置宽度为480f
			Log.d("asasas", "1  hh: "+hh +" ww:"+w);
		}else if(w>h){
			hh = h*ww/w;// 这里设置高度为800f
			ww = 70f;// 这里设置宽度为480f
			Log.d("asasas", "2  hh: "+hh +" ww:"+w);
		}else if(w==h){
			hh = 70f;// 这里设置高度为800f
			ww = 70f;// 这里设置宽度为480f
		}
		
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}
}
