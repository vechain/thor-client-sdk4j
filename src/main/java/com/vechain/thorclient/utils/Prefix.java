package com.vechain.thorclient.utils;

/**
 * A prefix enumeration.
 */
public enum Prefix {

    /**
     * "VX" prefix string
     */
    VeChainX("VX"),

    /**
     * "0x" prefix string
     */
    ZeroLowerX("0x");

    private final String prefixString;

    Prefix(String prefixString){
        this.prefixString = prefixString;
    }

    /**
     * Get prefix string.
     * @return prefix string "0x" or "VX"
     */
    String getPrefixString(){
        return this.prefixString;
    }
}
