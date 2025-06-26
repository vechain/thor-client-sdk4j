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

public class ERC20ContractClient extends TransactionClient {

    /**
     * Get amount from ERC20 contract.
     *
     * @param address  address of token holder.
     * @param token    {@link ERC20Token} required, the token {@link ERC20Token}
     * @param revision {@link Revision} if it is null, it will fallback to default
     *                 {@link Revision#BEST}
     * @return {@link Amount}
     * @throws ClientIOException {@link ClientIOException}
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
                ERC20Token.VTHO,
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
    public static TransferResult transferERC20TokenEIP1559(
            Address[] receivers,
            Amount[] amounts,
            int gas,
            BigInteger maxFeePerGas,
            BigInteger maxPriorityFeePerGas,
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
        if (maxPriorityFeePerGas.compareTo(BigInteger.ZERO) < 0) {
            throw ClientArgumentException.exception("maxPriorityFeePerGas is too small.");
        }
        if (maxFeePerGas.compareTo(BigInteger.ZERO) < 0) {
            throw ClientArgumentException.exception("maxFeePerGas is too small.");
        }

        final ToClause clause = ERC20Contract.buildTranferToClause(
                ERC20Token.VTHO,
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

        System.out.println("SendVTHO Raw: " + BytesUtils.toHexString(rawTransaction.encode(), Prefix.ZeroLowerX));

        return TransactionClient.signThenTransfer(rawTransaction, keyPair);
    }

}
