package com.imooc.o2o.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtil {

	private static String winPath;

	private static String linuxPath;

	private static String shopPath;

	// 获取目的系统文件路径的书写方法
		private static String seperator = System.getProperty("file.separator");

		// 会根据不同的执行环境 处理图片根目录
		public static String getImgBasePath() {
			// 获取系统类型
			String os = System.getProperty("os.name");
			String basePath = "";
			// windows 环境下
			if (os.toLowerCase().startsWith("win")) {
				basePath = winPath;
			} else {
				basePath = linuxPath;
			}
			basePath = basePath.replace("/", seperator);
			// 返回路径
			return basePath;
		}

		// 相对子路径
		public static String getShopImagePath(long shopId) {
			String imagePath = shopPath + shopId + seperator;
			return imagePath.replace("/", seperator);
		}

	public void setSeperator(String seperator) {
		PathUtil.seperator = seperator;
	}

	@Value("${win.base.path}")
	public void setWinPath(String winPath) {
		PathUtil.winPath = winPath;
	}

	@Value("${linux.base.path}")
	public void setLinuxPath(String linuxPath) {
		PathUtil.linuxPath = linuxPath;
	}

	@Value("${shop.relevant.path}")
	public void setShopPath(String shopPath) {
		PathUtil.shopPath = shopPath;
	}

}
