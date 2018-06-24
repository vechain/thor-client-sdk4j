package com.vechain.thorclient.model.clients;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.clients.Amount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AmountTest extends BaseTest {

    @Test
    public void testAmount(){
        String hex = "0x000000000000000000000000000000000000000000000000a688906bd8b00000";
        Amount amount = Amount.VTHO();
        amount.setHexAmount( hex );
        logger.info( "Decimal amount:" + amount.getAmount().toString() );
    }

}
