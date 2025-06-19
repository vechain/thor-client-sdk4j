package com.vechain.thorclient.clients;

import java.math.BigInteger;
import java.util.HashMap;

import com.vechain.thorclient.clients.base.AbstractClient;
import com.vechain.thorclient.core.model.blockchain.Receipt;
import com.vechain.thorclient.core.model.blockchain.Transaction;
import com.vechain.thorclient.core.model.blockchain.TransferRequest;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.BlockRef;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.core.model.clients.Revision;
import com.vechain.thorclient.core.model.clients.ToClause;
import com.vechain.thorclient.core.model.clients.ToData;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.core.model.exception.ClientIOException;
import com.vechain.thorclient.utils.BlockchainUtils;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.Prefix;
import com.vechain.thorclient.utils.RawTransactionFactory;
import com.vechain.thorclient.utils.StringUtils;
import com.vechain.thorclient.utils.crypto.ECDSASign;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

public class TransactionClient extends AbstractClient {

    public final static int ContractGasLimit = 21000;

    /**
     * Get transaction by transaction Id.
     *
     * @param txId     required transaction id .
     * @param isRaw    is response raw transaction.
     * @param revision {@link Revision} revision.
     * @return Transaction {@link Transaction}
     * @throws ClientIOException
     */
    public static Transaction getTransaction(String txId, boolean isRaw, Revision revision) throws ClientIOException {
        if (!BlockchainUtils.isId(txId)) {
            throw ClientArgumentException.exception("Tx id is invalid");
        }
        Revision currRevision = revision;
        if (currRevision != null && currRevision.toString().equals(Revision.BEST.toString())) {
            currRevision = null;
        }
        HashMap<String, String> uriParams = parameters(new String[]{"id"}, new String[]{txId});
        HashMap<String, String> queryParams = parameters(new String[]{"head", "raw"},
                new String[]{currRevision == null ? null : currRevision.toString(), Boolean.toString(isRaw)});
        return sendGetRequest(Path.GetTransactionPath, uriParams, queryParams, Transaction.class);
    }

    /**
     * Get transaction receipt
     *
     * @param txId     txId hex string start with "0x"
     * @param revision {@link Revision}
     * @return {@link Receipt} return Receipt .
     * @throws ClientIOException
     */
    public static Receipt getTransactionReceipt(String txId, Revision revision) throws ClientIOException {
        if (!BlockchainUtils.isId(txId)) {
            throw ClientArgumentException.exception("Tx id is invalid");
        }
        Revision currRevision = revision;
        if (currRevision != null && currRevision.toString().equals(Revision.BEST.toString())) {
            currRevision = null;
        }
        HashMap<String, String> uriParams = parameters(new String[]{"id"}, new String[]{txId});
        return sendGetRequest(Path.GetTransactionReceipt, uriParams, currRevision == null ? null
                : parameters(new String[]{"head"}, new String[]{currRevision.toString()}), Receipt.class);
    }

    /**
     * Transfer amount, raw transaction will be encoded by rlp encoder and convert
     * to hex string with prefix "0x". Then the hex string will be packaged into
     * {@link TransferRequest} bean object and serialized to JSON string.
     *
     * @param transaction {@link RawTransaction} raw transaction to to send
     * @return {@link TransferResult}
     * @throws ClientIOException network error, 5xx http status means request error,
     *                           4xx http status means no enough gas or balance.
     */
    public static TransferResult transfer(final RawTransaction transaction) throws ClientIOException {
        if (transaction == null || transaction.getSignature() == null) {
            ClientArgumentException.exception("Raw transaction is invalid");
        }
        byte[] rawBytes = transaction.encode();
        if (rawBytes == null) {
            throw ClientArgumentException.exception("Raw transaction is encode error");
        }
        String hexRaw = BytesUtils.toHexString(rawBytes, Prefix.ZeroLowerX);
        TransferRequest request = new TransferRequest();
        request.setRaw(hexRaw);
        return sendPostRequest(Path.PostTransaction, null, null, request, TransferResult.class);
    }

