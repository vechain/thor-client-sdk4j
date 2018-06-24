package com.vechain.thorclient.clients;

public class ThorMain {

    private static final String SIGN          = "sign";

    private static final String CREATE_WALLET = "createWallet";

    private static final String SEND          = "send";

    private static final String CHAIN_TAG     = "chainTag";

    private static final String BLOCK_REF     = "blockRef";

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("can not find arguments");
            return;
        }

        for (String arg : args) {
            System.out.println(arg);
        }

        if (args[0].equals(CHAIN_TAG)) {

        }

        if (args[0].equals(BLOCK_REF)) {

        }

        if (args[0].equals(CREATE_WALLET)) {

        }

        if (args[0].equals(SIGN)) {

        }

        if (args[0].equals(SEND)) {

        }

    }

}
