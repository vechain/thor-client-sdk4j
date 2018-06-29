package com.vechain.thorclient.clients;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.Receipt;
import com.vechain.thorclient.core.model.blockchain.Transaction;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.ERC20Contract;
import com.vechain.thorclient.core.model.clients.ERC20Token;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.core.model.clients.Revision;
import com.vechain.thorclient.core.model.clients.ToClause;
import com.vechain.thorclient.core.model.clients.ToData;
import com.vechain.thorclient.core.model.clients.base.AbstractToken;
import com.vechain.thorclient.core.model.exception.ClientIOException;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.Prefix;
import com.vechain.thorclient.utils.RawTransactionFactory;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

@RunWith(JUnit4.class)
public class TransactionClientTest extends BaseTest {

    static String hexId             = "0x19dd77d28ef70be8c924319a6c08b996dd456fa36f29f2427dbda90087a8a897";
    static String addUserTxId       = "0x652b5c0f68d03fed86625969ad38e0634993f8da950126518b0c02e6e630d3de";
    static String removeUserTxId    = "0x3bec812d64615584414595e050bb52be9c0807cb1c05dc2ea9286a1e7c6a4da0";
    static String setUserPlanTxId   = "0x9dbdd7dc102eafe882f9e084ca01671ae8eebe59751ffcfbd1abfeb5cb687846";
    static String addSponsorTxId    = "0x010fed13ca1d699674529a0c8621fe1ac61dfdf2f7a6d6fce77fbf7cbb77e092";
    static String selectSponsorTxId = "0x9cc90d37cf088b63b8180ab7978b673822a36000c39b5ce38da525e2a17ea5f0";
    static String unsponsorTxId     = "0xe86d6b5546e12741ce894ba25d5c3ed0a16e700ed93e18c6050451592b3f2b8c";

    @Test
    public void testGetTransaction() throws ClientIOException {

        Transaction transaction = TransactionClient.getTransaction(hexId, false, null);
        logger.info("Transaction:" + JSON.toJSONString(transaction));
        Assert.assertNotNull(transaction);
    }

    @Test
    public void testGetTransactionRaw() throws ClientIOException {
        Transaction transaction = TransactionClient.getTransaction(hexId, true, null);
        logger.info("Transaction:" + JSON.toJSONString(transaction));
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getRaw());
    }

    @Test
    public void testGetTransactionReceipt() throws ClientIOException {
        Receipt receipt = TransactionClient.getTransactionReceipt(hexId, null);
        logger.info("Receipt:" + JSON.toJSONString(receipt));
        Assert.assertNotNull(receipt);
    }

    @Test
    public void testSendVTHOTransaction() throws ClientIOException {
        byte chainTag = BlockchainClient.getChainTag();
        byte[] blockRef = BlockClient.getBlock(null).blockRef().toByteArray();
        Amount amount = Amount.createFromToken(ERC20Token.VTHO);
        amount.setDecimalAmount("60000");
        ToClause clause = ERC20Contract.buildTranferToClause(ERC20Token.VTHO, Address.fromHexString("0x5034aa590125b64023a0262112b98d72e3c8e40e"), amount);
        RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(chainTag, blockRef, 720, 80000, (byte) 0x0, CryptoUtils.generateTxNonce(), clause);
        logger.info("Raw:" + BytesUtils.toHexString(rawTransaction.encode(), Prefix.ZeroLowerX));
        TransferResult result = TransactionClient.signThenTransfer(rawTransaction, ECKeyPair.create(privateKey));
        logger.info("transfer vethor result:" + JSON.toJSONString(result));
        Assert.assertNotNull(result);
    }

    @Test
    public void testSendVETTransaction() throws ClientIOException {
        byte chainTag = BlockchainClient.getChainTag();
        byte[] blockRef = BlockchainClient.getBlockRef(Revision.BEST).toByteArray();
        Amount amount = Amount.createFromToken(AbstractToken.VET);
        amount.setDecimalAmount("1998990000");
        ToClause clause = TransactionClient.buildVETToClause(Address.fromHexString("0x5034aa590125b64023a0262112b98d72e3c8e40e"), amount, ToData.ZERO);
        RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(chainTag, blockRef, 720, 21000, (byte) 0x0, CryptoUtils.generateTxNonce(), clause);
        logger.info("Raw:" + BytesUtils.toHexString(rawTransaction.encode(), Prefix.ZeroLowerX));
        TransferResult result = TransactionClient.signThenTransfer(rawTransaction, ECKeyPair.create(privateKey));
        logger.info("transfer vet result:" + JSON.toJSONString(result));
        Assert.assertNotNull(result);
    }

}
