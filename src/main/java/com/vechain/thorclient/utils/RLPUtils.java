package com.vechain.thorclient.utils;

import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.core.model.clients.TransactionReserved;
import com.vechain.thorclient.utils.rlp.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * RLP encoding utility
 */
public class RLPUtils {
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

    private final static int CLAUSE_TO = 0;
    private final static int CLAUSE_VALUE = 1;
    private final static int CLAUSE_DATA = 2;


    public static byte[] encodeRawTransaction(final RawTransaction rawTransaction) {
        if (rawTransaction.isEIP1559()) {
            final byte[] encoded = RlpEncoder.encode(new RlpList(asRlpValuesEIP1559(rawTransaction)));
            final byte[] prefixed = new byte[encoded.length + 1];
            prefixed[0] = RawTransaction.EIP1559;
            System.arraycopy(encoded, 0, prefixed, 1, encoded.length);
            return prefixed;
        }
        return RlpEncoder.encode(new RlpList(asRlpValuesLegacy(rawTransaction)));
    }

    private static List<RlpType> asRlpValuesEIP1559(final RawTransaction rawTransaction) {
        final List<RlpType> result = asRlpValuesinCommon(rawTransaction);

        if (rawTransaction.getMaxPriorityFeePerGas() == null) {
            throw new IllegalArgumentException("getMaxPriorityFeePerGas is null");
        }
        result.add(RlpString.create(rawTransaction.getMaxPriorityFeePerGas()));

        if (rawTransaction.getMaxFeePerGas() == null) {
            throw new IllegalArgumentException("getMaxFeePerGas is null");
        }
        result.add(RlpString.create(rawTransaction.getMaxFeePerGas()));

        if (rawTransaction.getGas() == null) {
            throw new IllegalArgumentException("getGas is null");
        }
        result.add(RlpString.create(rawTransaction.getGas()));

        if (rawTransaction.getDependsOn() == null) {
            result.add(RlpString.create(RlpString.EMPTY));
        } else {
            result.add(RlpString.create(rawTransaction.getDependsOn()));
        }

        if (rawTransaction.getNonce() == null) {
            throw new IllegalArgumentException("getNonce is null");
        }
        result.add(RlpString.create(rawTransaction.getNonce()));

        if (rawTransaction.getReserved() == null) {
            List<RlpType> reservedRlp = new ArrayList<>();
            RlpList reservedList = new RlpList(reservedRlp);
            result.add(reservedList);
        } else {
            List<RlpType> reservedRlpList = new ArrayList<>();
            for (byte[] reservedValue : rawTransaction.getReserved().getReservedValues()) {
                reservedRlpList.add(RlpString.create(reservedValue));
            }
            RlpList reservedList = new RlpList(reservedRlpList);
            result.add(reservedList);
        }

        if (rawTransaction.getSignature() != null) {
            result.add(RlpString.create(rawTransaction.getSignature()));
        }
        return result;
    }

    private static List<RlpType> asRlpValuesinCommon(final RawTransaction rawTransaction) {
        final List<RlpType> result = new ArrayList<>();

        if (rawTransaction.getChainTag() == 0) {
            throw new IllegalArgumentException("getChainTag is null");
        }
        result.add(RlpString.create(rawTransaction.getChainTag()));

        if (rawTransaction.getBlockRef() == null) {
            throw new IllegalArgumentException("getBlockRef is null");
        }
        result.add(RlpString.create(rawTransaction.getBlockRef()));

        if (rawTransaction.getExpiration() == null) {
            throw new IllegalArgumentException("getExpiration is null");
        }
        result.add(RlpString.create(rawTransaction.getExpiration()));

        final List<RlpType> clauses = buildRlpClausesLIst(rawTransaction);
        final RlpList rlpList = new RlpList(clauses);
        result.add(rlpList);
        return result;
    }

