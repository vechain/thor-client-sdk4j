package com.vechain.thorclient.clients.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.CloseStatus;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket(maxTextMessageSize = 16 * 1024 * 1024)
public class SubscribeSocket<T> {

    private static Logger LOGGER = LoggerFactory.getLogger(SubscribeSocket.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private SubscribingCallback callback;

    private Session session;

    private WebSocketClient client;

    public SubscribeSocket(WebSocketClient client, SubscribingCallback callback) {
        this.client = client;
        this.callback = callback;
    }

    public void close(int status, String message) {
        LOGGER.info("close: {} {} {}", status, session, message);
        if (this.session != null) {
            CloseStatus closeStatus = new CloseStatus(status, message);
            this.session.close(closeStatus);
        }
        clean();
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        LOGGER.info("Connection closed: {} {}", statusCode, reason);
        if (this.callback != null) {
            this.callback.onClose(statusCode, reason);
        }
        clean();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        LOGGER.info("Got connect: {} ", session);
        this.session = session;
        if (this.callback != null) {
            this.callback.onConnect(session);
        }
    }

    @OnWebSocketMessage
    public void onMessage(String msg) throws JsonProcessingException {
        if (this.callback != null) {
            LOGGER.info("onMessage: {} ", msg);
            final Object obj = OBJECT_MAPPER.readValue(msg, callback.responseClass());
            callback.onSubscribe(obj);
        }
    }

    public boolean isConnected() {
        return this.session != null;
    }

    private void clean() {
        this.callback = null;
        this.session = null;
        if (this.client != null) {
            try {
                LOGGER.info("client closed...");
                this.client.stop();
                LOGGER.info("client closed success");
            } catch (Exception e) {
                LOGGER.error("WebSocketClient close error", e);
            } finally {
                this.client = null;
            }
        }
    }
}
