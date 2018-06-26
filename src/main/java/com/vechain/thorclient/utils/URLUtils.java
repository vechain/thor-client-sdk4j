package com.vechain.thorclient.utils;

import io.mikael.urlbuilder.UrlBuilder;

import java.util.HashMap;

public class URLUtils {

    public static String urlComposite(String urlString, HashMap<String, String> pathMap, HashMap<String, String> queryMap) {
        String compositeURL = String.valueOf(urlString);
        if (pathMap != null && pathMap.size() > 0) {
            for (String key : pathMap.keySet()) {
                String pathValue = pathMap.get(key);
                //String encodedPathValue = URLEncoder.encode( pathValue, "utf-8");
                compositeURL = compositeURL.replace("{" + key + "}", pathValue);
            }
        }
        String queryComposite = "";
        if (queryMap != null && queryMap.size() > 0) {
            final int querySize = queryMap.size();
            int       index     = 0;
            for (String key : queryMap.keySet()) {


                String queryValue  = queryMap.get(key);
                String queryString = key + "=" + queryValue;
                index++;
                if (index < querySize) {
                    queryString += "&";
                }
                queryComposite += queryString;
            }
        }
        if (!StringUtils.isBlank(queryComposite)) {
            compositeURL += "?" + queryComposite;
        }
        return UrlBuilder.fromString(compositeURL).toString();
    }
}
