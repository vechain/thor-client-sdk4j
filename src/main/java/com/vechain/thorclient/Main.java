package com.vechain.thorclient;

import java.io.File;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.clients.TransactionClient;
import com.vechain.thorclient.core.model.blockchain.Block;
import com.vechain.thorclient.core.model.blockchain.Receipt;
import com.vechain.thorclient.core.model.blockchain.Transaction;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import com.vechain.thorclient.clients.BlockClient;
import com.vechain.thorclient.clients.BlockchainClient;
import com.vechain.thorclient.core.model.blockchain.NodeProvider;
import com.vechain.thorclient.core.model.clients.Revision;
import com.vechain.thorclient.core.wallet.WalletInfo;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.Prefix;
import com.vechain.thorclient.utils.StringUtils;
import com.vechain.thorclient.utils.WalletUtils;

public class Main {

    private static final String SIGN = "signVET";

    private static final String CREATE_WALLET = "createWallet";

    private static final String SEND = "sendVET";

    private static final String CHAIN_TAG = "getChainTag";

    private static final String BLOCK_REF = "getBlockRef";

    private static final String GET_BLOCK = "getBlock";

    private static final String GET_TRANSACTION = "getTransaction";

    private static final String GET_TRANSACTION_RECEIPT = "getTransactionReceipt";

//    private static final String SEND_RAW     = "sendRaw";

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println("找不到有效的参数~");
            System.exit(0);
        }
        // String privateKey = System.getenv(PRIVATE_KEY);
        // String nodeProviderUrl = System.getenv(NODE_PROVIDER_URL);
        String privateKey      = null;
        String nodeProviderUrl = null;
//        for (String arg : args) {
//            System.out.println(arg);
//        }
        if (args[0].equals(CHAIN_TAG) || args[0].equals(BLOCK_REF) || args[0].equals(GET_BLOCK) || args[0].equals(SEND)) {
            // args=chainTag/blockRef {providerUrl}
            // args=send {providerUrl} {privateKey} {filePath}
            // args=sign filePath privateKey
            if (args.length > 1 && !StringUtils.isBlank(args[1]) && args[1].startsWith("http")) {
                nodeProviderUrl = args[1];
            }
            if (StringUtils.isBlank(nodeProviderUrl)) {
                System.out.println("You have input invalid parameters.");
                System.exit(0);
            }
            if (args.length > 2 && args[0].equals(SEND)) {
                // args=send {providerUrl} {privateKey}
                if (!StringUtils.isBlank(args[2])) {
                    privateKey = args[2];
                }
                if (StringUtils.isBlank(privateKey)) {
                    System.out.println("You have input invalid parameters.");
                    System.exit(0);
                }
            }
            NodeProvider nodeProvider = NodeProvider.getNodeProvider();
            nodeProvider.setProvider(nodeProviderUrl);
            nodeProvider.setSocketTimeout(5000);
        }

        if (args[0].equals(GET_TRANSACTION)) {
            // args=getTransaction txId providerUrl
            if (args.length < 3 || StringUtils.isBlank(args[2])) {
                System.out.println("You have input invalid parameters.");
                System.exit(0);
            }
            String txId    = args[1];
            String nodeUrl = args[2];
            if (StringUtils.isBlank(nodeUrl) && !nodeUrl.startsWith("http")) {
                System.out.println("You have input invalid parameters.");
                System.exit(0);
            }
            NodeProvider nodeProvider = NodeProvider.getNodeProvider();
            nodeProvider.setProvider(nodeUrl);
            nodeProvider.setTimeout(5000);
            Transaction transaction = TransactionClient.getTransaction(txId, true, null);
            System.out.println("Transaction:" + JSON.toJSONString(transaction));
        }

        if (args[0].equals(GET_TRANSACTION_RECEIPT)) {
            // args=getTransaction txId providerUrl
            if (args.length < 3 || StringUtils.isBlank(args[2])) {
                System.out.println("You have input invalid parameters.");
                System.exit(0);
            }
            String txId    = args[1];
            String nodeUrl = args[2];
            if (StringUtils.isBlank(nodeUrl) && !nodeUrl.startsWith("http")) {
                System.out.println("You have input invalid parameters.");
                System.exit(0);
            }
            NodeProvider nodeProvider = NodeProvider.getNodeProvider();
            nodeProvider.setProvider(nodeUrl);
            nodeProvider.setTimeout(5000);
            Receipt receipt = TransactionClient.getTransactionReceipt(txId, null);
            System.out.println("Receipt:" + JSON.toJSONString(receipt));
        }

        if (args[0].equals(SIGN)) {
            // args=sign filePath privateKey
            if (args.length < 3 || StringUtils.isBlank(args[2])) {
                System.out.println("You have input invalid parameters.");
                System.exit(0);
            }
            privateKey = args[2];
        }

        if (args[0].equals(CHAIN_TAG)) {
            byte   chainTagByte = BlockchainClient.getChainTag();
            String chainTag     = String.format("%02x", chainTagByte);
            System.out.println("ChainTag:");
            System.out.println("0x" + chainTag);
        }

        if (args[0].equals(GET_BLOCK)) {
            Block block = BlockClient.getBlock(Revision.BEST);
            System.out.println("Block:");
            System.out.println(JSON.toJSONString(block));
        }

        if (args[0].equals(BLOCK_REF)) {
            byte[] blockRefByte = BlockClient.getBlock(Revision.BEST).blockRef().toByteArray();
            String blockRef     = ByteUtils.toHexString(blockRefByte);
            System.out.println("BlockRef:");
            System.out.println("0x" + blockRef);
        }

        if (args[0].equals(CREATE_WALLET)) {
            // args=createWallet {password}
            if (args.length < 2) {
                System.out.println("You have input invalid parameters.");
                System.exit(0);
            }
            WalletInfo walletInfo    = WalletUtils.createWallet(args[1]);
            byte[]     rawPrivateKey = walletInfo.getKeyPair().getRawPrivateKey();
            String     newPrivateKey = BytesUtils.toHexString(rawPrivateKey, Prefix.ZeroLowerX);
            String     keyStoreStr   = walletInfo.toKeystoreString();
            System.out.println("The wallet created successfully and the key store is:");
            System.out.println(keyStoreStr);
            System.out.println("The wallet created successfully and the privateKey is:");
            System.out.println(newPrivateKey);
        }

        if (args[0].equals(SIGN)) {

            File file = new File(args[1]);
            if (file.isFile()) {
                List<String[]> transactionList = ConsoleUtils.readExcelFile(args[1]);
                String         rawTransaction  = ConsoleUtils.doSignVETTx(transactionList, privateKey, false);
                System.out.println("Raw Transaction:");
                System.out.println(rawTransaction);
            } else {
                System.out.println("You have input invalid parameters.");
            }
        }

        if (args[0].equals(SEND)) {
            // args=send {providerUrl} {privateKey} {filePath}
            if (args.length < 3) {
                System.out.println("You have input invalid parameters.");
                System.exit(0);
            }
            File file = new File(args[3]);
            if (file.isFile()) {
                List<String[]> transactionList = ConsoleUtils.readExcelFile(args[3]);
                String         result          = ConsoleUtils.doSignVETTx(transactionList, privateKey, true);
                System.out.println("Send Result:");
                System.out.println(result);
            } else {
                System.out.println("You have input invalid parameters.");
            }
        }

    }

}
