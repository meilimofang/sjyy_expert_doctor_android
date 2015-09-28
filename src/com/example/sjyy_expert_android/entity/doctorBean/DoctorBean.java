package com.example.sjyy_expert_android.entity.doctorBean;

import java.util.ArrayList;

public class DoctorBean {
	public String doctorId;//医生id
	public String doctorName;//医生名字
	public String hospitalName;//医院地址
	public String departmentName;//科室
	public String professionalField;//专业领域
	public String qualification;//职称
	public String iconUrl;//头像
	public String certification;//是否认证 0未认证
	public String orderStatus;//预约状态  0为 预约
	public String practiceExperience;
	public ArrayList<Integer> serviceTypeIdList;
}
