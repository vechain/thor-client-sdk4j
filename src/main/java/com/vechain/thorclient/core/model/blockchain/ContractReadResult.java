package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.base.AbstractToken;

public class ContractReadResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2695526756954990834L;
	private String data;
	private ArrayList<Event> events;
	private ArrayList<Transfer> transfers;

	private BigInteger gasUsed;
	private boolean reverted;
	private String vmError;


	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	public ArrayList<Transfer> getTransfers() {
		return transfers;
	}

	public void setTransfers(ArrayList<Transfer> transfers) {
		this.transfers = transfers;
	}

	public boolean isReverted() {
		return reverted;
	}

	public void setReverted(boolean reverted) {
		this.reverted = reverted;
	}

	public String getVmError() {
		return vmError;
	}

	public void setVmError(String vmError) {
		this.vmError = vmError;
	}

	public Amount getBalance(AbstractToken token) {
		Amount balance = Amount.createFromToken(token);
		balance.setHexAmount(data);
		return balance;
	}

	public BigInteger getGasUsed() {
		return gasUsed;
	}

	public void setGasUsed(BigInteger gasUsed) {
		this.gasUsed = gasUsed;
	}

}
