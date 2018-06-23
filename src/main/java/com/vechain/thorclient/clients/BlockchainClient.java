package com.vechain.thorclient.clients;

import com.vechain.thorclient.clients.base.AbstractClient;
import com.vechain.thorclient.core.model.blockchain.Block;
import com.vechain.thorclient.core.model.clients.Revision;
import com.vechain.thorclient.utils.BlockchainUtils;
import com.vechain.thorclient.utils.BytesUtils;

import java.io.IOException;

public class BlockchainClient extends AbstractClient{

    public static byte getChainTag() throws IOException{
        Block genesisBlock = BlockClient.getBlock( Revision.create( 0 ) );
        if(genesisBlock == null){
            throw new RuntimeException( " Get Genesis block error" );
        }
        String hexId = genesisBlock.getId();
        if(!BlockchainUtils.isId( hexId )){
            throw new RuntimeException( " Genesis block id is invalid" );
        }
        byte[] bytesId = BytesUtils.toByteArray( hexId );
        if(bytesId == null || bytesId.length != 32){
            throw new RuntimeException( " Genesis block id converted error" );
        }
        return bytesId[31];
    }

}
