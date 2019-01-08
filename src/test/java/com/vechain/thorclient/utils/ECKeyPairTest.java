package com.vechain.thorclient.utils;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.utils.crypto.ECDSASign;
import com.vechain.thorclient.utils.crypto.ECKeyPair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ECKeyPairTest extends BaseTest {

    @Test
    public void testGetAddress() {
        String privateKey = this.getEnvironment().get(PRIVATE_KEY);
        ECKeyPair keyPair = ECKeyPair.create(privateKey);
        String address = keyPair.getAddress();
        logger.info("Address is : " + address);
        Assert.assertEquals( "0x7567d83b7b8d80addcb281a71d54fc7b3364ffed", address );
    }

    @Test
    public void testSign(){
        String privateKey = this.getEnvironment().get(PRIVATE_KEY);
        ECKeyPair keyPair = ECKeyPair.create(privateKey);
        String helloWorld = "HelloWorld";
        byte[] messageHash = CryptoUtils.blake2b(helloWorld.getBytes());
        ECDSASign.SignatureData data = ECDSASign.signMessage( messageHash, keyPair, false );
        logger.info( "Signature:" + BytesUtils.toHexString( data.toByteArray(), Prefix.ZeroLowerX ) );

    }


    @Test
    public void testGetCompressedRawPublicKey(){
        String privateKey = this.getEnvironment().get(PRIVATE_KEY);
        ECKeyPair keyPair = ECKeyPair.create(privateKey);
        String pubkeyHex = BytesUtils.toHexString(keyPair.getRawPublicKey(true), Prefix.ZeroLowerX);
        logger.info("PublicKey is : " + pubkeyHex);
        Assert.assertEquals("0x0265e790f6065164e2f610297b5358b6c474f999fb5b4d2574fcaffccb59342c1f", pubkeyHex);
    }

    @Test
    public void testGetDecompressedRawPublicKey(){
        String privateKey = this.getEnvironment().get(PRIVATE_KEY);
        ECKeyPair keyPair = ECKeyPair.create(privateKey);
        String pubkeyHex = BytesUtils.toHexString(keyPair.getRawPublicKey(false), Prefix.ZeroLowerX);
        logger.info("PublicKey is : " + pubkeyHex);
        Assert.assertEquals("0x0465e790f6065164e2f610297b5358b6c474f999fb5b4d2574fcaffccb59342c1f6f28f0b684ec97946da65cd08a1b9fc276f79d90caed80e56456cebbc165938e" ,
        pubkeyHex);
    }

}
