package com.imooc.o2o.entity;

import java.util.Date;

public class Area {
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Date getCreateIime() {
		return createIime;
	}
	public void setCreateIime(Date createIime) {
		this.createIime = createIime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	//ID
	private Integer areaId;
	//名称
	private String areaName;
	//权重
	private Integer priority;
	//创建时间
	private Date createIime;
	//更新时间
	private Date lastEditTime;
	
}
