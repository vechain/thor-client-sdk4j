package com.vechain.thorclient.core.model.clients.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vechain.thorclient.core.model.blockchain.ContractCall;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract Contract class.
 */
public class AbstractContract {

    protected List<AbiDefinition> abiDefinitionList;

    public AbstractContract(String abiJsonString){
        this.abiDefinitionList = parseAbiList( abiJsonString );
    }

    /**
     * Build contract {@link ContractCall}. Only to view the data. Don't cost energy.
     * @param definition {@link AbiDefinition}
     * @param method method name.
     * @param hexParameters hex string array.
     * @return {@link ContractCall}
     */
    public ContractCall buildCall( AbiDefinition definition, String method, String...hexParameters ){
        if(definition == null){
            throw new IllegalArgumentException( "definition is null" );
        }

        if(StringUtils.isBlank( method )){
            throw new IllegalArgumentException( "method string is null" );
        }

        String methodId = getHexMethodCodeNoPefix( definition );

        String data = buildCallData(definition, methodId, hexParameters);
        ContractCall contractCall = new ContractCall();
        contractCall.setData( data );
        contractCall.setValue( "0x0" );
        return contractCall;
    }

    /**
     * Find AbiDefinition from ABI
     * @param name method name.
     * @return {@link AbiDefinition} abi definition.
     */
    public AbiDefinition findAbiDefinition(String name){
        if(StringUtils.isBlank( name )){
            throw new IllegalArgumentException( name );
        }
        for(AbiDefinition abiDefinition : abiDefinitionList){
            if(abiDefinition.getName().equalsIgnoreCase( name )){
                return abiDefinition;
            }
        }
        return null;
    }

    /**
     * Build call data
     * @param abiDefinition {@link AbiDefinition} abi definition
     * @param methodId method id hex string.
     * @param params hex string format parameters.
     * @return hex string of data.
     */
    private static String buildCallData(AbiDefinition abiDefinition, String methodId, String... params){
        int index = 0;
        List<AbiDefinition.NamedType> inputs = abiDefinition.getInputs();
        if(inputs.size() != params.length){
            throw new IllegalArgumentException( "Parameters length is not valid" );
        }
        StringBuffer dataBuffer = new StringBuffer();
        if(StringUtils.isHex( methodId )){
            methodId = BytesUtils.cleanHexPrefix( methodId );
        }
        if(methodId.length() != 8){
            throw new IllegalArgumentException( "Method id is not valid." );
        }
        dataBuffer.append( methodId );
        for(index = 0; index < params.length; index ++){
            if(!StringUtils.isHex( params[index] )){
                throw new IllegalArgumentException("Parameter format is not hex string");
            }
            byte[] paramBytes = BytesUtils.toByteArray( params[index] );
            if(paramBytes == null || paramBytes.length > 32){
                throw new IllegalArgumentException("Parameter format is hex string size too large, or null");
            }
            if(paramBytes.length < 32){
                byte[] fillingZero = new byte[32];
                System.arraycopy(paramBytes,0, fillingZero, 32 - paramBytes.length, paramBytes.length);
                dataBuffer.append( BytesUtils.toHexString(fillingZero, null) );
            }else{
                dataBuffer.append( params[0] );
            }

        }
        return "0x" + dataBuffer.toString();
    }

    /**
     * Get method code from abi definition
     * @param abiDefinition {@link AbiDefinition}
     * @return hex string.
     */
    public static String getHexMethodCodeNoPefix(AbiDefinition abiDefinition){
        if(abiDefinition == null){
            throw new IllegalArgumentException( "AbiDefinition is null" );
        }
        String name = abiDefinition.getName();
        String methodSignature = buildMethodSignature(name, abiDefinition.getInputs());
        byte[] hashCode =CryptoUtils.keccak256( methodSignature.getBytes());
        return BytesUtils.toHexString(hashCode, null).substring( 0,8 );
    }


    /**
     * Get list of {@link AbiDefinition}
     * @param abisString abi string.
     * @return
     */
    public static List<AbiDefinition> parseAbiList(String abisString){
        ObjectMapper objectMapper = new ObjectMapper(  );
        AbiDefinition[] list = null;
        try {
            list = objectMapper.readValue( abisString,  AbiDefinition[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list == null){
            return null;
        }
        return Arrays.asList(list);
    }


    private static String buildMethodSignature(String methodName, List<AbiDefinition.NamedType> parameters) {
        StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append("(");
        int index = 0;
        for(AbiDefinition.NamedType namedType: parameters){
            result.append(namedType.getType());
            index ++;
            if(index < parameters.size()){
                result.append( "," );
            }
        }
        result.append(")");
        return result.toString();
    }


}
