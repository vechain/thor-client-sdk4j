package com.vechain.thorclient.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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

    final ObjectMapper objectMapper = new ObjectMapper();

    final ObjectWriter writer = prettyFormat ? objectMapper.writerWithDefaultPrettyPrinter() : objectMapper.writer();


    @Test
    // Galactica documented at: http://localhost:8669/doc/stoplight-ui/#/paths/accounts-address/get.
    // Solo tested.
    public void testGetAccountInfo() throws JsonProcessingException {

        // Set in `config.properties`.
        final Address address = Address.fromHexString(System.getProperty("AccountClientTest.testGetAccountInfo"));
        final Account account = AccountClient.getAccountInfo(address, null);
        logger.info("account info:" + writer.writeValueAsString(account));
                logger.info("VET:" + account.VETBalance().getAmount() + " Energy:" + account.energyBalance().getAmount());
        Assert.assertNotNull(account);
    }

    // Galactica documented at: http://localhost:8669/doc/stoplight-ui/#/paths/accounts-address-storage-key/get.
    // Solo tested.
    @Test
    public void testGetStorageAt() throws JsonProcessingException {
        // Set in `config.properties`.
        byte[] address = BytesUtils.toByteArray(System.getProperty("AccountClientTest.testGetStorageAt"));
        StorageKey key = StorageKey.create(4, address);
        StorageData data = AccountClient.getStorageAt(Address.VTHO_Address, key, null);
        logger.info("Storage At:" + writer.writeValueAsString(data));
        Assert.assertNotNull(data);
    }


    @Test
    // Galactica documented at: http://localhost:8669/doc/stoplight-ui/#/paths/accounts-address--code/get.
    // Solo tested.
    public void testGetCode() throws JsonProcessingException {
        AccountCode code = AccountClient.getAccountCode(Address.VTHO_Address, null);
        logger.info("code:" + writer.writeValueAsString(code));
        Assert.assertNotNull(code);
    }

}
