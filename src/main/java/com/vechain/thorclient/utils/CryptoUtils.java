package com.vechain.thorclient.utils;

import com.rfksystems.blake2b.Blake2b;
import com.vechain.thorclient.utils.crypto.*;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * A cryptography relative utility class.
 */
public class CryptoUtils {

    /**
     * Blake2b hash with 32 bytes result return
     * @param message {@link byte[]} a input message need to be hashed.
     * @return  {@link byte[]} hashed result.
     */
    public static byte[] blake2b(byte[] message){
        /**
         * Thor public blockchain is using 256 bits digest
         */
        Blake2b blake2b = new Blake2b(256);
        blake2b.update(message, 0, message.length);
        byte[] digest = new byte[32];
        int size = blake2b.digest(digest, 0);
        if(size > 0){
            return digest;
        }else {
            return null;
        }
    }

    /**
     * Generate a random nonce for transaction with cryptography method.
     * @return byte[] random nonce with 8 bytes length array.
     */
    public static byte[] generateTxNonce(){
        if(isAndroidRuntime()){
            throw new RuntimeException("The random nonce is not supporting the Android OS.");
        }
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[8];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * Check if the current OS is Android or not.
     */
    static boolean isAndroidRuntime() {
        final String runtime = System.getProperty("java.runtime.name");
        int isAndroid = (runtime != null && runtime.equals("Android Runtime")) ? 1 : 0;

        return isAndroid == 1;
    }


    /**
     * Make message to the hash byte array with 256 bits result.
     * @param message the message to hash
     * @return
     */
    public static byte[] keccak256(byte[] message) {
        return keccak256(message, 0, message.length);
    }

    /**
     * Make message to the hash byte array with specific bits result.
     * @param message
     * @param offset
     * @param size
     * @return
     */
    public static byte[] keccak256(byte[] message, int offset, int size) {
        Keccak.DigestKeccak kecc = new Keccak.Digest256();
        kecc.update(message, offset, size);
        return kecc.digest();
    }


    public static byte[] randomBytes(int byteSize){
        if(isAndroidRuntime()){
            throw new RuntimeException("The random nonce is not supporting the Android OS.");
        }
        SecureRandom random = new SecureRandom();
        byte randomBytes[] = new byte[byteSize];
        random.nextBytes(randomBytes);
        return randomBytes;
    }

    public static Key recoverPublicKey(byte[] message, byte[] sig){
        if (message == null || message.length != 32){
            throw new RuntimeException("The recover message is not correct");
        }
        if (sig == null || sig.length != 65){
            throw new RuntimeException("The recover signature is not correct");
        }
        byte[] rBytes = new byte[32];
        byte[] sBytes = new byte[32];
        System.arraycopy( sig, 0, rBytes, 0, rBytes.length );
        System.arraycopy( sig, 32, sBytes, 0, sBytes.length );
        byte recovery = sig[64];
        ECDSASignature ecdsaSignature = new ECDSASignature(rBytes, sBytes);
        BigInteger publicKey = ECDSASign.recoverFromSignature( recovery, ecdsaSignature, message);
        return new ECPublicKey( publicKey );
    }

    public static byte[] sha256(byte[] bytes) {
        return sha256(bytes, 0, bytes.length);
    }

    public static byte[] sha256(byte[] bytes, int offset, int size) {
        SHA256Digest sha256Digest = new SHA256Digest();
        sha256Digest.update(bytes, offset, size);
        byte[] sha256 = new byte[32];
        sha256Digest.doFinal(sha256, 0);
        return sha256;
    }

    public static byte[] doubleSha256(byte[] bytes) {
        return doubleSha256(bytes, 0, bytes.length);
    }

    public static byte[] doubleSha256(byte[] bytes, int offset, int size) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(bytes, offset, size);
            return sha256.digest(sha256.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


}
