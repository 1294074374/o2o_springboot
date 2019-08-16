package com.imooc.o2o.entity;

import java.util.Date;

public class WechatAuth {

	// 微信ID
	private Long wechatAuthId;
	// openID
	private String openId;
	// 创建时间
	private Date createTime;
	// 用户
	private PersonInfo personInfo;

	public Long getWechatAuthId() {
		return wechatAuthId;
	}

	public void setWechatAuthId(Long wechatAuthId) {
		this.wechatAuthId = wechatAuthId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public PersonInfo getPersionInfo() {
		return personInfo;
	}

	public void setPersionInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
}