    /**
     * Send the transaction hex string.
     *
     * @param rawTransactionHexString hex string of raw transaction.
     * @return {@link TransferResult}
     * @throws ClientIOException
     */
    public static TransferResult transfer(final String rawTransactionHexString) throws ClientIOException {

        if (!StringUtils.isHex(rawTransactionHexString)) {
            throw ClientArgumentException.exception("Raw transaction is encode error");
        }
        TransferRequest request = new TransferRequest();
        request.setRaw(rawTransactionHexString);
        return sendPostRequest(Path.PostTransaction, null, null, request, TransferResult.class);
    }

    /**
     * Send transaction bytes.
     *
     * @param rawTransactionBytes byte array.
     * @return {@link TransferResult}
     * @throws ClientIOException
     */
    public static TransferResult transfer(final byte[] rawTransactionBytes) throws ClientIOException {
        if (rawTransactionBytes == null) {
            throw ClientArgumentException.exception("rawTransaction byte array is null.");
        }
        String hexString = BytesUtils.toHexString(rawTransactionBytes, Prefix.ZeroLowerX);
        return transfer(hexString);
    }

    /**
     * Sign the raw transaction.
     *
     * @param rawTransaction {@link RawTransaction}
     * @return {@link RawTransaction} with signature.
     */
    public static RawTransaction sign(RawTransaction rawTransaction, ECKeyPair keyPair) {
        if (rawTransaction == null || keyPair == null) {
            throw ClientArgumentException.exception("raw transaction object is invalid.");
        }
        ECDSASign.SignatureData signature = ECDSASign.signMessage(rawTransaction.encode(), keyPair, true);
        byte[] signBytes = signature.toByteArray();
        rawTransaction.setSignature(signBytes);
        return rawTransaction;
    }

    /**
     * Delegator signs the transaction.
     *
     * @param rawTransaction {@link RawTransaction}
     * @param keyPair
     * @return
     */
    public static RawTransaction delegatorSign(RawTransaction rawTransaction, ECKeyPair keyPair) {
        if (rawTransaction == null || keyPair == null) {
            throw ClientArgumentException.exception("raw transaction object or keyPair is invalid.");
        }
        if (!rawTransaction.getReserved().isDelegationFeature()) {
            throw ClientArgumentException
                    .exception("raw transaction has no delegation feature, the raw transaction is invalid.");
        }
        byte[] delegatorSigningHash = BlockchainUtils.delegatorSigningHash(rawTransaction);
        ECDSASign.SignatureData signatureData = ECDSASign.signMessage(delegatorSigningHash, keyPair, false);
        byte[] delegationSignature = signatureData.toByteArray();
        if (delegationSignature != null) {
            byte[] signature = rawTransaction.getSignature();
            byte[] concatenatingSignature = BlockchainUtils.concatenateSignature(signature, delegationSignature);
            rawTransaction.setSignature(concatenatingSignature);
            return rawTransaction;
        } else {
            return null;
        }

    }

    /**
     * Sign and transfer the raw transaction.
     *
     * @param rawTransaction {@link RawTransaction} raw transaction without
     *                       signature data
     * @param keyPair        {@link ECKeyPair} the key pair for the private key.
     * @return {@link TransferResult}
     * @throws ClientIOException
     */
    public static TransferResult signThenTransfer(RawTransaction rawTransaction, ECKeyPair keyPair)
            throws ClientIOException {
        RawTransaction signedRawTxn = sign(rawTransaction, keyPair);
        return transfer(signedRawTxn);
    }

