package com.vechain.thorclient.clients;

import java.util.ArrayList;
import java.util.List;

import com.vechain.thorclient.core.model.blockchain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.alibaba.fastjson.JSONObject;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.ERC20Contract;
import com.vechain.thorclient.core.model.clients.base.AbiDefinition;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.Prefix;

@RunWith(JUnit4.class)
public class LogsClientTest extends BaseTest {

    boolean prettyFormat = true;

    // Galactica tested: http://127.0.0.1:8669/doc/stoplight-ui/#/paths/logs-event/post
    // # curl --request POST
    // #  --url http://127.0.0.1:8669/logs/event
    // #  --header 'Accept: application/json, text/plain'
    // #  --header 'Content-Type: application/json'
    // #  --data '{
    // #  "range": {
    // #    "unit": "block",
    // #    "from": 0,
    // #    "to": 10
    // #  },
    // #  "options": {
    // #    "offset": 0,
    // #    "limit": 100,
    // #    "includeIndexes": true
    // #  },
    // #  "criteriaSet": [
    // #    {
    // #      "address": "0x0000000000000000000000000000506172616d73",
    // #      "topic0": "0x28e3246f80515f5c1ed987b133ef2f193439b25acba6a5e69f219e896fc9d179"
    // #    }
    // #  ],
    // #  "order": "desc"
    // #}'
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
        logFilter.addTopicSet(
                "0x0000000000000000000000000000506172616d73",
                "0x28e3246f80515f5c1ed987b133ef2f193439b25acba6a5e69f219e896fc9d179",
                null,
                null,
                null,
                null
        );
        logFilter.setOrder(Order.DESC.getValue());

        ArrayList<FilteredLogEvent> filteredEvents = LogsClient.getFilteredLogEvents(logFilter);
        for (FilteredLogEvent filteredTransferEvent : filteredEvents) {
            logger.info("filteredTransferEvent:{}", JSONObject.toJSONString(filteredTransferEvent, prettyFormat));
            LogMeta lm = filteredTransferEvent.getMeta();
            logger.info(JSONObject.toJSONString(lm, true));
        }
    }

    // Galactica tested: http://localhost:8669/doc/stoplight-ui/#/paths/logs-transfer/post
    // # curl --request POST
    // #  --url http://localhost:8669/logs/transfer
    // #  --header 'Accept: application/json, text/plain'
    // #  --header 'Content-Type: application/json'
    // #  --data '{
    // #  "range": {
    // #    "unit": "block",
    // #    "from": 0,
    // #    "to": 17289864
    // #  },
    // #  "options": {
    // #    "offset": 0,
    // #    "limit": 100,
    // #    "includeIndexes": true
    // #  },
    // #  "criteriaSet": [
    // #    {
    // #      "txOrigin": "0xf077b491b355e64048ce21e3a6fc4751eeea77fa",
    // #      "sender": "0xf077b491b355e64048ce21e3a6fc4751eeea77fa"
    // #    }
    // #  ],
    // #  "order": "asc"
    // #}'
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
    public void testTransferLogs() throws ClientArgumentException {
        Block block = BlockClient.getBlock(null);
        TransferredFilter transferredFilter = TransferredFilter
                .createFilter(Range.createBlockRange(0, Long.parseLong(block.getNumber())), Options.create(0, 10));
        transferredFilter.addTransferCriteria(
                Address.fromHexString("0xf077b491b355e64048ce21e3a6fc4751eeea77fa"),
                Address.fromHexString("0xf077b491b355e64048ce21e3a6fc4751eeea77fa"),
                null);
        transferredFilter.setOrder(Order.DESC.getValue());
        ArrayList<FilteredTransferEvent> transferLogs = LogsClient.getFilteredTransferLogs(transferredFilter);
        logger.info("transferLogs:{}", JSONObject.toJSONString(transferLogs, prettyFormat));
    }

    @Test
    public void x() {
        String body = "{\n" +
                "      \"blockID\": \"0x00000000c05a20fbca2bf6ae3affba6af4a74b800b585bf7a4988aba7aea69f6\",\n" +
                "      \"blockNumber\": 0,\n" +
                "      \"blockTimestamp\": 1526400000,\n" +
                "      \"txID\": \"0x0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "      \"txOrigin\": \"0x0000000000000000000000000000000000000000\",\n" +
                "      \"clauseIndex\": 1,\n" +
                "      \"txIndex\": 2,\n" +
                "      \"logIndex\": 3\n" +
                "    }";
        LogMeta lm = JSONObject.parseObject(body, LogMeta.class);
        logger.info("lm:{}", JSONObject.toJSONString(lm, true));
    }

}
