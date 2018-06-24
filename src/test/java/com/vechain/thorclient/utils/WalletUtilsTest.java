package com.vechain.thorclient.utils;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.wallet.WalletInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class WalletUtilsTest extends BaseTest {

	@Test
	public void testCreateWallet() {
		WalletInfo walletInfo = WalletUtils.createWallet("123456");
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
		WalletInfo walletInfo = WalletUtils.loadKeystore(keyStore, "123456");

		logger.info("privKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPrivateKey(), Prefix.ZeroLowerX));
		logger.info("pubKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPublicKey(), Prefix.ZeroLowerX));
		logger.info("address:" + walletInfo.getKeyPair().getAddress());
	}


}