    private static List<RlpType> asRlpValuesLegacy(final RawTransaction rawTransaction) {
        final List<RlpType> result = asRlpValuesinCommon(rawTransaction);

        if (rawTransaction.getGasPriceCoef() == null || rawTransaction.getGasPriceCoef() == 0) {
            result.add(RlpString.create(RlpString.EMPTY));
        } else {
            result.add(RlpString.create(rawTransaction.getGasPriceCoef()));
        }

        if (rawTransaction.getGas() == null) {
            throw new IllegalArgumentException("getGas is null");
        }
        result.add(RlpString.create(rawTransaction.getGas()));

        if (rawTransaction.getDependsOn() == null) {
            result.add(RlpString.create(RlpString.EMPTY));
        } else {
            result.add(RlpString.create(rawTransaction.getDependsOn()));
        }

        if (rawTransaction.getNonce() == null) {
            throw new IllegalArgumentException("getNonce is null");
        }
        result.add(RlpString.create(rawTransaction.getNonce()));

        if (rawTransaction.getReserved() == null) {
            List<RlpType> reservedRlp = new ArrayList<>();
            RlpList reservedList = new RlpList(reservedRlp);
            result.add(reservedList);
        } else {
            List<RlpType> reservedRlpList = new ArrayList<>();
            for (byte[] reservedValue : rawTransaction.getReserved().getReservedValues()) {
                reservedRlpList.add(RlpString.create(reservedValue));
            }
            RlpList reservedList = new RlpList(reservedRlpList);
            result.add(reservedList);
        }

        if (rawTransaction.getSignature() != null) {
            result.add(RlpString.create(rawTransaction.getSignature()));
        }
        return result;
    }

    private static List<RlpType> buildRlpClausesLIst(final RawTransaction rawTransaction) {
        final List<RlpType> clauses = new ArrayList<>();

        for (final RawClause clause : rawTransaction.getClauses()) {

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
            final RlpList clauseRLP = new RlpList(rlpClause);
            clauses.add(clauseRLP);
        }
        return clauses;
    }

    /**
     * Decode hex string
     *
     * @param hexRawTransaction hex raw transaction
     * @return
     */
    public static RawTransaction decode(final String hexRawTransaction) {
        if (!StringUtils.isHex(hexRawTransaction)) {
            return null;
        }
        final byte[] rawTxBytes = BytesUtils.toByteArray(hexRawTransaction);
        if (rawTxBytes[0] == RawTransaction.EIP1559) {
            return decode(Arrays.copyOfRange(rawTxBytes, 1, rawTxBytes.length), true);
        }
        return decode(rawTxBytes, false);
    }

    private static RawTransaction decode(final byte[] rawTxBytes, boolean isEIP1559) {
        final RlpList list = RlpDecoder.decode(rawTxBytes);
        final List<RlpType> rlpContent = list.getValues();
        //It should only have one element.
        if (rlpContent.size() != 1) {
            return null;
        }
        final RawTransaction rawTransaction = new RawTransaction();
        final List listValues = ((RlpList) rlpContent.get(0)).getValues();
        if (isEIP1559) {
            for (int index = 0; index < listValues.size(); index++) {
                fillTransactionEIP1559(rawTransaction, listValues, index);
            }
        } else {
            for (int index = 0; index < listValues.size(); index++) {
                fillTransactionLegacy(rawTransaction, listValues, index);
            }
        }
        return rawTransaction;
    }

    private static void fillTransactionEIP1559(RawTransaction rawTransaction, List listValues, int index) {
        RlpString rlpString;
        RlpList clauseList;
        switch (index) {
            case EIP1559_CHAIN_TAG:
                rlpString = (RlpString) listValues.get(index);
                rawTransaction.setChainTag(rlpString.getBytes()[0]);
                break;
            case EIP1559_BLOCK_REF:
                rlpString = (RlpString) listValues.get(index);
                rawTransaction.setBlockRef(rlpString.getBytes());
                break;
            case EIP1559_EXPIRATION:
                rlpString = (RlpString) listValues.get(index);
                rawTransaction.setExpiration(rlpString.getBytes());
                break;
            case EIP1559_CLAUSES:
                clauseList = (RlpList) listValues.get(index);
                fillClauses(rawTransaction, clauseList);
                break;
            case EIP1559_MAX_PRIORITY_FEE_PER_GAS:
                rlpString = (RlpString) listValues.get(index);
                rawTransaction.setMaxPriorityFeePerGas(rlpString.getBytes());
                break;
            case EIP1559_MAX_FEE_PER_GAS:
                rlpString = (RlpString) listValues.get(index);
                rawTransaction.setMaxFeePerGas(rlpString.getBytes());
                break;
            case EIP1559_GAS:
                rlpString = (RlpString) listValues.get(index);
                rawTransaction.setGas(rlpString.getBytes());
                break;
            case EIP1559_DEPENDS_ON:
                rlpString = (RlpString) listValues.get(index);
                if (rlpString.getBytes().length == 0) {
                    rawTransaction.setDependsOn(null);
                } else {
                    rawTransaction.setDependsOn(rlpString.getBytes());
                }
                break;
            case EIP1559_NONCE:
                rlpString = (RlpString) listValues.get(index);
                rawTransaction.setNonce(rlpString.getBytes());
                break;
            case EIP1559_RESERVED:
                RlpList rlpList = (RlpList) listValues.get(index);
                fillReserved(rlpList, rawTransaction);
                break;
            case EIP1559_SIGNATURE:
                rlpString = (RlpString) listValues.get(index);
                rawTransaction.setSignature(rlpString.getBytes());
                break;
        }
    }


