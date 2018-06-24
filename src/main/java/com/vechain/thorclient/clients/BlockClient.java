package com.vechain.thorclient.clients;

import com.vechain.thorclient.clients.base.AbstractClient;
import com.vechain.thorclient.core.model.blockchain.Block;
import com.vechain.thorclient.core.model.clients.Revision;

import java.io.IOException;
import java.util.HashMap;

public class BlockClient extends AbstractClient{

    /**
     * Get {@link Block} information.
     * @param revision {@link Revision} optional the block revision, can be null.
     * @return Block {@link Block} can be null.
     * @throws IOException
     */
    public static Block getBlock(Revision revision) throws IOException{
        Revision currentRevision = revision;
        if( revision == null){
            currentRevision =  Revision.BEST;
        }
        HashMap<String, String> uriParams = parameters( new String[]{"revision"}, new String[]{currentRevision.toString()} );
        return sendGetRequest( Path.GetBlockPath, uriParams, null, Block.class );
    }
}
