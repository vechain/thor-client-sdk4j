package com.vechain.thorclient.clients;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.ContractCallResult;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.utils.crypto.ECKeyPair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class PrototypeClientTest extends BaseTest {

    @Test
    public void testGetMaster() throws IOException {

        ContractCallResult callResult = ProtoTypeContractClient.getMasterAddress( Address.fromHexString( fromAddress ) );
        logger.info( "testGetMaster result:" + JSON.toJSONString( callResult ) );
    }

    @Test
    public void testSetMaster() throws IOException {
        TransferResult result = ProtoTypeContractClient.setMasterAddress( new Address[]{Address.fromHexString( fromAddress ) }, new Address[]{Address.fromHexString( "VXc71ADC46c5891a8963Ea5A5eeAF578E0A2959779" )},100000, (byte)0x1, 720, ECKeyPair.create(privateKey ) );
        logger.info( "result: " + JSON.toJSONString( result ) );
    }
}
