# VeChain Thor SDK - Model Classes Reference

## Core Data Types

### Address
Represents a VeChain address.

**Methods**:
- `fromHexString(String hex)`: Create from hex string
- `toHexString(Prefix prefix)`: Convert to hex string
- `getBytes()`: Get raw bytes

**Constants**:
- `Address.VTHO_Address`: VTHO token contract address
- `Address.NULL_ADDRESS`: Zero address

### Amount
Represents token amounts with decimal precision.

**Methods**:
- `createFromToken(AbstractToken token)`: Create for specific token
- `setDecimalAmount(String amount)`: Set decimal amount
- `toBigInteger()`: Convert to BigInteger (wei)
- `getDecimalAmount()`: Get decimal representation

### Revision
Represents block revision for queries.

**Static Methods**:
- `Revision.BEST`: Latest block
- `Revision.create(long blockNumber)`: Specific block number

## Transaction Models

### RawTransaction
Unsigned transaction data.

**Fields**:
- `chainTag`: Chain identifier
- `blockRef`: Reference block
- `expiration`: Block expiration
- `clauses`: Transaction clauses
- `gasPriceCoef`: Gas price coefficient
- `gas`: Gas limit
- `dependsOn`: Transaction dependency
- `nonce`: Transaction nonce
- `signature`: Transaction signature

### ToClause
Transaction clause specifying recipient, value, and data.

**Constructor**:
```java
ToClause(Address to, Amount value, ToData data)
```

### ToData
Transaction data payload.

**Methods**:
- `ToData.ZERO`: Empty data
- `setData(String hexData)`: Set hex data

### TransferResult
Result of transaction submission.

**Fields**:
- `id`: Transaction ID
- `success`: Success status

### Transaction
Complete transaction information.

**Fields**:
- `id`: Transaction ID
- `chainTag`: Chain tag
- `blockRef`: Block reference
- `expiration`: Expiration
- `clauses`: Transaction clauses
- `gasPriceCoef`: Gas price coefficient
- `gas`: Gas limit
- `origin`: Transaction origin
- `delegator`: Delegator address (if applicable)
- `size`: Transaction size
- `meta`: Transaction metadata

### Receipt
Transaction execution receipt.

**Fields**:
- `gasUsed`: Gas consumed
- `gasPayer`: Address that paid gas
- `paid`: Total payment
- `reward`: Block reward
- `reverted`: Execution status
- `meta`: Receipt metadata
- `outputs`: Transaction outputs

## Block Models

### Block
Blockchain block information.

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
- `isTrunk`: Trunk status
- `transactions`: Transaction list

**Methods**:
- `blockRef()`: Get BlockRef for transactions

### BlockRef
Block reference for transaction construction.

**Methods**:
- `toByteArray()`: Convert to byte array

## Account Models

### Account
Account state information.

**Fields**:
- `balance`: VET balance (hex string)
- `energy`: VTHO balance (hex string)
- `hasCode`: Contract indicator

### AccountCode
Contract bytecode information.

**Fields**:
- `code`: Contract bytecode (hex string)

### AccountCall
Account call request for contract interaction.

**Fields**:
- `clauses`: Call clauses
- `caller`: Caller address
- `gas`: Gas limit
- `gasPrice`: Gas price

### AccountCallResult
Result of account call.

**Fields**:
- `data`: Return data
- `events`: Emitted events
- `transfers`: Value transfers
- `gasUsed`: Gas consumed
- `reverted`: Execution status
- `vmError`: VM error message

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
ERC20 token specification.

**Constants**:
- `ERC20Token.VTHO`: VTHO token
- `ERC20Token.VET`: VET token (for compatibility)

**Fields**:
- `contractAddress`: Token contract address
- `decimals`: Token decimals
- `symbol`: Token symbol

### AbstractToken
Base token interface.

**Constants**:
- `AbstractToken.VET`: Native VET token

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
