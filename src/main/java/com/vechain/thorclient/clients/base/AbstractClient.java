package com.vechain.thorclient.clients.base;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vechain.thorclient.clients.AccountCall;
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

    /** Bound on the subscription WebSocket handshake, independent of the REST timeout. */
    private static final long SUBSCRIBE_CONNECT_TIMEOUT_SECONDS = 10;

    public enum Path {

        // Accounts

        GetAccountPath("/accounts/{address}"),
        PostContractCallPath("/accounts/{address}"),
        PostDeployContractPath(
                "/accounts"),
        PostAccountCallPath("/account"),
        PostInspectClauses("/accounts/*"),
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

    static {
        setTimeout(5000);
    }

    private static String rawUrl(Path path) {
        return NodeProvider.getNodeProvider().getProvider() + path.getPath();
    }

    /**
     * Set the connect and read timeout applied to every subsequent REST request.
     *
     * @param timeout milliseconds
     */
    public static void setTimeout(int timeout) {
        LOGGER.debug("setTimeout: {}", timeout);
        HttpTransport.setTimeout(timeout);
    }

    /**
     * Set only the connect timeout applied to every subsequent REST request.
     *
     * @param timeout milliseconds
     */
    public static void setConnectTimeout(int timeout) {
        LOGGER.debug("setConnectTimeout: {}", timeout);
        HttpTransport.setConnectTimeout(timeout);
    }

    /**
     * Set only the read (socket) timeout applied to every subsequent REST request.
     *
     * @param timeout milliseconds
     */
    public static void setReadTimeout(int timeout) {
        LOGGER.debug("setReadTimeout: {}", timeout);
        HttpTransport.setReadTimeout(timeout);
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
            return parseResult(tClass, HttpTransport.get(getURL));
        } catch (IOException e) {
            throw new ClientIOException(e);
        }
    }

    private static <T> T parseResult(
            final Class<T> tClass,
            final HttpTransport.Response response) throws ClientIOException {
        final int status = response.getStatus();
        final String body = response.getBody();
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
                return parseResult(tClass, HttpTransport.post(postURL, postJSON));
            } catch (IOException e) {
                throw new ClientIOException(e);
            }
        } catch (JsonProcessingException e) {
            throw new ClientIOException(e);
        }
    }

    /**
     * Open a subscription connection.
     *
     * <p>
     * <strong>This does not throw when the connection fails.</strong> A node that
     * is unreachable, refuses the port, or fails the TLS handshake still yields a
     * {@link SubscribeSocket} - one that is closed and will never deliver a
     * callback. Callers must check {@link SubscribeSocket#isConnected()} before
     * relying on the result, and {@link SubscribeSocket#getLastError()} reports
     * why it failed.
     * </p>
     *
     * @param url      long live connection url.
     * @param callback {@link SubscribingCallback} receiving subscription events.
     * @param <T>      type of the deserialized subscription response.
     * @return {@link SubscribeSocket}, which may not be connected; see above.
     * @throws Exception if the url is malformed or the arguments are invalid.
     */
    public static <T> SubscribeSocket<T> subscribeSocketConnect(String url, SubscribingCallback<T> callback)
            throws Exception {
        if (StringUtils.isBlank(url) || callback == null) {
            throw new ClientIOException("Invalid arguments ");
        }
        final SubscribeSocket<T> subscribeSocket = new SubscribeSocket<T>(new URI(url), callback);
        try {
            LOGGER.info("subscribeSocketConnect start connect ... {}", url);
            if (subscribeSocket.connectBlocking(SUBSCRIBE_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                LOGGER.info("subscribeSocketConnect success: {}", url);
            } else {
                LOGGER.error("subscribeSocketConnect failed: {}", url, subscribeSocket.getLastError());
            }
        } catch (InterruptedException e) {
            // Restore the flag so callers up the stack can still observe the interrupt.
            Thread.currentThread().interrupt();
            LOGGER.error("subscribeSocketConnect interrupted", e);
        } catch (Exception e) {
            LOGGER.error("SubscribeSocket error", e);
        } finally {
            if (!subscribeSocket.isConnected()) {
                LOGGER.info("subscribeSocketConnect stop...");
                try {
                    subscribeSocket.abort();
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

    /**
     * Read contract state.
     * Simulate the execution of a transaction. This can be useful to determine if
     * your transaction may revert before submitting it.
     * Inspect the outputs of a transaction before executing it.
     * Estimate the gas consumption of a transaction. Note: The caller field should
     * be provided for higher accuracy.
     *
     * @param call     {@link AccountCall}
     * @param revision {@link Revision}
     * @return {@link ContractCallResult}
     * @throws ClientIOException network error
     */

    public static ContractCallResult[] readContract(AccountCall call, Revision revision)
            throws ClientIOException {
        Revision currentRevision = revision;
        if (currentRevision == null) {
            currentRevision = Revision.BEST;
        }

        HashMap<String, String> queryParams = parameters(new String[] { "revision" },
                new String[] { currentRevision.toString() });

        return sendPostRequest(Path.PostInspectClauses, null, queryParams, call, ContractCallResult[].class);
    }

}
