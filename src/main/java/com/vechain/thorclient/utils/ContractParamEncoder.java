package com.vechain.thorclient.utils;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vechain.thorclient.core.model.clients.base.AbiDefinition;

public class ContractParamEncoder {

	public static final int MAX_BIT_LENGTH = 256;
	public static final int MAX_BYTE_LENGTH = MAX_BIT_LENGTH / 8;
	public static final int DEFAULT_BIT_LENGTH = MAX_BIT_LENGTH >> 1;

	public static boolean isDynamic(int index, Object param, String abiType) {
		boolean isDynamic = false;
		switch (abiType) {
		case "string":
			if (!(param instanceof String)) {
				throw new IllegalArgumentException("Parameter format is not match! index:" + index + " expect: "
						+ abiType + ",but get " + param.getClass().getSimpleName());
			}
			isDynamic = true;
			break;
		case "bytes":
			if (!param.getClass().isArray()) {
				throw new IllegalArgumentException("Parameter format is not match! index:" + index + " expect: "
						+ abiType + ",but get " + param.getClass().getSimpleName());
			} else {
				try {
					@SuppressWarnings("unused")
					byte[] t = (byte[]) param;
				} catch (Exception e) {
					throw new IllegalArgumentException("Parameter format is not match! index:" + index + " expect: "
							+ abiType + ",but get " + param.getClass().getSimpleName());
				}
			}
			isDynamic = true;
			break;
		default:
			if (abiType.contains("[]")) {
				if (!param.getClass().isArray()) {
					throw new IllegalArgumentException("Parameter format is not match! index:" + index + " expect: "
							+ abiType + ",but get " + param.getClass().getSimpleName());
				}
				isDynamic = true;
			}
			break;
		}
		return isDynamic;
	}

