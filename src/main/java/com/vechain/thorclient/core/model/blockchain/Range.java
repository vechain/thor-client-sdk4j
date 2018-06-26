package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;

public class Range implements Serializable {
    private String unit;
    private long from;
    private long to;

    public static Range createBlockRange(long from, long to){
        if(from >= to){
            throw new IllegalArgumentException( "Range from to error." );
        }
        Range range = new Range();
        range.from = from;
        range.to = to;
        range.unit = "block";
        return range;
    }
    public static  Range createTimeRange(long from, long to){
        if(from >= to){
            throw new IllegalArgumentException( "Range from to error." );
        }
        Range range = new Range();
        range.from = from;
        range.to = to;
        range.unit = "time";
        return range;
    }


    private Range(){};

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }
}
