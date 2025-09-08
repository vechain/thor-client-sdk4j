# ProtoType Contract Client - Multi-Party Payment (MPP) Guide

## Overview

The ProtoType Contract is VeChain's native contract for Multi-Party Payment (MPP), enabling scenarios where transaction fees can be paid by a different party than the transaction sender. This is useful for dApps that want to sponsor user transactions or implement credit-based payment systems.

## Core Concepts

### Roles
- **Master**: Controls user management and credit plans for a target address
- **User**: Can send transactions with fees paid by the target address
- **Sponsor**: Provides VTHO to cover transaction fees
- **Target**: The address that receives transactions and may pay fees

### Workflow
1. Set a master for target addresses
2. Add users to the target's user plan
3. Set credit plans (credit amount and recovery rate)
4. Users can now send transactions with fees deducted from target's credit

## ProtoTypeContractClient Functions

### 1. Master Management

#### Get Master Address
**Function**: `getMasterAddress(Address target, Revision revision)`

**Parameters**:
- `target`: Target address to query
- `revision`: Block revision (null for latest)

**Returns**: `ContractCallResult` containing master address

**Example**:
```java
ContractCallResult result = ProtoTypeContractClient.getMasterAddress(
    Address.fromHexString("0xTargetAddress"),
    null
);
```

#### Set Master Address
**Function**: `setMasterAddress(Address[] targets, Address[] newMasters, int gas, byte gasCoef, int expiration, ECKeyPair keyPair)`

**Parameters**:
- `targets`: Array of target addresses
- `newMasters`: Array of new master addresses
- `gas`: Gas limit
- `gasCoef`: Gas coefficient
- `expiration`: Transaction expiration
- `keyPair`: Current owner/master's key pair

**Returns**: `TransferResult`

**Example**:
```java
TransferResult result = ProtoTypeContractClient.setMasterAddress(
    new Address[]{Address.fromHexString("0xTargetAddress")},
    new Address[]{Address.fromHexString("0xNewMasterAddress")},
    100000,
    (byte) 0,
    720,
    masterKeyPair
);
```

### 2. User Management

#### Add Users
**Function**: `addUsers(Address[] targets, Address[] users, int gas, byte gasCoef, int expiration, ECKeyPair keyPair)`

**Parameters**:
- `targets`: Target addresses
- `users`: User addresses to add
- `gas`: Gas limit
- `gasCoef`: Gas coefficient
- `expiration`: Transaction expiration
- `keyPair`: Master's key pair

**Example**:
```java
TransferResult result = ProtoTypeContractClient.addUsers(
    new Address[]{Address.fromHexString("0xTargetAddress")},
    new Address[]{Address.fromHexString("0xUserAddress")},
    100000,
    (byte) 0,
    720,
    masterKeyPair
);
```

#### Remove Users
**Function**: `removeUsers(Address[] targets, Address[] users, int gas, byte gasCoef, int expiration, ECKeyPair keyPair)`

Similar to `addUsers` but removes users from the target's user plan.

#### Check if User
**Function**: `isUser(Address target, Address user, Revision revision)`

**Returns**: `ContractCallResult` with boolean indicating if address is a user

**Example**:
```java
ContractCallResult result = ProtoTypeContractClient.isUser(
    Address.fromHexString("0xTargetAddress"),
    Address.fromHexString("0xUserAddress"),
    null
);
```

### 3. Credit Plan Management

#### Set Credit Plans
**Function**: `setCreditPlans(Address[] targets, Amount[] credits, Amount[] recoveryRates, int gas, byte gasCoef, int expiration, ECKeyPair keyPair)`

**Parameters**:
- `targets`: Target addresses
- `credits`: Credit amounts (in VTHO wei)
- `recoveryRates`: Recovery rates (VTHO per second)
- `gas`: Gas limit
- `gasCoef`: Gas coefficient
- `expiration`: Transaction expiration
- `keyPair`: Master's key pair

**Example**:
```java
Amount credit = Amount.createFromToken(ERC20Token.VTHO);
credit.setDecimalAmount("1000"); // 1000 VTHO credit

Amount recoveryRate = Amount.createFromToken(ERC20Token.VTHO);
recoveryRate.setDecimalAmount("0.1"); // 0.1 VTHO per second recovery

TransferResult result = ProtoTypeContractClient.setCreditPlans(
    new Address[]{Address.fromHexString("0xTargetAddress")},
    new Amount[]{credit},
    new Amount[]{recoveryRate},
    100000,
    (byte) 0,
    720,
    masterKeyPair
);
```

#### Get Credit Plan
**Function**: `getCreditPlan(Address target, Revision revision)`

**Returns**: `ContractCallResult` with credit plan details

#### Get User Credit
**Function**: `getUserCredit(Address target, Address user, Revision revision)`

**Returns**: `ContractCallResult` with current user credit

**Example**:
```java
ContractCallResult result = ProtoTypeContractClient.getUserCredit(
    Address.fromHexString("0xTargetAddress"),
    Address.fromHexString("0xUserAddress"),
    null
);
```

### 4. Sponsorship Management

#### Sponsor Address
**Function**: `sponsor(Address[] targets, int gas, byte gasCoef, int expiration, ECKeyPair keyPair)`

**Parameters**:
- `targets`: Addresses to sponsor
- `gas`: Gas limit
- `gasCoef`: Gas coefficient
- `expiration`: Transaction expiration
- `keyPair`: Sponsor's key pair

**Example**:
```java
TransferResult result = ProtoTypeContractClient.sponsor(
    new Address[]{Address.fromHexString("0xTargetAddress")},
    100000,
    (byte) 0,
    720,
    sponsorKeyPair
);
```

