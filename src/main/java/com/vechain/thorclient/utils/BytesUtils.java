package com.vechain.thorclient.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Byte array relative
 */
public class BytesUtils {


    /**
     * Change the bytes array to hex string with or without prefix.
     * @param input
     * @param offset
     * @param length
     * @param prefix {@link Prefix}
     * @return blank string.
     */
    public static String toHexString(byte[] input, int offset, int length,Prefix prefix) {
        StringBuilder stringBuilder = new StringBuilder();
        if (prefix != null) {
            stringBuilder.append(prefix.getPrefixString());
        }

        for(int i = offset; i < offset + length; ++i) {
            stringBuilder.append(String.format("%02x", input[i] & 255));
        }

        return stringBuilder.toString();
    }

    /**
     * Convert byte array to hex string with prefix
     * @param input
     * @return
     */
    public static String toHexString(byte[] input, Prefix prefix) {
        return toHexString(input, 0, input.length, prefix);
    }

    /**
     * Convert hex string to byte array
     * @param hexString hex string with prefix "0x"
     * @return {@link byte[]} value, if failed return null;
     */
    public static byte[] toByteArray(String hexString){
        String currentHex = hexString;
        if(currentHex == null || StringUtils.isBlank(currentHex)){
            return null;
        }
        currentHex = cleanHexPrefix( currentHex );
        int len = currentHex.length();
        if (len == 0) {
            return new byte[] {};
        }

        byte[] data;
        int startIdx;
        if (len % 2 != 0) {
            data = new byte[(len / 2) + 1];
            data[0] = (byte) Character.digit(currentHex.charAt(0), 16);
            startIdx = 1;
        } else {
            data = new byte[len / 2];
            startIdx = 0;
        }

        for (int i = startIdx; i < len; i += 2) {
            data[(i + 1) / 2] = (byte) ((Character.digit(currentHex.charAt(i), 16) << 4)
                    + Character.digit(currentHex.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Get byte array from {@link BigInteger} object.
     * @param bigInteger {@link BigInteger} object.
     * @return {@link byte[]} value, if failed return null;
     */
    private static byte[] bigIntToBytes(BigInteger bigInteger) {
        if(bigInteger == null){
            return null;
        }
        return trimLeadingZeroes(bigInteger.toByteArray());
    }


    /**
     * Convert long value to byte array.
     * @param value long value.
     * @return byte array, if failed return null.
     */
    public static byte[] longToBytes(long value){
        BigInteger bigInteger = BigInteger.valueOf(value);
        return bigIntToBytes(bigInteger);
    }

    /**
     * Get BigInteger from byte array, the side effect is stripping the leading zeros.
     * @param bytes byte array.
     * @return
     */
    public static BigInteger bytesToBigInt(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new BigInteger(1,bytes);
    }


    /**
     * Trim the leading byte
     * @param bytes the input byte array.
     * @param b the byte need to be trimmed.
     * @return
     */
    public static byte[] trimLeadingBytes(byte[] bytes, byte b) {
        int offset = 0;
        for (; offset < bytes.length - 1; offset++) {
            if (bytes[offset] != b) {
                break;
            }
        }
        return Arrays.copyOfRange(bytes, offset, bytes.length);
    }

    /**
     * Trim the leading zero bytes
     * @param bytes
     * @return
     */
    public static byte[] trimLeadingZeroes(byte[] bytes) {
        return trimLeadingBytes(bytes, (byte) 0);
    }


    /**
     * Convert {@link BigInteger} value to {@link BigDecimal} value.
     * @param bgInt {@link BigInteger} value
     * @param precision  the precision value for VET and VeThor it is 18, means 10 power 18
     * @param scale the remain digits number of fractional part.
     * @return {@link BigDecimal} value.
     */
    public static BigDecimal bigIntToBigDecimal(BigInteger bgInt, int precision, int scale){
        if(bgInt == null || precision < 0 || scale < 0){
            return null;
        }
        BigDecimal decimal = new BigDecimal(bgInt);
        BigDecimal precisionDecimal = new BigDecimal(10).pow(precision);
        BigDecimal value = decimal.divide(precisionDecimal, scale, BigDecimal.ROUND_DOWN);
        return value;
    }


    /**
     * Convert a decimal defaultDecimalStringToByteArray to a byte array, with default 18 level precision, means 10 power 18.
     * @param amountString it is a decimal string. e.g. "42.42"
     * @return
     */
    public static byte[] defaultDecimalStringToByteArray(String amountString){
        return decimalStringToByteArray( amountString, 18 );
    }

    /**
     * Convert a decimal defaultDecimalStringToByteArray to a byte array.
     * @param amountString it is a decimal string. e.g. "42.42"
     * @param precisionLevel the precision level, means 10 power 18 precision.
     * @return
     */
    public static byte[] decimalStringToByteArray(String amountString, int precisionLevel){
        if(StringUtils.isBlank( amountString )){
            throw new IllegalArgumentException( "amount string is blank." );
        }
        if(precisionLevel < 0){
            throw new IllegalArgumentException( "precision level is null." );
        }
        BigDecimal amountDecimal = new BigDecimal(amountString);
        BigDecimal precisionDecimal = new BigDecimal(10).pow(precisionLevel);
        BigDecimal realAmount = amountDecimal.multiply(precisionDecimal);
        return trimLeadingZeroes(realAmount.toBigInteger().toByteArray());
    }

    /**
     * Check if  contains hex prefix
     * @param input
     * @return
     */
    public static boolean containsHexPrefix(String input) {
        return (input.startsWith(Prefix.ZeroLowerX.getPrefixString()));
    }

    public static String cleanHexPrefix(String input) {
        if (containsHexPrefix(input)) {
            return input.substring(2);
        } else {
            return input;
        }
    }


    /**
     *
     * @param value
     * @param length
     * @return
     */
    public static byte[] toBytesPadded(BigInteger value, int length) {
        byte[] result = new byte[length];
        byte[] bytes = value.toByteArray();
        int bytesLength;
        byte srcOffset;
        if (bytes[0] == 0) {
            bytesLength = bytes.length - 1;
            srcOffset = 1;
        } else {
            bytesLength = bytes.length;
            srcOffset = 0;
        }

        if (bytesLength > length) {
            throw new RuntimeException("Input is too large to put in byte array of size " + length);
        } else {
            int destOffset = length - bytesLength;
            System.arraycopy(bytes, srcOffset, result, destOffset, bytesLength);
            return result;
        }
    }


}
