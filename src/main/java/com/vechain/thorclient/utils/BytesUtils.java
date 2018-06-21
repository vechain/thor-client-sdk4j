package com.vechain.thorclient.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;


public class BytesUtils {


    /**
     * Change the bytes array to hex string with or without prefix.
     * @param input
     * @param offset
     * @param length
     * @param prefix {@link Prefix}
     * @return
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
     * @return {@link byte[]} value.
     */
    public static byte[] toByteArray(String hexString){
        if(hexString == null || StringUtils.isBlank(hexString)){
            return null;
        }

        if(hexString.startsWith(Prefix.ZeroLowerX.getPrefixString())
                || hexString.startsWith( Prefix.VeChainX.getPrefixString() )){
            hexString = hexString.substring(2);
        }

        int len = hexString.length();
        if (len == 0) {
            return new byte[] {};
        }

        byte[] data;
        int startIdx;
        if (len % 2 != 0) {
            data = new byte[(len / 2) + 1];
            data[0] = (byte) Character.digit(hexString.charAt(0), 16);
            startIdx = 1;
        } else {
            data = new byte[len / 2];
            startIdx = 0;
        }

        for (int i = startIdx; i < len; i += 2) {
            data[(i + 1) / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Get byte array from {@link BigInteger} object.
     * @param bigInteger {@link BigInteger} object.
     * @return {@link byte[]} value.
     */
    private static byte[] bigIntegerToBytes(BigInteger bigInteger) {
        if(bigInteger == null){
            return null;
        }
        return trimLeadingZeroes(bigInteger.toByteArray());
    }



    public static byte[] integerToBytes(long value){
        BigInteger bigInteger = BigInteger.valueOf(value);
        return bigIntegerToBytes(bigInteger);
    }

    /**
     * get BigInteger from byte array, the side effect is stripping the leading zeros.
     * @param bytes
     * @return
     */
    public static BigInteger bytesToBigInteger(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        BigInteger bigInteger = new BigInteger(1,bytes);
        return bigInteger;
    }


    /**
     * Trim the leading byte
     * @param bytes
     * @param b
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
    private static BigDecimal bigIntToBigDecimal(BigInteger bgInt, int precision, int scale){
        if(bgInt == null || precision < 0 || scale < 0){
            return null;
        }
        BigDecimal decimal = new BigDecimal(bgInt);
        BigDecimal precisionDecimal = new BigDecimal(10).pow(precision);
        BigDecimal value = decimal.divide(precisionDecimal, scale, BigDecimal.ROUND_DOWN);
        return value;
    }


    /**
     * get balance of {@link BigDecimal} value.
     * @param hexString hex string of the balance.
     * @param precision the precision of the balance, with is 18 by default
     * @param scale the remain digits numbers of fractional part
     * @return the balance value which can show to the end user.
     */
    public static BigDecimal balance(String hexString, int precision, int scale){
        byte[] balBytes = toByteArray(hexString);
        if(balBytes == null){
            return null;
        }
        BigInteger balInteger = bytesToBigInteger(balBytes);
        return bigIntToBigDecimal(balInteger, precision, scale);
    }

    /**
     * Convert a decimal defaultDecimalStringToByteArray to a byte array.
     * @param amountString it is a decimal string. e.g. "42.42"
     * @return
     */
    public static byte[] defaultDecimalStringToByteArray(String amountString){
        BigDecimal amountDecimal = new BigDecimal(amountString);
        BigDecimal precisionDecimal = new BigDecimal(10).pow(18);
        BigDecimal realAmount = amountDecimal.multiply(precisionDecimal);
        return trimLeadingZeroes(realAmount.toBigInteger().toByteArray());
    }

    /**
     * Check if  contains hex prefix
     * @param input
     * @return
     */
    public static boolean containsHexPrefix(String input) {
        return (input.startsWith(Prefix.ZeroLowerX.getPrefixString())
                || input.startsWith( Prefix.VeChainX.getPrefixString()));
    }

    public static String cleanHexPrefix(String input) {
        if (containsHexPrefix(input)) {
            return input.substring(2);
        } else {
            return input;
        }
    }


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
