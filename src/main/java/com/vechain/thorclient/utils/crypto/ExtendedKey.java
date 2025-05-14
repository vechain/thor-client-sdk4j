package com.vechain.thorclient.utils.crypto;


import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.Arrays;


import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class ExtendedKey {
    private static final X9ECParameters curve = SECNamedCurves.getByName("secp256k1");
    private final int parent;
    private final Key master;
    private final int sequence;
    private final byte[] chainCode;
    private final int depth;

    public ExtendedKey(Key key, byte[] chainCode, int depth, int parent, int sequence){
        this.master = key;
        this.chainCode = chainCode;
        this.depth = depth;
        this.parent = parent;
        this.sequence = sequence;
    }

    public Key getMaster(){
       return  this.master;
    }

    public ExtendedKey derived(int sequence) throws ValidationException {
        return derivedFrom(sequence);
    }

    private ExtendedKey derivedFrom(int sequence) throws  ValidationException{
        try {
            if ((sequence & 0x80000000) != 0 && master.getRawPrivateKey() == null) {
                throw new ValidationException("need private key for private generation");
            }

            Mac mac = Mac.getInstance("HmacSHA512", new BouncyCastleProvider());
            SecretKey key = new SecretKeySpec(chainCode, "HmacSHA512");
            mac.init(key);

            byte[] extended;
            byte[] pub = master.getRawPublicKey(true);
            if ((sequence & 0x80000000) == 0) {
                extended = new byte[pub.length + 4];
                System.arraycopy(pub, 0, extended, 0, pub.length);
                extended[pub.length] = (byte) ((sequence >>> 24) & 0xff);
                extended[pub.length + 1] = (byte) ((sequence >>> 16) & 0xff);
                extended[pub.length + 2] = (byte) ((sequence >>> 8) & 0xff);
                extended[pub.length + 3] = (byte) (sequence & 0xff);
            } else {
                byte[] priv = master.getRawPrivateKey();
                extended = new byte[priv.length + 5];
                System.arraycopy(priv, 0, extended, 1, priv.length);
                extended[priv.length + 1] = (byte) ((sequence >>> 24) & 0xff);
                extended[priv.length + 2] = (byte) ((sequence >>> 16) & 0xff);
                extended[priv.length + 3] = (byte) ((sequence >>> 8) & 0xff);
                extended[priv.length + 4] = (byte) (sequence & 0xff);
            }
            byte[] lr = mac.doFinal(extended);
            byte[] l = Arrays.copyOfRange(lr, 0, 32);
            byte[] r = Arrays.copyOfRange(lr, 32, 64);

            BigInteger m = new BigInteger(1, l);
            if (m.compareTo(curve.getN()) >= 0) {
                throw new ValidationException("This is rather unlikely, but it did just happen");
            }
            if (master.getRawPrivateKey() != null) {
                BigInteger k = m.add(new BigInteger(1, master.getRawPrivateKey())).mod(curve.getN
                        ());
                if (k.equals(BigInteger.ZERO)) {
                    throw new ValidationException("This is rather unlikely, but it did just " +
                            "happen");
                }
                return new ExtendedKey(new ECKeyPair(k), r, depth, parent, sequence);
            } else {
                ECPoint q = curve.getG().multiply(m).add(curve.getCurve().decodePoint(pub));
                if (q.isInfinity()) {
                    throw new ValidationException("This is rather unlikely, but it did just " +
                            "happen");
                }
                pub = q.getEncoded(false);
                return new ExtendedKey(new ECPublicKey(pub), r, depth, parent, sequence);
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new ValidationException(e);
        }
    }
}