    private static void fillTransactionLegacy(RawTransaction rawTransaction, List listValues, int index) {
        RlpString rlpString;
        RlpList clauseList;
        switch (index) {
            case LEGACY_CHAIN_TAG:
                rlpString = (RlpString) listValues.get(index);
                rawTransaction.setChainTag(rlpString.getBytes()[0]);
                break;
            case LEGACY_BLOCK_REF:
                rlpString = (RlpString) listValues.get(index);
                rawTransaction.setBlockRef(rlpString.getBytes());
                break;
            case LEGACY_EXPIRATION:
                rlpString = (RlpString) listValues.get(index);
                rawTransaction.setExpiration(rlpString.getBytes());
                break;
            case LEGACY_CLAUSES:
                clauseList = (RlpList) listValues.get(index);
                fillClauses(rawTransaction, clauseList);
                break;
            case LEGACY_GAS_PRICE_COEF:
                rlpString = (RlpString) listValues.get(index);
                if (rlpString.getBytes().length == 0) {
                    rawTransaction.setGasPriceCoef((byte) 0);
                } else {
                    rawTransaction.setGasPriceCoef(rlpString.getBytes()[0]);
                }
                break;
            case LEGACY_GAS:
                rlpString = (RlpString) listValues.get(index);
                rawTransaction.setGas(rlpString.getBytes());
                break;
            case LEGACY_DEPENDS_ON:
                rlpString = (RlpString) listValues.get(index);
                if (rlpString.getBytes().length == 0) {
                    rawTransaction.setDependsOn(null);
                } else {
                    rawTransaction.setDependsOn(rlpString.getBytes());
                }
                break;
            case LEGACY_NONCE:
                rlpString = (RlpString) listValues.get(index);
                rawTransaction.setNonce(rlpString.getBytes());
                break;
            case LEGACY_RESERVED:
                RlpList rlpList = (RlpList) listValues.get(index);
                fillReserved(rlpList, rawTransaction);
                break;
            case LEGACY_SIGNATURE:
                rlpString = (RlpString) listValues.get(index);
                rawTransaction.setSignature(rlpString.getBytes());
                break;
        }
    }

    private static void fillReserved(RlpList rlpList, RawTransaction rawTransaction) {

        List<RlpType> rlpTypeList = rlpList.getValues();
        TransactionReserved transactionReserved = new TransactionReserved();
        for (RlpType rlpType : rlpTypeList) {
            RlpString reservedRlpString = (RlpString) rlpType;
            byte[] reservedBytes = reservedRlpString.getBytes();
            transactionReserved.getReservedValues().add(reservedBytes);
        }
        rawTransaction.setReserved(transactionReserved);
    }

    private static void fillClauses(RawTransaction rawTransaction, RlpList list) {
        List clauses = (List) list.getValues();
        int clausesSize = clauses.size();
        RawClause[] rawClause = new RawClause[clausesSize];
        rawTransaction.setClauses(rawClause);
        for (int clauseIndex = 0; clauseIndex < clausesSize; clauseIndex++) {
            List<RlpType> clauseContent = ((RlpList) clauses.get(clauseIndex)).getValues();
            rawClause[clauseIndex] = new RawClause();
            fillOneClause(rawClause, clauseIndex, clauseContent);
        }
    }

    private static void fillOneClause(RawClause[] rawClause, int clauseIndex, List<RlpType> clauseContent) {
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
