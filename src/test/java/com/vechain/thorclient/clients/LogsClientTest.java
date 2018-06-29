package com.vechain.thorclient.clients;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.alibaba.fastjson.JSONObject;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.EventFilter;
import com.vechain.thorclient.core.model.blockchain.FilteredEvent;
import com.vechain.thorclient.core.model.blockchain.FilteredTransfer;
import com.vechain.thorclient.core.model.blockchain.Options;
import com.vechain.thorclient.core.model.blockchain.Order;
import com.vechain.thorclient.core.model.blockchain.Range;
import com.vechain.thorclient.core.model.blockchain.TransferFilter;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;

@RunWith(JUnit4.class)
public class LogsClientTest extends BaseTest {

	@Test
	public void testFilterEvents() throws ClientArgumentException {
		EventFilter filter = EventFilter.createFilter(Range.createBlockRange(24125, 27125), Options.create(0, 10));
		ArrayList<?> filteredEvents = LogsClient.filterEvents(filter, Order.DESC, null);
		Assert.assertNotEquals(0, filteredEvents.size());
		logger.info("filteredEvents:" + filteredEvents.toString());
		for (Object object : filteredEvents) {
			logger.info("filteredEvent:" + object.toString());
			FilteredEvent aFilteredEvent = JSONObject.parseObject(object.toString(), FilteredEvent.class);
			Assert.assertNotNull(aFilteredEvent.getData());
			Assert.assertNotNull(aFilteredEvent.getMeta());
			Assert.assertNotNull(aFilteredEvent.getTopics());
		}
	}

	@Test
	public void testTransferLogs() throws ClientArgumentException {
		TransferFilter filter = TransferFilter.createFilter(Range.createBlockRange(0, 27125), Options.create(0, 10));
		filter.addAddressSet(null, Address.fromHexString("0xe59d475abe695c7f67a8a2321f33a856b0b4c71d"), null);
		filter.addAddressSet(null, null, Address.fromHexString("0xe59d475abe695c7f67a8a2321f33a856b0b4c71d"));
		
		ArrayList<?> transferLogs = LogsClient.filterTransferLogs(filter, Order.DESC);
		logger.info("transferLogs:" + transferLogs.toString());
		for (Object object : transferLogs) {
			logger.info("transferLog:" + object.toString());
			FilteredTransfer aFilteredTransfer = JSONObject.parseObject(object.toString(), FilteredTransfer.class);
			Assert.assertNotNull(aFilteredTransfer.getRecipient());
			Assert.assertNotNull(aFilteredTransfer.getMeta());
			Assert.assertNotNull(aFilteredTransfer.getAmount());
			Assert.assertNotNull(aFilteredTransfer.getSender());
		}
	}

}
