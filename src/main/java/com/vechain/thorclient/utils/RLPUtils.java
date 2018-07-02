package com.vechain.thorclient.utils;

import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.utils.rlp.RlpEncoder;
import com.vechain.thorclient.utils.rlp.RlpList;
import com.vechain.thorclient.utils.rlp.RlpString;
import com.vechain.thorclient.utils.rlp.RlpType;


import java.util.ArrayList;
import java.util.List;

/**
 * RLP encoding utility
 */
public class RLPUtils {
    public static byte[] encodeRawTransaction(RawTransaction rawTransaction){
        List<RlpType> values = asRlpValues(rawTransaction);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    private static List<RlpType> asRlpValues(RawTransaction rawTransaction){
        List<RlpType> result = new ArrayList<>();
        if(rawTransaction.getChainTag() == 0){
            throw new IllegalArgumentException("getChainTag is null");
        }
        result.add(RlpString.create(rawTransaction.getChainTag()));

        if(rawTransaction.getBlockRef() == null){
            throw new IllegalArgumentException("getBlockRef is null");
        }
        result.add(RlpString.create(rawTransaction.getBlockRef()));

        if(rawTransaction.getExpiration() == null){
            throw new IllegalArgumentException("getExpiration is null");
        }
        result.add(RlpString.create(rawTransaction.getExpiration()));

        List<RlpType> clauses = buildRlpClausesLIst( rawTransaction );
        RlpList rlpList = new RlpList(clauses);
        result.add(rlpList);

        if(rawTransaction.getGasPriceCoef() == 0){
            result.add(RlpString.create(new byte[]{}));
        }else{
            result.add( RlpString.create( rawTransaction.getGasPriceCoef() ) );
        }

        if(rawTransaction.getGas() == null){
            throw new IllegalArgumentException("getGas is null");
        }
        result.add(RlpString.create(rawTransaction.getGas()));

        if(rawTransaction.getDependsOn() == null){
            throw new IllegalArgumentException("getDependsOn is null");
        }
        result.add(RlpString.create(rawTransaction.getDependsOn()));

        if(rawTransaction.getNonce() == null){
            throw new IllegalArgumentException("getNonce is null");
        }
        result.add(RlpString.create(rawTransaction.getNonce()));

        if(rawTransaction.getReserved() == null) {
            List<RlpType> reservedRlp = new ArrayList<>();
            RlpList reservedList = new RlpList(reservedRlp);
            result.add(reservedList);
        }else{
            throw new IllegalArgumentException("reservedList is not supported");
        }

        if(rawTransaction.getSignature() != null) {
            result.add(RlpString.create(rawTransaction.getSignature()));
        }
        return result;

    }

    private static List<RlpType> buildRlpClausesLIst(RawTransaction rawTransaction) {
        List<RlpType> clauses = new ArrayList<>();

        for (RawClause clause: rawTransaction.getClauses()){

            List<RlpType> rlpClause = new ArrayList<>();
            if(clause.getTo() == null){
                throw new IllegalArgumentException("getTo is null");
            }
            rlpClause.add( RlpString.create(clause.getTo()));

            if(clause.getValue() == null){
                throw new IllegalArgumentException("getValue is null");
            }
            rlpClause.add(RlpString.create(clause.getValue()));

            if(clause.getData() == null){
                throw new IllegalArgumentException("getData is null");
            }
            rlpClause.add(RlpString.create(clause.getData()));
            RlpList clauseRLP = new RlpList(rlpClause);
            clauses.add(clauseRLP);
        }
        return clauses;
    }

}
