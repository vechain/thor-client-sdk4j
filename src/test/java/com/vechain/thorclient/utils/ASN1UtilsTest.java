package com.vechain.thorclient.utils;

import org.junit.Test;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class ASN1UtilsTest {

    ASN1Utils asn = new ASN1Utils();

    @Test
    public void decodeASN1() {
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

        ASN1Utils.decodeASN1(certificate.getSignature());

    }

    @Test
    public void getOutputStreamString() {
    }

    @Test
    public void encodeASN1Interger() {
        asn.encodeASN1Interger(new BigInteger("10",16));
        System.out.println(asn.getOutputStreamString());
        asn.encodeASN1Interger(new BigInteger("20"));
        System.out.println(asn.getOutputStreamString());
    }

    @Test
    public void encodeASN1Boolean() {
        asn.encodeASN1Boolean(false);
        System.out.println(asn.getOutputStreamString());
        asn.encodeASN1Boolean(true);
        System.out.println(asn.getOutputStreamString());
    }

    @Test
    public void encodeASN1BitString() {
        asn.encodeASN1BitString("1234");
        System.out.println(asn.getOutputStreamString());
    }



    @Test
    public void decodeASN1Interger() {
        byte[] b = StringUtils.toHexBytes("020110");
        System.out.println(ASN1Utils.decodeASN1Interger(b));
    }

    @Test
    public void decodeASN1Boolean() {
        byte[] b = StringUtils.toHexBytes("010100");
        System.out.println(ASN1Utils.decodeASN1Boolean(b));
    }

    @Test
    public void encodeASN1UTCTime() {
        Date d = new Date();

        System.out.println(d);
        System.out.println(d.hashCode());

        ASN1UtilsTest.getCurrentUtcTime();
        Calendar cal = Calendar.getInstance();
        System.out.println(String.valueOf(cal.getTimeInMillis()/1000));

    }

    public static void getCurrentUtcTime() {
        Date l_datetime = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        TimeZone l_timezone = TimeZone.getTimeZone("GMT-0");
        formatter.setTimeZone(l_timezone);
        String l_utc_date = formatter.format(l_datetime);
        System.out.println(l_utc_date +"(Local)");
    }


}