package com.vechain.thorclient.utils.crypto;

import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.Prefix;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;


/**
 * ECDSA Keypair contains private key and public key.
 */
public class ECKeyPair extends ECKey{

    private final BigInteger privateKey;


    /**
     * Constructor of ECKeyPair
     * @param priv
     */
    public ECKeyPair(BigInteger priv) {
        this.privateKey = priv;

    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    @Override
    public String getAddress() {
        byte[] addressBytes = getRawAddress();
        return BytesUtils.toHexString( addressBytes, Prefix.ZeroLowerX );
    }

    @Override
    public byte[] getRawPublicKey(boolean isCompressed) {
        ECPoint point = publicPointFromPrivate(this.privateKey);
        return point.getEncoded(isCompressed);
    }

    public byte[] getRawPrivateKey(){
        return BytesUtils.toBytesPadded( privateKey, PRIVATE_KEY_SIZE );
    }


    /**
     * Get decompressed public key in {@link BigInteger} format.
     * @return
     */
    public BigInteger getPublicKey() {
        return publicKeyFromPrivate( this.privateKey, false);
    }

    private byte[] getPublicKeyPointBytes(){
        return BytesUtils.toBytesPadded( this.getPublicKey(), PUBLIC_KEY_POINT_SIZE );
    }


    public byte[] getRawAddress() {
        byte[] hash = CryptoUtils.keccak256(this.getPublicKeyPointBytes());
        byte[] address = new byte[20];
        System.arraycopy(hash, 12, address, 0, address.length);
        return address;  // right most 160 bits
    }

    /**
     * Get hex address string.
     * @return
     */
    @Deprecated
    public String getHexAddress(){
        return this.getAddress();
    }

    /**
     * Sign a hash with the private key of this key pair.
     * @param messageHash   the hash to sign
     * @return  An {@link ECDSASignature} of the hash
     */
    @Override
    public ECDSASignature sign(byte[] messageHash) {
        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(privateKey, CURVE);
        signer.init(true, privKey);
        BigInteger[] components = signer.generateSignature(messageHash);
        return new ECDSASignature(components[0], components[1]).toCanonicalised();
    }

    /**
     *  Create {@link ECKeyPair} from big integer private key.
     * @param privateKey
     * @return
     */
    public static ECKeyPair create(BigInteger privateKey) {
        return new ECKeyPair( privateKey );
    }

    /**
     * Create {@link ECKeyPair} from private key hex string.
     * @param privateKeyHex
     * @return
     */
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
        ECKeyGenerationParameters keygenParams = new ECKeyGenerationParameters(CURVE,
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

        return this.getPublicKey() != null
                ? this.getPublicKey().equals(ecKeyPair.getPublicKey()) : ecKeyPair.getPublicKey() == null;
    }

    @Override
    public int hashCode() {
        int result = privateKey != null ? privateKey.hashCode() : 0;
        result = 31 * result + (this.getPublicKey() != null ? this.getPublicKey().hashCode() : 0);
        return result;
    }

}
