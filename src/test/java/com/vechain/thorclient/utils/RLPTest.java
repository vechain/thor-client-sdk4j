package com.vechain.thorclient.utils;


import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.utils.rlp.RlpEncoder;
import com.vechain.thorclient.utils.rlp.RlpList;
import com.vechain.thorclient.utils.rlp.RlpString;
import com.vechain.thorclient.utils.rlp.RlpType;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.Prefix;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import java.math.BigInteger;
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
    public void testZero(){
        List<RlpType> result = new ArrayList<>();
        RlpList rlpList = new RlpList(result);
        result.add( RlpString.create( BigInteger.ZERO));
        byte[] empty =  RlpEncoder.encode(rlpList);

        logger.info("RLP zero array:" + BytesUtils.toHexString(empty, Prefix.ZeroLowerX));
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

    @Test
    public void testDecodeRlp(){
        String hexRaw = "0xf83d81c7860881eec535498202d0e1e094000000002beadb038203be21ed5ce7c9b1bff60289056bc75e2d63100000808082520880884773cc184328eb3ec0";
        RawTransaction rawTransaction =  RLPUtils.decode(hexRaw );
        byte[] encoded = RLPUtils.encodeRawTransaction( rawTransaction );
        String hexEncoded = BytesUtils.toHexString( encoded, Prefix.ZeroLowerX );
        Assert.assertEquals(hexRaw, hexEncoded);
    }

}
