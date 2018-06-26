package com.vechain.thorclient.utils;

import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.clients.BlockClient;
import com.vechain.thorclient.clients.BlockchainClient;
import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.CryptoUtils;
import com.vechain.thorclient.utils.RawTransactionBuilder;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * {"version":3,"id":"FEE947A3-785B-4839-A782-998E8F471DCC","crypto":{"ciphertext":"3b331bd66571d9e1ff4b7bbd10f68b0089301e0aae3c3507e44ce73588f049b4","cipherparams":{"iv":"d3f26875c39594d3c5fcb8c689e4a9a7"},"kdf":"scrypt","kdfparams":{"r":8,"p":1,"n":262144,"dklen":32,"salt":"ac901f3f5160c786ea0f5e760791e80f9fb4ddb7af2af69a697bcb61bc4dda13"},"mac":"c1444e90194d1d1357aa6d8d4f5754339c803c65867ef058e542a7118f219ca8","cipher":"aes-128-ctr"},"address":"c71adc46c5891a8963ea5a5eeaf578e0a2959779"}
 */

@RunWith(JUnit4.class)
public class RawTransactionBuilderTest extends BaseTest {

    @Test
    public void testUpdateBuild() throws IOException {
        RawTransactionBuilder builder = new RawTransactionBuilder();

        byte chainTag = BlockchainClient.getChainTag();
        int  n        = chainTag & 0xFF;
        logger.info("Current chainTag:" + n);
        builder.update(Byte.valueOf(chainTag), "chainTag");

        byte[] expirationBytes = BytesUtils.longToBytes(720);
        builder.update(expirationBytes, "expiration");

        byte[] blockRef = BlockClient.getBlock(null).blockRef().toByteArray();
        builder.update(blockRef, "blockRef");

        byte[] nonce = CryptoUtils.generateTxNonce();
        builder.update(nonce, "nonce");

        byte[] gas = BytesUtils.longToBytes(21000);
        builder.update(gas, "gas");


        RawClause clauses[] = new RawClause[1];
        clauses[0] = new RawClause();
        clauses[0].setTo(BytesUtils.toByteArray("0x42191bd624aBffFb1b65e92F1E51EB16f4d2A3Ce"));
        clauses[0].setValue(BytesUtils.defaultDecimalStringToByteArray("42.42"));
        builder.update(clauses);
        RawTransaction rawTxn = builder.build();
        byte           tag    = rawTxn.getChainTag();
        Assert.assertEquals("ChainTag is not equal.", chainTag, tag);

    }

    @Test
    public void test() {


//        HttpResponse<JsonNode> response = null;
//        try {
//            response = Unirest.get("https://vethor-node-test.vechaindev.com/accounts/0xD3EF28DF6b553eD2fc47259E8134319cB1121A2A").header("content-type", "application/json; charset=utf-8").asJson();
//        } catch (UnirestException e) {
//            e.printStackTrace();
//        }
//        JSONObject res     = response.getBody().getObject();
//        Object     balance = res.get("balance");
//        System.out.print(balance);

//        Map<String, String> map = new HashMap<String, String>();
//        map.put("content-type", "application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("raw", "0xf90137819a870246ed960606d88202d0f8d8e394d418898c20f76aa1790082539bd7dac341e078db8c06765c7931c049c6289c000080e394929c34f1e5eff3f21a106362bc7ba12d0e6b07838c06765c7931c049c6289c000080e39477cbc87e41995931f64b89bab757d5aac7cae4288c06765c7931c049c6289c000080e394cf3166f0003838516aff6c4165d1e55aefbc1afd8c06765c7931c049c6289c000080e394b7c37d077690059dbbf74433069d4397de4568218c04d8c55ae1d809a7b49c000080e394bddc442a4bd68b4054782a1710943d358f4eda688c019d971e4207896acc9c000080808301b9688085b2f3ff256ec0b8418ccaf764c91f0afde16c70c7a1162480fa7a1ad7fb75a9ad5e2447f2e50667586ecf3f51a45a07477e9177e2d8fd169ba94f1fe22081c2c1055c8a79d929430a01");
//        HttpUtils.post("https://vethor-node-test.vechaindev.com/transactions", map, jsonObject);


        RequestBodyEntity RequestBodyEntity = Unirest.post("https://vethor-node-test.vechaindev.com/transactions")
                .header("accept", "application/json")
                .body(jsonObject);
        try {
            RequestBodyEntity.asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }


}
