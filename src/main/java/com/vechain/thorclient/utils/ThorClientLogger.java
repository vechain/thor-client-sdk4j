package com.vechain.thorclient.utils;

import org.apache.commons.logging.impl.SimpleLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThorClientLogger {

    private final static Logger logger = LoggerFactory.getLogger(SimpleLog.class);

    public static void info(String msg){
        logger.info(msg);
    }

    public static void debug(String msg){
        logger.debug(msg);
    }

    public static void error(String msg){
        logger.error(msg);
    }


}
