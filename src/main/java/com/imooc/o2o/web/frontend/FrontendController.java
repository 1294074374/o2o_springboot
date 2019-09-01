package com.imooc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/frontend")
public class FrontendController {
	/**
	 * 商城首页路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "frontend/index";
	}

	/*
	 * 商品种类列表路由
	 */
	@RequestMapping(value = "/shoplist", method = RequestMethod.GET)
	public String shopList() {
		return "frontend/shoplist";
	}

	/**
	 * 商品详情页路由
	 */
	@RequestMapping(value = "/shopdetail", method = RequestMethod.GET)
	public String shopdatail() {
		return "frontend/shopdetail";
	}

	/**
	 * 积分兑换页路由
	 */
	@RequestMapping(value = "/pointrecord", method = RequestMethod.GET)
	public String pointrecord() {
		return "frontend/pointrecord";
	}

	/**
	 * 消费记录路由
	 */
	@RequestMapping(value = "/myrecord", method = RequestMethod.GET)
	public String myrecord() {
		return "frontend/myrecord";
	}

	/**
	 * 消费记录路由
	 */
	@RequestMapping(value = "/mypoint", method = RequestMethod.GET)
	public String mypoint() {
		return "frontend/mypoint";
	}
}
