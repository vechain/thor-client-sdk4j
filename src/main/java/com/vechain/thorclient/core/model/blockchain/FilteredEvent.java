package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;
import java.util.ArrayList;

@Deprecated
public class FilteredEvent implements Serializable {
	private static final long serialVersionUID = 8427904650485554238L;

	private ArrayList<String> topics;
	private String data;
	private LogMeta meta;

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
