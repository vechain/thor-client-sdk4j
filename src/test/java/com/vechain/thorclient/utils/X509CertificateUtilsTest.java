package com.vechain.thorclient.utils;

import com.vechain.thorclient.base.BaseTest;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
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
                "MIIBxzCCAW4CCH6qzOY1lA0KMAoGCCqGSM49BAMCMFoxCzAJBgNVBAYTAkNOMQsw\n" +
                "CQYDVQQIDAJTSDELMAkGA1UEBwwCU0gxEDAOBgNVBAoMB1ZlY2hhaW4xEDAOBgNV\n" +
                "BAsMB1ZlY2hhaW4xDTALBgNVBAMMBENhcnkwHhcNMTgwODE2MDQ1MjQ5WhcNMTkw\n" +
                "ODE2MDQ1MjQ5WjCBgTELMAkGA1UEBhMCQ04xCzAJBgNVBAgMAlNIMQswCQYDVQQH\n" +
                "DAJTSDEQMA4GA1UECgwHVmVjaGFpbjEQMA4GA1UECwwHVmVjaGFpbjEQMA4GA1UE\n" +
                "AwwHdmVjaGFpbjEiMCAGCSqGSIb3DQEJARYTdmVjaGFpbkB2ZWNoYWluLmNvbTBW\n" +
                "MBAGByqGSM49AgEGBSuBBAAKA0IABIl3SuEfwOsgGcoiHFU5AnAcpbG9aaKHbaON\n" +
                "ncavFElsNEaUzAWEWN+b8cEQ3P9fPazzh5hw0YRx5zc3gcbvwIcwCgYIKoZIzj0E\n" +
                "AwIDRwAwRAIgLYg9Fnh0CdU4N5V1UWMXVBrSi4Ry95DqwOC+ORpok2wCIBC33Llh\n" +
                "MnCnPOGiAACYuN5/bvxheCUL1+JhKQ47MNXy\n" +
                "-----END CERTIFICATE-----";

        X509Certificate certificate = X509CertificateUtils.loadCertificate( certStr );
        logger.info("version:" + certificate.getVersion());
        byte[] certSerialNumBytes = certificate.getSerialNumber().toByteArray();
        logger.info( "Certificate serial number: " + BytesUtils.toHexString( certSerialNumBytes, Prefix.ZeroLowerX ) );
        byte[] rootPubKey = BytesUtils.toByteArray(
                "0x0231D0D71862ED74A9A4F8850A79FB5338ED71400001170460E616D7A3AD38207F" );
        byte[] chaincode = BytesUtils.toByteArray(
                "0x301358799E03303D7D16B45DB1BC42357660A75C4FC9706DDCB729C682EE5CA5" );
        logger.info( "Certificate signature: " + BytesUtils.toHexString( certificate.getSignature(), Prefix.ZeroLowerX ) );
        boolean isVerified = X509CertificateUtils.verifyCertificateSignature( certificate, rootPubKey, chaincode );
        Assert.assertTrue( isVerified );
    }




}
