package com.vechain.thorclient.utils;

import com.vechain.thorclient.utils.crypto.ECKey;
import sun.security.ec.ECPublicKeyImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Base64;
import java.util.logging.Logger;

public class X509CertificateUtils {

    public static boolean verifyCertificateSignature(X509Certificate certificate, byte[] publicKey){
        if( certificate == null){
            return false;
        }
        PublicKey ecPublicKey = createECPublicKey( publicKey );
        if (ecPublicKey == null){
            return false;
        }
        try {
            certificate.verify( ecPublicKey );
            return true;
        } catch (CertificateException | SignatureException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] extractPublicKey(X509Certificate certificate){
        PublicKey publicKey = certificate.getPublicKey();

        if(publicKey instanceof ECPublicKeyImpl){
            return ((ECPublicKeyImpl) publicKey).getEncodedPublicValue();
        }else{
            return null;
        }
    }


    public static X509Certificate loadCertificate(String cert){
        byte[] certBytes = Base64.getDecoder().decode( cert );
        return parseCertificate( certBytes );
    }


    public static X509Certificate parseCertificate(byte[] certBytes){
        if (certBytes == null) {
            return null;
        }
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        if (cf == null){
            return null;
        }

        InputStream inputStream = new ByteArrayInputStream(certBytes);
        X509Certificate certificate = null;
        try {
            certificate = (X509Certificate) cf.generateCertificate( inputStream );
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return certificate;
    }

    public static PublicKey createECPublicKey(byte[] keyBytes){

        ECPoint pubPoint = ECKey.decodeECPoint( keyBytes );
        AlgorithmParameters parameters = null;
        try {
            parameters = AlgorithmParameters.getInstance("EC", "SunEC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
            return null;
        }
        try {
            parameters.init(new ECGenParameterSpec("secp256k1"));
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
            return null;
        }
        ECParameterSpec ecParameters = null;
        try {
            ecParameters = parameters.getParameterSpec(ECParameterSpec.class);
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
            return null;
        }
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(pubPoint, ecParameters);
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("EC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        try {
            return (ECPublicKey)kf.generatePublic(pubSpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static boolean verifyTxSignature(String hexTxStr, String hexSignature, String hexCertificateStr){
        return false;
    }


}
