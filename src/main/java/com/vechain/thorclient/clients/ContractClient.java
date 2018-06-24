package com.vechain.thorclient.clients;

import com.vechain.thorclient.core.model.blockchain.ContractCall;
import com.vechain.thorclient.core.model.blockchain.ContractCallResult;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.*;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.Prefix;
import com.vechain.thorclient.utils.RawTransactionFactory;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

import java.io.IOException;
import java.util.HashMap;

public class ContractClient extends TransactionClient {

    protected final static int GasLimit = 70000;

    /**
     *
     * @param call
     * @param contractAddress
     * @param revision
     * @return
     * @throws IOException
     */
    public static ContractCallResult callContract(ContractCall call, Address contractAddress, Revision revision)throws IOException {
        Revision currentRevision = revision;
        if(currentRevision == null){
            currentRevision = Revision.BEST;
        }

        HashMap<String, String> uriParams = parameters( new String[]{"address"}, new String[]{ contractAddress.toHexString( Prefix.ZeroLowerX )} );
        HashMap<String, String> queryParams = parameters(new String[]{"revision"}, new String[]{currentRevision.toString()}   );

        return sendPostRequest( Path.PostContractCallPath, uriParams, queryParams,call, ContractCallResult.class);
    }

    /**
     *
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
