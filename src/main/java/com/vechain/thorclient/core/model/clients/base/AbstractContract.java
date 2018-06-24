package com.vechain.thorclient.core.model.clients.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vechain.thorclient.core.model.blockchain.ContractCall;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.ToClause;
import com.vechain.thorclient.core.model.clients.ToData;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract Contract class.
 */
public class AbstractContract {

    private List<AbiDefinition> abiDefinitionList;

    public AbstractContract(String abiJsonString){
        this.abiDefinitionList = parseAbiList( abiJsonString );
    }

    /**
     * Build contract {@link ContractCall}. Only to view the data. Don't cost energy.
     * @param definition {@link AbiDefinition}
     * @param hexParameters hex string array.
     * @return {@link ContractCall}
     */
    public static ContractCall buildCall( AbiDefinition definition, String...hexParameters ){
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
     * build transaction clause
     * @param toAddress {@link Address}
     * @param abiDefinition {@link AbiDefinition} Abi definition.
     * @param hexArguments {@link String}
     * @return {@link ToClause}
     */
    public static ToClause buildToClause(Address toAddress, AbiDefinition abiDefinition, String... hexArguments){
        ToData toData = new ToData();
        String data = buildData( abiDefinition, hexArguments );
        toData.setData( data );
        return new ToClause( toAddress, Amount.ZERO,  toData);
    }

    /**
     * Build call data
     * @param abiDefinition {@link AbiDefinition} abi definition
     * @param params hex string format parameters.
     * @return hex string of data.
     */
    protected static String buildData(AbiDefinition abiDefinition, String... params){
        if(abiDefinition == null){
            return null;
        }
        int index ;
        List<AbiDefinition.NamedType> inputs = abiDefinition.getInputs();
        if(inputs == null || params == null ||inputs.size() != params.length){
            throw new IllegalArgumentException( "Parameters length is not valid" );
        }
        StringBuilder dataBuffer = new StringBuilder();
        dataBuffer.append( abiDefinition.getHexMethodCodeNoPefix() );
        for(index = 0; index < params.length; index ++) {
            if (!StringUtils.isHex( params[index] )) {
                throw new IllegalArgumentException( "Parameter format is not hex string" );
            }
            byte[] paramBytes = BytesUtils.toByteArray( params[index] );
            if (paramBytes == null || paramBytes.length > 32) {
                throw new IllegalArgumentException( "Parameter format is hex string size too large, or null" );
            }
            if (paramBytes.length < 32) {
                byte[] fillingZero = new byte[32];
                System.arraycopy( paramBytes, 0, fillingZero, 32 - paramBytes.length, paramBytes.length );
                dataBuffer.append( BytesUtils.toHexString( fillingZero, null ) );
            } else {
                dataBuffer.append( params[0] );
            }
        }
        return "0x" + dataBuffer.toString();
    }


    /**
     * Get list of {@link AbiDefinition}
     * @param abisString abi string.
     * @return list of {@link AbiDefinition}
     */
    private static List<AbiDefinition> parseAbiList(String abisString){
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
