package com.vechain.thorclient.utils.crypto;

import com.vechain.thorclient.utils.BytesUtils;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public abstract class ECKey implements Key{
    static final String SECP256K1 = "secp256k1";
    public static final int PRIVATE_KEY_SIZE = 32;
    protected static final int PUBLIC_KEY_POINT_SIZE = 64;
    protected static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName(SECP256K1);
    public static final BigInteger HALF_CURVE_ORDER = CURVE_PARAMS.getN().shiftRight(1);
    public static final ECDomainParameters CURVE = new ECDomainParameters(
            CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(), CURVE_PARAMS.getH());

    protected static final SecureRandom secureRandom = new SecureRandom();


    /**
     * Byte array of public key(decompressed or compressed) transformed.
     * @param encodedPublicKeyPoint
     * @param toCompressed
     * @return
     */
    public static byte[] pointBytesToPublicKey(byte[] encodedPublicKeyPoint, boolean toCompressed) {
        if (encodedPublicKeyPoint.length == 65 || encodedPublicKeyPoint.length == 33) {
            final ECNamedCurveParameterSpec curveParameterSpec = ECNamedCurveTable.getParameterSpec( SECP256K1 );
            final ECPoint decodedPoint = curveParameterSpec.getCurve().decodePoint( encodedPublicKeyPoint );
            final BigInteger x = decodedPoint.getXCoord().toBigInteger();
            final BigInteger y = decodedPoint.getYCoord().toBigInteger();
            final ECPoint decompressedPoint = curveParameterSpec.getCurve().createPoint( x, y );
            return decompressedPoint.getEncoded( toCompressed );
        }else{
            throw new RuntimeException( "Invalid public key bytes array." );
        }
    }

    /**
     * Get {@link java.security.spec.ECPoint} from decompress public key.
     * @param publicKey
     * @return
     */
    public static java.security.spec.ECPoint decodeECPoint(byte[] publicKey){
        final ECNamedCurveParameterSpec curveParameterSpec = ECNamedCurveTable.getParameterSpec(SECP256K1);
        ECPoint bcECPoint = curveParameterSpec.getCurve().decodePoint(publicKey);
        return new java.security.spec.ECPoint(bcECPoint.getXCoord().toBigInteger(), bcECPoint.getYCoord().toBigInteger()  );

    }

    /**
     * Returns public key from the given private key.
     *
     * @param privKey the private key to derive the public key from
     * @return BigInteger encoded public key
     */
    public static BigInteger publicKeyFromPrivate(BigInteger privKey, boolean isCompressed) {
        ECPoint point = publicPointFromPrivate(privKey);
        byte[] encoded = point.getEncoded(isCompressed);
        return new BigInteger(1, Arrays.copyOfRange(encoded, 1, encoded.length));  // remove prefix
    }

    /**
     * Returns public key point from the given private key.
     */
    public static ECPoint publicPointFromPrivate(BigInteger privKey) {
        /*
         * TODO: FixedPointCombMultiplier currently doesn't support scalars longer than the group
         * order, but that could change in future versions.
         */
        if (privKey.bitLength() > ECKeyPair.CURVE.getN().bitLength()) {
            privKey = privKey.mod( ECKeyPair.CURVE.getN());
        }
        return new FixedPointCombMultiplier().multiply( ECKeyPair.CURVE.getG(), privKey);
    }

    /**
     * Verify the signature
     * @param hash  message hash
     * @param signature  the signature bytes array |32 bytes r value| 32 bytes s value|
     * @param pub the public key bytes.
     * @return if ok return true, else return false.
     */
    public static boolean verify(byte[] hash, byte[] signature, byte[] pub) {

        try {
            ECDSASigner signer = new ECDSASigner();
            signer.init(false, new ECPublicKeyParameters(CURVE.getCurve().decodePoint(pub),
                    CURVE));
            byte[] rBytes = Arrays.copyOfRange(signature, 0,32  );
            byte[] sBytes = Arrays.copyOfRange( signature,32,64 );
            BigInteger r = BytesUtils.bytesToBigInt( rBytes );
            BigInteger s = BytesUtils.bytesToBigInt( sBytes );
            return signer.verifySignature(hash, r, s);
        } catch (Exception e) {
            // threat format errors as invalid signatures
            e.printStackTrace();
            return false;
        }
    }

}
