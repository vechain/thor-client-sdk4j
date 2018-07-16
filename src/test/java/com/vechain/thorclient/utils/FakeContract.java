package com.vechain.thorclient.utils;

import com.vechain.thorclient.core.model.clients.base.AbstractContract;

public class FakeContract extends AbstractContract {

	public FakeContract() {
		super(abi);
	}

	public static String abi = "[\n" + 
			"	{\n" + 
			"		\"constant\": true,\n" + 
			"		\"inputs\": [],\n" + 
			"		\"name\": \"winningProposal\",\n" + 
			"		\"outputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"_winningProposal\",\n" + 
			"				\"type\": \"uint8\"\n" + 
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
			"				\"name\": \"bytearray\",\n" + 
			"				\"type\": \"bytes\"\n" + 
			"			},\n" + 
			"			{\n" + 
			"				\"name\": \"str\",\n" + 
			"				\"type\": \"string\"\n" + 
			"			},\n" + 
			"			{\n" + 
			"				\"name\": \"intarray\",\n" + 
			"				\"type\": \"uint256[]\"\n" + 
			"			},\n" + 
			"			{\n" + 
			"				\"name\": \"a\",\n" + 
			"				\"type\": \"bytes32\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"name\": \"testParams\",\n" + 
			"		\"outputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"\",\n" + 
			"				\"type\": \"bytes\"\n" + 
			"			},\n" + 
			"			{\n" + 
			"				\"name\": \"\",\n" + 
			"				\"type\": \"string\"\n" + 
			"			},\n" + 
			"			{\n" + 
			"				\"name\": \"\",\n" + 
			"				\"type\": \"uint256[]\"\n" + 
			"			},\n" + 
			"			{\n" + 
			"				\"name\": \"\",\n" + 
			"				\"type\": \"bytes32\"\n" + 
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
			"				\"name\": \"to\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"name\": \"delegate\",\n" + 
			"		\"outputs\": [],\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"nonpayable\",\n" + 
			"		\"type\": \"function\"\n" + 
			"	},\n" + 
			"	{\n" + 
			"		\"constant\": false,\n" + 
			"		\"inputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"toVoter\",\n" + 
			"				\"type\": \"address\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"name\": \"giveRightToVote\",\n" + 
			"		\"outputs\": [],\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"nonpayable\",\n" + 
			"		\"type\": \"function\"\n" + 
			"	},\n" + 
			"	{\n" + 
			"		\"constant\": false,\n" + 
			"		\"inputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"toProposal\",\n" + 
			"				\"type\": \"uint8\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"name\": \"vote\",\n" + 
			"		\"outputs\": [],\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"nonpayable\",\n" + 
			"		\"type\": \"function\"\n" + 
			"	},\n" + 
			"	{\n" + 
			"		\"inputs\": [\n" + 
			"			{\n" + 
			"				\"name\": \"_numProposals\",\n" + 
			"				\"type\": \"uint8\"\n" + 
			"			}\n" + 
			"		],\n" + 
			"		\"payable\": false,\n" + 
			"		\"stateMutability\": \"nonpayable\",\n" + 
			"		\"type\": \"constructor\"\n" + 
			"	}\n" + 
			"]";
	
	public static String bin = "0x608060405234801561001057600080fd5b50604051602080610ae183398101806040528101908080519060200190929190505050336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060018060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600001819055508060ff166002816100ec91906100f3565b5050610146565b81548183558181111561011a57818360005260206000209182019101610119919061011f565b5b505050565b61014391905b8082111561013f5760008082016000905550600101610125565b5090565b90565b61098c806101556000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680635c19a95c14610072578063609ff1bd146100b55780639e7b8d61146100e6578063ae025a0b14610129578063b3f98adc14610365575b600080fd5b34801561007e57600080fd5b506100b3600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610395565b005b3480156100c157600080fd5b506100ca6106e7565b604051808260ff1660ff16815260200191505060405180910390f35b3480156100f257600080fd5b50610127600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610763565b005b34801561013557600080fd5b50610227600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080602002602001604051908101604052809392919081815260200183836020028082843782019150505050505091929192908035600019169060200190929190505050610860565b604051808060200180602001806020018560001916600019168152602001848103845288818151815260200191508051906020019080838360005b8381101561027d578082015181840152602081019050610262565b50505050905090810190601f1680156102aa5780820380516001836020036101000a031916815260200191505b50848103835287818151815260200191508051906020019080838360005b838110156102e35780820151818401526020810190506102c8565b50505050905090810190601f1680156103105780820380516001836020036101000a031916815260200191505b50848103825286818151815260200191508051906020019060200280838360005b8381101561034c578082015181840152602081019050610331565b5050505090500197505050505050505060405180910390f35b34801561037157600080fd5b50610393600480360381019080803560ff16906020019092919050505061087e565b005b600080600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002091508160010160009054906101000a900460ff16156103f5576106e2565b5b600073ffffffffffffffffffffffffffffffffffffffff16600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415801561052357503373ffffffffffffffffffffffffffffffffffffffff16600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614155b1561059257600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1692506103f6565b3373ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1614156105cb576106e2565b60018260010160006101000a81548160ff021916908315150217905550828260010160026101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002090508060010160009054906101000a900460ff16156106ca57816000015460028260010160019054906101000a900460ff1660ff168154811015156106ab57fe5b90600052602060002001600001600082825401925050819055506106e1565b816000015481600001600082825401925050819055505b5b505050565b6000806000809150600090505b6002805490508160ff16101561075e578160028260ff1681548110151561071757fe5b906000526020600020016000015411156107515760028160ff1681548110151561073d57fe5b906000526020600020016000015491508092505b80806001019150506106f4565b505090565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614158061080b5750600160008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160009054906101000a900460ff165b156108155761085d565b60018060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600001819055505b50565b60608060606000878787879350935093509350945094509450949050565b6000600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002090508060010160009054906101000a900460ff16806108e657506002805490508260ff1610155b156108f05761095c565b60018160010160006101000a81548160ff021916908315150217905550818160010160016101000a81548160ff021916908360ff160217905550806000015460028360ff1681548110151561094157fe5b90600052602060002001600001600082825401925050819055505b50505600a165627a7a723058208c077c43912435351dd80191489e41f55f9f214ebafaae78392dd95016106ff40029";

}
