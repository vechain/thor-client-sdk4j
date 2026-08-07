package com.vechain.thorclient.core.model.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ClientIOExceptionTest {

    /**
     * Regression: the (message, status) constructor used to hard-code httpStatus to
     * -1, silently discarding the status it was handed.
     */
    @Test
    public void statusConstructorRetainsTheGivenStatus() {
        Assert.assertEquals(403, new ClientIOException("request forbidden", 403).getHttpStatus());
        Assert.assertEquals(400, new ClientIOException("bad request", 400).getHttpStatus());
    }

    /** -1 is the documented "no HTTP status available" marker, not a default status. */
    @Test
    public void nonHttpConstructorsReportNoStatus() {
        Assert.assertEquals(-1, new ClientIOException("boom").getHttpStatus());
        Assert.assertEquals(-1, new ClientIOException(new RuntimeException("boom")).getHttpStatus());
    }

    @Test
    public void statusRemainsSettableAfterConstruction() {
        final ClientIOException exception = new ClientIOException("conflict", 409);
        exception.setHttpStatus(503);
        Assert.assertEquals(503, exception.getHttpStatus());
    }
}
