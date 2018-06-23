package com.vechain.thorclient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

@RunWith(JUnit4.class)
public class ECKeyPairTest extends BaseTest {

    @Test
    public void testGetAddress() {
        String privateKey = this.getEnvironment().get(PRIVATE_KEY);
        ECKeyPair keyPair = ECKeyPair.create(privateKey);
        String address = keyPair.getAddress();
        logger.info("Address is : " + address);
    }
}
