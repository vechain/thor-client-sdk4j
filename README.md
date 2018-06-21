# Thor Java Client SDK

A SDK for client to call Restful API.

## 1、Query the transactions, account, blocks, receipt, blockRef, .
You can find the code from test code as follows:

```
@Test
	public void testGetBestBlock(){
		Block block = blockchainAPI.getBestBlock();
        logger.info("BestBlock Info  " + block);
        Assert.notNull(block, "BEST BLOCK IS NULL");
	}


	@Test
    public void testGetBlockByNumber(){
        Block block = blockchainAPI.getBlockByNumber(4);
        logger.info("Getting Block Info  " + JSON.toJSONString(block));
        Assert.notNull(block, "BLOCK IS NULL");
    }


    @Test
    public void testGetBlockByid(){
        Block block = blockchainAPI.getBlockById("0x000000016e44c77ae7b7b804df6af935586ecf641869ea479b0eb17b594b3fd5");
        logger.info("Getting Block Info  " + JSON.toJSONString(block));
        Assert.notNull(block, "BLOCK IS NULL");
    }

    @Test
    public void testGetBalance(){
        Account account = blockchainAPI.getBalance("0x7567D83b7b8d80ADdCb281A71d54Fc7B3364ffed","best");
        String balanceString =  account.getBalance();
        logger.info("Current block balance:" +balanceString);
        logger.info("balance: " + BytesUtils.balance(balanceString, 18, 2).toString());
        Assert.notNull(account, "Account is null");
    }

    @Test
    public void testGetBlockRef(){
        byte[] blockRef = blockchainAPI.getBestBlockRef();
        logger.info("Block reference: " + BytesUtils.toHexString(blockRef, Prefix.ZeroLowerX));
    }
```

## 2、Post raw transaction.

###  2.1 The detail step as follows

    - 1. Construct a raw transaction Of RawTransaction class without signature.
    - 2. Encode the raw transaction with RLP.
    - 3. Use blake2b 32 bytes algrithm to hash the rlp encoded transaction
    - 4. Sign the hash with private key.
    - 5. Construct a raw transaction of RawTransaction class with signature.
    - 6. Encode the raw transaction with RLP
    - 7. Convert the encoded byte array to hex string with '0x'.
    - 8. Post the Json string to VeChain blockchain node.


###  2.2 You can find all apis in BlockchainAPI.java


## 3、Create wallet and serialized to keystore string, load keystore and decrypt keypair.

You can find all apis in *WalletAPI.java*


## 3. How to run the test

### 3.1 First you need to replace <your thor node address> with the your blockchain node address in BaseTest.java under test directory .

    ```
    @Before
    public void setProvider() {
    		blockchainAPI = new BlockchainAPIImpl();
    		NodeProvider provider = new NodeProvider();
    		provider.setProvider("<your thor node address>");
    		provider.setTimeout(5000);
    		blockchainAPI.setProvider(provider);
    }
    ```

### 3.2 Run the test.

Some case may be failed because of the account or block is not existed on your blockchain env.

    ```
        mvn clean install
    ```
