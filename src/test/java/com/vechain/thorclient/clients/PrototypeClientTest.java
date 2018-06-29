package com.vechain.thorclient.clients;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.Block;
import com.vechain.thorclient.core.model.blockchain.ContractCallResult;
import com.vechain.thorclient.core.model.blockchain.Receipt;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.Revision;
import com.vechain.thorclient.core.model.exception.ThorException;
import com.vechain.thorclient.core.model.exception.TransactionException;
import com.vechain.thorclient.utils.StringUtils;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

@RunWith(JUnit4.class)
public class PrototypeClientTest extends BaseTest {

    static final String UserAddress = "VXc71ADC46c5891a8963Ea5A5eeAF578E0A2959779";

    @Test
    public void testGetMaster() throws IOException {

        String currentPrivateKeyAddr = ECKeyPair.create(privateKey).getHexAddress();
        logger.info("current privatekey address:" + currentPrivateKeyAddr);
        ContractCallResult callResult = ProtoTypeContractClient.getMasterAddress(Address.fromHexString(currentPrivateKeyAddr), Revision.BEST);
        logger.info("testGetMaster result:" + JSON.toJSONString(callResult));

    }

    @Test
    public void testSetMaster() throws IOException {
        TransferResult result = ProtoTypeContractClient.setMasterAddress(new Address[] { Address.fromHexString(fromAddress) }, new Address[] { Address.fromHexString(fromAddress) },
                TransactionClient.ContractGasLimit, (byte) 0x0, 720, ECKeyPair.create(privateKey));

        logger.info("result: " + JSON.toJSONString(result));
    }

    @Test
    public void testIsUser() throws IOException {
        ContractCallResult callResult = ProtoTypeContractClient.isUser(Address.fromHexString(fromAddress), Address.fromHexString(UserAddress), Revision.BEST);
        logger.info("Get isUser result:" + JSON.toJSONString(callResult));
    }

    @Test
    public void testAddUser() throws IOException {

        TransferResult transferResult = ProtoTypeContractClient.addUsers(new Address[] { Address.fromHexString(fromAddress) }, new Address[] { Address.fromHexString(UserAddress) },
                TransactionClient.ContractGasLimit, (byte) 0x0, 720, ECKeyPair.create(privateKey));

        logger.info("Add user:" + JSON.toJSONString(transferResult));
    }

    @Test
    public void testRemoveUser() throws IOException {

        TransferResult transferResult = ProtoTypeContractClient.removeUsers(new Address[] { Address.fromHexString(fromAddress) },
                new Address[] { Address.fromHexString(UserAddress) }, TransactionClient.ContractGasLimit, (byte) 0x0, 720, ECKeyPair.create(privateKey));

        logger.info("Remove user:" + JSON.toJSONString(transferResult));
    }

    @Test
    public void testSetUserPlan() throws IOException {
        Amount credit = Amount.VTHO();
        credit.setDecimalAmount("12.00");
        Amount recovery = Amount.VTHO();
        recovery.setDecimalAmount("0.00001");

        TransferResult result = ProtoTypeContractClient.setCreditPlans(new Address[] { Address.fromHexString(fromAddress) }, new Amount[] { credit }, new Amount[] { recovery },
                TransactionClient.ContractGasLimit, (byte) 0x0, 720, ECKeyPair.create(privateKey));

        logger.info("set user plans:" + JSON.toJSONString(result));
    }

    @Test
    public void testGetUserPlan() throws IOException {
        ContractCallResult callResult = ProtoTypeContractClient.getCreditPlan(Address.fromHexString(fromAddress), Revision.BEST);
        logger.info("Get user plan result:" + JSON.toJSONString(callResult));
    }

    @Test
    public void testGetUserCredit() throws IOException {
        ContractCallResult callResult = ProtoTypeContractClient.getUserCredit(Address.fromHexString(fromAddress), Address.fromHexString(UserAddress), Revision.BEST);
        logger.info("Get user plan result:" + JSON.toJSONString(callResult));
    }

