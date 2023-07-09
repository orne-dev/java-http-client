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
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;

import dev.orne.http.client.engine.HttpClientEngine;
import dev.orne.http.client.op.AuthenticatedOperation;
import dev.orne.http.client.op.AuthenticationOperation;
import dev.orne.http.client.op.StatusInitOperation;

/**
 * Unit tests for {@code BaseAuthenticableHttpServiceClient}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 * @see BaseAuthenticableHttpServiceClient
 */
@Tag("ut")
class BaseAuthenticableHttpServiceClientTest
extends BaseStatedHttpServiceClientTest {

    protected @Mock StatusInitOperation<AuthenticableClientStatus> authInitOp;
    protected @Mock AuthenticationOperation<Object, Object, AuthenticableClientStatus> authOp;

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#BaseAuthenticableHttpServiceClient(HttpClientEngine, URI, StatusInitOperation, AuthenticationOperation)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testUriConstructor_requiredParameters()
    throws Throwable {
        final URI uri = URI.create("http://example.org/base/path/");
        assertThrows(NullPointerException.class, () -> {
            new BaseAuthenticableHttpServiceClient<>(null, uri, authInitOp, authOp);
        });
        assertThrows(NullPointerException.class, () -> {
            new BaseAuthenticableHttpServiceClient<>(engine, (URI) null, authInitOp, authOp);
        });
        assertThrows(NullPointerException.class, () -> {
            new BaseAuthenticableHttpServiceClient<>(engine, uri, (StatusInitOperation<AuthenticableClientStatus>) null, authOp);
        });
        assertThrows(NullPointerException.class, () -> {
            new BaseAuthenticableHttpServiceClient<>(engine, uri, authInitOp, null);
        });
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#BaseAuthenticableHttpServiceClient(HttpClientEngine, URI, StatusInitOperation, AuthenticationOperation)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testUriConstructor()
    throws Throwable {
        final URI uri = URI.create("http://example.org/base/path/");
        try (final BaseAuthenticableHttpServiceClient<AuthenticableClientStatus, Object> client =
                new BaseAuthenticableHttpServiceClient<>(engine, uri, authInitOp, authOp)) {
            assertSame(engine, client.getEngine());
            assertEquals(uri, client.getBaseURI());
            assertSame(authInitOp, client.getStatusInitOperation());
            assertSame(authOp, client.getAuthenticationOperation());
            assertNull(client.getStatus());
            assertFalse(client.isCredentialsStoringEnabled());
            assertFalse(client.hasStoredCredentials());
            assertFalse(client.isAuthenticationAutoRenewalEnabled());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#BaseAuthenticableHttpServiceClient(HttpClientEngine, ULR, StatusInitOperation, AuthenticationOperation)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testUrlConstructor_requiredParameters()
    throws Throwable {
        final URL url = new URL("http://example.org/base/path/");
        assertThrows(NullPointerException.class, () -> {
            new BaseAuthenticableHttpServiceClient<>(null, url, authInitOp, authOp);
        });
        assertThrows(NullPointerException.class, () -> {
            new BaseAuthenticableHttpServiceClient<>(engine, (URL) null, authInitOp, authOp);
        });
        assertThrows(NullPointerException.class, () -> {
            new BaseAuthenticableHttpServiceClient<>(engine, url, (StatusInitOperation<AuthenticableClientStatus>) null, authOp);
        });
        assertThrows(NullPointerException.class, () -> {
            new BaseAuthenticableHttpServiceClient<>(engine, url, authInitOp, null);
        });
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#BaseAuthenticableHttpServiceClient(HttpClientEngine, ULR, StatusInitOperation, AuthenticationOperation)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testUrlConstructor()
    throws Throwable {
        final HttpClientEngine engine = mock(HttpClientEngine.class);
        final URI uri = URI.create("http://example.org/base/path/");
        final URL url = new URL("http://example.org/base/path/");
        try (final BaseAuthenticableHttpServiceClient<AuthenticableClientStatus, Object> client =
                new BaseAuthenticableHttpServiceClient<>(engine, url, authInitOp, authOp)) {
            assertSame(engine, client.getEngine());
            assertEquals(uri, client.getBaseURI());
            assertSame(authInitOp, client.getStatusInitOperation());
            assertSame(authOp, client.getAuthenticationOperation());
            assertNull(client.getStatus());
            assertFalse(client.isCredentialsStoringEnabled());
            assertFalse(client.hasStoredCredentials());
            assertFalse(client.isAuthenticationAutoRenewalEnabled());
        }
    }

    @Override
    protected @NotNull BaseAuthenticableHttpServiceClient<? extends AuthenticableClientStatus, ?> createTestClient() {
        return createTestClient(authInitOp, authOp);
    }

    protected <S extends AuthenticableClientStatus, C> @NotNull BaseAuthenticableHttpServiceClient<S, C> createTestClient(
            final @NotNull StatusInitOperation<S> initOp,
            final @NotNull AuthenticationOperation<C, ?, S> authOp) {
        return new BaseAuthenticableHttpServiceClient<>(
                engine,
                URI.create("http://example.org/base/path/"),
                initOp,
                authOp);
    }

    @Override
    protected AuthenticableClientStatus createStatus() {
        return mock(AuthenticableClientStatus.class);
    }

    protected Object createCredentials() {
        return new Object();
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#setCredentialsStoringEnabled(boolean)}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testSetCredentialsStoringEnabled()
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient client = createTestClient()) {
            assertFalse(client.isCredentialsStoringEnabled());
            client.setCredentialsStoringEnabled(true);
            assertTrue(client.isCredentialsStoringEnabled());
            client.setStoredCredentials(createCredentials());
            assertTrue(client.hasStoredCredentials());
            client.setCredentialsStoringEnabled(false);
            assertFalse(client.isCredentialsStoringEnabled());
            assertFalse(client.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#setAuthenticationAutoRenewalEnabled(boolean)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetAuthenticationAutoRenewalEnabled()
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient<?, ?> client = createTestClient()) {
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testSetStoredCredentials()
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient client = createTestClient()) {
            assertFalse(client.hasStoredCredentials());
            client.setStoredCredentials(createCredentials());
            assertFalse(client.hasStoredCredentials());
            client.setCredentialsStoringEnabled(true);
            client.setStoredCredentials(createCredentials());
            assertTrue(client.hasStoredCredentials());
            client.setStoredCredentials(null);
            assertFalse(client.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate(Object)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testAuthenticate(
            final boolean credStorageEnabled)
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            client.setCredentialsStoringEnabled(credStorageEnabled);
            final AuthenticableClientStatus status = createStatus();
            client.setStatus(status);
            final Object credentials = createCredentials();
            final CompletableFuture futureAuthResult = new CompletableFuture<>();
            given(client.getAuthenticationOperation().execute(any(), any(), same(client))).willReturn(futureAuthResult);
            final CompletableFuture<?> futureResult = client.authenticate(credentials);
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            final Object authResult = new Object();
            futureAuthResult.complete(authResult);
            assertTrue(futureResult.isDone());
            assertSame(status, futureResult.get());
            final InOrder callOrder = inOrder(client, client.getAuthenticationOperation());
            callOrder.verify(client).ensureInitialized();
            callOrder.verify(client.getAuthenticationOperation()).execute(credentials, status, client);
            then(client.getAuthenticationOperation()).shouldHaveNoMoreInteractions();
            assertEquals(credStorageEnabled, client.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate(Object)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testAuthenticate_initError(
            final boolean credStorageEnabled)
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            client.setCredentialsStoringEnabled(credStorageEnabled);
            final AuthenticableClientStatus status = createStatus();
            client.setStatus(status);
            final Object credentials = createCredentials();
            final CompletableFuture futureInitResult = new CompletableFuture<>();
            willReturn(futureInitResult).given(client).ensureInitialized();
            final CompletableFuture<?> futureResult = client.authenticate(credentials);
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            final HttpClientException exception = new HttpClientException();
            futureInitResult.completeExceptionally(exception);
            assertTrue(futureResult.isDone());
            final Exception result = assertThrows(Exception.class, () -> futureResult.get());
            assertSame(exception, HttpClientException.unwrapFutureException(result));
            then(client.getStatusInitOperation()).shouldHaveNoInteractions();
            then(client.getAuthenticationOperation()).shouldHaveNoInteractions();
            assertFalse(client.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate(Object)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testAuthenticate_authError(
            final boolean credStorageEnabled)
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            client.setCredentialsStoringEnabled(credStorageEnabled);
            final AuthenticableClientStatus status = createStatus();
            client.setStatus(status);
            final Object credentials = createCredentials();
            final CompletableFuture futureAuthResult = new CompletableFuture<>();
            given(client.getAuthenticationOperation().execute(any(), any(), same(client))).willReturn(futureAuthResult);
            final CompletableFuture<?> futureResult = client.authenticate(credentials);
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            final HttpClientException exception = new HttpClientException();
            futureAuthResult.completeExceptionally(exception);
            assertTrue(futureResult.isDone());
            final Exception result = assertThrows(Exception.class, () -> futureResult.get());
            assertSame(exception, HttpClientException.unwrapFutureException(result));
            final InOrder callOrder = inOrder(client, client.getAuthenticationOperation());
            callOrder.verify(client).ensureInitialized();
            callOrder.verify(client.getAuthenticationOperation()).execute(credentials, status, client);
            then(client.getStatusInitOperation()).shouldHaveNoInteractions();
            then(client.getAuthenticationOperation()).shouldHaveNoMoreInteractions();
            assertFalse(client.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate()}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testAuthenticate_storedCredentials()
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            client.setCredentialsStoringEnabled(true);
            final AuthenticableClientStatus status = createStatus();
            client.setStatus(status);
            final Object credentials = createCredentials();
            client.setStoredCredentials(credentials);
            final CompletableFuture futureAuthResult = new CompletableFuture<>();
            given(client.getAuthenticationOperation().execute(any(), any(), same(client))).willReturn(futureAuthResult);
            final CompletableFuture<?> futureResult = client.authenticate();
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            final Object authResult = new Object();
            futureAuthResult.complete(authResult);
            assertTrue(futureResult.isDone());
            assertSame(status, futureResult.get());
            final InOrder callOrder = inOrder(client, client.getAuthenticationOperation());
            callOrder.verify(client).ensureInitialized();
            callOrder.verify(client.getAuthenticationOperation()).execute(credentials, status, client);
            then(client.getAuthenticationOperation()).shouldHaveNoMoreInteractions();
            assertTrue(client.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate()}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testAuthenticate_storedCredentials_initError()
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            client.setCredentialsStoringEnabled(true);
            final AuthenticableClientStatus status = createStatus();
            client.setStatus(status);
            final Object credentials = createCredentials();
            client.setStoredCredentials(credentials);
            final CompletableFuture futureInitResult = new CompletableFuture<>();
            willReturn(futureInitResult).given(client).ensureInitialized();
            final CompletableFuture<?> futureResult = client.authenticate();
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            final HttpClientException exception = new HttpClientException();
            futureInitResult.completeExceptionally(exception);
            assertTrue(futureResult.isDone());
            final Exception result = assertThrows(Exception.class, () -> futureResult.get());
            assertSame(exception, HttpClientException.unwrapFutureException(result));
            then(client.getStatusInitOperation()).shouldHaveNoInteractions();
            then(client.getAuthenticationOperation()).shouldHaveNoInteractions();
            assertTrue(client.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate()}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes" })
    void testAuthenticate_storedCredentials_noCredentials()
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            client.setCredentialsStoringEnabled(true);
            final CompletableFuture<?> futureResult = client.authenticate();
            assertNotNull(futureResult);
            assertTrue(futureResult.isDone());
            final Exception result = assertThrows(Exception.class, () -> futureResult.get());
            assertInstanceOf(CredentialsNotStoredException.class, HttpClientException.unwrapFutureException(result));
            then(client.getStatusInitOperation()).shouldHaveNoInteractions();
            then(client.getAuthenticationOperation()).shouldHaveNoInteractions();
            assertFalse(client.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate()}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testAuthenticate_storedCredentials_authError()
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            client.setCredentialsStoringEnabled(true);
            final AuthenticableClientStatus status = createStatus();
            client.setStatus(status);
            final Object credentials = createCredentials();
            client.setStoredCredentials(credentials);
            final CompletableFuture futureAuthResult = new CompletableFuture<>();
            given(client.getAuthenticationOperation().execute(any(), any(), same(client))).willReturn(futureAuthResult);
            final CompletableFuture<?> futureResult = client.authenticate();
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            final HttpClientException exception = new HttpClientException();
            futureAuthResult.completeExceptionally(exception);
            assertTrue(futureResult.isDone());
            final Exception result = assertThrows(Exception.class, () -> futureResult.get());
            assertSame(exception, HttpClientException.unwrapFutureException(result));
            final InOrder callOrder = inOrder(client, client.getAuthenticationOperation());
            callOrder.verify(client, times(1)).ensureInitialized();
            callOrder.verify(client.getAuthenticationOperation(), times(1)).execute(credentials, status, client);
            then(client.getStatusInitOperation()).shouldHaveNoInteractions();
            then(client.getAuthenticationOperation()).shouldHaveNoMoreInteractions();
            assertTrue(client.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#authenticate()}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testAuthenticate_storedCredentials_authInvalidCredentialsError()
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            client.setCredentialsStoringEnabled(true);
            final AuthenticableClientStatus status = createStatus();
            client.setStatus(status);
            final Object credentials = createCredentials();
            client.setStoredCredentials(credentials);
            final CompletableFuture futureAuthResult = new CompletableFuture<>();
            given(client.getAuthenticationOperation().execute(any(), any(), same(client))).willReturn(futureAuthResult);
            final CompletableFuture<?> futureResult = client.authenticate();
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            final CredentialsInvalidException exception = new CredentialsInvalidException();
            futureAuthResult.completeExceptionally(exception);
            assertTrue(futureResult.isDone());
            final Exception result = assertThrows(Exception.class, () -> futureResult.get());
            assertSame(exception, HttpClientException.unwrapFutureException(result));
            final InOrder callOrder = inOrder(client, client.getAuthenticationOperation());
            callOrder.verify(client, times(1)).ensureInitialized();
            callOrder.verify(client.getAuthenticationOperation(), times(1)).execute(credentials, status, client);
            then(client.getStatusInitOperation()).shouldHaveNoInteractions();
            then(client.getAuthenticationOperation()).shouldHaveNoMoreInteractions();
            assertFalse(client.hasStoredCredentials());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#ensureAuthenticated()}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testEnsureAuthenticated()
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            final CompletableFuture futureInitResult = new CompletableFuture<>();
            willReturn(futureInitResult).given(client).ensureInitialized();
            final CompletableFuture futureAuthResult = new CompletableFuture<>();
            willReturn(futureAuthResult).given(client).authenticate();
            final CompletableFuture<?> futureResult = client.ensureAuthenticated();
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            then(client).should().ensureInitialized();
            then(client).should(never()).authenticate();
            final AuthenticableClientStatus status = createStatus();
            given(status.isAuthenticated()).willReturn(false);
            futureInitResult.complete(status);
            assertFalse(futureResult.isDone());
            then(client).should().ensureInitialized();
            then(client).should().authenticate();
            final AuthenticableClientStatus result = createStatus();
            futureAuthResult.complete(result);
            assertTrue(futureResult.isDone());
            assertSame(result, futureResult.get());
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#ensureAuthenticated()}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes" })
    void testEnsureAuthenticated_initError()
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            final CompletableFuture futureInitResult = new CompletableFuture<>();
            willReturn(futureInitResult).given(client).ensureInitialized();
            final CompletableFuture<?> futureResult = client.ensureAuthenticated();
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            then(client).should().ensureInitialized();
            then(client).should(never()).authenticate();
            final HttpClientException exception = new HttpClientException();
            futureInitResult.completeExceptionally(exception);
            assertTrue(futureResult.isDone());
            final Exception result = assertThrows(Exception.class, () -> futureResult.get());
            assertSame(exception, HttpClientException.unwrapFutureException(result));
            then(client).should().ensureInitialized();
            then(client).should(never()).authenticate();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#ensureAuthenticated()}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testEnsureAuthenticated_alreadyAuthenticated()
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            final CompletableFuture futureInitResult = new CompletableFuture<>();
            willReturn(futureInitResult).given(client).ensureInitialized();
            final CompletableFuture<?> futureResult = client.ensureAuthenticated();
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            then(client).should().ensureInitialized();
            then(client).should(never()).authenticate();
            final AuthenticableClientStatus status = createStatus();
            given(status.isAuthenticated()).willReturn(true);
            futureInitResult.complete(status);
            assertTrue(futureResult.isDone());
            assertSame(status, futureResult.get());
            then(client).should().ensureInitialized();
            then(client).should(never()).authenticate();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#ensureAuthenticated()}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testEnsureAuthenticated_authError()
    throws Throwable {
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            final CompletableFuture futureInitResult = new CompletableFuture<>();
            willReturn(futureInitResult).given(client).ensureInitialized();
            final CompletableFuture futureAuthResult = new CompletableFuture<>();
            willReturn(futureAuthResult).given(client).authenticate();
            final CompletableFuture<?> futureResult = client.ensureAuthenticated();
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            then(client).should().ensureInitialized();
            then(client).should(never()).authenticate();
            final AuthenticableClientStatus status = createStatus();
            given(status.isAuthenticated()).willReturn(false);
            futureInitResult.complete(status);
            assertFalse(futureResult.isDone());
            then(client).should().ensureInitialized();
            then(client).should().authenticate();
            final HttpClientException exception = new HttpClientException();
            futureAuthResult.completeExceptionally(exception);
            final Exception result = assertThrows(Exception.class, () -> futureResult.get());
            assertSame(exception, HttpClientException.unwrapFutureException(result));
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#executeAuthenticated(AuthenticatedOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("operationParameters")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testExecuteAuthenticated(
            final Object params)
    throws Throwable {
        final AuthenticatedOperation<Object, Object, AuthenticableClientStatus> operation =
                mock(AuthenticatedOperation.class);
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            final CompletableFuture futureEnsureResult = new CompletableFuture<>();
            willReturn(futureEnsureResult).given(client).ensureAuthenticated();
            final CompletableFuture futureOperationResult = new CompletableFuture<>();
            given(operation.execute(any(), any(), any())).willReturn(futureOperationResult);
            final CompletableFuture<?> futureResult = client.execute(operation, params);
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            then(client).should().ensureAuthenticated();
            then(operation).shouldHaveNoInteractions();
            final AuthenticableClientStatus status = createStatus();
            client.setStatus(status);
            futureEnsureResult.complete(status);
            assertFalse(futureResult.isDone());
            final Object result = new Object();
            futureOperationResult.complete(result);
            assertTrue(futureResult.isDone());
            assertSame(result, futureResult.get());
            then(operation).should().execute(params, status, client);
            then(operation).shouldHaveNoMoreInteractions();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#executeAuthenticated(AuthenticatedOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("operationParameters")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testExecuteAuthenticated_initError(
            final Object params)
    throws Throwable {
        final AuthenticatedOperation<Object, Object, AuthenticableClientStatus> operation =
                mock(AuthenticatedOperation.class);
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            final CompletableFuture futureEnsureResult = new CompletableFuture<>();
            willReturn(futureEnsureResult).given(client).ensureAuthenticated();
            final CompletableFuture<?> futureResult = client.execute(operation, params);
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            then(client).should().ensureAuthenticated();
            then(operation).shouldHaveNoInteractions();
            final HttpClientException exception = new HttpClientException();
            futureEnsureResult.completeExceptionally(exception);
            assertTrue(futureResult.isDone());
            final Exception result = assertThrows(Exception.class, () -> futureResult.get());
            assertSame(exception, HttpClientException.unwrapFutureException(result));
            then(operation).shouldHaveNoInteractions();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#executeAuthenticated(AuthenticatedOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("operationParameters")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testExecuteAuthenticatedOperation_operationError(
            final Object params)
    throws Throwable {
        final AuthenticatedOperation<Object, Object, AuthenticableClientStatus> operation =
                mock(AuthenticatedOperation.class);
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            final CompletableFuture futureEnsureResult = new CompletableFuture<>();
            willReturn(futureEnsureResult).given(client).ensureAuthenticated();
            final CompletableFuture futureOperationResult = new CompletableFuture<>();
            given(operation.execute(any(), any(), any())).willReturn(futureOperationResult);
            final CompletableFuture<?> futureResult = client.execute(operation, params);
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            then(client).should().ensureAuthenticated();
            then(operation).shouldHaveNoInteractions();
            final AuthenticableClientStatus status = createStatus();
            client.setStatus(status);
            futureEnsureResult.complete(status);
            assertFalse(futureResult.isDone());
            final HttpClientException exception = new HttpClientException();
            futureOperationResult.completeExceptionally(exception);
            assertTrue(futureResult.isDone());
            final Exception result = assertThrows(Exception.class, () -> futureResult.get());
            assertSame(exception, HttpClientException.unwrapFutureException(result));
            then(operation).should().execute(params, status, client);
            then(operation).shouldHaveNoMoreInteractions();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#executeAuthenticated(AuthenticatedOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("operationParameters")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testExecuteAuthenticatedOperation_authExpired_noPolicy(
            final Object params)
    throws Throwable {
        final AuthenticatedOperation<Object, Object, AuthenticableClientStatus> operation =
                mock(AuthenticatedOperation.class);
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            client.setAuthenticationAutoRenewalEnabled(true);
            final CompletableFuture futureEnsureResult = new CompletableFuture<>();
            willReturn(futureEnsureResult).given(client).ensureAuthenticated();
            final CompletableFuture futureOperationResult = new CompletableFuture<>();
            given(operation.execute(any(), any(), any())).willReturn(futureOperationResult);
            final CompletableFuture<?> futureResult = client.execute(operation, params);
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            then(client).should().ensureAuthenticated();
            then(operation).shouldHaveNoInteractions();
            final AuthenticableClientStatus status = createStatus();
            client.setStatus(status);
            futureEnsureResult.complete(status);
            assertFalse(futureResult.isDone());
            final AuthenticationExpiredException exception = new AuthenticationExpiredException();
            futureOperationResult.completeExceptionally(exception);
            assertTrue(futureResult.isDone());
            final Exception result = assertThrows(Exception.class, () -> futureResult.get());
            assertSame(exception, HttpClientException.unwrapFutureException(result));
            then(operation).should().execute(params, status, client);
            then(operation).shouldHaveNoMoreInteractions();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#executeAuthenticated(AuthenticatedOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("operationParameters")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testExecuteAuthenticatedOperation_authExpired_renewalDisabled(
            final Object params)
    throws Throwable {
        final AuthenticatedOperation<Object, Object, AuthenticableClientStatus> operation =
                mock(AuthenticatedOperation.class);
        final AuthenticationAutoRenewalPolicy policy = mock(AuthenticationAutoRenewalPolicy.class);
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            given(client.getAuthenticationOperation().getAutoRenewalPolicy()).willReturn(policy);
            final CompletableFuture futureEnsureResult = new CompletableFuture<>();
            willReturn(futureEnsureResult).given(client).ensureAuthenticated();
            final CompletableFuture futureOperationResult = new CompletableFuture<>();
            given(operation.execute(any(), any(), any())).willReturn(futureOperationResult);
            final CompletableFuture<?> futureResult = client.execute(operation, params);
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            then(client).should().ensureAuthenticated();
            then(operation).shouldHaveNoInteractions();
            final AuthenticableClientStatus status = createStatus();
            client.setStatus(status);
            futureEnsureResult.complete(status);
            assertFalse(futureResult.isDone());
            final AuthenticationExpiredException exception = new AuthenticationExpiredException();
            futureOperationResult.completeExceptionally(exception);
            assertTrue(futureResult.isDone());
            final Exception result = assertThrows(Exception.class, () -> futureResult.get());
            assertSame(exception, HttpClientException.unwrapFutureException(result));
            then(operation).should().execute(params, status, client);
            then(operation).shouldHaveNoMoreInteractions();
            then(policy).shouldHaveNoInteractions();
        }
    }

    /**
     * Test for {@link BaseAuthenticableHttpServiceClient#executeAuthenticated(AuthenticatedOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("operationParameters")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testExecuteAuthenticatedOperation_authExpired_renewalEnabled(
            final Object params)
    throws Throwable {
        final AuthenticatedOperation<Object, Object, AuthenticableClientStatus> operation =
                mock(AuthenticatedOperation.class);
        final AuthenticationAutoRenewalPolicy policy = mock(AuthenticationAutoRenewalPolicy.class);
        try (final BaseAuthenticableHttpServiceClient client = spy(createTestClient())) {
            client.setAuthenticationAutoRenewalEnabled(true);
            given(client.getAuthenticationOperation().getAutoRenewalPolicy()).willReturn(policy);
            final CompletableFuture futureEnsureResult = new CompletableFuture<>();
            willReturn(futureEnsureResult).given(client).ensureAuthenticated();
            final CompletableFuture futureOperationResult = new CompletableFuture<>();
            given(operation.execute(any(), any(), any())).willReturn(futureOperationResult);
            final CompletableFuture<Object> futureResult = client.execute(operation, params);
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            then(client).should().ensureAuthenticated();
            then(operation).shouldHaveNoInteractions();
            final AuthenticableClientStatus status = createStatus();
            client.setStatus(status);
            futureEnsureResult.complete(status);
            assertFalse(futureResult.isDone());
            final AuthenticationExpiredException exception = new AuthenticationExpiredException();
            futureOperationResult.completeExceptionally(exception);
            assertFalse(futureResult.isDone());
            final ArgumentCaptor<Supplier<CompletableFuture<? extends AuthenticableClientStatus>>> doAuthenticateCaptor =
                    ArgumentCaptor.forClass(Supplier.class);
            final ArgumentCaptor<Function<AuthenticableClientStatus, CompletableFuture<Object>>> opExecuterCaptor =
                    ArgumentCaptor.forClass(Function.class);
            then(operation).should().execute(params, status, client);
            then(operation).shouldHaveNoMoreInteractions();
            then(policy).should().<Object, AuthenticableClientStatus>apply(
                    doAuthenticateCaptor.capture(),
                    opExecuterCaptor.capture(),
                    eq(futureResult));
            then(policy).shouldHaveNoMoreInteractions();
            then(client).should(never()).authenticate();
            final Object result = new Object();
            futureResult.complete(result);
            assertTrue(futureResult.isDone());
            assertSame(result, futureResult.get());
            // Verify authentication auto renewal parameters
            final CompletableFuture futureDoAuthenticateResult = new CompletableFuture<>();
            willReturn(futureDoAuthenticateResult).given(client).authenticate();
            final CompletableFuture doAuthenticateResult = doAuthenticateCaptor.getValue().get();
            assertSame(futureDoAuthenticateResult, doAuthenticateResult);
            then(client).should().authenticate();
            final CompletableFuture futureDoOperationResult = new CompletableFuture<>();
            given(operation.execute(any(), any(), any())).willReturn(futureDoOperationResult);
            final CompletableFuture doOperationResult = opExecuterCaptor.getValue().apply(status);
            assertSame(futureDoOperationResult, doOperationResult);
            then(operation).should(times(2)).execute(params, status, client);
        }
    }
}
