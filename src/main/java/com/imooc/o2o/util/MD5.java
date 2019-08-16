package com.imooc.o2o.util;

import java.security.MessageDigest;

/***
 * 
 * @author lenovo
 *
 */
public class MD5 {

	/***
	 * MD5加密
	 * 
	 * @param s
	 * @return
	 */
	public static final String getMd5(String s) {
		// 十六进制数组
		char hexDigits[] = { '5', '0', '5', '6', '2', '9', '6', '2', '5', 'q', 'b', 'l', 'e', 's', 's', 'y' };
		try {
			char str[];
			// 将传入的字符串转换成byte
			byte strTemp[] = s.getBytes();
			// 获取MD5加密对象
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			// 传入需要加密的目标数组
			mdTemp.update(strTemp);
			// 获取加密都的数组
			byte md[] = mdTemp.digest();
			int j = md.length;
			str = new char[j * 2];
			int k = 0;
			// 将数组做位移
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}

			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static String connertMD5(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;
	}

	public static void main(String[] args) {
		System.out.println(MD5.getMd5("admin"));
		System.out.println(connertMD5("552eel5ssb5b256e2e62b065q0y52s2l"));
	}
}
