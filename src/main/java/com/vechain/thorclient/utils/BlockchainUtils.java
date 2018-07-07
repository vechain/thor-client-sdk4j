package com.vechain.thorclient.utils;

import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.utils.crypto.ECDSASign;
import com.vechain.thorclient.utils.crypto.ECDSASignature;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * Blockchain utility, include address check, blockId check, amount calculate.
 */
public class BlockchainUtils {

	/**
	 * Check if the block revision is valid.
	 * 
	 * @param revision
	 *            block revision string.
	 * @return boolean value.
	 */
	public static boolean isValidRevision(String revision) {

		String blockNumPattern = "^[0-9]\\d*$";

		if (StringUtils.isBlank(revision)) {
			return false;
		}

		if ((StringUtils.isHex(revision) && revision.length() == 66)) {
			return true;
		} else if (Pattern.matches(blockNumPattern, revision)) {
			return true;
		} else if ("best".equalsIgnoreCase(revision)) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the address hex string is valid.
	 * 
	 * @param address
	 *            address hex string start with "0x", "VX" or without prefix string.
	 * @return boolean value.
	 */
	public static boolean isAddress(final String address) {
		return (StringUtils.isCriticalHex(address) && address.length() == 42);
	}


	/**
	 * Check if hexId string is valid.
	 * 
	 * @param hexId
	 *            is block Id or txId.
	 * @return true or false.
	 */
	public static boolean isId(String hexId) {
		return !StringUtils.isBlank(hexId) && StringUtils.isHex(hexId) && hexId.length() == 66;
	}

	/**
	 * Check if the address is correct for checksum.
	 * 
	 * @param address
	 * @return
	 */
	public static boolean checkSumAddress(String address) {
		String checkSumAddress = getChecksumAddress(address);
		if (address.equals(checkSumAddress)) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the address is correct for checksum.
	 * 
	 * @param address
	 * @param isCheck
	 * @return
	 */
	public static boolean checkSumAddress(final String address, boolean isCheck) {
		boolean rtn = false;
		if (address != null && address.length() == 42 && address.toLowerCase().matches("0x[A-Fa-f0-9]{40}")) {
			if (isCheck) {
				String realAddress = address.substring(2);
				rtn = checkSumAddress(realAddress);
			} else {
				rtn = true;
			}
		}
		return rtn;
	}

	/**
	 * Get checksum address from hex string address with 0x prefix
	 * 
	 * @param address
	 *            hex string
	 * @return checksum address string.
	 */
	public static String getChecksumAddress(String address) {

		// remove prefix 0x
		address = BytesUtils.cleanHexPrefix(address);
		address = address.toLowerCase();

		// do keccak256 once
		byte[] bytes = CryptoUtils.keccak256(address.getBytes());
		StringBuffer buffer = new StringBuffer();
		String hex = BytesUtils.toHexString(bytes, null);

		char[] chars = hex.toCharArray();
		int size = address.length();

		char[] raws = address.toCharArray();

		for (int i = 0; i < size; i++) {
			if (parseInt(chars[i]) >= 8) {
				buffer.append(("" + raws[i]).toUpperCase());

			} else {
				buffer.append(raws[i]);
			}
		}

		return "0x" + buffer.toString();
	}

	public static String fillZeroBefore(String s, int length) {
		for (int i = 0; i < length; i++) {
			s = "0" + s;
			if (s.length() >= length) {
				break;
			}
		}
		return s;
	}

	private static int parseInt(char value) {
		if (value >= 'a' && value <= 'f') {
			return 9 + (value - 'a' + 1);
		} else {
			return value - '0';
		}
	}

	/**
	 * get amount of {@link BigDecimal} value.
	 * 
	 * @param hexString
	 *            hex string of the amount.
	 * @param precision
	 *            the precision of the amount, with is 18 by default
	 * @param scale
	 *            the remain digits numbers of fractional part
	 * @return the amount value which can show to the end user.
	 */
	public static BigDecimal amount(String hexString, int precision, int scale) {
		byte[] balBytes = BytesUtils.toByteArray(hexString);
		if (balBytes == null) {
			return null;
		}
		BigInteger balInteger = BytesUtils.bytesToBigInt(balBytes);
		return BytesUtils.bigIntToBigDecimal(balInteger, precision, scale);
	}

	/**
	 * Convert big decimal to byte array.
	 * 
	 * @param amount
	 *            amount {@link BigDecimal}
	 * @param precision
	 *            must >= 0
	 * @return byte array.
	 */
	public static byte[] byteArrayAmount(BigDecimal amount, int precision) {
		if (amount == null) {
			throw new IllegalArgumentException("amount is null");
		}
		if (precision < 0) {
			throw new IllegalArgumentException("precision is invalid");
		}
		BigDecimal bigDecimal = amount.multiply(BigDecimal.TEN.pow(precision));
		BigInteger bigInt = bigDecimal.toBigInteger();
		return BytesUtils.trimLeadingZeroes(bigInt.toByteArray());
	}

    /**
     * Recover public key
     */
    public static ECKeyPair recoverPublicKey(String rawTransactionHex){
        if(StringUtils.isBlank( rawTransactionHex )){
            return null;
        }
        RawTransaction rawTransaction = RLPUtils.decode( rawTransactionHex );
        if(rawTransaction == null){
            return null;
        }
        RawTransaction newRawTransaction = rawTransaction.copy();
        newRawTransaction.setSignature( null );
        byte[] signature = rawTransaction.getSignature();
        if(signature == null || signature.length != 65){
            return null;
        }
        byte[] rlpTxRaw = RLPUtils.encodeRawTransaction( newRawTransaction );
        byte[] rBytes = new byte[32];
        byte[] sBytes = new byte[32];
        System.arraycopy( signature, 0, rBytes, 0, rBytes.length );
        System.arraycopy( signature, 32, sBytes, 0, sBytes.length );
        byte recovery = signature[64];
        byte[] signingHash = CryptoUtils.blake2b( rlpTxRaw );
        ECDSASignature ecdsaSignature = new ECDSASignature(rBytes, sBytes);
        BigInteger publicKey = ECDSASign.recoverFromSignature( recovery, ecdsaSignature, signingHash);
        return new ECKeyPair( null, publicKey );

    }


    /**
     * Generate transaction id.
     * @param rawTransaction {@link RawTransaction}
     * @param signer {@link Address}
     * @return a hex string id with "0x"
     */
	public static String generateTransactionId(RawTransaction rawTransaction, Address signer){
	    if(rawTransaction == null || signer == null){
	        return null;
        }
        RawTransaction copyRawTransaction = rawTransaction.copy();
	    copyRawTransaction.setSignature( null );
        byte[] rlp = RLPUtils.encodeRawTransaction( copyRawTransaction );
	    byte[] signHash = CryptoUtils.blake2b( rlp );
        return generateTransactionId( signHash, signer );
    }

    /**
     * Generate txId
     * @param signingHash byte array
     * @param signer
     * @return a hex string id with "0x"
     */
    public static String generateTransactionId(byte[] signingHash, Address signer){
	    if(signingHash == null || signer == null){
	        return null;
        }
        byte[] concatenatedBytes = new byte[52];
        System.arraycopy( signingHash, 0, concatenatedBytes, 0, signingHash.length );
        System.arraycopy( signer.toByteArray(), 0 , concatenatedBytes, signingHash.length, signer.toByteArray().length );
        byte[] txIdBytes = CryptoUtils.blake2b( concatenatedBytes );
        return BytesUtils.toHexString( txIdBytes, Prefix.ZeroLowerX );
    }

}
