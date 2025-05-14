package com.vechain.thorclient.utils.crypto;

import java.util.Arrays;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
import org.eclipse.jetty.io.RuntimeIOException;

public class PublicKeyECPoint {

	private byte[] x = new byte[32];
	private byte[] y = new byte[32];
	private ECPoint point;

	//TODO
	public PublicKeyECPoint(ECPoint point) {
		this.point = point;
		byte[] raw = point.getEncoded(false);
		if (raw.length != 65) {
			throw new RuntimeIOException("PublicKeyECPoint format error, must be 65 bytes");
		}
		byte[] p = Arrays.copyOfRange(raw, 1, 65);
		System.arraycopy(p, 0, x, 0, x.length);
		System.arraycopy(p, x.length, y, 0, y.length);
	}

	public byte[] getX() {
		return x;
	}

	public void setX(byte[] x) {
		this.x = x;
	}

	public byte[] getY() {
		return y;
	}

	public void setY(byte[] y) {
		this.y = y;
	}

	public ECPoint getPoint() {
		return point;
	}

	public void setPoint(ECPoint point) {
		this.point = point;
	}

	@Override
	public String toString() {
		return "[x=" + Hex.toHexString(x) + ", y=" + Hex.toHexString(y) + "]";
	}

}
