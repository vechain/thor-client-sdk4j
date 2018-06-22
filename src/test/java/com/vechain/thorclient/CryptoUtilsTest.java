package com.vechain.thorclient;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.Prefix;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CryptoUtilsTest extends BaseTest {

    @Test
    public void testKeccak(){
        byte[] hash = CryptoUtils.keccak256( "balanceOf(address)".getBytes() );
        logger.info( "hash:" + BytesUtils.toHexString( hash, Prefix.ZeroLowerX ) );
    }

}
