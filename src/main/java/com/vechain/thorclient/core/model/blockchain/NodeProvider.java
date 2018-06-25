package com.vechain.thorclient.core.model.blockchain;

import com.vechain.thorclient.utils.StringUtils;

/**
 * The blockchain node provider pojo object.
 * Set and get url and connecting timeout(in milliseconds).
 */
public class NodeProvider {

    private static NodeProvider INSTANCE = new NodeProvider();

    public static NodeProvider getNodeProvider() {
        return INSTANCE;
    }

    private NodeProvider() {
        this.timeout = 5000;
    }

    /**
     * get provider url
     * @return the url of the nodes.
     */
    public String getProvider() {
        if (StringUtils.isBlank(this.provider) || !this.provider.startsWith("http")) {
            throw new RuntimeException("The blockchain provider url must be set.");
        }
        return provider;
    }

    /**
     * Set provider url
     * @param provider the completed url of the nodes
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * get connect timeout
     * @return milliseconds
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Set connect timeout
     * @param timeout milliseconds
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private String provider;
    private int    timeout;
}