	public static int getLength(List<AbiDefinition.NamedType> inputs, Object... params) {
		int count = 0;
		Pattern pattern = Pattern.compile("\\w+\\[\\d+\\]*");
		for (int i = 0; i < params.length; i++) {
			Matcher matcher = pattern.matcher(inputs.get(i).getType());
			if (matcher.matches()) {
				if (params[i].getClass().isArray()) {
					count += Array.getLength(params[i]);
				} else if (params[i] instanceof List) {
					count += ((List) params[i]).size();
				} else {
					throw new IllegalArgumentException(
							inputs.get(i).getType() + " " + params[i].getClass().getSimpleName());
				}
			} else {
				count++;
			}
		}
		return count;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String encode(String abiType, Object param) {
		try {
			if (abiType.contains("[]")) {
				List<Object> list = new ArrayList<Object>();
				String type = abiType.substring(0, abiType.indexOf("["));
				for (int i = 0; i < Array.getLength(param); i++) {
					list.add(Array.get(param, i));
				}
				return encodeDynamicArray(type, list);
			} else if (abiType.contains("[")) {
				List<Object> list = new ArrayList<Object>();
				String type = abiType.substring(0, abiType.indexOf("["));
				if (param instanceof List) {
					list.addAll((List) param);
				} else {
					for (int i = 0; i < Array.getLength(param); i++) {
						list.add(Array.get(param, i));
					}
				}
				return encodeArrayValues(type, list);
			} else if (abiType.startsWith("int")) {
				return encodeNumeric((BigInteger) param, false);
			} else if (abiType.startsWith("uint")) {
				return encodeNumeric((BigInteger) param, true);
			} else if (abiType.equals("fixed")) {
				return encodeNumeric((BigInteger) param, false);
			} else if (abiType.equals("ufixed")) {
				return encodeNumeric((BigInteger) param, true);
			} else if (abiType.equals("address")) {
				return encodeAddress((String) param);
			} else if (abiType.equals("bool")) {
				return encodeBool((boolean) param);
			} else if (abiType.equals("bytes")) {
				return encodeDynamicBytes((byte[]) param);
			} else if (abiType.startsWith("bytes")) {
				return encodeBytes((byte[]) param);
			} else if (abiType.equals("string")) {
				return encodeString((String) param);
			} else {
				throw new UnsupportedOperationException("Type cannot be encoded: " + abiType);
			}
		} catch (Exception e) {
			throw new UnsupportedOperationException(
					"Type cannot be encoded: " + abiType + " paramClass:" + param.getClass().getSimpleName(), e);
		}
	}

	public static String encodeAddress(String address) {
		if (!StringUtils.isHex(address)) {
			throw new IllegalArgumentException("Parameter format is not hex string");
		}
		byte[] paramBytes = BytesUtils.toByteArray(address);
		if (paramBytes == null || paramBytes.length > MAX_BYTE_LENGTH) {
			throw new IllegalArgumentException("Parameter format is hex string size too large, or null");
		}
		if (paramBytes.length < MAX_BYTE_LENGTH) {
			byte[] fillingZero = new byte[MAX_BYTE_LENGTH];
			System.arraycopy(paramBytes, 0, fillingZero, MAX_BYTE_LENGTH - paramBytes.length, paramBytes.length);
			return BytesUtils.toHexString(fillingZero, null);
		} else {
			return BytesUtils.cleanHexPrefix(address);
		}
	}

	public static String encodeNumeric(BigInteger numericType, boolean isSigned) {
		byte[] rawValue = toByteArray(numericType, isSigned);
		byte paddingValue = getPaddingValue(numericType);
		byte[] paddedRawValue = new byte[MAX_BYTE_LENGTH];
		if (paddingValue != 0) {
			for (int i = 0; i < paddedRawValue.length; i++) {
				paddedRawValue[i] = paddingValue;
			}
		}

		System.arraycopy(rawValue, 0, paddedRawValue, MAX_BYTE_LENGTH - rawValue.length, rawValue.length);
		return BytesUtils.toHexString(paddedRawValue, null);
	}

	private static byte getPaddingValue(BigInteger numericType) {
		if (numericType.signum() == -1) {
			return (byte) 0xff;
		} else {
			return 0;
		}
	}

	private static byte[] toByteArray(BigInteger value, boolean isSigned) {
		if (isSigned) {
			if (value.bitLength() == MAX_BIT_LENGTH) {
				// As BigInteger is signed, if we have a 256 bit value, the resultant byte array
				// will contain a sign byte in it's MSB, which we should ignore for this
				// unsigned
				// integer type.
				byte[] byteArray = new byte[MAX_BYTE_LENGTH];
				System.arraycopy(value.toByteArray(), 1, byteArray, 0, MAX_BYTE_LENGTH);
				return byteArray;
			}
		}
		return value.toByteArray();
	}

	public static String encodeBool(boolean value) {
		byte[] rawValue = new byte[MAX_BYTE_LENGTH];
		if (value) {
			rawValue[rawValue.length - 1] = 1;
		}
		return BytesUtils.toHexString(rawValue, null);
	}

	public static String encodeBytes(byte[] value) {
		int length = value.length;
		int mod = length % MAX_BYTE_LENGTH;

		byte[] dest;
		if (mod != 0) {
			int padding = MAX_BYTE_LENGTH - mod;
			dest = new byte[length + padding];
			System.arraycopy(value, 0, dest, 0, length);
		} else {
			dest = value;
		}
		return BytesUtils.toHexString(dest, null);
	}

	public static String encodeDynamicBytes(byte[] dynamicBytes) {
		int size = dynamicBytes.length;
		String encodedLength = encode("uint", BigInteger.valueOf(size));
		String encodedValue = encodeBytes(dynamicBytes);

		StringBuilder result = new StringBuilder();
		result.append(encodedLength);
		result.append(encodedValue);
		return result.toString();
	}

	public static String encodeString(String string) {
		byte[] utfEncoded = string.getBytes(StandardCharsets.UTF_8);
		return encodeDynamicBytes(utfEncoded);
	}

	public static String encodeArrayValues(String type, List<Object> value) {
		StringBuilder result = new StringBuilder();
		for (Object o : value) {
			result.append(encode(type, o));
		}
		return result.toString();
	}

	public static <T> String encodeDynamicArray(String type, List<Object> value) {
		int size = value.size();
		String encodedLength = encode("uint", BigInteger.valueOf(size));
		String encodedValues = encodeArrayValues(type, value);

		StringBuilder result = new StringBuilder();
		result.append(encodedLength);
		result.append(encodedValues);
		return result.toString();
	}

	public static BigInteger convertFixed(BigInteger m, BigInteger n) {
		return convertFixed(DEFAULT_BIT_LENGTH, DEFAULT_BIT_LENGTH, m, n);
	}

	public static BigInteger convertFixed(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
		BigInteger mPadded = m.shiftLeft(nBitSize);
		int nBitLength = n.bitLength();

		// find next multiple of 4
		int shift = (nBitLength + 3) & ~0x03;
		return mPadded.or(n.shiftLeft(nBitSize - shift));
	}
}
