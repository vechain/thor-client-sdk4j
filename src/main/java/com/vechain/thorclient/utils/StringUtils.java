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


	public static byte[] toHexBytes(String strs) {
		String strsCopy = strs.toLowerCase();
		if (strsCopy.startsWith("0x")){
			strsCopy=strsCopy.substring(2);
		}

		if (strsCopy.length()%2==1){
			strsCopy = "0" + strsCopy;
		}

		byte[] retBytes = new byte[strsCopy.length()/2];
		int i = 0;

		for(byte a: strsCopy.getBytes()) {
			retBytes[i/2] = (byte) (((int)retBytes[i/2])*16 + asciiToHexByte(a));
			i++;
		}

		return retBytes;
	}

	public static byte[] toUTFBytes(String strs) {
		return strs.getBytes();
	}

	static byte asciiToHexByte(int i){
		if(i >= '0' && i <= '9') {
			return (byte)(i - '0');
		}

		if(i >= 'a' && i <= 'f') {
			return (byte)(i - 'a' + 10);
		}

		if(i >= 'A' && i <= 'F') {
			return (byte)(i - 'A' + 10);
		}

		return 0;
	}

}
