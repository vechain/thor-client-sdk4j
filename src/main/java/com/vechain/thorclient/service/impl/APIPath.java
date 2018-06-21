package com.vechain.thorclient.service.impl;

class APIPath {

    public static String getBlockPath(){
        return "/blocks/{revision}";

    }

    public static String getTransactionPath(){
        return "/transactions/{id}";

    }

    public static String getTransactionReceiptPath(){
        return "/transactions/{id}/receipt";
    }


    public static String getSendingTransactionPath(){
        return "/transactions";
    }

    public static String getTransferPath(){
        return "/transfers";
    }

    public static String getEventPath(){
        return "/events";
    }

    public static String getNodePath(){
        return "/node/network/peers";
    }

    public static String getBalanceAccountPath(){
        return "/accounts/{address}";
    }

    public static String getStorageAccountPath(){
        return "/accounts/{address}/storage/{key}";
    }

    public static String getContractCallPath(){
        return "/accounts/{address}";
    }



}
