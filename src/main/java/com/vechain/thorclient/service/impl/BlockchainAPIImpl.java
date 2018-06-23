package com.vechain.thorclient.service.impl;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.utils.crypto.ECKeyPair;
import com.vechain.thorclient.utils.crypto.ECDSASign;
import com.vechain.thorclient.core.model.blockchain.*;
import com.vechain.thorclient.service.BlockchainAPI;
import com.vechain.thorclient.utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author albertma
 */

public class BlockchainAPIImpl implements BlockchainAPI {

    private final static String APPLICATION_JSON_VALUE = "application/json";


    private NodeProvider provider;


    public void setProvider(NodeProvider provider) {
		this.provider = provider;
	}

    @Override
    public Block getBlockByNumber(long blockNumber) throws IOException {
        return this.getBlockByRevision(blockNumber+ "");
    }

    @Override
    public Block getBlockById(String blockId) throws IOException {
        return this.getBlockByRevision( blockId );
    }

    @Override
    public Block getBestBlock() throws IOException {
        return this.getBlockByRevision( "best" );
    }

    private Block getBlockByRevision(String revision) throws IOException {
        if(!BlockchainUtils.isValidRevision(String.valueOf(revision))){
            throw new IllegalArgumentException("blockNumber is not a legal argument");
        }

        if (provider == null){
            throw new IllegalStateException( "Provider is not set." );
        }
        String path = this.getUriPath( APIPath.getBlockPath());
        HashMap<String, String> pathMap = new HashMap<>();
        pathMap.put( "revision", revision);
        String url = URLUtils.urlComposite( path, pathMap, null );
        String response =  URLUtils.get(null, null, null, url);
        return JSON.parseObject(response, Block.class );

	}

	@Override
	public Account getBalance(String accountAddr, String revision) throws IOException {

        if (this.provider == null){
            throw new IllegalStateException( "Provider is not set." );
        }

        if (!BlockchainUtils.isAddress( accountAddr )){
            throw new IllegalArgumentException( "Address is not illegal." );
        }
        String hexAddress = BlockchainUtils.convertToHexAddress( accountAddr );

        //Add url parameter
        String path = this.getUriPath( APIPath.getBalanceAccountPath());
        HashMap<String, String> uriParams = new HashMap<>();
        uriParams.put("address", hexAddress);

        HashMap<String, String> queryMap = new HashMap<>();
        if(BlockchainUtils.isValidRevision(revision)) {
            //Add query parameter
            queryMap.put( "revision", revision);
        }

        String url = URLUtils.urlComposite( path, uriParams, queryMap );
        String response =  URLUtils.get(null, null, null, url);
        if(StringUtils.isBlank( response )) {
            return  null;
        }
        return JSON.parseObject(response, Account.class );
	}

    @Override
    public StorageData getStorageAt(String accountAddr, String key, String revision) throws IOException {

        if (this.provider == null){
            throw new IllegalStateException( "Provider is not set." );
        }

        if (!BlockchainUtils.isAddress( accountAddr )){
            throw new IllegalArgumentException( "accountAddr is not illegal." );
        }
        String hexAddress = BlockchainUtils.convertToHexAddress( accountAddr );
        if (StringUtils.isBlank(key)){
            throw new IllegalArgumentException( "key is not illegal" );
        }
        String path = this.getUriPath( APIPath.getStorageAccountPath());
        HashMap<String , String > uriParams = new HashMap<>(  );

        uriParams.put("address", hexAddress );
        uriParams.put("key", key);

        HashMap<String, String> queryMap = new HashMap<>( );
        if (BlockchainUtils.isValidRevision( revision )){
            queryMap.put( "revision", revision );
        }
        String url = URLUtils.urlComposite( path, uriParams, queryMap );
        String response =  URLUtils.get(null, null, null, url);
        if(StringUtils.isBlank( response )) {
            return  null;
        }
        return JSON.parseObject(response, StorageData.class );
    }

    @Override
    public ContractCallResult callContract(String contractAddress, String revision, ContractCall call) throws IOException {
        String path = this.getUriPath( APIPath.getContractCallPath());
        if(StringUtils.isBlank(path)){
            throw new IllegalArgumentException("No request url");
        }

        if(!BlockchainUtils.isAddress( contractAddress )) {
            throw new IllegalArgumentException("ContractAddress is not valid.");
        }
        String hexAddress = BlockchainUtils.convertToHexAddress( contractAddress );
        if (call == null){
            throw new IllegalArgumentException("ContractCall is not valid.");
        }
        HashMap<String, String> uriParams = new HashMap<>( );
        uriParams.put( "address", hexAddress );

        HashMap<String, String> queryParams = new HashMap<>(  );
        if(BlockchainUtils.isValidRevision( revision )){
            queryParams.put( "revision" , revision);
        }
        String url = URLUtils.urlComposite( path, uriParams, queryParams );
        String callString = JSON.toJSONString( call );
        String response = URLUtils.post(callString, APPLICATION_JSON_VALUE,"utf-8", url );
        if(StringUtils.isBlank( response )){
            return null;
        }
        ContractCallResult result = JSON.parseObject( response, ContractCallResult.class );
        return result;


    }


