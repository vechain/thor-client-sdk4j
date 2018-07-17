package com.vechain.thorclient.clients;

import com.vechain.thorclient.core.model.blockchain.ContractCall;
import com.vechain.thorclient.core.model.blockchain.ContractCallResult;
import com.vechain.thorclient.core.model.blockchain.TransferResult;
import com.vechain.thorclient.core.model.clients.*;
import com.vechain.thorclient.core.model.clients.base.AbiDefinition;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.core.model.exception.ClientIOException;
import com.vechain.thorclient.utils.Prefix;
import com.vechain.thorclient.utils.crypto.ECKeyPair;


/**
 * ProtoType Contract is a native contract for user to do multiple parties payment(mpp).
 * <a href="https://github.com/vechain/thor/wiki/Prototype(CN)">Prototype</a>
 * <br>What is ProtoType for?</br>
 *
 * In common scenario, if a sender A want to make a transaction to target B,
 * the sender A need to pay the transaction fee(gas).
 *
 * However, for some realistic reason, the target A want to pay the transaction fee. Then the mpp can do such a thing.
 * The sender A can be a user of target B({@linkplain #addUsers(Address[], Address[], int, byte, int, ECKeyPair)}),
 * then the target B can set user plan (credit and recovery) for all senders.
 * After all the things is done, then the sender A do transaction to target B, if the fee is less than the credit,
 * the ProtoType native contract is going to book fee(gas) from target B's account.
 *
 * <br>How to use ProtoType?</br>
 * First, you must be the master of the to address. By call {@link #setMasterAddress}, then you can query the master by
 * {@link #getMasterAddress(Address, Revision)}
 *
 * Second, you as a Master, you can set add user to user plan. This step is to set candidate, the one you want to give
 * credit. {@link #addUsers(Address[], Address[], int, byte, int, ECKeyPair)}
 *
 * Third, set a user plan to target address for all users on the users list.
 * {@link #setCreditPlans(Address[], Amount[], Amount[], int, byte, int, ECKeyPair)}
 *
 *
 */
public class ProtoTypeContractClient extends TransactionClient {


    /**
     * Get a master address from target address.
     * @param target required {@link Address} target address, means transfer to address.
     * @param revision optional can be null {@link Revision} block revision.
     * @return Contract call result {@link Revision}
     * @throws ClientIOException
     */
    public static ContractCallResult getMasterAddress(Address target, Revision revision) throws ClientIOException {

        if(target == null){
            throw new IllegalArgumentException( "target is null" );
        }
        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "master" );
        if(abi == null){
            throw new IllegalArgumentException( "Can not find abi master method" );
        }
        ContractCall call = ProtoTypeContract.buildCall(abi, target.toHexString( Prefix.ZeroLowerX ));

