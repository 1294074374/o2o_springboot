package com.imooc.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "/frontend")
public class ShopListController {

	@Autowired
	private AreaService areaService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private ShopService shopService;

	/**
	 * 返回商品列表页里的ShopCategory列表（二级或一级），以及区域信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "listshopspageinfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listShopsPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 试着从前端获取parenId
		long parenId = HttpServletRequestUtil.getLong(request, "parenId");
		List<ShopCategory> shopCategoryList = null;
		if (parenId != -1) {
			try {
				// 若果parenId存在则提取一级shopCategory下面的二级shopCategory列表
				ShopCategory shopCategoryCondition = new ShopCategory();
				ShopCategory parent = new ShopCategory();
				parent.setShopCategoryId(parenId);
				shopCategoryCondition.setParent(parent);
				shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			try {
				// 若果parenId不存在则提取改shopCategory下面shopCategory列表(用户在首页选择的是全部商品的列表)
				shopCategoryList = shopCategoryService.getShopCategoryList(null);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		// 获取地域信息
		List<Area> areaList = null;
		try {
			areaList = areaService.getAreaList();
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	/**
	 * 获取指定查询 条件下的店铺列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "listshops", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listShops(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 获取每一页需要显示的数据条数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 非空判断
		if ((pageIndex > -1) && (pageSize) > -1) {
			// 试着获取一级列表id
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			// 试着获取二级级列表id
			long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			// 试着获取区域Id
			int areaId = HttpServletRequestUtil.getInt(request, "areaId");
			// 试着获取模糊查询的名字
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			// 获取组合之后的查询条件
			Shop shopCondition = comparctShopCondition4Search(parentId, shopCategoryId, areaId, shopName);
			// 根据查询条件和分页信息获取店铺列表，并放回总数
			ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("errMsg", "empty pageSize or pageIndex");
			modelMap.put("success", false);
		}
		return modelMap;
	}

	/**
	 * 组合查询条件，并将条件封装在shopCondition中返回
	 * 
	 * @param parenId        父商品类别的ID
	 * @param shopCategoryId 商品所属类别的类别Id
	 * @param areaId         区域Id
	 * @param shopName       搜索的店铺名(模糊搜索)
	 * @return
	 */
	public Shop comparctShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
		Shop shopCondition = new Shop();
		// 判空
		if (parentId != -1L) {
			// 查询某个一级shopCategory下面的二级shopCategory里面的店铺列表
			ShopCategory childShopCategory = new ShopCategory();
			ShopCategory ParentShopCategory = new ShopCategory();
			ParentShopCategory.setShopCategoryId(parentId);
			childShopCategory.setParent(ParentShopCategory);
		}
		if (shopCategoryId != -1L) {
			// 查询二级shopCategory里面的店铺列表
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondition.setShopCategory(shopCategory);
		}
		if (areaId > -1) {
			// 查询位于某一区域Id下的店铺列表
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}
		if (shopName != null) {
			// 查询名字里带有shopName的店铺列表
			shopCondition.setShopName(shopName);
		}
		// 返回给前端该店铺的可用的
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}

}
