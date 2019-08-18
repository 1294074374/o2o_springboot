package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.dto.ShopAuthMapExecution;
import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.dto.WechatInfo;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.ShopAuthMapStateEnum;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ShortNetAddressUtil;
import com.imooc.o2o.util.wechat.WechatUtil;

@Controller
@RequestMapping("/shop")
public class ShopAuthManagementController {

	@Autowired
	private ShopAuthMapService shopAuthMapService;

	@Autowired
	private WechatAuthService wechatAuthService;

	@Autowired
	private PersonInfoService personInfoService;

	@RequestMapping(value = "/listshopauthmapsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopAuthMapsByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 取出分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 从session中获取店铺信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 空值判断
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			// 分页取出该店铺下面的授权信息列表
			ShopAuthMapExecution se = shopAuthMapService.listShopAuthMapByShopId(currentShop.getShopId(), pageIndex,
					pageSize);
			modelMap.put("shopAuthMapList", se.getShopAuthMapList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/getshopauthmapbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopAuthMapById(@RequestParam Long shopAuthId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 非空判断
		if (shopAuthId != null && shopAuthId > -1) {
			// 依据全台传入的shopAuthId 查找对应的授权信息
			ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
			modelMap.put("shopAuthMap", shopAuthMap);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopAuthId");
		}
		return modelMap;
	}

	/*
	 * @RequestMapping(value = "/addshopauthmap", method = RequestMethod.POST)
	 * 
	 * @ResponseBody private Map<String, Object> addShopAuthMap(String
	 * shopAuthMapStr, HttpServletRequest request) { Map<String, Object> modelMap =
	 * new HashMap<String, Object>(); ObjectMapper mapper = new ObjectMapper();
	 * ShopAuthMap shopAuthMap = null; try { shopAuthMap =
	 * mapper.readValue(shopAuthMapStr, ShopAuthMap.class); } catch (Exception e) {
	 * modelMap.put("success", false); modelMap.put("errMsg", e.toString()); return
	 * modelMap; } if (shopAuthMap != null) { try { Shop currentShop = (Shop)
	 * request.getSession().getAttribute("currentShop"); PersonInfo user =
	 * (PersonInfo) request.getSession().getAttribute("user"); if
	 * (currentShop.getOwnerId() != user.getUserId()) { modelMap.put("success",
	 * false); modelMap.put("errMsg", "无操作权限"); return modelMap; }
	 * shopAuthMap.setShopId(currentShop.getShopId());
	 * shopAuthMap.setEmployeeId(user.getUserId()); ShopAuthMapExecution se =
	 * shopAuthMapService.addShopAuthMap(shopAuthMap); if (se.getState() ==
	 * ShopAuthMapStateEnum.SUCCESS.getState()) { modelMap.put("success", true); }
	 * else { modelMap.put("success", false); modelMap.put("errMsg",
	 * se.getStateInfo()); } } catch (RuntimeException e) { modelMap.put("success",
	 * false); modelMap.put("errMsg", e.toString()); return modelMap; }
	 * 
	 * } else { modelMap.put("success", false); modelMap.put("errMsg", "请输入授权信息"); }
	 * return modelMap; }
	 */

	@RequestMapping(value = "/modifyshopauthmap", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShopAuthMap(String shopAuthMapStr, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 是授权编辑时候调用还是删除/回复授权操作的时候调用
		// 若为权责则进行验证码校验，后者则跳过验证码校验
		boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
		// 验证码校验
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		ObjectMapper mapper = new ObjectMapper();
		ShopAuthMap shopAuthMap = null;
		try {
			// 将前台传入的字符串json转换成shopAuthMap实例
			shopAuthMap = mapper.readValue(shopAuthMapStr, ShopAuthMap.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		// 空值判断
		if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
			try {
				// 看看被操作的对方是否为店家本身，店家本身不支持修改
				if (!checkPermission(shopAuthMap.getShopAuthId())) {
					modelMap.put("success", false);
					modelMap.put("errMsg", "无法对店家本身权限做操作(店家是店铺的最高权限)");
					return modelMap;
				}
				ShopAuthMapExecution se = shopAuthMapService.modifyShopAuthMap(shopAuthMap);
				if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入要修改的授权信息");
		}
		return modelMap;
	}

	private boolean checkPermission(Long shopAuthId) {
		ShopAuthMap grantedPerson = shopAuthMapService.getShopAuthMapById(shopAuthId);
		if (grantedPerson.getTitleFlag() == 0) {
			// 若是店家 则不能操作
			return false;
		}
		return true;
	}

	@RequestMapping(value = "/removeshopauthmap", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> removeShopAuthMap(Long shopAuthId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (shopAuthId != null && shopAuthId > 0) {
			try {
				ShopAuthMapExecution se = shopAuthMapService.removeShopAuthMap(shopAuthId);
				if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少选择一个授权进行删除");
		}
		return modelMap;
	}

	// 微信获取用户信息的api前缀
	private static String urlPrefix;
	// 微信获取用户信息的api中间部分
	private static String urlMiddle;
	// 微信获取用户信息的api后缀
	private static String urlSuffix;
	// 微信回传的响应添加授权信息的url
	private static String authUrl;

	@Value("${wechat.prefix}")
	public void setUrlPrefix(String urlPrefix) {
		ShopAuthManagementController.urlPrefix = urlPrefix;
	}

	@Value("${wechat.middle}")
	public void setUrlMiddle(String urlMiddle) {
		ShopAuthManagementController.urlMiddle = urlMiddle;
	}

	@Value("${wechat.suffix}")
	public void setUrlSuffix(String urlSuffix) {
		ShopAuthManagementController.urlSuffix = urlSuffix;
	}

	@Value("${wechat.auth.url}")
	public void setAuthUrl(String authUrl) {
		ShopAuthManagementController.authUrl = authUrl;
	}

	@RequestMapping(value = "/generateqrcode4shopauth", method = RequestMethod.GET)
	@ResponseBody
	private void generateQRCode4ShopAuth(HttpServletRequest request, HttpServletResponse response) {
		// 从session里获取当前shop信息
		Shop shop = (Shop) request.getSession().getAttribute("currentShop");
		if (shop != null && shop.getShopId() != null) {
			// 获取时间戳，保证二维码的时间有效性
			long timpStamp = System.currentTimeMillis();
			String content = "{aaashopIdaaa" + shop.getShopId() + "aaacreateTimeaaa" + timpStamp + "}";
			try {
				// 凭借URL
				String longUrl = urlPrefix + authUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
				String shopUrl = ShortNetAddressUtil.getShortURL(longUrl);
				// 将二维码以图片流的形式输出到前端
				BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(shopUrl, response);
				MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据微信回传的参数添加店铺的授权信息
	 * 
	 * @param shopAuthMapStr
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/addshopauthmap", method = RequestMethod.POST)
	@ResponseBody
	private String addShopAuthMap(String shopAuthMapStr, HttpServletRequest request)
			throws UnsupportedEncodingException {
		// 从request里面获取微信用户的信息
		WechatAuth auth = getEmployeeInfo(request);
		if (auth != null) {
			// 根据userId获取用户信息
			PersonInfo user = personInfoService.getPersonInfoById(auth.getPersionInfo().getUserId());
			// 根据将用户信息添加到user里
			request.getSession().setAttribute("user", user);
			// 解析微信回传过来的自定义参数state 由于之前进行了编码，这里需要解码
			String qrCodeinfo = new String(
					URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
			ObjectMapper mapper = new ObjectMapper();
			WechatInfo wechatInfo = null;
			try {
				// 将解码后的内容用aaa去掉替换之前生成二维啊的时候加入的aaa前缀，转换成WechatInfo实体类
				wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WechatInfo.class);
			} catch (Exception e) {
				return "shop/operationfail";
			}
			// 校验二维码是否过期
			if (!checkQRCodeInfo(wechatInfo)) {
				return "shop/operationfail";
			}
			// 去重校验
			// 获取该店铺下所有的授权信息
			ShopAuthMapExecution allMapList = shopAuthMapService.listShopAuthMapByShopId(wechatInfo.getShopId(), 1,
					999);

			List<ShopAuthMap> shopAuthList = allMapList.getShopAuthMapList();
			for (ShopAuthMap sm : shopAuthList) {
				if (sm.getEmployee().getUserId() == user.getUserId())
					return "shop/operationfail";
			}
			try {
				// 根据获取到的内容，添加到微信授权信息
				ShopAuthMap shopAuthMap = new ShopAuthMap();
				Shop shop = new Shop();
				shop.setShopId(wechatInfo.getShopId());
				shopAuthMap.setShop(shop);
				shopAuthMap.setEmployee(user);
				shopAuthMap.setTitle("员工");
				shopAuthMap.setTitleFlag(1);
				ShopAuthMapExecution se = shopAuthMapService.addShopAuthMap(shopAuthMap);
				if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
					return "shop/operationsuccess";
				} else {
					return "shop/operationsuccess";
				}
			} catch (RuntimeException e) {
				return "shop/operationfail";
			}
		}
		return "shop/operationfail";
	}

	/**
	 * 根据二维码携带的creatTime 判断是否超过了10分钟
	 * 
	 * @param wechatInfo
	 * @return
	 */
	private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
		if (wechatInfo != null && wechatInfo.getShopId() != null && wechatInfo.getCreateTime() != null) {
			long nowTime = System.currentTimeMillis();
			if ((nowTime - wechatInfo.getCreateTime() <= 600000)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 根据微信回传的code获取用户信息
	 * 
	 * @param request
	 * @return
	 */
	private WechatAuth getEmployeeInfo(HttpServletRequest request) {
		String code = request.getParameter("code");
		WechatAuth auth = null;
		if (null != code) {
			UserAccessToken token;
			try {
				token = WechatUtil.getUserAccessToken(code);
				String openId = token.getOpenId();
				request.getSession().setAttribute("openId", openId);
				auth = wechatAuthService.getWechatAuthByOpenId(openId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return auth;
	}
}
