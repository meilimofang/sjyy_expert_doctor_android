package com.example.sjyy_expert_android.entity.registered;

import java.util.List;

import com.example.sjyy_expert_android.entity.BaseRespons;
import com.example.sjyy_expert_android.entity.choosebean.ChooseServiceType;



public class RegisterResponse extends BaseRespons{
	public List<ServiceType> departmentList;//服务类型
	public List<Area> areaList;//区域
	public List<DoctorQualification> qualificationList;
	public String userId;
	public List<ChooseServiceType>  serviceTypeList;
}
