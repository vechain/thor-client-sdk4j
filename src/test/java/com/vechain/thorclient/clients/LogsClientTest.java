package com.vechain.thorclient.clients;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.alibaba.fastjson.JSONObject;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.Block;
import com.vechain.thorclient.core.model.blockchain.FilteredLogEvent;
import com.vechain.thorclient.core.model.blockchain.FilteredTransferEvent;
import com.vechain.thorclient.core.model.blockchain.LogFilter;
import com.vechain.thorclient.core.model.blockchain.Options;
import com.vechain.thorclient.core.model.blockchain.Order;
import com.vechain.thorclient.core.model.blockchain.Range;
import com.vechain.thorclient.core.model.blockchain.TransferredFilter;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.ERC20Contract;
import com.vechain.thorclient.core.model.clients.base.AbiDefinition;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.Prefix;

@RunWith(JUnit4.class)
public class LogsClientTest extends BaseTest {

	@Test
	public void testFilterEvents() throws ClientArgumentException {
		Block block = BlockClient.getBlock(null);

		List<String> eventsTransferInputs = new ArrayList<String>();
		eventsTransferInputs.add("address");
		eventsTransferInputs.add("address");
		eventsTransferInputs.add("uint256");
		AbiDefinition abiDefinition = ERC20Contract.defaultERC20Contract.findAbiDefinition("Transfer", "event",
				eventsTransferInputs);
		String abiMethodHexString = BytesUtils.toHexString(abiDefinition.getBytesMethodHashed(), Prefix.ZeroLowerX);
		logger.info("abi Transfer:" + abiMethodHexString);
		LogFilter logFilter = LogFilter.createFilter(Range.createBlockRange(0, Long.parseLong(block.getNumber())),
				Options.create(0, 10));
		logFilter.setOrder(Order.DESC.getValue());

		logFilter.addTopicSet(Address.VTHO_Address.toHexString(null), abiMethodHexString,
				"0x000000000000000000000000" + fromAddress.substring(2), null, null, null);
		logFilter.addTopicSet(Address.VTHO_Address.toHexString(null), abiMethodHexString, null,
				"0x000000000000000000000000" + fromAddress.substring(2), null, null);

		ArrayList<FilteredLogEvent> filteredEvents = LogsClient.getFilteredLogEvents(logFilter);
		logger.info("filteredEvents:{}", JSONObject.toJSONString(filteredEvents));
	}

	@Test
	public void testTransferLogs() throws ClientArgumentException {
		Block block = BlockClient.getBlock(null);

		TransferredFilter transferredFilter = TransferredFilter
				.createFilter(Range.createBlockRange(0, Long.parseLong(block.getNumber())), Options.create(0, 10));

		transferredFilter.addTransferCriteria(null, Address.fromHexString("0x733b7269443c70de16bbf9b0615307884bcc5636"),
				null);
		transferredFilter.addTransferCriteria(null, null,
				Address.fromHexString("0x90840190af69dbaac6d1398e521cfb64b2f33fac"));
		transferredFilter.setOrder(Order.DESC.getValue());
		ArrayList<FilteredTransferEvent> transferLogs = LogsClient.getFilteredTransferLogs(transferredFilter);
		logger.info("transferLogs:{}", JSONObject.toJSONString(transferLogs));
	}

}
