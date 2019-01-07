package com.vechain.thorclient.utils;

import org.bouncycastle.asn1.*;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class ASN1Utils {


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


    public static void encodeASN1(){
        try{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //创建ByteArrayOutputStream，用于放置输出的byte流
            DEROutputStream derOutputStream = new DEROutputStream(outputStream);
            //创建DEROutputStream
            derOutputStream.writeObject(new DERInteger(10));
            //写入DERInteger数据，10对应的hex为0a。
            derOutputStream.writeObject(new DERBoolean(false));
            //写入DERBoolean数据，false对应asn1中的hex为00

            ASN1EncodableVector encodableVector = new ASN1EncodableVector();
            //创建ASN1EncodableVector，用于放Sequence的数据
            encodableVector.add(new DERPrintableString("PP"));
            //encodableVector中写入各种对象
            encodableVector.add(new DERUTCTime(new Date()));
            encodableVector.add(new DERNull());
            DERSequence derSequence = new DERSequence(encodableVector);
            //ASN1EncodableVector封装为DERSequence

            derOutputStream.writeObject(derSequence);
            //写入DERSequence数据。

            derOutputStream.flush();
            System.out.println(BytesUtils.toHexString(outputStream.toByteArray(),Prefix.ZeroLowerX));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
