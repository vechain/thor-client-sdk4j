package com.vechain.thorclient.utils.crypto;

import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.Prefix;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;


import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * ECDSA Keypair contains private key and public key.
 */
public class ECKeyPair {

    public static final int PRIVATE_KEY_SIZE = 32;
    private static final int PUBLIC_KEY_SIZE = 64;
    private static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");
    public static final BigInteger HALF_CURVE_ORDER = CURVE_PARAMS.getN().shiftRight(1);
    public static final ECDomainParameters CURVE = new ECDomainParameters(
            CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(), CURVE_PARAMS.getH());

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final ECDomainParameters domain = new ECDomainParameters( CURVE.getCurve(),
            CURVE.getG(), CURVE.getN(), CURVE.getH());

    private final BigInteger privateKey;
    private final BigInteger publicKey;

    /**
     * Constructor of ECKeyPair
     * @param privateKey private key.
     * @param publicKey  public key.
     */
    public ECKeyPair(BigInteger privateKey, BigInteger publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public byte[] getRawPrivateKey(){
        return BytesUtils.toBytesPadded( privateKey, PRIVATE_KEY_SIZE );
    }


    public BigInteger getPublicKey() {
        return publicKey;
    }

    public byte[] getRawPublicKey(){
        return BytesUtils.toBytesPadded( publicKey, PUBLIC_KEY_SIZE );
    }

    public byte[] getRawAddress() {
        byte[] hash = CryptoUtils.keccak256(this.publicKey.toByteArray());
        return Arrays.copyOfRange(hash, hash.length - 20, hash.length);  // right most 160 bits
    }

    public String getAddress(){
        byte[] addressBytes = getRawAddress();
        return BytesUtils.toHexString( addressBytes, Prefix.ZeroLowerX );
    }

    /**
     * Sign a hash with the private key of this key pair.
     * @param message   the hash to sign
     * @return  An {@link ECDSASignature} of the hash
     */
    public ECDSASignature sign(byte[] message) {
        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));

        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(privateKey, CURVE);
        signer.init(true, privKey);
        BigInteger[] components = signer.generateSignature(message);

        return new ECDSASignature(components[0], components[1]).toCanonicalised();
    }

    public static ECKeyPair create(BigInteger privateKey) {
        return new ECKeyPair(privateKey, ECDSASign.publicKeyFromPrivate(privateKey));
    }

    public static ECKeyPair create(String privateKeyHex) {
        byte[] privKey = BytesUtils.toByteArray(privateKeyHex);
        return create(privKey);
    }

    public static ECKeyPair create(byte[] privateKey) {
        if(privateKey.length == PRIVATE_KEY_SIZE) {
            return create(BytesUtils.bytesToBigInt( privateKey ) );
        }else{
            throw new IllegalArgumentException("Invalid privatekey size");
        }
    }

    public static ECKeyPair create() {
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        ECKeyGenerationParameters keygenParams = new ECKeyGenerationParameters(domain,
                secureRandom);
        generator.init(keygenParams);
        AsymmetricCipherKeyPair keypair = generator.generateKeyPair();
        ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) keypair.getPrivate();
        ECKeyPair k = ECKeyPair.create(privParams.getD());
        return k;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ECKeyPair ecKeyPair = (ECKeyPair) o;

        if (privateKey != null
                ? !privateKey.equals(ecKeyPair.privateKey) : ecKeyPair.privateKey != null) {
            return false;
        }

        return publicKey != null
                ? publicKey.equals(ecKeyPair.publicKey) : ecKeyPair.publicKey == null;
    }

    @Override
    public int hashCode() {
        int result = privateKey != null ? privateKey.hashCode() : 0;
        result = 31 * result + (publicKey != null ? publicKey.hashCode() : 0);
        return result;
    }

}
