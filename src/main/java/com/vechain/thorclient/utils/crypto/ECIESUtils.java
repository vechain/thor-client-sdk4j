package com.vechain.thorclient.utils.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

public class ECIESUtils {

	public static byte[] pbkdf2withsha512(byte[] password, byte[] salt) throws UnsupportedEncodingException {
		PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA512Digest());
		gen.init(password, salt, 2048);
		byte[] dk = ((KeyParameter) gen.generateDerivedParameters(512)).getKey();
		return dk;
	}

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
