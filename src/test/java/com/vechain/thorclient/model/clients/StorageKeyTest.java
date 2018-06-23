package com.vechain.thorclient.model.clients;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.clients.StorageKey;
import com.vechain.thorclient.utils.BytesUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StorageKeyTest extends BaseTest {

    @Test
    public void testStorageKey() {
        byte[] address = BytesUtils.toByteArray(fromAddress);
        StorageKey key = StorageKey.create(4, address);
        logger.info("key hex:" + key.hexKey());
    }

}
