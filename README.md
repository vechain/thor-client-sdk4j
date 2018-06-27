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

There is a example transaction file in src/main/resources/exchange_example.xlsx

#### Get chainTag: 

```
 
java -jar thor-client-sdk4j-0.0.2.jar getChainTag {blockchain-server-url}

eg. java -jar thor-client-sdk4j-0.0.2.jar getChainTag "http://localhost:8669"
  ChainTag:
  0x9a
```


#### Get blockRef: 

```
  java -jar thor-client-sdk4j-0.0.2.jar getBlockRef {blockchain-server-url}

  eg. java -jar thor-client-sdk4j-0.0.2.jar getBlockRef "http://localhost:8669"
  
  BlockRef:
  0x000245e360d4cd1b
  
```

#### Create wallet: 

```
java -jar thor-client-sdk4j-0.0.2.jar createWallet {wallet-password}

The keystore.json file will be generated in current folder.

eg. java -jar thor-client-sdk4j-0.0.2.jar createWallet "my@wallet@pass"

  
The wallet created successfully and the key store is: 
  
  {
    "address": "0xc283b29b9e46ec62b1cc78d95ad2598e58f8af17",
    "crypto": {
      "cipher": "aes-128-ctr",
      "cipherparams": {
        "iv": "8bd06ab3901ec874c35c396736624800"
      },
      "ciphertext": "f69d08d62cdadae76da47e76e6d59b7c60e8e2bb8919c785bac0f6f8621ddfed",
      "kdf": "scrypt",
      "kdfparams": {
        "dklen": 32,
        "n": 262144,
        "p": 1,
        "r": 8,
        "salt": "b4e5b39cadcb66f4682f7863dde854b4a87b3810864d1ab6fafd1cdc923a2a1c"
      },
      "mac": "a2ee21d526a1d0113fde864bf67c55ff2b2cd85fc6a8f232e21d3b59da9790b3"
    },
    "id": "656a8193-a670-48fc-bbf7-eedbdb2dd5e7",
    "version": 3
  }
  
  The wallet created successfully and the privateKey is:
  0x5eabfe97a3854a16b2194ff18baf14471809896c73627414c7fbd35bd7431014
  
```



#### Get block: 

```
 java -jar thor-client-sdk4j-0.0.2.jar getBlock {blockchain-server-url}

  eg. java -jar thor-client-sdk4j-0.0.2.jar getBlock "http://localhost:8669"
  
  Block:
  { 
    number: 148847,
    id: '0x0002456fe81e6df0548a327faa3c1764eff7c3b7ce5cf1d1d27264818e78ea8c',
    size: 467,
    parentID: '0x0002456e56ae5827d90b760cf531fc98791621f548ee08dcedc6e5cbb97d5b3c',
    timestamp: 1529917460,
    gasLimit: 10448965,
    beneficiary: '0x97a79df349e06b472fba84aba852659f1bdbd90b',
    gasUsed: 51462,
    totalScore: 946359,
    txsRoot: '0xb914c99935d881b113a116c9a603ff670e27cc74703bade677735e13b7b53970',
    stateRoot: '0x3b391f04e9e9c61543b902e2fd6b1d7a617be2c3cf3ff90f43be05ff637a96b3',
    receiptsRoot: '0x262e43962d139e3dd6904c451c39dc5d14384343d41f60d70168f920bc7858c4',
    signer: '0x97a79df349e06b472fba84aba852659f1bdbd90b',
    isTrunk: true,
    transactions: [ '0x255576013fd61fa52f69d5d89af8751731d5e9e17215b0dd6c33af51bfe28710' ] 
  }
  
```

#### Get transaction: 

```
  
java -jar thor-client-sdk4j-0.0.2.jar getTransaction {transaction-id} {blockchain-server-url}

  eg. java -jar thor-client-sdk4j-0.0.2.jar getTransaction "0x255576013fd61fa52f69d5d89af8751731d5e9e17215b0dd6c33af51bfe28710"  "http://localhost:8669"
  
  {
      "id": "0x255576013fd61fa52f69d5d89af8751731d5e9e17215b0dd6c33af51bfe28710",
      "size": 224,
      "chainTag": "0x9a",
      "blockRef": "0x0002456e56ae5827",
      "expiration": 720,
      "clauses": [
          {
              "to": "0xB2ef3293Bb6c886d9e57ba205c46450B6d48A0A1",
              "value": "1234560000000000000",
              "data": "0x"
          },
          {
              "to": "0x0000000000000000000000000000456E65726779",
              "value": "0",
              "data": "0xa9059cbb000000000000000000000000b2ef3293bb6c886d9e57ba205c46450b6d48a0a100000000000000000000000000000000000000000000000000000000000003ff"
          }
      ],
      "gasPriceCoef": 0,
      "gas": 100000,
      "dependsOn": null,
      "nonce": "0x164362fbdd0",
      "origin": "0x267Dc1dF3e82E6BdAD45156C7c31Aad36DF2B5Fa",
      "block": {
          "id": "0x0002456fe81e6df0548a327faa3c1764eff7c3b7ce5cf1d1d27264818e78ea8c",
          "number": 148847,
          "timestamp": 1529917460
      },
      "blockNumber": 148847
  }
  
```

