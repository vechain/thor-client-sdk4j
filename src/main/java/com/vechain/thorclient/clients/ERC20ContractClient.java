package com.vechain.thorclient.clients;

import com.vechain.thorclient.core.model.blockchain.ContractCall;
import com.vechain.thorclient.core.model.blockchain.ContractCallResult;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.*;
import com.vechain.thorclient.core.model.clients.base.AbiDefinition;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.core.model.exception.ClientIOException;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.Prefix;
import com.vechain.thorclient.utils.RawTransactionFactory;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * ERC20ContractClient provides specialized operations for ERC20 token contracts on VeChain.
 * 
 * <p>This client extends TransactionClient to provide convenient methods for interacting with 
 * ERC20 token contracts, including balance queries and token transfers. It supports both the 
 * native VTHO token and custom ERC20 tokens deployed on VeChain.</p>
 * 
 * <h3>Supported Tokens</h3>
 * <ul>
 *   <li><strong>VTHO</strong>: Native VeChain energy token ({@link ERC20Token#VTHO})</li>
 *   <li><strong>Custom ERC20</strong>: Any ERC20-compliant token contract</li>
 * </ul>
 * 
 * <h3>Transaction Types</h3>
 * <p>Supports both legacy and EIP-1559 (Galactica) transaction types:</p>
 * <ul>
 *   <li><strong>Legacy</strong>: Uses gas price coefficient</li>
 *   <li><strong>EIP-1559</strong>: Uses maxFeePerGas and maxPriorityFeePerGas</li>
 * </ul>
 * 
 * <h3>Usage Examples</h3>
 * <pre>{@code
 * // Get VTHO balance
 * Amount balance = ERC20ContractClient.getERC20Balance(
 *     Address.fromHexString("0xYourAddress"),
 *     ERC20Token.VTHO,
 *     null
 * );
 * System.out.println("VTHO Balance: " + balance.getDecimalAmount());
 * 
 * // Transfer VTHO tokens (EIP-1559)
 * TransferResult result = ERC20ContractClient.transferERC20Token(
 *     ERC20Token.VTHO,
 *     new Address[]{Address.fromHexString("0xRecipient")},
 *     new Amount[]{Amount.createFromToken(ERC20Token.VTHO).setDecimalAmount("100")},
 *     80000,
 *     new BigInteger("1000000000"), // maxFeePerGas
 *     new BigInteger("1000000000"), // maxPriorityFeePerGas
 *     720,
 *     keyPair
 * );
 * System.out.println("Transfer TX: " + result.getId());
 * }</pre>
 * 
 * @see ERC20Token
 * @see Amount
 * @see TransactionClient
 * @since 0.1.0
 */
public class ERC20ContractClient extends TransactionClient {

    /**
     * Retrieves the ERC20 token balance for a specific address.
     * 
     * <p>Queries the token contract's balanceOf function to get the current token balance.
     * The returned amount includes proper decimal formatting based on the token's decimals.</p>
     * 
     * @param address the token holder's address to query
     * @param token the ERC20 token specification including contract address and decimals.
     *              Use {@link ERC20Token#VTHO} for VTHO or create custom token instances
     * @param revision the block revision for the query. Use {@code null} for latest block,
     *                 {@link Revision#BEST} for best block, or {@link Revision#create(long)}
     *                 for a specific block number
     * @return the token balance with proper decimal formatting
     * @throws ClientIOException if there's a network error or the contract call fails
     * 
     * @see ERC20Token#VTHO
     * @see Amount#getDecimalAmount()
     * @see Revision#BEST
     */
    public static Amount getERC20Balance(Address address, ERC20Token token, Revision revision)
            throws ClientIOException {
        Address contractAddr = token.getContractAddress();
        Revision currRevision = revision;
        if (currRevision == null) {
            currRevision = Revision.BEST;
        }
        AbiDefinition abiDefinition = ERC20Contract.defaultERC20Contract.findAbiDefinition("balanceOf");
        ContractCall call = ERC20Contract.buildCall(abiDefinition, address.toHexString(null));

        // Create a proper AccountCall object
        AccountCall accountCall = new AccountCall();
        ArrayList<ToClause> clauses = new ArrayList<>();
        clauses.add(new ToClause(
                contractAddr,
                Amount.ZERO, // No VET transfer needed for read-only call
                new ToData(call.getData()) // Use the data from the ContractCall
        ));
        accountCall.setClauses(clauses);

        // Use the readContract method with the proper AccountCall object
        ContractCallResult[] contractCallResult = readContract(accountCall, currRevision);

        if (contractCallResult == null) {
            return null;
        }
        return contractCallResult[0].getBalance(token);
    }

    /**
     * Transfer ERC20 token
     *
     * @param receivers  {@link Address} array
     * @param amounts    {@link Amount} array
     * @param gas        gas at least 7000
     * @param gasCoef    gas coef
     * @param expiration expiration
     * @param keyPair    your private key.
     * @return {@link TransferResult}
     * @throws ClientIOException
     */
    public static TransferResult transferERC20Token(
            ERC20Token token,
            Address[] receivers,
            Amount[] amounts,
            int gas,
            byte gasCoef,
            int expiration,
            ECKeyPair keyPair) throws ClientIOException {
        if (receivers == null) {
            throw ClientArgumentException.exception("receivers is null");
        }
        if (amounts == null) {
            throw ClientArgumentException.exception("amounts is null");
        }
        if (receivers.length != amounts.length) {
            throw ClientArgumentException.exception("receivers length equal to amounts length.");
        }

        final ToClause clause = ERC20Contract.buildTranferToClause(
                token,
                receivers[0], amounts[0]);

        final byte chainTag = BlockchainClient.getChainTag();
        final byte[] blockRef = BlockClient.getBlock(null).blockRef().toByteArray();

        final RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(
                chainTag,
                blockRef,
                720,
                80000,
                (byte) 0x0,
                CryptoUtils.generateTxNonce(),
                clause);

        return TransactionClient.signThenTransfer(rawTransaction, keyPair);
    }

    /**
     * Transfer ERC20 token using EIP-1559 transaction
     *
     * @param token                {@link ERC20Token} token to transfer
     * @param receivers            {@link Address} array
     * @param amounts              {@link Amount} array
     * @param gas                  gas at least 7000
     * @param maxFeePerGas         The maximum fee per gas in wei units allowed for
     *                             the transaction
     * @param maxPriorityFeePerGas The maximum priority fee in wei units for miners
     * @param expiration           expiration
     * @param keyPair              your private key.
     * @return {@link TransferResult}
     * @throws ClientIOException
     */
    public static TransferResult transferERC20Token(
            ERC20Token token,
            Address[] receivers,
            Amount[] amounts,
            int gas,
            BigInteger maxFeePerGas,
            BigInteger maxPriorityFeePerGas,
            int expiration,
            ECKeyPair keyPair) throws ClientIOException {
        if (token == null) {
            throw ClientArgumentException.exception("token is null");
        }
        if (receivers == null) {
            throw ClientArgumentException.exception("receivers is null");
        }
        if (amounts == null) {
            throw ClientArgumentException.exception("amounts is null");
        }
        if (receivers.length != amounts.length) {
            throw ClientArgumentException.exception("receivers length equal to amounts length.");
        }
        if (maxPriorityFeePerGas.compareTo(BigInteger.ZERO) < 0) {
            throw ClientArgumentException.exception("maxPriorityFeePerGas is too small.");
        }
        if (maxFeePerGas.compareTo(BigInteger.ZERO) < 0) {
            throw ClientArgumentException.exception("maxFeePerGas is too small.");
        }

        final ToClause clause = ERC20Contract.buildTranferToClause(
                token,
                receivers[0], amounts[0]);

        final byte chainTag = BlockchainClient.getChainTag();
        final byte[] blockRef = BlockClient.getBlock(null).blockRef().toByteArray();

        // Create raw transaction with explicit null dependsOn field to ensure proper
        // EIP-1559 structure
        RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(
                chainTag,
                blockRef,
                expiration,
                gas,
                maxFeePerGas,
                maxPriorityFeePerGas,
                CryptoUtils.generateTxNonce(),
                clause);

        rawTransaction.setDependsOn(null);

        return TransactionClient.signThenTransfer(rawTransaction, keyPair);
    }

}
