package com.vechain.thorclient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vechain.thorclient.utils.*;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.clients.TransactionClient;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.core.model.clients.ToClause;
import com.vechain.thorclient.core.model.clients.ToData;
import com.vechain.thorclient.core.model.clients.base.AbstractToken;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

public class ConsoleUtils {

    public static String doSignVETTx(List<String[]> transactions, String privateKey, boolean isSend) throws IOException {

        byte chainTag = 0;
        byte[] blockRef = null;

        List<ToClause> clauses = new ArrayList<ToClause>();
        for (String[] transaction : transactions) {
            Amount amount = Amount.createFromToken(AbstractToken.VET);
            amount.setDecimalAmount(transaction[1]);
            clauses.add(TransactionClient.buildVETToClause(Address.fromHexString(transaction[0]), amount, ToData.ZERO));
            chainTag = BytesUtils.toByteArray(transaction[2])[0];
            blockRef = BytesUtils.toByteArray(transaction[3]);
        }
        int gas = clauses.size() * 21000;
        RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(chainTag, blockRef, 720, gas, (byte) 0x01, CryptoUtils.generateTxNonce(),
                clauses.toArray(new ToClause[0]));
        if (isSend) {
            TransferResult result = TransactionClient.signThenTransfer(rawTransaction, ECKeyPair.create(privateKey));
            return JSON.toJSONString(result);
        } else {
            RawTransaction result = TransactionClient.sign(rawTransaction, ECKeyPair.create(privateKey));
            return BytesUtils.toHexString(result.encode(), Prefix.ZeroLowerX);
        }
    }

    public static List<String[]> readExcelFile(String fiePath) throws Exception {

        List<String[]> results = new ArrayList<String[]>();

        Workbook workbook = WorkbookFactory.create(new File(fiePath));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        sheet.forEach(row -> {
            int rowNum = row.getRowNum();
            if (rowNum > 2) {
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
}
