package com.vechain.thorclient.utils;

/**
 * String operation utility class.
 */
public class StringUtils {

	/**
	 * Check if the string is blank string, null or space.
	 * @param str a string
	 * @return true or false.
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().isEmpty();
	}

	/**
	 * Remove the prefix "0x" or "VX"
	 * @param value a string
	 * @return string without prefix.
	 */
	public static String sanitizeHex(String value) {
		if(StringUtils.isBlank(value)) {
			return value;
		}
		if(value.toLowerCase().indexOf(Prefix.ZeroLowerX.getPrefixString()) == 0 ) {
			return value.substring(2);
		}
		return value;
	}

	/**
	 * Check if the string is hex string or not.
	 * @param value string object
	 * @return true or false.
	 */
	public static boolean isHex(String value) {
		return value != null && value.matches( "^(0x|0X)?[0-9a-fA-F]+$" );
	}

	/**
	 * Check if the string is critical hex string or not, must start with 0x or OX
	 * @param value hex string.
	 * @return return or
	 */
	public static boolean isCriticalHex(String value){
		return value != null && value.matches( "^(0x|0X)[0-9a-fA-F]+$" );
	}

}
