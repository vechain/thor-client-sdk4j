package com.vechain.thorclient.core.model.exception;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A exception wrapper for {@link IOException}. You can get http status from {@link #getHttpStatus} method.
 */
public class ClientIOException extends IOException{

    public static ClientIOException create(IOException ex){
        return new ClientIOException(ex);
    }

    public  ClientIOException(Exception ex){
        super(ex);
    }

    public ClientIOException(String message){
        super(message);
    }

    /**
     * Get http status from exception.
     * http status 400 means bad request. Likes out of gas, address can not be recovered, any request parameters parsing error and etc.
     *
     * http status 403 means request forbidden, like transaction pool is full, transaction is expired etc.
     *
     * http status 404 means api path is not existed.
     * @return response http code, while it -1, then it is not http error happening.
     */
    public int getHttpStatus(){
        // Method 3 without access to the URLConnection object
        int responseCode = - 1;
        // First we try parsing the exception message to see if it contains the response code
        Matcher exMsgStatusCodeMatcher = Pattern.compile("^Server returned HTTP response code: (\\d+)").matcher(this.getMessage());
        if(exMsgStatusCodeMatcher.find()) {
            responseCode = Integer.parseInt(exMsgStatusCodeMatcher.group(1));
        } else if(this.getClass().getSimpleName().equals("FileNotFoundException")) {
            // 404 is a special case because it will throw a FileNotFoundException instead of having "404" in the message
            responseCode = 404;
        }
        return  responseCode;
    }
}
