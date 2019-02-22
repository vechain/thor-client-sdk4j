package com.vechain.thorclient.utils;

import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.clients.BlockchainClient;
import com.vechain.thorclient.clients.TransactionClient;
import com.vechain.thorclient.core.model.blockchain.NodeProvider;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.core.model.clients.ToClause;
import com.vechain.thorclient.core.model.clients.ToData;
import com.vechain.thorclient.core.model.clients.base.AbstractToken;
import com.vechain.thorclient.utils.crypto.ECDSASign;
import com.vechain.thorclient.utils.crypto.ECDSASignature;
import com.vechain.thorclient.utils.crypto.ECKey;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

@RunWith(JUnit4.class)
public class X509CertificateUtilsTest extends BaseTest {

	@Test
	public void testParseCertificate() {
		String certStr = "MIIBvjCCAWQCCQDQT06GZhvVezAKBggqhkjOPQQDAjBPMQswCQYDVQQGEwJDTjEK"
				+ "MAgGA1UECAwBYTEKMAgGA1UEBwwBYTEKMAgGA1UECgwBYTEKMAgGA1UECwwBYTEQ"
				+ "MA4GCSqGSIb3DQEJARYBYTAeFw0xODEyMjkwOTI3MTlaFw0xOTAxMjgwOTI3MTla"
				+ "MIGBMQswCQYDVQQGEwJDTjELMAkGA1UECAwCU0gxCzAJBgNVBAcMAlNIMRAwDgYD"
				+ "VQQKDAdWZWNoYWluMRAwDgYDVQQLDAdWZWNoYWluMRAwDgYDVQQDDAd2ZWNoYWlu"
				+ "MSIwIAYJKoZIhvcNAQkBFhN2ZWNoYWluQHZlY2hhaW4uY29tMFYwEAYHKoZIzj0C"
				+ "AQYFK4EEAAoDQgAEiXdK4R/A6yAZyiIcVTkCcBylsb1poodto42dxq8USWw0RpTM"
				+ "BYRY35vxwRDc/189rPOHmHDRhHHnNzeBxu/AhzAKBggqhkjOPQQDAgNIADBFAiEA"
				+ "+Px0VrRo0AxaTGHog71W6ohrq7Nn3ajZz/uWmnGzyCgCIBeoHzoK+z7SfWybpYlH" + "OrjrNiN9nldORqoFwXib3743";

		System.out.println(certStr);
		X509Certificate certificate = X509CertificateUtils.loadCertificate(certStr);
		System.out.println(
				"SN: " + BytesUtils.toHexString(certificate.getSerialNumber().toByteArray(), Prefix.ZeroLowerX));
		System.out.println("Sig: " + BytesUtils.toHexString(certificate.getSignature(), Prefix.ZeroLowerX));

		System.out
				.println("PUB: " + BytesUtils.toHexString(certificate.getPublicKey().getEncoded(), Prefix.ZeroLowerX));

		byte[] serialBytes = certificate.getSerialNumber().toByteArray();

		logger.info("serialBytes:" + BytesUtils.toHexString(serialBytes, Prefix.ZeroLowerX));
		byte[] signature = certificate.getSignature();
		logger.info("Signatures:" + BytesUtils.toHexString(signature, Prefix.ZeroLowerX));
		byte[] pub = X509CertificateUtils.extractPublicKey(certificate);
		logger.info("Public key:" + BytesUtils.toHexString(pub, Prefix.ZeroLowerX));
		Assert.assertNotNull(certificate);

	}

