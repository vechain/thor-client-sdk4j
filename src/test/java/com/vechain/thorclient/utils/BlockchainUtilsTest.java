package com.vechain.thorclient.utils;

import com.vechain.thorclient.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BlockchainUtilsTest extends BaseTest {

	@Test
	public void testChecksumAddress() {
		String address = "0xE247A45c287191d435A8a5D72A7C8dc030451E9F";
		String checksumAddress = BlockchainUtils.getChecksumAddress(address);
		Assert.assertEquals(checksumAddress, address);
	}

	@Test
	public void testIsChecksumAddress() {
		String address = "0xE247A45c287191d435A8a5D72A7C8dc030451E9F";
		boolean isChecked = BlockchainUtils.checkSumAddress(address);
		Assert.assertTrue(isChecked);
	}
}