    @Test
    public void testSponsor() throws IOException {

        TransferResult transferResult = ProtoTypeContractClient.sponsor(new Address[] { Address.fromHexString(fromAddress) }, TransactionClient.ContractGasLimit,
                (byte) 0x0, 720, ECKeyPair.create(sponsorKey));
        logger.info("sponsor the address result:" + JSON.toJSONString(transferResult));

    }

    @Test
    public void testIsOnSponsor() throws IOException {
        String addressHex = ECKeyPair.create(sponsorKey).getHexAddress();
        ContractCallResult contractCallResult = ProtoTypeContractClient.isSponsor(Address.fromHexString(fromAddress), Address.fromHexString(addressHex), null);
        logger.info("get isSponsor result :" + JSON.toJSONString(contractCallResult));
    }

    @Test
    public void testSelectSponsor() throws IOException {
        String addressHex = ECKeyPair.create(sponsorKey).getHexAddress();
        TransferResult transferResult = ProtoTypeContractClient.selectSponsor(

                new Address[] { Address.fromHexString(fromAddress) }, new Address[] { Address.fromHexString(addressHex) }, TransactionClient.ContractGasLimit, (byte) 0x0, 720,
                ECKeyPair.create(privateKey));
        logger.info("Select sponsor result:" + JSON.toJSONString(transferResult));

    }

    @Test
    public void testQueryCurrentSponsor() throws IOException {
        ContractCallResult result = ProtoTypeContractClient.getCurrentSponsor(Address.fromHexString(fromAddress), null);
        logger.info("getCurrentSponsor result :" + JSON.toJSONString(result));
    }

    @Test
    public void testUnSponsor() throws IOException {
        TransferResult transferResult = ProtoTypeContractClient.unsponsor(new Address[] { Address.fromHexString(fromAddress) }, TransactionClient.ContractGasLimit,
                (byte) 0x0, 720, ECKeyPair.create(sponsorKey));
        logger.info("un-sponsor the address result:" + JSON.toJSONString(transferResult));

    }

