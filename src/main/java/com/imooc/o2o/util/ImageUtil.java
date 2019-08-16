package com.imooc.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import com.imooc.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	private static String basePath = PathUtil.getImgBasePath();
	// 日期格式
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	// 随机数
	private static final Random r = new Random();
	// private static Logger logger;

	/**
	 * 处理缩略图
	 * 
	 * @param thumbnail  文件
	 * @param targetAddr 目标地址
	 * @return
	 */
	public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
		// 获取随机文件名
		String realFileName = getRandmFileName();
		// 获取文件扩展名
		String extension = getFileExtension(thumbnail.getImageName());
		// 创建路径
		makeDirPath(targetAddr);
		// 获取相对路径
		String relativeAddr = targetAddr + realFileName + extension;
		// logger.debug("current relativeAddr is:" + relativeAddr);
		// 创建文件
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		// logger.debug("current dest is:" + dest);
		// logger.debug("basePath is:" + basePath);
		try {
			Thumbnails.of(thumbnail.getImage()).size(200, 200)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
					.outputQuality(1f).toFile(dest);
		} catch (IOException e) {

		}
		return relativeAddr;
	}
	
	//处理缩略图
	public static String generateNornalImg(ImageHolder thumbnail, String targetAddr) {
		// 获取随机文件名
		String realFileName = getRandmFileName();
		// 获取文件扩展名
		String extension = getFileExtension(thumbnail.getImageName());
		// 创建路径
		makeDirPath(targetAddr);
		// 获取相对路径
		String relativeAddr = targetAddr + realFileName + extension;
		// logger.debug("current relativeAddr is:" + relativeAddr);
		// 创建文件
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		// logger.debug("current dest is:" + dest);
		// logger.debug("basePath is:" + basePath);
		try {
			Thumbnails.of(thumbnail.getImage()).size(337, 640)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
					.outputQuality(0.9f).toFile(dest);
		} catch (IOException e) {

		}
		return relativeAddr;
	}

	/**
	 * @ 创建目标路径所涉及的目录,即 /home/work/junlin/xxx.jpg
	 * @ 如果没有,那么home work junlin 这三个文件夹会自动创建
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		String realFilePareanPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFilePareanPath);
		if (!dirPath.exists()) {
			// 自动创建目录
			dirPath.mkdirs();
		}
	}

	/**
	 * @ 获取输入文件名的扩展名
	 * 
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(String fileName) {
		// 截取文件名中最后一个。 后的数据 即文件啊后缀名
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/*
	 * @获取随机文件名,当前年月日小时分钟秒钟+5位随机数
	 * 
	 * @Return
	 */
	public static String getRandmFileName() {
		// 获取随机五位数
		int rannum = r.nextInt(89999) + 10000;
		String nowTimeStr = sDateFormat.format(new Date());
		return nowTimeStr + rannum;
	}

	/**
	 * storePath是文件的路径还是目录的路径。 如果storePath是文件路径则删除该文件， 如果storePath是目录路径则删除该目录下的所有文件
	 * 
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath) {
		// 获取全路径
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
		// 判空
		if (fileOrPath.exists()) {
			// 如果 fileOrPath为路径
			if (fileOrPath.isDirectory()) {
				// 删除路径下的所有文件
				File files[] = fileOrPath.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[0].delete();
				}
			}
			//如果fileOrPath 是文件则直接删除 或者删除路径
			fileOrPath.delete();
		}
	}

	public static void main(String[] args) throws IOException {
		// 获取水印照片的绝对路径
		String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		/*
		 * @Thumbnail 主类方法
		 * 
		 * @of 需要处理的图片
		 * 
		 * @size 图片大小 第一个参数为高 第二个参数为宽
		 * 
		 * @watermark 加水印 第一个参数 水印的位置 BOTTOM_RIGHT在右下角 第二个参数 水印图片 第三个参数 透明度
		 * 
		 * @outputQulity 压缩程度
		 * 
		 * @toFile 文件输出的路径
		 */
		Thumbnails.of(new File("C:\\Users\\lenovo\\Desktop\\dabai.jpg")).size(200, 200)
				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
				.outputQuality(1f).toFile("C:\\Users\\lenovo\\Desktop\\dabainewfile.jpg");
		System.out.println("success");
	}
}
