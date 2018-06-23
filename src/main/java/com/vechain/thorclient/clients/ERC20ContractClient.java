package com.vechain.thorclient.clients;

import com.vechain.thorclient.clients.base.AbstractClient;
import com.vechain.thorclient.core.model.blockchain.ContractCall;
import com.vechain.thorclient.core.model.blockchain.ContractCallResult;
import com.vechain.thorclient.core.model.clients.*;
import com.vechain.thorclient.core.model.clients.base.AbiDefinition;
import java.io.IOException;


public class ERC20ContractClient extends AbstractClient{

    /**
     * Get amount from ERC20 contract.
     * @param address address of token holder.
     * @param token {@link ERC20Token} required, the token {@link ERC20Token}
     * @param revision {@link Revision} if it is null, it will fallback to default {@link Revision.Best}
     * @return {@link Amount}
     * @throws IOException {@link IOException}
     */
    public static Amount getERC20Balance(Address address, ERC20Token token, Revision revision) throws IOException{
        Address contractAddr = token.getContractAddress();
        Revision currRevision = revision;
        if(currRevision == null){
            currRevision = Revision.BEST;
        }
        AbiDefinition abiDefinition = ERC20Contract.defaultERC20Contract.findAbiDefinition("balanceOf");
        ContractCall call = ERC20Contract.buildCall( abiDefinition, address.toHexString( null ) );
        ContractCallResult contractCallResult = AccountClient.callContractInfo(contractAddr,  currRevision,  call );
        if(contractCallResult == null){
            return null;
        }
        return contractCallResult.getBalance( token );
    }

}
