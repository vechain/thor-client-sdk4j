package com.vechain.thorclient.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.clients.base.AbstractClient;
import com.vechain.thorclient.clients.base.SubscribeSocket;
import com.vechain.thorclient.clients.base.SubscribingCallback;
import com.vechain.thorclient.core.model.blockchain.BlockSubscribingResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * subscribeSocketConnect deliberately does not throw when the node is
 * unreachable; it returns a closed subscription. These tests pin that contract
 * and prove the reason is recoverable rather than silently swallowed.
 */
@RunWith(JUnit4.class)
public class SubscribeSocketFailureTest extends BaseTest {

    /** Port chosen so nothing is listening; the connect must be refused. */
    private static final String UNREACHABLE = "ws://127.0.0.1:1/subscriptions/block";

    private SubscribingCallback<BlockSubscribingResponse> noopCallback() {
        return new SubscribingCallback<BlockSubscribingResponse>() {
            @Override
            public void onClose(int statusCode, String reason) {
            }

            @Override
            public void onConnect(SubscribeSocket<BlockSubscribingResponse> socket) {
                Assert.fail("onConnect must not fire for an unreachable node");
            }

            @Override
            public Class<BlockSubscribingResponse> responseClass() {
                return BlockSubscribingResponse.class;
            }

            @Override
            public void onSubscribe(BlockSubscribingResponse response) throws JsonProcessingException {
                Assert.fail("onSubscribe must not fire for an unreachable node");
            }
        };
    }

    @Test
    public void failedConnectReturnsAClosedSocketRatherThanThrowing() throws Exception {
        final SubscribeSocket<BlockSubscribingResponse> socket =
                AbstractClient.subscribeSocketConnect(UNREACHABLE, noopCallback());

        Assert.assertNotNull("a socket is still returned on failure", socket);
        Assert.assertFalse("socket must report itself as not connected", socket.isConnected());
    }

    /**
     * Regression: the failure reason used to be logged and discarded, leaving
     * callers unable to tell a refused port from a TLS or DNS problem.
     */
    @Test
    public void failedConnectExposesTheUnderlyingCause() throws Exception {
        final SubscribeSocket<BlockSubscribingResponse> socket =
                AbstractClient.subscribeSocketConnect(UNREACHABLE, noopCallback());

        final Exception cause = socket.getLastError();
        Assert.assertNotNull("the connect failure reason must be recoverable", cause);
        logger.info("recovered connect failure: {}: {}",
                cause.getClass().getName(), cause.getMessage());
    }

    @Test
    public void invalidArgumentsStillThrow() {
        try {
            AbstractClient.subscribeSocketConnect("  ", noopCallback());
            Assert.fail("blank url must be rejected");
        } catch (Exception expected) {
            // argument validation is a caller error and remains an exception
        }
        try {
            AbstractClient.subscribeSocketConnect(UNREACHABLE, null);
            Assert.fail("null callback must be rejected");
        } catch (Exception expected) {
            // as above
        }
    }
}
