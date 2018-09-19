package com.vechain.thorclient.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vechain.thorclient.utils.crypto.ECDSASign;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

public class USBKeyUtils {

	private static Logger logger = LoggerFactory.getLogger(USBKeyUtils.class);

	/**
	 * 发送usb交易
	 * 
	 * @param cerHexStr
	 * @param txRawHash
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String buildSignature(byte[] cert, byte[] txRawHash, String privateKey) throws Exception {
		byte[] txRawBytes = CryptoUtils.blake2b(txRawHash);
		byte[] cerHexBytes = CryptoUtils.sha256(cert);

		byte[] message = new byte[txRawBytes.length + cerHexBytes.length];
		System.arraycopy(cerHexBytes, 0, message, 0, cerHexBytes.length);
		System.arraycopy(txRawBytes, 0, message, cerHexBytes.length, txRawBytes.length);

		ECKeyPair key = ECKeyPair.create(BytesUtils.toByteArray(privateKey));
		ECDSASign.SignatureData signature = ECDSASign.signMessage(CryptoUtils.sha256(message), key, false);

		byte[] signBytes = signature.toByteArray();
		logger.info("signature: {} {}", BytesUtils.toHexString(signBytes, null),
				BytesUtils.cleanHexPrefix(BytesUtils.toHexString(signature.getR(), Prefix.ZeroLowerX))
						+ BytesUtils.cleanHexPrefix(BytesUtils.toHexString(signature.getS(), Prefix.ZeroLowerX)) + "0"
						+ signature.getV());
		return BytesUtils.toHexString(signBytes, null);
	}
}
