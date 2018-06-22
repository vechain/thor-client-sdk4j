package com.vechain.thorclient;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.utils.crypto.ECKeyPair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ECKeyPairTest extends BaseTest {

    @Test
    public void testGetAddress(){
        ECKeyPair keyPair = ECKeyPair.create( "0xc8c53657e41a8d669349fc287f57457bd746cb1fcfc38cf94d235deb2cfca81b" );
        String address = keyPair.getAddress();
        logger.info( "Address is :" + address );
        Assert.assertEquals("0xa5e255d4c65af201b97210ff4cd9521a46427654", address);
    }
}
