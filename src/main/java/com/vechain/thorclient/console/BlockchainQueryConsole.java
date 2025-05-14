package com.vechain.thorclient.console;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.vechain.thorclient.clients.BlockClient;
import com.vechain.thorclient.clients.BlockchainClient;
import com.vechain.thorclient.core.model.blockchain.Block;
import com.vechain.thorclient.core.model.clients.Revision;
import org.bouncycastle.util.encoders.Hex;

public class BlockchainQueryConsole {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void getBestBlockRef() {
        byte[] blockRefByte = BlockClient.getBlock(Revision.BEST).blockRef().toByteArray();
        String blockRef = Hex.toHexString(blockRefByte);
        System.out.println("BlockRef:");
        System.out.println("0x" + blockRef);
    }

    public static void getBestBlock(String[] args) throws JsonProcessingException {
        Block block = null;
        if (args != null && args.length > 2) {
            Revision revision = Revision.create(Long.parseLong(args[2]));
            block = BlockClient.getBlock(revision);
        } else {
            block = BlockClient.getBlock(Revision.BEST);
        }
        System.out.println("Block:");
        System.out.println(OBJECT_MAPPER.writeValueAsString(block));
    }

    public static void getChainTag() {
        byte chainTagByte = BlockchainClient.getChainTag();
        String chainTag = String.format("%02x", chainTagByte);
        System.out.println("ChainTag:");
        System.out.println("0x" + chainTag);
    }

}
