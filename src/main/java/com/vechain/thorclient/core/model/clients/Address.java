package com.vechain.thorclient.core.model.clients;

import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.BlockchainUtils;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.Prefix;
import com.vechain.thorclient.utils.StringUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Represents a VeChain blockchain address with validation and conversion utilities.
 * 
 * <p>Address is a fundamental type in the VeChain ecosystem, representing 20-byte 
 * hexadecimal identifiers for accounts and contracts. This class provides safe 
 * construction, validation, and conversion methods.</p>
 * 
 * <h3>Address Format</h3>
 * <p>VeChain addresses are 20-byte (160-bit) values typically represented as:</p>
 * <ul>
 *   <li>40-character hexadecimal strings (with or without 0x prefix)</li>
 *   <li>20-byte arrays</li>
 * </ul>
 * 
 * <h3>Special Addresses</h3>
 * <ul>
 *   <li><strong>{@link #NULL_ADDRESS}</strong>: Zero address (0x0000...0000)</li>
 *   <li><strong>{@link #VTHO_Address}</strong>: VTHO token contract address</li>
 * </ul>
 * 
 * <h3>Usage Examples</h3>
 * <pre>{@code
 * // Create from hex string
 * Address addr1 = Address.fromHexString("0x1234567890123456789012345678901234567890");
 * Address addr2 = Address.fromHexString("1234567890123456789012345678901234567890");
 * 
 * // Create from bytes
 * byte[] addressBytes = new byte[20];
 * Address addr3 = Address.fromBytes(addressBytes);
 * 
 * // Convert to different formats
 * String hex = addr1.toHexString(Prefix.ZeroLowerX); // "0x1234..."
 * String hexNoPrefix = addr1.toHexString(null);      // "1234..."
 * byte[] bytes = addr1.getBytes();
 * 
 * // Use predefined addresses
 * Address vthoContract = Address.VTHO_Address;
 * Address nullAddr = Address.NULL_ADDRESS;
 * }</pre>
 * 
 * @see Prefix
 * @since 0.1.0
 */
@JsonSerialize(using = AddressSerializer.class)
public class Address {
    public static Address NULL_ADDRESS = new NULLAddress();
    public static Address VTHO_Address = Address.fromHexString("0x0000000000000000000000000000456e65726779"); // Galactica
                                                                                                              // tested.

    private static final int ADDRESS_SIZE = 20;
    private String sanitizeHexAddress;

    public static Address fromBytes(byte[] addressBytes) {
        if (addressBytes != null && addressBytes.length == ADDRESS_SIZE) {
            return new Address(addressBytes);
        } else {
            throw ClientArgumentException.exception("Address.fromBytes Argument Exception");
        }
    }

    public static Address fromHexString(String hexAddress) {
        if (StringUtils.isBlank(hexAddress)) {
            throw ClientArgumentException.exception("Address.fromHexString hexAddress is blank string");
        }
        if (!BlockchainUtils.isAddress(hexAddress)) {
            throw ClientArgumentException.exception("Address.fromHexString hexAddress is not hex format ");
        }
        final String sanitizeHexStr = StringUtils.sanitizeHex(hexAddress);
        return new Address(sanitizeHexStr);
    }

    private Address(byte[] addressBytes) {
        this.sanitizeHexAddress = BytesUtils.toHexString(addressBytes, null);
    }

    private Address(String sanitizeHexAddress) {
        this.sanitizeHexAddress = sanitizeHexAddress;
    }

    private Address() {
    }

    public byte[] toByteArray() {
        return BytesUtils.toByteArray(this.sanitizeHexAddress);
    }

    public String toHexString(Prefix prefix) {
        if (prefix != null) {
            return prefix.getPrefixString() + this.sanitizeHexAddress;
        } else {
            return this.sanitizeHexAddress;
        }
    }

    private static class NULLAddress extends Address {
        public byte[] toByteArray() {
            return new byte[] {};
        }
    }
}
