package com.vechain.thorclient.clients;

import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.BlockRef;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.core.model.clients.ToClause;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.RawTransactionFactory;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

import java.io.IOException;

public class ContractClient extends TransactionClient {

    protected final static int GasLimit = 70000;



    /**
     * invokeContractMethod send transaction to contract.
     * @param toClauses
     * @param gas
     * @param gasCoef
     * @param expiration
     * @param keyPair
     * @return
     * @throws IOException
     */
    public static TransferResult invokeContractMethod(ToClause[] toClauses, int gas, byte gasCoef, int expiration , ECKeyPair keyPair)throws IOException{

        if(keyPair == null){
            throw ClientArgumentException.exception( "ECKeyPair is null." );
        }

        if(gas < GasLimit){
            throw ClientArgumentException.exception( "gas is too small." );
        }
        if(gasCoef <= 0){
            throw ClientArgumentException.exception( "gas coef is too small." );
        }

        if(expiration <= 0) {
            throw ClientArgumentException.exception( "expiration is invalid." );
        }

        if(toClauses == null){
            throw ClientArgumentException.exception( "To clause is null" );
        }

        byte chainTag = BlockchainClient.getChainTag();
        BlockRef bestRef = BlockchainClient.getBlockRef(null);
        if(bestRef == null || chainTag == 0){
            throw new IOException( "Get chainTag: "+ chainTag + " BlockRef: "+ bestRef );
        }
        RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(chainTag,
                bestRef.toByteArray(),
                expiration,
                gas,
                gasCoef,
                CryptoUtils.generateTxNonce(),
                toClauses);
        return TransactionClient.signThenTransfer( rawTransaction, keyPair );
    }

}
