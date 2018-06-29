package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;
import java.util.ArrayList;

public class Receipt implements Serializable {
    private long gasUsed;
    private String gasPayer;
    private String paid; //hex form of defaultDecimalStringToByteArray of paid energy
    private String reward; //hex form of defaultDecimalStringToByteArray of reward
    private boolean reverted; //if it is true, then the transaction was reverted by blockchain network
    private LogMeta meta;

    private ArrayList<ReceiptOutput> outputs;

    public LogMeta getMeta() {
        return meta;
    }

    public void setMeta(LogMeta meta) {
        this.meta = meta;
    }

    public long getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(long gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getGasPayer() {
        return gasPayer;
    }

    public void setGasPayer(String gasPayer) {
        this.gasPayer = gasPayer;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public boolean isReverted() {
        return reverted;
    }

    public void setReverted(boolean reverted) {
        this.reverted = reverted;
    }

    public  ArrayList<ReceiptOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs( ArrayList<ReceiptOutput> outputs) {
        this.outputs = outputs;
    }


}
