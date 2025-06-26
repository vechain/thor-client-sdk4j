package com.vechain.thorclient.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vechain.thorclient.utils.Prefix;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.Receipt;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.ERC20Token;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

import java.math.BigInteger;

@RunWith(JUnit4.class)
public class ERC20ContractClientTest extends BaseTest {

    final boolean prettyFormat = isPretty();

    final ObjectMapper objectMapper = new ObjectMapper();

    final ObjectWriter writer = prettyFormat ? objectMapper.writerWithDefaultPrettyPrinter() : objectMapper.writer();

    // Galactica documented at:
    // http://localhost:8669/doc/stoplight-ui/#/paths/accounts-address/get
    // Solo tested.
    // GET http://localhost:8669/accounts/0x3e3d79163b08502a086213cd09660721740443d7
    // Accept: application/json, text/plain

    @Test
    public void testGetERC20Balance() {
        // Set in `config.properties`.
        final Address address = Address
                .fromHexString(System.getProperty("ERC20ContractClientTest.testGetERC20Balance"));
        final Amount balance = ERC20ContractClient.getERC20Balance(address, ERC20Token.VTHO, null);
        if (balance != null) {
            logger.info("Get VTHO: {}", balance.getAmount());
        }

        Assert.assertNotNull(balance);
    }

    // Galactica documented at:
    // http://127.0.0.1:8669/doc/stoplight-ui/#/paths/accounts-*/post
    // Solo tested.

    @Test
    public void testTransferERC20Token() throws JsonProcessingException {
        // Set in `config.properties`.
        final String fromPrivateKey = System.getProperty("ERC20ContractClientTest.testTransferERC20Token.fromKey");
        final Address fromAddress = Address.fromBytes(ECKeyPair.create(fromPrivateKey).getRawAddress());
        // Set in `config.properties`.
        final Address toAddress = Address
                .fromHexString(System.getProperty("ERC20ContractClientTest.testTransferERC20Token.toAddress"));
        final String amountFigure = "10000";
        final Amount amount = Amount.VTHO();
        amount.setDecimalAmount(amountFigure);

        final Amount fromBalanceBefore = ERC20ContractClient.getERC20Balance(fromAddress, ERC20Token.VTHO, null);
        if (fromBalanceBefore != null) {
            final String message = String.format(
                    "BEFORE: Account %s: VTHO %s.",
                    fromAddress.toHexString(Prefix.ZeroLowerX),
                    fromBalanceBefore.getAmount().toString());
            logger.info(message);
        } else {
            Assert.fail(String.format("Account %s not found!", fromAddress.toHexString(Prefix.ZeroLowerX)));
        }
        final Amount toBalanceBefore = ERC20ContractClient.getERC20Balance(toAddress, ERC20Token.VTHO, null);
        if (toBalanceBefore != null) {
            final String message = String.format(
                    "BEFORE: Account %s: VTHO %s.",
                    toAddress.toHexString(Prefix.ZeroLowerX),
                    toBalanceBefore.getAmount().toString());
            logger.info(message);
        } else {
            Assert.fail(String.format("Account %s not found!", toAddress.toHexString(Prefix.ZeroLowerX)));
        }

        final TransferResult transferResult = ERC20ContractClient.transferERC20Token(
                new Address[] { toAddress },
                new Amount[] { amount },
                1000000,
                (byte) 0x0,
                720,
                ECKeyPair.create(fromPrivateKey));
        logger.info("transferERC20Token: {}", writer.writeValueAsString(transferResult));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            final String message = String.format("InterruptedException: %s", e.getMessage());
            logger.error(message);
            Assert.fail(message);
        }

        final Receipt receipt = TransactionClient.getTransactionReceipt(transferResult.getId(), null);
        logger.info("Receipt: {}", writer.writeValueAsString(receipt));

