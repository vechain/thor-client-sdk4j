package com.vechain.thorclient.clients;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.Account;
import com.vechain.thorclient.core.model.blockchain.AccountCode;
import com.vechain.thorclient.core.model.blockchain.StorageData;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.StorageKey;
import com.vechain.thorclient.utils.BytesUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AccountClientTest extends BaseTest {

    final boolean prettyFormat = isPretty();

    @Test
    // Galactica documented at: http://localhost:8669/doc/stoplight-ui/#/paths/accounts-address/get.
    // Solo tested.
    public void testGetAccountInfo() {
        // Set in `config.properties`.
        final Address address = Address.fromHexString(System.getProperty("AccountClientTest.testGetAccountInfo"));
        final Account account = AccountClient.getAccountInfo(address, null);
        logger.info("account info:" + JSON.toJSONString(account, prettyFormat));
        logger.info("VET:" + account.VETBalance().getAmount() + " Energy:" + account.energyBalance().getAmount());
        Assert.assertNotNull(account);
    }

    // Galactica documented at: http://localhost:8669/doc/stoplight-ui/#/paths/accounts-address-storage-key/get.
    // Solo tested.
    @Test
    public void testGetStorageAt() {
        // Set in `config.properties`.
        byte[] address = BytesUtils.toByteArray(System.getProperty("AccountClientTest.testGetStorageAt"));
        StorageKey key = StorageKey.create(4, address);
        StorageData data = AccountClient.getStorageAt(Address.VTHO_Address, key, null);
        logger.info("Storage At:" + JSON.toJSONString(data, prettyFormat));
        Assert.assertNotNull(data);
    }


    @Test
    // Galactica documented at: http://localhost:8669/doc/stoplight-ui/#/paths/accounts-address--code/get.
    // Solo tested.
    public void testGetCode() {
        AccountCode code = AccountClient.getAccountCode(Address.VTHO_Address, null);
        logger.info("code:" + JSON.toJSONString(code, prettyFormat));
        Assert.assertNotNull(code);
    }

}
