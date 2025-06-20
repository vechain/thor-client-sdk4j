package com.vechain.thorclient.utils;

import java.math.BigInteger;
import java.util.Arrays;

public class BigIntegerUtils {

    public static final byte[] EMPTY = new byte[]{ };

    /**
     * Converts a BigInteger value to a byte[] suitable for RLP encoding
     * @param value big integer value
     * @return byte[] suitable for rlp encoding
     */
    public static byte[] convertToRLPByteArray(BigInteger value) {
        // RLP encoding only supports positive integer values
        if (value.signum() < 1) {
            return EMPTY;
        } else {
            byte[] bytes = value.toByteArray();
            if (bytes[0] == 0) {  // remove leading zero
                return Arrays.copyOfRange(bytes, 1, bytes.length);
            } else {
                return bytes;
            }
        }
    }
}
