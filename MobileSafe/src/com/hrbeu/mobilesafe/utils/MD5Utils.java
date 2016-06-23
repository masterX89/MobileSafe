package com.hrbeu.mobilesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 *
 * @author Hankai Xia
 */
public class MD5Utils {

	/**
	 * MD5加密
	 *
	 * @param password
	 * @return
	 */
	public static String encode(String password) {
		try {
			// 获取MD5算法对象
			MessageDigest instance = MessageDigest.getInstance("MD5");
			// 对字符串加密，返回字节数组
			byte[] digest = instance.digest(password.getBytes());
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				// 获取字节的第八位有效值
				int i = b & 0xff;
				// 将整数转换为16进制
				String hexString = Integer.toHexString(i);
				if (hexString.length() < 2) {
					// 如果为1位，首位补0
					hexString = "0" + hexString;
				}
				sb.append(hexString);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// 无此算法抛出此异常
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取到病毒特征码MD5
	 *
	 * @param sourceDir
	 * @return
	 */
	public static String getFileMd5(String sourceDir) {
		File file = new File(sourceDir);
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = -1;
			//获取到数字摘要
			MessageDigest messageDigest = MessageDigest.getInstance("md5");
			while ((len = fis.read(buffer)) != -1) {
				messageDigest.update(buffer, 0, len);
			}
			byte[] result = messageDigest.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : result) {
				// 获取字节的第八位有效值
				int i = b & 0xff;
				// 将整数转换为16进制
				String hexString = Integer.toHexString(i);
				if (hexString.length() < 2) {
					// 如果为1位，首位补0
					hexString = "0" + hexString;
				}
				sb.append(hexString);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} return null;
	}

}
