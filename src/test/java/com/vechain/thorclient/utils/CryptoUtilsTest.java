package com.vechain.thorclient.utils;

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

    @Test
    public void testDestroyAddress(){
        byte[] address1Bytes = BytesUtils.toByteArray( "0x0031231231231231231231231231231231231231" );
        byte[] address2Bytes = BytesUtils.toByteArray( "0x0231231231231231231231231231231231231231" );
        byte[]  content = new byte[40];
        System.arraycopy( address1Bytes, 0, content, 0, 20 );
        System.arraycopy( address2Bytes, 0, content, 20, 20 );
        byte[] address = CryptoUtils.sha256( content );

        logger.info( "hashed content:" + BytesUtils.toHexString( address, Prefix.ZeroLowerX ) );
    }

}
