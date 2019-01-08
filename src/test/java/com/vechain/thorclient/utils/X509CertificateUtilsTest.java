package com.vechain.thorclient.utils;

import com.vechain.thorclient.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.security.cert.X509Certificate;

@RunWith(JUnit4.class)
public class X509CertificateUtilsTest extends BaseTest {

    @Test
    public void testParseCertificate(){
       String certStr =
                "MIIBvjCCAWQCCQDQT06GZhvVezAKBggqhkjOPQQDAjBPMQswCQYDVQQGEwJDTjEK" +
                "MAgGA1UECAwBYTEKMAgGA1UEBwwBYTEKMAgGA1UECgwBYTEKMAgGA1UECwwBYTEQ" +
                "MA4GCSqGSIb3DQEJARYBYTAeFw0xODEyMjkwOTI3MTlaFw0xOTAxMjgwOTI3MTla" +
                "MIGBMQswCQYDVQQGEwJDTjELMAkGA1UECAwCU0gxCzAJBgNVBAcMAlNIMRAwDgYD" +
                "VQQKDAdWZWNoYWluMRAwDgYDVQQLDAdWZWNoYWluMRAwDgYDVQQDDAd2ZWNoYWlu" +
                "MSIwIAYJKoZIhvcNAQkBFhN2ZWNoYWluQHZlY2hhaW4uY29tMFYwEAYHKoZIzj0C" +
                "AQYFK4EEAAoDQgAEiXdK4R/A6yAZyiIcVTkCcBylsb1poodto42dxq8USWw0RpTM" +
                "BYRY35vxwRDc/189rPOHmHDRhHHnNzeBxu/AhzAKBggqhkjOPQQDAgNIADBFAiEA" +
                "+Px0VrRo0AxaTGHog71W6ohrq7Nn3ajZz/uWmnGzyCgCIBeoHzoK+z7SfWybpYlH" +
                "OrjrNiN9nldORqoFwXib3743";

        System.out.println(certStr);
        X509Certificate certificate = X509CertificateUtils.loadCertificate( certStr );
        System.out.println("SN: "+BytesUtils.toHexString(certificate.getSerialNumber().toByteArray(),Prefix.ZeroLowerX));
        System.out.println("Sig: "+BytesUtils.toHexString(certificate.getSignature(),Prefix.ZeroLowerX));

        System.out.println("PUB: "+BytesUtils.toHexString(certificate.getPublicKey().getEncoded(),Prefix.ZeroLowerX));

        byte[] serialBytes  = certificate.getSerialNumber().toByteArray();

        logger.info("serialBytes:" + BytesUtils.toHexString( serialBytes, Prefix.ZeroLowerX ));
        byte[] signature = certificate.getSignature();
        logger.info( "Signatures:" + BytesUtils.toHexString( signature, Prefix.ZeroLowerX ));
        byte[] pub = X509CertificateUtils.extractPublicKey( certificate );
        logger.info( "Public key:" + BytesUtils.toHexString( pub, Prefix.ZeroLowerX ) );
        Assert.assertNotNull(certificate);

    }

    @Test
    public void testVerifyCertificate(){
        String certStr = "-----BEGIN CERTIFICATE-----" +
                "MIIBvjCCAWQCCQDQT06GZhvVezAKBggqhkjOPQQDAjBPMQswCQYDVQQGEwJDTjEK" +
                        "MAgGA1UECAwBYTEKMAgGA1UEBwwBYTEKMAgGA1UECgwBYTEKMAgGA1UECwwBYTEQ" +
                        "MA4GCSqGSIb3DQEJARYBYTAeFw0xODEyMjkwOTI3MTlaFw0xOTAxMjgwOTI3MTla" +
                        "MIGBMQswCQYDVQQGEwJDTjELMAkGA1UECAwCU0gxCzAJBgNVBAcMAlNIMRAwDgYD" +
                        "VQQKDAdWZWNoYWluMRAwDgYDVQQLDAdWZWNoYWluMRAwDgYDVQQDDAd2ZWNoYWlu" +
                        "MSIwIAYJKoZIhvcNAQkBFhN2ZWNoYWluQHZlY2hhaW4uY29tMFYwEAYHKoZIzj0C" +
                        "AQYFK4EEAAoDQgAEiXdK4R/A6yAZyiIcVTkCcBylsb1poodto42dxq8USWw0RpTM" +
                        "BYRY35vxwRDc/189rPOHmHDRhHHnNzeBxu/AhzAKBggqhkjOPQQDAgNIADBFAiEA" +
                        "+Px0VrRo0AxaTGHog71W6ohrq7Nn3ajZz/uWmnGzyCgCIBeoHzoK+z7SfWybpYlH" +
                        "OrjrNiN9nldORqoFwXib3743" +
                "-----END CERTIFICATE-----";

        System.out.println(certStr);

        X509Certificate certificate = X509CertificateUtils.loadCertificate( certStr );
        String publicKeyHex = "0x040e8fe61092bbbbb468f17ccfa39548c7a161318ed4c6cb5b2bbd45f9562ba9133f2ce244fccd7e00cb0ab80b5790d678ca769fc629cfabed2a2dea3328f420e8";
        byte[] pub = BytesUtils.toByteArray( publicKeyHex );
        boolean isOK = X509CertificateUtils.verifyCertificateSignature( certificate, pub );
        Assert.assertTrue( isOK );
    }

