package com.vechain.thorclient.clients;

import java.io.IOException;

import com.vechain.thorclient.core.model.clients.Revision;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.Account;
import com.vechain.thorclient.core.model.blockchain.AccountCode;
import com.vechain.thorclient.core.model.blockchain.StorageData;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.StorageKey;
import com.vechain.thorclient.utils.BytesUtils;

@RunWith(JUnit4.class)
public class AccountClientTest extends BaseTest {

    @Test
    public void testGetAccountInfo() throws IOException {
        Address address = Address.fromHexString("0x3831Fc6ecF6C88D171e6eF3E2F291fE100925617");
        Account account = AccountClient.getAccountInfo(address, Revision.create( 296759 ));
        logger.info("account info:" + JSON.toJSONString(account));
        logger.info("VET:" + account.VETBalance().getAmount() + " Energy:" + account.energyBalance().getAmount());
        Assert.assertNotNull(account);
    }

    @Test
    public void testGetStorageAt() throws IOException {
        byte[] address = BytesUtils.toByteArray(fromAddress);
        StorageKey key = StorageKey.create(4, address);
        StorageData data = AccountClient.getStorageAt(Address.VTHO_Address, key, null);
        logger.info("Storage At:" + JSON.toJSONString(data));
        Assert.assertNotNull(data);
    }

    @Test
    public void testGetCodeTest() throws IOException {
        Address tokenAddr = Address.VTHO_Address;
        AccountCode code = AccountClient.getAccountCode(tokenAddr, null);
        logger.info("code:" + JSON.toJSONString(code));
        Assert.assertNotNull(code);
    }

}
