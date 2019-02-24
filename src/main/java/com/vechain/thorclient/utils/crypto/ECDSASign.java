package com.vechain.thorclient.utils.crypto;


import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve;

import java.math.BigInteger;
import java.util.Arrays;

import static com.vechain.thorclient.utils.Assertions.verifyPrecondition;

/**
 * Sign a message with ECDSA algrithm and secp256k1 curve.
 */
public class ECDSASign {


    public static ECDSASign.SignatureData signMessage(byte[] message, ECKeyPair keyPair) throws Exception{
        return signMessage(message, keyPair, true);
    }


    /**
     * Sign the message with {@link ECKeyPair}. Only recovery id 0 or 1 is accepted, otherwise the method will continue to retry to sign til get recovery id is 0 or 1.
     * @param message message to be signed.
     * @param keyPair {@link ECKeyPair}
     * @param needToHash set if the message to be hashed before signing. If it is true, hashed first,then sign. If it is false, signed directly.
     * @return signature data which contains 32 bytes r, 32 bytes s, 1 byte v.
     */
    public static ECDSASign.SignatureData signMessage(byte[] message, ECKeyPair keyPair, boolean needToHash) throws SignException {

        BigInteger publicKey = keyPair.getPublicKey();
        byte[] messageHash;
        if (needToHash) {
            messageHash = CryptoUtils.blake2b(message);
        } else {
            messageHash = message;
        }
        int recId = -1;
        ECDSASignature sig;

        sig = keyPair.sign(messageHash);
        for (int i = 0; i < 4; i++) {
            BigInteger k = recoverFromSignature(i, sig, messageHash);
            if (k != null && k.equals(publicKey)) {
                recId = i;
                break;
            }
        }

        if (recId == -1){
            throw new SignException( "Sign the data failed." );
        }

        if(recId == 2 || recId == 3){
            throw new SignException( "Recovery is not valid for VeChain MainNet." );
        }

        byte v = (byte) recId;
        byte[] r = BytesUtils.toBytesPadded(sig.r, 32);
        byte[] s = BytesUtils.toBytesPadded(sig.s, 32);

        return new ECDSASign.SignatureData(v, r, s);

    }

    /**
     * Recover the public key from signature and message.
     * @param recId recovery id which 0 or 1.
     * @param sig {@link ECDSASignature} a signature object
     * @param message message bytes array.
     * @return public key represented by {@link BigInteger}
     */
    public static BigInteger recoverFromSignature(int recId, ECDSASignature sig, byte[] message) {
        verifyPrecondition(recId == 0 || recId == 1, "recId must be 0 or 1");
        verifyPrecondition(sig.r.signum() >= 0, "r must be positive");
        verifyPrecondition(sig.s.signum() >= 0, "s must be positive");
        verifyPrecondition(message != null, "message cannot be null");

        BigInteger n = ECKeyPair.CURVE.getN();  // Curve order.
        BigInteger i = BigInteger.valueOf((long) recId / 2);
        BigInteger x = sig.r.add(i.multiply(n));

        BigInteger prime = SecP256K1Curve.q;
        if (x.compareTo(prime) >= 0) {

            return null;
        }

        ECPoint R = decompressKey(x, (recId & 1) == 1);

        if (!R.multiply(n).isInfinity()) {
            return null;
        }

        BigInteger e = new BigInteger(1, message);

        BigInteger eInv = BigInteger.ZERO.subtract(e).mod(n);
        BigInteger rInv = sig.r.modInverse(n);
        BigInteger srInv = rInv.multiply(sig.s).mod(n);
        BigInteger eInvrInv = rInv.multiply(eInv).mod(n);
        ECPoint q = ECAlgorithms.sumOfTwoMultiplies( ECKeyPair.CURVE.getG(), eInvrInv, R, srInv);

        byte[] qBytes = q.getEncoded(false);
        // We remove the prefix
        return new BigInteger(1, Arrays.copyOfRange(qBytes, 1, qBytes.length));
    }

    private static ECPoint decompressKey(BigInteger xBN, boolean yBit) {
        X9IntegerConverter x9 = new X9IntegerConverter();
        byte[] compEnc = x9.integerToBytes(xBN, 1 + x9.getByteLength( ECKeyPair.CURVE.getCurve()));
        compEnc[0] = (byte)(yBit ? 0x03 : 0x02);
        return ECKeyPair.CURVE.getCurve().decodePoint(compEnc);
    }

    /**
     * A signature data
     */
    public static class SignatureData {
        private final byte v;
        private final byte[] r;
        private final byte[] s;

        public SignatureData(byte v, byte[] r, byte[] s) {
            this.v = v;
            this.r = r;
            this.s = s;
        }

        /**
         * Get v value
         * @return byte value
         */
        public byte getV() {
            return v;
        }

        /**
         * Get r value
         * @return bytes array value.
         */
        public byte[] getR() {
            return r;
        }

        /**
         * Get s value
         * @return bytes array value.
         */
        public byte[] getS() {
            return s;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ECDSASign.SignatureData that = (ECDSASign.SignatureData) o;

            if (v != that.v) {
                return false;
            }
            if (!Arrays.equals(r, that.r)) {
                return false;
            }
            return Arrays.equals(s, that.s);
        }

        /**
         * Convert to bytes array. r bytes array append s bytes array, and then append v byte.
         * @return the bytes array.
         */
        public byte[] toByteArray() {
            int size = this.r.length + this.s.length + 1;
            byte[] flat = new byte[size];
            System.arraycopy(this.r,0, flat,0,r.length);
            System.arraycopy(this.s, 0, flat,r.length,s.length);
            flat[size - 1] = this.v;
            return flat;
        }
    }

   public static class SignException extends RuntimeException{
        public SignException(String exceptionMessage){
            super(exceptionMessage);
        }
   }
}



