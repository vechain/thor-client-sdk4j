package com.vechain.thorclient.core.model.blockchain;

import java.util.ArrayList;

public class LogFilter {
    private Range range;
    private Options options;
    private ArrayList<EventCriteria> criteriaSet;
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
