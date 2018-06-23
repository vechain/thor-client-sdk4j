package com.vechain.thorclient;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.utils.crypto.ECKeyPair;
import com.vechain.thorclient.core.model.blockchain.*;
import com.vechain.thorclient.utils.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class BlockchainAPITest extends BaseTest {


	@Test
	public void testGetBestBlock() throws IOException {
		Block block = blockchainAPI.getBestBlock();
		logger.info("BestBlock Info  " + JSON.toJSONString( block ));
		Assert.assertNotNull("BEST BLOCK IS NULL", block);
	}

	@Test
	public void testSendThorTransaction() throws IOException {

//		RawTransactionFactory transactionFactory = RawTransactionFactory.getInstance();
//		String contractAddress = "0x0000000000000000000000000000456e65726779";
//		List<Clause> clauses = new ArrayList<Clause>();
//		clauses.add(new Clause("0x42191bd624aBffFb1b65e92F1E51EB16f4d2A3Ce", Integer.toHexString(1200000), ""));
//		byte gasPriceCoef = 0x01;
//		int expiration = 720;
//		int gas = 80000;
//		RawTransaction rawTransaction = transactionFactory.createRawTransaction( blockchainAPI, expiration, gas, gasPriceCoef, clauses, contractAddress);
//		ECKeyPair keyPair = ECKeyPair.create( FromPrivKey );
//		String txId = blockchainAPI.signAndSendRawTransaction(rawTransaction, keyPair);
//		logger.info("Response id:" + txId);
//        Assert.assertNotNull( "Transaction id is null" , txId);

	}

//	@Test
//=======
//    @Test
//    public void testGetBestBlock() throws IOException {
//        Block block = blockchainAPI.getBestBlock();
//        logger.info("BestBlock Info  " + JSON.toJSONString(block));
//        Assert.assertNotNull("BEST BLOCK IS NULL", block);
//    }
//
//    @Test
//    public void testSendThorTransaction() throws IOException {
//
//        RawTransactionFactory transactionFactory = RawTransactionFactory.getInstance();
//        List<Clause> clauses = new ArrayList<Clause>();
//        clauses.add(new Clause("0x42191bd624aBffFb1b65e92F1E51EB16f4d2A3Ce", Integer.toHexString(1200000), ""));
//        byte gasPriceCoef = 0x01;
//        int expiration = 720;
//        int gas = 80000;
//        RawTransaction rawTransaction = transactionFactory.createRawTransaction(blockchainAPI, expiration, gas, gasPriceCoef, clauses,
//                this.getEnvironment().get(VTHO_TOKEN_ADDRESS));
//        ECKeyPair keyPair = ECKeyPair.create(privateKey);
//        String txId = blockchainAPI.signAndSendRawTransaction(rawTransaction, keyPair);
//        logger.info("Response id:" + txId);
//        Assert.assertNotNull("Transaction id is null", txId);
//
//    }
//
//    @Test
//>>>>>>> 462d8a3c4504f09ffd68a74077e5d30316d5cf76
//    public void testGetBlockByNumber() throws IOException {
//        Block block = blockchainAPI.getBlockByNumber(4);
//        logger.info("Getting Block Info  " + JSON.toJSONString(block));
//        Assert.assertNotNull("BLOCK IS NULL", block);
//    }
//
//    @Test
//    public void testGetBlockByid() throws IOException {
//        Block block = blockchainAPI.getBlockById("0x000003e8360fc802fec45e3a4386cbf55fbd3bd50bcc50808f84f635fb744f08");
//        logger.info("Getting Block Info  " + JSON.toJSONString(block));
//        Assert.assertNotNull("BLOCK IS NULL", block);
//    }
//
//    @Test
//    public void testGetBalance() throws IOException {
//        Account account = blockchainAPI.getBalance(fromAddress, "best");
//
//<<<<<<< HEAD
//        logger.info("Current block account:" +JSON.toJSONString( account ));
//        logger.info("amount vet: " + BlockchainUtils.amount(account.getBalance(), 18, 2).toString());
//		logger.info("amount energe: " + BlockchainUtils.amount(account.getEnergy(), 18, 2).toString());
//=======
//        logger.info("Current block account:" + JSON.toJSONString(account));
//        logger.info("balance vet: " + BlockchainUtils.balance(account.getBalance(), 18, 2).toString());
//        logger.info("balance energe: " + BlockchainUtils.balance(account.getEnergy(), 18, 2).toString());
//>>>>>>> 462d8a3c4504f09ffd68a74077e5d30316d5cf76
//        Assert.assertNotNull("Account is null", account);
//    }
//
//    @Test
//    public void testGetStorageAt() throws IOException {
//
//        String contractAddress = "0x0000000000000000000000000000456e65726779";
//        String postion = "0x0000000000000000000000000000000000000000000000000000000000000004";
//        String addr = "000000000000000000000000" + StringUtils.sanitizeHex(fromAddress);
//        String pos = StringUtils.sanitizeHex(postion);
//        String key = BytesUtils.toHexString(CryptoUtils.keccak256(BytesUtils.toByteArray(addr + pos)), Prefix.ZeroLowerX);
//        StorageData data = blockchainAPI.getStorageAt(contractAddress, key, "best");
//        logger.info("Storage:" + JSON.toJSONString(data));
//    }
//
//    @Test
//    public void testContractCall() throws IOException {
//        String contractAddress = "0x0000000000000000000000000000456e65726779";
//
//        String addr = "000000000000000000000000" + StringUtils.sanitizeHex(fromAddress);
//        String data = "0x" + TransactionAttributes.ERC20ContractMethod.BALANCEOF.getId() + addr;
//        ContractCall call = new ContractCall();
//        call.setData(data);
//        call.setValue("0x0");
//
//        ContractCallResult callResult = blockchainAPI.callContract(contractAddress, "best", call);
//        logger.info("Call result:" + JSON.toJSONString(callResult));
//    }
//
//    @Test
//    public void testGetReceipt() throws IOException {
//        String txId = "0x6c841dcb71d8ccb6fcf347e03fdf7b115038d9fe367b9e8597a33408113783cb";
//        Receipt receipt = blockchainAPI.getTransactionReceipt(txId);
//        logger.info("Receipt: " + JSON.toJSONString(receipt));
//        Assert.assertNotNull("Receipt is null", receipt);
//    }
//
//    @Test
//    public void testGetBlockRef() throws IOException {
//        byte[] blockRef = blockchainAPI.getBestBlockRef();
//        logger.info("Block reference: " + BytesUtils.toHexString(blockRef, Prefix.ZeroLowerX));
//        Assert.assertNotNull("The best block reference is null. ", blockRef);
//    }
//
//    @Test
//    public void testSendTransactionVET() throws IOException {
//<<<<<<< HEAD
////	    byte[] blockRef = blockchainAPI.getBestBlockRef();
////	    ArrayList<Clause> clausesList = new ArrayList<>();
////        Clause clause = new Clause();
////        clause.setTo("0x42191bd624aBffFb1b65e92F1E51EB16f4d2A3Ce");
////        clause.setValue("11.11");
////        clausesList.add(clause);
////
////        RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction((byte)0xab,blockRef,720, 21000, (byte)1, CryptoUtils.generateTxNonce(), clausesList);
////
////        ECKeyPair keyPair = ECKeyPair.create("0xc8c53657e41a8d669349fc287f57457bd746cb1fcfc38cf94d235deb2cfca81b");
////        String txId = blockchainAPI.signAndSendRawTransaction(rawTransaction, keyPair);
////        logger.info("Response id:" + txId);
////        Assert.assertNotNull( "transaction id is null", txId );
//=======
//        byte[] blockRef = blockchainAPI.getBestBlockRef();
//        ArrayList<Clause> clausesList = new ArrayList<>();
//        Clause clause = new Clause();
//        clause.setTo("0x42191bd624aBffFb1b65e92F1E51EB16f4d2A3Ce");
//        clause.setValue("11.11");
//        clausesList.add(clause);
//
//        RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction((byte) 0xab, blockRef, 720, 21000, (byte) 1, CryptoUtils.generateTxNonce(),
//                clausesList);
//
//        ECKeyPair keyPair = ECKeyPair.create("0xc8c53657e41a8d669349fc287f57457bd746cb1fcfc38cf94d235deb2cfca81b");
//        String txId = blockchainAPI.signAndSendRawTransaction(rawTransaction, keyPair);
//        logger.info("Response id:" + txId);
//        Assert.assertNotNull("transaction id is null", txId);
//    }
//
//    @Test
//    public void testGetTransferLog() throws IOException {
//        TransferFilter transferFilter = new TransferFilter();
//        transferFilter.setRange("block", 107800, 107902);
//        transferFilter.setOptions(0, 10);
//
//        ArrayList<TransferLog> transferLog = blockchainAPI.getTransferLog(OrderFilter.ASC, transferFilter);
//        if (transferLog != null) {
//            logger.info(JSON.toJSONString(transferLog));
//        }
//        Assert.assertNotNull("TransferLog is null", transferLog);
//>>>>>>> 462d8a3c4504f09ffd68a74077e5d30316d5cf76
//    }
//
//    @Test
//    public void testGetEventsLog() throws IOException {
//        EventFilter filter = new EventFilter();
//        filter.setRange("block", 50000, 100000);
//        filter.setOptions(0, 10);
//        ArrayList<FilteredEvent> events = blockchainAPI.getFilterEvent(OrderFilter.DESC, null, filter);
//        if (events != null) {
//            logger.info(JSON.toJSONString(events));
//        }
//        Assert.assertNotNull("filter event is null", events);
//    }
//
//    @Test
//    public void testGetTransaction() throws IOException {
//        Transaction transaction = blockchainAPI.getTransaction("0x6c841dcb71d8ccb6fcf347e03fdf7b115038d9fe367b9e8597a33408113783cb", false, "218143");
//        if (transaction != null) {
//            logger.info(JSON.toJSONString(transaction));
//        }
//        Assert.assertNotNull("transaction is null", transaction);
//    }

}
