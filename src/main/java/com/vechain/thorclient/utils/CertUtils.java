package com.vechain.thorclient.utils;

import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.utils.crypto.ECDSASign;
import com.vechain.thorclient.utils.crypto.ECDSASignature;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

public class CertUtils {

	public static String PERFIX_PUBLIC = "04";

	/**
	 * buildSignContent
	 * 
	 * @param certSha256Bytes
	 * @param params
	 * @return
	 * @throws CertificateEncodingException
	 */
	public static byte[] buildSignContent(String certString, Object... params) throws CertificateEncodingException {
		X509Certificate x509Certificate = X509CertificateUtils.loadCertificate(certString);
		byte[] tbs = x509Certificate.getTBSCertificate();
		// 将证书的内容转hex 字符
		byte[] allBytes = CryptoUtils.sha256(tbs);
		for (Object object : params) {
			byte[] param = null;
			if (object instanceof String) {
				param = CryptoUtils.sha256(BytesUtils.toByteArray((String) object));
			} else if (object instanceof RawTransaction) {
				param = CryptoUtils.sha256(CryptoUtils.blake2b(((RawTransaction) object).encode()));
			} else {
				param = CryptoUtils.sha256((byte[]) object);
			}
			allBytes = concatBytes(allBytes, param);
		}
		return allBytes;
	}

	/**
	 * 
	 * @param x509Certificate
	 * @param params
	 * @return
	 * @throws CertificateEncodingException
	 */
	public static byte[] buildSignContent(X509Certificate x509Certificate, Object... params)
			throws CertificateEncodingException {
		byte[] tbs = x509Certificate.getTBSCertificate();
		// 将证书的内容转hex 字符
		byte[] allBytes = CryptoUtils.sha256(tbs);
		for (Object object : params) {
			byte[] param = null;
			if (object instanceof String) {
				param = CryptoUtils.sha256(BytesUtils.toByteArray((String) object));
			} else if (object instanceof RawTransaction) {
				param = CryptoUtils.sha256(CryptoUtils.blake2b(((RawTransaction) object).encode()));
			} else {
				param = CryptoUtils.sha256((byte[]) object);
			}
			allBytes = concatBytes(allBytes, param);
		}
		return allBytes;
	}

	/**
	 * concatBytes
	 * 
	 * @param params
	 * @return
	 */
	private static byte[] concatBytes(byte[]... params) {
		int totalLength = 0;
		for (byte[] bs : params) {
			totalLength += bs.length;
		}
		byte[] rtnByte = new byte[totalLength];
		int index = 0;
		for (byte[] bs : params) {
			System.arraycopy(bs, 0, rtnByte, index, bs.length);
			index += bs.length;
		}
		return rtnByte;
	}

	/**
	 * hash and sign
	 * 
	 * @param message
	 * @param certPrivateKey
	 * @return
	 */
	public static String hashAndSign(byte[] message, String certPrivateKey) {
		return sign(CryptoUtils.sha256(message), certPrivateKey);
	}

	/**
	 * sign
	 * 
	 * @param message
	 * @param certPrivateKey
	 * @return
	 */
	public static String sign(byte[] message, String certPrivateKey) {
		// sign message
		StringBuffer signatureSb = new StringBuffer();
		ECKeyPair keyPair = ECKeyPair.create(certPrivateKey);
		ECDSASign.SignatureData signatureInfodata = ECDSASign.signMessage(message, keyPair, false);
		// 序列化
		signatureSb.append(BytesUtils.toHexString(signatureInfodata.getR(), null));
		signatureSb.append(BytesUtils.toHexString(signatureInfodata.getS(), null));
		signatureSb.append("0");
		signatureSb.append(signatureInfodata.getV());
		return BytesUtils.toHexString(signatureInfodata.toByteArray(), null);
	}

	/**
	 * recover public key
	 * 
	 * @param signature
	 * @param challengeHash
	 * @return
	 */
	public static String recoverPublicKey(byte[] signature, byte[] allMessage) {
		byte[] rBytes = new byte[32];
		byte[] sBytes = new byte[32];
		System.arraycopy(signature, 0, rBytes, 0, rBytes.length);
		System.arraycopy(signature, 32, sBytes, 0, sBytes.length);
		byte recovery = 0;
		if (signature.length >= 65) {
			recovery = signature[64];
		}
		ECDSASignature ecdsaSignature = new ECDSASignature(rBytes, sBytes);
		BigInteger publicKey = ECDSASign.recoverFromSignature(recovery, ecdsaSignature, CryptoUtils.sha256(allMessage));

		String publicKeyStr = BytesUtils.toHexString(BytesUtils.toBytesPadded(publicKey, 64), null);
		return PERFIX_PUBLIC + publicKeyStr;
	}

	/**
	 * verifySignature
	 * 
	 * @param certString
	 * @param signHexString
	 * @param params
	 * @return
	 * @throws CertificateEncodingException
	 */
	public static boolean verifySignature(String certString, String signHexString, Object... params)
			throws CertificateEncodingException {
		X509Certificate x509Certificate = X509CertificateUtils.loadCertificate(certString);
		byte[] allBytes = buildSignContent(x509Certificate, params);
		return X509CertificateUtils.verifyTxSignature(BytesUtils.toHexString(CryptoUtils.sha256(allBytes), null),
				signHexString, x509Certificate);

	}
}
