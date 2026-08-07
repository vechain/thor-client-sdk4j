package com.vechain.thorclient.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThorClientLogger {

    // Previously named after org.apache.commons.logging.impl.SimpleLog, which pulled in
    // jcl-over-slf4j purely for the class literal. Consumers filtering on that logger name
    // must now filter on this class instead.
    private final static Logger logger = LoggerFactory.getLogger(ThorClientLogger.class);

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
