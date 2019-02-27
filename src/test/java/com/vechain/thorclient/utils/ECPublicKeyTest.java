package com.vechain.thorclient.utils;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.utils.crypto.ECKeyPair;
import com.vechain.thorclient.utils.crypto.ECPublicKey;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sun.rmi.runtime.Log;

import java.math.BigInteger;

@RunWith(JUnit4.class)
public class ECPublicKeyTest extends BaseTest {

    @Test
    public void testCompressedPublicKey(){
        byte[] decompressPublicKey = BytesUtils.toByteArray( "0x0465e790f6065164e2f610297b5358b6c474f999fb5b4d2574fcaffccb59342c1f6f28f0b684ec97946da65cd08a1b9fc276f79d90caed80e56456cebbc165938e" );
        ECPublicKey publicKey = new ECPublicKey( decompressPublicKey);
        byte[] compressedPubKey = publicKey.getRawPublicKey( true );
        String compressedPubKeyHex = BytesUtils.toHexString( compressedPubKey, Prefix.ZeroLowerX );
        logger.info( "compressed pubKey:" + compressedPubKeyHex );
        Assert.assertEquals("0x0265e790f6065164e2f610297b5358b6c474f999fb5b4d2574fcaffccb59342c1f", compressedPubKeyHex);
    }

    @Test
    public void testUncompressedPublicKey(){
        byte[] compressedPubKey = BytesUtils.toByteArray( "0x0265e790f6065164e2f610297b5358b6c474f999fb5b4d2574fcaffccb59342c1f" );
        ECPublicKey publicKey = new ECPublicKey( compressedPubKey);
        byte[] decompressedPub = publicKey.getRawPublicKey( false );
        String decompressedPubKeyHex = BytesUtils.toHexString( decompressedPub, Prefix.ZeroLowerX );
        logger.info( "decompressed pubKey:" + decompressedPubKeyHex );
        Assert.assertEquals("0x0465e790f6065164e2f610297b5358b6c474f999fb5b4d2574fcaffccb59342c1f6f28f0b684ec97946da65cd08a1b9fc276f79d90caed80e56456cebbc165938e", decompressedPubKeyHex);
    }

    @Test
    public void testGetAddress(){
        String publicKeyHex = "0x0465e790f6065164e2f610297b5358b6c474f999fb5b4d2574fcaffccb59342c1f6f28f0b684ec97946da65cd08a1b9fc276f79d90caed80e56456cebbc165938e";
        byte[] pubKeyBytes = BytesUtils.toByteArray( publicKeyHex );

        ECPublicKey ecPublicKey = new ECPublicKey(pubKeyBytes);

        logger.info( "Address: " + ecPublicKey.getAddress() );
        Assert.assertEquals( "0x7567d83b7b8d80addcb281a71d54fc7b3364ffed" ,  ecPublicKey.getAddress() );
    }

    @Test
    public void testGetPublickey(){
        String privateKey = this.getEnvironment().get(PRIVATE_KEY);
        ECKeyPair keyPair = ECKeyPair.create(privateKey);
        keyPair.getRawPublicKey( false );
        String address = keyPair.getAddress();
        logger.info( "Address: {}", address );
        String publicKeyHex = "0x0465e790f6065164e2f610297b5358b6c474f999fb5b4d2574fcaffccb59342c1f6f28f0b684ec97946da65cd08a1b9fc276f79d90caed80e56456cebbc165938e";
        byte[] pubKeyBytes = BytesUtils.toByteArray( publicKeyHex );
        ECPublicKey ecPublicKey = new ECPublicKey(pubKeyBytes);
        logger.info( "public key:" + ecPublicKey.getPublicKey().toString() );
        Assert.assertTrue( keyPair.getPublicKey().equals( ecPublicKey.getPublicKey() ) );
    }

    @Test
    public void testIntegerPublicKey(){
        String privateKey = this.getEnvironment().get(PRIVATE_KEY);
        ECKeyPair keyPair = ECKeyPair.create(privateKey);
        BigInteger pubKeyInt = keyPair.getPublicKey();
        ECPublicKey ecPublicKey = new ECPublicKey(pubKeyInt);
        byte[] rawPub = ecPublicKey.getRawPublicKey( false );
        logger.info( "public key:" + BytesUtils.toHexString( rawPub, Prefix.ZeroLowerX ) );
        logger.info( "address:" + ecPublicKey.getAddress() );
        Assert.assertEquals( ecPublicKey.getAddress(), "0x7567d83b7b8d80addcb281a71d54fc7b3364ffed" );
    }
}
