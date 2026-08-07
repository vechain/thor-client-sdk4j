package com.vechain.thorclient.core.model.blockchain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class NodeProviderTest {

    private static final int DEFAULT_TIMEOUT = 5000;

    /** NodeProvider is a process-wide singleton, so restore it for later tests. */
    @After
    public void restoreDefaultTimeout() {
        NodeProvider.getNodeProvider().setTimeout(DEFAULT_TIMEOUT);
    }

    /**
     * Regression: setTimeout used to delegate to AbstractClient without updating
     * NodeProvider's own fields, so both getters always reported the constructor
     * default no matter what had been set.
     */
    @Test
    public void setTimeoutIsVisibleThroughBothGetters() {
        final NodeProvider provider = NodeProvider.getNodeProvider();
        provider.setTimeout(1234);
        Assert.assertEquals(1234, provider.getConnectTimeout());
        Assert.assertEquals(1234, provider.getSocketTimeout());
    }

    @Test
    public void connectAndSocketTimeoutsAreIndependentlySettable() {
        final NodeProvider provider = NodeProvider.getNodeProvider();
        provider.setConnectTimeout(1111);
        provider.setSocketTimeout(2222);
        Assert.assertEquals(1111, provider.getConnectTimeout());
        Assert.assertEquals(2222, provider.getSocketTimeout());
    }
}
