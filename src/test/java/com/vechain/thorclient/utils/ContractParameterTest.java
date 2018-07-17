package com.vechain.thorclient.utils;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.clients.TransactionClient;
import com.vechain.thorclient.core.model.blockchain.ContractCall;
import com.vechain.thorclient.core.model.blockchain.ContractCallResult;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.base.AbiDefinition;

@RunWith(JUnit4.class)
public class ContractParameterTest extends BaseTest {

	static Logger logger = Logger.getLogger("ContractParameterTest");
	static String bytesParam = "aaaaaaa";
	static String stringParam = "bbbbbbb";
	static BigInteger[] intarray = new BigInteger[] { BigInteger.valueOf(1), BigInteger.valueOf(2),
			BigInteger.valueOf(3), BigInteger.valueOf(4), BigInteger.valueOf(5) };

	static String fakeContractAddress = "0x13874d08c4194642ead0c414445226cebb7e376a";

	String fname = "testParams";

	@Test
	public void testGetLength() {
		FakeContract aFakeContract = new FakeContract();
		AbiDefinition abiDefinition = aFakeContract.findAbiDefinition("testParams");
		List<AbiDefinition.NamedType> inputs = abiDefinition.getInputs();
		int length = ContractParamEncoder.getLength(inputs, bytesParam, stringParam.getBytes(), intarray,
				fakeContractAddress);
		Assert.assertEquals(4, length);
	}

	// @SuppressWarnings("rawtypes")
	// @Test
	// public void testFunctionMDynamicArrayEncode1() {
	// String p1 =
	// "davedavedavedavedavedavedavedavedavedavedavedavedavedavedavedave";
	// List<Type> list = new ArrayList<Type>();
	// list.add(new DynamicBytes(p1.getBytes()));
	// list.add(new Bool(true));
	// list.add(new StaticArray<Uint>(new Uint(BigInteger.ONE), new
	// Uint(BigInteger.valueOf(2)),
	// new Uint(BigInteger.valueOf(3)), new Uint(BigInteger.valueOf(4)), new
	// Uint(BigInteger.valueOf(5)),
	// new Uint(BigInteger.valueOf(6))));
	// Function function = new Function(fname, list,
	// Collections.<TypeReference<?>>emptyList());
	// String s1 = FunctionEncoder.encode(function);
	//
	// List<NamedType> inputs = new ArrayList<NamedType>();
	// inputs.add(new NamedType("a", "bytes"));
	// inputs.add(new NamedType("b", "bool"));
	// inputs.add(new NamedType("c", "uint[6]"));
	// AbiDefinition abiDefinition = new AbiDefinition(true, inputs, fname, null,
	// "", false);
	//
	// List<BigInteger> plist = new ArrayList<BigInteger>();
	// plist.add(BigInteger.ONE);
	// plist.add(BigInteger.valueOf(2));
	// plist.add(BigInteger.valueOf(3));
	// plist.add(BigInteger.valueOf(4));
	// plist.add(BigInteger.valueOf(5));
	// plist.add(BigInteger.valueOf(6));
	// ContractCall call = FakeContract.buildCall(abiDefinition, p1.getBytes(),
	// true, plist);
	// Assert.assertEquals(s1.substring(10), call.getData().substring(10));
	// }
	//
	// @Test
	// public void testFunctionMDynamicArrayEncode2() {
	// List<Type> list = new ArrayList<Type>();
	// list.add(new DynamicBytes(stringParam.getBytes()));
	// list.add(new Utf8String(bytesParam));
	// list.add(new DynamicArray<Uint>(new Uint(BigInteger.ONE), new
	// Uint(BigInteger.valueOf(2)),
	// new Uint(BigInteger.valueOf(3)), new Uint(BigInteger.valueOf(4)), new
	// Uint(BigInteger.valueOf(5))));
	// list.add(new org.web3j.abi.datatypes.Address(fromAddress));
	// Function function = new Function(fname, list,
	// Collections.<TypeReference<?>>emptyList());
	// String s1 = FunctionEncoder.encode(function);
	// logger.info(s1);
	//
	// FakeContract aFakeContract = new FakeContract();
	// AbiDefinition abiDefinition = aFakeContract.findAbiDefinition(fname);
	// ContractCall call = FakeContract.buildCall(abiDefinition,
	// stringParam.getBytes(), bytesParam, intarray,
	// BytesUtils.toByteArray(fromAddress));
	//
	// logger.info(call.getData());
	// Assert.assertEquals(s1.substring(10), call.getData().substring(10));
	// }

	@Test
	public void testConstractParam() {
		// TransferResult transferResult =
		// TransactionClient.deployContract(FakeContract.bin, 10000000, (byte) 0, 720,
		// ECKeyPair.create(privateKey));
		// logger.info("Deploy contract result:" + JSON.toJSONString(transferResult));
		// try {
		// Thread.sleep(10000);
		// } catch (InterruptedException e) {
		// }
		//
		// Receipt receipt =
		// TransactionClient.getTransactionReceipt(transferResult.getId(), null);
		// logger.info("Deploy contract Receipt:" + JSON.toJSONString(receipt));
		//
		// fakeContractAddress = receipt.getOutputs().get(0).getContractAddress();

		FakeContract aFakeContract = new FakeContract();
		AbiDefinition abiDefinition = aFakeContract.findAbiDefinition("testParams");
		ContractCall call = FakeContract.buildCall(abiDefinition, stringParam.getBytes(), bytesParam, intarray,
				BytesUtils.toByteArray(fromAddress));

		logger.info(call.getData());
		ContractCallResult contractCallResult = TransactionClient.callContract(call,
				Address.fromHexString(fakeContractAddress), null);
		Assert.assertNotNull(contractCallResult);
		logger.info("call contract result:" + JSON.toJSONString(contractCallResult));

		String really = "ae025a0b000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000000000000000c000000000000000000000000000000000000000000000000000000000000001007567d83b7b8d80addcb281a71d54fc7b3364ffed0000000000000000000000000000000000000000000000000000000000000000000000000000000000000007626262626262620000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000076161616161616100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000500000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000300000000000000000000000000000000000000000000000000000000000000040000000000000000000000000000000000000000000000000000000000000005";
		Assert.assertEquals(really, call.getData().substring(2));
	}
}
