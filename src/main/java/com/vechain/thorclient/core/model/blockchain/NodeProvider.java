package com.vechain.thorclient.core.model.blockchain;

import com.vechain.thorclient.utils.StringUtils;

/**
 * @author albertma
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
     * 
     * @return
     */
    public String getProvider() {
        if (StringUtils.isBlank(this.provider) || !this.provider.startsWith("http")) {
            throw new RuntimeException("The blockchain provider url must be set.");
        }
        return provider;
    }

    /**
     * Set provider url
     * 
     * @param provider
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * get connect timeout
     * 
     * @return
     */
    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private String provider;
    private int    timeout;
}
