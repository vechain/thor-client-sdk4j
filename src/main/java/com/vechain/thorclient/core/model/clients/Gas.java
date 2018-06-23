package com.vechain.thorclient.core.model.clients;

public class Gas {
    private static final int LIMIT = 21000;
    private int gas;
    private byte gasCoef;

    public static final Gas  DefaultGas = createGas( LIMIT );

    public static Gas createGas(int gas ){
        Gas g = new Gas(gas);
        return g;
    }

    public Gas(int gas){
        if(gas < LIMIT){
            throw new IllegalArgumentException( "Gas is too small" );
        }
        this.gas = gas;
        this.gasCoef = 0x01;
    }

    public int getGas() {
        return gas;
    }

    public byte getGasCoef() {
        return gasCoef;
    }

    public void setGasCoef(byte gasCoef) {
        this.gasCoef = gasCoef;
    }
}
