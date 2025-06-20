package com.vechain.thorclient.utils;

import com.vechain.thorclient.clients.BlockClient;
import com.vechain.thorclient.clients.FeeClient;
import com.vechain.thorclient.core.model.blockchain.Block;
import com.vechain.thorclient.core.model.blockchain.FeeHistoryResponse;
import com.vechain.thorclient.core.model.blockchain.MaxFees;
import com.vechain.thorclient.core.model.blockchain.MaxPriorityFeeResponse;
import com.vechain.thorclient.core.model.clients.Revision;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MaxFeeCalculator {

    /**
     * Calculates the maxFeePerGas and maxPriority fee per gas from user provided values
     * By checking user provided values and adjusting as needed
     * @param providedFees user provided max fee settings
     * @return updated max fee settings
     */
    public static MaxFees calculateMaxFees(MaxFees providedFees) {
        if (providedFees.maxPriorityFeePerGas != null && providedFees.maxFeePerGas != null) {
            // if user provided not null return them
            return providedFees;
        }
        // else we calculate it for them
        BigInteger bestBlockBaseFeePerGas = getBestBlockBaseFeePerGas();
        BigInteger maxPriorityFeePerGas;
        BigInteger maxFeePerGas;
        // calculate max priority fee per gas, default if user specified it
        if (providedFees.maxPriorityFeePerGas == null) {
            maxPriorityFeePerGas = calculateMaxPriorityFeePerGas(bestBlockBaseFeePerGas);
        } else {
            maxPriorityFeePerGas = providedFees.maxPriorityFeePerGas;
        }
        // calculate max fee per gas, default if user specified it
        if (providedFees.maxFeePerGas == null) {
            maxFeePerGas = bestBlockBaseFeePerGas.add(maxPriorityFeePerGas);
        } else {
            maxFeePerGas = providedFees.maxFeePerGas;
        }
        return new MaxFees(maxFeePerGas, maxPriorityFeePerGas);
    }

    /**
     * Calculate max priority fee per gas based on recent block fee reward history
     * @param baseFeePerGas The base fee per gas, used to cap the priority fee
     * @return computed max priority fee
     * @throws IllegalStateException if galactica fork hasn't happened
     */
    public static BigInteger calculateMaxPriorityFeePerGas(BigInteger baseFeePerGas) {
        // check if galactica has forked
        if (ForkDetector.isGalacticaForked()) {
            // get recent fee history
            FeeHistoryResponse feeHistory = FeeClient.getFeeHistory(10, "25,50,75", "best");
            BigInteger percentile75;
            if (feeHistory.getReward() != null && feeHistory.getReward().length > 0) {
                // get latest rewards
                String[] latestRewards = feeHistory.getReward()[feeHistory.getReward().length - 1];
                // check if latest block has all equal percentiles
                Set<String> uniqueRewards = new HashSet<>(Arrays.asList(latestRewards));
                boolean equalRewards = uniqueRewards.size() == 1;
                // if all equal percentiles return 75% percentile (index 2)
                if (equalRewards) {
                    return StringUtils.hexStringToBigInteger(latestRewards[2]);
                } else {
                    // calculate 75th percentile over all blocks
                    BigInteger sum = BigInteger.valueOf(0L);
                    int count = 0;
                    for (String[] blockRewards : feeHistory.getReward()) {
                        if (blockRewards != null && blockRewards.length > 2 && StringUtils.isHex(blockRewards[2])) {
                            sum = sum.add(StringUtils.hexStringToBigInteger(blockRewards[2]));
                            count++;
                        }
                    }
                    percentile75 = count > 0 ? sum.divide(BigInteger.valueOf(count)) : BigInteger.ZERO;
                }
            } else {
                // default to max priority fees endpoint if history not available
                percentile75 = FeeClient.getPriorityFee();
            }
            // calculate fee cap as 4.6% of base fee
            BigInteger baseFeeCap = baseFeePerGas.multiply(BigInteger.valueOf(46L)).divide(BigInteger.valueOf(1000L));
            // use minimum of calculated fee and the cap
            return baseFeeCap.compareTo(percentile75) < 0 ? baseFeeCap : percentile75;
        }
        throw new IllegalStateException("Galactica fork has not happened yet");
    }

    /**
     * Calculates the max priority fee using the best block base fee
     * @return Calculated max priority fee
     */
    public static BigInteger calculateMaxPriorityFeePerGasFromBestBlock() {
        BigInteger baseFee = getBestBlockBaseFeePerGas();
        return calculateMaxPriorityFeePerGas(baseFee);
    }

    /**
     * Get the base fee per gas from the best/current block
     * @return Base fee per gas
     * @throws IllegalStateException if galactica fork hasn't happened
     */
    public static BigInteger getBestBlockBaseFeePerGas() {
        // check galactica has forked
        if (ForkDetector.isGalacticaForked()) {
            Block bestBlock = BlockClient.getBlock(Revision.BEST);
            String baseFeeHex = bestBlock.getBaseFeePerGas();
            return StringUtils.hexStringToBigInteger(baseFeeHex);
        }
        throw new IllegalStateException("Galactica fork has not happened yet");
    }

}
