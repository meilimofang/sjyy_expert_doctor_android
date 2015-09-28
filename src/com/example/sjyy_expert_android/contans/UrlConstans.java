package com.example.sjyy_expert_android.contans;

/***
 * 类描述：URL页面 功能：保存本地，测试，正式，预生产接口URL 描述：TEST1，HOST，PRODUCT
 * 
 * @author 海洋
 */
public interface UrlConstans {

	
	public static final String HOST_WEB = "http://101.200.181.84:8080/"; 
	public static final String RA_STATIC = HOST_WEB + "appimg/"; // 图片URL(目前仅活动使用)
	public static final String PRODUCT = "http://123.57.234.78:8080/";//生产URL
	public static final String HOST_NEW = "http://192.168.0.111:8080/"; //测试URL
	public static final String PROJECT_NEW = "mmAppInterface/";
	public static final String RA_INTERFACE = HOST_WEB + PROJECT_NEW;
	public static final String PICTURE_URL = HOST_WEB+"mmPictureServer/";
	/********************************* 接口BEGIN *********************************/
	
	/**
	 * 登陆
	 * */
	public static final String LOGIN = RA_INTERFACE + "account/loginExpert";
	
	/**
	 * 获取验证码
	 * */
	public static final String SEND_CODE = RA_INTERFACE + "account/sendCode";
	
	/**
	 * 设置新密码
	 * */
	public static final String NEW_PAW = RA_INTERFACE + "account/newPwd";
	
	/**
	 * 校验手机号
	 * */
	public static final String REGISTER = RA_INTERFACE + "account/registerWithoutAccountCode";
	
	/**
	 * 校验验证码
	 * */
	public static final String REGISTER_CODE = RA_INTERFACE + "account/registerWithAccountCode";
	
	
	/**
	 * 注册设置密码
	 * */
	public static final String REGISTER_WITHPAW = RA_INTERFACE + "account/registerWithPwd";
	
	/**
	 * 首页
	 * */
	public static final String HEAD_PAGE = RA_INTERFACE + "accountremand/getRemandInfo";
	
	/**
	 * 去圆点
	 * */
	public static final String REMOVE_REMAND = RA_INTERFACE + "accountremand/removeRemandInfo";
	/**
	 * 在线预约
	 * */
	public static final String ONLINE_LIST = RA_INTERFACE + "accountdoctor/getOnlineOrderList";
	
	/**
	 * 去预约
	 * */
	public static final String GOTO_APPOINTMENT = RA_INTERFACE + "accountdoctor/getDoctorDetails";
	/**
	 * 选择登门时间预约
	 * */
	public static final String BESURE_APPOINTMENT = RA_INTERFACE + "order/makeOrder";
	
	/**
	 * 我的信息
	 * */
	public static final String GETUSER_INFO = RA_INTERFACE + "account/getPersonInfoSimple";
	
	/**
	 * 患者的信息
	 * */
	public static final String GETPATIENT_INFO = RA_INTERFACE + "account/getPatientPersonInfo";
	

	/**
	 * 医生的信息
	 * */
	public static final String GETDOCTOR_INFO = RA_INTERFACE + "account/getDoctorPersonInfo";
	
	
	/**
	 * 我的出诊
	 * */
	public static final String GETDOCTORLIST_INFO = RA_INTERFACE + "accountdoctor/getDoctorOrderList";
	
	/**
	 * 订单操作状态
	 * */
	public static final String GETORDER_DETAIL = RA_INTERFACE + "order/doctorOrderOperate";
	/**
	 * 我的需求查看订单状态
	 * */
	public static final String GETORDER_STATE = RA_INTERFACE + "demand/getDemandDetail";
	
	/**
	 * 填写拒绝原因
	 * */
	public static final String ORDEROPERATE_STATE = RA_INTERFACE + "demand/cancelDemand";
	/**
	 * 我的预约 医院
	 * */
	public static final String MY_APPOINTMENT = RA_INTERFACE + "order/getHospitalOrderList";
	/**
	 * 取消订单
	 * */
	public static final String ORDER_CANCEL = RA_INTERFACE + "order/patientOrderCancel";
	