#### Get transaction receipt: 

```
  
 java -jar thor-client-sdk4j-0.0.2.jar getTransactionReceipt {transaction-id} {blockchain-server-url}
  
  eg. java -jar thor-client-sdk4j-0.0.2.jar getTransactionReceipt "0x255576013fd61fa52f69d5d89af8751731d5e9e17215b0dd6c33af51bfe28710"  "http://localhost:8669" 
  
  Return transaction receipt：this transaction contain two clauses，VET transfer and VTHO transfer。
  
  {
      "gasUsed": 51462,
      "gasPayer": "0x267Dc1dF3e82E6BdAD45156C7c31Aad36DF2B5Fa",
      "paid": "0x2ca2dc057b7270000",
      "reward": "0xd640ece71d588000",
      "reverted": false,
      "block": {
          "id": "0x0002456fe81e6df0548a327faa3c1764eff7c3b7ce5cf1d1d27264818e78ea8c",
          "number": 148847,
          "timestamp": 1529917460
      },
      "tx": {
          "id": "0x255576013fd61fa52f69d5d89af8751731d5e9e17215b0dd6c33af51bfe28710",
          "origin": "0x267Dc1dF3e82E6BdAD45156C7c31Aad36DF2B5Fa"
      },
      "outputs": [
          {
              "contractAddress": null,
              "events": [],
              "transfers": [                                //VET transfer
                  { 
                      "sender": "0x267dc1df3e82e6bdad45156c7c31aad36df2b5fa",
                      "recipient": "0xb2ef3293bb6c886d9e57ba205c46450b6d48a0a1",
                      "amount": "0x112209c76de80000"
                  }
              ]
          },
          {
              "contractAddress": null,
              "events": [                                   //VTHO transfer
                  {
                      "address": "0x0000000000000000000000000000456E65726779",
                      "topics": [
                          "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef",
                          "0x000000000000000000000000267dc1df3e82e6bdad45156c7c31aad36df2b5fa",
                          "0x000000000000000000000000b2ef3293bb6c886d9e57ba205c46450b6d48a0a1"
                      ],
                      "data": "0x00000000000000000000000000000000000000000000000000000000000003ff"
                  }
              ],
              "transfers": []
          }
      ],
      "blockNumber": 148847,
      "blockHash": "0x0002456fe81e6df0548a327faa3c1764eff7c3b7ce5cf1d1d27264818e78ea8c",
      "transactionHash": "0x255576013fd61fa52f69d5d89af8751731d5e9e17215b0dd6c33af51bfe28710",
      "status": "0x1"
  }
  
```

#### Sign VET transactions: 

```
java -jar thor-client-sdk4j-0.0.2.jar signVET {your-file-path} {privateKey}

eg. java -jar thor-client-sdk4j-0.0.2.jar signVET src/main/resources/exchange_example.xlsx 0xe0b80216ba7b880d85966b38fcd8f7253882bb1386b68b33a8e0b60775e947c0
  
  Raw Transaction:
  0xf902d6819a8702288058b9af928202d0f90273e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f8000080e094d3ef28df6b553ed2fc47259e8134319cb1121a2a89364200111c48f800008001830616988088ff9198c817655decc0b841bd61e198f126adddb169eebf5cd3da25ae3a3f07102e574bcd1368440d1e307c4c47884364e2abc66ef6940c4953758dd1c57f8255025639702104ce83e9a3b501
  
```

#### Send and Sign VET transactions:

```
  
java -jar thor-client-sdk4j-0.0.2.jar signAndSendVET {blockchain-server-url} {privateKey} {your-file-path}

eg. java -jar thor-client-sdk4j-0.0.2.jar signAndSendVET "http://localhost:8669" "0xe0b80216ba7b880d85966b38fcd8f7253882bb1386b68b33a8e0b60775e947c0" "src/main/resources/exchange_example.xlsx"
  
  Send Result:
  {"id":"0x4d5326eef692cb53d5cfb66e33571aba305848163318da85a334704143ae9c22"}

```



#### Send VET raw transactions:

```
  
java -jar thor-client-sdk4j-0.0.2.jar sendVETRaw {blockchain-server-url} {raw}

eg. java -jar thor-client-sdk4j-0.0.2.jar sendVETRaw "http://localhost:8669"  “0xf83d819a87027fdeb459bd708202d0e0df94d3ef28df6b553ed2fc47259e8134319cb1121a2a880f8b0a10e470000080808252088088a5366487948cf3edc0”
  
  Send Result:
  {"id":"0x4d5326eef692cb53d5cfb66e33571aba305848163318da85a334704143ae9c22"}

```





