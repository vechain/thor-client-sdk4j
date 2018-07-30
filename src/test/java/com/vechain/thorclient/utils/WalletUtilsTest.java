package com.vechain.thorclient.utils;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.wallet.WalletInfo;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class WalletUtilsTest extends BaseTest {

	// @Test
	public void testCreateWallet() {
		WalletInfo walletInfo = WalletUtils.createWallet("123456");
		String keyStoreString = walletInfo.toKeystoreString();

		logger.info("KeyStore:" + keyStoreString);
		logger.info("privKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPrivateKey(), Prefix.ZeroLowerX));
		logger.info("pubKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPublicKey(), Prefix.ZeroLowerX));
		logger.info("address:" + walletInfo.getKeyPair().getHexAddress());
		/**
		 *
		 * 2018-06-29 12:41:43.862 [main] INFO
		 * com.vechain.thorclient.utils.WalletUtilsTest -
		 * KeyStore:{"address":"0xdc4e3ba38f2ed4d270885dd7d0f95e3d09fbd902","crypto":{"cipher":"aes-128-ctr","cipherparams":{"iv":"47f69a61721e6c9aa0453301db38b6dd"},"ciphertext":"c7a3da20adcc15b11a1cd979a717e84756ac24205345ad5a9c96a90a81d6fb4d","kdf":"scrypt","kdfparams":{"dklen":32,"n":262144,"p":1,"r":8,"salt":"14220482313d30f4ff7978908a87366827d9185a104632e46cf008dcf7b9fcf7"},"mac":"4f3d60c781849557159aa81d1d34292bf133d8e87d441169aed643e7264a4cda"},"id":"6870071a-d9d3-4e2c-9148-6b52886a5129","version":3}
		 * 2018-06-29 12:41:43.864 [main] INFO
		 * com.vechain.thorclient.utils.WalletUtilsTest -
		 * privKey:0x21cff7040a640d856a8e6ea201baf33afdbdba47f03ed603e32d025d236cc4df
		 * 2018-06-29 12:41:43.865 [main] INFO
		 * com.vechain.thorclient.utils.WalletUtilsTest -
		 * pubKey:0xc0af301ba423508103c231e0b0e320fa6cddfee7154a411c0deca5937365b933817864c9839bcbbd45aec1b3aa3e6658639709ea34c0acfef41746f79614ccaa
		 * 2018-06-29 12:41:43.866 [main] INFO
		 * com.vechain.thorclient.utils.WalletUtilsTest -
		 * address:0xdc4e3ba38f2ed4d270885dd7d0f95e3d09fbd902
		 */
	}

	@Test
	public void testloadWallet() {
		String keyStore = "{\"address\":\"56de928bd517d49aa6596f8b8f0d64e83570d6f5\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"25cfe386c7e29b0831f6deb1cef34fa7\"},\"ciphertext\":\"8310937396d8b9142124449539ba60a8aaac8653e892404c800a4a1a28d61ca9\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"4b55556473e4ab9a4d1a09f3b9962e6d6f495d54ac8faa8a0bc5b53117541490\"},\"mac\":\"a5d614459b299b555878b43afe5e89b0ab3103f348b4345e52cfbdbe788b3b03\"},\"id\":\"be726bec-c6c6-4447-b4c1-2628c3c469b3\",\"version\":3}";
		WalletInfo walletInfo = WalletUtils.loadKeystore(keyStore, "Vechain1234");

		logger.info("privKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPrivateKey(), Prefix.ZeroLowerX));
		logger.info("pubKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPublicKey(), Prefix.ZeroLowerX));
		logger.info("address:" + walletInfo.getKeyPair().getHexAddress());
		Assert.assertEquals("0x56De928bD517D49aa6596f8b8F0d64e83570D6F5".toLowerCase(),
				walletInfo.getKeyPair().getHexAddress());

		System.out.println(ContractParamEncoder.encodeNumeric(BigInteger.valueOf(1), true));

	}

}
