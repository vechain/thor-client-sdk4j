package com.vechain.thorclient.utils;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.wallet.CipherException;
import com.vechain.thorclient.core.wallet.Wallet;
import com.vechain.thorclient.core.wallet.WalletFile;
import com.vechain.thorclient.core.wallet.WalletInfo;
import com.vechain.thorclient.utils.crypto.ECKeyPair;
import org.junit.Assert;
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
		logger.info("pubKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPublicKey(false), Prefix
				.ZeroLowerX));
		logger.info("address:" + walletInfo.getKeyPair().getHexAddress());
		/**
		 *
		 * 2018-06-29  12:41:43.862 [main] INFO  com.vechain.thorclient.utils.WalletUtilsTest - KeyStore:{"address":"0xdc4e3ba38f2ed4d270885dd7d0f95e3d09fbd902","crypto":{"cipher":"aes-128-ctr","cipherparams":{"iv":"47f69a61721e6c9aa0453301db38b6dd"},"ciphertext":"c7a3da20adcc15b11a1cd979a717e84756ac24205345ad5a9c96a90a81d6fb4d","kdf":"scrypt","kdfparams":{"dklen":32,"n":262144,"p":1,"r":8,"salt":"14220482313d30f4ff7978908a87366827d9185a104632e46cf008dcf7b9fcf7"},"mac":"4f3d60c781849557159aa81d1d34292bf133d8e87d441169aed643e7264a4cda"},"id":"6870071a-d9d3-4e2c-9148-6b52886a5129","version":3}
         2018-06-29  12:41:43.864 [main] INFO  com.vechain.thorclient.utils.WalletUtilsTest - privKey:0x21cff7040a640d856a8e6ea201baf33afdbdba47f03ed603e32d025d236cc4df
         2018-06-29  12:41:43.865 [main] INFO  com.vechain.thorclient.utils.WalletUtilsTest - pubKey:0xc0af301ba423508103c231e0b0e320fa6cddfee7154a411c0deca5937365b933817864c9839bcbbd45aec1b3aa3e6658639709ea34c0acfef41746f79614ccaa
         2018-06-29  12:41:43.866 [main] INFO  com.vechain.thorclient.utils.WalletUtilsTest - address:0xdc4e3ba38f2ed4d270885dd7d0f95e3d09fbd902
		 */
	}

	@Test
	public void testloadWallet() {
		String keyStore = "{\"version\":3,\"id\":\"F282FCC0-8A7F-4A1A-A8E1-14BA6A374489\",\"crypto\":{\"ciphertext\":\"3f1ed845d6a7bcc830a6dfdd494b114bfa42376c87da168421efe129a04cf9a8\",\"cipherparams\":{\"iv\":\"a9d5d891c61d254b0697d1b0a3fa0c0c\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"r\":8,\"p\":1,\"n\":262144,\"dklen\":32,\"salt\":\"fe4ee1e137097135937ea43e826ed12d224348a1d1291c3c88d3c99f2753cc39\"},\"mac\":\"cb953016a95dd67dcc259068986efa852c3e8eab9fa73d5ec7f53c2405321f62\",\"cipher\":\"aes-128-ctr\"},\"address\":\"866a849122133888214ac9fc59550077adf14975\"}\n";
		WalletInfo walletInfo = WalletUtils.loadKeystore(keyStore, "Vechain1234");

		logger.info("privKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPrivateKey(), Prefix.ZeroLowerX));
		logger.info("pubKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPublicKey(false), Prefix.ZeroLowerX));
		logger.info("address:" + walletInfo.getKeyPair().getHexAddress());
        Assert.assertEquals("0x866a849122133888214ac9fc59550077adf14975", walletInfo.getKeyPair().getHexAddress());
	}

	@Test
	public void testloadWalletFromPrivateKey() throws CipherException {
		String privateKey = "0xdce1443bd2ef0c2631adc1c67e5c93f13dc23a41c18b536effbbdcbcdb96fb65";
		ECKeyPair keyPair = ECKeyPair.create(privateKey);
		WalletFile walletFile = Wallet.createStandard("123456", keyPair);
		logger.info(JSON.toJSONString(walletFile));
	}
}