    /**
     * Build a transaction clause
     *
     * @param toAddress {@link Address} destination address.
     * @param amount    {@link Amount} amount to transfer.
     * @param data      {@link ToData} some comments maybe.
     * @return {@link ToClause} to clause.
     */
    public static ToClause buildVETToClause(Address toAddress, Amount amount, ToData data) {
        if (toAddress == null) {
            throw ClientArgumentException.exception("toAddress is null");
        }
        if (amount == null) {
            throw ClientArgumentException.exception("amount is null");
        }
        if (data == null) {
            throw ClientArgumentException.exception("data is null");
        }
        return new ToClause(toAddress, amount, data);
    }

    /**
     * Build deploying the contract codes.
     *
     * @param contractHex byte array
     * @return
     */
    public static ToClause buildDeployClause(String contractHex) {
        if (!StringUtils.isHex(contractHex)) {
            return null;
        }
        ToData toData = new ToData();
        toData.setData(contractHex);
        return new ToClause(Address.NULL_ADDRESS, Amount.ZERO, toData);
    }

    /**
     * Deploys a smart contract to the blockchain.
     *
     * @param contractHex the hexadecimal string of the contract code to be deployed.
     * @param gas         the gas limit to be used for the transaction.
     * @param gasCoef     the gas coefficient utilized for calculating the transaction fee.
     * @param expiration  the expiration time for the transaction, measured in blocks.
     * @param keyPair     the key pair used to sign the deployment transaction.
     * @return the result of the contract deployment, encapsulated in a {@link TransferResult}, which includes the transaction ID.
     * @throws IllegalArgumentException if the provided contract hex string is null.
     */
    public static TransferResult deployContract(
            final String contractHex,
            final int gas,
            final byte gasCoef,
            final int expiration,
            final ECKeyPair keyPair
    ) {
        final ToClause toClause = buildDeployClause(contractHex);
        if (toClause == null) {
            throw ClientArgumentException.exception("The contract hex string is null");
        }
        final ToClause[] toClauses = new ToClause[1];
        toClauses[0] = toClause;
        return invokeContractMethod(toClauses, gas, gasCoef, expiration, keyPair);
    }


    /**
     * Deploys a contract to the blockchain using the provided parameters.
     *
     * @param contractHex          the contract hex string to be deployed to the blockchain.
     * @param gas                  the gas limit to be used for the transaction.
     * @param maxPriorityFeePerGas the maximum priority fee (tip) per gas, used for faster inclusion in blocks.
     * @param maxFeePerGas         the maximum fee for the transaction, which includes the base fee and the priority fee.
     * @param expiration           the expiration time for the transaction, in blocks.
     * @param keyPair              the key pair to sign the deployment transaction, containing the private key.
     * @return {@link TransferResult} representing the result of the contract deployment, including the transaction ID.
     * @throws IllegalArgumentException if the contract hex string is null.
     */
    public static TransferResult deployContract(
            final String contractHex,
            final int gas,
            final BigInteger maxPriorityFeePerGas,
            final BigInteger maxFeePerGas,
            final int expiration,
            final ECKeyPair keyPair
    ) {
        final ToClause toClause = buildDeployClause(contractHex);
        if (toClause == null) {
            throw ClientArgumentException.exception("The contract hex string is null");
        }
        final ToClause[] toClauses = new ToClause[1];
        toClauses[0] = toClause;
        return invokeContractMethod(toClauses, gas, maxPriorityFeePerGas, maxFeePerGas, expiration, keyPair);
    }

