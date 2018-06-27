# Thor Java Client SDK
- - - -
A SDK for client toolkit to call Restful API.

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
		
```

#### Load keystore:

```

String keyStore = "{\"address\":\"0xf56a23f7b9c3b1fd68d812e3d2357bbe68bfd087\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"f390a8c9328ea46779cb17d02d7ef2a5\"},\"ciphertext\":\"622375a3ae80035f99b19d17639b1c4da12153d60b6735a3009065aa38766f64\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"c2d13d5476d7b807c8630cf931f4e0e474bbeae590fa43f90197214aa607d426\"},\"mac\":\"10cdadfdb6ecdeec40fe3330480a2978dff4d10c02e5d7231a88cafe7af711af\"},\"id\":\"6417d351-1cb9-421b-b446-1bd7f887dfbe\",\"version\":3}";
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
	
```

#### Sign VTHO transaction:

```

byte chainTag = BlockchainClient.getChainTag();
byte[] blockRef = BlockClient.getBlock(null).blockRef().toByteArray();
Amount amount = Amount.createFromToken(ERC20Token.VTHO);
amount.setDecimalAmount("11.12");
ToClause clause = ERC20Contract.buildTranferToClause(ERC20Token.VTHO, Address.fromHexString("VXc71ADC46c5891a8963Ea5A5eeAF578E0A2959779"), amount);
RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(chainTag, blockRef, 720, 80000, (byte) 0x0, CryptoUtils.generateTxNonce(), clause);

String raw = BytesUtils.toHexString(rawTransaction.encode(), Prefix.ZeroLowerX);

logger.info("raw=" + raw);

raw=0xf902d6819a8702288058b9af928202d0f90273e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f800008001830616988088ff9198c817655decc0b841bd61e198f126adddb169eebf5cd3da25ae3a3f07102e574bcd1368440d1e307c4c47884364e2abc66ef6940c4953758dd1c57f8255025639702104ce83e9a3b501

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

```

#### Call Contract view method:

```

Address contractAddr = token.getContractAddress();
Revision currRevision = revision;
if(currRevision == null){
    currRevision = Revision.BEST;
}
AbiDefinition abiDefinition = ERC20Contract.defaultERC20Contract.findAbiDefinition("balanceOf");
ContractCall call = ERC20Contract.buildCall( abiDefinition, address.toHexString( null ) );
ContractCallResult contractCallResult = callContract(call, contractAddr,  currRevision );

```
#### Query transaction:

```
Transaction transaction = TransactionClient.getTransaction(txId, isRaw, Revision);

eg.
Transaction transaction = TransactionClient.getTransaction("0xb7aaef583a70184cbd3cebc275c246ee91d05e04fb4b829f2a4a1cb0b1b1e829", true, null);
logger.info("Transaction:" + JSON.toJSONString(transaction));

```

#### Query transaction receipt

```
Receipt receipt = TransactionClient.getTransactionReceipt(txid, Revision);

eg. 
//query receipt info
Receipt receipt = TransactionClient.getTransactionReceipt("0xb7aaef583a70184cbd3cebc275c246ee91d05e04fb4b829f2a4a1cb0b1b1e829", null);
logger.info("Receipt:" + JSON.toJSONString(receipt));
Receipt:
{
  "block": {
    "id": "0x00026b141a583c5d728f99ab305948c6299935af740465fc89a8f3c9ae825bfd",
    "number": 158484,
    "timestamp": 1530013840
  },
  "gasPayer": "0xd3ef28df6b553ed2fc47259e8134319cb1121a2a",
  "gasUsed": 21000,
  "outputs": [
    {
      "events": [],
      "transfers": [
        {
          "amount": "0x125195019f8400000",
          "recipient": "0xc71adc46c5891a8963ea5a5eeaf578e0a2959779",
          "sender": "0xd3ef28df6b553ed2fc47259e8134319cb1121a2a"
        }
      ]
    }
  ],
  "paid": "0x1236efcbcbb340000",
  "reverted": false,
  "reward": "0x576e189f04f60000",
  "tx": {
    "id": "0xb7a36e2e2ea92aad8aa90dfa7850eee743d9fc9855f364adf45c145e74d2995f",
    "origin": "0xd3ef28df6b553ed2fc47259e8134319cb1121a2a"
  }
}

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
ArrayList filteredEvents =  LogsClient.filterEvents( filter, Order.DESC, null);

