package com.vechain.thorclient.clients;

import java.io.IOException;

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
	public void sendERC20Token() {
		String toAmount = "10000000";
		String toAddress = "0x733b7269443c70de16bbf9b0615307884bcc5636";
		Address address = Address.fromHexString(toAddress);
		Amount balance = ERC20ContractClient.getERC20Balance(address, ERC20Token.VTHO, null);
		if (balance != null) {
			logger.info("Get vtho before:" + balance.getAmount());
		}

		Amount amount = Amount.VTHO();
		amount.setDecimalAmount(toAmount);
		TransferResult result = ERC20ContractClient.transferERC20Token(
				new Address[] { Address.fromHexString(toAddress) }, new Amount[] { amount }, 1000000, (byte) 0x0, 720,
				ECKeyPair.create(privateKey));
		logger.info("sendERC20Token: " + JSON.toJSONString(result));

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}

		Receipt receipt = TransactionClient.getTransactionReceipt(result.getId(), null);
		logger.info("Receipt:" + JSON.toJSONString(receipt));

		Amount balance2 = ERC20ContractClient.getERC20Balance(address, ERC20Token.VTHO, null);
		if (balance2 != null) {
			logger.info("Get vtho after:" + balance2.getAmount());
		}
		Assert.assertEquals(0,
				amount.getAmount().subtract(balance2.getAmount().subtract(balance.getAmount())).longValue());

	}

}
