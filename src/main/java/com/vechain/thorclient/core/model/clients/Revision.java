package com.vechain.thorclient.core.model.clients;

import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.BlockchainUtils;

/**
 * Block revision object, it is constructed by block number, block hex string Id or "best" refer to {@link #BEST}.
 */
public class Revision {

    public static final Revision BEST = new Revision() ;
    private String revision;

    /**
     * Create from block number.
     * @param blockNumber block number.
     * @return {@link Revision}
     */
    public static Revision create(long blockNumber){
        Revision revision = new Revision();
        revision.revision = "" + blockNumber;
        return revision;
    }

    /**
     * Create from blockId or best block.
     * @param blockId block id hex string start with prefix "0x" or "best" string ignored case.
     * @return {@link Revision}
     */
    public static Revision create(String blockId){
        if(blockId.equalsIgnoreCase( "best" )){
            return BEST;
        }

        if(!BlockchainUtils.isId(blockId)){
            throw ClientArgumentException.exception( "create revision from blockId invalid" );
        }
        Revision revision = new Revision();
        revision.revision = blockId;
        return revision;
    }

    private Revision(){
        this.revision = "best";
    }

    public String toString(){
        return this.revision;
    }


}
