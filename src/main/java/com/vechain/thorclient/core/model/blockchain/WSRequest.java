package com.vechain.thorclient.core.model.blockchain;

import com.vechain.thorclient.utils.URLUtils;

import java.lang.reflect.Field;
import java.util.HashMap;


public class WSRequest {

    public HashMap<String, String> compositeRequestHashMap() throws IllegalAccessException {
        HashMap<String, String> queryMap = new HashMap<>(  );
        for(Field field:this.getClass().getFields()){
            String key = field.getName();
            String value  = (String)field.get("");
            if(value != null){
                queryMap.put( key, value );
            }

        }
        return queryMap;
    }


}
