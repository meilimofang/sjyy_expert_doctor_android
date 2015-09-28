package com.example.sjyy_expert_android.entity.registered;

import java.util.List;
/**
 * 第一级     科室
 * 第二级 疾病
 * @author smn
 *
 */
public class ServiceType {
	public int departmentId;
	public String department;
	public List<ServiceType> departmentList ;
}