#### Unsponsor Address
**Function**: `unsponsor(Address[] targets, int gas, byte gasCoef, int expiration, ECKeyPair keyPair)`

Removes sponsorship from target addresses.

#### Select Sponsor
**Function**: `selectSponsor(Address[] targets, Address[] sponsors, int gas, byte gasCoef, int expiration, ECKeyPair keyPair)`

**Parameters**:
- `targets`: Target addresses
- `sponsors`: Sponsor addresses to select
- `gas`: Gas limit
- `gasCoef`: Gas coefficient
- `expiration`: Transaction expiration
- `keyPair`: Master's key pair

#### Get Current Sponsor
**Function**: `getCurrentSponsor(Address target, Revision revision)`

**Returns**: `ContractCallResult` with current sponsor address

#### Check if Sponsor
**Function**: `isSponsor(Address target, Address sponsor, Revision revision)`

**Returns**: `ContractCallResult` with boolean indicating sponsorship status

## Complete MPP Implementation Example

```java
public class MPPImplementation {
    private ECKeyPair masterKeyPair;
    private ECKeyPair sponsorKeyPair;
    private ECKeyPair userKeyPair;
    
    public void setupMPP() throws ClientIOException {
        Address targetAddress = Address.fromHexString("0xTargetAddress");
        Address userAddress = Address.fromHexString("0xUserAddress");
        Address sponsorAddress = Address.fromHexString("0xSponsorAddress");
        
        // Step 1: Set master (if not already set)
        TransferResult masterResult = ProtoTypeContractClient.setMasterAddress(
            new Address[]{targetAddress},
            new Address[]{Address.fromHexString("0xMasterAddress")},
            100000,
            (byte) 0,
            720,
            masterKeyPair
        );
        
        // Step 2: Add user to target's user plan
        TransferResult userResult = ProtoTypeContractClient.addUsers(
            new Address[]{targetAddress},
            new Address[]{userAddress},
            100000,
            (byte) 0,
            720,
            masterKeyPair
        );
        
        // Step 3: Set credit plan
        Amount credit = Amount.createFromToken(ERC20Token.VTHO);
        credit.setDecimalAmount("1000"); // 1000 VTHO credit
        
        Amount recoveryRate = Amount.createFromToken(ERC20Token.VTHO);
        recoveryRate.setDecimalAmount("0.1"); // 0.1 VTHO per second
        
        TransferResult creditResult = ProtoTypeContractClient.setCreditPlans(
            new Address[]{targetAddress},
            new Amount[]{credit},
            new Amount[]{recoveryRate},
            100000,
            (byte) 0,
            720,
            masterKeyPair
        );
        
        // Step 4: Sponsor the target address
        TransferResult sponsorResult = ProtoTypeContractClient.sponsor(
            new Address[]{targetAddress},
            100000,
            (byte) 0,
            720,
            sponsorKeyPair
        );
        
        // Step 5: Select the sponsor
        TransferResult selectResult = ProtoTypeContractClient.selectSponsor(
            new Address[]{targetAddress},
            new Address[]{sponsorAddress},
            100000,
            (byte) 0,
            720,
            masterKeyPair
        );
    }
    
    public void checkMPPStatus(Address targetAddress, Address userAddress) throws ClientIOException {
        // Check if user is registered
        ContractCallResult isUserResult = ProtoTypeContractClient.isUser(
            targetAddress, userAddress, null
        );
        
        // Get user's current credit
        ContractCallResult creditResult = ProtoTypeContractClient.getUserCredit(
            targetAddress, userAddress, null
        );
        
        // Get credit plan
        ContractCallResult planResult = ProtoTypeContractClient.getCreditPlan(
            targetAddress, null
        );
        
        // Get current sponsor
        ContractCallResult sponsorResult = ProtoTypeContractClient.getCurrentSponsor(
            targetAddress, null
        );
        
        System.out.println("User registered: " + isUserResult.getData());
        System.out.println("Current credit: " + creditResult.getData());
        System.out.println("Credit plan: " + planResult.getData());
        System.out.println("Current sponsor: " + sponsorResult.getData());
    }
}
```

## Use Cases

### 1. dApp User Onboarding
- dApp sponsors new users by covering their transaction fees
- Users can interact with the dApp without holding VTHO
- Gradual transition to self-paying as users become engaged

### 2. Enterprise B2B Payments
- Company sets up MPP for employee transactions
- Employees can send transactions with company paying fees
- Credit limits and recovery rates control spending

### 3. Gaming Applications
- Game sponsors player transactions for better UX
- Players can perform in-game actions without gas fees
- Credit system prevents abuse while maintaining engagement

### 4. Subscription Services
- Service provider sponsors subscriber transactions
- Subscribers get allocated credits based on subscription tier
- Automatic credit recovery ensures continuous service

## Best Practices

### 1. Credit Management
- Set appropriate credit limits based on expected usage
- Use recovery rates to prevent credit exhaustion
- Monitor credit usage and adjust plans as needed

### 2. Security
- Carefully manage master key pairs
- Regularly audit user lists and credit allocations
- Implement monitoring for unusual spending patterns

### 3. Gas Optimization
- Batch operations when adding/removing multiple users
- Use appropriate gas limits for contract calls
- Consider gas costs when setting credit plans

### 4. Monitoring
- Track credit usage patterns
- Monitor sponsor VTHO balances
- Set up alerts for low credit situations

This guide provides comprehensive coverage of the ProtoType Contract functionality for implementing Multi-Party Payment solutions.
