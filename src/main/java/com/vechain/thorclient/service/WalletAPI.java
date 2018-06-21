package com.vechain.thorclient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vechain.thorclient.core.crypto.ECKeyPair;
import com.vechain.thorclient.core.wallet.CipherException;
import com.vechain.thorclient.core.wallet.Wallet;
import com.vechain.thorclient.core.wallet.WalletFile;
import com.vechain.thorclient.core.wallet.WalletInfo;
import com.vechain.thorclient.utils.StringUtils;

import java.io.IOException;

/**
 * Wallet API is used to create or load keystore.
 */
public class WalletAPI {

    /**
     * Load keystore for keystore string and passphases.
     * @param keystore  keystore string
     * @param passphases password string to encrypt
     * @return {@link WalletInfo}
     */
    public static WalletInfo loadKeystore(String keystore, String passphases) {
        if(StringUtils.isBlank( keystore ) && StringUtils.isBlank( passphases )){
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        keystore = keystore.toLowerCase();
        WalletFile walletFile = null;
        try {
            walletFile = objectMapper.readValue(keystore, WalletFile.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        ECKeyPair ecKeyPair = null;
        try {
            ecKeyPair = Wallet.decrypt( passphases, walletFile );
        } catch (CipherException e) {
            e.printStackTrace();
            return null;
        }

        return new WalletInfo( walletFile, ecKeyPair);
    }

    /**
     * Create wallet from password.
     * @param passphases passsword to encrypt the private key.
     * @return {@link WalletInfo}
     */
    public static WalletInfo createWallet(String passphases){
        if(StringUtils.isBlank( passphases )){
            return null;
        }
        ECKeyPair keyPair = ECKeyPair.create();
        WalletFile walletFile = null;
        try {
            walletFile = Wallet.createStandard(passphases,keyPair);
        } catch (CipherException e) {
            e.printStackTrace();
        }

        return new WalletInfo(walletFile, keyPair);
    }


}
