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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.net.URI;
import java.net.URL;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

/**
 * Unit tests for {@code BaseStatedHttpServiceClient}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see BaseStatedHttpServiceClient
 */
@Tag("ut")
class BaseStatedHttpServiceClientTest
extends BaseHttpServiceClientTest {

    /**
     * Test for {@link BaseStatedHttpServiceClient#BaseStatedHttpServiceClient(URL, StatusInitOperation)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructorNullNull()
    throws Throwable {
        assertThrows(NullPointerException.class, () -> {
            new BaseStatedHttpServiceClient<Object>(null, null);
        });
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#BaseStatedHttpServiceClient(URL, StatusInitOperation)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructorNullURL()
    throws Throwable {
        @SuppressWarnings("unchecked")
        final StatusInitOperation<Object> mockInitOp = mock(StatusInitOperation.class);
        assertThrows(NullPointerException.class, () -> {
            new BaseStatedHttpServiceClient<Object>(null, mockInitOp);
        });
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#BaseStatedHttpServiceClient(URL, StatusInitOperation)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructorNullInitOp()
    throws Throwable {
        final String schema = "https";
        final String host = "some.host.example.com";
        final int port = 3654;
        final String path = "some/path";
        final URL url = new URL(schema, host, port, path);
        assertThrows(NullPointerException.class, () -> {
            new BaseStatedHttpServiceClient<Object>(url, null);
        });
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#BaseStatedHttpServiceClient(URL, StatusInitOperation)}.
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
        @SuppressWarnings("unchecked")
        final StatusInitOperation<Object> mockInitOp = mock(StatusInitOperation.class);
        try (final BaseStatedHttpServiceClient<Object> client =
                new BaseStatedHttpServiceClient<Object>(url, mockInitOp)) {
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
            
            assertNotNull(client.getStatusInitOperation());
            assertSame(mockInitOp, client.getStatusInitOperation());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BaseStatedHttpServiceClient<? extends Object> createClientFromUrl(
            final @NotNull URL url) {
        @SuppressWarnings("unchecked")
        final StatusInitOperation<Object> mockInitOp = mock(StatusInitOperation.class);
        return createClientFromUrlAndInitOp(url, mockInitOp);
    }

    /**
     * {@inheritDoc}
     */
    protected <T extends Object> BaseStatedHttpServiceClient<T> createClientFromUrlAndInitOp(
            final @NotNull URL url,
            final @NotNull StatusInitOperation<T> mockInitOp) {
        return new BaseStatedHttpServiceClient<T>(url, mockInitOp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BaseHttpServiceClient createClientFromParts(
            final @NotNull HttpHost mockHost,
            final @NotNull URI mockBaseUri,
            final @NotNull CookieStore mockCookieStore,
            final @NotNull CloseableHttpClient mockClient) {
        @SuppressWarnings("unchecked")
        final StatusInitOperation<Object> mockInitOp = mock(StatusInitOperation.class);
        return new BaseStatedHttpServiceClient<Object>(
                mockHost,
                mockBaseUri,
                mockCookieStore,
                mockClient,
                mockInitOp);
    }

    /**
     * Creates a mock status for testing.
     * 
     * @return The created mock status
     */
    protected Object createMockStatus() {
        return mock(Object.class);
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#execute(StatusDependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteDependentFailedInit()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<Object> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final StatusDependentOperation<Object, Object, Object> operation =
                mock(StatusDependentOperation.class);
        final HttpClientException mockInitException = new HttpClientException();
        final Object mockParam = new Object();
        try (final BaseStatedHttpServiceClient<Object> client =
                createClientFromUrlAndInitOp(url, mockInitOp)) {
            final BaseStatedHttpServiceClient<Object> clientSpy = spy(client);
            willThrow(mockInitException).given(clientSpy).ensureInitialized();
            final HttpClientException result = assertThrows(HttpClientException.class, () -> {
                clientSpy.execute(operation, mockParam);
            });
            assertNotNull(result);
            assertSame(mockInitException, result);
            then(clientSpy).should(times(1)).ensureInitialized();
            then(operation).shouldHaveNoInteractions();
        }
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#execute(StatusDependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteDependentNullParam()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<Object> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final StatusDependentOperation<Object, Object, Object> operation =
                mock(StatusDependentOperation.class);
        final Object mockStatus = createMockStatus();
        final Object mockResult = new Object();
        try (final BaseStatedHttpServiceClient<Object> client =
                createClientFromUrlAndInitOp(url, mockInitOp)) {
            final BaseStatedHttpServiceClient<Object> clientSpy = spy(client);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            given(operation.execute(null, clientSpy)).willReturn(mockResult);
            final Object result = clientSpy.execute(operation, null);
            assertNotNull(result);
            assertSame(mockResult, result);
            final InOrder inOrder = inOrder(clientSpy, operation);
            inOrder.verify(clientSpy, times(1)).ensureInitialized();
            inOrder.verify(operation, times(1)).execute(null, clientSpy);
            inOrder.verifyNoMoreInteractions();
        }
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#execute(StatusDependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteDependent()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<Object> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final StatusDependentOperation<Object, Object, Object> operation =
                mock(StatusDependentOperation.class);
        final Object mockParam = new Object();
        final Object mockStatus = createMockStatus();
        final Object mockResult = new Object();
        try (final BaseStatedHttpServiceClient<Object> client =
                createClientFromUrlAndInitOp(url, mockInitOp)) {
            final BaseStatedHttpServiceClient<Object> clientSpy = spy(client);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            given(operation.execute(mockParam, clientSpy)).willReturn(mockResult);
            final Object result = clientSpy.execute(operation, mockParam);
            assertNotNull(result);
            assertSame(mockResult, result);
            final InOrder inOrder = inOrder(clientSpy, operation);
            inOrder.verify(clientSpy, times(1)).ensureInitialized();
            inOrder.verify(operation, times(1)).execute(mockParam, clientSpy);
            inOrder.verifyNoMoreInteractions();
        }
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#execute(StatusDependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteDependentFailed()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<Object> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final StatusDependentOperation<Object, Object, Object> operation =
                mock(StatusDependentOperation.class);
        final HttpClientException mockResult = new HttpClientException();
        final Object mockParam = new Object();
        final Object mockStatus = createMockStatus();
        try (final BaseStatedHttpServiceClient<Object> client =
                createClientFromUrlAndInitOp(url, mockInitOp)) {
            final BaseStatedHttpServiceClient<Object> clientSpy = spy(client);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            given(operation.execute(mockParam, clientSpy)).willThrow(mockResult);
            final HttpClientException thrown = assertThrows(HttpClientException.class, () -> {
                clientSpy.execute(operation, mockParam);
            });
            assertNotNull(thrown);
            assertSame(mockResult, thrown);
            final InOrder inOrder = inOrder(clientSpy, operation);
            inOrder.verify(clientSpy, times(1)).ensureInitialized();
            inOrder.verify(operation, times(1)).execute(mockParam, clientSpy);
            inOrder.verifyNoMoreInteractions();
        }
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#setStatus()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetStatus()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<Object> mockInitOp = mock(StatusInitOperation.class);
        final Object mockStatus = createMockStatus();
        try (final BaseStatedHttpServiceClient<Object> client =
                createClientFromUrlAndInitOp(url, mockInitOp)) {
            client.setStatus(mockStatus);
            assertSame(mockStatus, client.getStatus());
            then(mockInitOp).shouldHaveNoInteractions();
        }
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#resetStatus()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testResetStatus()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<Object> mockInitOp = mock(StatusInitOperation.class);
        final Object mockStatus = createMockStatus();
        try (final BaseStatedHttpServiceClient<Object> client =
                createClientFromUrlAndInitOp(url, mockInitOp)) {
            client.setStatus(mockStatus);
            client.resetStatus();
            assertNull(client.getStatus());
            then(mockInitOp).shouldHaveNoInteractions();
        }
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#initializeStatus()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testInitializeStatus()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<Object> mockInitOp = mock(StatusInitOperation.class);
        final Object mockStatus = createMockStatus();
        try (final BaseStatedHttpServiceClient<Object> client =
                createClientFromUrlAndInitOp(url, mockInitOp)) {
            given(mockInitOp.execute(any(), same(client))).willReturn(mockStatus);
            final Object result = client.initializeStatus();
            assertNotNull(result);
            assertSame(mockStatus, result);
            assertSame(mockStatus, client.getStatus());
            then(mockInitOp).should(times(1)).execute(any(), same(client));
        }
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#ensureInitialized()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testEnsureInitialized()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<Object> mockInitOp = mock(StatusInitOperation.class);
        final Object mockStatus = createMockStatus();
        try (final BaseStatedHttpServiceClient<Object> client =
                createClientFromUrlAndInitOp(url, mockInitOp)) {
            final BaseStatedHttpServiceClient<Object> clientSpy = spy(client);
            willReturn(null).given(clientSpy).getStatus();
            willReturn(mockStatus).given(clientSpy).initializeStatus();
            final Object result = clientSpy.ensureInitialized();
            assertNotNull(result);
            assertSame(mockStatus, result);
            final InOrder callOrder = inOrder(clientSpy);
            callOrder.verify(clientSpy, times(1)).getStatus();
            callOrder.verify(clientSpy, times(1)).initializeStatus();
        }
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#ensureInitialized()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testEnsureInitializedAlreadyInitialized()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<Object> mockInitOp = mock(StatusInitOperation.class);
        final Object mockStatus = createMockStatus();
        try (final BaseStatedHttpServiceClient<Object> client =
                createClientFromUrlAndInitOp(url, mockInitOp)) {
            final BaseStatedHttpServiceClient<Object> clientSpy = spy(client);
            willReturn(mockStatus).given(clientSpy).getStatus();
            final Object result = clientSpy.ensureInitialized();
            assertNotNull(result);
            assertSame(mockStatus, result);
            then(clientSpy).should(times(1)).getStatus();
            then(clientSpy).should(never()).initializeStatus();
        }
    }
}
