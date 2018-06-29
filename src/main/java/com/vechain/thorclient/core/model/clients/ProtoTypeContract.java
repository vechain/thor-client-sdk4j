package com.vechain.thorclient.core.model.clients;

import com.vechain.thorclient.core.model.clients.base.AbstractContract;

/**
 * Created by albertma on 2018/6/23.
 */
public class ProtoTypeContract extends AbstractContract{
    public  static final Address ContractAddress = Address.fromHexString( "0x000000000000000000000050726f746f74797065" );


    public static ProtoTypeContract defaultContract = new ProtoTypeContract();

    private ProtoTypeContract(){
        super(ProtoTypeABI);
    }


    private  final static String ProtoTypeABI = "[\n" +
            "    {\n" +
            "        \"constant\": false,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"name\": \"_newMaster\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"setMaster\",\n" +
            "        \"outputs\": [\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"nonpayable\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": true,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"name\": \"_user\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"isUser\",\n" +
            "        \"outputs\": [\n" +
            "            {\n" +
            "                \"name\": \"\",\n" +
            "                \"type\": \"bool\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"view\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": true,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"name\": \"_key\",\n" +
            "                \"type\": \"bytes32\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"storageFor\",\n" +
            "        \"outputs\": [\n" +
            "            {\n" +
            "                \"name\": \"\",\n" +
            "                \"type\": \"bytes32\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"view\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": true,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"name\": \"_blockNumber\",\n" +
            "                \"type\": \"uint256\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"energy\",\n" +
            "        \"outputs\": [\n" +
            "            {\n" +
            "                \"name\": \"\",\n" +
            "                \"type\": \"uint256\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"view\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": false,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"name\": \"_user\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"removeUser\",\n" +
            "        \"outputs\": [\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"nonpayable\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": true,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"currentSponsor\",\n" +
            "        \"outputs\": [\n" +
            "            {\n" +
            "                \"name\": \"\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"view\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": false,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"name\": \"_credit\",\n" +
            "                \"type\": \"uint256\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"name\": \"_recoveryRate\",\n" +
            "                \"type\": \"uint256\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"setCreditPlan\",\n" +
            "        \"outputs\": [\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"nonpayable\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": false,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"name\": \"_sponsor\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"selectSponsor\",\n" +
            "        \"outputs\": [\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"nonpayable\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": true,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"name\": \"_blockNumber\",\n" +
            "                \"type\": \"uint256\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"balance\",\n" +
            "        \"outputs\": [\n" +
            "            {\n" +
            "                \"name\": \"\",\n" +
            "                \"type\": \"uint256\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"view\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": false,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"sponsor\",\n" +
            "        \"outputs\": [\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"nonpayable\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": true,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"creditPlan\",\n" +
            "        \"outputs\": [\n" +
            "            {\n" +
            "                \"name\": \"credit\",\n" +
            "                \"type\": \"uint256\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"name\": \"recoveryRate\",\n" +
            "                \"type\": \"uint256\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"view\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": false,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"name\": \"_user\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"addUser\",\n" +
            "        \"outputs\": [\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"nonpayable\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": true,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"hasCode\",\n" +
            "        \"outputs\": [\n" +
            "            {\n" +
            "                \"name\": \"\",\n" +
            "                \"type\": \"bool\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"view\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": true,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"master\",\n" +
            "        \"outputs\": [\n" +
            "            {\n" +
            "                \"name\": \"\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"view\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": true,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"name\": \"_user\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"userCredit\",\n" +
            "        \"outputs\": [\n" +
            "            {\n" +
            "                \"name\": \"\",\n" +
            "                \"type\": \"uint256\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"view\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": false,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"unsponsor\",\n" +
            "        \"outputs\": [\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"nonpayable\",\n" +
            "        \"type\": \"function\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"constant\": true,\n" +
            "        \"inputs\": [\n" +
            "            {\n" +
            "                \"name\": \"_self\",\n" +
            "                \"type\": \"address\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"name\": \"_sponsor\",\n" +
            "                \"type\": \"address\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"name\": \"isSponsor\",\n" +
            "        \"outputs\": [\n" +
            "            {\n" +
            "                \"name\": \"\",\n" +
            "                \"type\": \"bool\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"payable\": false,\n" +
            "        \"stateMutability\": \"view\",\n" +
            "        \"type\": \"function\"\n" +
            "    }\n" +
            "]";
}