	@Test
	public void testVerifyCertificate() {
		String certStr = "-----BEGIN CERTIFICATE-----"
				+ "MIIBvjCCAWQCCQDQT06GZhvVezAKBggqhkjOPQQDAjBPMQswCQYDVQQGEwJDTjEK"
				+ "MAgGA1UECAwBYTEKMAgGA1UEBwwBYTEKMAgGA1UECgwBYTEKMAgGA1UECwwBYTEQ"
				+ "MA4GCSqGSIb3DQEJARYBYTAeFw0xODEyMjkwOTI3MTlaFw0xOTAxMjgwOTI3MTla"
				+ "MIGBMQswCQYDVQQGEwJDTjELMAkGA1UECAwCU0gxCzAJBgNVBAcMAlNIMRAwDgYD"
				+ "VQQKDAdWZWNoYWluMRAwDgYDVQQLDAdWZWNoYWluMRAwDgYDVQQDDAd2ZWNoYWlu"
				+ "MSIwIAYJKoZIhvcNAQkBFhN2ZWNoYWluQHZlY2hhaW4uY29tMFYwEAYHKoZIzj0C"
				+ "AQYFK4EEAAoDQgAEiXdK4R/A6yAZyiIcVTkCcBylsb1poodto42dxq8USWw0RpTM"
				+ "BYRY35vxwRDc/189rPOHmHDRhHHnNzeBxu/AhzAKBggqhkjOPQQDAgNIADBFAiEA"
				+ "+Px0VrRo0AxaTGHog71W6ohrq7Nn3ajZz/uWmnGzyCgCIBeoHzoK+z7SfWybpYlH" + "OrjrNiN9nldORqoFwXib3743"
				+ "-----END CERTIFICATE-----";

		System.out.println(certStr);

		X509Certificate certificate = X509CertificateUtils.loadCertificate(certStr);
		String publicKeyHex = "0x040e8fe61092bbbbb468f17ccfa39548c7a161318ed4c6cb5b2bbd45f9562ba9133f2ce244fccd7e00cb0ab80b5790d678ca769fc629cfabed2a2dea3328f420e8";
		byte[] pub = BytesUtils.toByteArray(publicKeyHex);
		boolean isOK = X509CertificateUtils.verifyCertificateSignature(certificate, pub);
		Assert.assertTrue(isOK);
	}

	@Test
	public void testVerifyCertificateFromRootPublicKey() {
		String certStr = "-----BEGIN CERTIFICATE-----\n"
				+ "MIIBBTCBrQIKfqrMEyQ7DQoAATAKBggqhkjOPQQDAjANMQswCQYDVQQKDAJ2ZTAe\n"
				+ "Fw0xOTAxMDgwNjQ0MDNaFw0yMDAxMDgwNjQ0MDNaMAwxCjAIBgNVBAoMAWgwVjAQ\n"
				+ "BgcqhkjOPQIBBgUrgQQACgNCAARgR37amqthM4xc+G5rY7K4yu5GNo5gB6EkhxWj\n"
				+ "uS4mURWoUyhp9aTMwXWXpla0h1u/EqNuVBg/jfZ029PGGur/MAoGCCqGSM49BAMC\n"
				+ "A0cAMEQCIB6BSeGhhyYHLyh9wb5KB4QpMMRi6wPhoGvqSV6TItdGAiA/jVbD2v5n\n"
				+ "MNciVOwZRkv9ibyQFtZQEqCEuO7dWCUUxw==\n" + "-----END CERTIFICATE-----";

		X509Certificate certificate = X509CertificateUtils.loadCertificate(certStr);
		logger.info("version:" + certificate.getVersion());
		byte[] certSerialNumBytes = certificate.getSerialNumber().toByteArray();
		logger.info("Certificate serial number: " + BytesUtils.toHexString(certSerialNumBytes, Prefix.ZeroLowerX));
		byte[] rootPubKey = BytesUtils
				.toByteArray("0x036311FBABB1216467D7D823CBB2D3B9DBFB843A7D91AD16B1ED71596E094E4BBF");
		byte[] chaincode = BytesUtils.toByteArray("0xD39E4AACAB973117A4E64E636E067DA715337508F4DDDEF14C0BD2AD6E598760");
		logger.info("Certificate signature: " + BytesUtils.toHexString(certificate.getSignature(), Prefix.ZeroLowerX));
		boolean isVerified = X509CertificateUtils.verifyCertificateSignature(certificate, rootPubKey, chaincode);
		Assert.assertTrue(isVerified);
	}

