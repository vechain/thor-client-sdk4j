package com.vechain.thorclient.utils;

import com.vechain.thorclient.utils.URLUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.HashMap;

@RunWith(JUnit4.class)
public class URLUtilsTest {

    @Test
    public void testUrlComposite(){
        String path = "http://thor-node.com/accounts/{address}";
        HashMap<String, String> paths = new HashMap<>( );
        paths.put("address", "0x3213132321421423143");
        String compositedURL = URLUtils.urlComposite( path, paths, null );
        Assert.assertEquals("Composite path url is failed.", "http://thor-node.com/accounts/0x3213132321421423143", compositedURL);
    }

    @Test
    public void testUrlCompositeQueryString(){
        String path = "http://thor-node.com/accounts/{address}";
        HashMap<String, String> paths = new HashMap<>( );
        paths.put("address", "0x3213132321421423143");

        HashMap<String, String> querys = new HashMap<>();
        querys.put("name", "foo");
        querys.put("age", "21");
        querys.put("description", "hello world");

        String compositedURL = URLUtils.urlComposite( path, paths, querys);
        Assert.assertEquals("Composite path url is failed.", "http://thor-node.com/accounts/0x3213132321421423143?name=foo&description=hello%20world&age=21", compositedURL);

    }

}