    private void checkReceiptAndBlock(String id, long start, long expiration) {
        Block block = BlockClient.getBlock(Revision.BEST);
        long startBlockNumber = Integer.parseInt(block.getNumber());
        if (!StringUtils.isBlank(id)) {
            try {
                Thread.sleep(12 * 10 * 1000);
            } catch (InterruptedException e) {
                throw new ThorException(e);
            }
            while (true) {
                final long current = System.currentTimeMillis();
                if (current - start > expiration) {
                    throw new ThorException("找不到有效的交易Receipt~");
                }
                Receipt receipt = ProtoTypeContractClient.getTransactionReceipt(id, null);
                if (receipt != null) {
                    Block currentBlock = BlockClient.getBlock(Revision.BEST);
                    if (currentBlock != null) {
                        long currentBlockNumber = Integer.parseInt(currentBlock.getNumber());
                        if (currentBlockNumber - startBlockNumber > 12) {
                            break;
                        }
                    }
                } else {
                    throw new ThorException("找不到有效的交易Receipt~");
                }
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    throw new ThorException(e);
                }
            }
        } else {
            throw new TransactionException("invalid tx id.");
        }
    }

    @Test
    public void testNormalAddressWithSponsor() {

        int expirationBlock = 720;
        int expiration = 10 * expirationBlock * 1000;

        long start = System.currentTimeMillis();

        TransferResult transferResult = ProtoTypeContractClient.addUsers(new Address[] { Address.fromHexString(fromAddress) }, new Address[] { Address.fromHexString(UserAddress) },
                TransactionClient.ContractGasLimit, (byte) 0x0, expiration, ECKeyPair.create(privateKey));
        if (transferResult != null) {
            logger.info("MPP add user:" + JSON.toJSONString(transferResult));
            this.checkReceiptAndBlock(transferResult.getId(), start, expiration);
        } else {
            throw new ThorException("ProtoTypeContractClient.addUser出错了~");
        }

        start = System.currentTimeMillis();
        Amount credit = Amount.VTHO();
        credit.setDecimalAmount("100.00");
        Amount recovery = Amount.VTHO();
        recovery.setDecimalAmount("0.00001");

        TransferResult setUserPlansResult = ProtoTypeContractClient.setCreditPlans(new Address[] { Address.fromHexString(fromAddress) }, new Amount[] { credit },
                new Amount[] { recovery }, TransactionClient.ContractGasLimit, (byte) 0x0, expirationBlock, ECKeyPair.create(privateKey));
        if (setUserPlansResult != null) {
            logger.info("MPP set user plans:" + JSON.toJSONString(setUserPlansResult));
            this.checkReceiptAndBlock(setUserPlansResult.getId(), start, expiration);
        } else {
            throw new ThorException("ProtoTypeContractClient.setUserPlans出错了~");
        }
        TransferResult sponsorResult = ProtoTypeContractClient.sponsor(new Address[] { Address.fromHexString(fromAddress) }, TransactionClient.ContractGasLimit,
                (byte) 0x0, 720, ECKeyPair.create(sponsorKey));
        if (sponsorResult != null) {
            logger.info("MPP the address setUserPlansResult:" + JSON.toJSONString(sponsorResult));
            this.checkReceiptAndBlock(sponsorResult.getId(), start, expiration);
        } else {
            throw new ThorException("ProtoTypeContractClient.setUserPlans出错了~");
        }

        String addressHex = ECKeyPair.create(sponsorKey).getHexAddress();
        TransferResult selectSponsorResult = ProtoTypeContractClient.selectSponsor(new Address[] { Address.fromHexString(fromAddress) },
                new Address[] { Address.fromHexString(addressHex) }, TransactionClient.ContractGasLimit, (byte) 0x0, expirationBlock, ECKeyPair.create(privateKey));
        if (selectSponsorResult != null) {
            logger.info("MPP selectSponsor:" + JSON.toJSONString(selectSponsorResult));
            this.checkReceiptAndBlock(selectSponsorResult.getId(), start, expiration);
        } else {
            throw new ThorException("ProtoTypeContractClient.selectSponsor出错了~");
        }

    }

    @Test
    public void testNormalAddressWithoutSponsor() {
        int expirationBlock = 720;
        int expiration = 10 * expirationBlock * 1000;

        long start = System.currentTimeMillis();
        TransferResult transferResult = ProtoTypeContractClient.addUsers(new Address[] { Address.fromHexString(fromAddress) }, new Address[] { Address.fromHexString(UserAddress) },
                TransactionClient.ContractGasLimit, (byte) 0x0, expirationBlock, ECKeyPair.create(privateKey));

        if (transferResult != null) {
            logger.info("MPP to add user:" + JSON.toJSONString(transferResult));
            this.checkReceiptAndBlock(transferResult.getId(), start, expiration);
        } else {
            throw new ThorException("ProtoTypeContractClient.addUser出错了~");
        }

        start = System.currentTimeMillis();
        Amount credit = Amount.VTHO();
        credit.setDecimalAmount("100.00");
        Amount recovery = Amount.VTHO();
        recovery.setDecimalAmount("0.00001");

        TransferResult setUserPlansResult = ProtoTypeContractClient.setCreditPlans(new Address[] { Address.fromHexString(fromAddress) }, new Amount[] { credit },
                new Amount[] { recovery }, TransactionClient.ContractGasLimit, (byte) 0x0, expirationBlock, ECKeyPair.create(privateKey));
        if (setUserPlansResult != null) {
            logger.info("MPP to set user plans:" + JSON.toJSONString(setUserPlansResult));
            this.checkReceiptAndBlock(setUserPlansResult.getId(), start, expiration);
        } else {
            throw new ThorException("ProtoTypeContractClient.setUserPlans出错了~");
        }
    }

}
