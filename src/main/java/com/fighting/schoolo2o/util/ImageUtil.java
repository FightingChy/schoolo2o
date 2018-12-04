package com.fighting.schoolo2o.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r = new Random();
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	/**
	 * 将CommonsMultipartFile转换为File
	 * @param cFile
	 * @return
	 */
	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {

		File newFile = new File(cFile.getOriginalFilename());
		try {
			cFile.transferTo(newFile);
		} catch (IllegalStateException | IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return newFile;
	}

	/**
	 * 处理缩略图并返回新生成图片的相对值路径
	 * 
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	public static String generateThumbnail(InputStream thumbnailInputStream, String fileName, String targetAddr) {
		// 随机名
		String realFileName = getRandomFileName();
		// 扩展名
		String extension = getFileExtension(fileName);
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is:" + relativeAddr);
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is:" + PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnailInputStream).size(200, 200)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
					.outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return relativeAddr;

	}

	/**
	 * 创建目标路径所涉及到的目录,即/home/work/xiangze/xxx.jpg
	 * 
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath();
		File dirPath = new File(realFileParentPath + targetAddr);
		if (!dirPath.isDirectory()) {
			dirPath.mkdirs();
		}
	}

	/**
	 * 获取输入文件流的扩展名
	 * 
	 * @param cFile
	 * @return
	 */
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 生成随机文件名，当前年月日小时分钟秒钟 + 五位随机数
	 * 
	 * @return
	 */
	public static String getRandomFileName() {
		// 获取随机的5位数
		int rannum = r.nextInt(89999) + 10000;
		String nowTimeStr = sdf.format(new Date());
		return nowTimeStr + rannum;
	}

	public static void main(String[] args) throws IOException {

		System.out.println(ImageUtil.class.getClassLoader().toString());
		System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
		Thumbnails.of(new File("D:/eclipse-workspace/photo8.jpg")).size(200, 200)
				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
				.outputQuality(0.8f).toFile("D:/eclipse-workspace/photo8new.jpg");

		// Thumbnails.of(new File())
		// Thread.currentThread().getContextClassLoader().getResource(name)
	}

}
