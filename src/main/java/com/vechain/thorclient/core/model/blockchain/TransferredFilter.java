package com.vechain.thorclient.core.model.blockchain;

import java.util.ArrayList;

public class TransferredFilter {
    private Range range;
    private Options options;
    private ArrayList<TransferCriteria> criteriaSet;
    private String order;

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
