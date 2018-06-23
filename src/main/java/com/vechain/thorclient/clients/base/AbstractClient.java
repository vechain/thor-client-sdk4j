package com.vechain.thorclient.clients.base;


import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.core.model.blockchain.NodeProvider;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.StringUtils;
import com.vechain.thorclient.utils.URLUtils;

import java.io.IOException;
import java.util.HashMap;

public abstract class AbstractClient {

    private final static String APPLICATION_JSON_VALUE = "application/json";
    public enum Path{
        //Accounts
        GetAccountPath("/accounts/{address}"),
        PostContractCallPath("/accounts/{address}"),
        PostDeployContractPath("/accounts"),
        GetAccountCodePath("/accounts/{address}/code"),
        GetStorageValuePath("/accounts/{address}/storage/{key}"),

        //Transactions
        GetTransactionPath("/transactions/{id}"),
        GetTransactionReceipt("/transactions/{id}/receipt"),
        PostTransaction("/transactions"),

        //Blocks
        GetBlockPath("/blocks/{revision}"),

        //Events
        PostFilterEventsLogPath("/events"),

        //Transfers
        PostFilterTransferLogPath("/transfers"),

        //Nodes
        GetNodeInfoPath("/node/network/peers"),

        ;
        private final String value;

        Path(String value){
            this.value = value;
        }

        public String getPath() {
            return  value;
        }

    }

    private static String rawUrl(Path path){
        return NodeProvider.getNodeProvider().getProvider(  ) + path.getPath();
    }

    /**
     * Get the request
     * @param path  {@link Path}
     * @param uriParams uri parameters
     * @param queryParams query string parameters
     * @param tClass the class of result java object.
     * @param <T> Type of result java object.
     * @return response java object, could be null, mean can not find any result.
     * @throws IOException node is not reachable or request is not valid.
     */
    public static  <T> T sendGetRequest(Path path, HashMap<String, String> uriParams, HashMap<String, String> queryParams, Class<T> tClass) throws IOException{
        String rawURL = rawUrl( path );
        String getURL =  URLUtils.urlComposite(rawURL, uriParams, queryParams);
        String response =  URLUtils.get(null, APPLICATION_JSON_VALUE, "utf-8", getURL);
        if(StringUtils.isBlank( response )){
            return null;
        }
        return JSON.parseObject(response, tClass);
    }

    /**
     * Post the request
     * @param path {@link Path}
     * @param uriParams uri parameters
     * @param queryParams query string parameters
     * @param tClass  the class of result java object.
     * @param <T> Type of result java object.
     * @return response java object, could be null, mean can not find any result.
     * @throws IOException http status 4xx means not enough energy amount.
     */
    public static <T> T sendPostRequest(Path path, HashMap<String, String> uriParams, HashMap<String, String> queryParams, Object postBody, Class<T> tClass) throws IOException{
        String rawURL = rawUrl( path );
        String postURL =  URLUtils.urlComposite(rawURL, uriParams, queryParams);
        String postString = JSON.toJSONString( postBody );
        String response =  URLUtils.post(postString, APPLICATION_JSON_VALUE, "utf-8", postURL);
        if(StringUtils.isBlank( response )){
            return null;
        }
        return JSON.parseObject(response, tClass);
    }


    protected  static HashMap<String , String> parameters(String[] keys, String[] values){
        if(keys == null || values == null || keys.length != values.length){
            throw ClientArgumentException.exception( "Parameters creating failed" );
        }

        HashMap<String, String> params = new HashMap<>();
        for(int index = 0; index < keys.length; index++){
            params.put( keys[index], values[index] );
        }
        return params;
    }


}
