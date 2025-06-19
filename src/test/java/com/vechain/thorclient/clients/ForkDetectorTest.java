package com.vechain.thorclient.clients;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.utils.ForkDetector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;

@RunWith(JUnit4.class)
public class ForkDetectorTest extends BaseTest {

    /**
     * Tests that galactica is forked
     * This should be true for solo as it forks at block 1
     * The docker compose healthcheck waits for block 1
     */
    @Test
    public void testGalacticaFork() {
        boolean isForked = ForkDetector.isGalacticaForked();
        Assert.assertTrue(isForked);
    }

}
