package com.vechain.thorclient.console;

import com.vechain.thorclient.core.model.blockchain.NodeProvider;
import com.vechain.thorclient.utils.StringUtils;

public class Main {

	private static final String SIGN = "sign";

	private static final String CREATE_WALLET = "createWallet";

	private static final String SEND = "signAndSend";

	private static final String TRANSFER_VET = "transferVet";

	private static final String TRANSFER_VTHO = "transferVtho";

	private static final String CHAIN_TAG = "getChainTag";

	private static final String BLOCK_REF = "getBlockRef";

	private static final String GET_BLOCK = "getBlock";

	private static final String GET_TRANSACTION = "getTransaction";

	private static final String GET_TRANSACTION_RECEIPT = "getTransactionReceipt";

	private static final String SEND_RAW = "sendRaw";

	private static final String SIGN_VTHO = "signVTHO";

	private static final String PARSE = "parse";

	private static final String BALANCE = "balance";

	public static void main(String[] args) throws Exception {

		try {
			if (args.length == 0) {
				System.out.println("找不到有效的参数~");
				System.exit(0);
			}

			String privateKey = processConsoleArguments(args);
			if (args[0].equals(GET_TRANSACTION)) {
				// args=getTransaction txId providerUrl
				TransactionConsole.getTransaction(args);
			} else if (args[0].equals(GET_TRANSACTION_RECEIPT)) {
				// args=getTransaction txId providerUrl
				TransactionConsole.getTransactionRecipient(args);
			} else if (args[0].equals(SIGN)) {
				TransactionConsole.signVETTxn(args);
			} else if (args[0].equals(SIGN_VTHO)) {
				TransactionConsole.signVTHOTxn(args);
			} else if (args[0].equals(TRANSFER_VET)) {
				TransactionConsole.transferVet(args);
			} else if (args[0].equals(TRANSFER_VTHO)) {
				TransactionConsole.transferVtho(args);
			} else if (args[0].equals(BALANCE)) {
				TransactionConsole.getBalance(args);
			} else if (args[0].equals(CHAIN_TAG)) {
				BlockchainQueryConsole.getChainTag();
			} else if (args[0].equals(GET_BLOCK)) {
				BlockchainQueryConsole.getBestBlock(args);
			} else if (args[0].equals(BLOCK_REF)) {
				BlockchainQueryConsole.getBestBlockRef();
			} else if (args[0].equals(CREATE_WALLET)) {
				System.out.println("This feature is deprecated");
				//WalletConsole.createWalletToKeystoreFile(args);
			} else if (args[0].equals(SEND)) {
				// args=signAndSendVET {providerUrl} {privateKey} {filePath}
				TransactionConsole.sendTransactionFromCSVFile(args, privateKey);
			} else if (args[0].equals(SEND_RAW)) {
				// args=sendVETRaw {providerUrl} {rawTransaction}
				TransactionConsole.sendRawTransaction(args);
			} else if (args[0].equals(PARSE)) {
				ParserConsole.parse(args);
			} else {
				System.out.println("不支持的操作命令");
			}
		} catch (Exception e) {
			System.out.println("操作失败 " + e.getMessage());
		}
	}

	/**
	 * Process console input arguments
	 * 
	 * @param args
	 *            input arguments
	 * @return
	 */
	private static String processConsoleArguments(String[] args) {
		String privateKey = null;
		String nodeProviderUrl = null;
		if (args[0].equals(CHAIN_TAG) || args[0].equals(BLOCK_REF) || args[0].equals(GET_BLOCK) || args[0].equals(SEND)
				|| args[0].equals(SEND_RAW) || args[0].equals(TRANSFER_VET) || args[0].equals(TRANSFER_VTHO)
				|| args[0].equals(BALANCE)) {

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
