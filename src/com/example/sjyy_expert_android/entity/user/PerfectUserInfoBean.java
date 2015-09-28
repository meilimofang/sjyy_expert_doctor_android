package com.example.sjyy_expert_android.entity.user;

import java.util.List;

import com.example.sjyy_expert_android.entity.BaseRequest;



public class PerfectUserInfoBean  extends BaseRequest{

	public String iconUrl;//头像
	public String name;//姓名
	public String tel;//电话
	public Integer departmentId;//科室
	public Integer qualification;//职称
	public Integer hospitalId;//医院
	public String professionalField;//专业领域
	public String practiceExperience;//实践经验
//	public  List<Integer> diseaseIdList;
	public Integer visbility;
	public List<Integer> serviceTypeIdList;
}
