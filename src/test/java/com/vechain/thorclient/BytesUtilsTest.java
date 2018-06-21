package com.vechain.thorclient;

import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.Prefix;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.util.logging.Logger;

@RunWith(JUnit4.class)
public class BytesUtilsTest {

    static Logger logger = Logger.getLogger("thorclient");


    @Test
    public void testBalance(){
        String balanceHex = "0x1af38bec818ff7d52fbad8f0";
        BigDecimal balanceAmt = BytesUtils.balance(balanceHex, 18, 2);
        logger.info("Test BytesUtils balance");
        Assert.assertEquals("8341040001.22", balanceAmt.toString());
    }

    @Test
    public void testBlake2b(){
        String helloword = "Hello world";
        byte[] helloBytes = helloword.getBytes();
        byte[] blake2b = CryptoUtils.blake2b(helloBytes);
        String hexString = BytesUtils.toHexString(blake2b, Prefix.ZeroLowerX);
        logger.info("Blake2b:" + hexString);
        Assert.assertEquals("0xa21cf4b3604cf4b2bc53e6f88f6a4d75ef5ff4ab415f3e99aea6b61c8249c4d0",hexString);
    }
}
