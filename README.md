# Thor Java Client SDK
[![Build Status](https://travis-ci.org/vechain/thor-client-sdk4j.svg?branch=master)](https://travis-ci.org/vechain/thor-client-sdk4j.svg?branch=master)

A SDK toolkit for client to call VeChainThor Restful API.

- - - -
Requires JDK8.

- - - -
latest version 0.0.8

- - - -
default package jar without dependencies,use -Pall to build all in one jar

## License
Thor Java Client SDK is licensed under the GNU Lesser General Public License v3.0, also included in LICENSE file in repository.
- - - -

## Set blockchain nodes provider
Set blockchain nodes sample as follows

```
NodeProvider nodeProvider = NodeProvider.getNodeProvider();
nodeProvider.setProvider("http://localhost:8669");
nodeProvider.setTimeout(10000);
```

##  You can find the clients toolkit under the directory :
 **src/main/java/com/vechain/thorclients/clients**

There are some SDK specifications <a href="https://github.com/vechain/thor-client-sdk4j/blob/dev/doc"> sdk4j doc </a>

For JUnit Test example: there are some required parameters in config.properties file to support JUnit Test running. 

### WalletUtils

Create Wallet and ouput keystore:

```
WalletInfo walletInfo = WalletUtils.createWallet("123456");
String keyStoreString = walletInfo.toKeystoreString();

logger.info("KeyStore:" + keyStoreString);
logger.info("privKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPrivateKey(), Prefix.ZeroLowerX));
logger.info("pubKey:" + BytesUtils.toHexString(walletInfo.getKeyPair().getRawPublicKey(), Prefix.ZeroLowerX));
logger.info("address:" + walletInfo.getKeyPair().getHexAddress());

KeyStore:
{
  "address": "0x2a200dd7a805db17e9cecbb8e33067306dca143b",
  "crypto": {
    "cipher": "aes-128-ctr",
    "cipherparams": {
      "iv": "f8ab97c61fa736a5cb040b1912f9075c"
    },
    "ciphertext": "0def4f8fad25b5169178f02c93b53c1a7fcbcdbe6b7e960b2e8badcf8098d878",
    "kdf": "scrypt",
    "kdfparams": {
      "dklen": 32,
      "n": 262144,
      "p": 1,
      "r": 8,
      "salt": "e7c337c143029a8ce4ea41153747fc3b103e7c416040a907c36c32f9ca34dbb6"
    },
    "mac": "af1fc6fed812610ce13daaae8bb5dd1e83eba3d100675a9ae07de9357952fc8e"
  },
  "id": "8df9173d-4c05-4a7e-b847-69ccf490db06",
  "version": 3
}
privKey:0xe5a7eec8266f6b5ed3c9578944e22d396228d9cd9ef36d26497edc89237104f0
pubKey:0x60c40e63d06929b338812757462f3f7b54ddb888f9acc1d4b269c553d423169e8efd06e914d180def23150dbdca76eceee18ea19ec7c7ab11b2747e17a8a960f
address:0x2a200dd7a805db17e9cecbb8e33067306dca143b

```

#### Load keystore:

```

String keyStore = "{\"address\":\"0x2a200dd7a805db17e9cecbb8e33067306dca143b\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"f8ab97c61fa736a5cb040b1912f9075c\"},\"ciphertext\":\"0def4f8fad25b5169178f02c93b53c1a7fcbcdbe6b7e960b2e8badcf8098d878\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"e7c337c143029a8ce4ea41153747fc3b103e7c416040a907c36c32f9ca34dbb6\"},\"mac\":\"af1fc6fed812610ce13daaae8bb5dd1e83eba3d100675a9ae07de9357952fc8e\"},\"id\":\"8df9173d-4c05-4a7e-b847-69ccf490db06\",\"version\":3}";
WalletInfo walletInfo = WalletUtils.loadKeystore(keyStore, "123456");

privateKey: BytesUtils.toHexString(walletInfo.getKeyPair().getRawPrivateKey(), Prefix.ZeroLowerX));
publicKey: BytesUtils.toHexString(walletInfo.getKeyPair().getRawPublicKey(), Prefix.ZeroLowerX));
Get address: walletInfo.getKeyPair().getHexAddress());
```

### AccountClient

User can use this client :

#### Get Account information: VET balance and VTHO balance

```
Address address = Address.fromHexString(fromAddress);
Account account = AccountClient.getAccountInfo(address, null);
logger.info("account info:" + JSON.toJSONString(account));
Assert.assertNotNull(account);

eg. 
Address address = Address.fromHexString("0xD3EF28DF6b553eD2fc47259E8134319cB1121A2A");
Account account = AccountClient.getAccountInfo(
	address,  // account address
	null      // null = Revision.BEST
	);
logger.info("account info:" + JSON.toJSONString(account));
account info:
{"balance":"0x42aeda6af58002f600000","energy":"0x14234c71f08e4db8e504","hasCode":false}

```


#### Get code on a address：

```
Address tokenAddr = Address.VTHO_Address;
AccountCode code = AccountClient.getAccountCode(tokenAddr, null);
logger.info("code:" + JSON.toJSONString(code));
code:
{
  "code": "0x6080604052600436106100af576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306fdde03146100b4578063095ea7b31461014457806318160ddd146101a957806323b872dd146101d4578063313ce5671461025957806370a082311461028a57806395d89b41146102e1578063a9059cbb14610371578063bb35783b146103d6578063d89135cd1461045b578063dd62ed3e14610486575b600080fd5b3480156100c057600080fd5b506100c96104fd565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101095780820151818401526020810190506100ee565b50505050905090810190601f1680156101365780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561015057600080fd5b5061018f600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061053a565b604051808215151515815260200191505060405180910390f35b3480156101b557600080fd5b506101be61062b565b6040518082815260200191505060405180910390f35b3480156101e057600080fd5b5061023f600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506106d1565b604051808215151515815260200191505060405180910390f35b34801561026557600080fd5b5061026e610865565b604051808260ff1660ff16815260200191505060405180910390f35b34801561029657600080fd5b506102cb600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061086e565b6040518082815260200191505060405180910390f35b3480156102ed57600080fd5b506102f661094d565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561033657808201518184015260208101905061031b565b50505050905090810190601f1680156103635780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561037d57600080fd5b506103bc600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061098a565b604051808215151515815260200191505060405180910390f35b3480156103e257600080fd5b50610441600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506109a1565b604051808215151515815260200191505060405180910390f35b34801561046757600080fd5b50610470610b67565b6040518082815260200191505060405180910390f35b34801561049257600080fd5b506104e7600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610c0d565b6040518082815260200191505060405180910390f35b60606040805190810160405280600681526020017f566554686f720000000000000000000000000000000000000000000000000000815250905090565b6000816000803373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925846040518082815260200191505060405180910390a36001905092915050565b60003073ffffffffffffffffffffffffffffffffffffffff1663592b389c6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561069157600080fd5b505af11580156106a5573d6000803e3d6000fd5b505050506040513d60208110156106bb57600080fd5b8101908080519060200190929190505050905090565b6000816000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054101515156107c6576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f6275696c74696e3a20696e73756666696369656e7420616c6c6f77616e63650081525060200191505060405180910390fd5b816000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555061085a848484610c93565b600190509392505050565b60006012905090565b60003073ffffffffffffffffffffffffffffffffffffffff1663ee660480836040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b15801561090b57600080fd5b505af115801561091f573d6000803e3d6000fd5b505050506040513d602081101561093557600080fd5b81019080805190602001909291905050509050919050565b60606040805190810160405280600481526020017f5654484f00000000000000000000000000000000000000000000000000000000815250905090565b6000610997338484610c93565b6001905092915050565b60003373ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff161480610add57503373ffffffffffffffffffffffffffffffffffffffff163073ffffffffffffffffffffffffffffffffffffffff1663059950e9866040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b158015610a8a57600080fd5b505af1158015610a9e573d6000803e3d6000fd5b505050506040513d6020811015610ab457600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff16145b1515610b51576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f6275696c74696e3a2073656c66206f72206d617374657220726571756972656481525060200191505060405180910390fd5b610b5c848484610c93565b600190509392505050565b60003073ffffffffffffffffffffffffffffffffffffffff1663138d4d0c6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b158015610bcd57600080fd5b505af1158015610be1573d6000803e3d6000fd5b505050506040513d6020811015610bf757600080fd5b8101908080519060200190929190505050905090565b60008060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905092915050565b6000811115610eaa573073ffffffffffffffffffffffffffffffffffffffff166339ed08d584836040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b158015610d3f57600080fd5b505af1158015610d53573d6000803e3d6000fd5b505050506040513d6020811015610d6957600080fd5b81019080805190602001909291905050501515610dee576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f6275696c74696e3a20696e73756666696369656e742062616c616e636500000081525060200191505060405180910390fd5b3073ffffffffffffffffffffffffffffffffffffffff16631cedfac183836040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050600060405180830381600087803b158015610e9157600080fd5b505af1158015610ea5573d6000803e3d6000fd5b505050505b8173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050505600a165627a7a72305820bd55cb9aff347dc60fe8280ae6b08a6f6deacc85a4e1c89ba0a8ef31fbcaecc60029"
}
```

### TransactionClient


#### Sign VET transaction:


```
byte chainTag = BlockchainClient.getChainTag();
byte[] blockRef = BlockchainClient.getBlockRef(Revision.BEST).toByteArray();
Amount amount = Amount.createFromToken(AbstractToken.VET);
amount.setDecimalAmount("1.12");
ToClause clause = TransactionClient.buildVETToClause(Address.fromHexString("0xD3EF28DF6b553eD2fc47259E8134319cB1121A2A"), amount, ToData.ZERO);
RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(chainTag, blockRef, 720, 21000, (byte) 0x0, CryptoUtils.generateTxNonce(), clause);
	

String raw = BytesUtils.toHexString(rawTransaction.encode(), Prefix.ZeroLowerX);

logger.info("raw=" + raw);

raw=0xf902d6819a8702288058b9af928202d0f90273e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f800008001830616988088ff9198c817655decc0b841bd61e198f126adddb169eebf5cd3da25ae3a3f07102e574bcd1368440d1e307c4c47884364e2abc66ef6940c4953758dd1c57f8255025639702104ce83e9a3b501

TransferResult result = TransactionClient.signThenTransfer(rawTransaction, ECKeyPair.create(privateKey));
logger.info("SendVET result:" + JSON.toJSONString(result));
Assert.assertNotNull(result);
// you can generate transaction id local and verify transfer result
String hexAddress = ECKeyPair.create(privateKey).getHexAddress();
String txIdHex = BlockchainUtils.generateTransactionId(rawTransaction, Address.fromHexString(hexAddress));
logger.info("Calculate transaction txid:" + txIdHex);
Assert.assertEquals(txIdHex, result.getId());
	
```

#### Sign VTHO transaction:

```

byte chainTag = BlockchainClient.getChainTag();
byte[] blockRef = BlockClient.getBlock(null).blockRef().toByteArray();
Amount amount = Amount.createFromToken(ERC20Token.VTHO);
amount.setDecimalAmount("11.12");
ToClause clause = ERC20Contract.buildTranferToClause(ERC20Token.VTHO, Address.fromHexString("0xc71ADC46c5891a8963Ea5A5eeAF578E0A2959779"), amount);
RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(chainTag, blockRef, 720, 80000, (byte) 0x0, CryptoUtils.generateTxNonce(), clause);

String raw = BytesUtils.toHexString(rawTransaction.encode(), Prefix.ZeroLowerX);

logger.info("raw=" + raw);

raw=0xf902d6819a8702288058b9af928202d0f90273e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f800008001830616988088ff9198c817655decc0b841bd61e198f126adddb169eebf5cd3da25ae3a3f07102e574bcd1368440d1e307c4c47884364e2abc66ef6940c4953758dd1c57f8255025639702104ce83e9a3b501

TransferResult result = TransactionClient.signThenTransfer(rawTransaction, ECKeyPair.create(privateKey));
logger.info("SendVTHO Result:" + JSON.toJSONString(result));
Assert.assertNotNull(result);
// you can generate transaction id local and verify transfer result
String hexAddress = ECKeyPair.create(privateKey).getHexAddress();
String txIdHex = BlockchainUtils.generateTransactionId(rawTransaction, Address.fromHexString(hexAddress));
logger.info("Calculate transaction txid:" + txIdHex);
Assert.assertEquals(txIdHex, result.getId());

```

#### Send VET to account：

```
byte chainTag = BlockchainClient.getChainTag();
byte[] blockRef = BlockchainClient.getBlockRef( Revision.BEST).toByteArray();
Amount amount = Amount.createFromToken( AbstractToken.VET);
amount.setDecimalAmount( "21.12" );
ToClause clause = TransactionClient.buildVETToClause(
        Address.fromHexString( "0xc71ADC46c5891a8963Ea5A5eeAF578E0A2959779" ),  // reveiver address
        amount,                                                                 // transfer amount
        ToData.ZERO );                                                          // The default value ToData.ZERO
//construct RawTransaction
RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction( chainTag, blockRef, 720, 21000, (byte)0x0, CryptoUtils.generateTxNonce(), clause);

String raw = BytesUtils.toHexString(rawTransaction.encode(), Prefix.ZeroLowerX);

logger.info("raw=" + raw);

raw=0xf902d6819a8702288058b9af928202d0f90273e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f800008001830616988088ff9198c817655decc0b841bd61e198f126adddb169eebf5cd3da25ae3a3f07102e574bcd1368440d1e307c4c47884364e2abc66ef6940c4953758dd1c57f8255025639702104ce83e9a3b501

```

#### Send VTHO to account:

```
byte chainTag = BlockchainClient.getChainTag();
// null means the best block
byte[] blockRef = BlockClient.getBlock( null ).blockRef().toByteArray();
Amount amount = Amount.createFromToken( ERC20Token.VTHO);
amount.setDecimalAmount( "11.12" );
// construct transaction clause
ToClause clause = ERC20Contract.buildTranferToClause( 
        ERC20Token.VTHO,                                                        // the default ERC20Token.VTHO
        Address.fromHexString("0xc71ADC46c5891a8963Ea5A5eeAF578E0A2959779"),    // receiver address
        amount);                                                                // transfer amount
RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction( chainTag, blockRef, 720, 80000, (byte)0x0, CryptoUtils.generateTxNonce(), clause);

TransferResult result = TransactionClient.signThenTransfer( rawTransaction, ECKeyPair.create( privateKey ) );
logger.info( "transfer vethor result:" + JSON.toJSONString( result ) );
result:{"id":"0xbae8e1cf5d61e6d8a9765dd21cdf35abe919fefc8335d9f9ead13aa3e7f12c83"}

```

#### Call Contract view method:

```

Address contractAddr = ERC20Token.VTHO.getContractAddress();
Address address = Address.fromHexString(fromAddress);
Revision currRevision = Revision.BEST;
AbiDefinition abiDefinition = ERC20Contract.defaultERC20Contract.findAbiDefinition("balanceOf");
ContractCall call = ERC20Contract.buildCall(abiDefinition, address.toHexString(null));
ContractCallResult contractCallResult = ERC20ContractClient.callContract(call, contractAddr, currRevision);

contractCallResult:
{
  "data": "0x00000000000000000000000000000000000000000000001e0c2fcee984ee5c00",
  "events": [
    
  ],
  "gasUsed": 870,
  "reverted": false,
  "transfers": [
    
  ],
  "vmError": ""
}

```
#### Query transaction:

```
Transaction transaction = TransactionClient.getTransaction(txId, isRaw, Revision);

eg.
Transaction transaction = TransactionClient.getTransaction("0x2c405851ca789f48c51c8ede5fadf682fc6636a9aa7ca366328332a3472326ae", true, null);
logger.info("Transaction:" + JSON.toJSONString(transaction));
transaction:
{
  "chainTag": 0,
  "expiration": 0,
  "gas": 0,
  "gasPriceCoef": 0,
  "meta": {
    "pos": "0x000068536517b9017f2e39fd3b6811c60aa30ffbb4f7da102a76d9285133ba91",
    "blockNumber": 26707,
    "blockTimestamp": 1530281470
  },
  "raw": "0xf87e2786685288d071cd8202d0e0df94d3ef28df6b553ed2fc47259e8134319cb1121a2a880de0b6b3a764000080808252088088c5ca5b42e2071935c0b841d05e70cb0e59416db4e59d7d5f5db3908596c4140fa2d47ef082ab2405125726345ef4d58fdc895828feebba7295335aee60818425ed37ee7140e892dc21c1b001",
  "size": 0
}

```

#### Query transaction receipt

```
Receipt receipt = TransactionClient.getTransactionReceipt(txid, Revision);

eg. 
//query receipt info
Receipt receipt = TransactionClient.getTransactionReceipt("0x6b99c0f1ebfa3b9d93dcfc503f468104ac74271728841551aaa44115d080f5b5", null);
logger.info("Receipt:" + JSON.toJSONString(receipt));
Receipt:
{
  "gasPayer": "0x866a849122133888214ac9fc59550077adf14975",
  "gasUsed": 21000,
  "meta": {
    "pos": "0x000066e58079163edacb0bfe06b52a4d16cc646ce8039a2b7cf5136cbc9fb186",
    "blockNumber": 26341,
    "blockTimestamp": 1530277810,
    "txID": "0x6b99c0f1ebfa3b9d93dcfc503f468104ac74271728841551aaa44115d080f5b5",
    "txOrigin": "0x866a849122133888214ac9fc59550077adf14975"
  },
  "outputs": [
    {
      "events": [
        
      ],
      "transfers": [
        {
          "amount": "0xde0b6b3a7640000",
          "recipient": "0xd3ef28df6b553ed2fc47259e8134319cb1121a2a",
          "sender": "0x866a849122133888214ac9fc59550077adf14975"
        }
      ]
    }
  ],
  "paid": "0x1236efcbcbb340000",
  "reverted": false,
  "reward": "0x576e189f04f60000"
}

```
#### Generate TransactionId local

```
// you can generate transaction id and verify the transfer result
String hexAddress = ECKeyPair.create(privateKey).getHexAddress();
String txIdHex = BlockchainUtils.generateTransactionId(rawTransaction, Address.fromHexString(hexAddress));
logger.info("Calculate transaction txid:" + txIdHex);

```

- - - -
### BlockClient

#### Get block:

```
Get specified block:
Revision revision = Revision.create(0);
Block block = BlockClient.getBlock(revision);
logger.info("block:" + JSON.toJSONString(block));
block:
{
  "beneficiary": "0x0000000000000000000000000000000000000000",
  "gasLimit": 10000000,
  "gasUsed": 0,
  "id": "0x00000000ef3b214ad627b051f42add3b93b2f913f2594b94a64b2377b0f9159a",
  "isTrunk": true,
  "number": "0",
  "parentID": "0xffffffff00000000000000000000000000000000000000000000000000000000",
  "receiptsRoot": "0x45b0cfc220ceec5b7c1c62c4d4193d38e4eba48e8815729ce75f9c0ab0e4c1c0",
  "signer": "0x0000000000000000000000000000000000000000",
  "size": 170,
  "stateRoot": "0x120df3368f409525ed30fd98c999af8d66bfa553cae14005fc3b7f00bcc60de1",
  "timestamp": 1528387200,
  "totalScore": 0,
  "transactions": [],
  "txsRoot": "0x45b0cfc220ceec5b7c1c62c4d4193d38e4eba48e8815729ce75f9c0ab0e4c1c0"
}

or

Get best block:
Block block = BlockClient.getBlock(Revision.BEST);
logger.info("block:" + JSON.toJSONString(block));
block:
{
  "beneficiary": "0xafbd76f9cdd19015c2d322a35bbea0480f5d70e1",
  "gasLimit": 10448965,
  "gasUsed": 0,
  "id": "0x00026bfa7cbbd7c8cf643e45eadff1ddce1395cc47a5c08c521498f693381840",
  "isTrunk": true,
  "number": "158714",
  "parentID": "0x00026bf9c0828062b25d0b23df0c99f6571af389d273961b82c90906a0a96b1b",
  "receiptsRoot": "0x45b0cfc220ceec5b7c1c62c4d4193d38e4eba48e8815729ce75f9c0ab0e4c1c0",
  "signer": "0xafbd76f9cdd19015c2d322a35bbea0480f5d70e1",
  "size": 239,
  "stateRoot": "0xa8dd31b95e227b92e800d65c824d2fb124a36e924b398252ec995d3611a69d43",
  "timestamp": 1530016140,
  "totalScore": 1034108,
  "transactions": [],
  "txsRoot": "0x45b0cfc220ceec5b7c1c62c4d4193d38e4eba48e8815729ce75f9c0ab0e4c1c0"
}

```

- - - -
### LogsClient
You can get events logs and transfer logs, the api is also supporting pagination query.

#### Query events logs

```
EventFilter filter = EventFilter.createFilter( Range.createBlockRange(1000, 20000), Options.create( 0, 10 ) );
// you also can filter even logs by topics
// get abiMethodHexString
List<String > eventsTransferInputs = new ArrayList<String>(  );
eventsTransferInputs.add( "address" );
eventsTransferInputs.add( "address" );
eventsTransferInputs.add( "uint256" );
AbiDefinition abiDefinition = ERC20Contract.defaultERC20Contract.findAbiDefinition( "Transfer", "event" , eventsTransferInputs);
String abiMethodHexString = BytesUtils.toHexString( abiDefinition.getBytesMethodHashed(), Prefix.ZeroLowerX);
// filter even logs by topics
filter.addTopicSet( abiMethodHexString, "0x000000000000000000000000866a849122133888214ac9fc59550077adf14975", null, null, null );
filter.addTopicSet( abiMethodHexString, null, "0x000000000000000000000000866a849122133888214ac9fc59550077adf14975", null, null );
ArrayList<?> filteredEvents = LogsClient.filterEvents(filter, Order.DESC, Address.VTHO_Address);
filteredEvents:
[
  {
    "data": "0x00000000000000000000000000000000000000000000d3c21bcecceda1000000",
    "topics": [
      "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef",
      "0x000000000000000000000000e59d475abe695c7f67a8a2321f33a856b0b4c71d",
      "0x000000000000000000000000866a849122133888214ac9fc59550077adf14975"
    ],
    "meta": {
      "pos": "0x000059bfeded8c115511608311f8c9574f197712cc934564979fe6a0abeb46e2",
      "txOrigin": "0xe59d475abe695c7f67a8a2321f33a856b0b4c71d",
      "blockNumber": 22975,
      "txID": "0x56bbf4d86dfaebcf37fa6d2a56532b3cff4dcaeab9590606eb37d1d9d175b58a",
      "blockTimestamp": 1530244150
    }
  },
  {
    "data": "0x00000000000000000000000000000000000000000000d3c21bcecceda1000000",
    "topics": [
      "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef",
      "0x000000000000000000000000866a849122133888214ac9fc59550077adf14975",
      "0x000000000000000000000000e2c3b55d8aa9920058030f73bacece582f2123ff"
    ],
    "meta": {
      "pos": "0x00005bca771336117946a24fd52cfb4d8d94ca4409762a04187a18440527d3c0",
      "txOrigin": "0x866a849122133888214ac9fc59550077adf14975",
      "blockNumber": 23498,
      "txID": "0xe290b4f86a57ba859277117613046fb7159e7ac35bf708c213ef38ac2b7faafd",
      "blockTimestamp": 1530249380
    }
  },
  {
    "data": "0x000000000000000000000000000000000000000000000cb49b44ba602d800000",
    "topics": [
      "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef",
      "0x000000000000000000000000866a849122133888214ac9fc59550077adf14975",
      "0x0000000000000000000000005034aa590125b64023a0262112b98d72e3c8e40e"
    ],
    "meta": {
      "pos": "0x00005d8d601ee2af2566f41eeab03003e6c1f2bcc9c80628dce02d2445d21df8",
      "txOrigin": "0x866a849122133888214ac9fc59550077adf14975",
      "blockNumber": 23949,
      "txID": "0xe2c8df790a210093ca271f40335930593b8fc40e8978b9ec5bbf1865b64ab765",
      "blockTimestamp": 1530253890
    }
  },
  {
    "data": "0x0000000000000000000000000000000000000000000000000de0b6b3a7640000",
    "topics": [
      "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef",
      "0x000000000000000000000000866a849122133888214ac9fc59550077adf14975",
      "0x000000000000000000000000f881a94423f22ee9a0e3e1442f515f43c966b7ed"
    ],
    "meta": {
      "pos": "0x00006263e8cda3d2f89479e652e24acac0f032cae59d78b3fb8125634e4cf85c",
      "txOrigin": "0x866a849122133888214ac9fc59550077adf14975",
      "blockNumber": 25187,
      "txID": "0xc502170ce2516bbb52428714fa2ee9f4a157ca3f2ce779dbba2bb9fb639d82ad",
      "blockTimestamp": 1530266270
    }
  },
  {
    "data": "0x00000000000000000000000000000000000000000000003635c9adc5dea00000",
    "topics": [
      "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef",
      "0x000000000000000000000000866a849122133888214ac9fc59550077adf14975",
      "0x000000000000000000000000f881a94423f22ee9a0e3e1442f515f43c966b7ed"
    ],
    "meta": {
      "pos": "0x000062724a2e8939846a99bf300a76c3ccba11f1b3dcc49e7469e5d4540748e6",
      "txOrigin": "0x866a849122133888214ac9fc59550077adf14975",
      "blockNumber": 25202,
      "txID": "0x63aa0c84478536f43353df520e70aa91490530ee9ef3d462a514bd3006b0b8e7",
      "blockTimestamp": 1530266420
    }
  },
  {
    "data": "0x00000000000000000000000000000000000000000000003635c9adc5dea00000",
    "topics": [
      "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef",
      "0x000000000000000000000000866a849122133888214ac9fc59550077adf14975",
      "0x000000000000000000000000f881a94423f22ee9a0e3e1442f515f43c966b7ed"
    ],
    "meta": {
      "pos": "0x0000627c7d117bd770083d8525f392feeba052a9c85301402f6ed526255444ba",
      "txOrigin": "0x866a849122133888214ac9fc59550077adf14975",
      "blockNumber": 25212,
      "txID": "0x1f2b669a965e656ba853830f21a4200a96c96b572436d9e0a9a924f2e6084f43",
      "blockTimestamp": 1530266520
    }
  },
  {
    "data": "0x00000000000000000000000000000000000000000000003635c9adc5dea00000",
    "topics": [
      "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef",
      "0x000000000000000000000000866a849122133888214ac9fc59550077adf14975",
      "0x000000000000000000000000d3ef28df6b553ed2fc47259e8134319cb1121a2a"
    ],
    "meta": {
      "pos": "0x000066427e0728c29e3945408e9f25671873feb8b9743be8d454a6fcbab265eb",
      "txOrigin": "0x866a849122133888214ac9fc59550077adf14975",
      "blockNumber": 26178,
      "txID": "0x5dee9e3db6a9f53992314dfbf41197a5f6405cf0fd421a5427c274ae6d2479a4",
      "blockTimestamp": 1530276180
    }
  },
  {
    "data": "0x0000000000000000000000000000000000000000000000000de0b6b3a7640000",
    "topics": [
      "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef",
      "0x000000000000000000000000866a849122133888214ac9fc59550077adf14975",
      "0x000000000000000000000000d3ef28df6b553ed2fc47259e8134319cb1121a2a"
    ],
    "meta": {
      "pos": "0x000066d0b71d88edfacfdb2ec33654dc98fd771cf349667e8225f167135a4bac",
      "txOrigin": "0x866a849122133888214ac9fc59550077adf14975",
      "blockNumber": 26320,
      "txID": "0x396d4940947ea65283da2c20f6e655e8f95abaa26075cea33520d867c89d9afa",
      "blockTimestamp": 1530277600
    }
  },
  {
    "data": "0x0000000000000000000000000000000000000000000000000de0b6b3a7640000",
    "topics": [
      "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef",
      "0x000000000000000000000000866a849122133888214ac9fc59550077adf14975",
      "0x000000000000000000000000d3ef28df6b553ed2fc47259e8134319cb1121a2a"
    ],
    "meta": {
      "pos": "0x000066e58079163edacb0bfe06b52a4d16cc646ce8039a2b7cf5136cbc9fb186",
      "txOrigin": "0x866a849122133888214ac9fc59550077adf14975",
      "blockNumber": 26341,
      "txID": "0xb978482708ca35556a3d2872e11849b9aa60a81bc6c7e1d97dfec54db3470d2f",
      "blockTimestamp": 1530277810
    }
  },
  {
    "data": "0x0000000000000000000000000000000000000000000000000de0b6b3a7640000",
    "topics": [
      "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef",
      "0x000000000000000000000000866a849122133888214ac9fc59550077adf14975",
      "0x000000000000000000000000d3ef28df6b553ed2fc47259e8134319cb1121a2a"
    ],
    "meta": {
      "pos": "0x000066eb51b4e2955a9bb4ea287890c34489eeade60840ecfb7959a3352b433f",
      "txOrigin": "0x866a849122133888214ac9fc59550077adf14975",
      "blockNumber": 26347,
      "txID": "0x1a9e9106174e8e54debd2552cf0665a2ffefb62f13d3c1d3e3d01faf88920fbc",
      "blockTimestamp": 1530277870
    }
  }
]
```
#### Query transfer logs

```
TransferFilter filter = TransferFilter.createFilter(Range.createBlockRange( 1000, 20000 ) ,Options.create( 0, 10 ) );
// you can filter condition by sender address or recipient address
filter.addAddressSet(null, Address.fromHexString("0xe59d475abe695c7f67a8a2321f33a856b0b4c71d"), null);
filter.addAddressSet(null, null, Address.fromHexString("0xe59d475abe695c7f67a8a2321f33a856b0b4c71d"));
ArrayList transferLogs = LogsClient.filterTransferLogs( filter, Order.DESC);
transferLogs:
[
  {
    "amount": "0x152d02c7e14af6800000",
    "sender": "0xe59d475abe695c7f67a8a2321f33a856b0b4c71d",
    "meta": {
      "pos": "0x00003abbf8435573e0c50fed42647160eabbe140a87efbe0ffab8ef895b7686e",
      "txOrigin": "0xe59d475abe695c7f67a8a2321f33a856b0b4c71d",
      "blockNumber": 15035,
      "txID": "0x9daa5b584a98976dfca3d70348b44ba5332f966e187ba84510efb810a0f9f851",
      "blockTimestamp": 1530164750
    },
    "recipient": "0x7567d83b7b8d80addcb281a71d54fc7b3364ffed"
  },
  {
    "amount": "0x204fb9313b5d211619800000",
    "sender": "0xe59d475abe695c7f67a8a2321f33a856b0b4c71d",
    "meta": {
      "pos": "0x00003acbdaa137ddf8e2ebee923caebe8f82dece89b67b4f1cae85c097274f3b",
      "txOrigin": "0xe59d475abe695c7f67a8a2321f33a856b0b4c71d",
      "blockNumber": 15051,
      "txID": "0xf01eb38ccbe4111c8b80a34cf864ba3e23a1ba4ab0707415b9efe74d3ab54859",
      "blockTimestamp": 1530164910
    },
    "recipient": "0x4f6fc409e152d33843cf4982d414c1dd0879277e"
  },
  {
    "amount": "0x10f0cf064dd59200000",
    "sender": "0xe59d475abe695c7f67a8a2321f33a856b0b4c71d",
    "meta": {
      "pos": "0x00003dd7d7820959bee931778065e33d418a88ceed23e5b13a59514ae4a9598d",
      "txOrigin": "0xe59d475abe695c7f67a8a2321f33a856b0b4c71d",
      "blockNumber": 15831,
      "txID": "0x5a907f0912656d7bf76a7635c1420454da497e6beec4f025500c1778dd043906",
      "blockTimestamp": 1530172710
    },
    "recipient": "0x50262b2622a5e75d57c5c26770bf4aebf076a9a9"
  }
]
```

- - - -
### BlockchainClient

You can get the chain tag and block reference.

#### Get chain tag:

```
byte chainTag = BlockchainClient.getChainTag();
int chainTagInt = chainTag & 0xff;
logger.info( "chainTag: " + chainTagInt);
chainTag: 39

```
#### Get block reference:

```

Revision revision = Revision.create(0);
Block block = BlockClient.getBlock(revision);
logger.info("blockRef;" + BytesUtils.toHexString(block.blockRef().toByteArray(), Prefix.ZeroLowerX));
blockRef:0x000000000b2bce3c

```


- - - -
### ProtoTypeClient
The detail information you can refer to the page[ProtoType Wiki](https://github.com/vechain/thor/wiki/Prototype(CN))

#### Get master address:

```

ContractCallResult callResult = ProtoTypeContractClient.getMasterAddress( Address.fromHexString( "0x866a849122133888214ac9fc59550077adf14975" ) , Revision.BEST);
logger.info( "testGetMaster result:" + JSON.toJSONString( callResult ) );
{
  "data": "0x000000000000000000000000f881a94423f22ee9a0e3e1442f515f43c966b7ed",
  "events": [
    
  ],
  "gasUsed": 1058,
  "reverted": false,
  "transfers": [
    
  ],
  "vmError": ""
}

```

#### Set master address:

```
String fromAddress = "0x866a849122133888214ac9fc59550077adf14975";
String privateKey = "0x4aa49af0d1c105e70eb71d31e066d8d0f06e46927194e561ea302d57ad0c9ad1";
ECKeyPair aECKeyPair = ECKeyPair.create(privateKey);
TransferResult result = ProtoTypeContractClient.setMasterAddress(
		new Address[] { Address.fromHexString(fromAddress) },
		new Address[] { Address.fromHexString(masterAddress) }, TransactionClient.ContractGasLimit, (byte) 0x0,
		720, aECKeyPair);
result: {"id":"0xbd4d94595b500bc9217839cc5b1987f4a277a277b6ede317a740480a28978ae2"}

```

#### Add user:

```
String fromAddress = "0x866a849122133888214ac9fc59550077adf14975";
String privateKey = "0x4aa49af0d1c105e70eb71d31e066d8d0f06e46927194e561ea302d57ad0c9ad1";
ECKeyPair aECKeyPair = ECKeyPair.create(privateKey);
String userAddress = "0xc71ADC46c5891a8963Ea5A5eeAF578E0A2959779";
TransferResult transferResult = ProtoTypeContractClient.addUser(
        new Address[]{Address.fromHexString( fromAddress )},
        new Address[]{Address.fromHexString( userAddress )},
        70000, (byte)0x0, 720, aECKeyPair );
logger.info("Add user:" + JSON.toJSONString( transferResult ));
transferResult:{"id":"0x1eb2927e48d497d70f4530471abe62aa5700086e54af75ad8523d08005eab45f"}

```

#### Check if it is user:

```
ContractCallResult isUserResult = ProtoTypeContractClient.isUser(Address.fromHexString(fromAddress),
				Address.fromHexString(userAddress), Revision.BEST);
logger.info( "Get isUser result:" + JSON.toJSONString( callResult ) );
{
  "data": "0x0000000000000000000000000000000000000000000000000000000000000001",
  "events": [
    
  ],
  "gasUsed": 664,
  "reverted": false,
  "transfers": [
    
  ],
  "vmError": ""
}

```

#### Remove user:

```

TransferResult transferResult = ProtoTypeContractClient.removeUsers(
      new Address[]{Address.fromHexString( fromAddress )},
      new Address[]{Address.fromHexString( userAddress)},
        70000, (byte)0x0, 720, ECKeyPair.create( privateKey ) );
logger.info( "Remove user:"  + JSON.toJSONString( transferResult ));
transferResult:{"id":"0x1eb2927e48d497d70f4530471abe62aa5700086e54af75ad8523d08005eab45f"}

```
#### Set Credit Plan:

```
Amount credit = Amount.VTHO();
credit.setDecimalAmount("10.00");
Amount recovery = Amount.VTHO();
recovery.setDecimalAmount("0.00001");

TransferResult setCreditPlansResult = ProtoTypeContractClient.setCreditPlans(
		new Address[] { Address.fromHexString(fromAddress) }, new Amount[] { credit },
		new Amount[] { recovery }, TransactionClient.ContractGasLimit, (byte) 0x0, 720, aECKeyPair);
logger.info("set user plans:" + JSON.toJSONString(setCreditPlansResult));
{"id":"0xafe89d896b993efc98cda84add6ded39ea264a19b8a886fcc3f769e54964e990"}

```
#### Get Credit plan:

```
ContractCallResult callResult = ProtoTypeContractClient.getCreditPlan(Address.fromHexString(fromAddress),
				Revision.BEST);
logger.info("Get user plan result:" + JSON.toJSONString(callResult));
getUserCreditCallResult:
{
  "data": "0x0000000000000000000000000000000000000000000000008ac7230489e80000000000000000000000000000000000000000000000000000000009184e72a000",
  "events": [
    
  ],
  "gasUsed": 832,
  "reverted": false,
  "transfers": [
    
  ],
  "vmError": ""
}

```

#### Get User Credit:

```
ContractCallResult getUserCreditCallResult = ProtoTypeContractClient
				.getUserCredit(Address.fromHexString(fromAddress), Address.fromHexString(userAddress), Revision.BEST);
logger.info("Get user plan result:" + JSON.toJSONString(getUserCreditCallResult));
{
  "data": "0x0000000000000000000000000000000000000000000000008ac7230489e80000",
  "events": [
    
  ],
  "gasUsed": 1138,
  "reverted": false,
  "transfers": [
    
  ],
  "vmError": ""
}

```

#### Multiple Party Payment:


```
String targetAddress = "0xD3EF28DF6b553eD2fc47259E8134319cB1121A2A";
String userAddress = "0xba5f00a28f732f23ba946c594716496ebdc9aef5";
String privateKey = "0xeb78d6405ba1a28ccd938a72195e0802dfbe1de463bc6e5dd491b2c7562b5e3f";
// set expiration block
int expirationBlock = 720;

long start = System.currentTimeMillis();
//add user(UserAddress) to owner (fromAddress)
TransferResult transferResult = ProtoTypeContractClient.addUser(
	new Address[] { Address.fromHexString(targetAddress) },   //target address to add user
	new Address[] { Address.fromHexString(userAddress) },     //user address to be added
        TransactionClient.ContractGasLimit,                       //TransactionClient.ContractGasLimit = 70000 gas
	(byte) 0x0,                                               //gasCoef
	expirationBlock,                                          //720 block
	ECKeyPair.create(privateKey));
if (transferResult != null) {
    logger.info("Add user:" + JSON.toJSONString(transferResult));
}

start = System.currentTimeMillis();
Amount credit = Amount.VTHO();
credit.setDecimalAmount("100.00");
Amount recovery = Amount.VTHO();
recovery.setDecimalAmount("0.00001");
//set user plan 
TransferResult setUserPlansResult = ProtoTypeContractClient.setUserPlans(
	new Address[] { Address.fromHexString(targetAddress) }, 
	new Amount[] { credit },
        new Amount[] { recovery }, 
	TransactionClient.ContractGasLimit, 
	(byte) 0x0, 720, 
	ECKeyPair.create(privateKey));
if (setUserPlansResult != null) {
    logger.info("set user plans:" + JSON.toJSONString(setUserPlansResult));
} 

```


- - - -
### Run the test

Use maven to run full test case, which many need about 15 minutes to wait for the test case run, some case may be failed because of the account or block is not existed on your blockchain env.

```
mvn clean install

```

or you can skip test with

```
mvn clean install -Dmaven.test.skip=true

```

### 4. Java console approach

The SDK support the command line approach to get chainTag, blockRef, create wallet, sign transaction, send transaction, get transaction, query transaction receipt; 

Package the jar with maven

```
mvn clean package -Dmaven.test.skip=true

The maven will generate the jar file in folder target: thor-client-sdk4j-0.0.8.jar

```

Run the following command with jar

There is a example transaction file in src/main/resources/exchange_example.xlsx, you need to replace the blockRef in this file.

#### Get chainTag: 

```
 
java -jar thor-client-sdk4j-0.0.8.jar getChainTag {blockchain-server-url}

eg. java -jar thor-client-sdk4j-0.0.8.jar getChainTag http://localhost:8669

ChainTag:
0x27

```


#### Get blockRef: 

```
java -jar thor-client-sdk4j-0.0.8.jar getBlockRef {blockchain-server-url}

eg. java -jar thor-client-sdk4j-0.0.8.jar getBlockRef http://localhost:8669

BlockRef:
0x0000695540f491a5
  
```

#### Create wallet: 

```
java -jar thor-client-sdk4j-0.0.8.jar createWallet {wallet-password} {keystore-file-path}(optional, defalut: ./keystore.json)

eg. java -jar thor-client-sdk4j-0.0.8.jar createWallet <your password> <keystore-file-path>

The keystore.json file will generate in current folder.

The wallet created successfully and the key store is: 
  
{
  "address": "0x2a200dd7a805db17e9cecbb8e33067306dca143b",
  "crypto": {
    "cipher": "aes-128-ctr",
    "cipherparams": {
      "iv": "f8ab97c61fa736a5cb040b1912f9075c"
    },
    "ciphertext": "0def4f8fad25b5169178f02c93b53c1a7fcbcdbe6b7e960b2e8badcf8098d878",
    "kdf": "scrypt",
    "kdfparams": {
      "dklen": 32,
      "n": 262144,
      "p": 1,
      "r": 8,
      "salt": "e7c337c143029a8ce4ea41153747fc3b103e7c416040a907c36c32f9ca34dbb6"
    },
    "mac": "af1fc6fed812610ce13daaae8bb5dd1e83eba3d100675a9ae07de9357952fc8e"
  },
  "id": "8df9173d-4c05-4a7e-b847-69ccf490db06",
  "version": 3
}
  
  The wallet created successfully and the privateKey is:
  0xe5a7eec8266f6b5ed3c9578944e22d396228d9cd9ef36d26497edc89237104f0
  
```



#### Get block: 

```
 java -jar thor-client-sdk4j-0.0.8.jar getBlock {blockchain-server-url}

eg. java -jar thor-client-sdk4j-0.0.8.jar getBlock http://localhost:8669

Block:
{
  "beneficiary": "0xb4094c25f86d628fdd571afc4077f0d0196afb48",
  "gasLimit": 10000000,
  "gasUsed": 0,
  "id": "0x00006971f2f8306fbc37d9b797c083d3966c494520edba1dd77a57eae03be50d",
  "isTrunk": true,
  "number": "26993",
  "parentID": "0x0000697038c5f563c8a7f68ae064424580cae01342e4d432321f875d18c779a0",
  "receiptsRoot": "0x45b0cfc220ceec5b7c1c62c4d4193d38e4eba48e8815729ce75f9c0ab0e4c1c0",
  "signer": "0x25ae0ef84da4a76d5a1dfe80d3789c2c46fee30a",
  "size": 238,
  "stateRoot": "0x282794403cfe505528360a98e9bc6cab151b8ccf9e2812b7c316493a8504afa3",
  "timestamp": 1530284330,
  "totalScore": 27016,
  "transactions": [
    
  ],
  "txsRoot": "0x45b0cfc220ceec5b7c1c62c4d4193d38e4eba48e8815729ce75f9c0ab0e4c1c0"
}
  
```

#### Get transaction: 

```
  
java -jar thor-client-sdk4j-0.0.8.jar getTransaction {transaction-id} {blockchain-server-url}

eg. java -jar thor-client-sdk4j-0.0.8.jar getTransaction 0x19dd77d28ef70be8c924319a6c08b996dd456fa36f29f2427dbda90087a8a897 http://localhost:8669

Transaction:
{
  "chainTag": 0,
  "expiration": 0,
  "gas": 0,
  "gasPriceCoef": 0,
  "meta": {
    "pos": "0x00005c31bcdf0f3de6dd10ad776bc4313b98b5d90e81428345a87c04e5ab2924",
    "blockNumber": 23601,
    "blockTimestamp": 1530250410
  },
  "raw": "0xf88227865c309a71f9d28202d0e4e3945034aa590125b64023a0262112b98d72e3c8e40e8c0675869909f169d17cc00000808082520880888cd02f9cd3a2af07c0b8412097cdeebb3219df52345c5cdc251bd07f359435164019ada5041416014f7f046ccbb615d24a0d342c5001578aa788cb11ace0891f540bdb0bc7b110f2b6d7d900",
  "size": 0
}
  
```

#### Get transaction receipt: 

```
  
java -jar thor-client-sdk4j-0.0.8.jar getTransactionReceipt {transaction-id} {blockchain-server-url}
  
eg. java -jar thor-client-sdk4j-0.0.8.jar getTransactionReceipt 0x6b99c0f1ebfa3b9d93dcfc503f468104ac74271728841551aaa44115d080f5b5 http://localhost:8669
  
Receipt:
{
  "gasPayer": "0x866a849122133888214ac9fc59550077adf14975",
  "gasUsed": 21000,
  "meta": {
    "pos": "0x000066e58079163edacb0bfe06b52a4d16cc646ce8039a2b7cf5136cbc9fb186",
    "blockNumber": 26341,
    "blockTimestamp": 1530277810,
    "txID": "0x6b99c0f1ebfa3b9d93dcfc503f468104ac74271728841551aaa44115d080f5b5",
    "txOrigin": "0x866a849122133888214ac9fc59550077adf14975"
  },
  "outputs": [
    {
      "events": [
        
      ],
      "transfers": [
        {
          "amount": "0xde0b6b3a7640000",
          "recipient": "0xd3ef28df6b553ed2fc47259e8134319cb1121a2a",
          "sender": "0x866a849122133888214ac9fc59550077adf14975"
        }
      ]
    }
  ],
  "paid": "0x1236efcbcbb340000",
  "reverted": false,
  "reward": "0x576e189f04f60000"
}
  
```

#### Sign transactions: 

```
java -jar thor-client-sdk4j-0.0.8.jar sign {your-file-path} {privateKey}

eg. java -jar thor-client-sdk4j-0.0.8.jar sign src/main/resources/exchange_example.xlsx 0xe0b80216ba7b880d85966b38fcd8f7253882bb1386b68b33a8e0b60775e947c0
  
Raw Transaction:
0xf8a3819a8702819f5cfc12d38202d0f842e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f80000808082a4108088f06f91293e58610dc0b84173346fba62605d510895a0d240b89a38e0b87fd8a58df2ce17075cd493e8e316528b4ed0f049cef1710936bbd4bd3af23eb3ffb3740dc0fb59db585714dbeaa001
  
```
#### Sign VTHO transactions: 

```
java -jar thor-client-sdk4j-0.0.8.jar signVTHO {your-file-path} {privateKey}

eg. java -jar thor-client-sdk4j-0.0.8.jar signVTHO src/main/resources/exchange_example.xlsx 0xdce1443bd2ef0c2631adc1c67e5c93f13dc23a41c18b536effbbdcbcdb96fb65
  
Raw Transaction:
0xf9011d81c787015e41be43bb958202d0f8bcf85c940000000000000000000000000000456e6572677980b844a9059cbb000000000000000000000000d3ef28df6b553ed2fc47259e8134319cb1121a2a0000000000000000000000000000000000000000000027cf801b9d4f7d800000f85c940000000000000000000000000000456e6572677980b844a9059cbb000000000000000000000000f881a94423f22ee9a0e3e1442f515f43c966b7ed0000000000000000000000000000000000000000000027cf801b9d4f7d8000008082a41080887650b326b78e0c57c0b841fe27b8866d8a658a66a2d8241a310d1ef72e2954d397fc52aa5b4295f9686d0f6705301acc9a0aac6e9e0f93e3aa6fe0a07ff48e4e3287d0db45905d8a0756eb01
  
```

#### Send and Sign transactions:

```
  
java -jar thor-client-sdk4j-0.0.8.jar signAndSend {blockchain-server-url} {privateKey} {your-file-path}

eg. java -jar thor-client-sdk4j-0.0.8.jar signAndSend http://localhost:8669 0xe0b80216ba7b880d85966b38fcd8f7253882bb1386b68b33a8e0b60775e947c0 src/main/resources/exchange_example.xlsx
  
Send Result:
{"id":"0xd751c50b81c1f13ebd86f4fcd0028a501b6c792fa8b5bbf64028b924a6b2efc9"}

```



#### Send raw transactions:

```
  
java -jar thor-client-sdk4j-0.0.8.jar sendRaw {blockchain-server-url} {raw}

eg. java -jar thor-client-sdk4j-0.0.8.jar sendRaw http://localhost:8669 0xf8a3819a8702819f5cfc12d38202d0f842e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f80000808082a4108088f06f91293e58610dc0b84173346fba62605d510895a0d240b89a38e0b87fd8a58df2ce17075cd493e8e316528b4ed0f049cef1710936bbd4bd3af23eb3ffb3740dc0fb59db585714dbeaa001
  
Send Result:
{"id":"0xd751c50b81c1f13ebd86f4fcd0028a501b6c792fa8b5bbf64028b924a6b2efc9"}

```





