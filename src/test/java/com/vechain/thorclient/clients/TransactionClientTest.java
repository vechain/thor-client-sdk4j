package com.vechain.thorclient.clients;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.Receipt;
import com.vechain.thorclient.core.model.blockchain.Transaction;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.*;
import com.vechain.thorclient.core.model.clients.base.AbstractToken;
import com.vechain.thorclient.core.model.exception.ClientIOException;
import com.vechain.thorclient.utils.*;
import com.vechain.thorclient.utils.crypto.ECKeyPair;
import com.vechain.thorclient.utils.crypto.Key;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TransactionClientTest extends BaseTest {

    final boolean prettyFormat = isPretty();

    // Galactica documented at: http://localhost:8669/doc/stoplight-ui/#/paths/transactions-id/get
    // Solo tested.
    // GET http://localhost:8669/transactions/0xda74337f4c5ab50dbd34624df2de7a1f5f6ebe9408aa511632e43af6a0be5f07
    // Accept: application/json, text/plain
    @Test
    public void testGetTransaction() throws ClientIOException {
        // The first transaction of block 1
        final String txId = System.getProperty("TransactionClientTest.testGetTransaction.txId");
        final Transaction transaction = TransactionClient.getTransaction(txId, false, null);
        logger.info("Transaction WithOut Raw: " + JSON.toJSONString(transaction, prettyFormat));
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getMeta());
        Assert.assertNull(transaction.getRaw());
    }

    // Galactica documented at: http://localhost:8669/doc/stoplight-ui/#/paths/transactions-id/get
    // Solo tested.
    // GET http://localhost:8669/transactions/0xda74337f4c5ab50dbd34624df2de7a1f5f6ebe9408aa511632e43af6a0be5f07?raw=true
    // Accept: application/json, text/plain
    @Test
    public void testGetTransactionRaw() throws ClientIOException {
        final String txId = System.getProperty("TransactionClientTest.testGetTransactionRaw.txId");
        final Transaction transaction = TransactionClient.getTransaction(txId, true, null);
        logger.info("Transaction With Raw: " + JSON.toJSONString(transaction, prettyFormat));
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getRaw());
        Assert.assertNotNull(transaction.getMeta());
    }

    // Galactica documented at: http://localhost:8669/doc/stoplight-ui/#/paths/transactions/post
    // Solo tested.
    @Test
    public void testDeployContract() throws ClientIOException {
        final String privateKey = System.getProperty("TransactionClientTest.testDeployContract.privateKey");
        final String contractHex = System.getProperty("TransactionClientTest.testDeployContract.contractHex");
        final TransferResult result = TransactionClient.deployContract(
                contractHex,
                9000000,
                (byte) 0,
                720,
                ECKeyPair.create(privateKey)
        );
        logger.info("Deploy contract result: " + JSON.toJSONString(result, prettyFormat));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            final String message = String.format("InterruptedException: %s", e.getMessage());
            logger.error(message);
            Assert.fail(message);
        }
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId());
    }


    // Galactica documented at: http://localhost:8669/doc/stoplight-ui/#/paths/transactions-id--receipt/get
    // Solo tested.
    // GET http://localhost:8669/transactions/0xda74337f4c5ab50dbd34624df2de7a1f5f6ebe9408aa511632e43af6a0be5f07/receipt
    // Accept: application/json, text/plain
    @Test
    public void testGetTransactionReceipt() throws ClientIOException {
        final String txId = System.getProperty("TransactionClientTest.testGetTransactionReceipt.txId");
        final Receipt receipt = TransactionClient.getTransactionReceipt(txId, null);
        logger.info("Receipt:" + JSON.toJSONString(receipt, prettyFormat));
        Assert.assertNotNull(receipt);
        Assert.assertNotNull(receipt.getMeta());
    }

    // Galactica documented at http://localhost:8669/doc/stoplight-ui/#/paths/transactions/post.
    // Solo tested.
    @Test
    public void testSendVTHOTransaction() throws ClientIOException {
        final String fromPrivateKey = System.getProperty("TransactionClientTest.testSendVTHOTransaction.fromPrivateKey");
        final String toAddress = System.getProperty("TransactionClientTest.testSendVTHOTransaction.toAddress");
        final byte chainTag = BlockchainClient.getChainTag();
        final byte[] blockRef = BlockClient.getBlock(null).blockRef().toByteArray();
        final Amount amount = Amount.createFromToken(ERC20Token.VTHO);
        amount.setDecimalAmount("10000");
        final ToClause clause = ERC20Contract.buildTranferToClause(
                ERC20Token.VTHO,
                Address.fromHexString(toAddress), amount
        );
        final RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(
                chainTag,
                blockRef,
                720,
                80000,
                (byte) 0x0,
                CryptoUtils.generateTxNonce(),
                clause
        );
        logger.info("SendVTHO Raw:" + BytesUtils.toHexString(rawTransaction.encode(), Prefix.ZeroLowerX));
        final TransferResult result = TransactionClient.signThenTransfer(
                rawTransaction,
                ECKeyPair.create(fromPrivateKey)
        );
        logger.info("SendVTHO Result:" + JSON.toJSONString(result, prettyFormat));
        Assert.assertNotNull(result);
        final String txIdHex = BlockchainUtils.generateTransactionId(
                rawTransaction,
                Address.fromHexString(ECKeyPair.create(fromPrivateKey).getHexAddress())
        );
        logger.info("Calculate transaction txid: " + txIdHex);
        Assert.assertEquals(txIdHex, result.getId());
    }

    // Galactica documented at http://localhost:8669/doc/stoplight-ui/#/paths/transactions/post.
    // Solo tested.
    @Test
    public void testSendRemarkTx() throws ClientIOException {
        final String fromPrivateKey = System.getProperty("TransactionClientTest.testSendRemarkTx.fromPrivateKey");
        final String toAddress = System.getProperty("TransactionClientTest.testSendRemarkTx.toAddress");
        final byte chainTag = BlockchainClient.getChainTag();
        final byte[] blockRef = BlockchainClient.getBlockRef(Revision.BEST).toByteArray();
        final ToData toData = new ToData();
        final int size = 47 * 1000;
        final byte[] k64 = new byte[size];
        for (int i = 0; i < size; i++) {
            k64[i] = (byte) 0xff;
        }
        toData.setData(BytesUtils.toHexString(k64, Prefix.ZeroLowerX));
        final ToClause clause = TransactionClient.buildVETToClause(
                Address.fromHexString(toAddress),
                Amount.ZERO,
                toData
        );
        final RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(
                chainTag,
                blockRef,
                720,
                4000000,
                (byte) 0x0,
                CryptoUtils.generateTxNonce(),
                clause
        );
        logger.info("SendVET Raw:" + BytesUtils.toHexString(rawTransaction.encode(), Prefix.ZeroLowerX));
        logger.info("SignHash Raw:" + BytesUtils.toHexString(CryptoUtils.blake2b(rawTransaction.encode()), Prefix.ZeroLowerX));
        TransferResult result = TransactionClient.signThenTransfer(rawTransaction, ECKeyPair.create(fromPrivateKey));
        logger.info("SendVET result:" + JSON.toJSONString(result), prettyFormat);
        Assert.assertNotNull(result);
        final String fromAddress = ECKeyPair.create(fromPrivateKey).getHexAddress();
        final String txIdHex = BlockchainUtils.generateTransactionId(rawTransaction, Address.fromHexString(fromAddress));
        logger.info("Calculate transaction txid: " + txIdHex);
        Assert.assertEquals(txIdHex, result.getId());
    }

    // Galactica documented at http://localhost:8669/doc/stoplight-ui/#/paths/transactions/post.
    // Solo tested.
    @Test
    public void testSendVETTransaction() throws ClientIOException {
        // pre-seeded galactica solo account[8]
        final String fromPrivateKey = "521b7793c6eb27d137b617627c6b85d57c0aa303380e9ca4e30a30302fbc6676";
        // pre-seeded galactica solo account[9]
        final Address toAddress = Address.fromHexString("0x3e3d79163b08502a086213cd09660721740443d7");
        final byte chainTag = BlockchainClient.getChainTag();
        final byte[] blockRef = BlockchainClient.getBlockRef(Revision.BEST).toByteArray();
        final Amount amount = Amount.createFromToken(AbstractToken.VET);
        amount.setDecimalAmount("100");
        final ToClause clause = TransactionClient.buildVETToClause(toAddress, amount, ToData.ZERO);
        final RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(
                chainTag,
                blockRef,
                720,
                21000,
                (byte) 0x0,
                CryptoUtils.generateTxNonce(),
                clause
        );
        logger.info("SendVET Raw:" + BytesUtils.toHexString(rawTransaction.encode(), Prefix.ZeroLowerX));
        logger.info("SignHash Raw:" + BytesUtils.toHexString(CryptoUtils.blake2b(rawTransaction.encode()), Prefix.ZeroLowerX));
        final TransferResult result = TransactionClient.signThenTransfer(rawTransaction, ECKeyPair.create(fromPrivateKey));
        logger.info("SendVET result:" + JSON.toJSONString(result, prettyFormat));
        Assert.assertNotNull(result);
        final String hexAddress = ECKeyPair.create(fromPrivateKey).getHexAddress();
        final String txIdHex = BlockchainUtils.generateTransactionId(rawTransaction, Address.fromHexString(hexAddress));
        logger.info("Calculate transaction txid: " + txIdHex);
        Assert.assertEquals(txIdHex, result.getId());
    }

    private static RawTransaction generatingVETRawTxn(
            final String toAddress,
            final String decimalAmount
    ) {
        final byte chainTag = BlockchainClient.getChainTag();
        final byte[] blockRef = BlockchainClient.getBlockRef(Revision.BEST).toByteArray();
        final Amount amount = Amount.createFromToken(AbstractToken.VET);
        amount.setDecimalAmount(decimalAmount);
        final ToClause clause = TransactionClient.buildVETToClause(
                Address.fromHexString(toAddress),
                amount,
                ToData.ZERO
        );
        return RawTransactionFactory.getInstance().createRawTransaction(
                chainTag,
                blockRef,
                720,
                31000,
                (byte) 0x0,
                CryptoUtils.generateTxNonce(),
                clause
        );
    }

    private static RawTransaction generatingVTHORawTxn(
            final String toAddress,
            final String decimalAmount
    ) {
        final byte chainTag = BlockchainClient.getChainTag();
        final byte[] blockRef = BlockClient.getBlock(null).blockRef().toByteArray();
        final Amount amount = Amount.createFromToken(ERC20Token.VTHO);
        amount.setDecimalAmount(decimalAmount);
        final ToClause clause = ERC20Contract.buildTranferToClause(
                ERC20Token.VTHO,
                Address.fromHexString(toAddress),
                amount
        );
        final RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(
                chainTag,
                blockRef,
                720,
                80000,
                (byte) 0x0,
                CryptoUtils.generateTxNonce(),
                clause
        );
        return rawTransaction;
    }

    private String rlpEncodedRawTxHex(RawTransaction rawTransaction) throws ClientIOException {
        byte[] rawTxBytes = RLPUtils.encodeRawTransaction(rawTransaction);
        return BytesUtils.toHexString(rawTxBytes, Prefix.ZeroLowerX);
    }

    // Galactica documented at http://localhost:8669/doc/stoplight-ui/#/paths/transactions/post.
    // Solo tested.
    @Test
    public void testRecoverAddressAndCalcTxId_VET() throws ClientIOException {
        // pre-seeded solo account[3]
        final String fromPrivateKey = "432f38bcf338c374523e83fdb2ebe1030aba63c7f1e81f7d76c5f53f4d42e766";
        // pre-seeded solo account[4]
        final String toAddress = "0xf02f557c753edf5fcdcbfe4c1c3a448b3cc84d54";
        final RawTransaction rawTransaction = generatingVETRawTxn(toAddress, "100");
        final RawTransaction signedRawTxn = TransactionClient.sign(rawTransaction, ECKeyPair.create(fromPrivateKey));
        final String rawTxHex = rlpEncodedRawTxHex(signedRawTxn);
        logger.info("Tx raw hex: " + rawTxHex);
        final Key publicKey = BlockchainUtils.recoverPublicKey(rawTxHex);
        Assert.assertNotNull(publicKey);
        final String fromAddress = publicKey.getAddress();
        final RawTransaction newRawTransaction = RLPUtils.decode(rawTxHex);
        newRawTransaction.setSignature(null);
        final String txIdHex = BlockchainUtils.generateTransactionId(
                newRawTransaction,
                Address.fromHexString(fromAddress)
        );
        TransferResult transferResult = TransactionClient.transfer(rawTxHex);
        logger.info("Calculate transaction TxId: " + txIdHex);
        logger.info("SendVET result: " + JSON.toJSONString(transferResult, prettyFormat));
        Assert.assertNotNull(transferResult);
        Assert.assertEquals(txIdHex, transferResult.getId());
    }

    // Galactica documented at http://localhost:8669/doc/stoplight-ui/#/paths/transactions/post.
    // Solo tested.
    @Test
    public void testRecoverAddressAndCalcTxId_VTHO() throws ClientIOException {
        // pre-seeded solo account[3]
        final String fromPrivateKey = "432f38bcf338c374523e83fdb2ebe1030aba63c7f1e81f7d76c5f53f4d42e766";
        // pre-seeded solo account[4]
        final String toAddress = "0xf02f557c753edf5fcdcbfe4c1c3a448b3cc84d54";
        final RawTransaction rawTransaction = generatingVTHORawTxn(toAddress, "10000");
        final RawTransaction signedRawTxn = TransactionClient.sign(rawTransaction, ECKeyPair.create(fromPrivateKey));
        final String rawTxHex = rlpEncodedRawTxHex(signedRawTxn);
        logger.info("Tx raw hex: " + rawTxHex);
        final Key publicKey = BlockchainUtils.recoverPublicKey(rawTxHex);
        Assert.assertNotNull(publicKey);
        final String fromAddress = publicKey.getHexAddress();
        final RawTransaction newRawTransaction = RLPUtils.decode(rawTxHex);
        newRawTransaction.setSignature(null);
        final String txIdHex = BlockchainUtils.generateTransactionId(
                newRawTransaction,
                Address.fromHexString(fromAddress)
        );
        final TransferResult transferResult = TransactionClient.transfer(rawTxHex);
        logger.info("Calculate transaction TxId: " + txIdHex);
        logger.info("Send Vethor result:" + JSON.toJSONString(transferResult, prettyFormat));
        Assert.assertNotNull(transferResult);
        Assert.assertEquals(txIdHex, transferResult.getId());

    }

    // Galactica documented at http://localhost:8669/doc/stoplight-ui/#/paths/transactions/post.
    // Solo tested.
    @Test
    public void testDelegatorSignAndTransfer() throws ClientIOException {
        // pre-seed solo account[0]
        final String gasPayerPrivateKey = "7f9290cc44c5fd2b95fe21d6ad6fe5fa9c177e1cd6f3b4c96a97b13e09eaa158";
        // pre-seed solo account[1]
        final String senderPrivateKey = "ea5383ac1f9e625220039a4afac6a7f868bf1ad4f48ce3a1dd78bd214ee4ace5";
        // pre-seed solo account[2]
        final String toAddress = "0x9e7911de289c3c856ce7f421034f66b6cde49c39";
        final RawTransaction rawTransaction = generatingVETRawTxn(toAddress, "100");
        final TransactionReserved reserved = new TransactionReserved();
        reserved.setDelegationFeature(true);
        rawTransaction.setReserved(reserved);
        final RawTransaction senderSignRawTransaction = TransactionClient.sign(
                rawTransaction,
                ECKeyPair.create(senderPrivateKey));
        final RawTransaction gasPayerSignRawTransaction = TransactionClient.delegatorSign(
                senderSignRawTransaction,
                ECKeyPair.create(gasPayerPrivateKey));
        final TransferResult transferResult = TransactionClient.transfer(gasPayerSignRawTransaction);
        final String txIdHex = BlockchainUtils.generateTransactionId(
                rawTransaction,
                Address.fromHexString(ECKeyPair.create(senderPrivateKey).getAddress()));
        logger.info("Send delegator pay gas Transaction transferResult: " + JSON.toJSONString(transferResult, prettyFormat));
        logger.info("Calc txId: " + txIdHex);
        Assert.assertNotNull(transferResult);
        Assert.assertEquals(txIdHex, transferResult.getId());
    }
}