        final Amount fromBalanceAfter = ERC20ContractClient.getERC20Balance(fromAddress, ERC20Token.VTHO, null);
        if (fromBalanceAfter != null) {
            final String message = String.format(
                    "AFTER: Account %s: VTHO %s.",
                    fromAddress.toHexString(Prefix.ZeroLowerX),
                    fromBalanceAfter.getAmount().toString());
            logger.info(message);
        } else {
            Assert.fail(String.format("Account %s not found!", fromAddress.toHexString(Prefix.ZeroLowerX)));
        }
        final Amount toBalanceAfter = ERC20ContractClient.getERC20Balance(toAddress, ERC20Token.VTHO, null);
        if (toBalanceAfter != null) {
            String message = String.format(
                    "AFTER: Account %s: VTHO %s.",
                    toAddress.toHexString(Prefix.ZeroLowerX),
                    toBalanceAfter.getAmount().toString());
            logger.info(message);
        } else {
            Assert.fail(String.format("Account %s not found!", toAddress.toHexString(Prefix.ZeroLowerX)));
        }
        Assert.assertTrue(fromBalanceAfter.getAmount().compareTo(fromBalanceBefore.getAmount()) < 0);
        Assert.assertTrue(toBalanceAfter.getAmount().compareTo(toBalanceBefore.getAmount()) > 0);
    }

    // Tests ERC20 token transfer using dynamic fee (EIP-1559) transaction
    @Test
    public void testTransferERC20TokenWithDynamicFee() throws JsonProcessingException {
        // Set in `config.properties`.
        final String fromPrivateKey = System.getProperty("ERC20ContractClientTest.testTransferERC20Token.fromKey");
        final Address fromAddress = Address.fromBytes(ECKeyPair.create(fromPrivateKey).getRawAddress());
        // Set in `config.properties`.
        final Address toAddress = Address
                .fromHexString(System.getProperty("ERC20ContractClientTest.testTransferERC20Token.toAddress"));
        final String amountFigure = "10000";
        final Amount amount = Amount.VTHO();
        amount.setDecimalAmount(amountFigure);

        final Amount fromBalanceBefore = ERC20ContractClient.getERC20Balance(fromAddress, ERC20Token.VTHO, null);
        if (fromBalanceBefore != null) {
            final String message = String.format(
                    "BEFORE: Account %s: VTHO %s.",
                    fromAddress.toHexString(Prefix.ZeroLowerX),
                    fromBalanceBefore.getAmount().toString());
            logger.info(message);
        } else {
            Assert.fail(String.format("Account %s not found!", fromAddress.toHexString(Prefix.ZeroLowerX)));
        }
        final Amount toBalanceBefore = ERC20ContractClient.getERC20Balance(toAddress, ERC20Token.VTHO, null);
        if (toBalanceBefore != null) {
            final String message = String.format(
                    "BEFORE: Account %s: VTHO %s.",
                    toAddress.toHexString(Prefix.ZeroLowerX),
                    toBalanceBefore.getAmount().toString());
            logger.info(message);
        } else {
            Assert.fail(String.format("Account %s not found!", toAddress.toHexString(Prefix.ZeroLowerX)));
        }

        BigInteger maxFeePerGas = new BigInteger("5000000000"); // 5 gwei
        BigInteger maxPriorityFeePerGas = new BigInteger("1000000000"); // 1 gwei

        final TransferResult transferResult = ERC20ContractClient.transferERC20TokenEIP1559(
                new Address[] { toAddress },
                new Amount[] { amount },
                1000000,
                maxFeePerGas,
                maxPriorityFeePerGas,
                720,
                ECKeyPair.create(fromPrivateKey));
        logger.info("transferERC20Token: {}", writer.writeValueAsString(transferResult));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            final String message = String.format("InterruptedException: %s", e.getMessage());
            logger.error(message);
            Assert.fail(message);
        }

        final Receipt receipt = TransactionClient.getTransactionReceipt(transferResult.getId(), null);
        logger.info("Receipt: {}", writer.writeValueAsString(receipt));

        final Amount fromBalanceAfter = ERC20ContractClient.getERC20Balance(fromAddress, ERC20Token.VTHO, null);
        if (fromBalanceAfter != null) {
            final String message = String.format(
                    "AFTER: Account %s: VTHO %s.",
                    fromAddress.toHexString(Prefix.ZeroLowerX),
                    fromBalanceAfter.getAmount().toString());
            logger.info(message);
        } else {
            Assert.fail(String.format("Account %s not found!", fromAddress.toHexString(Prefix.ZeroLowerX)));
        }
        final Amount toBalanceAfter = ERC20ContractClient.getERC20Balance(toAddress, ERC20Token.VTHO, null);
        if (toBalanceAfter != null) {
            String message = String.format(
                    "AFTER: Account %s: VTHO %s.",
                    toAddress.toHexString(Prefix.ZeroLowerX),
                    toBalanceAfter.getAmount().toString());
            logger.info(message);
        } else {
            Assert.fail(String.format("Account %s not found!", toAddress.toHexString(Prefix.ZeroLowerX)));
        }
        Assert.assertTrue(fromBalanceAfter.getAmount().compareTo(fromBalanceBefore.getAmount()) < 0);
        Assert.assertTrue(toBalanceAfter.getAmount().compareTo(toBalanceBefore.getAmount()) > 0);
    }
}