    @Override
    public Transaction getTransaction(String txId, boolean isRaw, String revision) throws IOException {
        if(!BlockchainUtils.isId(txId)){
            throw new IllegalArgumentException("Illegal transaction id.");
        }

        //Add url parameter
        String path = this.getUriPath( APIPath.getTransactionPath());
        HashMap<String, String> uriParams = new HashMap<>();
        uriParams.put("id", txId);


        HashMap<String, String> queryMap = new HashMap<>();
        if(BlockchainUtils.isValidRevision(revision)) {
            //Add query parameter
            queryMap.put( "revision", revision);
        }

        String url = URLUtils.urlComposite( path, uriParams, queryMap );
        String response =  URLUtils.get(null, null, null, url);
        if(StringUtils.isBlank( response )){
            return null;
        }
        return JSON.parseObject(response, Transaction.class );
    }

	@Override
	public String signAndSendRawTransaction(RawTransaction rawTransaction, ECKeyPair keyPair) throws IOException {
        String path = this.getUriPath( APIPath.getSendingTransactionPath());
        if(StringUtils.isBlank(path)){
            throw new IllegalArgumentException("No request url");
        }
        if(rawTransaction.getSignature() == null && keyPair == null) {
            throw new IllegalArgumentException("Can not sign the txRaw without keypair.");
        }else if (rawTransaction.getSignature() == null){
            byte[] rawTx = RLPUtils.encodeRawTransaction(rawTransaction);
            ECDSASign.SignatureData signature = ECDSASign.signMessage(rawTx, keyPair);
            rawTransaction.setSignature(signature.toByteArray());
        }
        byte[] signRawTx = RLPUtils.encodeRawTransaction(rawTransaction);
        if(signRawTx != null){
            TransferRequest request = new TransferRequest();
            request.setRaw(BytesUtils.toHexString(signRawTx, Prefix.ZeroLowerX));
            String postString = JSON.toJSONString( request);
            String response = URLUtils.post( postString, APPLICATION_JSON_VALUE,"utf-8", path );
            if(StringUtils.isBlank( response )){
                return null;
            }
            TransferResult result = JSON.parseObject( response, TransferResult.class );
            return result.getId();
        }else{
            return null;
        }
	}


    @Override
    public Receipt getTransactionReceipt(String txId) throws IOException {
        return getTransactionReceipt(txId, null);
    }

	@Override
	public Receipt getTransactionReceipt(String txId, String revision) throws IOException {
        if(!BlockchainUtils.isId(txId)){
            throw new IllegalArgumentException("Illegal txId, must be a hex string with 0x prefix");
        }
        //Add url parameter
        String path = this.getUriPath( APIPath.getTransactionReceiptPath());
        HashMap<String, String> uriParams = new HashMap<>();
        uriParams.put("id", txId);

        HashMap<String, String> queryMap = new HashMap<>();
        if(BlockchainUtils.isValidRevision(revision)) {
            //Add query parameter
            queryMap.put( "revision", revision);
        }

        String url = URLUtils.urlComposite( path, uriParams, queryMap );
        String response =  URLUtils.get(null, null, null, url);
        if(StringUtils.isBlank( response )){
            return null;
        }
        return JSON.parseObject(response, Receipt.class );
	}

    @Override
    public byte getChainTag() throws IOException {
        Block genesisBlock = this.getBlockByNumber(0);
        if(genesisBlock == null){
            return -1;
        }
        ThorClientLogger.debug("Genesis block id:" + genesisBlock.getId());
        byte[] blockId = BytesUtils.toByteArray(genesisBlock.getId());
        if(blockId.length != 32) {
            return -1;
        }
        return blockId[31];
    }

    @Override
    public byte[] getBestBlockRef() throws IOException {
        Block block = this.getBestBlock();
        ThorClientLogger.debug("block info: " + JSON.toJSONString(block));
        String blockId = block.getId();
        byte[] blockIdBytes = BytesUtils.toByteArray(blockId);
        byte[] blockRefBytes = new byte[8];
        System.arraycopy(blockIdBytes, 0, blockRefBytes, 0, 8);

        blockRefBytes = BytesUtils.trimLeadingZeroes(blockRefBytes);
        return blockRefBytes;
    }

    @Override
    public ArrayList<FilteredEvent> getFilterEvent(OrderFilter order, String address, EventFilter eventFilter) throws IOException {
        String path = this.getUriPath( APIPath.getEventPath());

        HashMap<String, String> queryMap = new HashMap<>();

        if(order != null) {
            queryMap.put( "order", order.getValue() );

        }
        if(BlockchainUtils.isAddress(address)) {
            queryMap.put("address", address);
        }


        String url = URLUtils.urlComposite( path, null, queryMap );
        String postString = JSON.toJSONString( eventFilter );
        String response = URLUtils.post( postString, APPLICATION_JSON_VALUE,"utf-8", url);
        if(StringUtils.isBlank( response )){
            return null;
        }
        ArrayList<FilteredEvent> events = JSON.parseObject( response,  new ArrayList<FilteredEvent>().getClass());
        return events;

    }

    @Override
    public ArrayList<TransferLog> getTransferLog(OrderFilter order, TransferFilter transferFilter) throws IOException {
        String path = this.getUriPath( APIPath.getTransferPath());
        HashMap<String, String> queryMap = new HashMap<>();
        if(order != null) {
            queryMap.put( "order", order.getValue() );
        }
        String url = URLUtils.urlComposite( path, null, queryMap );
        String postString = JSON.toJSONString( transferFilter );
        String response = URLUtils.post( postString, APPLICATION_JSON_VALUE,"utf-8", url);
        if(StringUtils.isBlank( response )){
            return null;
        }
        ArrayList<TransferLog> logs = JSON.parseObject( response,  new ArrayList<TransferLog>().getClass());
        return logs;
    }


    private String getUriPath(String path){
        return this.provider.getProvider() + path;
    }


}


