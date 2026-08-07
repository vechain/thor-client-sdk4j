package com.vechain.thorclient.clients.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Minimal JSON-over-HTTP transport built on {@link HttpURLConnection}.
 *
 * <p>
 * Package-private on purpose: this is an implementation detail of
 * {@link AbstractClient} and deliberately not part of the SDK's public API, so
 * the underlying HTTP mechanism can be changed without a breaking release.
 * </p>
 */
final class HttpTransport {

    /**
     * Thor's REST API is JSON, and RFC 8259 fixes JSON exchanged between systems
     * to UTF-8. The node does not send a charset parameter, so decoding must not
     * fall back to the platform default.
     */
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final int DEFAULT_TIMEOUT_MILLIS = 5000;

    private static volatile int connectTimeoutMillis = DEFAULT_TIMEOUT_MILLIS;

    private static volatile int readTimeoutMillis = DEFAULT_TIMEOUT_MILLIS;

    private HttpTransport() {
    }

    /** Sets the connect and read timeouts to the same value. */
    static void setTimeout(int millis) {
        connectTimeoutMillis = millis;
        readTimeoutMillis = millis;
    }

    static void setConnectTimeout(int millis) {
        connectTimeoutMillis = millis;
    }

    static void setReadTimeout(int millis) {
        readTimeoutMillis = millis;
    }

    /**
     * A response that has been fully read. Thor reports application-level
     * failures (insufficient energy, expired transaction, bad clause) through the
     * status code with an explanatory plain-text body, so the body must be
     * captured for non-2xx responses too rather than discarded.
     */
    static final class Response {

        private final int status;
        private final String body;

        Response(int status, String body) {
            this.status = status;
            this.body = body;
        }

        int getStatus() {
            return status;
        }

        String getBody() {
            return body;
        }
    }

    static Response get(final String url) throws IOException {
        final HttpURLConnection connection = open(url, "GET");
        return execute(connection, null);
    }

    static Response post(final String url, final String jsonBody) throws IOException {
        final HttpURLConnection connection = open(url, "POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", CONTENT_TYPE_JSON);
        return execute(connection, jsonBody == null ? new byte[0] : jsonBody.getBytes(UTF_8));
    }

    private static HttpURLConnection open(final String url, final String method) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(connectTimeoutMillis);
        connection.setReadTimeout(readTimeoutMillis);
        connection.setRequestProperty("Accept", CONTENT_TYPE_JSON);
        return connection;
    }

    private static Response execute(final HttpURLConnection connection, final byte[] body) throws IOException {
        if (body != null) {
            try (OutputStream out = connection.getOutputStream()) {
                out.write(body);
            }
        }

        final int status = connection.getResponseCode();
        // getInputStream() throws on 4xx/5xx; the explanatory body is on the error
        // stream instead, and either stream may legitimately be null (e.g. 204).
        final InputStream stream = status >= HttpURLConnection.HTTP_BAD_REQUEST
                ? connection.getErrorStream()
                : connection.getInputStream();

        return new Response(status, readFully(stream));
    }

    private static String readFully(final InputStream stream) throws IOException {
        if (stream == null) {
            return "";
        }
        // Draining to EOF before closing lets the JVM return the socket to its
        // keep-alive pool. Calling HttpURLConnection#disconnect() here instead
        // would tear the socket down and force a new handshake per request.
        try (InputStream in = stream) {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            final byte[] chunk = new byte[8192];
            int read;
            while ((read = in.read(chunk)) != -1) {
                buffer.write(chunk, 0, read);
            }
            return new String(buffer.toByteArray(), UTF_8);
        }
    }
}
