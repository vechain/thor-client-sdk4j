package com.vechain.thorclient.utils.crypto;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;

import java.math.BigInteger;
import java.util.Arrays;

public abstract class ECKey implements Key{
    static final String SECP256K1 = "secp256k1";

    /**
     * Byte array of public key(decompressed or compressed) transformed.
     * @param encodedPublicKeyPoint
     * @param toCompressed
     * @return
     */
    public static byte[] pointBytesToPublicKey(byte[] encodedPublicKeyPoint, boolean toCompressed) {
        final ECNamedCurveParameterSpec curveParameterSpec = ECNamedCurveTable.getParameterSpec(SECP256K1);
        final ECPoint decodedPoint = curveParameterSpec.getCurve().decodePoint(encodedPublicKeyPoint);
        final BigInteger x = decodedPoint.getXCoord().toBigInteger();
        final BigInteger y = decodedPoint.getYCoord().toBigInteger();
        final ECPoint decompressedPoint = curveParameterSpec.getCurve().createPoint(x, y);
        return decompressedPoint.getEncoded(toCompressed);
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

}
