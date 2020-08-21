package com.vechain.thorclient.core.model.blockchain;

import com.vechain.thorclient.clients.base.AbstractClient;
import com.vechain.thorclient.utils.StringUtils;

/**
 * The blockchain node provider pojo object. Set and get url and connecting
 * timeout(in milliseconds).
 */
public class NodeProvider {

	private static NodeProvider INSTANCE = new NodeProvider();

	public static NodeProvider getNodeProvider() {
		return INSTANCE;
	}

	private NodeProvider() {
		this.socketTimeout = 5000;
		this.connectTimeout = 5000;
	}

	/**
	 * get provider url
	 * 
	 * @return the url of the nodes.
	 */
	public String getProvider() {
		String url = StringUtils.isBlank(getProviderThreadLocal()) ? this.provider : getProviderThreadLocal();
		if (StringUtils.isBlank(url) || !url.startsWith("http")) {
			throw new RuntimeException("The blockchain provider url must be set.");
		}
		return url;
	}

	/**
	 * Set provider url
	 * 
	 * @param provider the completed url of the nodes
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * set socket timeout
	 * 
	 * @return milliseconds
	 */
	public int getSocketTimeout() {
		return socketTimeout;
	}

	/**
	 * Set socket timeout
	 * 
	 * @param timeout milliseconds
	 */
	public void setSocketTimeout(int timeout) {
		this.socketTimeout = timeout;
	}

	/**
	 * Set connect timeout, socket timeout
	 * 
	 * @param timeout milliseconds
	 */
	public void setTimeout(int timeout) {
		AbstractClient.setTimeout(timeout);
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getConnectTimeout() {
		return this.connectTimeout;
	}

	public String getWsProvider() {
		if (StringUtils.isBlank(this.wsProvider) || !this.wsProvider.startsWith("ws")) {
			throw new RuntimeException("The blockchain provider url must be set.");
		}
		return wsProvider;
	}

	public void setWsProvider(String wsProvider) {
		this.wsProvider = wsProvider;
	}

	private String provider;

	private String wsProvider;
	private int socketTimeout;
	private int connectTimeout;
	private static ThreadLocal<String> providerThreadLocal = new ThreadLocal<String>();

	public void setProviderThreadLocal(String url) {
		providerThreadLocal.set(url);
	}

	public String getProviderThreadLocal() {
		return providerThreadLocal.get();
	}

	public void cleanProviderThreadLocal() {
		providerThreadLocal.remove();
	}
}
