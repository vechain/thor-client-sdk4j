package com.vechain.thorclient.core.model.clients;


import com.vechain.thorclient.utils.BytesUtils;

import java.math.BigInteger;
import java.util.ArrayList;

public class TransactionReserved {
    private ArrayList<byte[]> reservedValues = new ArrayList<>();

    public boolean isDelegationFeature() {
        byte[] feature0 = reservedValues.get( 0 );
        byte[] delegation = BigInteger.ONE.toByteArray();
        int len = Math.min( feature0.length, delegation.length );
        for(int index = 0; index < len; index ++){
            byte feature0Byte = feature0[index];
            byte delegationByte = delegation[index];
            if((feature0Byte&delegationByte) > 0){
               return true;
            }
        }
        return false;
    }

    public void setDelegationFeature(boolean isDelegationOn) {
        BigInteger feature0 = BigInteger.ZERO;
        if(isDelegationOn){
            feature0 = feature0.add(BigInteger.ONE);
            reservedValues.add( BytesUtils.trimLeadingZeroes(feature0.toByteArray()) );
        }
    }

    public ArrayList<byte[]> getReservedValues() {
        return reservedValues;
    }
}
