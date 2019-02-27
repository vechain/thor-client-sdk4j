package com.vechain.thorclient.utils;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.Prefix;
import com.vechain.thorclient.utils.crypto.ECKeyPair;
import com.vechain.thorclient.utils.crypto.Key;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.security.interfaces.ECKey;

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

    @Test
    public void testRecover(){
        byte[] signData = BytesUtils.toByteArray( "0x2084e6a798c87edab61f6f726475f94e4265a654c77c5c4e43047203ba9b01f20057df1f8dd0c5a1ecdc08e97a9aa87befe22fa93fe7ca94a65bc39e0b686ddc00" );
        byte[] message = BytesUtils.toByteArray( "0x1073a4d83294c771d16af16fe287fb734f3521f01575557ddcac7e3e62019ba8" );
        Key keyPair = CryptoUtils.recoverPublicKey( message, signData );
        logger.info( "Address:" + keyPair.getAddress());
        //Assert.assertEquals(keyPair.getAddress(), );
    }
}
