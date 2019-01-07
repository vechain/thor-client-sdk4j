package com.vechain.thorclient.utils;

import java.math.BigInteger;
import java.util.Arrays;

import com.vechain.thorclient.utils.crypto.ECKey;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.vechain.thorclient.utils.crypto.ECDSASign;
import com.vechain.thorclient.utils.crypto.ECIESUtils;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

@RunWith(JUnit4.class)
public class ECIESTest {

	@Test
	public void test() throws Exception {
		String msg = "Taylor Swift";

		System.out.println("Alice Key===================");
		String AlicePrivateKey = "a8e1664e42fa6014c8a97fb64f943a114660de14dc8889f1024487ebcc9aa67a";
		ECKeyPair aliceKey = ECKeyPair.create(BytesUtils.toByteArray(AlicePrivateKey));
		System.out.println(ByteUtils.toHexString(aliceKey.getRawPublicKey(false)));
		System.out.println(aliceKey.getAddress());

		System.out.println("Bob Key===================");
		String BobPrivateKey = "cab2002ddebea021ef9da251ea92198cf6bbeb6de34d834078783fbd86446334";
		ECKeyPair bobKey = ECKeyPair.create(BytesUtils.toByteArray(BobPrivateKey));
		ECPoint bobPoint = ECKey.publicPointFromPrivate(BytesUtils.bytesToBigInt(BytesUtils.toByteArray(BobPrivateKey)));
		System.out.println(ByteUtils.toHexString(bobKey.getRawPublicKey(false)));
		System.out.println(bobKey.getHexAddress());

		ECPoint bobPoint1 = ECIESUtils.createECPointFromPublicKey(ByteUtils.toHexString(bobKey.getRawPublicKey(false)));
		System.out.println(bobPoint.equals(bobPoint1));

		System.out.println("S1 S2===================");
		String S1 = "1c6ce96caacedd14f7f9ee25bdb4b75fbd2db4df";// bob的地址？
		String S2 = "1c6ce96caacedd14f7f9ee25bdb4b75fbd2db4df";// 7292c34b9a9fda166781144ecde28df279ca52a1
		byte[] s2bytes = BytesUtils.toByteArray(S2);
		System.out.println(S1);
		System.out.println(S2);

		System.out.println("1. 产⽣生随机数r，并计算R=r*G");
		String r = "640330c7610ee80b8fac93b079810d1712d9eca83e9f39f6aa75b3923e9b734b";

		ECPoint R = ECKey.publicPointFromPrivate(BytesUtils.bytesToBigInt(BytesUtils.toByteArray(r)));
		byte[] Rencoded = R.getEncoded(false);
		System.out.println("R:" + ByteUtils.toHexString(Rencoded));
		// remove prefix
		byte[] Rencoded2 = BytesUtils.toBytesPadded(new BigInteger(1, Arrays.copyOfRange(Rencoded, 1, Rencoded.length)),
				64);
		System.out.println("R:" + ByteUtils.toHexString(Rencoded2));

		System.out.println("2. 计算共享密钥，S=Px，P=(Px,Py)=r*KB,这⾥里里KB为Bob的公钥");
		ECPoint P = new FixedPointCombMultiplier().multiply(bobPoint,
				BytesUtils.bytesToBigInt(BytesUtils.toByteArray(r)));
		byte[] Pencoded = P.getEncoded(false);
		byte[] Pencoded2 = BytesUtils.toBytesPadded(new BigInteger(1, Arrays.copyOfRange(Pencoded, 1, Pencoded.length)),
				64);
		byte[] pxBytes = new byte[32];
		byte[] pyBytes = new byte[32];
		System.arraycopy(Pencoded2, 0, pxBytes, 0, pxBytes.length);
		System.arraycopy(Pencoded2, 32, pyBytes, 0, pyBytes.length);
		System.out.println("Px:" + ByteUtils.toHexString(pxBytes));
		System.out.println("Py:" + ByteUtils.toHexString(pyBytes));

		System.out.println("3. 使⽤用KDF算法，⽣生成对称加密密码和MAC的密码:KE||KM = KDF(S||S1)");

		byte[] K = ECIESUtils.pbkdf2withsha512(pxBytes, BytesUtils.toByteArray(S1));
		byte[] keBytes = new byte[32];
		byte[] kmBytes = new byte[32];
		System.arraycopy(K, 0, keBytes, 0, keBytes.length);
		System.arraycopy(K, 32, kmBytes, 0, kmBytes.length);
		System.out.println("Ke:" + ByteUtils.toHexString(keBytes));
		System.out.println("Km:" + ByteUtils.toHexString(kmBytes));

		System.out.println("Ke:" + ByteUtils.toHexString(Arrays.copyOfRange(K, 0, 32)));
		System.out.println("Km:" + ByteUtils.toHexString(Arrays.copyOfRange(K, 32, K.length)));

		System.out.println("4. 对消息进⾏行行加密，c=E(KE,m)");
		byte[] ivBytes = new byte[16];
		System.arraycopy(pyBytes, 0, ivBytes, 0, ivBytes.length);
		byte[] cBytes = null;
		try {
			cBytes = ECIESUtils.encodeAesCtr128(keBytes, msg.getBytes(), ivBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("c:" + ByteUtils.toHexString(cBytes));

		System.out.println("5. 计算加密信息的tag d，d=MAC(KM,c||S2)");
		byte[] blakeContent = new byte[kmBytes.length + cBytes.length + s2bytes.length];
		System.arraycopy(kmBytes, 0, blakeContent, 0, kmBytes.length);
		System.arraycopy(cBytes, 0, blakeContent, kmBytes.length, cBytes.length);
		System.arraycopy(s2bytes, 0, blakeContent, kmBytes.length + cBytes.length, s2bytes.length);
		byte[] dBytes = CryptoUtils.blake2b(blakeContent);
		System.out.println("d:" + ByteUtils.toHexString(dBytes));

		System.out.println("6. 返回结果 R||c||d");
		System.out.println(
				ByteUtils.toHexString(Rencoded) + ByteUtils.toHexString(cBytes) + ByteUtils.toHexString(dBytes));

		// 解密
		System.out.println("解密======================");
		System.out.println("1. 计算共享密钥，S=Px。 P=(Px,Py)=kb*R=kb*r*G=r*kb*G=r*KB");
		byte[] p_RBytes = BytesUtils.toBytesPadded(new BigInteger(1, Arrays.copyOfRange(Rencoded, 1, Rencoded.length)),
				64);
		byte[] RxBytes = new byte[32];
		byte[] RyBytes = new byte[32];
		System.arraycopy(p_RBytes, 0, RxBytes, 0, RxBytes.length);
		System.arraycopy(p_RBytes, RxBytes.length, RyBytes, 0, RyBytes.length);

		ECPoint R_client = ECKeyPair.CURVE.getCurve().createPoint(BytesUtils.bytesToBigInt(RxBytes),
				BytesUtils.bytesToBigInt(RyBytes));
		ECPoint P_client = new FixedPointCombMultiplier().multiply(R_client,
				BytesUtils.bytesToBigInt(BytesUtils.toByteArray(BobPrivateKey)));
		byte[] P_client_bytes = P_client.getEncoded(false);
		byte[] P_client_bytes_without_prefix = BytesUtils
				.toBytesPadded(new BigInteger(1, Arrays.copyOfRange(P_client_bytes, 1, P_client_bytes.length)), 64);
		byte[] px_client_bytes = new byte[32];
		byte[] py_client_bytes = new byte[32];
		System.arraycopy(P_client_bytes_without_prefix, 0, px_client_bytes, 0, px_client_bytes.length);
		System.arraycopy(P_client_bytes_without_prefix, 32, py_client_bytes, 0, py_client_bytes.length);
		System.out.println("Px:" + ByteUtils.toHexString(px_client_bytes));
		System.out.println("Py:" + ByteUtils.toHexString(py_client_bytes));

		System.out.println("2. 使⽤用KDF算法，⽣生成对称加密密码和MAC的密码:KE||KM = KDF(S||S1)");
		byte[] K_client = ECIESUtils.pbkdf2withsha512(px_client_bytes, BytesUtils.toByteArray(S1));
		byte[] ke_client_bytes = new byte[32];
		byte[] km_client_bytes = new byte[32];
		System.arraycopy(K_client, 0, ke_client_bytes, 0, ke_client_bytes.length);
		System.arraycopy(K_client, 32, km_client_bytes, 0, km_client_bytes.length);
		System.out.println("Ke:" + ByteUtils.toHexString(ke_client_bytes));
		System.out.println("Km:" + ByteUtils.toHexString(km_client_bytes));

		System.out.println("3. 使⽤用Mac计算Mac是否正确:MAC(KM||c||S2)");
		byte[] blake_client = new byte[km_client_bytes.length + cBytes.length + s2bytes.length];
		System.arraycopy(km_client_bytes, 0, blake_client, 0, km_client_bytes.length);
		System.arraycopy(cBytes, 0, blake_client, km_client_bytes.length, cBytes.length);
		System.arraycopy(s2bytes, 0, blake_client, km_client_bytes.length + cBytes.length, s2bytes.length);
		byte[] mac_bytes = CryptoUtils.blake2b(blake_client);
		System.out.println("calc d:" + ByteUtils.toHexString(mac_bytes));

		System.out.println("4. 解码原始加密⽂文件，解码密码为Ke，c为收到的加密⽂文件");
		byte[] iv_client_bytes = new byte[16];
		System.arraycopy(py_client_bytes, 0, iv_client_bytes, 0, iv_client_bytes.length);
		try {
			byte[] plain = ECIESUtils.decodeAesCtr128(ke_client_bytes, cBytes, iv_client_bytes);
			System.out.println("plain:" + new String(plain));

			Assert.assertEquals(msg, new String(plain));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testUtil() throws Exception {
		String msg = "{\n" + "\"sites\": [\n" + "{ \"name\":\"百度\" , \"url\":\"www.baidu.com\" }, \n"
				+ "{ \"name\":\"google\" , \"url\":\"www.google.com\" }, \n"
				+ "{ \"name\":\"微博\" , \"url\":\"www.weibo.com\" }\n" + "]\n" + "}";
		String receiverPublicKey = "048e8c9a162dec4a76dbbdd89391353ad399e7a6c8c2332ed04ca22f77f51f45f0af867b4e06a82cdfd819019ca335b33ac369ac3ccd3920a6befad2c7903f50d4";
		String receiverPrivateKey = "cab2002ddebea021ef9da251ea92198cf6bbeb6de34d834078783fbd86446334";
		String shareSecretKey = "1c6ce96caacedd14f7f9ee25bdb4b75fbd2db4df";
		String enString = ECIESUtils.encrypt(receiverPublicKey, shareSecretKey, msg);

		String receiverMsg = ECIESUtils.decrypt(receiverPrivateKey, shareSecretKey, enString);

		Assert.assertEquals(msg, receiverMsg);

	}

}
