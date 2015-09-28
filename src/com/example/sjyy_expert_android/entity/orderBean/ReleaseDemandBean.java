package com.example.sjyy_expert_android.entity.orderBean;

import java.math.BigDecimal;

import com.example.sjyy_expert_android.entity.BaseRequest;


public class ReleaseDemandBean extends BaseRequest{
	public String demandDesc;//需求内容
	public BigDecimal demandPrice;//预算价格
	public String demandTitle;
	public String demandTimeString;//预计时间
	public int serviceTypeId;
}
