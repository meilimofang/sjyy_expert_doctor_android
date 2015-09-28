package com.example.sjyy_expert_android.http;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.sjyy_expert_android.contans.UrlConstans;
import com.example.sjyy_expert_android.entity.BaseRequest;
import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.appointment.AppointmentRequest;
import com.example.sjyy_expert_android.entity.appointment.AppointmentRespons;
import com.example.sjyy_expert_android.entity.appointment.GoAppintment;
import com.example.sjyy_expert_android.entity.city.CitySelectResponse;
import com.example.sjyy_expert_android.entity.doctorBean.DoctorRequest;
import com.example.sjyy_expert_android.entity.doctorBean.DoctorRespons;
import com.example.sjyy_expert_android.entity.employyee.SubmitEmployeeInfoBean;
import com.example.sjyy_expert_android.entity.home.HomePageRespons;
import com.example.sjyy_expert_android.entity.hospatil.Hospatil;
import com.example.sjyy_expert_android.entity.loadmore.LoadMoreXunBean;
import com.example.sjyy_expert_android.entity.login.LoginRequest;
import com.example.sjyy_expert_android.entity.login.LoginRespons;
import com.example.sjyy_expert_android.entity.orderBean.AppointmentOrderDetailRepson;
import com.example.sjyy_expert_android.entity.orderBean.AppointmentOrderDetailState;
import com.example.sjyy_expert_android.entity.orderBean.CancelRequest;
import com.example.sjyy_expert_android.entity.orderBean.ChangeOrderDetail;
import com.example.sjyy_expert_android.entity.orderBean.GetOrderDetail;
import com.example.sjyy_expert_android.entity.orderBean.MyOrderBean;
import com.example.sjyy_expert_android.entity.orderBean.MyVisit;
import com.example.sjyy_expert_android.entity.orderBean.OrderDemand;
import com.example.sjyy_expert_android.entity.orderBean.OrderState;
import com.example.sjyy_expert_android.entity.orderBean.OrderStateRespons;
import com.example.sjyy_expert_android.entity.orderBean.RefuseRequest;
import com.example.sjyy_expert_android.entity.orderBean.ReleaseDemandBean;
import com.example.sjyy_expert_android.entity.orderBean.XuQiuBean;
import com.example.sjyy_expert_android.entity.patient.EvaluationRequest;
import com.example.sjyy_expert_android.entity.patient.PatientRespons;
import com.example.sjyy_expert_android.entity.patient.SeeCaseDemandRequestBean;
import com.example.sjyy_expert_android.entity.patient.SeeCaseDemandResons;
import com.example.sjyy_expert_android.entity.patient.SeeCaseOrderRequestBean;
import com.example.sjyy_expert_android.entity.registered.CheckCodeRequest;
import com.example.sjyy_expert_android.entity.registered.CheckPhoneRequest;
import com.example.sjyy_expert_android.entity.registered.RegisterResponse;
import com.example.sjyy_expert_android.entity.registered.SettingPasswordRequest;
import com.example.sjyy_expert_android.entity.seekneed.SeekNeedBaseRespons;
import com.example.sjyy_expert_android.entity.user.AuthenticationRequest;
import com.example.sjyy_expert_android.entity.user.DoctorUserInfo;
import com.example.sjyy_expert_android.entity.user.PatientUserInfo;
import com.example.sjyy_expert_android.entity.user.PerfectHospitalInfoBean;
import com.example.sjyy_expert_android.entity.user.PerfectUserInfoBean;
import com.example.sjyy_expert_android.entity.user.RenZheng;
import com.example.sjyy_expert_android.entity.user.UserInfo;
import com.example.sjyy_expert_android.entity.workphoto.UpLoadPhoto;
import com.example.sjyy_expert_android.entity.workphoto.WorkPhotoBean;
import com.example.sjyy_expert_android.util.StreamTools;

/***
 * 类描述：网络连接引擎 功能： 描述：TEST1，HOST，PRODUCT
 * 
 * @author 海洋
 */
public class NetRequestEngine {

	// 登陆
	public static Object login(LoginRequest baseRequest) {
		return postRequest(UrlConstans.LOGIN, JSON.toJSONString(baseRequest),
				LoginRespons.class);
	}

