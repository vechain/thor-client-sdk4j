package com.vechain.thorclient.core.model;

/**
 * @author albertma
 */
public class NodeProvider {

    public NodeProvider(){
        this.port = 80;
    }

    /**
     * get provider url
     * @return
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Set provider url
     * @param provider
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * get connect timeout
     * @return
     */
    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private String provider;
    private int timeout;



    private int port;
}
