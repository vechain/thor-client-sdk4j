package com.vechain.thorclient.utils;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.utils.crypto.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ExtendedKeyTest extends BaseTest{

    @Test
    public void testHardenDerivedFromPrivateKey(){

    }

    @Test
    public void testNormalDerivedFromPrivateKey(){

    }

    @Test
    public void testNormalDerivedFromPublicKey(){
        String ecPub = "0x0227128ce999f0f760d1fcecc6d81a94e32f1ef12fd81a21f0d6b26fabb2729412";
        String chainCode = "0xb4d37eb4d87e8e6502d35899ae81056bdc40d5ea7531bef1ff6ace95c86fd0c1";
        ECPublicKey publicKey = new ECPublicKey( BytesUtils.toByteArray( ecPub ));
        byte[] chainCodeBytes = BytesUtils.toByteArray( chainCode );

        ExtendedKey key = new ExtendedKey(publicKey, chainCodeBytes, 0,0, 0 );
        try {
            Key child = key.derived( 0x00c04133 ).getMaster();
            byte[] pub = child.getRawPublicKey( false );
            logger.info( "child pubkey :" + BytesUtils.toHexString( pub, Prefix.ZeroLowerX ) );
            Assert.assertEquals("0x04a023b109ea129e1526cac7384ef17ef2092182499f94e80973608f9985dea45d0df740616133bb50c1d8ed30aa4945c5f564f4657cb5ec2b81d33ca5f9ce946e",
                    BytesUtils.toHexString( pub, Prefix.ZeroLowerX ));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


}
