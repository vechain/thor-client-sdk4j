package com.vechain.thorclient.core.model.clients;

import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.BlockchainUtils;

/**
 * Block revision object.
 */
public class Revision {

    public static final Revision BEST = new Revision() ;
    private String revision;

    /**
     * Create from block number.
     * @param blockNumber block number.
     * @return {@link Revision}
     */
    public static Revision fromBlockNumber(long blockNumber){
        Revision revision = new Revision();
        revision.revision = "" + blockNumber;
        return revision;
    }

    /**
     * Create from blockId.
     * @param blockId block id hex string. start with prefix "0x"
     * @return
     */
    public static Revision create(String blockId){
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
