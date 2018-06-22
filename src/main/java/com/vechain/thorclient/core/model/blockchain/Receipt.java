package com.vechain.thorclient.core.model.blockchain;

public class Receipt {
    private long gasUsed;
    private String gasPayer;
    private String paid; //hex form of defaultDecimalStringToByteArray of paid energy
    private String reward; //hex form of defaultDecimalStringToByteArray of reward
    private boolean reverted; //if it is true, then the transaction was reverted by blockchain network
    private BlockContext block;
    private TxContext tx;
    private ReceiptOutput[] outputs;

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

    public BlockContext getBlock() {
        return block;
    }

    public void setBlock(BlockContext block) {
        this.block = block;
    }

    public TxContext getTx() {
        return tx;
    }

    public void setTx(TxContext tx) {
        this.tx = tx;
    }

    public ReceiptOutput[] getOutputs() {
        return outputs;
    }

    public void setOutputs(ReceiptOutput[] outputs) {
        this.outputs = outputs;
    }
}
