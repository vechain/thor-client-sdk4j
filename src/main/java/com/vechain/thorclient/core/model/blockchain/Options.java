package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;

public class Options implements Serializable {
    private long offset;
    private long limit;

    public static Options create(long offset, long limit){
        if(offset < 0){
            throw new IllegalArgumentException( "Offset invalid." );
        }
        if(limit <= 0){
            throw new IllegalArgumentException( "limit invalid." );
        }
        Options options = new Options();
        options.offset = offset;
        options.limit = limit;
        return options;
    }

    private Options(){}

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }
}
