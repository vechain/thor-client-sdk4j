package com.vechain.thorclient.console;

import com.vechain.thorclient.core.model.blockchain.NodeProvider;
import com.vechain.thorclient.utils.StringUtils;
import org.apache.commons.cli.CommandLine;

public class Main {

    private static final String SIGN                    = "sign";

    private static final String CREATE_WALLET           = "createWallet";

    private static final String SEND                    = "signAndSend";

    private static final String CHAIN_TAG               = "getChainTag";

    private static final String BLOCK_REF               = "getBlockRef";

    private static final String GET_BLOCK               = "getBlock";

    private static final String GET_TRANSACTION         = "getTransaction";

    private static final String GET_TRANSACTION_RECEIPT = "getTransactionReceipt";

    private static final String SEND_RAW                = "sendRaw";

    private static final String SEND_TRANSACTION        = "sendTxn";


    public static void main(String[] args) throws Exception {

//        CommandLine commandLine = new CommandLine.Builder().addArg( args ).parse();

        if (args.length == 0) {
            System.out.println("找不到有效的参数~");
            System.exit(0);
        }

        String privateKey = processConsoleArguments( args );
        if (args[0].equals(GET_TRANSACTION)) {
            // args=getTransaction txId providerUrl
            TransactionConsole.getTransaction( args );
        }else if (args[0].equals(GET_TRANSACTION_RECEIPT)) {
            // args=getTransaction txId providerUrl
            TransactionConsole.getTransactionRecipient( args );
        }else if (args[0].equals(SIGN)) {
            TransactionConsole.signVETTxn( args );
        }else if (args[0].equals(CHAIN_TAG)) {
            BlockchainQueryConsole.getChainTag();
        }else if (args[0].equals(GET_BLOCK)) {
            BlockchainQueryConsole.getBestBlock();
        }else if (args[0].equals(BLOCK_REF)) {
            BlockchainQueryConsole.getBestBlockRef();
        }else if (args[0].equals(CREATE_WALLET)) {
            WalletConsole.createWalletToKeystoreFile( args );
        }else if (args[0].equals(SEND)) {
            // args=signAndSendVET {providerUrl} {privateKey} {filePath}
            TransactionConsole.sendTransactionFromCSVFile( args, privateKey );
        }else if (args[0].equals( SEND_RAW )) {
            // args=sendVETRaw {providerUrl} {rawTransaction}
            TransactionConsole.sendRawTransaction( args );
        }else if (args[0].equals( SEND_TRANSACTION )) {

        }
    }


    /**
     * Process console input arguments
     * @param args input arguments
     * @return
     */
    private static String processConsoleArguments(String[] args) {
        String privateKey = null;
        String nodeProviderUrl = null;
        if (args[0].equals(CHAIN_TAG) || args[0].equals(BLOCK_REF) || args[0].equals(GET_BLOCK) || args[0].equals(SEND) || args[0].equals( SEND_RAW )) {

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
        return privateKey;
    }

}
