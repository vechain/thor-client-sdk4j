package com.vechain.thorclient.clients;

import com.vechain.thorclient.core.model.clients.base.AbstractContract;

public class DataBusBuilderContract extends AbstractContract {

	public static final String ADD_USER = "addUser";
	public static final String SET_CREDITPLAN = "setCreditPlan";
	public DataBusBuilderContract() {
		super(DataBusBuilderABI);
	}

	private final static String DataBusBuilderABI = "[\n" + 
			"	{\n" + 
			"		\"constant\": false,\n" + 
			"		\"inputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"databus\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			},\n" + 
			"			{\n" + 
			"				\"name\": \"_newMaster\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"name\": \"setMaster\",\n" + 
			"		\"outputs\": [],\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"nonpayable\",\n" + 
			"		\"type\": \"function\"\n" + 
			"	},\n" + 
			"	{\n" + 
			"		\"constant\": false,\n" + 
			"		\"inputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"databus\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			},\n" + 
			"			{\n" + 
			"				\"name\": \"_user\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"name\": \"removeUser\",\n" + 
			"		\"outputs\": [],\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"nonpayable\",\n" + 
			"		\"type\": \"function\"\n" + 
			"	},\n" + 
			"	{\n" + 
			"		\"constant\": false,\n" + 
			"		\"inputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"databus\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			},\n" + 
			"			{\n" + 
			"				\"name\": \"_credit\",\n" + 
			"				\"type\": \"uint256\"\n" + 
			"			},\n" + 
			"			{\n" + 
			"				\"name\": \"_recoveryRate\",\n" + 
			"				\"type\": \"uint256\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"name\": \"setCreditPlan\",\n" + 
			"		\"outputs\": [],\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"nonpayable\",\n" + 
			"		\"type\": \"function\"\n" + 
			"	},\n" + 
			"	{\n" + 
			"		\"constant\": false,\n" + 
			"		\"inputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"databus\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			},\n" + 
			"			{\n" + 
			"				\"name\": \"_sponsor\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"name\": \"selectSponsor\",\n" + 
			"		\"outputs\": [],\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"nonpayable\",\n" + 
			"		\"type\": \"function\"\n" + 
			"	},\n" + 
			"	{\n" + 
			"		\"constant\": false,\n" + 
			"		\"inputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"databus\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			},\n" + 
			"			{\n" + 
			"				\"name\": \"_user\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"name\": \"addUser\",\n" + 
			"		\"outputs\": [],\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"nonpayable\",\n" + 
			"		\"type\": \"function\"\n" + 
			"	},\n" + 
			"	{\n" + 
			"		\"constant\": false,\n" + 
			"		\"inputs\": [],\n" + 
			"		\"name\": \"build\",\n" + 
			"		\"outputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"nonpayable\",\n" + 
			"		\"type\": \"function\"\n" + 
			"	}\n" + 
			"]";
}
