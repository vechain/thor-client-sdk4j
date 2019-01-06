package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;
import java.util.ArrayList;

import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.utils.BlockchainUtils;
import com.vechain.thorclient.utils.Prefix;

public class TransferFilter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2715720246287162258L;
	private Range range;
	private Options options;
	private ArrayList<AddressSet> criteriaSet;

	/**
	 * Create TransferFilter
	 * 
	 * @param range
	 *            {@link Range} range from and to
	 * @param options
	 *            {@link Options} offset and limit.
	 * @return {@link TransferFilter} filter.
	 */
	public static TransferFilter createFilter(Range range, Options options) {
		if (range == null) {
			throw new IllegalArgumentException("Invalid range");
		}
		if (options == null) {
			throw new IllegalArgumentException("Invalid options");
		}
		TransferFilter transferFilter = new TransferFilter();
		transferFilter.range = range;
		transferFilter.options = options;
		return transferFilter;
	}

	private TransferFilter() {
		this.criteriaSet = new ArrayList<AddressSet>();

	}

	public void addAddressSet(Address txOrigin, Address sender, Address recipient) {
		AddressSet addressSet = new AddressSet();
		if (txOrigin != null) {
			addressSet.setTxOrigin(txOrigin.toHexString(Prefix.ZeroLowerX));
		}
		if (sender != null) {
			addressSet.setSender(sender.toHexString(Prefix.ZeroLowerX));
		}
		if (recipient != null) {
			addressSet.setRecipient(recipient.toHexString(Prefix.ZeroLowerX));
		}
		criteriaSet.add(addressSet);

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

	public ArrayList<AddressSet> getCriteriaSet() {
		return criteriaSet;
	}

	public void setCriteriaSet(ArrayList<AddressSet> criteriaSet) {
		this.criteriaSet = criteriaSet;
	}
}
