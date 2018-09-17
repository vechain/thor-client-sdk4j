package com.vechain.thorclient.clients;


import com.vechain.thorclient.core.model.clients.base.AbstractContract;

public class AddressingContract extends AbstractContract {
	
	public final static String SET = "set";
	public final static String GET = "get";

	public AddressingContract() {
		super(AddressingABI);
	}

	private final static String AddressingABI = "[\n" + 
			"	{\n" + 
			"		\"constant\": true,\n" + 
			"		\"inputs\": [],\n" + 
			"		\"name\": \"creator\",\n" + 
			"		\"outputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"view\",\n" + 
			"		\"type\": \"function\"\n" + 
			"	},\n" + 
			"	{\n" + 
			"		\"constant\": true,\n" + 
			"		\"inputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"name\",\n" + 
			"				\"type\": \"string\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"name\": \"get\",\n" + 
			"		\"outputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"view\",\n" + 
			"		\"type\": \"function\"\n" + 
			"	},\n" + 
			"	{\n" + 
			"		\"constant\": false,\n" + 
			"		\"inputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"name\",\n" + 
			"				\"type\": \"string\"\n" + 
			"			},\n" + 
			"			{\n" + 
			"				\"name\": \"addr\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"name\": \"set\",\n" + 
			"		\"outputs\": [],\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"nonpayable\",\n" + 
			"		\"type\": \"function\"\n" + 
			"	},\n" + 
			"	{\n" + 
			"		\"inputs\": [],\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"nonpayable\",\n" + 
			"		\"type\": \"constructor\"\n" + 
			"	},\n" + 
			"	{\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"nonpayable\",\n" + 
			"		\"type\": \"fallback\"\n" + 
			"	},\n" + 
			"	{\n" + 
			"		\"anonymous\": false,\n" + 
			"		\"inputs\": [\n" + 
			"			{\n" + 
			"				\"indexed\": false,\n" + 
			"				\"name\": \"name\",\n" + 
			"				\"type\": \"string\"\n" + 
			"			},\n" + 
			"			{\n" + 
			"				\"indexed\": false,\n" + 
			"				\"name\": \"addr\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"name\": \"onSet\",\n" + 
			"		\"type\": \"event\"\n" + 
			"	}\n" + 
			"]";
}