	// 校验手机号
	public static Object checkphone(CheckPhoneRequest baseRequest) {
		return postRequest(UrlConstans.REGISTER,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 获取验证码
	public static Object getcode(CheckPhoneRequest baseRequest) {
		return postRequest(UrlConstans.SEND_CODE,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 校验验证码
	public static Object checkcode(CheckCodeRequest baseRequest) {
		return postRequest(UrlConstans.REGISTER_CODE,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 设置密码
	public static Object setpassword(SettingPasswordRequest baseRequest) {
		return postRequest(UrlConstans.REGISTER_WITHPAW,
				JSON.toJSONString(baseRequest), RegisterResponse.class);
	}

	// // 找回密码
	// public static Object setnewpassword(SettingNewPasswordRequest
	// baseRequest) {
	// return postRequest(UrlConstans.NEW_PAW, JSON.toJSONString(baseRequest),
	// BaseRespons.class);
	// }

	// 首页
	public static Object homepage(BaseRequest baseRequest) {
		return postRequest(UrlConstans.HEAD_PAGE,
				JSON.toJSONString(baseRequest), HomePageRespons.class);
	}

	// 去圆点
	public static Object removeRemandInfo(BaseRequest baseRequest) {
		return postRequest(UrlConstans.REMOVE_REMAND,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 在线预约
	public static Object getonline(AppointmentRequest baseRequest) {
		return postRequest(UrlConstans.ONLINE_LIST,
				JSON.toJSONString(baseRequest), AppointmentRespons.class);
	}

	// 去预约
	public static Object gotoappointment(DoctorRequest baseRequest) {
		return postRequest(UrlConstans.GOTO_APPOINTMENT,
				JSON.toJSONString(baseRequest), DoctorRespons.class);
	}

	// 选择时间登门预约
	public static Object besureappointment(GoAppintment baseRequest) {
		return postRequest(UrlConstans.BESURE_APPOINTMENT,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 我的信息
	public static Object getPersonInfo(BaseRequest baseRequest) {
		return postRequest(UrlConstans.GETUSER_INFO,
				JSON.toJSONString(baseRequest), UserInfo.class);
	}

	// 患者的个人信息
	public static Object getPatient(BaseRequest baseRequest) {
		return postRequest(UrlConstans.GETPATIENT_INFO,
				JSON.toJSONString(baseRequest), PatientUserInfo.class);
	}

	// 医生的个人信息
	public static Object getDoctor(BaseRequest baseRequest) {
		return postRequest(UrlConstans.GETDOCTOR_INFO,
				JSON.toJSONString(baseRequest), DoctorUserInfo.class);
	}

	// 我的出诊
	public static Object getDoctorOrderList(GetOrderDetail baseRequest) {
		return postRequest(UrlConstans.GETDOCTORLIST_INFO,
				JSON.toJSONString(baseRequest), MyVisit.class);
	}

	// 订单操作状态
	public static Object getOrderDetail(ChangeOrderDetail baseRequest) {
		return postRequest(UrlConstans.GETORDER_DETAIL,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 查看订单详情
	public static Object getOrderState(OrderState baseRequest) {
		return postRequest(UrlConstans.GETORDER_STATE,
				JSON.toJSONString(baseRequest), OrderStateRespons.class);
	}

	// 填写拒绝原因
	public static Object OrderOperate(RefuseRequest baseRequest) {
		return postRequest(UrlConstans.ORDEROPERATE_STATE,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 我的预约
	public static Object getPatientOrderList(BaseRequest baseRequest) {
		return postRequest(UrlConstans.MY_APPOINTMENT,
				JSON.toJSONString(baseRequest), PatientRespons.class);
	}

	// 取消订单
	public static Object patientOrderCancel(CancelRequest baseRequest) {
		return postRequest(UrlConstans.ORDER_CANCEL,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 认证信息
	public static Object getDoctorCertificationInfo(
			AuthenticationRequest baseRequest) {
		return postRequest(UrlConstans.GETDOCTOR,
				JSON.toJSONString(baseRequest), RenZheng.class);
	}

	// 完善医生信息
	public static Object completeAccountInfo(PerfectUserInfoBean baseRequest) {
		return postRequest(UrlConstans.COMPLETE,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 完善医院信息
	public static Object completeHospitalAccountInfo(
			PerfectHospitalInfoBean baseRequest) {
		return postRequest(UrlConstans.COMPLETEHOS,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 评价
	public static Object patientEvaluateOrder(EvaluationRequest baseRequest) {
		return postRequest(UrlConstans.EVALUATEORDER,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	public static Object getCityList(BaseRequest baseRequest) {
		return postRequest(UrlConstans.CITY_SELECT,
				JSON.toJSONString(baseRequest), CitySelectResponse.class);
	}

	// 获取医生信息
	public static Object getDoctorPersonInfo(BaseRequest baseRequest) {
		return postRequest(UrlConstans.GETDOCTORINFO,
				JSON.toJSONString(baseRequest), DoctorUserInfo.class);
	}

	// 获取医院信息
	public static Object getHospatilPersonInfo(BaseRequest baseRequest) {
		return postRequest(UrlConstans.GETDOCTORINFO,
				JSON.toJSONString(baseRequest), Hospatil.class);
	}

	// 发布需求
	public static Object makeDemand(ReleaseDemandBean baseRequest) {
		return postRequest(UrlConstans.MAKEDEMAND,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 寻需求列表
	public static Object getDemandList(LoadMoreXunBean baseRequest) {
		return postRequest(UrlConstans.GETDEMANDLIST,
				JSON.toJSONString(baseRequest), SeekNeedBaseRespons.class);
	}

	// 寻需求列表预约
	public static Object orderDemand(OrderDemand baseRequest) {
		return postRequest(UrlConstans.ORDERDEMAND,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 医生我的需求列表
	public static Object getDoctorDemandList(BaseRequest baseRequest) {
		return postRequest(UrlConstans.GETDOCTORDEMANDLIST,
				JSON.toJSONString(baseRequest), MyVisit.class);
	}

	// 订单操作状态
	public static Object cancelDemond(ChangeOrderDetail baseRequest) {
		return postRequest(UrlConstans.CANCELDEMOND,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 接诊操作状态
	public static Object admissionDemand(ChangeOrderDetail baseRequest) {
		return postRequest(UrlConstans.ADMISSIONDMAND,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 医院我的需求列表
	public static Object getHospitalDemandList(BaseRequest baseRequest) {
		return postRequest(UrlConstans.HOSPITALDEMANDLIST,
				JSON.toJSONString(baseRequest), MyVisit.class);
	}

	// 我的预约查看订单详情
	public static Object getOrderDetail(AppointmentOrderDetailState baseRequest) {
		return postRequest(UrlConstans.GETORDERDETAIL,
				JSON.toJSONString(baseRequest),
				AppointmentOrderDetailRepson.class);
	}

	// 医生我的预约
	public static Object getDoctorOrderList(BaseRequest baseRequest) {
		return postRequest(UrlConstans.GETDOCTORORDERLIST,
				JSON.toJSONString(baseRequest), MyOrderBean.class);
	}

	// 就诊筛选
	public static Object visDemand(ChangeOrderDetail baseRequest) {
		return postRequest(UrlConstans.ADMISSIONDEMAND,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 医生我的需求
	public static Object getHospitalDemandLists(BaseRequest baseRequest) {
		return postRequest(UrlConstans.GETHOSPITALDEMANDLIST,
				JSON.toJSONString(baseRequest), XuQiuBean.class);
	}

	// 绑定员工信息
	public static Object matchEmployee(SubmitEmployeeInfoBean baseRequest) {
		return postRequest(UrlConstans.MATCHEMPLOYEE,
				JSON.toJSONString(baseRequest), BaseRespons.class);
	}

	// 需求获取病例
	public static Object getDemandPatientInfo(
			SeeCaseDemandRequestBean baseRequest) {
		return postRequest(UrlConstans.GETDEMANDPATIENTINFO,
				JSON.toJSONString(baseRequest), SeeCaseDemandResons.class);
	}

	// 预约获取病例
	public static Object getOrderPatientInfo(SeeCaseOrderRequestBean baseRequest) {
		return postRequest(UrlConstans.GETORDERPATIENTINFO,
				JSON.toJSONString(baseRequest), SeeCaseDemandResons.class);
	}
	
	
	// 上传图片
		public static Object uploadDoctorWorkPhoto(
				UpLoadPhoto baseRequest) {
			return postRequest(UrlConstans.UPLOADPHOTO,
					JSON.toJSONString(baseRequest), BaseRespons.class);
		}

		// 获取工牌照片等
		public static Object getDoctorWorkPhoto(BaseRequest baseRequest) {
			return postRequest(UrlConstans.GETDOCTORWORKPHOTO,
					JSON.toJSONString(baseRequest), WorkPhotoBean.class);
		}

	private static Object postRequest(String url, String jsonStr,
			Class<? extends BaseRespons> clazz) {

		Object obj = null;
		Log.e("", "数据请求JSON==" + jsonStr);
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		Log.e("", "数据请求URL==" + url);
		httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
		int statusCodes = 0;
		String content = "";
		try {

			HttpEntity entity = new StringEntity(jsonStr, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse response = client.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			Log.e("", "数据请求URL==" + statusCode);
			if (200 == statusCode) {

				InputStream is = response.getEntity().getContent();

				content = StreamTools.readInputStream(is);
				Log.e("", "数据请求==" + content);
				obj = JSON.parseObject(content, clazz);

			} else {
				statusCodes = statusCode;
				obj = "发送失败：请检查您的网络连接";
			}

		} catch (Exception e) {
			e.printStackTrace();
			obj = "发送失败：请检查您的网络连接";
		}

		return obj;
	}

}
