package com.vechain.thorclient.core.model.blockchain;

import java.util.ArrayList;

import com.vechain.thorclient.utils.BlockchainUtils;

public class LogFilter {
	private Range range;
	private Options options;
	private ArrayList<EventCriteria> criteriaSet = new ArrayList<EventCriteria>();
	private String order;

	public static LogFilter createFilter(Range range, Options options) {
		LogFilter eventFilter = new LogFilter();
		eventFilter.range = range;
		eventFilter.options = options;
		return eventFilter;
	}

	/**
	 * Add topic filter
	 * 
	 * @param topic0 method ABI full code:keccak256(function_signature)
	 * @param topic1 function parameter 66bit hex string,can be null
	 * @param topic2 function parameter 66bit hex string,can be null
	 * @param topic3 function parameter 66bit hex string,can be null
	 * @param topic4 function parameter 66bit hex string,can be null
	 */
	public void addTopicSet(String address, String topic0, String topic1, String topic2, String topic3, String topic4) {
		if (topic0 != null && !BlockchainUtils.isId(topic0)) {
			throw new IllegalArgumentException("Invalid topic0");
		}
		if (topic1 != null && !BlockchainUtils.isId(topic1)) {
			throw new IllegalArgumentException("Invalid topic1");
		}
		if (topic2 != null && !BlockchainUtils.isId(topic2)) {
			throw new IllegalArgumentException("Invalid topic2");
		}
		if (topic3 != null && !BlockchainUtils.isId(topic3)) {
			throw new IllegalArgumentException("Invalid topic3");
		}
		if (topic4 != null && !BlockchainUtils.isId(topic4)) {
			throw new IllegalArgumentException("Invalid topic4");
		}

		EventCriteria aEventCriteria = new EventCriteria();
		aEventCriteria.setAddress(address);
		aEventCriteria.setTopic0(topic0);
		aEventCriteria.setTopic1(topic1);
		aEventCriteria.setTopic2(topic2);
		aEventCriteria.setTopic3(topic3);
		aEventCriteria.setTopic4(topic4);
		criteriaSet.add(aEventCriteria);
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

	public ArrayList<EventCriteria> getCriteriaSet() {
		return criteriaSet;
	}

	public void setCriteriaSet(ArrayList<EventCriteria> criteriaSet) {
		this.criteriaSet = criteriaSet;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
