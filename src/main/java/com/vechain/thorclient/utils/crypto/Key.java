package com.vechain.thorclient.utils.crypto;

import javax.naming.OperationNotSupportedException;
import java.math.BigInteger;

public interface Key extends Cloneable {

    /**
     * Get raw private key bytes array.
     * @return
     */
    byte[] getRawPrivateKey();


    /**
     * Get raw address.
     */
    byte[] getRawAddress();

    /**
     * Get
     * @param isCompressed public key with compression.
     * @return
     */
    byte[] getRawPublicKey(boolean isCompressed);

    /**
     * Get private key
     */
    BigInteger getPrivateKey();

    /**
     * Get public key {@link BigInteger} with decompressed format
     * @return
     */
    BigInteger getPublicKey();

    /**
     * Get hexadecimal address string with prefix "0x"
     * @return
     */
    String getAddress();

    /**
     * Get hexadecimal address string with prefix "0x"
     * @return
     */
    @Deprecated
    String getHexAddress();

    <T> T sign(byte[] messageHash) throws OperationNotSupportedException;
}
