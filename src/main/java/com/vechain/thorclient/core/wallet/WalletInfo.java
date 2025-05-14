package com.vechain.thorclient.core.wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

/**
 * WalletInfo contains the {@link WalletFile} instance and {@link ECKeyPair}
 */
public class WalletInfo {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
    public String toKeystoreString() throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(this.getWalletFile());
    }

}