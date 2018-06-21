package com.vechain.thorclient.utils;

public class StringUtils {

	public static boolean isBlank(String str) {
		if (str == null || str.trim().isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static String sanitizeHex(String value) {
		if(StringUtils.isBlank(value)) {
			return value;
		}
		if(value.toLowerCase().indexOf("0x") == 0 || value.toLowerCase().indexOf("vx") == 0) {
			return value.substring(2);
		}
		return value;
	}
	
	public static boolean isHex(String value) {
		return value.matches("^(-0x|0x)?[0-9a-fA-F]+$");
	}
}
