package com.vechain.thorclient.clients.base;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Receives events for a {@link SubscribeSocket}.
 *
 * <p>
 * All methods are invoked on the WebSocket read thread, so implementations must
 * be thread-safe with respect to their own state and should not block.
 * </p>
 *
 * @param <T> the deserialized subscription response type
 */
public interface SubscribingCallback<T> {

    void onClose(int statusCode, String reason);

    /**
     * Called once the subscription is established.
     *
     * @param socket the now-open subscription, e.g. to close it from the callback
     */
    void onConnect(SubscribeSocket<T> socket);

    Class<T> responseClass();

    void onSubscribe(T response) throws JsonProcessingException;
}