```
#### Query transfer logs

```
TransferFilter filter = TransferFilter.createFilter(Range.createBlockRange( 1000, 20000 ) ,Options.create( 0, 10 ) );
ArrayList transferLogs = LogsClient.filterTransferLogs( filter, Order.DESC);
```

- - - -
### BlockchainClient

You can get the chain tag and block reference.

#### Get chain tag:

```
byte chainTag = BlockchainClient.getChainTag();
int chainTagInt = chainTag & 0xff;
logger.info( "chainTag: " + chainTagInt);
```
#### Get block reference:

```
Revision revision = Revision.create(0);
Block block = BlockClient.getBlock(revision);
logger.info("blockRef;" + BytesUtils.toHexString(block.blockRef().toByteArray(), Prefix.ZeroLowerX));
```


- - - -
### ProtoTypeClient
The detail information you can refer to the page[ProtoType Wiki](https://github.com/vechain/thor/wiki/Prototype(CN))

#### Get master address:

```
ContractCallResult callResult = ProtoTypeContractClient.getMasterAddress( Address.fromHexString( "0xD3EF28DF6b553eD2fc47259E8134319cB1121A2A" ) , Revision.BEST);
logger.info( "testGetMaster result:" + JSON.toJSONString( callResult ) );

```

#### Set master address:

```
TransferResult result = ProtoTypeContractClient.setMasterAddress( new Address[]{Address.fromHexString( "0xD3EF28DF6b553eD2fc47259E8134319cB1121A2A" ) }, new Address[]{Address.fromHexString( fromAddress )},ContractClient.GasLimit, (byte)0x0, 720, ECKeyPair.create(privateKey ) );
logger.info( "result: " + JSON.toJSONString( result ) );

```

#### Add user:

```
TransferResult transferResult = ProtoTypeContractClient.addUser(
        new Address[]{Address.fromHexString( fromAddress )},
        new Address[]{Address.fromHexString(UserAddress)},
        ContractClient.GasLimit, (byte)0x0, 720, ECKeyPair.create( "0xeb78d6405ba1a28ccd938a72195e0802dfbe1de463bc6e5dd491b2c7562b5e3f" ) );
logger.info("Add user:" + JSON.toJSONString( transferResult ));


```

#### Check if it is user:

```
ContractCallResult callResult = ProtoTypeContractClient.isUser( Address.fromHexString( "0xD3EF28DF6b553eD2fc47259E8134319cB1121A2A" ) ,Address.fromHexString( "0xD3EF28DF6b553eD2fc47259E8134319cB1121A2A" ),
        Revision.BEST);
logger.info( "Get isUser result:" + JSON.toJSONString( callResult ) );
```

#### Remove user:

```
String targetAddress = "0xD3EF28DF6b553eD2fc47259E8134319cB1121A2A";
String userAddress = "0xba5f00a28f732f23ba946c594716496ebdc9aef5";
String privateKey = "0xeb78d6405ba1a28ccd938a72195e0802dfbe1de463bc6e5dd491b2c7562b5e3f";
TransferResult transferResult = ProtoTypeContractClient.removeUsers(
      new Address[]{Address.fromHexString( targetAddress )},
      new Address[]{Address.fromHexString( userAddress)},
        ContractClient.GasLimit, (byte)0x0, 720, ECKeyPair.create( privateKey ) );
logger.info( "Remove user:"  + JSON.toJSONString( transferResult ));

```
#### Set User plan:

```
Amount credit = Amount.VTHO();
credit.setDecimalAmount( "12.00" );
Amount recovery = Amount.VTHO();
recovery.setDecimalAmount( "0.00001" );

TransferResult result = ProtoTypeContractClient.setUserPlans(
        new Address[]{Address.fromHexString( "0xD3EF28DF6b553eD2fc47259E8134319cB1121A2A")},
        new Amount[]{credit},
        new Amount[]{recovery},
        ContractClient.GasLimit, (byte)0x0, 720, ECKeyPair.create( "0xeb78d6405ba1a28ccd938a72195e0802dfbe1de463bc6e5dd491b2c7562b5e3f" ) );
logger.info( "set user plans:" + JSON.toJSONString( result ) );

```
#### Get User plan:

```
ContractCallResult callResult = ProtoTypeContractClient.getUserPlan( Address.fromHexString( "0xD3EF28DF6b553eD2fc47259E8134319cB1121A2A" ) , Revision.BEST);
logger.info( "Get user plan result:" + JSON.toJSONString( callResult ) );

```

#### Get User credits:

```
ContractCallResult callResult = ProtoTypeContractClient.getUserCredit(
        Address.fromHexString( "0xD3EF28DF6b553eD2fc47259E8134319cB1121A2A" ),
        Address.fromHexString( "0xD3EF28DF6b553eD2fc47259E8134319cB1121A2A"),
        Revision.BEST);