	@Test
	public void testVerifyTxSignature() {
		String certStr = "-----BEGIN CERTIFICATE-----\n"
				+ "MIIBCzCBsgIKfqrMwEEzDQoAATAKBggqhkjOPQQDAjAMMQowCAYDVQQKDAFhMB4X\n"
				+ "DTE5MDEwNzA5MzAxOFoXDTIwMDEwNzA5MzAxOFowEjEQMA4GA1UECgwHVmVjaGFp\n"
				+ "bjBWMBAGByqGSM49AgEGBSuBBAAKA0IABGBHftqaq2EzjFz4bmtjsrjK7kY2jmAH\n"
				+ "oSSHFaO5LiZRFahTKGn1pMzBdZemVrSHW78So25UGD+N9nTb08Ya6v8wCgYIKoZI\n"
				+ "zj0EAwIDSAAwRQIhAPcZnmzIQ/whXYMN4inqwxVBY7z/oOj1CQFrHgEfuFxOAiAm\n"
				+ "jx8AdXNGYvhhQR2e9KGsNc6femrqC+gSqdl8SVX6Ug==\n" + "-----END CERTIFICATE-----\n";

		X509Certificate certificate = X509CertificateUtils.loadCertificate(certStr);
		byte[] pub = X509CertificateUtils.extractPublicKey(certificate);
		logger.info("Certificate public key: " + BytesUtils.toHexString(pub, Prefix.ZeroLowerX));
		boolean isVerified = X509CertificateUtils.verifyTxSignature(
				"0x6DA528CFCFD9575BD432614D65A7BDA7B3D8129B54B74631D5DFCD9879387860",
				"0x7D0966B15136E7F4A4E3C12C376AC170341A6879FD2943D93BFA242DA080388A26162C0283F7922277A8DC1CAFFD624BB3C6A7EF94F61ACBE5B31BFEF233C215",
				certificate);
		Assert.assertTrue(isVerified);
	}

	@Test
	public void testCertSignAndVerify() {
		String pem = "-----BEGIN CERTIFICATE-----" + "MIIBBTCBrQIKfqrMEyQ7DQoAATAKBggqhkjOPQQDAjANMQswCQYDVQQKDAJ2ZTAe"
				+ "Fw0xOTAxMDgwNjQ0MDNaFw0yMDAxMDgwNjQ0MDNaMAwxCjAIBgNVBAoMAWgwVjAQ"
				+ "BgcqhkjOPQIBBgUrgQQACgNCAARgR37amqthM4xc+G5rY7K4yu5GNo5gB6EkhxWj"
				+ "uS4mURWoUyhp9aTMwXWXpla0h1u/EqNuVBg/jfZ029PGGur/MAoGCCqGSM49BAMC"
				+ "A0cAMEQCIB6BSeGhhyYHLyh9wb5KB4QpMMRi6wPhoGvqSV6TItdGAiA/jVbD2v5n"
				+ "MNciVOwZRkv9ibyQFtZQEqCEuO7dWCUUxw==" + "-----END CERTIFICATE-----";
		logger.info("pem:");
		logger.info(pem);

		byte chainTag = 0x01;
//		byte[] blockRef = BlockchainClient.getBlockRef(Revision.BEST).toByteArray();
		byte[] blockRef = BytesUtils.toByteArray("000007715a8c08a4");
		logger.info(BytesUtils.toHexString(blockRef, null));
		Amount amount = Amount.createFromToken(AbstractToken.VET);
		amount.setDecimalAmount("1");
		ToClause clause = TransactionClient.buildVETToClause(
				Address.fromHexString("0x7567d83b7b8d80addcb281a71d54fc7b3364ffed"), amount, ToData.ZERO);
		RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(chainTag, blockRef,
				720, 21000, (byte) 0x0, "222".getBytes(), clause);

		String privateKey = "380481fbd888d0c27bc75d6deb0576cc7e31e91cd98078e9bde6294171c70b88";
		String txHash = BytesUtils.toHexString(CryptoUtils.blake2b((rawTransaction).encode()), null);
		logger.info("txHash:{}", txHash);

		String pemHex = BytesUtils.toHexString(pem.getBytes(), null);

		X509Certificate x509Certificate = X509CertificateUtils.loadCertificate(pem);
		byte[] tbs = null;
		try {
			tbs = x509Certificate.getTBSCertificate();
		} catch (CertificateEncodingException e) {
		}
		String certHexString = BytesUtils.toHexString(tbs, null);

		logger.info("certHexString {}", certHexString);

		byte[] certSha256Bytes = CryptoUtils.sha256(BytesUtils.toByteArray(certHexString));
		logger.info("certShaString {}", BytesUtils.toHexString(certSha256Bytes, null));


		String sign = sign(privateKey, certSha256Bytes, txHash);

		logger.info("sign:{}", sign);

		byte[] content = buildSignContent(certSha256Bytes, txHash);
		logger.info("content:{}", BytesUtils.toHexString(content, null));

		String publicKeyStr = recoverPublicKey(BytesUtils.toByteArray(sign), BytesUtils.toHexString(content, null));
		logger.info("recoverPublicKey:{}", publicKeyStr);

		logger.info("PUB: {}", BytesUtils.toHexString(X509CertificateUtils.extractPublicKey(x509Certificate), null));

//		boolean verifyTxSignature = X509CertificateUtils
//				.verifyTxSignature(BytesUtils.toHexString(CryptoUtils.sha256(content), null), sign, x509Certificate);
//		logger.info("verifyTxSignature:{}", verifyTxSignature);

		logger.info("verifying message hash: {}", BytesUtils.toHexString(CryptoUtils.sha256(content), null));
		logger.info("signature: {}",BytesUtils.toHexString(BytesUtils.toByteArray(sign), null));
		logger.info("public key: {}", BytesUtils.toHexString(X509CertificateUtils.extractPublicKey(x509Certificate),
				null));

		boolean verify = ECKey.verify(CryptoUtils.sha256(content), BytesUtils.toByteArray(sign),
				X509CertificateUtils.extractPublicKey(x509Certificate));

		logger.info("verify:{}", verify);
	}

