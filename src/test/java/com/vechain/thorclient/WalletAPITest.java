package com.vechain.thorclient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.utils.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.vechain.thorclient.utils.crypto.ECKeyPair;
import com.vechain.thorclient.core.model.blockchain.Account;
import com.vechain.thorclient.core.model.blockchain.Clause;
import com.vechain.thorclient.core.model.blockchain.RawTransaction;
import com.vechain.thorclient.core.wallet.WalletInfo;
import com.vechain.thorclient.service.WalletAPI;

@RunWith(JUnit4.class)
public class WalletAPITest extends BaseTest {

	@Test
	public void testCreateWallet() {
		WalletInfo walletInfo = WalletAPI.createWallet("123456");
		String keyStoreString = walletInfo.toKeystoreString();

		logger.info("KeyStore:" + keyStoreString);
		logger.info("privKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPrivateKey(), Prefix.ZeroLowerX));
		logger.info("pubKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPublicKey(), Prefix.ZeroLowerX));
		logger.info("address:" + walletInfo.getKeyPair().getAddress());
		/**
		 *
		 * 2018-06-19 15:12:50.351 INFO 23601 --- [ main] thorclient :
		 * KeyStore:{"address":"0xf56a23f7b9c3b1fd68d812e3d2357bbe68bfd087","crypto":{"cipher":"aes-128-ctr","cipherparams":{"iv":"f390a8c9328ea46779cb17d02d7ef2a5"},"ciphertext":"622375a3ae80035f99b19d17639b1c4da12153d60b6735a3009065aa38766f64","kdf":"scrypt","kdfparams":{"dklen":32,"n":262144,"p":1,"r":8,"salt":"c2d13d5476d7b807c8630cf931f4e0e474bbeae590fa43f90197214aa607d426"},"mac":"10cdadfdb6ecdeec40fe3330480a2978dff4d10c02e5d7231a88cafe7af711af"},"id":"6417d351-1cb9-421b-b446-1bd7f887dfbe","version":3}
		 * 2018-06-19 15:12:50.352 INFO 23601 --- [ main] thorclient :
		 * privKey:0x493241296c17302e66e5da072673bd2161f6f2d3b7a72c87562685e107b28e7f
		 * 2018-06-19 15:12:50.353 INFO 23601 --- [ main] thorclient :
		 * pubKey:0xbdb4aa66b6df60fca14a7b3874d6ad95597fd92711e483259509546156f16d6c237a82619dade08ea4db14488495cb5066245d585e975ce0aa7a4d8ae3445570
		 * 2018-06-19 15:12:50.354 INFO 23601 --- [ main] thorclient :
		 * address:0xf56a23f7b9c3b1fd68d812e3d2357bbe68bfd087
		 */
	}

	@Test
	public void testloadWallet() {
		String keyStore = "{\"address\":\"0xf56a23f7b9c3b1fd68d812e3d2357bbe68bfd087\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"f390a8c9328ea46779cb17d02d7ef2a5\"},\"ciphertext\":\"622375a3ae80035f99b19d17639b1c4da12153d60b6735a3009065aa38766f64\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"c2d13d5476d7b807c8630cf931f4e0e474bbeae590fa43f90197214aa607d426\"},\"mac\":\"10cdadfdb6ecdeec40fe3330480a2978dff4d10c02e5d7231a88cafe7af711af\"},\"id\":\"6417d351-1cb9-421b-b446-1bd7f887dfbe\",\"version\":3}";
		WalletInfo walletInfo = WalletAPI.loadKeystore(keyStore, "123456");

		logger.info("privKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPrivateKey(), Prefix.ZeroLowerX));
		logger.info("pubKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPublicKey(), Prefix.ZeroLowerX));
		logger.info("address:" + walletInfo.getKeyPair().getAddress());
	}
	
	@Test
    public void testWalletForTransaction() throws InterruptedException, IOException {
        String contractAddress = "0x0000000000000000000000000000456e65726779";
        String amount          = "100000.00";
        String privateKey      = "0xc8c53657e41a8d669349fc287f57457bd746cb1fcfc38cf94d235deb2cfca81b";
        byte[] blockRef        = blockchainAPI.getBestBlockRef();

        //新建wallet one
        WalletInfo oneWalletInfo = WalletAPI.createWallet("123456");
        String            oneAddress    = oneWalletInfo.getWalletFile().getAddress();

        ArrayList<Clause> clauseList = new ArrayList<>();
        Clause            clause      = new Clause();
        clause.setTo(oneAddress);
        clause.setValue(amount);
        clauseList.add(clause);

        logger.info("The oneAddress is " + oneAddress);
        RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction((byte) 0xab, blockRef, 720, 21000, (byte) 1, CryptoUtils.generateTxNonce(), clauseList);

        ECKeyPair keyPair = ECKeyPair.create(privateKey);
        String    id      = blockchainAPI.signAndSendRawTransaction(rawTransaction, keyPair);
        logger.info("The TX id is " + id);

        Assert.assertNotNull(id);

        Thread.sleep(10000);

        Account account = blockchainAPI.getBalance(oneAddress, "best");
        String  balance = account.getBalance();

        BigDecimal balanceAmt = BlockchainUtils.balance(balance, 18, 2);
        Assert.assertEquals(amount, balanceAmt.toString());
        logger.info("The balance is " + balanceAmt.toString());

        clauseList.clear();
        clauseList.add(new Clause(oneAddress, "0xa968163f0a57b4000000", ""));
        RawTransaction vthoRawTransaction = RawTransactionFactory.getInstance().createRawTransaction((byte) 0xab, blockRef, 720, 80000, (byte) 1, clauseList, contractAddress);
        String         vthoId             = blockchainAPI.signAndSendRawTransaction(vthoRawTransaction, keyPair);
        logger.info("The VTHO TX id is " + vthoId);

        Thread.sleep(10000);

        account = blockchainAPI.getBalance(oneAddress, "best");
        String energy = account.getEnergy();
        BigDecimal energyBal = BlockchainUtils.balance(energy, 18, 2);
        logger.info("The energy balance is " + energyBal.toString());

        //从wallet one 发交易到新建的wallet two
        String onePrivateKey = BytesUtils.toHexString(oneWalletInfo.getKeyPair().getRawPrivateKey(), Prefix.ZeroLowerX);

        WalletInfo twoWallet  = WalletAPI.createWallet("123456");
        String            twoAddress = twoWallet.getKeyPair().getAddress();
        logger.info("The two wallet address:" + twoAddress);
        String transferAmount = "1000.11";
        clauseList.clear();
        clauseList.add(new Clause(twoAddress, transferAmount, ""));
        RawTransaction rawTransactionForNewWallet = RawTransactionFactory.getInstance().createRawTransaction((byte) 0xab, blockRef, 720, 21000, (byte) 1, CryptoUtils.generateTxNonce(), clauseList);

        ECKeyPair walletKeyPair = ECKeyPair.create(onePrivateKey);
        String    txId          = blockchainAPI.signAndSendRawTransaction(rawTransactionForNewWallet, walletKeyPair);
        Assert.assertNotNull(txId);
        Thread.sleep(20000);

        logger.info("The TX id is " + txId);
        Account twoAddressAccount = blockchainAPI.getBalance(twoAddress, "best");

        String     transferBalance = twoAddressAccount.getBalance();
        BigDecimal twoBalanceAmt   = BlockchainUtils.balance(transferBalance, 18, 2);
        Assert.assertEquals(transferAmount, twoBalanceAmt.toString());
        logger.info("The balance is " + twoBalanceAmt.toString());

    }

}
