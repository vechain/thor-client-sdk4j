package com.vechain.thorclient.utils;

import java.math.BigDecimal;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BytesUtilsTest {

    static Logger logger = Logger.getLogger("thorclient");

    @Test
    public void testBalance() {
        String balanceHex = "0x1af38bec818ff7d52fbad8f0";
        BigDecimal balanceAmt = BlockchainUtils.amount(balanceHex, 18, 2);
        logger.info("Test BytesUtils amount");
        Assert.assertEquals("8341040001.22", balanceAmt.toString());
    }

    @Test
    public void testBlake2b() {
        String helloword = "Hello world";
        byte[] helloBytes = helloword.getBytes();
        byte[] blake2b = CryptoUtils.blake2b(helloBytes);
        String hexString = BytesUtils.toHexString(blake2b, Prefix.ZeroLowerX);
        logger.info("Blake2b:" + hexString);
        Assert.assertEquals("0xa21cf4b3604cf4b2bc53e6f88f6a4d75ef5ff4ab415f3e99aea6b61c8249c4d0", hexString);
    }

}
