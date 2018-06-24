package com.vechain.thorclient.core.wallet;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

/**
 * WalletInfo contains the {@link WalletFile} instance and {@link ECKeyPair}
 */
public class WalletInfo{
    private WalletFile walletFile;
    private ECKeyPair keyPair;

    public WalletInfo(WalletFile walletFile, ECKeyPair keyPair) {
        this.walletFile = walletFile;
        this.keyPair = keyPair;
    }

    public ECKeyPair getKeyPair() {
        return keyPair;
    }

    public WalletFile getWalletFile() {
        return walletFile;
    }

    /**
     * get the keystore string from {@link WalletFile}
     * @return keystore string
     */
    public String toKeystoreString(){
        return JSON.toJSONString(this.getWalletFile());
    }

}