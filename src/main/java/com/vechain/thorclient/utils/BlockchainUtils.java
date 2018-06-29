package com.vechain.thorclient.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * Blockchain utility, include address check, blockId check, amount calculate.
 */
public class BlockchainUtils {

	/**
	 * Check if the block revision is valid.
	 * 
	 * @param revision
	 *            block revision string.
	 * @return boolean value.
	 */
	public static boolean isValidRevision(String revision) {

		String blockNumPattern = "^[0-9]\\d*$";

		if (StringUtils.isBlank(revision)) {
			return false;
		}

		if ((StringUtils.isHex(revision) && revision.length() == 66)) {
			return true;
		} else if (Pattern.matches(blockNumPattern, revision)) {
			return true;
		} else if ("best".equalsIgnoreCase(revision)) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the address hex string is valid.
	 * 
	 * @param address
	 *            address hex string start with "0x", "VX" or without prefix string.
	 * @return boolean value.
	 */
	public static boolean isAddress(final String address) {
		String addressStr = address;
		if (!StringUtils.isBlank(addressStr) && (addressStr.startsWith(Prefix.ZeroLowerX.getPrefixString())
				|| addressStr.startsWith(Prefix.VeChainX.getPrefixString())) && addressStr.length() == 42) {
			addressStr = addressStr.substring(2);

		}
		return StringUtils.isHex(addressStr);
	}

	public static String convertToHexAddress(String address) {

		if (!StringUtils.isBlank(address) && (address.startsWith(Prefix.ZeroLowerX.getPrefixString())
				|| address.startsWith(Prefix.VeChainX.getPrefixString())) && address.length() == 42) {
			String currentAddr = address.substring(2);
			currentAddr = Prefix.ZeroLowerX.getPrefixString() + currentAddr;
			return currentAddr.toLowerCase();
		} else {
			return null;
		}

	}

	/**
	 * Check if hexId string is valid.
	 * 
	 * @param hexId
	 *            is block Id or txId.
	 * @return true or false.
	 */
	public static boolean isId(String hexId) {
		return !StringUtils.isBlank(hexId) && StringUtils.isHex(hexId) && hexId.length() == 66;
	}

	/**
	 * Check if the address is correct for checksum.
	 * 
	 * @param address
	 * @return
	 */
	public static boolean checkSumAddress(String address) {
		String checkSumAddress = getChecksumAddress(address);
		if (address.equals(checkSumAddress)) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the address is correct for checksum.
	 * 
	 * @param address
	 * @param isCheck
	 * @return
	 */
	public static boolean checkSumAddress(final String address, boolean isCheck) {
		boolean rtn = false;
		if (address != null && address.length() == 42 && address.toLowerCase().matches("vx[A-Fa-f0-9]{40}")) {
			if (isCheck) {
				String realAddress = address.substring(2);
				rtn = checkSumAddress(realAddress);
			} else {
				rtn = true;
			}
		}
		return rtn;
	}

	/**
	 * Get checksum address from hex string address with 0x prefix
	 * 
	 * @param address
	 *            hex string
	 * @return checksum address string.
	 */
	public static String getChecksumAddress(String address) {

		// remove prefix 0x
		address = BytesUtils.cleanHexPrefix(address);
		address = address.toLowerCase();

		// do keccak256 once
		byte[] bytes = CryptoUtils.keccak256(address.getBytes());
		StringBuffer buffer = new StringBuffer();
		String hex = BytesUtils.toHexString(bytes, null);

		char[] chars = hex.toCharArray();
		int size = address.length();

		char[] raws = address.toCharArray();

		for (int i = 0; i < size; i++) {
			if (parseInt(chars[i]) >= 8) {
				buffer.append(("" + raws[i]).toUpperCase());

			} else {
				buffer.append(raws[i]);
			}
		}

		return buffer.toString();
	}

	public static String fillZeroBefore(String s, int length) {
		for (int i = 0; i < length; i++) {
			s = "0" + s;
			if (s.length() >= length) {
				break;
			}
		}
		return s;
	}

	private static int parseInt(char value) {
		if (value >= 'a' && value <= 'f') {
			return 9 + (value - 'a' + 1);
		} else {
			return value - '0';
		}
	}

	/**
	 * get amount of {@link BigDecimal} value.
	 * 
	 * @param hexString
	 *            hex string of the amount.
	 * @param precision
	 *            the precision of the amount, with is 18 by default
	 * @param scale
	 *            the remain digits numbers of fractional part
	 * @return the amount value which can show to the end user.
	 */
	public static BigDecimal amount(String hexString, int precision, int scale) {
		byte[] balBytes = BytesUtils.toByteArray(hexString);
		if (balBytes == null) {
			return null;
		}
		BigInteger balInteger = BytesUtils.bytesToBigInt(balBytes);
		return BytesUtils.bigIntToBigDecimal(balInteger, precision, scale);
	}

	/**
	 * Convert big decimal to byte array.
	 * 
	 * @param amount
	 *            amount {@link BigDecimal}
	 * @param precision
	 *            must >= 0
	 * @return byte array.
	 */
	public static byte[] byteArrayAmount(BigDecimal amount, int precision) {
		if (amount == null) {
			throw new IllegalArgumentException("amount is null");
		}
		if (precision < 0) {
			throw new IllegalArgumentException("precision is invalid");
		}
		BigDecimal bigDecimal = amount.multiply(BigDecimal.TEN.pow(precision));
		BigInteger bigInt = bigDecimal.toBigInteger();
		return BytesUtils.trimLeadingZeroes(bigInt.toByteArray());
	}
}
