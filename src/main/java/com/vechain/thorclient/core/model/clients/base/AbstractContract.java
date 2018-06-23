package com.vechain.thorclient.core.model.clients.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vechain.thorclient.core.model.blockchain.ContractCall;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.StringUtils;

import java.io.IOException;
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
     * @param hexParameters hex string array.
     * @return {@link ContractCall}
     */
    public ContractCall buildCall( AbiDefinition definition, String...hexParameters ){
        if(definition == null){
            throw new IllegalArgumentException( "definition is null" );
        }
        String data = buildData(definition, hexParameters);
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
     * @param params hex string format parameters.
     * @return hex string of data.
     */
    public static String buildData(AbiDefinition abiDefinition, String... params){
        int index = 0;
        List<AbiDefinition.NamedType> inputs = abiDefinition.getInputs();
        if(inputs.size() != params.length){
            throw new IllegalArgumentException( "Parameters length is not valid" );
        }
        StringBuffer dataBuffer = new StringBuffer();
        dataBuffer.append( abiDefinition.getHexMethodCodeNoPefix() );
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


}
