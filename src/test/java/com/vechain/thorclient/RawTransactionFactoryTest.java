package com.vechain.thorclient;



import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.Clause;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.Prefix;
import com.vechain.thorclient.utils.RLPUtils;
import com.vechain.thorclient.utils.RawTransactionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

@RunWith(JUnit4.class)
public class RawTransactionFactoryTest extends BaseTest {

    /**
     * //ChainTag
     byte chainTag = (byte)0xab;
     int  n = chainTag & 0xFF;
     logger.info("Current chainTag:" + n);
     builder.update(Byte.valueOf(chainTag), "chainTag");

     //Expiration
     byte[] expirationBytes = BytesUtils.longToBytes(720);
     builder.update(expirationBytes, "expiration");

     //BlockRef
     byte[] blockRef = BytesUtils.trimLeadingZeroes(BytesUtils.toByteArray("0x0002049d16168115"));
     builder.update(blockRef, "blockRef");

     //Nonce
     byte[] nonce = BytesUtils.trimLeadingZeroes(BytesUtils.toByteArray("0x0002049d16068015"));
     builder.update(nonce , "nonce");

     //gas
     byte[] gas = BytesUtils.longToBytes(21000);
     builder.update(gas, "gas");

     builder.update((byte)0x01, "gasPriceCoef");
     //clause
     RawClause clauses[] = new RawClause[1];
     clauses[0] = new RawClause();
     clauses[0].setTo(BytesUtils.toByteArray("0x42191bd624aBffFb1b65e92F1E51EB16f4d2A3Ce"));
     clauses[0].setValue(BytesUtils.defaultDecimalStringToByteArray("42.42"));
     builder.update(clauses);
     */

    @Test
    public void testCreateRawTxn(){
        ArrayList<Clause> rawClauseList = new ArrayList<>();
        Clause clause = new Clause();
        clause.setTo("0x42191bd624aBffFb1b65e92F1E51EB16f4d2A3Ce");
        clause.setValue("42.42");
        rawClauseList.add(clause);
       RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction((byte)0xab, BytesUtils.trimLeadingZeroes( BytesUtils.toByteArray("0x0002049d16168115")), 720, 21000, (byte)0x01 , BytesUtils.toByteArray("0x0002049d16068015"), rawClauseList);
        byte[] rlpTransactionEncoded = RLPUtils.encodeRawTransaction(rawTransaction);

        String hexRLPTransaction = BytesUtils.toHexString(rlpTransactionEncoded, Prefix.ZeroLowerX);
        logger.info("RLP encoded:" + hexRLPTransaction);

        Assert.assertEquals("The RLP is not correct.","0xf83d81ab8702049d161681158202d0e1e09442191bd624abfffb1b65e92f1e51eb16f4d2a3ce89024cb21d3fcc1200008001825208808702049d16068015c0", hexRLPTransaction);
    }

}
