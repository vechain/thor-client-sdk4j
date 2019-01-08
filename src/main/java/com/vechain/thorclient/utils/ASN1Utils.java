package com.vechain.thorclient.utils;

import org.bouncycastle.asn1.*;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;

public class ASN1Utils {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    DEROutputStream derOutputStream = new DEROutputStream(outputStream);

    public ASN1Utils() {
    }



    public static void decodeASN1(byte[] cert){
        try{
            ASN1InputStream asn1InputStream = new ASN1InputStream(cert);
            //将hex转换为byte输出s
            ASN1Primitive asn1Primitive = null;
            while ((asn1Primitive = asn1InputStream.readObject()) != null) {
                //循环读取，分类解析。这样的解析方式可能不适合有两个同类的ASN1对象解析，如果遇到同类，那就需要按照顺序来调用readObject，就可以实现解析了。
                if ( asn1Primitive instanceof ASN1Integer) {
                    ASN1Integer asn1Integer = (ASN1Integer) asn1Primitive;
                    System.out.println("Integer:" + asn1Integer.getValue());
                }else if (asn1Primitive instanceof ASN1Boolean) {
                    ASN1Boolean asn1Boolean = (ASN1Boolean) asn1Primitive;
                    System.out.println("Boolean:"+asn1Boolean.isTrue());
                }else if (asn1Primitive instanceof ASN1Sequence) {
                    ASN1Sequence asn1Sequence = (ASN1Sequence) asn1Primitive;
                    ASN1SequenceParser asn1SequenceParser = asn1Sequence.parser();
                    ASN1Encodable asn1Encodable = null;
                    while ((asn1Encodable = asn1SequenceParser.readObject()) != null) {
                        asn1Primitive = asn1Encodable.toASN1Primitive();
                        if (asn1Primitive instanceof ASN1String) {
                            ASN1String string = (ASN1String) asn1Primitive;
                            System.out.println("PrintableString:"+string.getString());
                        }else if (asn1Primitive instanceof ASN1UTCTime) {
                            ASN1UTCTime asn1utcTime = (ASN1UTCTime) asn1Primitive;
                            System.out.println("UTCTime:"+asn1utcTime.getTime());
                        }else if (asn1Primitive instanceof ASN1Null) {
                            System.out.println("NULL");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String decodeASN1Date(byte[] i){
        ASN1InputStream asn1InputStream = new ASN1InputStream(i);

        try {
            ASN1Primitive asn1Primitive = asn1InputStream.readObject();
            if (asn1Primitive instanceof DERUTCTime){
                DERUTCTime derutcTime = (DERUTCTime) asn1Primitive;
                return derutcTime.getTime();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static BigInteger decodeASN1Interger(byte[] i){
        ASN1InputStream asn1InputStream = new ASN1InputStream(i);

        try {
            ASN1Primitive asn1Primitive = asn1InputStream.readObject();
            if (asn1Primitive instanceof ASN1Integer){
                ASN1Integer asn1Integer = (ASN1Integer) asn1Primitive;
                return asn1Integer.getValue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BigInteger.ZERO;
    }

    public static boolean decodeASN1Boolean(byte[] i){
        ASN1InputStream asn1InputStream = new ASN1InputStream(i);

        try {
            ASN1Primitive asn1Primitive = asn1InputStream.readObject();
            if (asn1Primitive instanceof ASN1Boolean){
                ASN1Boolean asn1Boolean = (ASN1Boolean) asn1Primitive;
                return asn1Boolean.isTrue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getOutputStreamString(){
        return BytesUtils.toHexString(outputStream.toByteArray(),Prefix.NoPrefix);
    }

    public void encodeASN1Interger(BigInteger i){
        try {
            derOutputStream.writeObject(new ASN1Integer(i));
            derOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void encodeASN1Boolean(boolean b){
        try {
            derOutputStream.writeObject(ASN1Boolean.getInstance(b));
            derOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void encodeASN1UTCTime(Date t){
        try {
            derOutputStream.writeObject(new DERUTCTime(t));
            derOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void encodeASN1BitString(String s){
        try {
            derOutputStream.writeObject(new DERBitString(StringUtils.toHexBytes(s),0));
            derOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void encodeASN1Sequence(DERSequence seq){
        try {
            derOutputStream.writeObject(seq);
            derOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
