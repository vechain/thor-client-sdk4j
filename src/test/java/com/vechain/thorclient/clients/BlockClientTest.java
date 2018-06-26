package com.vechain.thorclient.clients;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.Block;
import com.vechain.thorclient.core.model.clients.Revision;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.Prefix;

@RunWith(JUnit4.class)
public class BlockClientTest extends BaseTest {

    @Test
    public void testGetBlock() throws IOException {
        Revision revision = Revision.create(1232);
        Block block = BlockClient.getBlock(revision);
        logger.info( "unserialized: " + JSON.parseObject("{\"number\":0,\"id\":\"0x00000000ef3b214ad627b051f42add3b93b2f913f2594b94a64b2377b0f9159a\",\"size\":170,\"parentID\":\"0xffffffff00000000000000000000000000000000000000000000000000000000\",\"timestamp\":1528387200,\"gasLimit\":10000000,\"beneficiary\":\"0x0000000000000000000000000000000000000000\",\"gasUsed\":0,\"totalScore\":0,\"txsRoot\":\"0x45b0cfc220ceec5b7c1c62c4d4193d38e4eba48e8815729ce75f9c0ab0e4c1c0\",\"stateRoot\":\"0x120df3368f409525ed30fd98c999af8d66bfa553cae14005fc3b7f00bcc60de1\",\"receiptsRoot\":\"0x45b0cfc220ceec5b7c1c62c4d4193d38e4eba48e8815729ce75f9c0ab0e4c1c0\",\"signer\":\"0x0000000000000000000000000000000000000000\",\"isTrunk\":true,\"transactions\":[]}", Block.class  ) );
        logger.info("block:" + JSON.toJSONString(block));
        logger.info("blockRef;" + BytesUtils.toHexString(block.blockRef().toByteArray(), Prefix.ZeroLowerX));
        Assert.assertNotNull(block);
    }

}