logger.info( "Get user plan result:" + JSON.toJSONString( callResult ) );

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

Some case may be failed because of the account or block is not existed on your blockchain env.

```
mvn clean install

```


### 4. Java console approach

The SDK support the command line approach to get chainTag, blockRef, create wallet, sign transaction; Run with maven：

```
mvn clean package -Dmaven.test.skip=true

The maven will generate the jar file in folder target: thor-client-sdk4j-0.0.2.jar

```

Run the following command:

There is a example transaction file in src/main/resources/exchange_example.xlsx, you need to replace the blockRef in this file.

#### Get chainTag: 

```
 
java -jar thor-client-sdk4j-0.0.2.jar getChainTag {blockchain-server-url}

eg. java -jar thor-client-sdk4j-0.0.2.jar getChainTag http://localhost:8669
  ChainTag:
  0x9a
```


#### Get blockRef: 

```
  java -jar thor-client-sdk4j-0.0.2.jar getBlockRef {blockchain-server-url}

  eg. java -jar thor-client-sdk4j-0.0.2.jar getBlockRef http://localhost:8669
  
  BlockRef:
  0x000245e360d4cd1b
  
```

#### Create wallet: 

```
java -jar thor-client-sdk4j-0.0.2.jar createWallet {wallet-password}

eg. java -jar thor-client-sdk4j-0.0.2.jar createWallet my@wallet@pass

The wallet created successfully and the key store is: 
  
{
  "address": "0x4cd1c03c39637cc5c4b6e8c02d06bb4eef76c5ea",
  "crypto": {
    "cipher": "aes-128-ctr",
    "cipherparams": {
      "iv": "125246d1644716acd3399a5ddac832f0"
    },
    "ciphertext": "6aef2fdb3b1ca7015774f273caec2c45f5ab76b70105f7f432413fa8ce189505",
    "kdf": "scrypt",
    "kdfparams": {
      "dklen": 32,
      "n": 262144,
      "p": 1,
      "r": 8,
      "salt": "c08e550d586af29b5cdc830b8ec7241c29b3dbcd5121e463e44e10026ea04453"
    },
    "mac": "42d855c653bb8560b3c87007475ef459746fe6bcafdfa3fcf5c906d698849320"
  },
  "id": "626aff56-19ff-42d1-b6fe-7da96f51d2b0",
  "version": 3
}
  
  The wallet created successfully and the privateKey is:
  0x83d0d6c41e402e1cdb49076f7b8003a289482ed3e406413c721783224c7dcb72
  
```



#### Get block: 

```
 java -jar thor-client-sdk4j-0.0.2.jar getBlock {blockchain-server-url}

  eg. java -jar thor-client-sdk4j-0.0.2.jar getBlock http://localhost:8669
  
  Block:
  {
  "beneficiary": "0xafbd76f9cdd19015c2d322a35bbea0480f5d70e1",
  "gasLimit": 10448965,
  "gasUsed": 0,
  "id": "0x000281ce7f291d3dea4d26c7eaf9104bdf0542e96df7fff3ff6df0b625412994",
  "isTrunk": true,
  "number": "164302",
  "parentID": "0x000281cddb31b82e111b3d6e5a5896ff32216a91868da249723bf17fbdd3b596",
  "receiptsRoot": "0x45b0cfc220ceec5b7c1c62c4d4193d38e4eba48e8815729ce75f9c0ab0e4c1c0",
  "signer": "0xafbd76f9cdd19015c2d322a35bbea0480f5d70e1",
  "size": 239,
  "stateRoot": "0xc7cef51188e95ab25721cd700b097f5fb47c5865046761c3fc60f83f025b3e2d",
  "timestamp": 1530072030,
  "totalScore": 1078802,
  "transactions": [],
  "txsRoot": "0x45b0cfc220ceec5b7c1c62c4d4193d38e4eba48e8815729ce75f9c0ab0e4c1c0"
}
  
```

#### Get transaction: 

