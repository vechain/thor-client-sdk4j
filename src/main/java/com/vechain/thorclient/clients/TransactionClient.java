package com.vechain.thorclient.clients;

import com.vechain.thorclient.clients.base.AbstractClient;
import com.vechain.thorclient.core.model.blockchain.*;
import com.vechain.thorclient.core.model.clients.*;
import com.vechain.thorclient.core.model.clients.base.AbstractToken;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.BlockchainUtils;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.Prefix;
import com.vechain.thorclient.utils.RawTransactionFactory;
import com.vechain.thorclient.utils.crypto.ECDSASign;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class TransactionClient extends AbstractClient {

    /**
     * Get transaction by transaction Id.
     * @param txId required transaction id .
     * @param isRaw is response raw transaction.
     * @param revision {@link Revision} revision.
     * @return Transaction {@link Transaction}
     * @throws IOException
     */
    public static Transaction getTransaction(String txId, boolean isRaw, Revision revision) throws IOException {
        if (!BlockchainUtils.isId( txId )){
            throw ClientArgumentException.exception( "Tx id is invalid" );
        }
        Revision currRevision = revision;
        if(currRevision == null){
            currRevision = Revision.BEST;
        }
        HashMap<String, String> uriParams = parameters(new String[]{"id"},new String[]{txId});
        HashMap<String, String> queryParams = parameters(new String[]{"revision", "raw"},new String[]{currRevision.toString(), Boolean.toString( isRaw )});
        return sendGetRequest( Path.GetTransactionPath, uriParams, queryParams, Transaction.class );
    }

    /**
     * Get transaction receipt
     * @param txId  txId hex string start with "0x"
     * @param revision {@link Revision}
     * @return {@link Receipt} return Receipt .
     * @throws IOException
     */
    public static Receipt getTransactionReceipt(String txId, Revision revision) throws IOException{
        if( !BlockchainUtils.isId( txId )){
            throw ClientArgumentException.exception( "Tx id is invalid" );
        }
        Revision currRevision = revision;
        if(currRevision == null){
            currRevision = Revision.BEST;
        }
        HashMap<String, String> uriParams = parameters(new String[]{"id"},new String[]{txId});
        HashMap<String, String> queryParams = parameters(new String[]{"revision"},new String[]{currRevision.toString()});
        return sendGetRequest( Path.GetTransactionReceipt, uriParams, queryParams,  Receipt.class );
    }


    /**
     * Transfer amount, raw transaction will be encoded by rlp encoder and convert to hex string with prefix "0x".
     * Then the hex string will be packaged into {@link TransferRequest} bean object and serialized to JSON string.
     * @param transaction {@link RawTransaction} raw transaction to to send
     * @return {@link TransferResult}
     * @throws IOException network error, 5xx http status means request error, 4xx http status means no enough gas or balance.
     */
    public static TransferResult transfer(final RawTransaction transaction) throws IOException {
        if(transaction == null || transaction.getSignature() == null){
            ClientArgumentException.exception( "Raw transaction is invalid" );
        }
        byte[] rawBytes= transaction.encode();
        if(rawBytes == null){
            throw   ClientArgumentException.exception( "Raw transaction is encode error" );
        }
        String hexRaw = BytesUtils.toHexString( rawBytes, Prefix.ZeroLowerX );
        TransferRequest request = new TransferRequest();
        request.setRaw( hexRaw );
        return sendPostRequest( Path.PostTransaction, null, null, request, TransferResult.class);
    }


    /**
     * Sign the raw transaction.
     * @param rawTransaction {@link RawTransaction}
     * @return {@link RawTransaction} with signature.
     */
    public static RawTransaction sign(RawTransaction rawTransaction, ECKeyPair keyPair){
        if(rawTransaction == null){
            throw ClientArgumentException.exception( "raw transaction object is invalid" );
        }
        ECDSASign.SignatureData signature = ECDSASign.signMessage( rawTransaction.encode(), keyPair, true );
        byte[] signBytes = signature.toByteArray();
        rawTransaction.setSignature( signBytes );
        return rawTransaction;
    }


    /**
     * Sign and transfer the raw transaction.
     * @param rawTransaction {@link RawTransaction} raw transaction without signature data
     * @param keyPair {@link ECKeyPair} the key pair for the private key.
     * @return {@link TransferResult}
     * @throws IOException
     */
    public static TransferResult signThenTransfer(RawTransaction rawTransaction, ECKeyPair keyPair) throws IOException {
       RawTransaction signedRawTxn =  sign( rawTransaction, keyPair );
       return transfer( signedRawTxn );
    }


    /**
     * Build a transaction clause
     * @param toAddress {@link Address} destination address.
     * @param amount {@link Amount} amount to transfer.
     * @param data {@link ToData} some comments maybe.
     * @return {@link ToClause} to clause.
     */
    public static ToClause buildVETToClause(Address toAddress, Amount amount, ToData data){
        if(toAddress == null){
            throw ClientArgumentException.exception( "toAddress is null" );
        }
        if(amount == null){
            throw ClientArgumentException.exception( "amount is null" );
        }
        if(data == null){
            throw ClientArgumentException.exception( "data is null" );
        }
        return new ToClause( toAddress, amount, data );
    }


}
