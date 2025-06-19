package com.vechain.thorclient.utils;

import com.vechain.thorclient.clients.TransactionClient;
import com.vechain.thorclient.core.model.blockchain.Receipt;
import com.vechain.thorclient.core.model.clients.Revision;

public class TransactionUtils {

    /**
     * Waits for a transaction to be included in a block
     * By polling the transaction receipt
     * @param txId transaction id
     * @return transaction receipt or null
     */
    public static Receipt pollForReceipt(String txId) {
        int maxPolls = 15;
        int pollInterval = 1000;
        int pollCount = 0;
        while (pollCount < maxPolls) {
            Receipt receipt = TransactionClient.getTransactionReceipt(txId, Revision.BEST);
            if (receipt != null) {
                return receipt;
            }
            try {
                Thread.sleep(pollInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            pollCount++;
        }
        return null; // timed out
    }

}
