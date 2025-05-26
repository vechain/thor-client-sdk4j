package com.vechain.thorclient.core.model.clients;

import com.vechain.thorclient.core.model.blockchain.RawClause;

public interface TransactionCommonalities {

    byte getChainTag();

    void setChainTag(byte chainTag);

    byte[] getBlockRef();

    void setBlockRef(byte[] blockRef);

    byte[] getExpiration();

    void setExpiration(byte[] expiration);

    RawClause[] getClauses();

    void setClauses(RawClause[] clauses);

    byte[] getGas();

    void setGas(byte[] gas);

    byte[] getDependsOn();

    void setDependsOn(byte[] dependsOn);

    byte[] getNonce();

    void setNonce(byte[] nonce);

    byte[] getSignature();

    void setSignature(byte[] signature);

    TransactionReserved getReserved();

    void setReserved(TransactionReserved reserved);

    byte[] encode();

    TransactionCommonalities copy();
}
