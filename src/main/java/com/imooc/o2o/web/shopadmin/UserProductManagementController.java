package com.imooc.o2o.web.shopadmin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.EchartSeries;
import com.imooc.o2o.dto.EchartXAxis;
import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.ProductSellDailyService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.UserProductMapService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shop")
public class UserProductManagementController {
	@Autowired
	private UserProductMapService userProductMapService;
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ShopAuthMapService shopAuthMapService;
	@Autowired
	private ProductSellDailyService productSellDailyService;

	@RequestMapping(value = "/listuserproductmapsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserProductMapsByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 获取当前的店铺信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 空值检验
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			UserProductMap userProductMapCondition = new UserProductMap();
			userProductMapCondition.setShop(currentShop);
			String productName = HttpServletRequestUtil.getString(request, "productName");
			if (productName != null) {
				// 若前段相按照商品名模糊查询，则传入productName
				Product product = new Product();
				product.setProductName(productName);
				userProductMapCondition.setProduct(product);
			}
			UserProductMapExecution ue = userProductMapService.listUserProductMap(userProductMapCondition, pageIndex,
					pageSize);
			modelMap.put("userProductMapList", ue.getUserProductMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/listproductselldailyinfobyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductSellDailyinfobyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取当前店铺信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 空值校验 确保shopid不为空
		if ((currentShop != null) && (currentShop.getShopId() != null)) {
			// 添加查询条件
			ProductSellDaily productSellDailyCondition = new ProductSellDaily();
			productSellDailyCondition.setShop(currentShop);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			Date endTime = calendar.getTime();
			// 获取七天前的日期
			calendar.add(Calendar.DATE, -6);
			Date beginTime = calendar.getTime();
			// 根据传入的查询条件获取该店铺的商品销售情况
			List<ProductSellDaily> productSellDailyList = productSellDailyService
					.listProductSellDaily(productSellDailyCondition, beginTime, endTime);
			// 指定日期格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// 商品名列表 保证唯一性
			HashSet<String> legendData = new HashSet<String>();
			// x轴数据
			HashSet<String> xData = new HashSet<String>();
			// 定义series
			List<EchartSeries> series = new ArrayList<EchartSeries>();
			// 日销量列表
			List<Integer> totalList = new ArrayList<Integer>();
			// 当前商品名 默认为空
			String currentProductName = "";
			for (int i = 0; i < productSellDailyList.size(); i++) {
				ProductSellDaily productSellDaily = productSellDailyList.get(i);
				// 去重
				legendData.add(productSellDaily.getProduct().getProductName());
				xData.add(sdf.format(productSellDaily.getCreateTime()));
				if (!currentProductName.equals(productSellDaily.getProduct().getProductName())
						&& !currentProductName.isEmpty()) {
					// 如果currentProductName不扥与获取的商品，或者已遍历到列表的末尾，
					// 则遍历到下一个商品的日销量信息了，将前一轮遍历的信息放入series当中
					// 包括了商品名以及商品对应的统计日期以及当日销量
					EchartSeries es = new EchartSeries();
					es.setName(currentProductName);
					es.setData(totalList.subList(0, totalList.size()));
					series.add(es);
					totalList = new ArrayList<Integer>();
					// 变换下currentProductId为当前的productId
					currentProductName = productSellDaily.getProduct().getProductName();
					// 继续添加新的值
					totalList.add(productSellDaily.getTotal());
				} else {
					totalList.add(productSellDaily.getTotal());
					currentProductName = productSellDaily.getProduct().getProductName();
				}
				// 队列之末尾
				if (i == productSellDailyList.size() - 1) {
					EchartSeries es = new EchartSeries();
					es.setName(currentProductName);

					es.setData(totalList.subList(0, totalList.size()));
					series.add(es);

				}
			}
			modelMap.put("series", series);
			modelMap.put("legendData", legendData);
			// 凭借出xaxis
			List<EchartXAxis> xAxis = new ArrayList<EchartXAxis>();
			EchartXAxis exa = new EchartXAxis();
			exa.setData(xData);
			xAxis.add(exa);
			modelMap.put("xAxis", xAxis);
			modelMap.put("success", true);
		} else {

			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	/*
	 * @RequestMapping(value = "/adduserproductmap", method = RequestMethod.GET)
	 * 
	 * @ResponseBody private Map<String, Object>
	 * addUserProductMap(HttpServletRequest request) { Map<String, Object> modelMap
	 * = new HashMap<String, Object>(); PersonInfo user = (PersonInfo)
	 * request.getSession() .getAttribute("user"); String qrCodeinfo =
	 * HttpServletRequestUtil.getString(request, "state"); ObjectMapper mapper = new
	 * ObjectMapper(); WechatInfo wechatInfo = null; try { wechatInfo =
	 * mapper.readValue(qrCodeinfo, WechatInfo.class); } catch (Exception e) {
	 * modelMap.put("success", false); modelMap.put("errMsg", e.toString()); return
	 * modelMap; } if (!checkQRCodeInfo(wechatInfo)) { modelMap.put("success",
	 * false); modelMap.put("errMsg", "二维码信息非法！"); return modelMap; } Long productId
	 * = wechatInfo.getProductId(); Long customerId = wechatInfo.getCustomerId();
	 * UserProductMap userProductMap = compactUserProductMap4Add(customerId,
	 * productId); if (userProductMap != null && customerId != -1) { try { if
	 * (!checkShopAuth(user.getUserId(), userProductMap)) { modelMap.put("success",
	 * false); modelMap.put("errMsg", "无操作权限"); return modelMap; }
	 * UserProductMapExecution se = userProductMapService
	 * .addUserProductMap(userProductMap); if (se.getState() ==
	 * UserProductMapStateEnum.SUCCESS.getState()) { modelMap.put("success", true);
	 * } else { modelMap.put("success", false); modelMap.put("errMsg",
	 * se.getStateInfo()); } } catch (RuntimeException e) { modelMap.put("success",
	 * false); modelMap.put("errMsg", e.toString()); return modelMap; }
	 * 
	 * } else { modelMap.put("success", false); modelMap.put("errMsg", "请输入授权信息"); }
	 * return modelMap; }
	 * 
	 * private boolean checkQRCodeInfo(WechatInfo wechatInfo) { if (wechatInfo !=
	 * null && wechatInfo.getProductId() != null && wechatInfo.getCustomerId() !=
	 * null && wechatInfo.getCreateTime() != null) { long nowTime =
	 * System.currentTimeMillis(); if ((nowTime - wechatInfo.getCreateTime()) <=
	 * 5000) { return true; } else { return false; } } else { return false; } }
	 * 
	 * private UserProductMap compactUserProductMap4Add(Long customerId, Long
	 * productId) { UserProductMap userProductMap = null; if (customerId != null &&
	 * productId != null) { userProductMap = new UserProductMap(); PersonInfo
	 * personInfo = personInfoService .getPersonInfoById(customerId); Product
	 * product = productService.getProductById(productId);
	 * userProductMap.setProductId(productId);
	 * userProductMap.setShopId(product.getShop().getShopId());
	 * userProductMap.setProductName(product.getProductName());
	 * userProductMap.setUserName(personInfo.getName());
	 * userProductMap.setPoint(product.getPoint()); userProductMap.setCreateTime(new
	 * Date()); } return userProductMap; }
	 * 
	 * private boolean checkShopAuth(long userId, UserProductMap userProductMap) {
	 * ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
	 * .listShopAuthMapByShopId(userProductMap.getShopId(), 1, 1000); for
	 * (ShopAuthMap shopAuthMap : shopAuthMapExecution .getShopAuthMapList()) { if
	 * (shopAuthMap.getEmployeeId() == userId) { return true; } } return false; }
	 */
}
