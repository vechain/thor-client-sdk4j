package com.vechain.thorclient.utils.crypto;


import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.Prefix;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.Arrays;

import javax.naming.OperationNotSupportedException;
import java.math.BigInteger;

public class ECPublicKey extends ECKey {
    private byte[] decompressedPub;

    public ECPublicKey(byte[] pub) {
        byte[] encoded = pointBytesToPublicKey( pub,  false);
        this.decompressedPub = encoded;

    }

    public ECPublicKey(BigInteger pubKeyInteger){
        byte[] publicKeyPoints =  BytesUtils.toBytesPadded( pubKeyInteger, PUBLIC_KEY_POINT_SIZE );
        byte[] publicKeyBytes = new byte[65];
        publicKeyBytes[0]= 0x04;
        System.arraycopy(publicKeyPoints, 0, publicKeyBytes, 1, 64);
        this.decompressedPub = publicKeyBytes;
    }


    @Override
    public byte[] getRawPrivateKey(){
       return null;
    }

    @Override
    public byte[] getRawAddress() {

        byte[] pointBytes = new byte[64];
        System.arraycopy( this.decompressedPub, 1, pointBytes, 0, 64);
        byte[] hash = CryptoUtils.keccak256(pointBytes);
        byte[] address = new byte[20];
        System.arraycopy(hash, 12, address, 0, address.length);
        return address;
    }

    @Override
    public byte[] getRawPublicKey(boolean isCompressed) {
         ECPoint pubPoint = CURVE.getCurve().decodePoint( this.decompressedPub );
         return pubPoint.getEncoded( isCompressed );

    }
    
    @Override
    public BigInteger getPrivateKey() {
        throw new RuntimeException("Please use private key to sign signature");
    }

    @Override
    public BigInteger getPublicKey() {
        byte[] pointBytes = new byte[64];
        System.arraycopy( this.decompressedPub, 1, pointBytes, 0, 64);
        return BytesUtils.bytesToBigInt( pointBytes );
    }

    @Override
    public String getAddress() {
       return BytesUtils.toHexString( this.getRawAddress(), Prefix.ZeroLowerX );
    }

    @Override
    public String getHexAddress() {
        return this.getAddress();
    }


    @Override
    public ECPublicKey clone() throws CloneNotSupportedException {
        ECPublicKey c = (ECPublicKey) super.clone();
        c.decompressedPub = Arrays.clone( decompressedPub );
        return c;
    }

    @Override
    public <T> T sign(byte[] messageHash) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Please use private key to sign signature");
    }
}
