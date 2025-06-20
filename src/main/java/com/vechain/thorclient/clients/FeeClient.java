package com.vechain.thorclient.clients;

import java.math.BigInteger;
import java.util.HashMap;

import com.vechain.thorclient.clients.base.AbstractClient;
import com.vechain.thorclient.core.model.blockchain.FeeHistoryResponse;
import com.vechain.thorclient.core.model.blockchain.MaxPriorityFeeResponse;
import com.vechain.thorclient.core.model.exception.ClientIOException;
import com.vechain.thorclient.utils.StringUtils;

public class FeeClient extends AbstractClient {
    /**
     * Get Fee history
     * 
     * @param blockCount        The number of blocks in the requested range. Must be
     *                          a positive integer.
     * @param rewardPercentiles A comma-separated list of percentiles (0-100) at
     *                          which to calculate fee percentiles, e.g., "25,50,75"
     *                          for lower quartile, median, and upper quartile.
     * @param newestBlock       The newest block to consider. Can be a block number
     *                          or "latest", "pending", or "earliest".
     * @return Fee object containing fee history data including base fee per gas and
     *         priority fee percentiles.
     */
    public static FeeHistoryResponse getFeeHistory(Number blockCount, String rewardPercentiles, String newestBlock)
            throws ClientIOException {
        HashMap<String, String> queryParams = parameters(
                new String[] { "blockCount", "rewardPercentiles", "newestBlock" },
                new String[] { blockCount.toString(), rewardPercentiles, newestBlock });
        return sendGetRequest(Path.GetFeeHistoryPath, null, queryParams, FeeHistoryResponse.class);
    }

    /**
     * Gets the suggested maxPriorityFeePerGas that ensures transaction will be in next block
     * @return suggested maxPriorityFeePerGas
     */
    public static BigInteger getPriorityFee() {
        MaxPriorityFeeResponse response =  sendGetRequest(Path.GetFeePriorityPath, null, null, MaxPriorityFeeResponse.class);
        return StringUtils.hexStringToBigInteger(response.getMaxPriorityFeePerGas());
    }
}