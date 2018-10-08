package com.vechain.thorclient.clients;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.clients.base.SubscribeSocket;
import com.vechain.thorclient.clients.base.SubscribingCallback;
import com.vechain.thorclient.core.model.blockchain.BlockSubscribingRequest;
import com.vechain.thorclient.core.model.blockchain.BlockSubscribingResponse;
import org.eclipse.jetty.websocket.api.CloseStatus;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Type;

@RunWith(JUnit4.class)
public class SubscribeClientTest extends BaseTest {

    @Test
    public void testSubscribeBlock() throws Exception {
        SubscribingCallback<BlockSubscribingResponse> callback = new SubscribingCallback<BlockSubscribingResponse>() {
            @Override
            public void onClose(int statusCode, String reason) {
                logger.info( "on close:" + statusCode + " reason:" + reason );
            }

            @Override
            public void onConnect(Session session) {
                logger.info( "On connect:" + session.toString() );
            }

            @Override
            public Class<BlockSubscribingResponse> responseClass() {
                return BlockSubscribingResponse.class;
            }

            @Override
            public void onSubscribe(BlockSubscribingResponse response) {
                logger.info( "Block Response :" + JSON.toJSONString(response) );
            }
        };
        SubscribeSocket socket = SubscribeClient.subscribeBlock( null,  callback);
        Thread.sleep( 20000 );
        socket.close( 0, "user close" );
    }

}
