package com.vechain.thorclient.clients;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.ERC20Token;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class ERC20ContractClientTest extends BaseTest{
    @Test
    public void testERC20GetBalance() throws IOException {
        Address address = Address.fromHexString( MyAccountAddr );
        Amount balance = ERC20ContractClient.getERC20Balance(address, ERC20Token.VTHO, null );
        if(balance != null){
            logger.info( "Get vtho:" + balance.getAmount());
        }

        Assert.assertNotNull(balance);
    }


}
