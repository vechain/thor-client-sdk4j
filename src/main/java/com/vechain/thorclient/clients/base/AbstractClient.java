package com.vechain.thorclient.clients.base;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public abstract class AbstractClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractClient.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public enum Path {

        // Accounts

        GetAccountPath("/accounts/{address}"),
        PostContractCallPath("/accounts/{address}"),
        PostDeployContractPath(
                "/accounts"),
        PostAccountCallPath("/account"),
        GetAccountCodePath(
                "/accounts/{address}/code"),

        GetStorageValuePath("/accounts/{address}/storage/{key}"),

        // Transactions
        GetTransactionPath("/transactions/{id}"), GetTransactionReceipt("/transactions/{id}/receipt"),
        PostTransaction("/transactions"),

        // Blocks
        GetBlockPath("/blocks/{revision}"),

        // Fees
        GetFeeHistoryPath("/fees/history"),
        GetFeePriorityPath("/fees/priority"),

        // Events
        PostFilterEventsLogPath("/logs/event"),

        // Transfers
        PostFilterTransferLogPath("/logs/transfer"),

        // Nodes
        GetNodeInfoPath("/node/network/peers"),

        // SubscribeSocket
        GetSubBlockPath("/subscriptions/block"), GetSubEventPath("/subscriptions/event"),
        GetSubTransferPath("/subscriptions/transfer"),
        ;

        private final String value;

        Path(String value) {
            this.value = value;
        }

        public String getPath() {
            return value;
        }

    }

    protected WebSocketClient client = new WebSocketClient();

    static {
        setTimeout(5000);
    }

    private static String rawUrl(Path path) {
        return NodeProvider.getNodeProvider().getProvider() + path.getPath();
    }

    public static void setTimeout(int timeout) {
        LOGGER.warn("setTimeout: " + timeout);
        Unirest.setTimeouts(timeout, timeout);
    }

    /**
     * Get the request
     *
     * @param path        {@link Path}
     * @param uriParams   uri parameters
     * @param queryParams query string parameters
     * @param tClass      the class of result java object.
     * @param <T>         Type of result java object.
     * @return response java object, could be null, mean cannot find any result.
     * @throws IOException if the node is not reachable or the request is not valid.
     */
    public static <T> T sendGetRequest(
            final Path path,
            final HashMap<String, String> uriParams,
            final HashMap<String, String> queryParams,
            final Class<T> tClass) throws ClientIOException {
        final String rawURL = rawUrl(path);
        final String getURL = URLUtils.urlComposite(rawURL, uriParams, queryParams);
        try {
            final HttpResponse<String> jsonNode = Unirest.get(getURL).asString();
            return parseResult(tClass, jsonNode);
        } catch (UnirestException e) {
            throw new ClientIOException(e);
        }
    }

    private static <T> T parseResult(
            final Class<T> tClass,
            final HttpResponse<String> jsonNode) throws ClientIOException {
        final int status = jsonNode.getStatus();
        final String body = jsonNode.getBody();
        if (status != 200) {
            String exception_msg = "response exception";
            if (status == 400) {
                exception_msg = "bad request";
            } else if (status == 403) {
                exception_msg = "request forbidden";
            }
            ClientIOException clientIOException = new ClientIOException(
                    exception_msg + " " + body);
            clientIOException.setHttpStatus(status);
            throw clientIOException;
        } else
            try {
                return OBJECT_MAPPER.readValue(body, tClass);
            } catch (JsonProcessingException e) {
                throw new ClientIOException(e);
            }

    }

    /**
     * Post the request
     *
     * @param path        {@link Path}
     * @param uriParams   uri parameters
     * @param queryParams query string parameters
     * @param tClass      the class of result java object.
     * @param <T>         Type of result java object.
     * @return response java object, could be null, mean cannot find any result.
     * @throws ClientIOException http status 4xx means not enough energy amounts.
     */
    public static <T> T sendPostRequest(
            final Path path, HashMap<String, String> uriParams,
            final HashMap<String, String> queryParams,
            final Object postBody,
            final Class<T> tClass) throws ClientIOException {
        final String rawURL = rawUrl(path);
        final String postURL = URLUtils.urlComposite(rawURL, uriParams, queryParams);
        try {
            final String postJSON = OBJECT_MAPPER.writeValueAsString(postBody);
            try {
                final HttpResponse<String> jsonNode = Unirest.post(postURL).body(postJSON).asString();
                return parseResult(tClass, jsonNode);
            } catch (UnirestException e) {
                throw new ClientIOException(e);
            }
        } catch (JsonProcessingException e) {
            throw new ClientIOException(e);
        }
    }

    /**
     * Make connection for subscription.
     *
     * @param url      long live connection url.
     * @param callback {@link SubscribeSocket}
     * @return {@link SubscribeSocket}
     * @throws Exception
     */
    public static SubscribeSocket subscribeSocketConnect(String url, SubscribingCallback<?> callback) throws Exception {
        if (StringUtils.isBlank(url) || callback == null) {
            throw new ClientIOException("Invalid arguments ");
        }
        WebSocketClient client = new WebSocketClient();
        SubscribeSocket subscribeSocket = new SubscribeSocket(client, callback);
        try {
            LOGGER.info("subscribeSocketConnect start connect ... {}", url);
            client.start();
            URI subUri = new URI(url);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            Future<Session> f = client.connect(subscribeSocket, subUri, request);
            LOGGER.info("subscribeSocketConnect end connect...");
            Session s = f.get(10, TimeUnit.SECONDS);
            if (s.isOpen()) {
                LOGGER.info("subscribeSocketConnect success: {}", s.getRemoteAddress().toString());
            } else {
                LOGGER.error("subscribeSocketConnect failed: " + s.isOpen());
            }
        } catch (Exception e) {
            LOGGER.error("SubscribeSocket error", e);
        } finally {
            if (!subscribeSocket.isConnected()) {
                LOGGER.info("subscribeSocketConnect stop...");
                try {
                    subscribeSocket.close(0, "WebSocket can't connect to: " + url);
                } catch (Exception e) {
                    LOGGER.error("SubscribeSocket stop error", e);
                }
            }
        }
        return subscribeSocket;
    }

    protected static HashMap<String, String> parameters(String[] keys, String[] values) {
        if (keys == null || values == null || keys.length != values.length) {
            throw ClientArgumentException.exception("Parameters creating failed");
        }

        HashMap<String, String> params = new HashMap<>();
        for (int index = 0; index < keys.length; index++) {
            params.put(keys[index], values[index]);
        }
        return params;
    }

    /**
     * Call the contract view function or try to run the transaction to see the
     * gas-used.
     *
     * @param call            {@link ContractCall}
     * @param contractAddress {@link Address}
     * @param revision        {@link Revision}
     * @return {@link ContractCallResult}
     * @throws ClientIOException network error
     */

    @Deprecated
    public static ContractCallResult callContract(ContractCall call, Address contractAddress, Revision revision)
            throws ClientIOException {
        Revision currentRevision = revision;
        if (currentRevision == null) {
            currentRevision = Revision.BEST;
        }

        HashMap<String, String> uriParams = parameters(new String[] { "address" },
                new String[] { contractAddress.toHexString(Prefix.ZeroLowerX) });
        HashMap<String, String> queryParams = parameters(new String[] { "revision" },
                new String[] { currentRevision.toString() });

        return sendPostRequest(Path.PostContractCallPath, uriParams, queryParams, call, ContractCallResult.class);
    }

}
