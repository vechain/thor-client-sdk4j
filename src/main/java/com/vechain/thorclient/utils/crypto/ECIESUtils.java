package com.vechain.thorclient.utils.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.eclipse.jetty.io.RuntimeIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;

public class ECIESUtils {

	private static Logger logger = LoggerFactory.getLogger(ECIESUtils.class);

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
	public static String encrypt(String receiverPublicKey, String shareSecretKey, String msgtoEncrypt)
			throws UnsupportedEncodingException {
		StringBuffer rtnStr = new StringBuffer();
		logger.info("1. 产⽣生随机数r，并计算R=r*G");
		byte[] r = CryptoUtils.randomBytes(32);

		ECPoint R = ECKey.publicPointFromPrivate(BytesUtils.bytesToBigInt(r));
		rtnStr.append(ByteUtils.toHexString(R.getEncoded(false)));
		logger.info("R:{}", rtnStr.toString());

		logger.info("2. 计算共享密钥，S=Px，P=(Px,Py)=r*KB,这⾥里里KB为Bob的公钥");
		ECPoint receiverPublicKeyPoint = ECIESUtils.createECPointFromPublicKey(receiverPublicKey);
		PublicKeyECPoint P = ECIESUtils.multiply(receiverPublicKeyPoint, r);
		logger.info("P:" + P.toString());

		logger.info("3. 使⽤用KDF算法，⽣生成对称加密密码和MAC的密码:KE||KM = KDF(S||S1)");
		byte[] shareKeyBytes = BytesUtils.toByteArray(shareSecretKey);

		byte[] K = ECIESUtils.pbkdf2withsha512(P.getX(), shareKeyBytes);
		byte[] keBytes = Arrays.copyOfRange(K, 0, 32);
		byte[] kmBytes = Arrays.copyOfRange(K, 32, K.length);
		logger.info("Ke:" + ByteUtils.toHexString(keBytes));
		logger.info("Km:" + ByteUtils.toHexString(kmBytes));

		logger.info("4. 对消息进⾏行行加密，c=E(KE,m)");
		byte[] cBytes = null;
		try {
			cBytes = ECIESUtils.encodeAesCtr128(keBytes, msgtoEncrypt.getBytes(), Arrays.copyOfRange(P.getY(), 0, 16));
		} catch (Exception e) {
			logger.error("encodeAesCtr128 error", e);
		}
		logger.info("c:" + ByteUtils.toHexString(cBytes));
		rtnStr.append(ByteUtils.toHexString(cBytes));

		logger.info("5. 计算加密信息的tag d，d=MAC(KM,c||S2)");
		byte[] dBytes = calcSignature(shareKeyBytes, kmBytes, cBytes);
		logger.info("d:" + ByteUtils.toHexString(dBytes));
		rtnStr.append(ByteUtils.toHexString(dBytes));

		logger.info("6. 返回结果 R||c||d");
		logger.info(rtnStr.toString());
		return rtnStr.toString();
	}

	/**
	 * decrypt with ECIES
	 * 
	 * @param receiverPrivateKey
	 * @param shareSecretKey
	 * @param cryptMsg
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String receiverPrivateKey, String shareSecretKey, String cryptMsg) throws Exception {
		byte[] cryptMsgBytes = BytesUtils.toByteArray(cryptMsg);
		byte[] Rbytes = Arrays.copyOfRange(cryptMsgBytes, 0, 65);
		byte[] msgBytes = Arrays.copyOfRange(cryptMsgBytes, 65, cryptMsgBytes.length - 32);
		byte[] dBytes = Arrays.copyOfRange(cryptMsgBytes, cryptMsgBytes.length - 32, cryptMsgBytes.length);
		logger.info(cryptMsg);
		logger.info("receiver R:{} msg:{} d:{}", ByteUtils.toHexString(Rbytes), ByteUtils.toHexString(msgBytes),
				ByteUtils.toHexString(dBytes));

		logger.info("1. 计算共享密钥，S=Px。 P=(Px,Py)=kb*R=kb*r*G=r*kb*G=r*KB");
		ECPoint R = ECIESUtils.createECPointFromPublicKey(ByteUtils.toHexString(Rbytes));
		PublicKeyECPoint P = ECIESUtils.multiply(R, BytesUtils.toByteArray(receiverPrivateKey));
		logger.info("P:" + P.toString());

		byte[] shareKeyBytes = BytesUtils.toByteArray(shareSecretKey);
		logger.info("2. 使⽤用KDF算法，⽣生成对称加密密码和MAC的密码:KE||KM = KDF(S||S1)");
		byte[] K = ECIESUtils.pbkdf2withsha512(P.getX(), shareKeyBytes);
		byte[] keBytes = Arrays.copyOfRange(K, 0, 32);
		byte[] kmBytes = Arrays.copyOfRange(K, 32, K.length);
		logger.info("Ke:" + ByteUtils.toHexString(keBytes));
		logger.info("Km:" + ByteUtils.toHexString(kmBytes));

		logger.info("3. 使⽤用Mac计算Mac是否正确:MAC(KM||c||S2)");
		byte[] calcD = calcSignature(shareKeyBytes, kmBytes, msgBytes);
		logger.info("calc d:" + ByteUtils.toHexString(calcD));
		if (!ByteUtils.toHexString(calcD).equals(ByteUtils.toHexString(dBytes))) {
			logger.error("different d:{} calc:{}", ByteUtils.toHexString(dBytes), ByteUtils.toHexString(calcD));
			throw new RuntimeIOException("decrypt error : 加密信息的tag验证失败");
		}

		logger.info("4. 解码原始加密⽂文件，解码密码为Ke，c为收到的加密⽂文件");
		try {
			byte[] plain = ECIESUtils.decodeAesCtr128(keBytes, msgBytes, Arrays.copyOfRange(P.getY(), 0, 16));
			logger.info("plain:" + new String(plain));

			return new String(plain, "UTF-8");
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
		boolean hasPrefix = receiverPublicKeyBytes.length == 65;
		byte[] RxBytes = new byte[32];
		byte[] RyBytes = new byte[32];
		System.arraycopy(receiverPublicKeyBytes, hasPrefix ? 1 : 0, RxBytes, 0, RxBytes.length);
		System.arraycopy(receiverPublicKeyBytes, RxBytes.length + (hasPrefix ? 1 : 0), RyBytes, 0, RyBytes.length);

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
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IOException
	 */
	public static byte[] decodeAesCtr128(byte[] keyBytes, byte[] cipherBytes, byte[] ivBytes)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IOException {
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		CipherOutputStream cOut = new CipherOutputStream(bOut, cipher);
		try {
			cOut.write(cipherBytes);
		} catch (IOException e) {
			// ignore
		} finally {
			cOut.close();
		}
		return bOut.toByteArray();
	}

	/**
	 * encodeAesCtr128
	 * 
	 * @param keyBytes
	 *            秘钥
	 * @param msg
	 *            加密原文
	 * @param ivBytes
	 *            初始向量
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IOException
	 */
	public static byte[] encodeAesCtr128(byte[] keyBytes, byte[] msg, byte[] ivBytes)
			throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IOException {
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		ByteArrayInputStream bIn = new ByteArrayInputStream(msg);
		CipherInputStream cIn = new CipherInputStream(bIn, cipher);
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		int ch;
		try {
			while ((ch = cIn.read()) >= 0) {
				bOut.write(ch);
			}
		} catch (IOException e) {
			// ignore
		} finally {
			cIn.close();
			bIn.close();
			bOut.close();
		}

		byte[] cipherText = bOut.toByteArray();

		return cipherText;
	}

}
