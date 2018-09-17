package com.vechain.thorclient.clients;

import com.vechain.thorclient.clients.base.AbstractClient;
import com.vechain.thorclient.clients.base.SubscribeSocket;
import com.vechain.thorclient.clients.base.SubscribingCallback;
import com.vechain.thorclient.core.model.blockchain.*;
import com.vechain.thorclient.utils.URLUtils;

import java.util.HashMap;

public class SubscribeClient extends AbstractClient {



    /**
     * Subscribe the block event.
     * @param request can be null, the best block is by default.
     * @param callback the callback instance.
     * @return
     */
    public static SubscribeSocket subscribeBlock(BlockSubscribingRequest request,
                                                 SubscribingCallback<BlockSubscribingResponse> callback) throws Exception {

        String url = compositeSubscribeURI(Path.GetSubBlockPath.getPath(), request);
        return subscribeSocketConnect( url, callback );
    }

    /**
     * Subscribe the event log.
     * @param request the query request.
     * @param callback the callback instance.
     * @return
     */
    public static SubscribeSocket subscribeEvent(EventSubscribingRequest request, SubscribingCallback<EventSubscribingResponse> callback) throws Exception {
        String url = compositeSubscribeURI(Path.GetSubEventPath.getPath(), request);
        return subscribeSocketConnect( url, callback );
    }

    /**
     * Subscribe the transfer log.
     * @param request the query request.
     * @param callback the callback instance.
     * @return
     */
    public static SubscribeSocket subscribeTransfer(TransferSubscribingRequest request, SubscribingCallback<TransferSubscribingResponse> callback) throws Exception {
        String url = compositeSubscribeURI(Path.GetSubTransferPath.getPath(), request );
        return subscribeSocketConnect( url, callback );
    }


    public static  String compositeSubscribeURI(String path, WSRequest request) throws IllegalAccessException {
        String url = NodeProvider.getNodeProvider().getWsProvider() + path;
        if (request != null) {
            HashMap<String, String> queryMap = request.compositeRequestHashMap();
            if (queryMap.size() > 0) {
                return URLUtils.urlComposite( url, null, queryMap );
            }
        }
        return URLUtils.urlComposite( url, null, null);
    }
}
