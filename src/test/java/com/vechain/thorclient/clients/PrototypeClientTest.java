package com.vechain.thorclient.clients;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.ContractCallResult;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.Revision;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

@RunWith(JUnit4.class)
public class PrototypeClientTest extends BaseTest {

    static final String UserAddress = "VXc71ADC46c5891a8963Ea5A5eeAF578E0A2959779";

    @Test
    public void testGetMaster() throws IOException {

        ContractCallResult callResult = ProtoTypeContractClient.getMasterAddress(Address.fromHexString(fromAddress), Revision.BEST);
        logger.info("testGetMaster result:" + JSON.toJSONString(callResult));
    }

    @Test
    public void testSetMaster() throws IOException {
        TransferResult result = ProtoTypeContractClient.setMasterAddress(new Address[] { Address.fromHexString(fromAddress) }, new Address[] { Address.fromHexString(fromAddress) },
                ContractClient.GasLimit, (byte) 0x1, 720, ECKeyPair.create(privateKey));
        logger.info("result: " + JSON.toJSONString(result));
    }

    @Test
    public void testIsUser() throws IOException {
        ContractCallResult callResult = ProtoTypeContractClient.isUser(Address.fromHexString(fromAddress), Address.fromHexString(UserAddress), Revision.BEST);
        logger.info("Get isUser result:" + JSON.toJSONString(callResult));
    }

    @Test
    public void testAddUser() throws IOException {
        TransferResult transferResult = ProtoTypeContractClient.addUser(new Address[] { Address.fromHexString(fromAddress) }, new Address[] { Address.fromHexString(UserAddress) },
                ContractClient.GasLimit, (byte) 0x1, 720, ECKeyPair.create(privateKey));
        logger.info("Add user:" + JSON.toJSONString(transferResult));
    }

    @Test
    public void testRemoveUser() throws IOException {
        TransferResult transferResult = ProtoTypeContractClient.removeUsers(new Address[] { Address.fromHexString(fromAddress) },
                new Address[] { Address.fromHexString(UserAddress) }, ContractClient.GasLimit, (byte) 0x1, 720, ECKeyPair.create(privateKey));
        logger.info("Remove user:" + JSON.toJSONString(transferResult));
    }

    @Test
    public void testSetUserPlan() throws IOException {
        Amount credit = Amount.VTHO();
        credit.setDecimalAmount("12.00");
        Amount recovery = Amount.VTHO();
        recovery.setDecimalAmount("0.00001");

        TransferResult result = ProtoTypeContractClient.setUserPlans(new Address[] { Address.fromHexString(fromAddress) }, new Amount[] { credit }, new Amount[] { recovery },
                ContractClient.GasLimit, (byte) 0x1, 720, ECKeyPair.create(privateKey));
        logger.info("set user plans:" + JSON.toJSONString(result));
    }

    @Test
    public void testGetUserPlan() throws IOException {
        ContractCallResult callResult = ProtoTypeContractClient.getUserPlan(Address.fromHexString(fromAddress), Revision.BEST);
        logger.info("Get user plan result:" + JSON.toJSONString(callResult));
    }

    @Test
    public void testGetUserCredit() throws IOException {
        ContractCallResult callResult = ProtoTypeContractClient.getUserCredit(Address.fromHexString(fromAddress), Address.fromHexString(UserAddress), Revision.BEST);
        logger.info("Get user plan result:" + JSON.toJSONString(callResult));
    }

}
