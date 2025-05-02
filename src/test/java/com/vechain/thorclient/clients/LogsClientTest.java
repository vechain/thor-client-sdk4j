package com.vechain.thorclient.clients;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.alibaba.fastjson.JSONObject;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.*;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.ERC20Contract;
import com.vechain.thorclient.core.model.clients.base.AbiDefinition;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.Prefix;

@RunWith(JUnit4.class)
public class LogsClientTest extends BaseTest {

    final boolean prettyFormat = isPretty();

    // Galactica documented at: http://127.0.0.1:8669/doc/stoplight-ui/#/paths/logs-event/post
    // Solo tested.
    // POST http://127.0.0.1:8669/logs/event
    // Accept: application/json, text/plain
    // Content-Type: application/json
    //
    // {
    //   "range": {
    //     "unit": "block",
    //     "from": 0,
    //     "to": 10
    //   },
    //   "options": {
    //     "offset": 0,
    //     "limit": 100,
    //     "includeIndexes": true
    //   },
    //   "criteriaSet": [
    //     {
    //       "address": "0x0000000000000000000000000000506172616d73",
    //       "topic0": "0x28e3246f80515f5c1ed987b133ef2f193439b25acba6a5e69f219e896fc9d179"
    //     }
    //   ],
    //   "order": "desc"
    //}
    @Test
    public void testGetFilteredLogEvents() throws ClientArgumentException {
        final String address = System.getProperty("LogsClientTest.testGetFilteredLogEvents.address");
        final String topic0 = System.getProperty("LogsClientTest.testGetFilteredLogEvents.topic0");
        final Block block = BlockClient.getBlock(null);
        final List<String> eventsTransferInputs = new ArrayList<>();
        eventsTransferInputs.add("address");
        eventsTransferInputs.add("address");
        eventsTransferInputs.add("uint256");
        final AbiDefinition abiDefinition = ERC20Contract.defaultERC20Contract.findAbiDefinition(
                "Transfer",
                "event",
                eventsTransferInputs
        );
        final String abiMethodHexString = BytesUtils.toHexString(abiDefinition.getBytesMethodHashed(), Prefix.ZeroLowerX);
        logger.info("abi Transfer:" + abiMethodHexString);
        final LogFilter logFilter = LogFilter.createFilter(
                Range.createBlockRange(0, Long.parseLong(block.getNumber())),
                Options.create(0, 10));
        logFilter.addTopicSet(address, topic0, null, null, null, null);
        logFilter.setOrder(Order.DESC.getValue());
        final ArrayList<FilteredLogEvent> filteredEvents = LogsClient.getFilteredLogEvents(logFilter);
        for (FilteredLogEvent filteredTransferEvent : filteredEvents) {
            logger.info("filteredTransferEvent:{}", JSONObject.toJSONString(filteredTransferEvent, prettyFormat));
            LogMeta lm = filteredTransferEvent.getMeta();
            logger.info(JSONObject.toJSONString(lm, prettyFormat));
        }
    }

    // Galactica documented at: http://localhost:8669/doc/stoplight-ui/#/paths/logs-transfer/post
    // Solo tested.
    // POST http://localhost:8669/logs/transfer
    // Accept: application/json, text/plain
    // Content-Type: application/json
    //
    // {
    //  "range": {
    //    "unit": "block",
    //    "from": 0,
    //    "to": 17289864
    //  },
    //  "options": {
    //    "offset": 0,
    //    "limit": 100,
    //    "includeIndexes": true
    //  },
    //  "criteriaSet": [
    //    {
    //      "txOrigin": "0xf077b491b355e64048ce21e3a6fc4751eeea77fa",
    //      "sender": "0xf077b491b355e64048ce21e3a6fc4751eeea77fa"
    //    }
    //  ],
    //  "order": "desc"
    // }
    @Test
    public void testGetFilteredTransferLogs() throws ClientArgumentException {
        final String txOrigin = System.getProperty("LogsClientTest.testGetFilteredTransferLogs.txOrigin");
        final String sender = System.getProperty("LogsClientTest.testGetFilteredTransferLogs.sender");
        final Block block = BlockClient.getBlock(null);
        final TransferredFilter transferredFilter = TransferredFilter.createFilter(
                Range.createBlockRange(0, Long.parseLong(block.getNumber())),
                Options.create(0, 10)
        );
        transferredFilter.addTransferCriteria(
                Address.fromHexString(txOrigin),
                Address.fromHexString(sender),
                null);
        transferredFilter.setOrder(Order.DESC.getValue());
        final ArrayList<FilteredTransferEvent> transferLogs = LogsClient.getFilteredTransferLogs(transferredFilter);
        logger.info("transferLogs:{}", JSONObject.toJSONString(transferLogs, prettyFormat));
    }

}
