package com.vechain.thorclient.clients;

import com.vechain.thorclient.core.model.blockchain.RawTransaction;
import com.vechain.thorclient.core.model.blockchain.Transaction;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

import java.io.IOException;


public class TransactionClient {

    /**
     * Get transaction by transaction Id.
     * @param txId hex string id.
     * @return
     */
    public static Transaction getTransaction(String txId) throws IOException {

        return null;
    }


    /**
     * Sign the raw transaction.
     * @param rawTransaction {@link RawTransaction}
     * @return {@link RawTransaction} with signature.
     */
    public static RawTransaction sign(RawTransaction rawTransaction, ECKeyPair keyPair){
        return null;
    }
}
