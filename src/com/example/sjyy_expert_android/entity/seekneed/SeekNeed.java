package com.example.sjyy_expert_android.entity.seekneed;

import java.util.List;

public class SeekNeed {
	public Integer demandId;//需求Id
	   
	public Integer demanderId;
	
	public String demanderName;//用户名
 
	public Integer doctorId;

	public String demandDesc;//需求内容
   
	public long demandPrice;//预算价格
  
	public Integer demandStatus;//需求状态   1 可以预约 4可以预约 2不可以预约
  
	public String createTimeString;//订单时间
   
	public String demandTimeString;//预计时间
	public java.lang.String demandTitle;//主题
	public int serviceTypeId;
}
