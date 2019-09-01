package com.imooc.o2o.web.superadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.CacheService;
import com.imooc.o2o.service.HeadLineService;
import com.imooc.o2o.service.ShopCategoryService;

@Controller
public class CacheController {
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private HeadLineService headLineService;
	@Autowired
	private ShopCategoryService shopCategoryService;

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/clearcache4area", method = RequestMethod.GET)
	private String clearCacheArea() {
		cacheService.removeFromCache(areaService.AREALISTKEY);
		return "shop/operationsuccess";
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/clearcache4headLine", method = RequestMethod.GET)
	private String clearCacheHeadLine() {
		cacheService.removeFromCache(headLineService.HLLISTKEY);
		return "shop/operationsuccess";
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/clearcache4shopCategory", method = RequestMethod.GET)
	private String clearCacheShopCategory() {
		cacheService.removeFromCache(shopCategoryService.SCLISTKEY);
		return "shop/operationsuccess";
	}
}
