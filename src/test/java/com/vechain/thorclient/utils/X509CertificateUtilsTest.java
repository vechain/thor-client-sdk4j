package com.vechain.thorclient.utils;

import com.vechain.thorclient.base.BaseTest;
import org.bouncycastle.asn1.x509.Certificate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.math.BigInteger;
import java.security.PublicKey;
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

        X509Certificate certificate = X509CertificateUtils.loadCertificate( certStr );

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

        X509Certificate certificate = X509CertificateUtils.loadCertificate( certStr );
        String publicKeyHex = "0x040e8fe61092bbbbb468f17ccfa39548c7a161318ed4c6cb5b2bbd45f9562ba9133f2ce244fccd7e00cb0ab80b5790d678ca769fc629cfabed2a2dea3328f420e8";
        byte[] pub = BytesUtils.toByteArray( publicKeyHex );
        boolean isOK = X509CertificateUtils.verifyCertificateSignature( certificate, pub );
        Assert.assertTrue( isOK );
    }


}
