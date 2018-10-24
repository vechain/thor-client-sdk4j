package com.vechain.thorclient.console;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.clients.TransactionClient;
import com.vechain.thorclient.core.model.blockchain.NodeProvider;
import com.vechain.thorclient.core.model.blockchain.Receipt;
import com.vechain.thorclient.core.model.blockchain.Transaction;
import com.vechain.thorclient.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransactionConsole {

	/**
	 * args: [getTransactionRecipient] [txId] [node url]
	 * 
	 * @param args
	 */
	public static void getTransactionRecipient(String[] args) {

		if (args.length < 3 || StringUtils.isBlank(args[2])) {
			System.out.println("You have input invalid parameters.");
			System.exit(0);
		}
		String txId = args[1];
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

	public static void getTransaction(String[] args) {
		if (args.length < 3 || StringUtils.isBlank(args[2])) {
			System.out.println("You have input invalid parameters.");
			System.exit(0);
		}
		String txId = args[1];
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

	public static void sendRawTransaction(String[] args) throws IOException {
		if (args.length < 3) {
			System.out.println("You have input invalid parameters.");
			System.exit(0);
		}
		String result = ConsoleUtils.sendRawVETTx(args[2]);
		System.out.println("Send Result:");
		System.out.println(result);
	}

	public static void sendTransactionFromCSVFile(String[] args, String privateKey) throws Exception {
		if (args.length < 4) {
			System.out.println("You have input invalid parameters.");
			System.exit(0);
		}
		File file = new File(args[3]);
		if (file.isFile()) {
			List<String[]> transactionList = ConsoleUtils.readExcelFile(args[3]);
			String result = ConsoleUtils.doSignVETTx(transactionList, privateKey, true);
			System.out.println("Send Result:");
			System.out.println(result);
		} else {
			System.out.println("You have input invalid parameters.");
		}
	}

	public static void signVETTxn(String[] args) throws Exception {
		String privateKey;// args=sign filePath privateKey
		if (args.length < 3 || StringUtils.isBlank(args[2])) {
			System.out.println("You have input invalid parameters.");
			System.exit(0);
		}
		privateKey = args[2];
		File file = new File(args[1]);
		if (file.isFile()) {
			List<String[]> transactionList = ConsoleUtils.readExcelFile(args[1]);
			String rawTransaction = ConsoleUtils.doSignVETTx(transactionList, privateKey, false);
			System.out.println("Raw Transaction:");
			System.out.println(rawTransaction);
		} else {
			System.out.println("You have input invalid parameters.");
		}
	}

	public static void signVTHOTxn(String[] args) throws Exception {
		String privateKey;// args=sign filePath privateKey
		if (args.length < 3 || StringUtils.isBlank(args[2])) {
			System.out.println("You have input invalid parameters.");
			System.exit(0);
		}
		privateKey = args[2];
		File file = new File(args[1]);
		if (file.isFile()) {
			List<String[]> transactionList = ConsoleUtils.readExcelFile(args[1]);
			String rawTransaction = ConsoleUtils.doSignVTHOTx(transactionList, privateKey, false);
			System.out.println("Raw Transaction:");
			System.out.println(rawTransaction);
		} else {
			System.out.println("You have input invalid parameters.");
		}
	}

	/**
	 * transferVet
	 * 
	 * @param args
	 *            server-url to amount chainTag privateKey
	 * @throws Exception
	 */
	public static void transferVet(String[] args) throws Exception {
		String privateKey;
		if (args.length < 5) {
			System.out.println("You have input invalid parameters.");
			System.exit(0);
		}
		privateKey = args[4];

		List<String[]> transactionList = new ArrayList<String[]>();
		String[] tranfs = new String[4];
		tranfs[0] = args[1];
		tranfs[1] = args[2];
		tranfs[2] = args[3];
		tranfs[3] = null;

		NodeProvider nodeProvider = NodeProvider.getNodeProvider();
		nodeProvider.setProvider(args[0]);
		String result = ConsoleUtils.doSignVETTx(transactionList, privateKey, true);
		System.out.println(result);

	}

	/**
	 * transferVet
	 * 
	 * @param args
	 *            server-url to amount chainTag privateKey
	 * @throws Exception
	 */
	public static void transferVtho(String[] args) throws Exception {
		String privateKey;
		if (args.length < 5) {
			System.out.println("You have input invalid parameters.");
			System.exit(0);
		}
		privateKey = args[4];

		List<String[]> transactionList = new ArrayList<String[]>();
		String[] tranfs = new String[4];
		tranfs[0] = args[1];
		tranfs[1] = args[2];
		tranfs[2] = args[3];
		tranfs[3] = null;

		NodeProvider nodeProvider = NodeProvider.getNodeProvider();
		nodeProvider.setProvider(args[0]);
		String result = ConsoleUtils.doSignVTHOTx(transactionList, privateKey, true);
		System.out.println(result);
	}
}
