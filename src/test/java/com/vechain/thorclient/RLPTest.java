package com.vechain.thorclient;


import com.vechain.thorclient.core.rlp.RlpEncoder;
import com.vechain.thorclient.core.rlp.RlpList;
import com.vechain.thorclient.core.rlp.RlpString;
import com.vechain.thorclient.core.rlp.RlpType;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.Prefix;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import java.util.ArrayList;
import java.util.List;



@RunWith(JUnit4.class)
public class RLPTest extends BaseTest {


    @Test
    public void testByteArray(){
        List<RlpType> result = new ArrayList<>();
        result.add( RlpString.create(new byte[]{}));

        RlpList rlpList = new RlpList(result);
        byte[] empty =  RlpEncoder.encode(rlpList);

        logger.info("RLP empty array:" + BytesUtils.toHexString(empty, Prefix.ZeroLowerX));

    }
}
