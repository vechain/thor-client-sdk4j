package com.vechain.thorclient.clients;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.ArrayList;

@RunWith(JUnit4.class)
public class LogsClientTest extends BaseTest{
    @Test
    public void testFilterEvents() throws IOException {
        EventFilter filter = EventFilter.createFilter( Range.createBlockRange(1000, 20000), Options.create( 0, 10 ) );
       ArrayList filteredEvents =  LogsClient.filterEvents( filter, Order.DESC, null);
       logger.info( "filteredEvents:" + JSON.toJSONString( filteredEvents ));
    }

    @Test
    public void testTransferLogs() throws IOException{
        TransferFilter filter = TransferFilter.createFilter(Range.createBlockRange( 1000, 20000 ) ,Options.create( 0, 10 ) );
        ArrayList transferLogs = LogsClient.filterTransferLogs( filter, Order.DESC);
        logger.info( "transfer logs:" + JSON.toJSONString( transferLogs ) );
    }

}
