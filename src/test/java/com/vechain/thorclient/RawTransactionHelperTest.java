package com.vechain.thorclient;



import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.utils.crypto.ECKeyPair;
import com.vechain.thorclient.utils.crypto.ECDSASigning;
import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.utils.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RawTransactionHelperTest extends BaseTest {



    @Test
    public void testGenerateTransactionNonce(){
       byte[] nonce =  CryptoUtils.generateTxNonce();
       logger.info("Generated Nonce is :" + BytesUtils.toHexString(nonce, Prefix.ZeroLowerX));
       Assert.assertNotNull(nonce);
    }


    public RawTransaction createRawTransaction(){
        RawTransactionBuilder builder  = new RawTransactionBuilder();

        //ChainTag
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

        RawTransaction rawTxn = builder.build();

        return rawTxn;
    }



    @Test
    public void testRLPRawTransaction(){
        RawTransaction rawTransaction = createRawTransaction();
        byte[] rlpTransactionEncoded = RLPUtils.encodeRawTransaction(rawTransaction);

        String hexRLPTransaction = BytesUtils.toHexString(rlpTransactionEncoded, Prefix.ZeroLowerX);
        logger.info("RLP encoded:" + hexRLPTransaction);

        Assert.assertEquals("The RLP is not correct.","0xf83d81ab8702049d161681158202d0e1e09442191bd624abfffb1b65e92f1e51eb16f4d2a3ce89024cb21d3fcc1200008001825208808702049d16068015c0", hexRLPTransaction);
    }

    @Test
    public void testSignTransaction(){
        RawTransaction rawTransaction = createRawTransaction();
        byte[] rlpTransactionEncoded = RLPUtils.encodeRawTransaction(rawTransaction);
        byte[] rawTxHash = CryptoUtils.blake2b(rlpTransactionEncoded);
        ECKeyPair keyPair = ECKeyPair.create(BytesUtils.toByteArray("0xc8c53657e41a8d669349fc287f57457bd746cb1fcfc38cf94d235deb2cfca81b"));
        ECDSASigning.SignatureData signature = ECDSASigning.signMessage(rawTxHash, keyPair, false);

        logger.info("R:" + BytesUtils.toHexString(signature.getR(), Prefix.ZeroLowerX));
        logger.info("S:" + BytesUtils.toHexString(signature.getS(), Prefix.ZeroLowerX));
        logger.info("V:" + signature.getV());

        logger.info("Flat signature:" + BytesUtils.toHexString(signature.toByteArray(), Prefix.ZeroLowerX));
        rawTransaction.setSignature(signature.toByteArray());
        byte[] signRawTx = RLPUtils.encodeRawTransaction(rawTransaction);
        String signatureStr = BytesUtils.toHexString(signRawTx, Prefix.ZeroLowerX);
        logger.info("Sign Raw Tx:" + signatureStr);
        Assert.assertEquals( "Signature is not expected", signatureStr, "0xf88081ab8702049d161681158202d0e1e09442191bd624abfffb1b65e92f1e51eb16f4d2a3ce89024cb21d3fcc1200008001825208808702049d16068015c0b8411d4d117761e2d2d5cea0b4b659470d349b593ce13ec13d0672b9504d0710896e4135eb964837607d4a5d70cd93ac0a0fe4c0f19831f303981b3703a3700e894400" );

    }

}
