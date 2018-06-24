package com.vechain.thorclient.clients;

import com.vechain.thorclient.clients.base.AbstractClient;
import com.vechain.thorclient.core.model.blockchain.*;
import com.vechain.thorclient.core.model.clients.*;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.Prefix;

import java.io.IOException;
import java.util.HashMap;

/**
 * Accounts module client. It can get Account,
 */
public class AccountClient extends AbstractClient {

    /**
     * Get Account Info.
     * @param address required, if null will throw the {@link ClientArgumentException}.
     * @param revision block revision.
     * @return {@link Account}
     * @throws IOException if network error or invalid request.
     */
    public static Account getAccountInfo(Address address,  Revision revision) throws IOException{

        if (address == null){
            throw ClientArgumentException.exception( "Address account is null" );
        }
        Revision currRevision = revision;
        if(currRevision == null){
            currRevision = Revision.BEST;
        }
        HashMap<String, String> uriParams = parameters(new String[]{"address"},new String[]{address.toHexString( Prefix.ZeroLowerX )});
        HashMap<String, String> queryParams = parameters(new String[]{"revision"},new String[]{currRevision.toString()});
        return sendGetRequest(Path.GetAccountPath, uriParams, queryParams, Account.class );
    }


    /**
     * Deploy a Contract.
     * @param contractCall {@link ContractCall}
     * @return {@link ContractCallResult}
     * @throws IOException if network error or invalid request.
     */
    public static ContractCallResult deployContractInfo(ContractCall contractCall) throws IOException{
        if(contractCall  == null){
            throw ClientArgumentException.exception( "contract call object is null" );
        }
        return sendPostRequest( Path.PostDeployContractPath, null, null, contractCall, ContractCallResult.class );
    }

    /**
     * Get code of the address.
     * @param address the address which has contract code or data.
     * @param revision block revision.
     * @return {@link AccountCode} codes on the account address.
     * @throws IOException if network error or invalid request.
     */
    public static AccountCode getAccountCode(Address address, Revision revision) throws IOException {
        if (address == null){
            throw ClientArgumentException.exception( "Address account is null" );
        }
        Revision currRevision = revision;
        if(currRevision == null){
            currRevision = Revision.BEST;
        }
        HashMap<String, String> uriParams = parameters(new String[]{"address"},new String[]{address.toHexString( Prefix.ZeroLowerX )});
        HashMap<String, String> queryParams = parameters(new String[]{"revision"},new String[]{currRevision.toString()});
        return sendGetRequest(Path.GetAccountCodePath, uriParams, queryParams, AccountCode.class );
    }


    /**
     * Get storage at key
     * @param address {@link Address} required the account address.
     * @param key {@link StorageKey} required key.
     * @param revision {@link Revision} optional block revision.
     * @return {@link StorageKey} storage key.
     * @throws IOException if network error or invalid request.
     */
    public static StorageData getStorageAt(Address address, StorageKey key, Revision revision) throws IOException {
        if (address == null){
            throw ClientArgumentException.exception( "Address account is null" );
        }
        if(key == null){
            throw ClientArgumentException.exception( "key is null" );
        }
        Revision currRevision = revision;
        if(currRevision == null){
            currRevision = Revision.BEST;
        }
        HashMap<String, String> uriParams = parameters(new String[]{"address", "key"},new String[]{address.toHexString( Prefix.ZeroLowerX ), key.hexKey()});
        HashMap<String, String> queryParams = parameters(new String[]{"revision"},new String[]{currRevision.toString()});
        return sendGetRequest(Path.GetStorageValuePath, uriParams, queryParams, StorageData.class );
    }

}