	/**
	 * 评价
	 * */
	public static final String EVALUATEORDER = RA_INTERFACE + "order/patientEvaluateOrder";
	/**
	 * 图片上传

	 * */
	public static final String UPLOADIMAGE = HOST_NEW+"sjyyManagerSystem/" + "uploadify/upload";
	/**
	 * 完善信息

	 * */
	public static final String COMACCOUNTINFO = RA_INTERFACE+"account/completeAccountInfo";
	/**
	 *  认证信息

	 * */
	public static final String GETDOCTOR = RA_INTERFACE+"accountdoctor/getDoctorCertificationInfo";
	
	/**
	 * 城市选择列表
	 * */
	public static final String CITY_SELECT = RA_INTERFACE + "account/v3/area";
	
	/**
	 * 完善医生信息
	 * */
	public static final String COMPLETE = RA_INTERFACE + "account/completeDoctorExpertAccountInfo";
	
	/**
	 * 完善医院信息
	 * */
	public static final String COMPLETEHOS = RA_INTERFACE + "account/completeHospitalAccountInfo";
	/**
	 * 获取医生信息
	 * */
	public static final String GETDOCTORINFO = RA_INTERFACE + "account/getPersonInfo";
	/**
	 * 获取医生信息
	 * */
	public static final String MAKEDEMAND = RA_INTERFACE + "demand/makeDemand";
	/**
	 * 获取寻求列表
	 * */
	public static final String GETDEMANDLIST = RA_INTERFACE + "demand/getDemandList";
	/**
	 * 寻需求列表预约
	 * */
	public static final String ORDERDEMAND = RA_INTERFACE + "demand/orderDemand";
	/**
	 * 我的需求getDoctorDemandList

	 * */
	public static final String GETDOCTORDEMANDLIST = RA_INTERFACE + "demand/getDoctorDemandList";
	/**
	 * 操作订单状态
	 * */
	public static final String CANCELDEMOND = RA_INTERFACE + "demand/cancelDemond";
	/**
	 * 操作订单状态
	 * */
	public static final String HOSPITALDEMANDLIST = RA_INTERFACE + "demand/getHospitalDemandList";
	/**
	 * 我的预约查看订单详情
	 * */
	public static final String GETORDERDETAIL = RA_INTERFACE + "order/getOrderDetail";
	
	/**
	 * 接诊订单状态
	 * */
	public static final String ADMISSIONDMAND = RA_INTERFACE + "demand/admissionDemand";
	

	/**
	 * 医生我的预约
	 * */
	public static final String GETDOCTORORDERLIST = RA_INTERFACE + "order/getDoctorOrderList";
	
	
	/**
	 * 首页筛选
	 * */

	public static final String GETONLINEORDERLIST = RA_INTERFACE + "accountdoctor/getOnlineOrderListByServiceTypeId";
	
	
	
	/**
	 * 就诊
	 * */
	public static final String ADMISSIONDEMAND = RA_INTERFACE + "demand/visDemand";
	
	/**
	 * 医生我的预约getHospitalDemandList 
	 * */
	public static final String GETHOSPITALDEMANDLIST = RA_INTERFACE + "demand/getDoctorDemandList";
	
	/**
	 * 提交员工信息
	 * */

	public static final String MATCHEMPLOYEE = RA_INTERFACE + "account/matchEmployee";
	
	/**
	 * 需求获取病例信息
	 * */

	public static final String GETDEMANDPATIENTINFO = RA_INTERFACE + "demand/getDemandPatientInfo";
	/**
	 * 预约获取病例信息
	 * */
	public static final String GETORDERPATIENTINFO = RA_INTERFACE + "order/getOrderPatientInfo";
	
	/**
	 * 获取个人工牌照片及状态getDoctorWorkPhoto
	 * */
	public static final String GETDOCTORWORKPHOTO = RA_INTERFACE + "accountdoctor/getDoctorWorkPhoto";
	/**
	 * 图片上传
	 * */
	public static final String UPLOADPHOTO = RA_INTERFACE + "accountdoctor/uploadDoctorWorkPhoto";
	/********************************* 接口END *********************************/

}