        return callContract( call, ProtoTypeContract.ContractAddress, revision );
    }

    /**
     * Set master user, the caller must be the owner or master of target addresses.
     * @param targets {@link Address} arrays as target address.
     * @param newMasters {@link Address} arrays as new master address.
     * @param gas  contract invoker gas
     * @param gasCoef gas coef
     * @param expiration expiration, suggest 720
     * @param keyPair private key {@link ECKeyPair}
     * @return {@link TransferResult}
     * @throws ClientIOException network error.
     */
    public static TransferResult setMasterAddress(Address[] targets, Address[] newMasters, int gas, byte gasCoef, int expiration , ECKeyPair keyPair) throws ClientIOException {
        if(targets == null){
            throw ClientArgumentException.exception( "targets is null" );
        }
        if(newMasters == null){
            throw ClientArgumentException.exception( "newMasters in null" );
        }
        if(targets.length != newMasters.length){
            throw ClientArgumentException.exception( "targets size must equal to newMasters size. " );
        }

        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "setMaster" );
        if(abi == null){
            throw new IllegalArgumentException( "Can not find abi master method" );
        }
        ToClause[] clauses = new ToClause[targets.length];
        for(int index = 0; index < targets.length; index ++){
            clauses[index] = ProtoTypeContract.buildToClause( ProtoTypeContract.ContractAddress, abi, targets[index].toHexString( Prefix.ZeroLowerX ),newMasters[index].toHexString( Prefix.ZeroLowerX ));

        }
        return invokeContractMethod( clauses,gas,gasCoef,expiration, keyPair );
    }

    /**
     * Add user to user plan, the caller must be the owner or master of target addresses.
     * @param targets {@link Address} arrays.
     * @param users {@link Address} arrays.
     * @param gas  a contract invoker gas
     * @param gasCoef gas coef
     * @param expiration expiration, suggest it is 720.
     * @param keyPair {@link ECKeyPair}
     * @return {@link TransferResult}
     * @throws ClientIOException network error.
     */
    public static TransferResult addUsers(Address[] targets, Address[] users, int gas, byte gasCoef, int expiration , ECKeyPair keyPair) throws ClientIOException{
        if(targets == null){
            throw ClientArgumentException.exception( "targets is null" );
        }
        if(users == null){
            throw ClientArgumentException.exception( "users is null" );
        }
        if(targets.length != users.length){
            throw ClientArgumentException.exception( "targets size must equal to users size. " );
        }
        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "addUser" );
        if(abi == null){
            throw new IllegalArgumentException( "Can not find abi master method" );
        }
        ToClause[] clauses = new ToClause[targets.length];
        for(int index = 0; index < targets.length; index ++){
            clauses[index] = ProtoTypeContract.buildToClause(
                    ProtoTypeContract.ContractAddress,
                    abi,
                    targets[index].toHexString( Prefix.ZeroLowerX ),
                    users[index].toHexString( Prefix.ZeroLowerX ));

        }
        return invokeContractMethod( clauses,gas,gasCoef,expiration, keyPair );

    }

    /**
     * Remove user from target addresses, the caller must be the owner or master of target addresses.
     * @param targets {@link Address} arrays.
     * @param users {@link Address} arrays.
     * @param gas  a contract invoker gas
     * @param gasCoef gas coef
     * @param expiration expiration, suggest it is 720.
     * @param keyPair {@link ECKeyPair}
     * @return {@link TransferResult}
     * @throws ClientIOException network error.
     */
    public  static TransferResult removeUsers(Address[] targets, Address[] users, int gas, byte gasCoef, int expiration , ECKeyPair keyPair) throws ClientIOException {
        if(targets == null){
            throw ClientArgumentException.exception( "targets is null" );
        }
        if(users == null){
            throw ClientArgumentException.exception( "users is null" );
        }
        if(targets.length != users.length){
            throw ClientArgumentException.exception( "targets size must equal to users size. " );
        }
        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "removeUser" );
        if(abi == null){
            throw new IllegalArgumentException( "Can not find abi master method" );
        }
        ToClause[] clauses = new ToClause[targets.length];
        for(int index = 0; index < targets.length; index ++){
            clauses[index] = ProtoTypeContract.buildToClause(
                    ProtoTypeContract.ContractAddress,
                    abi,
                    targets[index].toHexString( Prefix.ZeroLowerX ),
                    users[index].toHexString( Prefix.ZeroLowerX ));

        }
        return invokeContractMethod( clauses,gas,gasCoef,expiration, keyPair );
    }

    /**
     * Set user plan, the caller must be the owner or master of target addresses.
     * @param targets {@link Address} array.
     * @param credits {@link Amount} array.
     * @param recoveryRates {@link Amount} array. thor per seconds.
     * @return {@link TransferResult}
     * @throws ClientIOException network error.
     */
    public static TransferResult setCreditPlans( Address[] targets,
                                      Amount[] credits, Amount[] recoveryRates,
                                      int gas, byte gasCoef, int expiration , ECKeyPair keyPair
    ) throws ClientIOException {

        if(targets == null){
            throw ClientArgumentException.exception( "targets is null" );
        }
        if(credits == null){
            throw ClientArgumentException.exception( "credits is null" );
        }
        if(recoveryRates == null){
            throw ClientArgumentException.exception( "recoveries is null" );
        }
        if(targets.length != credits.length || targets.length != recoveryRates.length){
            throw ClientArgumentException.exception( "users.length must equal to credits.length and equal to recoveries.length" );
        }
        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "setCreditPlan" );
        if(abi == null){
            throw new IllegalArgumentException( "Can not find abi master method" );
        }
        ToClause[] clauses = new ToClause[targets.length];
        for(int index = 0; index < targets.length; index ++){
            clauses[index] = ProtoTypeContract.buildToClause(
                    ProtoTypeContract.ContractAddress,
                    abi,
                    targets[index].toHexString( Prefix.ZeroLowerX ),
                    credits[index].toBigInteger(),
                    recoveryRates[index].toBigInteger());

        }
        return invokeContractMethod( clauses,gas,gasCoef,expiration, keyPair );
    }

    /**
     * Check if user address is a user of target address.
     * @param target required {@link Address} the target address.
     * @param user required {@link Address} the user address.
     * @param revision optional {@link Revision}.
     * @return {@link ContractCallResult}
     * @throws ClientIOException network error.
     */
    public static ContractCallResult isUser(Address target, Address user, Revision revision) throws ClientIOException{
        if(target == null){
            throw ClientArgumentException.exception( "target address is null" );
        }
        if(user == null){
            throw ClientArgumentException.exception( "user address is null" );
        }
        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "isUser" );
        ContractCall call = ProtoTypeContract.buildCall(abi,
                target.toHexString( Prefix.ZeroLowerX ) ,
                user.toHexString( Prefix.ZeroLowerX ));

        return callContract( call, ProtoTypeContract.ContractAddress,revision );
    }

    /**
     * Get user plan
     * @param target required {@link Address}
     * @param revision optional
     * @return {@link ContractCallResult}
     * @throws ClientIOException network error.
     */
    public static ContractCallResult getCreditPlan(Address target, Revision revision) throws ClientIOException {
        if(target == null){
            throw ClientArgumentException.exception( "target is null" );
        }
        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "creditPlan" );
        ContractCall call = ProtoTypeContract.buildCall(abi,
                target.toHexString( Prefix.ZeroLowerX ));

        return callContract( call, ProtoTypeContract.ContractAddress,revision );
    }


    /**
     * Get user credit from target address by some block revision.
     * @param target {@link Address} target address.
     * @param user {@link Address} user address.
     * @param revision {@link Revision} revision.
     * @return {@link ContractCallResult}
     * @throws ClientIOException network error.
     */
    public static ContractCallResult getUserCredit(Address target, Address user, Revision revision) throws ClientIOException{
        if(target == null){
            throw ClientArgumentException.exception( "target address is null" );
        }
        if(user == null){
            throw ClientArgumentException.exception( "user address is null" );
        }

        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "userCredit" );
        ContractCall call = ProtoTypeContract.buildCall(abi,
                target.toHexString( Prefix.ZeroLowerX ) ,
                user.toHexString( Prefix.ZeroLowerX ));

        return callContract( call, ProtoTypeContract.ContractAddress,revision );
    }


    /**
     * Sponsor the address, any address can sponsor target addresses.
     * @param targets required {@link Address} the targets address
     * @param gas required int gas
     * @param gasCoef required byte gas coef
     * @param expiration required int expiration
     * @param keyPair required {@link Address}
     * @return {@link TransferResult}
     * @throws ClientIOException network error.
     */
    public static TransferResult sponsor(Address[] targets,  int gas, byte gasCoef, int expiration , ECKeyPair keyPair)throws ClientIOException{
        if(targets == null){
            throw ClientArgumentException.exception( "targets is null" );
        }
        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "sponsor" );
        if(abi == null){
            throw new IllegalArgumentException( "Can not find abi master method" );
        }
        ToClause[] clauses = new ToClause[targets.length];

        for(int index = 0; index < targets.length; index ++){
            clauses[index] = ProtoTypeContract.buildToClause(
                    ProtoTypeContract.ContractAddress,
                    abi,
                    targets[index].toHexString( Prefix.ZeroLowerX )

                    );
        }
        return invokeContractMethod( clauses,gas,gasCoef,expiration, keyPair );
    }

    /**
     * Unsponsor the address, only sponsor can invoke this method.
     * @param targets required {@link Address} the targets address
     * @param gas required int gas
     * @param gasCoef required byte gas coef
     * @param expiration required int expiration
     * @param keyPair required {@link Address}
     * @return {@link TransferResult}
     * @throws ClientIOException network error.
     */
    public static TransferResult unsponsor(Address[] targets,  int gas, byte gasCoef, int expiration , ECKeyPair keyPair)throws ClientIOException{
        if(targets == null){
            throw ClientArgumentException.exception( "targets is null" );
        }
        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "unsponsor" );
        if(abi == null){
            throw new IllegalArgumentException( "Can not find abi master method" );
        }
        ToClause[] clauses = new ToClause[targets.length];

        for(int index = 0; index < targets.length; index ++){
            clauses[index] = ProtoTypeContract.buildToClause(
                    ProtoTypeContract.ContractAddress,
                    abi,
                    targets[index].toHexString( Prefix.ZeroLowerX )
            );
        }
        return invokeContractMethod( clauses,gas,gasCoef,expiration, keyPair );
    }

    /**
     * Select sponsors for targets addresses, the caller must be the owner or master of target addresses.
     * @param targets required {@link Address} array
     * @param sponsors required {@link Address} array
     * @param gas required int gas
     * @param gasCoef required byte gasCoef
     * @param expiration required int recommendation value is 720
     * @param keyPair required {@link ECKeyPair}
     * @return {@link TransferResult}
     * throw ClientIOException network error.
     */
    public static TransferResult selectSponsor(Address[] targets, Address[] sponsors, int gas, byte gasCoef, int expiration , ECKeyPair keyPair)throws ClientIOException{
        if (targets == null){
            throw ClientArgumentException.exception( "targets is null" );
        }
        if (sponsors == null){
            throw ClientArgumentException.exception( "sponsors is null" );
        }

        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "selectSponsor" );
        if(abi == null){
            throw new IllegalArgumentException( "Can not find abi master method" );
        }
        ToClause[] clauses = new ToClause[targets.length];

        for(int index = 0; index < targets.length; index ++){
            clauses[index] = ProtoTypeContract.buildToClause(
                    ProtoTypeContract.ContractAddress,
                    abi,
                    targets[index].toHexString( Prefix.ZeroLowerX ),
                    sponsors[index].toHexString( Prefix.ZeroLowerX )
            );
        }
        return invokeContractMethod( clauses,gas,gasCoef,expiration, keyPair );
    }

    /**
     * Get current sponsor from target address.
     * @param target {@link Address}
     * @param revision {@link Revision}
     * @return {@link ContractCallResult}
     * throw ClientIOException network error.
     */
    public static ContractCallResult getCurrentSponsor(Address target, Revision revision)throws ClientIOException{
        if(target == null){
            throw ClientArgumentException.exception( "target is null" );
        }
        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "currentSponsor" );
        ContractCall call = ProtoTypeContract.buildCall(abi,
                target.toHexString( Prefix.ZeroLowerX ));

        return callContract( call, ProtoTypeContract.ContractAddress,revision );
    }

    /**
     * Get boolean value if the sponsor address sponsored the target address.
     * @param target required {@link Address} target address.
     * @param sponsor required {@link Address} sponsor
     * @param revision optional {@link Revision} block revision
     * @return {@link ContractCallResult}
     * throw ClientIOException network error.
     */
    public static ContractCallResult isSponsor(Address target, Address sponsor, Revision revision)throws ClientIOException{
        if(target == null){
            throw ClientArgumentException.exception( "target is null" );
        }
        if(sponsor == null){
            throw ClientArgumentException.exception( "sponsor is null" );
        }
        AbiDefinition abi = ProtoTypeContract.defaultContract.findAbiDefinition( "isSponsor" );
        ContractCall call = ProtoTypeContract.buildCall(abi,
                target.toHexString( Prefix.ZeroLowerX ),
                sponsor.toHexString( Prefix.ZeroLowerX ));

        return callContract( call, ProtoTypeContract.ContractAddress,revision );
    }


}
