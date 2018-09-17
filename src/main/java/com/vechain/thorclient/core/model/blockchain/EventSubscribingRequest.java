package com.vechain.thorclient.core.model.blockchain;

public class EventSubscribingRequest extends WSRequest{
    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getT0() {
        return t0;
    }

    public void setT0(String t0) {
        this.t0 = t0;
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public String getT3() {
        return t3;
    }

    public void setT3(String t3) {
        this.t3 = t3;
    }

    public String getT4() {
        return t4;
    }

    public void setT4(String t4) {
        this.t4 = t4;
    }

    private String pos;
    private String addr;
    private String t0;
    private String t1;
    private String t2;
    private String t3;
    private String t4;
}
