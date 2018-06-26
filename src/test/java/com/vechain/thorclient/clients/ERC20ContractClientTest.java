package com.vechain.thorclient.clients;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.utils.crypto.ECKeyPair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.ERC20Token;

@RunWith(JUnit4.class)
public class ERC20ContractClientTest extends BaseTest {
    @Test
    public void testERC20GetBalance() throws IOException {
        Address address = Address.fromHexString(fromAddress);
        Amount balance = ERC20ContractClient.getERC20Balance(address, ERC20Token.VTHO, null);
        if (balance != null) {
            logger.info("Get vtho:" + balance.getAmount());
        }

        Assert.assertNotNull(balance);
    }

    @Test
    public void sendERC20Token(){
        Amount amount = Amount.VTHO();
        amount.setDecimalAmount( "122.33" );
        TransferResult result = ERC20ContractClient.transferERC20Token(
                new Address[]{Address.fromHexString( "VXc71ADC46c5891a8963Ea5A5eeAF578E0A2959779" )} ,
                new Amount[]{amount},
                TransactionClient.ContractGasLimit, (byte) 0x1, 720, ECKeyPair.create(privateKey)
                );
        logger.info( "sendERC20Token: " + JSON.toJSONString(result) );
    }

}
