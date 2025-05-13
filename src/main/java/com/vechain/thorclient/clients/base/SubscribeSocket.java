package com.vechain.thorclient.clients.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.jetty.websocket.api.CloseStatus;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

@WebSocket(maxTextMessageSize = 16 * 1024 * 1024)
public class SubscribeSocket<T> {

	private static Logger logger = LoggerFactory.getLogger(SubscribeSocket.class);
	private SubscribingCallback callback;
	@SuppressWarnings("unused")
	private Session session;
	private WebSocketClient client;

	public SubscribeSocket(WebSocketClient client, SubscribingCallback callback) {
		this.client = client;
		this.callback = callback;
	}

	public void close(int status, String message) {
		logger.info("close: {} {} {}", status, session, message);
		if (this.session != null) {
			CloseStatus closeStatus = new CloseStatus(status, message);
			this.session.close(closeStatus);
		}
		clean();
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		logger.info("Connection closed: {} {}", statusCode, reason);
		if (this.callback != null) {
			this.callback.onClose(statusCode, reason);
		}
		clean();
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		logger.info("Got connect: {} ", session);
		this.session = session;
		if (this.callback != null) {
			this.callback.onConnect(session);
		}
	}

	@OnWebSocketMessage
	public void onMessage(String msg) throws JsonProcessingException {
		if (this.callback != null) {
			logger.info("onMessage: {} ", msg);
			Object obj = JSONObject.parseObject(msg, callback.responseClass());
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
				logger.info("client closed...");
				this.client.stop();
				logger.info("client closed success");
			} catch (Exception e) {
				logger.error("WebSocketClient close error", e);
			} finally {
				this.client = null;
			}
		}
	}
}
