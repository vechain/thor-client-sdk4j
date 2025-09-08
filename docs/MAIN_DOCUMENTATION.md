# VeChain Thor Client SDK for Java - Partner Integration Guide

## Table of Contents
1. [Quick Start](#quick-start)
2. [Setup & Configuration](#setup--configuration)
3. [Blocks](#blocks)
4. [Transactions](#transactions)
5. [Accounts](#accounts)
6. [ERC20 Tokens](#erc20-tokens)
7. [Events & Logs](#events--logs)
8. [Fees](#fees)
9. [Subscriptions](#subscriptions)
10. [Multi-Party Payment (MPP)](#multi-party-payment-mpp)
11. [Error Handling](#error-handling)
12. [Best Practices](#best-practices)

## Quick Start

### Maven Dependency
```xml
<dependency>
    <groupId>com.vechain</groupId>
    <artifactId>thor-client-sdk4j</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Basic Setup
```java
import com.vechain.thorclient.core.model.blockchain.NodeProvider;

// Configure node provider
NodeProvider nodeProvider = NodeProvider.getNodeProvider();
nodeProvider.setProvider("https://mainnet.veblocks.net");
nodeProvider.setTimeout(10000);
```

## Setup & Configuration

### Node Provider Configuration
```java
// Mainnet
nodeProvider.setProvider("https://mainnet.veblocks.net");

// Testnet
nodeProvider.setProvider("https://testnet.veblocks.net");

// Local development
nodeProvider.setProvider("http://localhost:8669");
```

### Wallet Creation & Management
```java
import com.vechain.thorclient.utils.WalletUtils;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

// Create new wallet
WalletInfo walletInfo = WalletUtils.createWallet("password123");
String keyStore = walletInfo.toKeystoreString();
ECKeyPair keyPair = walletInfo.getKeyPair();

// Load existing wallet
WalletInfo loadedWallet = WalletUtils.loadKeystore(keyStore, "password123");
```

## Blocks

### Overview
Blocks are the fundamental units of the VeChain blockchain, containing transactions and state changes. The SDK provides comprehensive block querying capabilities.

### Data Types

#### Block
Represents a complete blockchain block.

**Fields**:
- `number`: Block number (string)
- `id`: Block ID (hex string)
- `size`: Block size in bytes
- `parentID`: Parent block ID
- `timestamp`: Block timestamp (Unix)
- `gasLimit`: Block gas limit
- `gasUsed`: Gas used in block
- `totalScore`: Cumulative difficulty
- `txsRoot`: Transactions merkle root
- `stateRoot`: State merkle root
- `receiptsRoot`: Receipts merkle root
- `signer`: Block signer address
- `beneficiary`: Block reward recipient
- `isTrunk`: Whether block is on main chain
- `transactions`: Array of transaction IDs or full transactions
- `baseFeePerGas`: Base fee per gas (Galactica)

#### BlockRef
Block reference used for transaction construction.

**Methods**:
- `toByteArray()`: Convert to 8-byte array for transactions

#### Revision
Specifies which block to query.

**Types**:
- `Revision.BEST`: Latest block
- `Revision.create(blockNumber)`: Specific block number
- `null`: Defaults to best block

### Functions

#### 1. Get Block
**Function**: `BlockClient.getBlock(Revision revision)`

**Parameters**:
- `revision`: Block to retrieve (null for latest)

**Returns**: `Block` object

**Example**:
```java
// Get latest block
Block latestBlock = BlockClient.getBlock(null);
System.out.println("Latest block: " + latestBlock.getNumber());
System.out.println("Block ID: " + latestBlock.getId());
System.out.println("Timestamp: " + latestBlock.getTimestamp());

// Get specific block
Block block = BlockClient.getBlock(Revision.create(1000000));
System.out.println("Block 1000000 signer: " + block.getSigner());
```

#### 2. Get Expanded Block
**Function**: `BlockClient.getBlockExpanded(Revision revision)`

**Parameters**:
- `revision`: Block to retrieve

**Returns**: `Block` object with full transaction details

**Example**:
```java
Block expandedBlock = BlockClient.getBlockExpanded(null);
System.out.println("Transactions in block: " + expandedBlock.getTransactions().size());
```

#### 3. Get Chain Tag
**Function**: `BlockchainClient.getChainTag()`

**Returns**: `byte` - Last byte of genesis block ID (chain identifier)

**Example**:
```java
byte chainTag = BlockchainClient.getChainTag();
System.out.println("Chain tag: " + chainTag);
// Mainnet: 39, Testnet: 39
```

#### 4. Get Block Reference
**Function**: `BlockchainClient.getBlockRef(Revision revision)`

**Parameters**:
- `revision`: Block to reference (null for best)

**Returns**: `BlockRef` for transaction construction

**Example**:
```java
BlockRef blockRef = BlockchainClient.getBlockRef(null);
byte[] refBytes = blockRef.toByteArray();
// Use in transaction construction
```

#### 5. Get Peer Status
**Function**: `BlockchainClient.getPeerStatusList()`

**Returns**: `PeerStatList` with connected node information

**Example**:
```java
PeerStatList peers = BlockchainClient.getPeerStatusList();
for (PeerStat peer : peers.getPeers()) {
    System.out.println("Peer: " + peer.getName());
    System.out.println("Best block: " + peer.getBestBlockID());
}
```

### Complete Block Example
```java
public class BlockExplorer {
    public void exploreLatestBlock() throws ClientIOException {
        // Get latest block
        Block block = BlockClient.getBlock(null);
        
        System.out.println("=== Block Information ===");
        System.out.println("Number: " + block.getNumber());
        System.out.println("ID: " + block.getId());
        System.out.println("Size: " + block.getSize() + " bytes");
        System.out.println("Timestamp: " + new Date(block.getTimestamp() * 1000));
        System.out.println("Gas Used: " + block.getGasUsed() + "/" + block.getGasLimit());
        System.out.println("Signer: " + block.getSigner());
        System.out.println("Transaction count: " + block.getTransactions().size());
        
        // Get block reference for transactions
        BlockRef ref = block.blockRef();
        System.out.println("Block reference: " + BytesUtils.toHexString(ref.toByteArray()));
        
        // Check if this is the main chain
        System.out.println("Is trunk: " + block.isTrunk());
    }
}
```

## Transactions

### Overview
Transactions are state-changing operations on the VeChain blockchain. The SDK provides comprehensive transaction creation, signing, and querying capabilities.

### Data Types

#### Transaction
Complete transaction information.

**Fields**:
- `id`: Transaction ID (hex string)
- `chainTag`: Chain identifier
- `blockRef`: Reference block (8 bytes)
- `expiration`: Block expiration
- `clauses`: Array of transaction clauses
- `gasPriceCoef`: Gas price coefficient
- `gas`: Gas limit
- `origin`: Transaction sender
- `delegator`: Fee delegator (if applicable)
- `size`: Transaction size
- `meta`: Transaction metadata
- `type`: Transaction type (0=legacy, 2=EIP-1559)
- `maxFeePerGas`: Maximum fee per gas (EIP-1559)
- `maxPriorityFeePerGas`: Maximum priority fee (EIP-1559)

#### RawTransaction
Unsigned transaction data for construction.

**Fields**:
- `chainTag`: Chain identifier
- `blockRef`: Reference block bytes
- `expiration`: Block expiration
- `clauses`: Transaction clauses
- `gasPriceCoef`: Gas price coefficient
- `gas`: Gas limit
- `dependsOn`: Transaction dependency
- `nonce`: Random nonce
- `signature`: Transaction signature
- `maxFeePerGas`: Maximum fee (EIP-1559)
- `maxPriorityFeePerGas`: Priority fee (EIP-1559)

#### ToClause
Transaction clause specifying recipient, value, and data.

**Constructor**:
```java
ToClause(Address to, Amount value, ToData data)
```

#### Receipt
Transaction execution result.

**Fields**:
- `gasUsed`: Gas consumed
- `gasPayer`: Address that paid gas
- `paid`: Total VTHO paid
- `reward`: Block reward
- `reverted`: Whether execution failed
- `meta`: Receipt metadata
- `outputs`: Array of clause outputs
- `type`: Transaction type

#### TransferResult
Result of transaction submission.

**Fields**:
- `id`: Transaction ID

### Functions

#### 1. Get Transaction
**Function**: `TransactionClient.getTransaction(String txId, boolean isRaw, Revision revision)`

**Parameters**:
- `txId`: Transaction ID (hex with 0x prefix)
- `isRaw`: Return raw transaction data
- `revision`: Block revision

**Returns**: `Transaction` object

**Example**:
```java
Transaction tx = TransactionClient.getTransaction(
    "0x2c405851ca789f48c51c8ede5fadf682fc6636a9aa7ca366328332a3472326ae",
    false,
    null
);
System.out.println("Gas used: " + tx.getGas());
System.out.println("Origin: " + tx.getOrigin());
System.out.println("Clauses: " + tx.getClauses().length);
```

#### 2. Get Transaction Receipt
**Function**: `TransactionClient.getTransactionReceipt(String txId, Revision revision)`

**Parameters**:
- `txId`: Transaction ID
- `revision`: Block revision

**Returns**: `Receipt` object

**Example**:
```java
Receipt receipt = TransactionClient.getTransactionReceipt(
    "0x6b99c0f1ebfa3b9d93dcfc503f468104ac74271728841551aaa44115d080f5b5",
    null
);
System.out.println("Gas used: " + receipt.getGasUsed());
System.out.println("Reverted: " + receipt.isReverted());
System.out.println("Events: " + receipt.getOutputs()[0].getEvents().size());
```

#### 3. Build VET Transfer Clause
**Function**: `TransactionClient.buildVETToClause(Address toAddress, Amount amount, ToData data)`

**Parameters**:
- `toAddress`: Recipient address
- `amount`: VET amount to transfer
- `data`: Additional data

**Returns**: `ToClause`

**Example**:
```java
ToClause clause = TransactionClient.buildVETToClause(
    Address.fromHexString("0xRecipientAddress"),
    Amount.createFromToken(AbstractToken.VET).setDecimalAmount("1.5"),
    ToData.ZERO
);
```

#### 4. Transfer Transaction
**Function**: `TransactionClient.transfer(RawTransaction transaction)`

**Parameters**:
- `transaction`: Signed raw transaction

**Returns**: `TransferResult`

**Example**:
```java
// Build transaction
ToClause clause = TransactionClient.buildVETToClause(
    Address.fromHexString("0xRecipientAddress"),
    Amount.createFromToken(AbstractToken.VET).setDecimalAmount("1.5"),
    ToData.ZERO
);

RawTransaction rawTx = RawTransactionFactory.getInstance().createRawTransaction(
    BlockchainClient.getChainTag(),
    BlockchainClient.getBlockRef(null).toByteArray(),
    720,  // expiration (2 hours)
    21000, // gas limit
    (byte) 0x0, // gas coefficient
    CryptoUtils.generateTxNonce(),
    clause
);

// Sign and send
TransferResult result = TransactionClient.signThenTransfer(rawTx, keyPair);
System.out.println("Transaction ID: " + result.getId());
```

#### 5. Sign Transaction
**Function**: `TransactionClient.sign(RawTransaction rawTransaction, ECKeyPair keyPair)`

**Parameters**:
- `rawTransaction`: Unsigned transaction
- `keyPair`: Private key pair

**Returns**: Signed `RawTransaction`

#### 6. Deploy Contract
**Function**: `TransactionClient.deployContract(String contractHex, int gas, byte gasCoef, int expiration, ECKeyPair keyPair)`

**Parameters**:
- `contractHex`: Contract bytecode
- `gas`: Gas limit
- `gasCoef`: Gas coefficient
- `expiration`: Block expiration
- `keyPair`: Deployer's key pair

**Returns**: `TransferResult`

**Example**:
```java
TransferResult result = TransactionClient.deployContract(
    "0x608060405234801561001057600080fd5b50...",
    1000000,
    (byte) 0,
    720,
    keyPair
);
System.out.println("Contract deployed: " + result.getId());
```

#### 7. EIP-1559 Contract Deployment
**Function**: `TransactionClient.deployContract(String contractHex, int gas, BigInteger maxPriorityFeePerGas, BigInteger maxFeePerGas, int expiration, ECKeyPair keyPair)`

For Galactica dynamic fee transactions.

### Complete Transaction Example
```java
public class TransactionManager {
    private ECKeyPair keyPair;
    
    public String sendVET(String toAddress, String amount) throws ClientIOException {
        // Build transaction clause
        ToClause clause = TransactionClient.buildVETToClause(
            Address.fromHexString(toAddress),
            Amount.createFromToken(AbstractToken.VET).setDecimalAmount(amount),
            ToData.ZERO
        );
        
        // Create raw transaction
        RawTransaction rawTx = RawTransactionFactory.getInstance().createRawTransaction(
            BlockchainClient.getChainTag(),
            BlockchainClient.getBlockRef(null).toByteArray(),
            720,
            21000,
            (byte) 0x0,
            CryptoUtils.generateTxNonce(),
            clause
        );
        
        // Sign and send
        TransferResult result = TransactionClient.signThenTransfer(rawTx, keyPair);
        
        // Verify transaction ID matches
        String calculatedId = BlockchainUtils.generateTransactionId(
            rawTx, 
            Address.fromHexString(keyPair.getHexAddress())
        );
        assert result.getId().equals(calculatedId);
        
        return result.getId();
    }
    
    public void checkTransactionStatus(String txId) throws ClientIOException {
        // Get transaction details
        Transaction tx = TransactionClient.getTransaction(txId, false, null);
        System.out.println("Transaction found in block: " + tx.getMeta().getBlockNumber());
        
        // Get receipt for execution details
        Receipt receipt = TransactionClient.getTransactionReceipt(txId, null);
        System.out.println("Gas used: " + receipt.getGasUsed());
        System.out.println("Success: " + !receipt.isReverted());
        
        if (receipt.getOutputs().length > 0) {
            System.out.println("Events emitted: " + receipt.getOutputs()[0].getEvents().size());
            System.out.println("Transfers: " + receipt.getOutputs()[0].getTransfers().size());
        }
    }
}
```

## Accounts

### Overview
Accounts represent addresses on the VeChain blockchain, containing VET/VTHO balances and potentially smart contract code.

### Data Types

#### Account
Account state information.

**Fields**:
- `balance`: VET balance (hex string in wei)
- `energy`: VTHO balance (hex string in wei)  
- `hasCode`: Whether address contains contract code

#### AccountCode
Contract bytecode at an address.

**Fields**:
- `code`: Contract bytecode (hex string)

#### AccountCall
Request for contract view function calls.

**Fields**:
- `clauses`: Array of call clauses
- `caller`: Caller address
- `gas`: Gas limit
- `gasPrice`: Gas price

#### AccountCallResult
Result of account call execution.

**Fields**:
- `data`: Return data
- `events`: Emitted events
- `transfers`: Value transfers
- `gasUsed`: Gas consumed
- `reverted`: Execution status
- `vmError`: VM error message

### Functions

#### 1. Get Account Information
**Function**: `AccountClient.getAccountInfo(Address address, Revision revision)`

**Parameters**:
- `address`: Account address to query
- `revision`: Block revision (null for latest)

**Returns**: `Account` object

**Example**:
```java
Account account = AccountClient.getAccountInfo(
    Address.fromHexString("0xYourAddress"),
    null
);
System.out.println("VET Balance: " + account.getBalance());
System.out.println("VTHO Balance: " + account.getEnergy());
System.out.println("Has Code: " + account.isHasCode());
```

#### 2. Get Account Code
**Function**: `AccountClient.getAccountCode(Address address, Revision revision)`

**Parameters**:
- `address`: Contract address
- `revision`: Block revision

**Returns**: `AccountCode` object

**Example**:
```java
AccountCode code = AccountClient.getAccountCode(
    Address.VTHO_Address,
    null
);
System.out.println("Contract code: " + code.getCode());
```

#### 3. Perform Account Call
**Function**: `AccountClient.performAccountCall(Revision revision, AccountCall accountCall)`

**Parameters**:
- `revision`: Block revision
- `accountCall`: Call specification

**Returns**: `AccountCallResult`

**Example**:
```java
AccountCall call = new AccountCall();
ArrayList<ToClause> clauses = new ArrayList<>();
clauses.add(new ToClause(
    Address.VTHO_Address,
    Amount.ZERO,
    new ToData("0x70a08231000000000000000000000000" + "YourAddress")
));
call.setClauses(clauses);

AccountCallResult result = AccountClient.performAccountCall(null, call);
System.out.println("Call result: " + result.getData());
```

#### 4. Get Storage Value
**Function**: `AccountClient.getStorageAt(Address address, StorageKey key, Revision revision)`

**Parameters**:
- `address`: Contract address
- `key`: Storage key
- `revision`: Block revision

**Returns**: `StorageData`

## ERC20 Tokens

### Overview
ERC20 tokens on VeChain, including the native VTHO token. The SDK provides specialized functions for token operations.

### Data Types

#### ERC20Token
Token specification.

**Constants**:
- `ERC20Token.VTHO`: VTHO token contract
- `ERC20Token.VET`: VET compatibility token

**Fields**:
- `contractAddress`: Token contract address
- `decimals`: Token decimal places
- `symbol`: Token symbol

#### Amount
Token amount with decimal precision.

**Methods**:
- `createFromToken(token)`: Create for specific token
- `setDecimalAmount(String)`: Set decimal amount
- `toBigInteger()`: Convert to wei
- `getDecimalAmount()`: Get decimal string

### Functions

#### 1. Get Token Balance
**Function**: `ERC20ContractClient.getERC20Balance(Address address, ERC20Token token, Revision revision)`

**Parameters**:
- `address`: Token holder address
- `token`: ERC20Token specification
- `revision`: Block revision

**Returns**: `Amount` object

**Example**:
```java
Amount balance = ERC20ContractClient.getERC20Balance(
    Address.fromHexString("0xYourAddress"),
    ERC20Token.VTHO,
    null
);
```

#### 2. Transfer Tokens (Legacy)
**Function**: `transferERC20Token(ERC20Token token, Address[] receivers, Amount[] amounts, int gas, byte gasCoef, int expiration, ECKeyPair keyPair)`

#### 3. Transfer Tokens (EIP-1559)
**Function**: `transferERC20Token(ERC20Token token, Address[] receivers, Amount[] amounts, int gas, BigInteger maxFeePerGas, BigInteger maxPriorityFeePerGas, int expiration, ECKeyPair keyPair)`

**Example**:
```java
TransferResult result = ERC20ContractClient.transferERC20Token(
    ERC20Token.VTHO,
    new Address[]{Address.fromHexString("0xRecipient")},
    new Amount[]{Amount.createFromToken(ERC20Token.VTHO).setDecimalAmount("100")},
    80000,
    new BigInteger("1000000000"), // maxFeePerGas
    new BigInteger("1000000000"), // maxPriorityFeePerGas
    720,
    keyPair
);
```

## Event & Log Monitoring

### LogsClient Functions

#### 1. Filter Event Logs
**Function**: `getFilteredLogEvents(LogFilter logFilter)`

**Example**:
```java
LogFilter filter = new LogFilter();
filter.setRange(Range.createBlockRange(1000, 2000));
filter.setOptions(Options.create(0, 100));

ArrayList<FilteredLogEvent> events = LogsClient.getFilteredLogEvents(filter);
```

#### 2. Filter Transfer Logs
**Function**: `getFilteredTransferLogs(TransferredFilter transferredFilter)`

### SubscribeClient Functions

#### 1. Subscribe to Blocks
**Function**: `subscribeBlock(BlockSubscribingRequest request, SubscribingCallback<BlockSubscribingResponse> callback)`

#### 2. Subscribe to Events
**Function**: `subscribeEvent(EventSubscribingRequest request, SubscribingCallback<EventSubscribingResponse> callback)`

#### 3. Subscribe to Transfers
**Function**: `subscribeTransfer(TransferSubscribingRequest request, SubscribingCallback<TransferSubscribingResponse> callback)`

**Example**:
```java
SubscribeSocket socket = SubscribeClient.subscribeBlock(
    null, // null for best blocks
    new SubscribingCallback<BlockSubscribingResponse>() {
        @Override
        public void onSubscribe(BlockSubscribingResponse response) {
            System.out.println("New block: " + response.getNumber());
        }
        
        @Override
        public void onError(Exception error) {
            error.printStackTrace();
        }
    }
);
```

## Fee Management

### FeeClient Functions

#### 1. Get Fee History
**Function**: `getFeeHistory(Number blockCount, String rewardPercentiles, String newestBlock)`

**Example**:
```java
FeeHistoryResponse history = FeeClient.getFeeHistory(
    10,           // last 10 blocks
    "25,50,75",   // percentiles
    "latest"      // newest block
);
```

#### 2. Get Priority Fee
**Function**: `getPriorityFee()`

**Returns**: `BigInteger` - Suggested priority fee for next block inclusion

## Error Handling

### Common Exceptions

- `ClientIOException`: Network or API errors
- `ClientArgumentException`: Invalid parameters

**Example**:
```java
try {
    Transaction tx = TransactionClient.getTransaction(txId, false, null);
} catch (ClientIOException e) {
    // Handle network errors
    logger.error("Network error: " + e.getMessage());
} catch (ClientArgumentException e) {
    // Handle invalid parameters
    logger.error("Invalid argument: " + e.getMessage());
}
```

## Best Practices

### 1. Transaction Construction
- Always use current chain tag and block reference
- Set appropriate gas limits (21000 for VET transfers, 80000+ for contract calls)
- Use proper expiration values (720 blocks ≈ 2 hours)

### 2. Fee Management
- For Galactica network, use EIP-1559 transactions with dynamic fees
- Query fee history for optimal fee estimation
- Use priority fees for faster transaction inclusion

### 3. Error Handling
- Implement retry logic for network failures
- Validate addresses and amounts before transaction creation
- Check transaction receipts for execution status

### 4. Performance
- Reuse NodeProvider instances
- Cache frequently accessed data (chain tag, block references)
- Use appropriate timeouts for network requests

### 5. Security
- Never hardcode private keys
- Use secure key storage mechanisms
- Validate all user inputs before blockchain operations

## Complete Integration Example

```java
public class VeChainIntegration {
    private static final String NODE_URL = "https://mainnet.veblocks.net";
    private ECKeyPair keyPair;
    
    public void initialize() {
        // Setup node provider
        NodeProvider.getNodeProvider().setProvider(NODE_URL);
        NodeProvider.getNodeProvider().setTimeout(10000);
        
        // Load wallet
        keyPair = loadWalletFromKeystore();
    }
    
    public String sendVET(String toAddress, String amount) throws ClientIOException {
        // Build transaction clause
        ToClause clause = TransactionClient.buildVETToClause(
            Address.fromHexString(toAddress),
            Amount.createFromToken(AbstractToken.VET).setDecimalAmount(amount),
            ToData.ZERO
        );
        
        // Create raw transaction
        RawTransaction rawTx = RawTransactionFactory.getInstance().createRawTransaction(
            BlockchainClient.getChainTag(),
            BlockchainClient.getBlockRef(null).toByteArray(),
            720,
            21000,
            (byte) 0x0,
            CryptoUtils.generateTxNonce(),
            clause
        );
        
        // Sign and send
        TransferResult result = TransactionClient.signThenTransfer(rawTx, keyPair);
        return result.getId();
    }
    
    public Account getAccountInfo(String address) throws ClientIOException {
        return AccountClient.getAccountInfo(
            Address.fromHexString(address),
            null
        );
    }
}
```

This documentation provides comprehensive coverage of all SDK functions with practical examples for partner integration.
