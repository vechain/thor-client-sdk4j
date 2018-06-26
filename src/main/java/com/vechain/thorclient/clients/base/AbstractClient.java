package com.vechain.thorclient.clients.base;


import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.vechain.thorclient.core.model.blockchain.ContractCall;
import com.vechain.thorclient.core.model.blockchain.ContractCallResult;
import com.vechain.thorclient.core.model.blockchain.NodeProvider;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Revision;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.core.model.exception.ClientIOException;
import com.vechain.thorclient.utils.Prefix;
import com.vechain.thorclient.utils.StringUtils;
import com.vechain.thorclient.utils.URLUtils;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
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
    public static  <T> T sendGetRequest(Path path, HashMap<String, String> uriParams, HashMap<String, String> queryParams, Class<T> tClass) throws ClientIOException {
        String rawURL = rawUrl( path );
        String getURL =  URLUtils.urlComposite(rawURL, uriParams, queryParams);
        try {
            String response =  URLUtils.get(null, APPLICATION_JSON_VALUE, "utf-8", getURL);
            if(StringUtils.isBlank( response )){
                return null;
            }
            return JSON.parseObject(response, tClass);
        }catch (IOException ex){
            throw new ClientIOException(ex);
        }

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
    public static <T> T sendPostRequest(Path path, HashMap<String, String> uriParams, HashMap<String, String> queryParams, Object postBody, Class<T> tClass) throws ClientIOException {
        Unirest.setTimeouts(NodeProvider.getNodeProvider().getConnectTimeout(), NodeProvider.getNodeProvider().getSocketTimeout());
        String rawURL = rawUrl( path );
        String postURL =  URLUtils.urlComposite(rawURL, uriParams, queryParams);
        String postString = JSON.toJSONString( postBody );
        HttpResponse<JsonNode> jsonNode = null;
        try {
            jsonNode =  Unirest.post( postURL ).body( postString ).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw ClientIOException.create(e);
        }
        int status = jsonNode.getStatus();
        if(status != 200){
            String exception_msg = "response exception";
            if(status == 400){
                exception_msg = "bad request";
            }else if( status == 403){
                exception_msg = "request forbidden";
            }
            ClientIOException clientIOException = ClientIOException.create(exception_msg);
            clientIOException.setHttpStatus( status );
            throw  clientIOException;
        }else{
            String  response = jsonNode.getBody().toString();
            return JSON.parseObject( response, tClass );
        }

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


    /**
     * Call the contract view function.
     * @param call {@link ContractCall}
     * @param contractAddress {@link Address}
     * @param revision {@link Revision}
     * @return {@link ContractCallResult}
     * @throws ClientIOException network error
     */
    public static ContractCallResult callContract(ContractCall call, Address contractAddress, Revision revision)throws ClientIOException {
        Revision currentRevision = revision;
        if(currentRevision == null){
            currentRevision = Revision.BEST;
        }

        HashMap<String, String> uriParams = parameters( new String[]{"address"}, new String[]{ contractAddress.toHexString( Prefix.ZeroLowerX )} );
        HashMap<String, String> queryParams = parameters(new String[]{"revision"}, new String[]{currentRevision.toString()}   );

        return sendPostRequest( Path.PostContractCallPath, uriParams, queryParams,call, ContractCallResult.class);
    }


}
