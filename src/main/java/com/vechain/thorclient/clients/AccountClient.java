package com.vechain.thorclient.clients;

import com.vechain.thorclient.clients.base.AbstractClient;
import com.vechain.thorclient.core.model.blockchain.*;
import com.vechain.thorclient.core.model.clients.*;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.core.model.exception.ClientIOException;
import com.vechain.thorclient.utils.Prefix;
import java.util.HashMap;

/**
 * AccountClient provides access to VeChain blockchain account information and operations.
 * 
 * <p>Accounts represent addresses on the VeChain blockchain, containing VET/VTHO balances 
 * and potentially smart contract code. This client provides comprehensive account querying 
 * capabilities including balance checks, contract code retrieval, and contract calls.</p>
 * 
 * <h3>Account Information</h3>
 * <p>Each account contains:</p>
 * <ul>
 *   <li><strong>balance</strong>: VET balance (hex string in wei)</li>
 *   <li><strong>energy</strong>: VTHO balance (hex string in wei)</li>
 *   <li><strong>hasCode</strong>: Whether address contains contract code</li>
 * </ul>
 * 
 * <h3>Usage Examples</h3>
 * <pre>{@code
 * // Get account information
 * Account account = AccountClient.getAccountInfo(
 *     Address.fromHexString("0xYourAddress"),
 *     null
 * );
 * System.out.println("VET Balance: " + account.getBalance());
 * System.out.println("VTHO Balance: " + account.getEnergy());
 * System.out.println("Has Code: " + account.isHasCode());
 * 
 * // Get contract code
 * AccountCode code = AccountClient.getAccountCode(
 *     Address.VTHO_Address,
 *     null
 * );
 * System.out.println("Contract code: " + code.getCode());
 * 
 * // Perform contract call
 * AccountCall call = new AccountCall();
 * ArrayList<ToClause> clauses = new ArrayList<>();
 * clauses.add(new ToClause(
 *     Address.VTHO_Address,
 *     Amount.ZERO,
 *     new ToData("0x70a08231000000000000000000000000" + "YourAddress")
 * ));
 * call.setClauses(clauses);
 * 
 * AccountCallResult result = AccountClient.performAccountCall(null, call);
 * System.out.println("Call result: " + result.getData());
 * }</pre>
 * 
 * @see Account
 * @see AccountCode
 * @see AccountCall
 * @see AccountCallResult
 * @since 0.1.0
 */
public class AccountClient extends AbstractClient {

    /**
     * Retrieves account information including VET/VTHO balances and contract status.
     * 
     * <p>Returns comprehensive account state information including native VET balance,
     * VTHO energy balance, and whether the address contains smart contract code.</p>
     * 
     * @param address the account address to query. Must not be {@code null}
     * @param revision the block revision for the query. Use {@code null} for latest block,
     *                 {@link Revision#BEST} for best block, or {@link Revision#create(long)}
     *                 for a specific block number
     * @return the account information including balances and contract status
     * @throws ClientIOException if there's a network error or the request fails
     * @throws ClientArgumentException if the address is {@code null}
     * 
     * @see Account
     * @see Revision#BEST
     * @see Revision#create(long)
     */
    public static Account getAccountInfo(Address address, Revision revision) throws ClientIOException {

        if (address == null) {
            throw ClientArgumentException.exception("Address account is null");
        }
        Revision currRevision = revision;
        if (currRevision == null) {
            currRevision = Revision.BEST;
        }
        HashMap<String, String> uriParams = parameters(new String[] { "address" },
                new String[] { address.toHexString(Prefix.ZeroLowerX) });
        HashMap<String, String> queryParams = parameters(new String[] { "revision" },
                new String[] { currRevision.toString() });
        return sendGetRequest(Path.GetAccountPath, uriParams, queryParams, Account.class);
    }

    /**
     * @deprecated Since 2.1.5. This method is deprecated using it can lead to
     *             errors.
     * 
     *             Deploy a Contract.
     * @param contractCall {@link ContractCall}
     * @return {@link ContractCallResult}
     * @throws ClientIOException if network error or invalid request.
     * @Deprecated please use {@link AccountClient.performAccountCall} instead.
     */
    @Deprecated
    public static ContractCallResult deployContractInfo(ContractCall contractCall) throws ClientIOException {
        if (contractCall == null) {
            throw ClientArgumentException.exception("contract call object is null");
        }
        return sendPostRequest(Path.PostDeployContractPath, null, null, contractCall, ContractCallResult.class);
    }

    /**
     * Perform an account call
     * 
     * @param revision    block revision, can be null
     * @param accountCall required, an account call object.
     * @return {@link AccountCallResult}
     * @throws ClientIOException
     */
    public static AccountCallResult performAccountCall(Revision revision, AccountCall accountCall)
            throws ClientIOException {
        if (accountCall == null) {
            throw ClientArgumentException.exception("account call object is null");
        }
        Revision currRevision = revision;
        if (currRevision == null) {
            currRevision = Revision.BEST;
        }
        HashMap<String, String> queryParams = parameters(new String[] { "revision" },
                new String[] { currRevision.toString() });
        return sendPostRequest(Path.PostAccountCallPath, null, queryParams, accountCall, AccountCallResult.class);
    }

    /**
     * Get code of the address.
     * 
     * @param address  the address which has contract code or data.
     * @param revision block revision.
     * @return {@link AccountCode} codes on the account address.
     * @throws ClientIOException if network error or invalid request.
     */
    public static AccountCode getAccountCode(Address address, Revision revision) throws ClientIOException {
        if (address == null) {
            throw ClientArgumentException.exception("Address account is null");
        }
        Revision currRevision = revision;
        if (currRevision == null) {
            currRevision = Revision.BEST;
        }
        HashMap<String, String> uriParams = parameters(new String[] { "address" },
                new String[] { address.toHexString(Prefix.ZeroLowerX) });
        HashMap<String, String> queryParams = parameters(new String[] { "revision" },
                new String[] { currRevision.toString() });
        return sendGetRequest(Path.GetAccountCodePath, uriParams, queryParams, AccountCode.class);
    }

    /**
     * Get storage at key
     * 
     * @param address  {@link Address} required the account address.
     * @param key      {@link StorageKey} required key.
     * @param revision {@link Revision} optional block revision.
     * @return {@link StorageKey} storage key.
     * @throws ClientIOException if network error or invalid request.
     */
    public static StorageData getStorageAt(Address address, StorageKey key, Revision revision)
            throws ClientIOException {
        if (address == null) {
            throw ClientArgumentException.exception("Address account is null");
        }
        if (key == null) {
            throw ClientArgumentException.exception("key is null");
        }
        Revision currRevision = revision;
        if (currRevision == null) {
            currRevision = Revision.BEST;
        }
        HashMap<String, String> uriParams = parameters(new String[] { "address", "key" },
                new String[] { address.toHexString(Prefix.ZeroLowerX), key.hexKey() });
        HashMap<String, String> queryParams = parameters(new String[] { "revision" },
                new String[] { currRevision.toString() });
        return sendGetRequest(Path.GetStorageValuePath, uriParams, queryParams, StorageData.class);
    }

}
