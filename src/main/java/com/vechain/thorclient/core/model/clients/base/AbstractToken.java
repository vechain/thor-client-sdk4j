package com.vechain.thorclient.core.model.clients.base;

import java.math.BigDecimal;

/**
 * AbstractToken amount class.
 */
public class AbstractToken {

    public static final AbstractToken VET = new AbstractToken( "VET" );

    protected String name;
    protected BigDecimal precision;
    protected BigDecimal scale;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrecision() {
        return precision;
    }

    public void setPrecision(BigDecimal precision) {
        this.precision = precision;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }

    protected AbstractToken(String name){
        this.name = name;
        this.precision = BigDecimal.valueOf( 18 );
        this.scale = BigDecimal.valueOf( 8 );
    }

}
