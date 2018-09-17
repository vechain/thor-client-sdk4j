package com.vechain.thorclient.core.model.blockchain;

public class BlockSubscribingRequest extends WSRequest {
    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    private String pos;

}
