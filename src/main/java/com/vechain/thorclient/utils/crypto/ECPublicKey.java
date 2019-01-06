package com.vechain.thorclient.utils.crypto;


import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.Prefix;
import org.bouncycastle.util.Arrays;

import javax.naming.OperationNotSupportedException;
import java.math.BigInteger;

public class ECPublicKey extends ECKey {
    private byte[] pub;
    private boolean isCompressed;

    public ECPublicKey(byte[] pub, boolean compressed) {
        this.pub = pub;
        this.isCompressed = compressed;
    }

    public ECPublicKey(BigInteger pubKeyInteger, boolean compressed){
        this.pub = BytesUtils.toBytesPadded(pubKeyInteger, 65 );
        this.isCompressed = compressed;
    }


    @Override
    public byte[] getRawPrivateKey(){
       return null;
    }

    @Override
    public byte[] getRawAddress() {
        byte[] publicPointBytes = BytesUtils.toBytesPadded( this.getPublicKey(), 64 );
        byte[] hash = CryptoUtils.keccak256(publicPointBytes);
        byte[] address = new byte[20];
        System.arraycopy(hash, 12, address, 0, address.length);
        return address;
    }

    @Override
    public byte[] getRawPublicKey(boolean isCompressed) {
        return pointBytesToPublicKey(this.pub, isCompressed );

    }


    @Override
    public BigInteger getPrivateKey() {
        throw new RuntimeException("Please use private key to sign signature");
    }

    @Override
    public BigInteger getPublicKey() {
        byte[] encoded = pointBytesToPublicKey( this.pub, false );
        return new BigInteger(1, java.util.Arrays.copyOfRange(encoded, 1, encoded.length));
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
        c.pub = Arrays.clone(pub);
        return c;
    }

    @Override
    public <T> T sign(byte[] messageHash) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Please use private key to sign signature");
    }
}
