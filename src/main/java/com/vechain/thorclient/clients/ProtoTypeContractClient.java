package com.vechain.thorclient.clients;

import com.vechain.thorclient.clients.base.AbstractClient;
import com.vechain.thorclient.core.model.clients.Address;
import com.vechain.thorclient.core.model.clients.Amount;

import java.io.IOException;

/**
 * Created by albertma on 2018/6/23.
 */
public class ProtoTypeContractClient extends AbstractClient{


    /**
     *
     * @param user
     * @param credit
     * @return
     */
    public static String setUserCredit(Address user, Amount credit) throws IOException {
        return null;
    }


    /**
     *
     * @param user
     * @return
     */
    public static String removeUserCredit(Address user) throws IOException{
        return null;
    }



}
