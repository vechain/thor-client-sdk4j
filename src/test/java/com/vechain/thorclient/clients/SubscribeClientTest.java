package com.vechain.thorclient.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.clients.base.SubscribeSocket;
import com.vechain.thorclient.clients.base.SubscribingCallback;
import com.vechain.thorclient.core.model.blockchain.BlockSubscribingResponse;
import com.vechain.thorclient.core.model.clients.*;
import com.vechain.thorclient.core.model.clients.base.AbstractToken;
import com.vechain.thorclient.utils.*;
import com.vechain.thorclient.utils.crypto.ECKeyPair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(JUnit4.class)
public class SubscribeClientTest extends BaseTest {

    final boolean prettyFormat = isPretty();

    final ObjectMapper objectMapper = new ObjectMapper();

    final ObjectWriter writer = prettyFormat ? objectMapper.writerWithDefaultPrettyPrinter() : objectMapper.writer();

    private static final int AWAIT_SECONDS = 30;

    // Galactica documented at http://localhost:8669/doc/stoplight-ui/#/paths/subscriptions-block/get
    // Solo tested
    @Test
    public void testSubscribeBlock() throws Exception {
        // thor solo runs with --on-demand, so it only packs a block when a transaction is
        // pending. Without submitting one below, this test would wait out its timeout and
        // prove nothing about the subscription.
        final CountDownLatch received = new CountDownLatch(1);
        final AtomicReference<BlockSubscribingResponse> firstBlock = new AtomicReference<BlockSubscribingResponse>();
        final CountDownLatch connected = new CountDownLatch(1);

        SubscribingCallback<BlockSubscribingResponse> callback = new SubscribingCallback<BlockSubscribingResponse>() {
            @Override
            public void onClose(int statusCode, String reason) {
                logger.info("On close: {} reason {}", statusCode, reason);
            }

            @Override
            public void onConnect(SubscribeSocket<BlockSubscribingResponse> socket) {
                logger.info("On connect: connected={}", socket.isConnected());
                connected.countDown();
            }

            @Override
            public Class<BlockSubscribingResponse> responseClass() {
                return BlockSubscribingResponse.class;
            }

            @Override
            public void onSubscribe(BlockSubscribingResponse response) throws JsonProcessingException {
                logger.info("Block Response: {}", writer.writeValueAsString(response));
                firstBlock.compareAndSet(null, response);
                received.countDown();
            }
        };

        final SubscribeSocket<BlockSubscribingResponse> socket = SubscribeClient.subscribeBlock(null, callback);
        try {
            Assert.assertTrue("subscription did not open", socket.isConnected());
            Assert.assertTrue("onConnect was not invoked", connected.await(AWAIT_SECONDS, TimeUnit.SECONDS));

            sendTransactionToForceNewBlock();

            Assert.assertTrue(
                    "no block was delivered over the subscription within " + AWAIT_SECONDS + "s",
                    received.await(AWAIT_SECONDS, TimeUnit.SECONDS));

            final BlockSubscribingResponse block = firstBlock.get();
            Assert.assertNotNull(block);
            // Proves the frame was actually deserialized rather than merely delivered.
            Assert.assertNotNull("subscribed block carried no id", block.getId());
        } finally {
            socket.close(SubscribeSocket.NORMAL_CLOSURE, "Closed by test.");
        }
    }

    /** Submits a minimal VET transfer so that on-demand solo packs a block. */
    private void sendTransactionToForceNewBlock() throws Exception {
        final String fromPrivateKey = System.getProperty("TransactionClientTest.testSendVETTransaction.fromPrivateKey");
        final Address toAddress = Address.fromHexString(
                System.getProperty("TransactionClientTest.testSendVETTransaction.toAddress"));
        final Amount amount = Amount.createFromToken(AbstractToken.VET);
        amount.setDecimalAmount("1");
        final ToClause clause = TransactionClient.buildVETToClause(toAddress, amount, ToData.ZERO);
        final RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(
                BlockchainClient.getChainTag(),
                BlockchainClient.getBlockRef(Revision.BEST).toByteArray(),
                720,
                21000,
                (byte) 0x0,
                CryptoUtils.generateTxNonce(),
                clause);
        TransactionClient.signThenTransfer(rawTransaction, ECKeyPair.create(fromPrivateKey));
    }

}
