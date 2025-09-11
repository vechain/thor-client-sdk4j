# VeChain Thor SDK - Model Classes Reference

## Core Data Types

### Address
Represents a VeChain address. See [Address Javadoc](../doc/com/vechain/thorclient/core/model/clients/Address.html) for complete method reference and constants.

### Amount
Represents token amounts with decimal precision. See [Amount Javadoc](../doc/com/vechain/thorclient/core/model/clients/Amount.html) for all methods and usage patterns.

### Revision
Represents block revision for queries. See [Revision Javadoc](../doc/com/vechain/thorclient/core/model/blockchain/Revision.html) for static methods and usage.

## Transaction Models

### RawTransaction
Unsigned transaction data. See [RawTransaction Javadoc](../doc/com/vechain/thorclient/core/model/blockchain/RawTransaction.html) for complete field reference.

### ToClause
Transaction clause specifying recipient, value, and data. See [ToClause Javadoc](../doc/com/vechain/thorclient/core/model/clients/ToClause.html) for constructor details.

### ToData
Transaction data payload. See [ToData Javadoc](../doc/com/vechain/thorclient/core/model/clients/ToData.html) for methods and constants.

### TransferResult
Result of transaction submission. See [TransferResult Javadoc](../doc/com/vechain/thorclient/core/model/clients/TransferResult.html) for field details.

### Transaction
Complete transaction information. See [Transaction Javadoc](../doc/com/vechain/thorclient/core/model/blockchain/Transaction.html) for complete field reference including EIP-1559 support.

### Receipt
Transaction execution receipt. See [Receipt Javadoc](../doc/com/vechain/thorclient/core/model/blockchain/Receipt.html) for complete field reference.

## Block Models

### Block
Blockchain block information. See [Block Javadoc](../doc/com/vechain/thorclient/core/model/blockchain/Block.html) for complete field reference and methods.

### BlockRef
Block reference for transaction construction. See [BlockRef Javadoc](../doc/com/vechain/thorclient/core/model/blockchain/BlockRef.html) for methods.

## Account Models

### Account
Account state information. See [Account Javadoc](../doc/com/vechain/thorclient/core/model/blockchain/Account.html) for field details.

### AccountCode
Contract bytecode information. See [AccountCode Javadoc](../doc/com/vechain/thorclient/core/model/blockchain/AccountCode.html) for field details.

### AccountCall
Account call request for contract interaction. See [AccountCall Javadoc](../doc/com/vechain/thorclient/core/model/blockchain/AccountCall.html) for field specifications.

### AccountCallResult
Result of account call. See [AccountCallResult Javadoc](../doc/com/vechain/thorclient/core/model/blockchain/AccountCallResult.html) for complete result structure.

## Contract Models

### ContractCall
Contract method call specification.

**Fields**:
- `data`: Call data (hex string)
- `value`: Value to send

### ContractCallResult
Contract call execution result.

**Fields**:
- `data`: Return data
- `events`: Emitted events
- `transfers`: Value transfers
- `gasUsed`: Gas consumed
- `reverted`: Execution status
- `vmError`: VM error message

**Methods**:
- `getBalance(ERC20Token token)`: Extract token balance

## Event and Log Models

### Event
Blockchain event log.

**Fields**:
- `address`: Contract address
- `topics`: Event topics
- `data`: Event data

### FilteredEvent
Filtered event with metadata.

**Fields**:
- `address`: Contract address
- `topics`: Event topics
- `data`: Event data
- `meta`: Event metadata

### FilteredLogEvent
Log event with filtering metadata.

**Fields**:
- `address`: Contract address
- `topics`: Event topics
- `data`: Event data
- `meta`: Log metadata

### EventFilter
Event filtering criteria.

**Fields**:
- `range`: Block range
- `options`: Pagination options
- `criteriaSet`: Filter criteria

### LogFilter
Log filtering specification.

**Fields**:
- `range`: Block range
- `options`: Pagination options
- `criteriaSet`: Filter criteria

## Transfer Models

### FilteredTransfer
VET transfer with metadata.

**Fields**:
- `sender`: Sender address
- `recipient`: Recipient address
- `amount`: Transfer amount
- `meta`: Transfer metadata

### FilteredTransferEvent
Transfer event with metadata.

**Fields**:
- `sender`: Sender address
- `recipient`: Recipient address
- `amount`: Transfer amount
- `meta`: Event metadata

### TransferFilter
Transfer filtering criteria.

**Fields**:
- `range`: Block range
- `options`: Pagination options
- `criteriaSet`: Address criteria

## Fee Models

### FeeHistoryResponse
Historical fee data.

**Fields**:
- `oldestBlock`: Oldest block in range
- `baseFeePerGas`: Base fees per block
- `gasUsedRatio`: Gas usage ratios
- `reward`: Priority fee percentiles

