package com.vechain.thorclient.clients;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.Receipt;
import com.vechain.thorclient.core.model.blockchain.Transaction;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.*;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.RawTransactionFactory;
import com.vechain.thorclient.utils.crypto.ECKeyPair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.security.KeyPair;

@RunWith(JUnit4.class)
public class TransactionClientTest extends BaseTest {
    static String hexId = "0xa8488f297015797667adf92abd942c3016320710882e1a1dd867f6070455553f";
    @Test
    public void testGetTransaction() throws IOException {

        Transaction transaction = TransactionClient.getTransaction( hexId, false, null );
        logger.info( "Transaction:" + JSON.toJSONString( transaction ) );
        Assert.assertNotNull( transaction );
    }

    @Test
    public void testGetTransactionRaw() throws IOException {
        Transaction transaction = TransactionClient.getTransaction( hexId, true, null );
        logger.info( "Transaction:" + JSON.toJSONString( transaction ) );
        Assert.assertNotNull( transaction );
        Assert.assertNotNull( transaction.getRaw() );
    }

    @Test
    public void testGetTransactionReceipt() throws IOException{
        Receipt receipt = TransactionClient.getTransactionReceipt( hexId, null);
        logger.info( "Receipt:" + JSON.toJSONString( receipt ) );
        Assert.assertNotNull( receipt );
    }


    @Test
    public void testSendVTHOTransaction() throws IOException{
        byte chainTag = BlockchainClient.getChainTag();
        byte[] blockRef = BlockClient.getBlock( null ).blockRef().toByteArray();
        Amount amount = Amount.createFromToken( ERC20Token.VTHO );
        amount.setDecimalAmount( "11.12" );
        ToClause clause = ERC20Contract.buildTranferToClause( ERC20Token.VTHO,
                Address.fromHexString("VXc71ADC46c5891a8963Ea5A5eeAF578E0A2959779"),
                amount);
        RawTransaction rawTransaction =RawTransactionFactory.getInstance().createRawTransaction( chainTag, blockRef, 720, 80000, (byte)0x01, CryptoUtils.generateTxNonce(), clause);

        TransferResult result = TransactionClient.signThenTransfer( rawTransaction, ECKeyPair.create( "0xedbc62392034f159c248e453908475e7e4bee795459d33adf2f7e8047bb033c4" ) );
        logger.info( "transfer result:" + JSON.toJSONString( result ) );
    }




}
