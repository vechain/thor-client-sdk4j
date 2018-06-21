package com.vechain.thorclient.service;

import com.vechain.thorclient.core.crypto.ECKeyPair;
import com.vechain.thorclient.core.model.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The Blockchain API is a service interface for client to get or post request to the remote blockchain url.
 * The client must set {@link NodeProvider} at first.
 * @author albertma
 */
public interface BlockchainAPI {

	/**
	 * Set provider for the blockchain nodes
	 * @param provider {@link NodeProvider} provider of blockchain
	 */
	void setProvider(NodeProvider provider);

	/**
	 * get block by block number.
	 * @param blockNumber integer value, the block number.
	 * @return {@link Block}
	 */
	Block getBlockByNumber(long blockNumber) throws IOException;

	/**
	 * get block by block id.
	 * @param blockId the hex string with prefix string "0x", the specified block Id
	 * @return {@link Block}
	 */
	Block getBlockById(String blockId) throws IOException;

	/**
	 * get best block number.
	 * @return {@link Block}
	 */
	Block getBestBlock() throws IOException;

	/**
	 * get balance from an address and block revision.
	 * @param accountAddr the account address, a hex string like "0x7567D83b7b8d80ADdCb281A71d54Fc7B3364ffed"
	 * @param revision it could be null or blockId with prefix 0x, or block number or "best".
	 * @return {@link Account}
	 */
	Account getBalance(String accountAddr, String revision) throws IOException;


	/**
	 * Get storage at account address
	 * @param accountAddr required, a hex string.
	 * @param key required, sha3(input = "64 address"+"64 variable position")
	 * @param revision optional
	 * @return
	 */
	StorageData getStorageAt(String accountAddr,String key, String revision) throws IOException;


	/**
	 * Call contract by the {@link ContractCall}
	 * @param contractAddress required the contract address, a hex string.
	 * @param revision optional block revision, could be null.
	 * @param call required, the {@link ContractCall}
	 * @return
	 * @throws IOException
	 */
	ContractCallResult callContract(String contractAddress, String revision, ContractCall call) throws IOException;

    /**
     * Send raw transaction with keyPair to sign.
     * @param rawTransaction raw transaction
     * @param keyPair {@link ECKeyPair}
     * @return transactionId
     */
    String signAndSendRawTransaction(RawTransaction rawTransaction, ECKeyPair keyPair) throws IOException;


	/**
	 * get transaction by transaction
	 * @param txId   transaction id hex string which is blake2b hash of sign hash byte array appending signer address byte array.
	 * @param isRaw  true the return value is rlp encoded, false the return value is transaction.
	 * @param revision  the block revision which is block number, block id or "best", could be null.
	 * @return {@link Transaction}
	 */
    Transaction getTransaction(String txId, boolean isRaw, String revision) throws IOException;

    /**
     * get transaction receipt
     * @param txId   transaction id hex string which is blake2b hash of sign hash byte array appending signer address byte array.
     * @param revision  block revision which is block number, block id or "best", could be null.
     * @return {@link Receipt}
     */
    Receipt getTransactionReceipt(String txId, String revision) throws IOException;

	/**
	 * get transaction receipt
	 * @param txId transaction id which value is like "0xdd2e72852f1cc6c349d5424d8f898c72e749d22249b24cbdd7831bffdef8cdba"
	 * @return {@link Receipt}
	 */
	Receipt getTransactionReceipt(String txId) throws IOException;

	/**
	 * get chain tag which is the last byte of genesis block id.
	 * @return the tag of blockchain which is the last byte of genesis block id. If return -1 then it is failed.
	 */
	byte getChainTag() throws IOException;


	/**
	 * get block reference from best block
	 * @return 8 bytes length byte array which is the best block reference.
	 */
	byte[] getBestBlockRef() throws IOException;


    /**
     * get the event of contracts which are filtered by {@link OrderFilter}, address and {@link EventFilter}
     * @param order {@link OrderFilter}
     * @param address the address hex string which is like "0x7567d83b7b8d80addcb281a71d54fc7b3364ffed"
     * @param eventFilter {@link EventFilter}
     * @return ArrayList<{@link FilteredEvent}>
     */
    ArrayList<FilteredEvent> getFilterEvent(OrderFilter order, String address, EventFilter eventFilter) throws IOException;


	/**
	 * get transfer logs which are filtered by  {@link OrderFilter} and {@link TransferFilter}
	 * @param order {@link OrderFilter}
	 * @param transferFilter {@link TransferFilter}
	 * @return ArrayList<{@link TransferLog}>
	 */
    ArrayList<TransferLog> getTransferLog(OrderFilter order, TransferFilter transferFilter) throws IOException;
}