    @Test
    public void testVerifyCertificateFromRootPublicKey() {
        String certStr = "-----BEGIN CERTIFICATE-----\n" +
                "MIIBBTCBrQIKfqrMEyQ7DQoAATAKBggqhkjOPQQDAjANMQswCQYDVQQKDAJ2ZTAe\n" +
                "Fw0xOTAxMDgwNjQ0MDNaFw0yMDAxMDgwNjQ0MDNaMAwxCjAIBgNVBAoMAWgwVjAQ\n" +
                "BgcqhkjOPQIBBgUrgQQACgNCAARgR37amqthM4xc+G5rY7K4yu5GNo5gB6EkhxWj\n" +
                "uS4mURWoUyhp9aTMwXWXpla0h1u/EqNuVBg/jfZ029PGGur/MAoGCCqGSM49BAMC\n" +
                "A0cAMEQCIB6BSeGhhyYHLyh9wb5KB4QpMMRi6wPhoGvqSV6TItdGAiA/jVbD2v5n\n" +
                "MNciVOwZRkv9ibyQFtZQEqCEuO7dWCUUxw==\n" +
                "-----END CERTIFICATE-----";

        X509Certificate certificate = X509CertificateUtils.loadCertificate( certStr );
        logger.info("version:" + certificate.getVersion());
        byte[] certSerialNumBytes = certificate.getSerialNumber().toByteArray();
        logger.info( "Certificate serial number: " + BytesUtils.toHexString( certSerialNumBytes, Prefix.ZeroLowerX ) );
        byte[] rootPubKey = BytesUtils.toByteArray(
                "0x036311FBABB1216467D7D823CBB2D3B9DBFB843A7D91AD16B1ED71596E094E4BBF" );
        byte[] chaincode = BytesUtils.toByteArray(
                "0xD39E4AACAB973117A4E64E636E067DA715337508F4DDDEF14C0BD2AD6E598760" );
        logger.info( "Certificate signature: " + BytesUtils.toHexString( certificate.getSignature(), Prefix.ZeroLowerX ) );
        boolean isVerified = X509CertificateUtils.verifyCertificateSignature( certificate, rootPubKey, chaincode );
        Assert.assertTrue( isVerified );
    }

    @Test
    public void testVerifyTxSignature(){
        String certStr = "-----BEGIN CERTIFICATE-----\n" +
                "MIIBCzCBsgIKfqrMwEEzDQoAATAKBggqhkjOPQQDAjAMMQowCAYDVQQKDAFhMB4X\n" +
                "DTE5MDEwNzA5MzAxOFoXDTIwMDEwNzA5MzAxOFowEjEQMA4GA1UECgwHVmVjaGFp\n" +
                "bjBWMBAGByqGSM49AgEGBSuBBAAKA0IABGBHftqaq2EzjFz4bmtjsrjK7kY2jmAH\n" +
                "oSSHFaO5LiZRFahTKGn1pMzBdZemVrSHW78So25UGD+N9nTb08Ya6v8wCgYIKoZI\n" +
                "zj0EAwIDSAAwRQIhAPcZnmzIQ/whXYMN4inqwxVBY7z/oOj1CQFrHgEfuFxOAiAm\n" +
                "jx8AdXNGYvhhQR2e9KGsNc6femrqC+gSqdl8SVX6Ug==\n" +
                "-----END CERTIFICATE-----\n";

        X509Certificate certificate = X509CertificateUtils.loadCertificate( certStr );
        byte[] pub = X509CertificateUtils.extractPublicKey( certificate );
        logger.info( "Certificate public key: " + BytesUtils.toHexString( pub, Prefix.ZeroLowerX ) );
        boolean isVerified = X509CertificateUtils.verifyTxSignature( "0x6DA528CFCFD9575BD432614D65A7BDA7B3D8129B54B74631D5DFCD9879387860", "0x7D0966B15136E7F4A4E3C12C376AC170341A6879FD2943D93BFA242DA080388A26162C0283F7922277A8DC1CAFFD624BB3C6A7EF94F61ACBE5B31BFEF233C215", certificate);
        Assert.assertTrue( isVerified );
    }

}
