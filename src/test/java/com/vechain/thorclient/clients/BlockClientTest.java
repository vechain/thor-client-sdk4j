package com.vechain.thorclient.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.Block;
import com.vechain.thorclient.core.model.clients.Revision;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.Prefix;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class BlockClientTest extends BaseTest {

    final boolean prettyFormat = isPretty();

    final ObjectMapper objectMapper = new ObjectMapper();

    final ObjectWriter writer = prettyFormat ? objectMapper.writerWithDefaultPrettyPrinter() : objectMapper.writer();


    // Galactica documented at: http://localhost:8669/doc/stoplight-ui/#/paths/blocks-revision/get
    // Solo tested.
    // GET http://localhost:8669/blocks/best
    // Accept: application/json, text/plain
    // https://galactica.dev.node.vechain.org tested.
    @Test
    public void testGetBlock() throws IOException {
        final Revision revision = Revision.BEST;
        final Block block = BlockClient.getBlock(revision);
        logger.info("block: {}", writer.writeValueAsString(block));
        logger.info("blockRef: {}", BytesUtils.toHexString(block.blockRef().toByteArray(), Prefix.ZeroLowerX));
        Assert.assertNotNull(block);
    }

    // Scan all blocks to stress Unirest.
    // Solo tested.
    // @Test
    public void testUnirest() {
        Block best = BlockClient.getBlock(Revision.BEST);
        for (int i = 0; i < Long.parseLong(best.getNumber()); i++) {
            try {
                Revision revision = Revision.create(i);
                Block block = BlockClient.getBlock(revision);
                logger.info("block: {}", block.getId());

            } catch (Exception e) {
                String message = String.format("InterruptedException: %s", e.getMessage());
                logger.error(message);
                Assert.fail(message);
            }
        }
    }
}
