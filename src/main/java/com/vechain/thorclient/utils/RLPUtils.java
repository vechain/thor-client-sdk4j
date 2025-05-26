package com.vechain.thorclient.utils;

import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.core.model.clients.RawTransactionEIP1559;
import com.vechain.thorclient.core.model.clients.TransactionCommonalities;
import com.vechain.thorclient.core.model.clients.TransactionReserved;
import com.vechain.thorclient.utils.rlp.*;


import java.util.ArrayList;
import java.util.List;

/**
 * RLP encoding utility
 */
public class RLPUtils {
    private final static int CLAUSE_TO = 0;
    private final static int CLAUSE_VALUE = 1;
    private final static int CLAUSE_DATA = 2;

    private final static int EIP1559_CHAIN_TAG = 0;
    private final static int EIP1559_BLOCK_REF = 1;
    private final static int EIP1559_EXPIRATION = 2;
    private final static int EIP1559_CLAUSES = 3;
    private final static int EIP1559_MAX_PRIORITY_FEE_PER_GAS = 4;
    private final static int EIP1559_MAX_FEE_PER_GAS = 5;
    private final static int EIP1559_GAS = 6;
    private final static int EIP1559_DEPENDS_ON = 7;
    private final static int EIP1559_NONCE = 8;
    private final static int EIP1559_RESERVED = 9;
    private final static int EIP1559_SIGNATURE = 10;

    private final static int LEGACY_CHAIN_TAG = 0;
    private final static int LEGACY_BLOCK_REF = 1;
    private final static int LEGACY_EXPIRATION = 2;
    private final static int LEGACY_CLAUSES = 3;
    private final static int LEGACY_GAS_PRICE_COEF = 4;
    private final static int LEGACY_GAS = 5;
    private final static int LEGACY_DEPENDS_ON = 6;
    private final static int LEGACY_NONCE = 7;
    private final static int LEGACY_RESERVED = 8;
    private final static int LEGACY_SIGNATURE = 9;

