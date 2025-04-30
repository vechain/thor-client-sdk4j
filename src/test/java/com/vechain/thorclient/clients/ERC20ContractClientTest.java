package com.vechain.thorclient.clients;

import com.vechain.thorclient.utils.Prefix;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.Receipt;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.ERC20Token;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

@RunWith(JUnit4.class)
public class ERC20ContractClientTest extends BaseTest {

    boolean prettyFormat = false;

    // Galactica documented at: http://localhost:8669/doc/stoplight-ui/#/paths/accounts-address/get
    // Solo tested.
    // GET http://localhost:8669/accounts/0x3e3d79163b08502a086213cd09660721740443d7
    // Accept: application/json, text/plain
    @Test
    public void testERC20GetBalance() {
        Address address = Address.fromHexString("0x3e3d79163b08502a086213cd09660721740443d7");
        Amount balance = ERC20ContractClient.getERC20Balance(address, ERC20Token.VTHO, null);
        if (balance != null) {
            logger.info("Get VTHO:" + balance.getAmount());
        }

        Assert.assertNotNull(balance);
    }

    // Galactica documented at: http://127.0.0.1:8669/doc/stoplight-ui/#/paths/accounts-*/post
    // Solo tested.
    @Test
    public void sendERC20Token() {
        // pre-seeded galactica solo account[8]
        String fromPrivateKey = "521b7793c6eb27d137b617627c6b85d57c0aa303380e9ca4e30a30302fbc6676";
        Address fromAddress = Address.fromHexString("0x062f167a905c1484de7e75b88edc7439f82117de");
        // pre-seeded galactica solo account[9]
        Address toAddress = Address.fromHexString("0x3e3d79163b08502a086213cd09660721740443d7");

        String amountFigure = "10000000";
        Amount amount = Amount.VTHO();
        amount.setDecimalAmount(amountFigure);

        Amount fromBalanceBefore = ERC20ContractClient.getERC20Balance(fromAddress, ERC20Token.VTHO, null);
        if (fromBalanceBefore != null) {
            String message = String.format(
                    "BEFORE: Account %s: VTHO %s.",
                    fromAddress.toHexString(Prefix.ZeroLowerX),
                    fromBalanceBefore.getAmount().toString()
            );
            logger.info(message);
        } else {
            Assert.fail(String.format("Account %s not found!", fromAddress.toHexString(Prefix.ZeroLowerX)));
        }
        Amount toBalanceBefore = ERC20ContractClient.getERC20Balance(toAddress, ERC20Token.VTHO, null);
        if (toBalanceBefore != null) {
            String message = String.format(
                    "BEFORE: Account %s: VTHO %s.",
                    toAddress.toHexString(Prefix.ZeroLowerX),
                    toBalanceBefore.getAmount().toString()
            );
            logger.info(message);
        } else {
            Assert.fail(String.format("Account %s not found!", toAddress.toHexString(Prefix.ZeroLowerX)));
        }

        TransferResult transferResult = ERC20ContractClient.transferERC20Token(
                new Address[]{toAddress}, new Amount[]{amount}, 1000000, (byte) 0x0, 720,
                ECKeyPair.create(fromPrivateKey));
        logger.info("sendERC20Token: " + JSON.toJSONString(transferResult, prettyFormat));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            String message = String.format("InterruptedException: %s", e.getMessage());
            logger.error(message);
            Assert.fail(message);
        }

        Receipt receipt = TransactionClient.getTransactionReceipt(transferResult.getId(), null);
        logger.info("Receipt:" + JSON.toJSONString(receipt, prettyFormat));

        Amount fromBalanceAfter = ERC20ContractClient.getERC20Balance(fromAddress, ERC20Token.VTHO, null);
        if (fromBalanceAfter != null) {
            String message = String.format(
                    "AFTER: Account %s: VTHO %s.",
                    fromAddress.toHexString(Prefix.ZeroLowerX),
                    fromBalanceAfter.getAmount().toString()
            );
            logger.info(message);
        } else {
            Assert.fail(String.format("Account %s not found!", fromAddress.toHexString(Prefix.ZeroLowerX)));
        }
        Amount toBalanceAfter = ERC20ContractClient.getERC20Balance(toAddress, ERC20Token.VTHO, null);
        if (toBalanceAfter != null) {
            String message = String.format(
                    "AFTER: Account %s: VTHO %s.",
                    toAddress.toHexString(Prefix.ZeroLowerX),
                    toBalanceAfter.getAmount().toString()
            );
            logger.info(message);
        } else {
            Assert.fail(String.format("Account %s not found!", toAddress.toHexString(Prefix.ZeroLowerX)));
        }
        Assert.assertTrue(fromBalanceAfter.getAmount().compareTo(fromBalanceBefore.getAmount()) < 0);
        Assert.assertTrue(toBalanceAfter.getAmount().compareTo(toBalanceBefore.getAmount()) > 0);
    }

}
