package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;
import java.util.ArrayList;

public class FilteredLogEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3469622661699885493L;
	private String address;
	private ArrayList<String> topics;
	private String data;
	private LogMeta meta;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ArrayList<String> getTopics() {
		return topics;
	}

	public void setTopics(ArrayList<String> topics) {
		this.topics = topics;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public LogMeta getMeta() {
		return meta;
	}

	public void setMeta(LogMeta meta) {
		this.meta = meta;
	}
}
