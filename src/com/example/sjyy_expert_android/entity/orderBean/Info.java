package com.example.sjyy_expert_android.entity.orderBean;

import java.math.BigDecimal;

public class Info {
	public Integer demandId;//需求Id
	public String demanderName;//用户名
	public String demandDesc;//需求内容
	public BigDecimal demandPrice;//预算价格
	public Integer demandStatus;//2接诊//5就诊 6已诊    3取消 
	public String demandStatusStr;//状态字符串
	public String createTimeString;//订单时间
	public String demandTimeString;//预计时间
	public String demandTitle;
	public String areaName;
	public String hospitalName;
	public String departmentName;
	public String qualificationName;
	public String isBusinessTripString;
	
	
}
