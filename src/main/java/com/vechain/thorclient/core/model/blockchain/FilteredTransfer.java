package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;

@Deprecated
public class FilteredTransfer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6053746933706373378L;
	private String sender;
	private String recipient;
	private String amount;
	private LogMeta meta;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public LogMeta getMeta() {
		return meta;
	}

	public void setMeta(LogMeta meta) {
		this.meta = meta;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
