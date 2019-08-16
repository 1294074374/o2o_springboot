package com.imooc.o2o.web.shopadmin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "shopadmin", method = { RequestMethod.GET })
public class ShopAdminController {
	/***
	 * 主要用于解析路由转发到相应的URL中
	 * 
	 * @return
	 */

	@RequestMapping(value = "/shopoperation")
	public String shopOperation() {
		// 转发到店铺添加/编辑页面
		return "shop/shopoperation";
	}

	@RequestMapping(value = "/shoplist")
	public String shopList() {
		// 转发到店铺列表页面
		return "shop/shoplist";
	}

	@RequestMapping(value = "/productcategorymanage", method = RequestMethod.GET)
	private String productCategoryManage() {
		// 转发到商品类别管理页面
		return "shop/productcategorymanage";
	}

	@RequestMapping(value = "/shopmanage", method = RequestMethod.GET)
	private String shopManage(HttpServletRequest request) {
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		Shop currentShop = new Shop();
		currentShop.setShopId(shopId);
		request.getSession().setAttribute("currentShop", currentShop);
		// 转发到店铺管理页面
		return "shop/shopmanage";
	}

	@RequestMapping(value = "/productoperation")
	public String productOperation() {
		// 转发到商品添加/编辑页面
		return "shop/productoperation";
	}
	
	@RequestMapping(value = "/productmanagement")
	public String productManagement() {
		// 转发到商品添加/编辑页面
		return "shop/productmanage";
	}
	
	@RequestMapping(value = "/shopauthmanage")
	public String shopauthmanage() {
		// 转发到权限管理页面
		return "shop/shopauthmanage";
	}
	@RequestMapping(value = "/shopauthedit")
	public String shopauthedit() {
		// 转发到权限管理页面
		return "shop/shopauthedit";
	}
}