    /**
     * Invokes a method on a smart contract and sends the transaction to the blockchain.
     *
     * @param toClauses  an array of {@link ToClause} representing transaction clauses,
     *                   detailing the target address, value, and additional data.
     * @param gas        the gas limit to be consumed by the transaction.
     * @param gasCoef    the gas coefficient used to calculate the transaction fee.
     * @param expiration the expiration time for the transaction, measured in blocks.
     * @param keyPair    the {@link ECKeyPair} used to sign the transaction, containing the private key.
     * @return {@link TransferResult} representing the result of the transaction, including its transaction ID.
     * @throws ClientIOException       if a network error occurs or if chainTag or bestRef cannot be fetched.
     * @throws ClientArgumentException if any input parameter (gas, gasCoef, expiration, toClauses, or keyPair) is invalid.
     */
    protected static TransferResult invokeContractMethod(
            final ToClause[] toClauses,
            final int gas,
            final byte gasCoef,
            final int expiration,
            final ECKeyPair keyPair
    ) throws ClientIOException {
        if (keyPair == null) {
            throw ClientArgumentException.exception("ECKeyPair is null.");
        }
        if (gas < ContractGasLimit) {
            throw ClientArgumentException.exception("gas is too small.");
        }
        if (gasCoef < 0) {
            throw ClientArgumentException.exception("gas coef is too small.");
        }
        if (expiration <= 0) {
            throw ClientArgumentException.exception("expiration is invalid.");
        }
        if (toClauses == null) {
            throw ClientArgumentException.exception("To clause is null");
        }
        final byte chainTag = BlockchainClient.getChainTag();
        final BlockRef bestRef = BlockchainClient.getBlockRef(null);
        if (bestRef == null || chainTag == 0) {
            throw new ClientIOException("Get chainTag: " + chainTag + " BlockRef: " + bestRef);
        }
        final RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(
                chainTag,
                bestRef.toByteArray(),
                expiration,
                gas,
                gasCoef,
                CryptoUtils.generateTxNonce(),
                toClauses);
        return TransactionClient.signThenTransfer(rawTransaction, keyPair);
    }

    /**
     * Invokes a method on a smart contract and sends the transaction to the blockchain.
     *
     * @param toClauses  an array of {@link ToClause} representing transaction clauses,
     *                   detailing the target address, value, and additional data.
     * @param gas        the gas limit to be consumed by the transaction.
     * @param maxPriorityFeePerGas  total vtho in wei willing to spend as a validator tip.
     * @param maxFeePerGas  total vtho in wei willing to spend on the transaction.
     * @param expiration the expiration time for the transaction, measured in blocks.
     * @param keyPair    the {@link ECKeyPair} used to sign the transaction, containing the private key.
     * @return {@link TransferResult} representing the result of the transaction, including its transaction ID.
     * @throws ClientIOException       if a network error occurs or if chainTag or bestRef cannot be fetched.
     * @throws ClientArgumentException if any input parameter (gas, gasCoef, expiration, toClauses, or keyPair) is invalid.
     */
    protected static TransferResult invokeContractMethod(
            final ToClause[] toClauses,
            final int gas,
            final BigInteger maxPriorityFeePerGas,
            final BigInteger maxFeePerGas,
            final int expiration,
            final ECKeyPair keyPair
    ) throws ClientIOException {
        if (keyPair == null) {
            throw ClientArgumentException.exception("ECKeyPair is null.");
        }
        if (gas < ContractGasLimit) {
            throw ClientArgumentException.exception("gas is too small.");
        }
        if (maxPriorityFeePerGas.compareTo(BigInteger.ZERO) < 0) {
            throw ClientArgumentException.exception("maxPriorityFeePerGas is too small.");
        }
        if (maxFeePerGas.compareTo(BigInteger.ZERO) < 0) {
            throw ClientArgumentException.exception("maxFeePerGas is too small.");
        }
        if (expiration <= 0) {
            throw ClientArgumentException.exception("expiration is invalid.");
        }
        if (toClauses == null) {
            throw ClientArgumentException.exception("To clause is null");
        }
        final byte chainTag = BlockchainClient.getChainTag();
        final BlockRef bestRef = BlockchainClient.getBlockRef(null);
        if (bestRef == null || chainTag == 0) {
            throw new ClientIOException("Get chainTag: " + chainTag + " BlockRef: " + bestRef);
        }
        final RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(
                chainTag,
                bestRef.toByteArray(),
                expiration,
                gas,
                maxPriorityFeePerGas,
                maxFeePerGas,
                CryptoUtils.generateTxNonce(),
                toClauses);
        return TransactionClient.signThenTransfer(rawTransaction, keyPair);
    }

}
