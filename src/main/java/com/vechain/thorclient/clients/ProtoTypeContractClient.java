package com.vechain.thorclient.clients;

import com.vechain.thorclient.clients.base.AbstractClient;
import com.vechain.thorclient.core.model.blockchain.*;
import com.vechain.thorclient.core.model.clients.*;
import com.vechain.thorclient.core.model.clients.base.AbiDefinition;
import com.vechain.thorclient.core.model.clients.base.AbstractContract;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.Prefix;
import com.vechain.thorclient.utils.RawTransactionFactory;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ProtoType Contract is a native contract for user to do multiple parties payment(mpp).
 * What is mpp for?
 * In common scenario, if a sender A want to make a transaction to receiver B(to address), the sender A need to pay the transaction fee(gas).
 * However, for some realistic reason, the receiver A want to pay the transaction fee. Then the mpp can do such a thing.
 * The sender A can be a user of receiver B, then the receiver B can set user plan (credit and recovery) to sender A.
 * After all the things is done, then the sender A do transaction to receiver B, if the fee is less than the credit, the blockchain system is going to book fee(gas) from receiver B's account.
 *
 * How to use mpp?
 * First, you must be the master of the to address. By call
 *
 *
 */
public class ProtoTypeContractClient extends AbstractClient{


    /**
     *
     * @param address
     * @return
     * @throws IOException
     */
    public static ContractCallResult getMasterAddress(Address receiverAddress) throws IOException {
        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "master" );
        if(abi == null){
            throw new IllegalArgumentException( "Can not find abi master method" );
        }
        ContractCall call = ProtoTypeContract.buildCall(abi, receiverAddress.toHexString( Prefix.ZeroLowerX ));
        HashMap<String, String> uriParams = parameters( new String[]{"address"}, new String[]{ ProtoTypeContract.ContractAddress.toHexString( Prefix.ZeroLowerX )} );
        return sendPostRequest( Path.PostContractCallPath, uriParams, null,call, ContractCallResult.class);
    }

    /**
     *
     * @param receiverAddresses
     * @param newMasterAddresses
     * @param keyPair
     * @return
     * @throws IOException
     */
    public static TransferResult setMasterAddress(Address[] receiverAddresses, Address[] newMasterAddresses, int gas, byte gasCoef, int expiration , ECKeyPair keyPair) throws IOException {
        if(receiverAddresses == null){
            throw ClientArgumentException.exception( "receiverAddresses is null" );
        }
        if(newMasterAddresses == null){
            throw ClientArgumentException.exception( "newMasterAddresses in null" );
        }
        if(receiverAddresses.length != newMasterAddresses.length){
            throw ClientArgumentException.exception( "receiverAddresses size must equal to newMasterAddresses size. " );
        }

        if(keyPair == null){
            throw ClientArgumentException.exception( "ECKeyPair is null" );
        }


        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "setMaster" );
        if(abi == null){
            throw new IllegalArgumentException( "Can not find abi master method" );
        }
        ToClause[] clauses = new ToClause[receiverAddresses.length];
        for(int index = 0; index < receiverAddresses.length; index ++){
            clauses[index] = ProtoTypeContract.buildToClause( ProtoTypeContract.ContractAddress, abi, receiverAddresses[index].toHexString( Prefix.ZeroLowerX ),newMasterAddresses[index].toHexString( Prefix.ZeroLowerX ));

        }
        byte chainTag = BlockchainClient.getChainTag();
        Block bestBlock = BlockClient.getBlock( null );
        RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(chainTag,
                bestBlock.blockRef().toByteArray(),
                expiration,
                gas,
                gasCoef,
                CryptoUtils.generateTxNonce(),
                clauses);
        return TransactionClient.signThenTransfer( rawTransaction, keyPair );
    }


    /**
     *
     * @param user
     * @param credit
     * @return
     */
    public static String setUserCredit(Address user, Amount credit, RecoveryRate recovery ) throws IOException {


        return null;
    }



    public static TransferResult addUser(Address user) throws IOException{
        return null;
    }





}
