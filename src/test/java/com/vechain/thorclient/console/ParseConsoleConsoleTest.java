package com.vechain.thorclient.console;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ParseConsoleConsoleTest {
    @Test
    public void testParseVET(){
        ParserConsole.parseVET( "0xf83d81c7860881eec535498202d0e1e094000000002beadb038203be21ed5ce7c9b1bff60289056bc75e2d63100000808082520880884773cc184328eb3ec0" );
    }

    @Test
    public void testParseVTHO() throws Exception {
        ParserConsole.parseERC20( "0xf8bf81c786275af3aa5d6b8202d0f85ef85c940000000000000000000000000000456e6572677980b844a9059cbb000000000000000000000000000000002beadb038203be21ed5ce7c9b1bff60200000000000000000000000000000000000000000000021e19e0c9bab24000008083013880808871eb6b977e653515c0b8413705eec7219e6df602dc7bd1a7f450111f1f3342e7ca9975640bcfa3f020dce544edfb9b3e971722d9164bec8c67399c452827b1b7a0c8c26b5d339b6a479ccc01" );
    }
}