    public static byte[] encodeRawTransaction(RawTransaction rawTx) {
        List<RlpType> values = asRlpValues(rawTx);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    public static byte[] encodeRawTransaction(RawTransactionEIP1559 rawTxEIP1559) {
        List<RlpType> values = asRlpValues(rawTxEIP1559);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    private static List<RlpType> asRlpValues(RawTransaction rawTx) {
        final List<RlpType> result = new ArrayList<>();
        rlpValuesOf(result, rawTx.getChainTag(), rawTx.getBlockRef(), rawTx.getExpiration());
        result.add(new RlpList(buildRlpClausesList(rawTx.getClauses())));
        if (rawTx.getGasPriceCoef() == 0) {
            result.add(RlpString.create(RlpString.EMPTY));
        } else {
            result.add(RlpString.create(rawTx.getGasPriceCoef()));
        }
        return rlpValuesOf(result, rawTx.getGas(), rawTx.getDependsOn(), rawTx.getNonce(), rawTx.getReserved(), rawTx.getSignature());
    }

    private static List<RlpType> asRlpValues(RawTransactionEIP1559 rawTxEIP1559) {
        final List<RlpType> result = new ArrayList<>();
        rlpValuesOf(result, rawTxEIP1559.getChainTag(), rawTxEIP1559.getBlockRef(), rawTxEIP1559.getExpiration());
        result.add(new RlpList(buildRlpClausesList(rawTxEIP1559.getClauses())));
        if (rawTxEIP1559.getMaxPriorityFeePerGas() == null) {
            throw new IllegalArgumentException("getMaxPriorityFeePerGas is null");
        }
        result.add(RlpString.create(rawTxEIP1559.getMaxPriorityFeePerGas()));
        if (rawTxEIP1559.getMaxFeePerGas() == null) {
            throw new IllegalArgumentException("getMaxFeePerGas is null");
        }
        result.add(RlpString.create(rawTxEIP1559.getMaxFeePerGas()));
        return rlpValuesOf(result, rawTxEIP1559.getGas(), rawTxEIP1559.getDependsOn(), rawTxEIP1559.getNonce(), rawTxEIP1559.getReserved(), rawTxEIP1559.getSignature());
    }

    private static void rlpValuesOf(final List<RlpType> result, final byte chainTag, final byte[] blockRef, final byte[] expiration) {
        if (chainTag == 0) {
            throw new IllegalArgumentException("getChainTag is null");
        }
        result.add(RlpString.create(chainTag));
        if (blockRef == null) {
            throw new IllegalArgumentException("getBlockRef is null");
        }
        result.add(RlpString.create(blockRef));
        if (expiration == null) {
            throw new IllegalArgumentException("getExpiration is null");
        }
        result.add(RlpString.create(expiration));
    }

    private static List<RlpType> rlpValuesOf(final List<RlpType> result, final byte[] gas, final byte[] dependsOn, final byte[] nonce, final TransactionReserved reserved, final byte[] signature) {
        if (gas == null) {
            throw new IllegalArgumentException("getGas is null");
        }
        result.add(RlpString.create(gas));
        if (dependsOn == null) {
            result.add(RlpString.create(RlpString.EMPTY));
        } else {
            result.add(RlpString.create(dependsOn));
        }
        if (nonce == null) {
            throw new IllegalArgumentException("getNonce is null");
        }
        result.add(RlpString.create(nonce));
        if (reserved == null) {
            result.add(new RlpList(new ArrayList<>()));
        } else {
            final List<RlpType> reservedRlpList = new ArrayList<>();
            for (byte[] reservedValue : reserved.getReservedValues()) {
                reservedRlpList.add(RlpString.create(reservedValue));
            }
            result.add(new RlpList(reservedRlpList));
        }
        if (signature != null) {
            result.add(RlpString.create(signature));
        }
        return result;
    }

    private static List<RlpType> buildRlpClausesList(RawClause[] rawClauses) {
        final List<RlpType> clauses = new ArrayList<>();
        for (RawClause clause : rawClauses) {
            final List<RlpType> rlpClause = new ArrayList<>();
            if (clause.getTo() == null) {
                rlpClause.add(RlpString.create(RlpString.EMPTY));
            } else {
                rlpClause.add(RlpString.create(clause.getTo()));
            }
            if (clause.getValue() == null) {
                rlpClause.add(RlpString.create(RlpString.EMPTY));
            } else {
                rlpClause.add(RlpString.create(clause.getValue()));
            }
            if (clause.getData() == null) {
                rlpClause.add(RlpString.create(RlpString.EMPTY));
            } else {
                rlpClause.add(RlpString.create(clause.getData()));
            }
            RlpList clauseRLP = new RlpList(rlpClause);
            clauses.add(clauseRLP);
        }
        return clauses;
    }

    private static List<RlpType> decodeCommonalities(final String hexRawTransaction) {
        if (!StringUtils.isHex(hexRawTransaction)) {
            return null;
        }
        final byte[] rawTxBytes = BytesUtils.toByteArray(hexRawTransaction);
        final RlpList list = RlpDecoder.decode(rawTxBytes);
        final List<RlpType> rlpContent = list.getValues();
        // It should only have one element.
        if (rlpContent.size() != 1) {
            return null;
        }
        return rlpContent;
    }

    /**
     * Decode hex string
     *
     * @param hexRawTransaction hex raw legacy transaction
     * @return RawTransaction
     */
    public static RawTransaction decode(final String hexRawTransaction) {
        final List<RlpType> rlpContent = decodeCommonalities(hexRawTransaction);
        final RawTransaction rawTx = new RawTransaction();
        final List<RlpType> listValues = ((RlpList) rlpContent.get(0)).getValues();
        for (int index = 0; index < listValues.size(); index++) {
            fillTransactionLegacy(rawTx, listValues, index);
        }
        return rawTx;
    }

    /**
     * Decode hex string
     *
     * @param hexRawTransaction hex raw EIP1559 transaction
     * @return RawTransactionEIP1559
     */
    public static RawTransactionEIP1559 decodeEIP1559(final String hexRawTransaction) {
        final List<RlpType> rlpContent = decodeCommonalities(hexRawTransaction);
        final RawTransactionEIP1559 rawTx = new RawTransactionEIP1559();
        final List<RlpType> listValues = ((RlpList) rlpContent.get(0)).getValues();
        for (int index = 0; index < listValues.size(); index++) {
            fillTransactionEIP1559(rawTx, listValues, index);
        }
        return rawTx;
    }

    private static void fillTransactionEIP1559(final RawTransactionEIP1559 rawTx, final List listValues, final int index) {
        RlpString rlpString;
        RlpList clauseList;
        switch (index) {
            case EIP1559_CHAIN_TAG:
                rlpString = (RlpString) listValues.get(index);
                rawTx.setChainTag(rlpString.getBytes()[0]);
                break;
            case EIP1559_BLOCK_REF:
                rlpString = (RlpString) listValues.get(index);
                rawTx.setBlockRef(rlpString.getBytes());
                break;
            case EIP1559_EXPIRATION:
                rlpString = (RlpString) listValues.get(index);
                rawTx.setExpiration(rlpString.getBytes());
                break;
            case EIP1559_CLAUSES:
                clauseList = (RlpList) listValues.get(index);
                fillClausesLegacy(rawTx, clauseList);
                break;
            case EIP1559_MAX_PRIORITY_FEE_PER_GAS:
                rlpString = (RlpString) listValues.get(index);
                rawTx.setMaxPriorityFeePerGas(rlpString.getBytes());
                break;
            case EIP1559_MAX_FEE_PER_GAS:
                rlpString = (RlpString) listValues.get(index);
                rawTx.setMaxFeePerGas(rlpString.getBytes());
                break;
            case EIP1559_GAS:
                rlpString = (RlpString) listValues.get(index);
                rawTx.setGas(rlpString.getBytes());
                break;
            case EIP1559_DEPENDS_ON:
                rlpString = (RlpString) listValues.get(index);
                if (rlpString.getBytes().length == 0) {
                    rawTx.setDependsOn(null);
                } else {
                    rawTx.setDependsOn(rlpString.getBytes());
                }
                break;
            case EIP1559_NONCE:
                rlpString = (RlpString) listValues.get(index);
                rawTx.setNonce(rlpString.getBytes());
                break;
            case EIP1559_RESERVED:
                RlpList rlpList = (RlpList) listValues.get(index);
                fillReserved(rlpList, rawTx);
                break;
            case EIP1559_SIGNATURE:
                rlpString = (RlpString) listValues.get(index);
                rawTx.setSignature(rlpString.getBytes());
                break;
        }
    }

    private static void fillTransactionLegacy(final RawTransaction rawTx, final List listValues, final int index) {
        RlpString rlpString;
        RlpList clauseList;
        switch (index) {
            case LEGACY_CHAIN_TAG:
                rlpString = (RlpString) listValues.get(index);
                rawTx.setChainTag(rlpString.getBytes()[0]);
                break;
            case LEGACY_BLOCK_REF:
                rlpString = (RlpString) listValues.get(index);
                rawTx.setBlockRef(rlpString.getBytes());
                break;
            case LEGACY_EXPIRATION:
                rlpString = (RlpString) listValues.get(index);
                rawTx.setExpiration(rlpString.getBytes());
                break;
            case LEGACY_CLAUSES:
                clauseList = (RlpList) listValues.get(index);
                fillClausesLegacy(rawTx, clauseList);
                break;
            case LEGACY_GAS_PRICE_COEF:
                rlpString = (RlpString) listValues.get(index);
                if (rlpString.getBytes().length == 0) {
                    rawTx.setGasPriceCoef((byte) 0);
                } else {
                    rawTx.setGasPriceCoef(rlpString.getBytes()[0]);
                }
                break;
            case LEGACY_GAS:
                rlpString = (RlpString) listValues.get(index);
                rawTx.setGas(rlpString.getBytes());
                break;
            case LEGACY_DEPENDS_ON:
                rlpString = (RlpString) listValues.get(index);
                if (rlpString.getBytes().length == 0) {
                    rawTx.setDependsOn(null);
                } else {
                    rawTx.setDependsOn(rlpString.getBytes());
                }
                break;
            case LEGACY_NONCE:
                rlpString = (RlpString) listValues.get(index);
                rawTx.setNonce(rlpString.getBytes());
                break;
            case LEGACY_RESERVED:
                RlpList rlpList = (RlpList) listValues.get(index);
                fillReserved(rlpList, rawTx);
                break;
            case LEGACY_SIGNATURE:
                rlpString = (RlpString) listValues.get(index);
                rawTx.setSignature(rlpString.getBytes());
                break;
        }
    }

    private static void fillReserved(RlpList rlpList, TransactionCommonalities rawTx) {
        List<RlpType> rlpTypeList = rlpList.getValues();
        TransactionReserved transactionReserved = new TransactionReserved();
        for (RlpType rlpType : rlpTypeList) {
            RlpString reservedRlpString = (RlpString) rlpType;
            byte[] reservedBytes = reservedRlpString.getBytes();
            transactionReserved.getReservedValues().add(reservedBytes);
        }
        rawTx.setReserved(transactionReserved);
    }

    private static void fillClausesLegacy(TransactionCommonalities rawTx, RlpList list) {
        final List<RlpType> clauses = list.getValues();
        final int clausesSize = clauses.size();
        final RawClause[] rawClause = new RawClause[clausesSize];
        rawTx.setClauses(rawClause);
        for (int clauseIndex = 0; clauseIndex < clausesSize; clauseIndex++) {
            final List<RlpType> clauseContent = ((RlpList) clauses.get(clauseIndex)).getValues();
            rawClause[clauseIndex] = new RawClause();
            fillOneClause(rawClause, clauseIndex, clauseContent);
        }
    }

    private static void fillOneClause(final RawClause[] rawClause, final int clauseIndex, final List<RlpType> clauseContent) {
        for (int index = 0; index < clauseContent.size(); index++) {
            RlpString clause = (RlpString) clauseContent.get(index);
            switch (index) {
                case CLAUSE_TO:
                    rawClause[clauseIndex].setTo(clause.getBytes());
                    break;
                case CLAUSE_VALUE:
                    rawClause[clauseIndex].setValue(clause.getBytes());
                    break;
                case CLAUSE_DATA:
                    rawClause[clauseIndex].setData(clause.getBytes());
                    break;
            }
        }
    }

}