```
  
java -jar thor-client-sdk4j-0.0.2.jar getTransaction {transaction-id} {blockchain-server-url}

  eg. java -jar thor-client-sdk4j-0.0.2.jar getTransaction 0xd751c50b81c1f13ebd86f4fcd0028a501b6c792fa8b5bbf64028b924a6b2efc9 http://localhost:8669
  Transaction:
  {
  "block": {
    "id": "0x000281a71b862025b02427f3998303b04b897f02057e97184b8638d306d0c99b",
    "number": 164263,
    "timestamp": 1530071640
  },
  "chainTag": 0,
  "expiration": 0,
  "gas": 0,
  "gasPriceCoef": 0,
  "raw":   "0xf8a3819a8702819f5cfc12d38202d0f842e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f80000808082a4108088f06f91293e58610dc0b84173346fba62605d510895a0d240b89a38e0b87fd8a58df2ce17075cd493e8e316528b4ed0f049cef1710936bbd4bd3af23eb3ffb3740dc0fb59db585714dbeaa001",
  "size": 0
}
  
```

#### Get transaction receipt: 

```
  
 java -jar thor-client-sdk4j-0.0.2.jar getTransactionReceipt {transaction-id} {blockchain-server-url}
  
  eg. java -jar thor-client-sdk4j-0.0.2.jar getTransactionReceipt 0xd751c50b81c1f13ebd86f4fcd0028a501b6c792fa8b5bbf64028b924a6b2efc9 http://localhost:8669
  
  Receipt:
  {
  "block": {
    "id": "0x000281a71b862025b02427f3998303b04b897f02057e97184b8638d306d0c99b",
    "number": 164263,
    "timestamp": 1530071640
  },
  "gasPayer": "0xf881a94423f22ee9a0e3e1442f515f43c966b7ed",
  "gasUsed": 37000,
  "outputs": [
    {
      "events": [],
      "transfers": [
        {
          "amount": "0x364200111c48f80000",
          "recipient": "0xd3ef28df6b553ed2fc47259e8134319cb1121a2a",
          "sender": "0xf881a94423f22ee9a0e3e1442f515f43c966b7ed"
        }
      ]
    },
    {
      "events": [],
      "transfers": [
        {
          "amount": "0x364200111c48f80000",
          "recipient": "0xd3ef28df6b553ed2fc47259e8134319cb1121a2a",
          "sender": "0xf881a94423f22ee9a0e3e1442f515f43c966b7ed"
        }
      ]
    }
  ],
  "paid": "0x2017a67f731740000",
  "reverted": false,
  "reward": "0x9a0b1f308ed60000",
  "tx": {
    "id": "0xd751c50b81c1f13ebd86f4fcd0028a501b6c792fa8b5bbf64028b924a6b2efc9",
    "origin": "0xf881a94423f22ee9a0e3e1442f515f43c966b7ed"
  }
}
  
```

#### Sign transactions: 

```
java -jar thor-client-sdk4j-0.0.2.jar sign {your-file-path} {privateKey}

eg. java -jar thor-client-sdk4j-0.0.2.jar sign src/main/resources/exchange_example.xlsx 0xe0b80216ba7b880d85966b38fcd8f7253882bb1386b68b33a8e0b60775e947c0
  
  Raw Transaction:
  0xf8a3819a8702819f5cfc12d38202d0f842e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f80000808082a4108088f06f91293e58610dc0b84173346fba62605d510895a0d240b89a38e0b87fd8a58df2ce17075cd493e8e316528b4ed0f049cef1710936bbd4bd3af23eb3ffb3740dc0fb59db585714dbeaa001
  
```

#### Send and Sign transactions:

```
  
java -jar thor-client-sdk4j-0.0.2.jar signAndSend {blockchain-server-url} {privateKey} {your-file-path}

eg. java -jar thor-client-sdk4j-0.0.2.jar signAndSend http://localhost:8669 0xe0b80216ba7b880d85966b38fcd8f7253882bb1386b68b33a8e0b60775e947c0 src/main/resources/exchange_example.xlsx
  
  Send Result:
 {"id":"0xd751c50b81c1f13ebd86f4fcd0028a501b6c792fa8b5bbf64028b924a6b2efc9"}

```



#### Send raw transactions:

```
  
java -jar thor-client-sdk4j-0.0.2.jar sendRaw {blockchain-server-url} {raw}

eg. java -jar thor-client-sdk4j-0.0.2.jar sendRaw http://localhost:8669 0xf8a3819a8702819f5cfc12d38202d0f842e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f80000808082a4108088f06f91293e58610dc0b84173346fba62605d510895a0d240b89a38e0b87fd8a58df2ce17075cd493e8e316528b4ed0f049cef1710936bbd4bd3af23eb3ffb3740dc0fb59db585714dbeaa001
  
  Send Result:
 {"id":"0xd751c50b81c1f13ebd86f4fcd0028a501b6c792fa8b5bbf64028b924a6b2efc9"}

```





