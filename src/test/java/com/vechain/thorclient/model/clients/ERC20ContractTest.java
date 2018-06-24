package com.vechain.thorclient.model.clients;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.clients.ERC20Contract;
import com.vechain.thorclient.core.model.clients.base.AbiDefinition;

@RunWith(JUnit4.class)
public class ERC20ContractTest extends BaseTest {

    @Test
    public void testERC20BalanceOfCode(){
        ERC20Contract  contract = new ERC20Contract();
        AbiDefinition abiDefinition = contract.findAbiDefinition( "balanceOf" );
        String hexString = abiDefinition.getHexMethodCodeNoPefix();
        logger.info( "balanceOf abi:" + hexString );

        Assert.assertEquals("70a08231", hexString);
    }

    @Test
    public void testERC20TransferCode(){
        ERC20Contract  contract = new ERC20Contract();
        AbiDefinition abiDefinition = contract.findAbiDefinition( "transfer" );
        String hexString = abiDefinition.getHexMethodCodeNoPefix();
        logger.info( "transfer abi:" + hexString );

        Assert.assertEquals("a9059cbb", hexString);
    }

}
