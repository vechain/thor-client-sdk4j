package com.vechain.thorclient.clients.base;

import org.eclipse.jetty.websocket.api.Session;

public interface SubscribingCallback<T> {
    void onClose(int statusCode, String reason);
    void onConnect(Session session);
    Class<T> responseClass();
    void onSubscribe(T response);
}

