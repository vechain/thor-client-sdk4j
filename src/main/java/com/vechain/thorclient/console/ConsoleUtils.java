package com.vechain.thorclient.console;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.vechain.thorclient.clients.BlockchainClient;
import com.vechain.thorclient.clients.TransactionClient;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.ERC20Contract;
import com.vechain.thorclient.core.model.clients.ERC20Token;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.core.model.clients.ToClause;
import com.vechain.thorclient.core.model.clients.ToData;
import com.vechain.thorclient.core.model.clients.base.AbstractToken;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.Prefix;
import com.vechain.thorclient.utils.RawTransactionFactory;
import com.vechain.thorclient.utils.StringUtils;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

public class ConsoleUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String doSignVETTx(List<String[]> transactions, String privateKey, boolean isSend)
            throws IOException {

        byte chainTag = 0;
        byte[] blockRef = null;

        List<ToClause> clauses = new ArrayList<ToClause>();
        for (String[] transaction : transactions) {
            Amount amount = Amount.createFromToken(AbstractToken.VET);
            amount.setDecimalAmount(transaction[1]);
            clauses.add(TransactionClient.buildVETToClause(Address.fromHexString(transaction[0]), amount, ToData.ZERO));
            chainTag = BytesUtils.toByteArray(transaction[2])[0];
            if (transaction[3] == null) {
                blockRef = BlockchainClient.getBlockRef(null).toByteArray();
            } else {
                blockRef = BytesUtils.toByteArray(transaction[3]);
            }
        }
        int gas = clauses.size() * 21000;
        RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(chainTag, blockRef,
                720, gas, (byte) 0x0, CryptoUtils.generateTxNonce(), clauses.toArray(new ToClause[0]));
        if (isSend) {
            TransferResult result = TransactionClient.signThenTransfer(rawTransaction, ECKeyPair.create(privateKey));
            return OBJECT_MAPPER.writeValueAsString(result);
        } else {
            RawTransaction result = TransactionClient.sign(rawTransaction, ECKeyPair.create(privateKey));
            return BytesUtils.toHexString(result.encode(), Prefix.ZeroLowerX);
        }
    }

    public static String sendRawVETTx(String rawTransaction) throws IOException {
        TransferResult result = TransactionClient.transfer(rawTransaction);
        return OBJECT_MAPPER.writeValueAsString(result);
    }

    public static List<String[]> readExcelFile(String fiePath) throws Exception {

        List<String[]> results = new ArrayList<String[]>();

        Workbook workbook = WorkbookFactory.create(new File(fiePath));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        sheet.forEach(row -> {
            int rowNum = row.getRowNum();
            if (rowNum > 1) {
                int length = row.getLastCellNum();
                List<String> rowData = new ArrayList<String>(length);
                row.forEach(cell -> {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    if (!StringUtils.isBlank(cellValue)) {
                        rowData.add(cellValue);
                    }
                });
                if (!rowData.isEmpty()) {
                    results.add(rowData.toArray(new String[0]));
                }
            }
        });
        workbook.close();
        return results;
    }

    public static String doSignVTHOTx(List<String[]> transactions, String privateKey, boolean isSend)
            throws IOException {
        return doSignVTHOTx(transactions, privateKey, isSend, null);
    }

    public static String doSignVTHOTx(List<String[]> transactions, String privateKey, boolean isSend, Integer gasLimit)
            throws IOException {

        byte chainTag = 0;
        byte[] blockRef = null;

        List<ToClause> clauses = new ArrayList<ToClause>();
        for (String[] transaction : transactions) {
            Amount amount = Amount.VTHO();
            amount.setDecimalAmount(transaction[1]);
            clauses.add(
                    ERC20Contract.buildTranferToClause(ERC20Token.VTHO, Address.fromHexString(transaction[0]), amount));
            chainTag = BytesUtils.toByteArray(transaction[2])[0];
            if (transaction[3] == null) {
                blockRef = BlockchainClient.getBlockRef(null).toByteArray();
            } else {
                blockRef = BytesUtils.toByteArray(transaction[3]);
            }
        }
        if (gasLimit == null) {
            gasLimit = 80000;
        }
        int gas = clauses.size() * gasLimit;
        RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(chainTag, blockRef,
                720, gas, (byte) 0x0, CryptoUtils.generateTxNonce(), clauses.toArray(new ToClause[0]));
        if (isSend) {
            TransferResult result = TransactionClient.signThenTransfer(rawTransaction, ECKeyPair.create(privateKey));
            return OBJECT_MAPPER.writeValueAsString(result);
        } else {
            RawTransaction result = TransactionClient.sign(rawTransaction, ECKeyPair.create(privateKey));
            return BytesUtils.toHexString(result.encode(), Prefix.ZeroLowerX);
        }
    }
}
