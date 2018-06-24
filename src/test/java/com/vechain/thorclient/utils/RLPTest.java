package com.vechain.thorclient.utils;


import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.utils.rlp.RlpEncoder;
import com.vechain.thorclient.utils.rlp.RlpList;
import com.vechain.thorclient.utils.rlp.RlpString;
import com.vechain.thorclient.utils.rlp.RlpType;
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
    @Test
    public void testBytesZero(){
        List<RlpType> result = new ArrayList<>();
        byte zeros[] = new byte[32];
        result.add( RlpString.create(zeros));

        RlpList rlpList = new RlpList(result);
        byte[] empty =  RlpEncoder.encode(rlpList);
        logger.info("RLP zeros array:" + BytesUtils.toHexString(empty, Prefix.ZeroLowerX));
    }

}