### MaxPriorityFeeResponse
Priority fee recommendation.

**Fields**:
- `maxPriorityFeePerGas`: Recommended priority fee

## Subscription Models

### BlockSubscribingRequest
Block subscription parameters.

**Fields**:
- `pos`: Starting position

### BlockSubscribingResponse
Block subscription notification.

**Fields**:
- `number`: Block number
- `id`: Block ID
- `size`: Block size
- `parentID`: Parent block ID
- `timestamp`: Block timestamp
- `gasLimit`: Gas limit
- `gasUsed`: Gas used
- `totalScore`: Total score
- `txsRoot`: Transactions root
- `stateRoot`: State root
- `receiptsRoot`: Receipts root
- `signer`: Block signer
- `beneficiary`: Beneficiary address
- `obsolete`: Obsolete flag

### EventSubscribingRequest
Event subscription parameters.

**Fields**:
- `pos`: Starting position
- `addr`: Contract address
- `t0`, `t1`, `t2`, `t3`, `t4`: Topic filters

### EventSubscribingResponse
Event subscription notification.

**Fields**:
- `address`: Contract address
- `topics`: Event topics
- `data`: Event data
- `meta`: Event metadata
- `obsolete`: Obsolete flag

### TransferSubscribingRequest
Transfer subscription parameters.

**Fields**:
- `pos`: Starting position
- `txOrigin`: Transaction origin
- `sender`: Transfer sender
- `recipient`: Transfer recipient

### TransferSubscribingResponse
Transfer subscription notification.

**Fields**:
- `sender`: Sender address
- `recipient`: Recipient address
- `amount`: Transfer amount
- `meta`: Transfer metadata
- `obsolete`: Obsolete flag

## Utility Models

### Range
Block range specification.

**Static Methods**:
- `createBlockRange(long from, long to)`: Create block range
- `createTimeRange(long from, long to)`: Create time range

### Options
Pagination options.

**Static Methods**:
- `create(int offset, int limit)`: Create pagination options

### Order
Result ordering specification.

**Values**:
- `Order.ASC`: Ascending order
- `Order.DESC`: Descending order

## Token Models

### ERC20Token
ERC20 token specification. See [ERC20Token Javadoc](../doc/com/vechain/thorclient/core/model/clients/ERC20Token.html) for constants and field details.

### AbstractToken
Base token interface. See [AbstractToken Javadoc](../doc/com/vechain/thorclient/core/model/clients/AbstractToken.html) for constants and methods.

## Error Models

### ClientIOException
Network and I/O related errors.

### ClientArgumentException
Invalid parameter errors.

## Node Models

### NodeProvider
Node connection configuration.

**Methods**:
- `getNodeProvider()`: Get singleton instance
- `setProvider(String url)`: Set node URL
- `setTimeout(int timeout)`: Set request timeout
- `getProvider()`: Get current provider URL
- `getWsProvider()`: Get WebSocket provider URL

### PeerStat
Node peer statistics.

**Fields**:
- `name`: Peer name
- `bestBlockID`: Best block ID
- `totalScore`: Total score
- `peerID`: Peer identifier
- `netAddr`: Network address
- `inbound`: Connection direction
- `duration`: Connection duration

### PeerStatList
Collection of peer statistics.

**Fields**:
- `peers`: List of peer stats

## Usage Examples

### Creating Transaction Clauses
```java
// VET transfer clause
ToClause vetClause = new ToClause(
    Address.fromHexString("0xRecipient"),
    Amount.createFromToken(AbstractToken.VET).setDecimalAmount("1.5"),
    ToData.ZERO
);

// Contract call clause
ToData callData = new ToData();
callData.setData("0xa9059cbb000000000000000000000000...");
ToClause contractClause = new ToClause(
    Address.fromHexString("0xContractAddress"),
    Amount.ZERO,
    callData
);
```

### Filtering Events
```java
EventFilter filter = new EventFilter();
filter.setRange(Range.createBlockRange(1000, 2000));
filter.setOptions(Options.create(0, 100));

// Add topic filters
EventCriteria criteria = new EventCriteria();
criteria.setAddress(Address.fromHexString("0xContractAddress"));
criteria.setTopic0("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef");
filter.addCriteria(criteria);
```

### Working with Amounts
```java
// Create VET amount
Amount vetAmount = Amount.createFromToken(AbstractToken.VET);
vetAmount.setDecimalAmount("10.5");

// Create VTHO amount
Amount vthoAmount = Amount.createFromToken(ERC20Token.VTHO);
vthoAmount.setDecimalAmount("1000");

// Convert to wei
BigInteger weiValue = vetAmount.toBigInteger();
```

This reference covers all major model classes used throughout the VeChain Thor SDK.
