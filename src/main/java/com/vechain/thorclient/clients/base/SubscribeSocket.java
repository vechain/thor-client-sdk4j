package com.vechain.thorclient.clients.base;

import com.alibaba.fastjson.JSONObject;
import org.eclipse.jetty.websocket.api.CloseStatus;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.lang.reflect.ParameterizedType;


@WebSocket(maxTextMessageSize = 16 * 1024* 1024)
public class SubscribeSocket<T> {
    private SubscribingCallback callback;
    @SuppressWarnings("unused")
    private Session session;

    public SubscribeSocket(SubscribingCallback callback)
    {
        this.callback = callback;
    }

    public void close(int status, String message){
        CloseStatus closeStatus = new CloseStatus( status, message );
        this.session.close(closeStatus);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason)
    {
        System.out.printf("Connection closed: %d - %s%n",statusCode,reason);
        if(this.callback != null){
            this.callback.onClose(statusCode, reason);
        }
        this.callback = null;
        this.session = null;
    }

    @OnWebSocketConnect
    public void onConnect(Session session)
    {
        System.out.printf("Got connect: %s%n",session);
        this.session = session;
        if (this.callback != null){
            this.callback.onConnect( session );
        }
    }

    @OnWebSocketMessage
    public void onMessage(String msg)
    {
        if (this.callback != null){
            Object obj =  JSONObject.parseObject(msg,  callback.responseClass());
            callback.onSubscribe( obj );
        }
    }
}
