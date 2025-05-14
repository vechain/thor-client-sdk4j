package com.vechain.thorclient.utils;

import com.vechain.thorclient.core.model.exception.HttpException;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONObject;

import java.util.Map;

public class HttpUtils {

    public static JSONObject get(final String url, final Map<String, String> headers) {
        try {
            HttpResponse<JsonNode> response = Unirest.get(url).headers(headers).asJson();
            return response.getBody().getObject();
        } catch (UnirestException e) {
            throw new HttpException();
        }
    }

    public static JSONObject post(final String url, final Map<String, String> headers, JSONObject json) {
        try {
            HttpResponse<JsonNode> postResponse = Unirest.post(url)
            		.headers(headers)
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(json)
                    .asJson();
            return postResponse.getBody().getObject();
        } catch (UnirestException e) {
            throw new HttpException();
        }
    }
}
