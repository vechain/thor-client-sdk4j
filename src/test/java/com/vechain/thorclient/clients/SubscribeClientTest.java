package com.vechain.thorclient.clients;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.clients.base.SubscribeSocket;
import com.vechain.thorclient.clients.base.SubscribingCallback;
import com.vechain.thorclient.core.model.blockchain.BlockSubscribingResponse;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SubscribeClientTest extends BaseTest {

    final boolean prettyFormat = true;

    // Galactica documented at http://localhost:8669/doc/stoplight-ui/#/paths/subscriptions-block/get
    // Solo tested
    @Test
    public void testSubscribeBlock() throws Exception {
        SubscribingCallback<BlockSubscribingResponse> callback = new SubscribingCallback<BlockSubscribingResponse>() {
            @Override
            public void onClose(int statusCode, String reason) {
                logger.info("On close: " + statusCode + " reason: " + reason);
            }

            @Override
            public void onConnect(Session session) {
                logger.info("On connect: " + session.toString());
            }

            @Override
            public Class<BlockSubscribingResponse> responseClass() {
                return BlockSubscribingResponse.class;
            }

            @Override
            public void onSubscribe(BlockSubscribingResponse response) {
                logger.info("Block Response :" + JSON.toJSONString(response, prettyFormat));
            }
        };
        final SubscribeSocket socket = SubscribeClient.subscribeBlock(null, callback);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            String message = String.format("InterruptedException: %s", e.getMessage());
            logger.error(message);
            Assert.fail(message);
        } finally {
            socket.close(StatusCode.NORMAL, "Closed by test.");
        }
    }

}
