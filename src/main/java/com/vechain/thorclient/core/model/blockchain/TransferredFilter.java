package com.vechain.thorclient.core.model.blockchain;

import java.util.ArrayList;

import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.utils.Prefix;

public class TransferredFilter {
	private Range range;
	private Options options;
	private ArrayList<TransferCriteria> criteriaSet = new ArrayList<TransferCriteria>();
	private String order;

	/**
	 * Create TransferFilter
	 * 
	 * @param range   {@link Range} range from and to
	 * @param options {@link Options} offset and limit.
	 * @return {@link TransferFilter} filter.
	 */
	public static TransferredFilter createFilter(Range range, Options options) {
		if (range == null) {
			throw new IllegalArgumentException("Invalid range");
		}
		if (options == null) {
			throw new IllegalArgumentException("Invalid options");
		}
		TransferredFilter transferFilter = new TransferredFilter();
		transferFilter.range = range;
		transferFilter.options = options;
		return transferFilter;
	}

	/**
	 * add Transfer Criteria
	 * 
	 * @param txOrigin
	 * @param sender
	 * @param recipient
	 */
	public void addTransferCriteria(Address txOrigin, Address sender, Address recipient) {
		TransferCriteria aTransferCriteria = new TransferCriteria();
		if (txOrigin != null) {
			aTransferCriteria.setTxOrigin(txOrigin.toHexString(Prefix.ZeroLowerX));
		}
		if (sender != null) {
			aTransferCriteria.setSender(sender.toHexString(Prefix.ZeroLowerX));
		}
		if (recipient != null) {
			aTransferCriteria.setRecipient(recipient.toHexString(Prefix.ZeroLowerX));
		}
		criteriaSet.add(aTransferCriteria);
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	public ArrayList<TransferCriteria> getCriteriaSet() {
		return criteriaSet;
	}

	public void setCriteriaSet(ArrayList<TransferCriteria> criteriaSet) {
		this.criteriaSet = criteriaSet;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
