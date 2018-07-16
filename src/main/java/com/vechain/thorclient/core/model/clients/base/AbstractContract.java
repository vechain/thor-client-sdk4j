package com.vechain.thorclient.core.model.clients.base;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vechain.thorclient.core.model.blockchain.ContractCall;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.ToClause;
import com.vechain.thorclient.core.model.clients.ToData;
import com.vechain.thorclient.utils.ContractParamEncoder;
import com.vechain.thorclient.utils.StringUtils;

/**
 * Abstract Contract class.
 */
public class AbstractContract {

	private List<AbiDefinition> abiDefinitionList;

	public AbstractContract(String abiJsonString) {
		this.abiDefinitionList = parseAbiList(abiJsonString);
	}

	/**
	 * Build contract {@link ContractCall}. Only to view the data. Don't cost
	 * energy.
	 * 
	 * @param definition
	 *            {@link AbiDefinition}
	 * @param hexParameters
	 *            hex string array.
	 * @return {@link ContractCall}
	 */
	public static ContractCall buildCall(AbiDefinition definition, Object... hexParameters) {
		if (definition == null) {
			throw new IllegalArgumentException("definition is null");
		}
		String data = buildData(definition, hexParameters);
		ContractCall contractCall = new ContractCall();
		contractCall.setData(data);
		contractCall.setValue("0x0");
		return contractCall;
	}

	/**
	 * Find function AbiDefinition from ABI
	 * 
	 * @param name
	 *            method name.
	 * @return {@link AbiDefinition} abi definition.
	 */
	public AbiDefinition findAbiDefinition(String name) {
		return findAbiDefinition(name, "function", null);
	}

	/**
	 * Find function AbiDefinition from ABI by inpu
	 * 
	 * @param name
	 * @param type
	 * @param inputTypes
	 * @return
	 */
	public AbiDefinition findAbiDefinition(String name, String type, List<String> inputTypes) {
		if (StringUtils.isBlank(name) || StringUtils.isBlank(type)) {
			throw new IllegalArgumentException("name or type is blank.");
		}
		for (AbiDefinition abiDefinition : abiDefinitionList) {
			if (abiDefinition.getName().equals(name) && abiDefinition.getType().equals(type)) {
				if (inputTypes != null) {
					if (checkInputsType(inputTypes, abiDefinition)) {
						return abiDefinition;
					}
				} else { // find abi by name ignore the arguments.
					return abiDefinition;
				}
			}
		}
		throw new IllegalArgumentException(this.getClass().getSimpleName() + " doesn't has abi define of name:" + name);
	}

	private boolean checkInputsType(List<String> inputTypes, AbiDefinition abiDefinition) {
		if (inputTypes == null) {
			return false;
		}
		if (abiDefinition == null) {
			return false;
		}
		if (inputTypes.size() != abiDefinition.getInputs().size()) {
			return false;
		}
		for (int index = 0; index < inputTypes.size(); index++) {
			if (!inputTypes.get(index).equals(abiDefinition.getInputs().get(index).getType())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * build transaction clause
	 * 
	 * @param toAddress
	 *            {@link Address}
	 * @param abiDefinition
	 *            {@link AbiDefinition} Abi definition.
	 * @param hexArguments
	 *            {@link String}
	 * @return {@link ToClause}
	 */
	public static ToClause buildToClause(Address toAddress, AbiDefinition abiDefinition, Object... hexArguments) {
		ToData toData = new ToData();
		String data = buildData(abiDefinition, hexArguments);
		toData.setData(data);
		return new ToClause(toAddress, Amount.ZERO, toData);
	}

	/**
	 * Build call data
	 * 
	 * @param abiDefinition
	 *            {@link AbiDefinition} abi definition
	 * @param params
	 * @return hex string of data without "0x"
	 */
	protected static String buildData(AbiDefinition abiDefinition, Object... params) {
		if (abiDefinition == null) {
			return null;
		}
		int index;
		List<AbiDefinition.NamedType> inputs = abiDefinition.getInputs();
		if (inputs == null || params == null || inputs.size() != params.length) {
			throw new IllegalArgumentException("Parameters length is not valid");
		}
		StringBuilder dataBuffer = new StringBuilder();
		dataBuffer.append(abiDefinition.getHexMethodCodeNoPefix());

		int dynamicDataOffset = ContractParamEncoder.getLength(inputs, params) * 32;
		StringBuilder dynamicData = new StringBuilder();

		for (index = 0; index < params.length; index++) {
			Object param = params[index];
			String abiType = inputs.get(index).getType();
			String code = ContractParamEncoder.encode(abiType, param);
			boolean isDynamic = ContractParamEncoder.isDynamic(index, param, abiType);
			if (isDynamic) {
				String encodedDataOffset = ContractParamEncoder.encodeNumeric(BigInteger.valueOf(dynamicDataOffset),
						true);
				dataBuffer.append(encodedDataOffset);
				dynamicData.append(code);
				dynamicDataOffset += code.length() >> 1;
			} else {
				dataBuffer.append(code);
			}
		}
		dataBuffer.append(dynamicData);
		return "0x" + dataBuffer.toString();
	}

	/**
	 * Get list of {@link AbiDefinition}
	 * 
	 * @param abisString
	 *            abi string.
	 * @return list of {@link AbiDefinition}
	 */
	private static List<AbiDefinition> parseAbiList(String abisString) {
		ObjectMapper objectMapper = new ObjectMapper();
		AbiDefinition[] list = null;
		try {
			list = objectMapper.readValue(abisString, AbiDefinition[].class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (list == null) {
			return null;
		}
		return Arrays.asList(list);
	}

}
