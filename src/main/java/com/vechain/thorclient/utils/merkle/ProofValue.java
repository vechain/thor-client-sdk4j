package com.vechain.thorclient.utils.merkle;

import java.io.Serializable;

public class ProofValue implements Serializable {
    private int type;
    private String brotherValue;
    private int level;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getBrotherValue() {
        return brotherValue;
    }

    public void setBrotherValue(String brotherValue) {
        this.brotherValue = brotherValue;
    }
}
