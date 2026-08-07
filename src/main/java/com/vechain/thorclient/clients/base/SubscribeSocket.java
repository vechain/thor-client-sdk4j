package com.vechain.thorclient.clients.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.extensions.IExtension;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * A live subscription to a Thor node.
 *
 * <p>
 * The underlying WebSocket implementation is held by composition rather than
 * inheritance so that no third-party type appears in this class's public API.
 * That is deliberate: the previous Jetty-based implementation exposed
 * {@code org.eclipse.jetty.websocket} types through its constructor and
 * callback, which meant Jetty could not be replaced without breaking callers.
 * </p>
 *
 * <p>
 * Callbacks are delivered on the WebSocket read thread, not the thread that
 * created the subscription. Implementations of {@link SubscribingCallback} must
 * therefore be thread-safe with respect to their own state.
 * </p>
 *
 * @param <T> the deserialized subscription response type
 */
public class SubscribeSocket<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeSocket.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /** RFC 6455 normal closure status code. */
    public static final int NORMAL_CLOSURE = 1000;

    /**
     * Caps a single inbound frame, preserving the 16 MiB limit the previous Jetty
     * implementation declared via {@code maxTextMessageSize}. Without a cap, a
     * faulty or hostile node could drive unbounded heap allocation in the client.
     */
    private static final int MAX_FRAME_SIZE = 16 * 1024 * 1024;

    private final SubscribingCallback<T> callback;

    private final Connection connection;

    /**
     * @param serverUri the {@code ws://} or {@code wss://} subscription endpoint
     * @param callback  receives connection, message and close events
     */
    public SubscribeSocket(final URI serverUri, final SubscribingCallback<T> callback) {
        if (serverUri == null) {
            throw new IllegalArgumentException("serverUri must not be null");
        }
        if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        this.callback = callback;
        this.connection = new Connection(serverUri);
    }

    /**
     * Whether the subscription is currently open. A subscription that failed to
     * connect, was closed locally, or was dropped by the node reports false.
     */
    public boolean isConnected() {
        return connection.isOpen();
    }

    /**
     * Close the subscription, sending a close frame to the node.
     *
     * @param status  RFC 6455 status code, e.g. {@link #NORMAL_CLOSURE}
     * @param message human-readable close reason
     */
    public void close(final int status, final String message) {
        LOGGER.info("Closing subscription: {} {}", status, message);
        connection.close(status, message);
    }

    /**
     * Blocks until the connection is established or the timeout elapses.
     *
     * @return true if the subscription is open
     */
    boolean connectBlocking(final long timeout, final TimeUnit unit) throws InterruptedException {
        return connection.connectBlocking(timeout, unit);
    }

    /**
     * Releases resources for a subscription that never opened. Distinct from
     * {@link #close}, which sends a close frame over an established connection.
     */
    void abort() {
        connection.close();
    }

    private final class Connection extends org.java_websocket.client.WebSocketClient {

        Connection(final URI serverUri) {
            super(serverUri, new Draft_6455(Collections.<IExtension>emptyList(), MAX_FRAME_SIZE));
        }

        @Override
        public void onOpen(final ServerHandshake handshake) {
            LOGGER.info("Subscription connected: {}", getURI());
            callback.onConnect(SubscribeSocket.this);
        }

        @Override
        public void onMessage(final String message) {
            try {
                final T response = OBJECT_MAPPER.readValue(message, callback.responseClass());
                callback.onSubscribe(response);
            } catch (Exception e) {
                // Runs on the read thread, where an escaping exception would tear down the
                // subscription instead of reaching the caller. A single undecodable frame
                // must not end an otherwise healthy stream.
                LOGGER.error("Failed to handle subscription message", e);
            }
        }

        @Override
        public void onClose(final int code, final String reason, final boolean remote) {
            LOGGER.info("Subscription closed: {} {} remote={}", code, reason, remote);
            callback.onClose(code, reason);
        }

        @Override
        public void onError(final Exception ex) {
            LOGGER.error("Subscription error", ex);
        }
    }
}
