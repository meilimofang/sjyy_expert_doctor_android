package com.example.sjyy_expert_android.entity.registered;

import java.util.List;

/**
 * 第一级   省  直辖市
 * 第二级  市（省下属） 区（直辖市）
 * 第三级  医院名称
 * @author smn
 *
 */
public class Area {
	public Integer areaId;
	public String areaName;
	public List<Area> sonAreaList;
}
