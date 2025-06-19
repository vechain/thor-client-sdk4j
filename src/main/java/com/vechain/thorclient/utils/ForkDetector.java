package com.vechain.thorclient.utils;

import com.vechain.thorclient.clients.BlockClient;
import com.vechain.thorclient.core.model.blockchain.Block;
import com.vechain.thorclient.core.model.clients.Revision;

/**
 * Class to check if Thor HardForks have happened
 */
public class ForkDetector {

    // Store previous call
    private static Boolean cachedGalacticaForkedStatus = null;

    /**
     * Detects if galactic hard fork has happened
     * @return True/False
     */
    public static Boolean isGalacticaForked() {
        if (cachedGalacticaForkedStatus) {
            return true;
        }
        // check best block
        Block bestBlock = BlockClient.getBlock(Revision.BEST);
        String bestBlockBaseFee = bestBlock.getBaseFeePerGas();
        // cache result
        cachedGalacticaForkedStatus = bestBlockBaseFee != null && !bestBlockBaseFee.trim().isEmpty();
        return cachedGalacticaForkedStatus;
    }
}
