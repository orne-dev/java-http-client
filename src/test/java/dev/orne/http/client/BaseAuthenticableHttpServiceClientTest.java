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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Unit tests for {@code BaseAuthenticableHttpServiceClient}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see BaseAuthenticableHttpServiceClient
 */
@Tag("ut")
class BaseAuthenticableHttpServiceClientTest
extends BaseStatedHttpServiceClientTest {

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#BaseAuthenticableHttpServiceClient(URL, StatusInitOperation, AuthenticationOperation)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructorNullNull()
    throws Throwable {
        assertThrows(NullPointerException.class, () -> {
            new BaseAuthenticableHttpServiceClient<TestState, Object>(null, null, null);
        });
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#BaseAuthenticableHttpServiceClient(URL, StatusInitOperation, AuthenticationOperation)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructorNullURL()
    throws Throwable {
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        assertThrows(NullPointerException.class, () -> {
            new BaseAuthenticableHttpServiceClient<TestState, Object>(
                    null, mockInitOp, mockAuthOp);
        });
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#BaseAuthenticableHttpServiceClient(URL, StatusInitOperation, AuthenticationOperation)}.
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
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        assertThrows(NullPointerException.class, () -> {
            new BaseAuthenticableHttpServiceClient<TestState, Object>(
                    url, null, mockAuthOp);
        });
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#BaseAuthenticableHttpServiceClient(URL, StatusInitOperation, AuthenticationOperation)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructorNullAuthOp()
    throws Throwable {
        final String schema = "https";
        final String host = "some.host.example.com";
        final int port = 3654;
        final String path = "some/path";
        final URL url = new URL(schema, host, port, path);
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        assertThrows(NullPointerException.class, () -> {
            new BaseAuthenticableHttpServiceClient<TestState, Object>(
                    url, mockInitOp, null);
        });
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#BaseAuthenticableHttpServiceClient(URL, StatusInitOperation, AuthenticationOperation)}.
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
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                new BaseAuthenticableHttpServiceClient<TestState, Object>(url, mockInitOp, mockAuthOp)) {
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
            
            assertNotNull(client.getAuthenticationOperation());
            assertSame(mockAuthOp, client.getAuthenticationOperation());
            
            assertFalse(client.isCredentialsStoringEnabled());
            assertFalse(client.hasStoredCredentials());
            assertFalse(client.isAuthenticationAutoRenewalEnabled());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BaseStatedHttpServiceClient<? extends Object> createClientFromUrl(
            final @NotNull URL url) {
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp =
                mock(AuthenticationOperation.class);
        return createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    protected <T extends Object> BaseStatedHttpServiceClient<T> createClientFromUrlAndInitOp(
            final @NotNull URL url,
            final @NotNull StatusInitOperation<T> mockInitOp) {
        final StatusInitOperation<TestState> castedMockInitOp =
                (StatusInitOperation<TestState>) mockInitOp;
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp =
                mock(AuthenticationOperation.class);
        return (BaseStatedHttpServiceClient<T>) createClientFromUrlAndInitOpAndAuthOp(url, castedMockInitOp, mockAuthOp);
    }

    /**
     * {@inheritDoc}
     */
    protected <T extends TestState> BaseAuthenticableHttpServiceClient<T, Object> createClientFromUrlAndInitOpAndAuthOp(
            final @NotNull URL url,
            final @NotNull StatusInitOperation<T> mockInitOp,
            final @NotNull AuthenticationOperation<Object, ?, T> mockAuthOp) {
        return new BaseAuthenticableHttpServiceClient<T, Object>(
                url, mockInitOp, mockAuthOp);
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
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp =
                mock(AuthenticationOperation.class);
        return new BaseAuthenticableHttpServiceClient<TestState, Object>(
                mockHost,
                mockBaseUri,
                mockCookieStore,
                mockClient,
                mockInitOp,
                mockAuthOp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TestState createMockStatus() {
        return mock(TestState.class);
    }

    /**
     * Creates a mock credentials for testing.
     * 
     * @return The created mock credentials
     */
    protected Object createMockCredentials() {
        return mock(Object.class);
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#setCredentialsStoringEnabled(boolean)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetCredentialsStoringEnabled()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            assertFalse(client.isCredentialsStoringEnabled());
            client.setCredentialsStoringEnabled(true);
            assertTrue(client.isCredentialsStoringEnabled());
            client.setCredentialsStoringEnabled(false);
            assertFalse(client.isCredentialsStoringEnabled());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#setAuthenticationAutoRenewalEnabled(boolean)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetAuthenticationAutoRenewalEnabled()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            assertFalse(client.isAuthenticationAutoRenewalEnabled());
            client.setAuthenticationAutoRenewalEnabled(true);
            assertTrue(client.isAuthenticationAutoRenewalEnabled());
            client.setAuthenticationAutoRenewalEnabled(false);
            assertFalse(client.isAuthenticationAutoRenewalEnabled());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#setStoredCredentials(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetStoredCredentials()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            assertFalse(client.hasStoredCredentials());
            client.setStoredCredentials(new Object());
            assertTrue(client.hasStoredCredentials());
            client.setStoredCredentials(null);
            assertFalse(client.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testAuthenticateCredentials()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        final TestState mockStatus = createMockStatus();
        final Object mockCredentials = createMockCredentials();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            clientSpy.authenticate(mockCredentials);
            final InOrder callOrder = inOrder(clientSpy, mockAuthOp);
            callOrder.verify(clientSpy, times(1)).ensureInitialized();
            callOrder.verify(mockAuthOp, times(1)).execute(mockCredentials, clientSpy);
            then(mockInitOp).shouldHaveNoInteractions();
            assertFalse(clientSpy.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testAuthenticateCredentialsFail()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        final TestState mockStatus = createMockStatus();
        final Object mockCredentials = createMockCredentials();
        final HttpClientException mockResult = new HttpClientException();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            willThrow(mockResult).given(mockAuthOp).execute(mockCredentials, clientSpy);
            final HttpClientException thrown = assertThrows(HttpClientException.class, () -> {
                clientSpy.authenticate(mockCredentials);
            });
            assertNotNull(thrown);
            assertSame(mockResult, thrown);
            final InOrder callOrder = inOrder(clientSpy, mockAuthOp);
            callOrder.verify(clientSpy, times(1)).ensureInitialized();
            callOrder.verify(mockAuthOp, times(1)).execute(mockCredentials, clientSpy);
            then(mockInitOp).shouldHaveNoInteractions();
            assertFalse(clientSpy.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testAuthenticateCredentialsStorageEnabled()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        final TestState mockStatus = createMockStatus();
        final Object mockCredentials = createMockCredentials();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            clientSpy.setCredentialsStoringEnabled(true);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            clientSpy.authenticate(mockCredentials);
            final InOrder callOrder = inOrder(clientSpy, mockAuthOp);
            callOrder.verify(clientSpy, times(1)).ensureInitialized();
            callOrder.verify(mockAuthOp, times(1)).execute(mockCredentials, clientSpy);
            then(mockInitOp).shouldHaveNoInteractions();
            assertTrue(clientSpy.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testAuthenticateCredentialsStorageEnabledFail()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        final TestState mockStatus = createMockStatus();
        final Object mockCredentials = createMockCredentials();
        final HttpClientException mockResult = new HttpClientException();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            clientSpy.setCredentialsStoringEnabled(true);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            willThrow(mockResult).given(mockAuthOp).execute(mockCredentials, clientSpy);
            final HttpClientException thrown = assertThrows(HttpClientException.class, () -> {
                clientSpy.authenticate(mockCredentials);
            });
            assertNotNull(thrown);
            assertSame(mockResult, thrown);
            final InOrder callOrder = inOrder(clientSpy, mockAuthOp);
            callOrder.verify(clientSpy, times(1)).ensureInitialized();
            callOrder.verify(mockAuthOp, times(1)).execute(mockCredentials, clientSpy);
            then(mockInitOp).shouldHaveNoInteractions();
            assertFalse(clientSpy.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testAuthenticate()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        final TestState mockStatus = createMockStatus();
        final Object mockCredentials = createMockCredentials();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            clientSpy.setStoredCredentials(mockCredentials);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            clientSpy.authenticate();
            final InOrder callOrder = inOrder(clientSpy, mockAuthOp);
            callOrder.verify(clientSpy, times(1)).ensureInitialized();
            callOrder.verify(mockAuthOp, times(1)).execute(mockCredentials, clientSpy);
            then(mockInitOp).shouldHaveNoInteractions();
            assertTrue(clientSpy.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testAuthenticateNoStoredCredentials()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        final TestState mockStatus = createMockStatus();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            assertThrows(CredentialsNotStoredException.class, () -> {
                clientSpy.authenticate();
            });
            then(mockInitOp).shouldHaveNoInteractions();
            assertFalse(clientSpy.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testAuthenticateFail()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        final TestState mockStatus = createMockStatus();
        final Object mockCredentials = createMockCredentials();
        final HttpClientException mockResult = new HttpClientException();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            clientSpy.setStoredCredentials(mockCredentials);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            willThrow(mockResult).given(mockAuthOp).execute(mockCredentials, clientSpy);
            final HttpClientException thrown = assertThrows(HttpClientException.class, () -> {
                clientSpy.authenticate();
            });
            assertNotNull(thrown);
            assertSame(mockResult, thrown);
            final InOrder callOrder = inOrder(clientSpy, mockAuthOp);
            callOrder.verify(clientSpy, times(1)).ensureInitialized();
            callOrder.verify(mockAuthOp, times(1)).execute(mockCredentials, clientSpy);
            then(mockInitOp).shouldHaveNoInteractions();
            assertTrue(clientSpy.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testAuthenticateInvalidCrendentialsFail()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        final TestState mockStatus = createMockStatus();
        final Object mockCredentials = createMockCredentials();
        final CredentialsInvalidException mockResult = new CredentialsInvalidException();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            clientSpy.setStoredCredentials(mockCredentials);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            willThrow(mockResult).given(mockAuthOp).execute(mockCredentials, clientSpy);
            final HttpClientException thrown = assertThrows(HttpClientException.class, () -> {
                clientSpy.authenticate();
            });
            assertNotNull(thrown);
            assertSame(mockResult, thrown);
            final InOrder callOrder = inOrder(clientSpy, mockAuthOp);
            callOrder.verify(clientSpy, times(1)).ensureInitialized();
            callOrder.verify(mockAuthOp, times(1)).execute(mockCredentials, clientSpy);
            then(mockInitOp).shouldHaveNoInteractions();
            assertFalse(clientSpy.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#ensureAuthenticated()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testEnsureAuthenticated()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        final TestState mockStatus = createMockStatus();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            willDoNothing().given(clientSpy).authenticate();
            willReturn(false).given(mockStatus).isAuthenticated();
            clientSpy.ensureAuthenticated();
            final InOrder callOrder = inOrder(clientSpy, mockStatus);
            callOrder.verify(clientSpy, times(1)).ensureInitialized();
            callOrder.verify(mockStatus, times(1)).isAuthenticated();
            callOrder.verify(clientSpy, times(1)).authenticate();
            then(mockInitOp).shouldHaveNoInteractions();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#ensureAuthenticated()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testEnsureAuthenticatedFail()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        final TestState mockStatus = createMockStatus();
        final HttpClientException mockResult = new HttpClientException();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            willThrow(mockResult).given(clientSpy).authenticate();
            willReturn(false).given(mockStatus).isAuthenticated();
            final HttpClientException thrown = assertThrows(HttpClientException.class, () -> {
                clientSpy.ensureAuthenticated();
            });
            assertNotNull(thrown);
            assertSame(mockResult, thrown);
            final InOrder callOrder = inOrder(clientSpy, mockStatus);
            callOrder.verify(clientSpy, times(1)).ensureInitialized();
            callOrder.verify(mockStatus, times(1)).isAuthenticated();
            callOrder.verify(clientSpy, times(1)).authenticate();
            then(mockInitOp).shouldHaveNoInteractions();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#ensureAuthenticated()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testEnsureAuthenticatedAlreadyAuthenticated()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        final TestState mockStatus = createMockStatus();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            willDoNothing().given(clientSpy).authenticate();
            willReturn(true).given(mockStatus).isAuthenticated();
            clientSpy.ensureAuthenticated();
            final InOrder callOrder = inOrder(clientSpy, mockStatus);
            callOrder.verify(clientSpy, times(1)).ensureInitialized();
            callOrder.verify(mockStatus, times(1)).isAuthenticated();
            callOrder.verify(clientSpy, never()).authenticate();
            then(mockAuthOp).shouldHaveNoInteractions();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#execute(StatusDependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteAuthenticated()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticatedOperation<Object, Object, TestState> operation =
                mock(AuthenticatedOperation.class);
        final Object mockParam = new Object();
        final TestState mockStatus = createMockStatus();
        final Object mockResult = new Object();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            willDoNothing().given(clientSpy).ensureAuthenticated();
            willReturn(mockResult).given(operation).execute(mockParam, clientSpy);
            final Object result = clientSpy.execute(operation, mockParam);
            assertNotNull(result);
            assertSame(mockResult, result);
            final InOrder inOrder = inOrder(clientSpy, operation);
            inOrder.verify(clientSpy, times(1)).ensureAuthenticated();
            inOrder.verify(operation, times(1)).execute(mockParam, clientSpy);
            inOrder.verifyNoMoreInteractions();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#execute(StatusDependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteAuthenticatedEnsureAuthenticationFail()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticatedOperation<Object, Object, TestState> operation =
                mock(AuthenticatedOperation.class);
        final Object mockParam = new Object();
        final TestState mockStatus = createMockStatus();
        final HttpClientException mockResult = new HttpClientException();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            willThrow(mockResult).given(clientSpy).ensureAuthenticated();
            final HttpClientException thrown = assertThrows(HttpClientException.class, () -> {
                clientSpy.execute(operation, mockParam);
            });
            assertNotNull(thrown);
            assertSame(mockResult, thrown);
            then(clientSpy).should(times(1)).ensureAuthenticated();
            then(operation).should(never()).execute(mockParam, clientSpy);
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#execute(StatusDependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteAuthenticatedFail()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticatedOperation<Object, Object, TestState> operation =
                mock(AuthenticatedOperation.class);
        final Object mockParam = new Object();
        final TestState mockStatus = createMockStatus();
        final HttpClientException mockResult = new HttpClientException();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            willDoNothing().given(clientSpy).ensureAuthenticated();
            willThrow(mockResult).given(operation).execute(mockParam, clientSpy);
            final HttpClientException thrown = assertThrows(HttpClientException.class, () -> {
                clientSpy.execute(operation, mockParam);
            });
            assertNotNull(thrown);
            assertSame(mockResult, thrown);
            final InOrder inOrder = inOrder(clientSpy, operation);
            inOrder.verify(clientSpy, times(1)).ensureAuthenticated();
            inOrder.verify(operation, times(1)).execute(mockParam, clientSpy);
            inOrder.verifyNoMoreInteractions();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#execute(StatusDependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteAuthenticatedExpiredNoAutorenewal()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticatedOperation<Object, Object, TestState> operation =
                mock(AuthenticatedOperation.class);
        final Object mockParam = new Object();
        final TestState mockStatus = createMockStatus();
        final AuthenticationExpiredException mockResult = new AuthenticationExpiredException();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            clientSpy.setAuthenticationAutoRenewalEnabled(false);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            willDoNothing().given(clientSpy).ensureAuthenticated();
            willThrow(mockResult).given(operation).execute(mockParam, clientSpy);
            final HttpClientException thrown = assertThrows(HttpClientException.class, () -> {
                clientSpy.execute(operation, mockParam);
            });
            assertNotNull(thrown);
            assertSame(mockResult, thrown);
            final InOrder inOrder = inOrder(clientSpy, operation, mockStatus);
            inOrder.verify(clientSpy, times(1)).ensureAuthenticated();
            inOrder.verify(operation, times(1)).execute(mockParam, clientSpy);
            inOrder.verify(mockStatus, times(1)).resetAuthentication();
            inOrder.verifyNoMoreInteractions();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#execute(StatusDependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteAuthenticatedExpiredNoStoredCredentials()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticatedOperation<Object, Object, TestState> operation =
                mock(AuthenticatedOperation.class);
        final Object mockParam = new Object();
        final TestState mockStatus = createMockStatus();
        final AuthenticationExpiredException mockResult = new AuthenticationExpiredException();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            clientSpy.setAuthenticationAutoRenewalEnabled(true);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            willDoNothing().given(clientSpy).ensureAuthenticated();
            willThrow(mockResult).given(operation).execute(mockParam, clientSpy);
            final HttpClientException thrown = assertThrows(HttpClientException.class, () -> {
                clientSpy.execute(operation, mockParam);
            });
            assertNotNull(thrown);
            assertSame(mockResult, thrown);
            final InOrder inOrder = inOrder(clientSpy, operation, mockStatus);
            inOrder.verify(clientSpy, times(1)).ensureAuthenticated();
            inOrder.verify(operation, times(1)).execute(mockParam, clientSpy);
            inOrder.verify(mockStatus, times(1)).resetAuthentication();
            inOrder.verifyNoMoreInteractions();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#execute(StatusDependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteAuthenticatedExpiredAutorenewal()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticatedOperation<Object, Object, TestState> operation =
                mock(AuthenticatedOperation.class);
        final Object mockParam = new Object();
        final TestState mockStatus = createMockStatus();
        final Object mockCredentials = createMockCredentials();
        final AuthenticationExpiredException mockExpired = new AuthenticationExpiredException();
        final Object mockResult = new Object();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            clientSpy.setAuthenticationAutoRenewalEnabled(true);
            clientSpy.setStoredCredentials(mockCredentials);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            willDoNothing().given(clientSpy).ensureAuthenticated();
            will(new Answer<Object>() {
                private int operationExecutions = 0;
                @Override
                public Object answer(
                        final InvocationOnMock invocation)
                throws Throwable {
                    operationExecutions++;
                    if (operationExecutions == 1) {
                        throw mockExpired;
                    } else {
                        return mockResult;
                    }
                }
            }).given(operation).execute(mockParam, clientSpy);
            willDoNothing().given(clientSpy).authenticate();
            final Object result = clientSpy.execute(operation, mockParam);
            assertNotNull(result);
            assertSame(mockResult, result);
            final InOrder inOrder = inOrder(clientSpy, operation, mockStatus);
            inOrder.verify(mockStatus, times(1)).resetAuthentication();
            inOrder.verify(clientSpy, times(1)).authenticate();
            inOrder.verify(operation, times(1)).execute(mockParam, clientSpy);
            inOrder.verifyNoMoreInteractions();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#execute(StatusDependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteAuthenticatedExpiredAutorenewalFail()
    throws Throwable {
        final URL url = new URL("https", "some.host.example.com", 3654, "some/path");
        @SuppressWarnings("unchecked")
        final StatusInitOperation<TestState> mockInitOp = mock(StatusInitOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticationOperation<Object, ?, TestState> mockAuthOp = mock(AuthenticationOperation.class);
        @SuppressWarnings("unchecked")
        final AuthenticatedOperation<Object, Object, TestState> operation =
                mock(AuthenticatedOperation.class);
        final Object mockParam = new Object();
        final TestState mockStatus = createMockStatus();
        final Object mockCredentials = createMockCredentials();
        final AuthenticationExpiredException mockExpired = new AuthenticationExpiredException();
        final HttpClientException mockResult = new HttpClientException();
        try (final BaseAuthenticableHttpServiceClient<TestState, Object> client =
                createClientFromUrlAndInitOpAndAuthOp(url, mockInitOp, mockAuthOp)) {
            final BaseAuthenticableHttpServiceClient<TestState, Object> clientSpy = spy(client);
            clientSpy.setAuthenticationAutoRenewalEnabled(true);
            clientSpy.setStoredCredentials(mockCredentials);
            willReturn(mockStatus).given(clientSpy).ensureInitialized();
            willDoNothing().given(clientSpy).ensureAuthenticated();
            will(new Answer<Object>() {
                private int operationExecutions = 0;
                @Override
                public Object answer(
                        final InvocationOnMock invocation)
                throws Throwable {
                    operationExecutions++;
                    if (operationExecutions == 1) {
                        throw mockExpired;
                    } else {
                        throw mockResult;
                    }
                }
            }).given(operation).execute(mockParam, clientSpy);
            willDoNothing().given(clientSpy).authenticate();
            final HttpClientException thrown = assertThrows(HttpClientException.class, () -> {
                clientSpy.execute(operation, mockParam);
            });
            assertNotNull(thrown);
            assertSame(mockResult, thrown);
            final InOrder inOrder = inOrder(clientSpy, operation, mockStatus);
            inOrder.verify(mockStatus, times(1)).resetAuthentication();
            inOrder.verify(clientSpy, times(1)).authenticate();
            inOrder.verify(operation, times(1)).execute(mockParam, clientSpy);
            inOrder.verifyNoMoreInteractions();
        }
    }

    /**
     * Mock implementation of {@code AuthenticableClientStatus}
     * for testing.
     */
    protected static class TestState
    implements AuthenticableClientStatus {

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        public boolean isAuthenticated() {
            return false;
        }

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        public void resetAuthentication() {
            
        }
    }
}
