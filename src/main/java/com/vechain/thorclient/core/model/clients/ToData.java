package com.vechain.thorclient.core.model.clients;

import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.StringUtils;

/**
 * ToData is contained by {@link ToClause}
 * If it is 0 value, use the {@link ToData#ZERO}.
 */
public class ToData {

    public static ToData ZERO = new Zero();

    private String hexString;
    public ToData(){

    }

    /**
     * Add hex string contract codes to to clause data.
     * @param hexString the hex string with "0x"
     */
    public void setData(String hexString){
        if(!StringUtils.isHex( hexString )){
            throw new IllegalArgumentException( "hex string is not valid" );
        }
        String noPrefixHex = StringUtils.sanitizeHex( hexString );
        if(noPrefixHex.length() <= 0){
            throw new IllegalArgumentException( "hex string is not valid" );
        }
        this.hexString = hexString;
    }

    /**
     * Convert to byte array.
     * @return byte array.
     */
    public byte[] toByteArray(){

        return BytesUtils.toByteArray( hexString );
    }

    static private class Zero extends ToData{
        public byte[] toByteArray(){
            return new byte[]{};
        }
        @Override
        public void setData(String hexString){
            throw new RuntimeException( "Not allowed to call" );
        }
    }



}
