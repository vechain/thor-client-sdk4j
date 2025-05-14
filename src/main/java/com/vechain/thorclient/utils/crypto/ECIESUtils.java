package com.vechain.thorclient.utils.crypto;

import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.encoders.Hex;
import org.eclipse.jetty.io.RuntimeIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ECIESUtils {
	private static Logger logger = LoggerFactory.getLogger(ECIESUtils.class);
	private static final int MaxContentLength = 2*1024*1024;

	/**
	 * encrypt with ECIES(uncompressed publicKey)
	 * 
	 * @param receiverPublicKey(uncompressed)
	 *            hex string
	 * @param shareSecretKey
	 *            hex string
	 * @return encrypted hex string
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] encrypt(String receiverPublicKey, String shareSecretKey, byte[] message)
			throws UnsupportedEncodingException {
        if(message == null || message.length == 0 ){
            throw new IllegalArgumentException( "message is illegal." );
        }
        if(message.length >= MaxContentLength){
            throw new IllegalArgumentException( "message length is too large" );
        }
		logger.debug("1. 产⽣生随机数r，并计算R=r*G");
		byte[] r = CryptoUtils.randomBytes(32);

		ECPoint rPoint = ECKey.publicPointFromPrivate(BytesUtils.bytesToBigInt(r));
		byte[] rBytes =  rPoint.getEncoded( false );

		logger.debug("2. 计算共享密钥，S=Px，P=(Px,Py)=r*KB,这⾥KB为Bob的公钥");
		ECPoint receiverPublicKeyPoint = ECIESUtils.createECPointFromPublicKey(receiverPublicKey);
		PublicKeyECPoint P = ECIESUtils.multiply(receiverPublicKeyPoint, r);


		logger.debug("3. 使⽤用KDF算法，⽣生成对称加密密码和MAC的密码:KE||KM = KDF(S||S1)");
		byte[] shareKeyBytes = BytesUtils.toByteArray(shareSecretKey);

		byte[] K = ECIESUtils.pbkdf2withsha512(P.getX(), shareKeyBytes);
		byte[] keBytes = Arrays.copyOfRange(K, 0, 32);
		byte[] kmBytes = Arrays.copyOfRange(K, 32, K.length);

		logger.debug("4. 对消息进⾏行行加密，c=E(KE,m)");
		byte[] cBytes = null;
		try {
			cBytes = ECIESUtils.encodeAesCtr128(keBytes, message, Arrays.copyOfRange(P.getY(), 0, 16));
		} catch (Exception e) {
			logger.error("encodeAesCtr128 error", e);
		}

		logger.debug("5. 计算加密信息的tag d，d=MAC(KM,c||S2)");
		byte[] dBytes = calcSignature(shareKeyBytes, kmBytes, cBytes);


		byte[] buffer = new byte[rBytes.length + cBytes.length + dBytes.length];
        logger.debug( "rBytes:" + rBytes.length + "  cBytes:" + cBytes.length + "  dBytes:" + dBytes.length );
        System.arraycopy( rBytes, 0, buffer, 0, rBytes.length );
        System.arraycopy( cBytes, 0, buffer, rBytes.length, cBytes.length );
        System.arraycopy( dBytes, 0, buffer, cBytes.length + rBytes.length, dBytes.length);
		logger.debug("6. 返回结果 R||c||d");

		return buffer;
	}

	/**
	 * decrypt with ECIES
	 * 
	 * @param receiverPrivateKey
	 * @param shareSecretKey
	 * @param cryptedMessage
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(String receiverPrivateKey, String shareSecretKey, byte[] cryptedMessage) throws
            Exception {
        if(cryptedMessage == null || cryptedMessage.length == 0){
            throw new IllegalArgumentException( "cryptedMessage is illegal." );
        }

        if (cryptedMessage.length >= MaxContentLength + 65 + 32 + 32){
            throw new IllegalArgumentException( "cryptedMessage is too large" );
        }

		byte[] Rbytes = Arrays.copyOfRange(cryptedMessage, 0, 65);
		byte[] msgBytes = Arrays.copyOfRange(cryptedMessage, 65, cryptedMessage.length - 32);
		byte[] dBytes = Arrays.copyOfRange(cryptedMessage, cryptedMessage.length - 32, cryptedMessage.length);

		logger.debug("1. 计算共享密钥，S=Px。 P=(Px,Py)=kb*R=kb*r*G=r*kb*G=r*KB");
		ECPoint R = ECIESUtils.createECPointFromPublicKey(Hex.toHexString(Rbytes));
		PublicKeyECPoint P = ECIESUtils.multiply(R, BytesUtils.toByteArray(receiverPrivateKey));


		byte[] shareKeyBytes = BytesUtils.toByteArray(shareSecretKey);
		logger.debug("2. 使⽤用KDF算法，⽣生成对称加密密码和MAC的密码:KE||KM = KDF(S||S1)");
		byte[] K = ECIESUtils.pbkdf2withsha512(P.getX(), shareKeyBytes);
		byte[] keBytes = Arrays.copyOfRange(K, 0, 32);
		byte[] kmBytes = Arrays.copyOfRange(K, 32, K.length);


		logger.debug("3. 使⽤用Mac计算Mac是否正确:MAC(KM||c||S2)");
		byte[] calcD = calcSignature(shareKeyBytes, kmBytes, msgBytes);

		if (!Hex.toHexString(calcD).equals(Hex.toHexString(dBytes))) {
			logger.error("different d:{} calc:{}", Hex.toHexString(dBytes), Hex.toHexString(calcD));
			throw new RuntimeIOException("decrypt error : 加密信息的tag验证失败");
		}


		logger.debug("4. 解码原始加密⽂文件，解码密码为Ke，c为收到的加密⽂文件");
		try {
            return ECIESUtils.decodeAesCtr128(keBytes, msgBytes, Arrays.copyOfRange(P.getY(), 0, 16));
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		}

	}

	/**
	 * ECPoint multiply a BigIntBytes
	 * 
	 * @param p
	 * @param k
	 * @return
	 */
	private static PublicKeyECPoint multiply(ECPoint p, byte[] k) {
		ECPoint publicKey = new FixedPointCombMultiplier().multiply(p, BytesUtils.bytesToBigInt(k));
		// TODO check is P in CURVE
		return new PublicKeyECPoint(publicKey);
	}

	/**
	 * format public key hex string to ECPoint
	 * 
	 * @param receiverPublicKey
	 * @return
	 */
	public static ECPoint createECPointFromPublicKey(String receiverPublicKey) {
		byte[] receiverPublicKeyBytes = BytesUtils.toByteArray(receiverPublicKey);
		if(receiverPublicKeyBytes == null || receiverPublicKeyBytes.length != 65 || receiverPublicKeyBytes[0] != 0x04){
            throw new IllegalArgumentException( "receiverPublicKey is illegal." );
        }
		byte[] RxBytes = new byte[32];
		byte[] RyBytes = new byte[32];
		System.arraycopy(receiverPublicKeyBytes, 1, RxBytes, 0, RxBytes.length);
		System.arraycopy(receiverPublicKeyBytes, RxBytes.length +  1, RyBytes, 0, RyBytes.length);

		ECPoint publicKeyPoint = ECKeyPair.CURVE.getCurve().createPoint(BytesUtils.bytesToBigInt(RxBytes),
				BytesUtils.bytesToBigInt(RyBytes));
		return publicKeyPoint;
	}

	/**
	 * calcSignature
	 * 
	 * @param shareKeyBytes
	 * @param kmBytes
	 * @param cBytes
	 * @return
	 */
	private static byte[] calcSignature(byte[] shareKeyBytes, byte[] kmBytes, byte[] cBytes) {
		byte[] blakeContent = new byte[kmBytes.length + cBytes.length + shareKeyBytes.length];
		System.arraycopy(kmBytes, 0, blakeContent, 0, kmBytes.length);
		System.arraycopy(cBytes, 0, blakeContent, kmBytes.length, cBytes.length);
		System.arraycopy(shareKeyBytes, 0, blakeContent, kmBytes.length + cBytes.length, shareKeyBytes.length);
		byte[] dBytes = CryptoUtils.blake2b(blakeContent);
		return dBytes;
	}

	/**
	 * pbkdf2withsha512
	 * 
	 * @param password
	 * @param salt
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] pbkdf2withsha512(byte[] password, byte[] salt) throws UnsupportedEncodingException {
		PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA512Digest());
		gen.init(password, salt, 2048);
		byte[] dk = ((KeyParameter) gen.generateDerivedParameters(512)).getKey();
		return dk;
	}

	/**
	 * decodeAesCtr128
	 * 
	 * @param keyBytes
	 *            秘钥
	 * @param cipherBytes
	 *            解密密文
	 * @param ivBytes
	 *            初始向量
	 * @return
	 *
	 */
	public static byte[] decodeAesCtr128(byte[] keyBytes, byte[] cipherBytes, byte[] ivBytes)
			throws Exception {
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		return cipher.doFinal( cipherBytes );
	}

	/**
	 * encodeAesCtr128
	 * 
	 * @param keyBytes
	 *            private key bytes array
	 * @param msg
	 *            plain original text
	 * @param ivBytes
	 *            initial bytes array
	 * @return
	 * @throws Exception
	 */
	public static byte[] encodeAesCtr128(byte[] keyBytes, byte[] msg, byte[] ivBytes)
            throws Exception{
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        return cipher.doFinal( msg );

	}

}
