package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;

public class PeerStat implements Serializable {

    private String bestBlockID;
    private int totalScore;
    private String peerID;
    private String netAddr;
    private boolean inbound;
    private int duration;

    public String getBestBlockID() {
        return bestBlockID;
    }

    public void setBestBlockID(String bestBlockID) {
        this.bestBlockID = bestBlockID;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getPeerID() {
        return peerID;
    }

    public void setPeerID(String peerID) {
        this.peerID = peerID;
    }

    public String getNetAddr() {
        return netAddr;
    }

    public void setNetAddr(String netAddr) {
        this.netAddr = netAddr;
    }

    public boolean isInbound() {
        return inbound;
    }

    public void setInbound(boolean inbound) {
        this.inbound = inbound;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