	public static String PERFIX_PUBLIC = "04";

	public  String sign(String certPrivateKey, byte[] certSha256Bytes, Object... params) {
		byte[] allBytes = buildSignContent(certSha256Bytes, params);

		logger.info( "Signing hash: {}",  BytesUtils.toHexString(CryptoUtils.sha256(allBytes), Prefix.ZeroLowerX));
		String signatureStr = sign(CryptoUtils.sha256(allBytes), certPrivateKey);
		return signatureStr;
	}

	private static byte[] buildSignContent(byte[] certSha256Bytes, Object... params) {
		byte[] allBytes = certSha256Bytes;
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

	public static String sign(byte[] message, String certPrivateKey) {
		StringBuffer signatureSb = new StringBuffer();
		ECKeyPair keyPair = ECKeyPair.create(certPrivateKey);
		ECDSASign.SignatureData signatureInfodata = ECDSASign.signMessage(message, keyPair, false);
		signatureSb.append(BytesUtils.toHexString(signatureInfodata.getR(), null));
		signatureSb.append(BytesUtils.toHexString(signatureInfodata.getS(), null));
		signatureSb.append("0");
		signatureSb.append(signatureInfodata.getV());
		return BytesUtils.toHexString(signatureInfodata.toByteArray(), null);
	}

	public static String recoverPublicKey(byte[] signature, String challengeHexStr) {
		byte[] rBytes = new byte[32];
		byte[] sBytes = new byte[32];
		System.arraycopy(signature, 0, rBytes, 0, rBytes.length);
		System.arraycopy(signature, 32, sBytes, 0, sBytes.length);
		byte recovery = 0;
		if (signature.length >= 65) {
			recovery = signature[64];
		}
		ECDSASignature ecdsaSignature = new ECDSASignature(rBytes, sBytes);
		BigInteger publicKey = ECDSASign.recoverFromSignature(recovery, ecdsaSignature,
				CryptoUtils.sha256(BytesUtils.toByteArray(challengeHexStr)));

		String publicKeyStr = BytesUtils.toHexString(BytesUtils.toBytesPadded(publicKey, 64), null);
		return PERFIX_PUBLIC + publicKeyStr;
	}

}
