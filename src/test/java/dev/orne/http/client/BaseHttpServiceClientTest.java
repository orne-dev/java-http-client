package dev.orne.http.client;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2020 - 2021 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.net.URI;
import java.net.URL;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for {@code BaseHttpServiceClient}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see BaseHttpServiceClient
 */
@Tag("ut")
class BaseHttpServiceClientTest {

    /**
     * Test for {@link BaseHttpServiceClient#BaseHttpServiceClient(URL)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructorNullURL()
    throws Throwable {
        assertThrows(NullPointerException.class, () -> {
            new BaseHttpServiceClient(null);
        });
    }

    /**
     * Test for {@link BaseHttpServiceClient#BaseHttpServiceClient(URL)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructor()
    throws Throwable {
        final String schema = "https";
        final String host = "some.host.example.com";
        final int port = 3654;
        final String path = "some/path";
        final URL url = new URL(schema, host, port, path);
        try (final BaseHttpServiceClient client = new BaseHttpServiceClient(url)) {
            final HttpHost clientHost = client.getHost();
            assertNotNull(clientHost);
            assertNotNull(clientHost.getSchemeName());
            assertEquals(schema, clientHost.getSchemeName());
            assertNotNull(clientHost.getHostName());
            assertEquals(host, clientHost.getHostName());
            assertEquals(port, clientHost.getPort());
            assertNull(clientHost.getAddress());
            
            assertNotNull(client.getBaseURI());
            assertFalse(client.getBaseURI().isAbsolute());
            assertEquals(path, client.getBaseURI().getPath());
            
            assertNotNull(client.getCookieStore());
            assertTrue(client.getCookieStore().getCookies().isEmpty());
            
            assertNotNull(client.getClient());
        }
    }

    /**
     * Creates an instance from specified URL.
     * 
     * @param baseURL The HTTP service's base URL
     * @return The created instance
     */
    protected BaseHttpServiceClient createClientFromUrl(
            final @NotNull URL url) {
        return new BaseHttpServiceClient(url);
    }

    /**
     * Creates an instance from specified parts.
     * 
     * @param host The HTTP service's host
     * @param baseURI The HTTP service's base URI
     * @param cookieStore The HTTP client's cookie store
     * @param client The HTTP client
     * @return The created instance
     */
    protected BaseHttpServiceClient createClientFromParts(
            final @NotNull HttpHost mockHost,
            final @NotNull URI mockBaseUri,
            final @NotNull CookieStore mockCookieStore,
            final @NotNull CloseableHttpClient mockClient) {
        return new BaseHttpServiceClient(
                mockHost,
                mockBaseUri,
                mockCookieStore,
                mockClient);
    }

    /**
     * Test for {@link BaseHttpServiceClient#close()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testClose()
    throws Throwable {
        final HttpHost mockHost = new HttpHost("example.org");
        final URI mockBaseUri = URI.create("some/path");
        final CookieStore mockCookieStore = mock(CookieStore.class);
        final CloseableHttpClient mockClient = mock(CloseableHttpClient.class);
        try (final BaseHttpServiceClient client = createClientFromParts(
                mockHost,
                mockBaseUri,
                mockCookieStore,
                mockClient)) {
            then(mockCookieStore).should(times(0)).clear();
            then(mockClient).should(times(0)).close();
        }
        then(mockCookieStore).should(times(1)).clear();
        then(mockClient).should(times(1)).close();
    }

    /**
     * Test for {@link BaseHttpServiceClient#execute(StatusIndependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteNullParam()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusIndependentOperation<Object, Object> operation =
                mock(StatusIndependentOperation.class);
        final Object mockResult = new Object();
        try (final BaseHttpServiceClient client = createClientFromUrl(url)) {
            given(operation.execute(null, client)).willReturn(mockResult);
            final Object result = client.execute(operation, null);
            assertNotNull(result);
            assertSame(mockResult, result);
            then(operation).should(times(1)).execute(null, client);
        }
    }

    /**
     * Test for {@link BaseHttpServiceClient#execute(StatusIndependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecute()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusIndependentOperation<Object, Object> operation =
                mock(StatusIndependentOperation.class);
        final Object mockParam = new Object();
        final Object mockResult = new Object();
        try (final BaseHttpServiceClient client = createClientFromUrl(url)) {
            given(operation.execute(mockParam, client)).willReturn(mockResult);
            final Object result = client.execute(operation, mockParam);
            assertNotNull(result);
            assertSame(mockResult, result);
            then(operation).should(times(1)).execute(mockParam, client);
        }
    }

    /**
     * Test for {@link BaseHttpServiceClient#execute(StatusIndependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteFailed()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusIndependentOperation<Object, Object> operation =
                mock(StatusIndependentOperation.class);
        final HttpClientException mockResult = new HttpClientException();
        final Object mockParam = new Object();
        try (final BaseHttpServiceClient client = createClientFromUrl(url)) {
            given(operation.execute(mockParam, client)).willThrow(mockResult);
            final HttpClientException thrown = assertThrows(HttpClientException.class, () -> {
                client.execute(operation, mockParam);
            });
            assertNotNull(thrown);
            assertSame(mockResult, thrown);
            then(operation).should(times(1)).execute(mockParam, client);
        }
    }

    /**
     * Test for {@link BaseHttpServiceClient#getLogger()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testLogger()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        try (final BaseHttpServiceClient client = createClientFromUrl(url)) {
            final Logger logger = client.getLogger();
            assertNotNull(logger);
            assertSame(LoggerFactory.getLogger(client.getClass()), logger);
        }
    }
}
