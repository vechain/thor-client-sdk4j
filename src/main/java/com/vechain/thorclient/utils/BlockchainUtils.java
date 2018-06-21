package com.vechain.thorclient.utils;


import java.util.regex.Pattern;

/**
 *
 */
public class BlockchainUtils {

    /**
     * Check if the block revision is valid.
     * @param blockRevision
     * @return
     */
    public static boolean isValidBlockRevision(String blockRevision){

        String blockNumPattern = "^[0-9]\\d*$";

        /**
         * block revision illegal
         */
        if(blockRevision == null || StringUtils.isBlank(blockRevision)){
            return false;
        }

        //block revision
        if((blockRevision.startsWith(Prefix.ZeroLowerX.getPrefixString()))
                &&blockRevision.length() == 66){
            return true;
        }else if(Pattern.matches(blockNumPattern, blockRevision)){
            return true;
        }else if("best".equalsIgnoreCase(blockRevision)){
            return true;
        }
        return false;
    }

    /**
     * Check if the address hex string is valid.
     * @param address
     * @return
     */
    public static boolean isAddress(String address){
        if(!StringUtils.isBlank(address)
                &&(address.startsWith(Prefix.ZeroLowerX.getPrefixString()) || address.startsWith(Prefix.VeChainX.getPrefixString()))
                &&address.length() == 42){
            return true;
        }

        return false;
    }


    public static String convertToHexAddress(String address){

        if(!StringUtils.isBlank(address)
                &&(address.startsWith(Prefix.ZeroLowerX.getPrefixString()) || address.startsWith(Prefix.VeChainX.getPrefixString()))
                &&address.length() == 42) {
            String currentAddr = address.substring( 2 );
            currentAddr = Prefix.ZeroLowerX.getPrefixString() + currentAddr;
            return currentAddr.toLowerCase();
        }else{
            return null;
        }

    }


    /**
     * Check if the Transaction id hex string is valid.
     * @param txId
     * @return
     */
    public static boolean isTxId(String txId){
        if(!StringUtils.isBlank(txId)
                &&(txId.startsWith("0x") || txId.startsWith("0X"))
                &&txId.length() == 66){
            return true;
        }
        return false;
    }

    /**
     * check if the address is correct for checksum.
     * @param address
     * @return
     */
    public static boolean checkSumAddress(String address){
        String checkSumAddress = getChecksumAddress(address);
        if(address.equals(checkSumAddress)){
            return true;
        }
        return false;
    }

    /**
     * get checksum address from hex string address with 0x prefix
     * @param address
     * @return
     */
    public static String getChecksumAddress(String address){

        //remove prefix 0x
        address = BytesUtils.cleanHexPrefix(address);
        address = address.toLowerCase();

        //do keccak256 once
        byte[] bytes = CryptoUtils.keccak256(address.getBytes());
        StringBuffer buffer = new StringBuffer();
        String hex = BytesUtils.toHexString(bytes, null);

        char[] chars = hex.toCharArray();
        int size = address.length();

        char[] raws = address.toCharArray();

        for (int i = 0; i < size; i++) {
            if (parseInt(chars[i]) >= 8) {
                buffer.append((""+raws[i]).toUpperCase());

            } else {
                buffer.append(raws[i]);
            }
        }

        return buffer.toString();
    }
    
    public static String fillZeroBefore(String s, int length) {
    		for(int i = 0; i < length; i++) {
    			s = "0" + s;
    			if(s.length() >= length) {
    				break;
    			}
    		}
    		return s;
    }

    private static int parseInt(char value){
        if(value>='a' && value<='f'){
            return 9 + (value - 'a'+1);
        }else {
            return value - '0';
        }
    }
}
