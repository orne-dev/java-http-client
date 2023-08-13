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

import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import dev.orne.http.client.engine.HttpClientEngine;
import dev.orne.http.client.op.StatusDependentOperation;
import dev.orne.http.client.op.StatusInitOperation;

/**
 * Unit tests for {@code BaseStatedHttpServiceClient}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 * @see BaseStatedHttpServiceClient
 */
@Tag("ut")
class BaseStatedHttpServiceClientTest
extends BaseHttpServiceClientTest {

    /** The default status initialization operation. */
    protected @Mock StatusInitOperation<Object> initOp;

    /**
     * Test for {@link BaseStatedHttpServiceClient#BaseStatedHttpServiceClient(HttpClientEngine, URI, StatusInitOperation)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testUriConstructor_requiredParameters()
    throws Throwable {
        final URI uri = URI.create("http://example.org/base/path/");
        assertThrows(NullPointerException.class, () -> {
            new BaseStatedHttpServiceClient<>(null, uri, initOp);
        });
        assertThrows(NullPointerException.class, () -> {
            new BaseStatedHttpServiceClient<>(engine, (URI) null, initOp);
        });
        assertThrows(NullPointerException.class, () -> {
            new BaseStatedHttpServiceClient<>(engine, uri, (StatusInitOperation<Object>) null);
        });
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#BaseStatedHttpServiceClient(HttpClientEngine, URI, StatusInitOperation)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testUriConstructor()
    throws Throwable {
        final URI uri = URI.create("http://example.org/base/path/");
        try (final BaseStatedHttpServiceClient<Object> client = new BaseStatedHttpServiceClient<>(engine, uri, initOp)) {
            assertSame(engine, client.getEngine());
            assertEquals(uri, client.getBaseURI());
            assertSame(initOp, client.getStatusInitOperation());
            assertNull(client.getStatus());
        }
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#BaseStatedHttpServiceClient(HttpClientEngine, URL, StatusInitOperation)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testUrlConstructor_requiredParameters()
    throws Throwable {
        final URL url = new URL("http://example.org/base/path/");
        assertThrows(NullPointerException.class, () -> {
            new BaseStatedHttpServiceClient<>(null, url, initOp);
        });
        assertThrows(NullPointerException.class, () -> {
            new BaseStatedHttpServiceClient<>(engine, (URL) null, initOp);
        });
        assertThrows(NullPointerException.class, () -> {
            new BaseStatedHttpServiceClient<>(engine, url, (StatusInitOperation<Object>) null);
        });
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#BaseStatedHttpServiceClient(HttpClientEngine, URL, StatusInitOperation)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testUrlConstructor()
    throws Throwable {
        final URI uri = URI.create("http://example.org/base/path/");
        final URL url = new URL("http://example.org/base/path/");
        try (final BaseStatedHttpServiceClient<Object> client = new BaseStatedHttpServiceClient<>(engine, url, initOp)) {
            assertSame(engine, client.getEngine());
            assertEquals(uri, client.getBaseURI());
            assertSame(initOp, client.getStatusInitOperation());
            assertNull(client.getStatus());
        }
    }

    @Override
    protected @NotNull BaseStatedHttpServiceClient<? extends Object> createTestClient() {
        return createTestClient(initOp);
    }

    protected <S> @NotNull BaseStatedHttpServiceClient<S> createTestClient(
            final @NotNull StatusInitOperation<S> initOp) {
        return new BaseStatedHttpServiceClient<>(
                engine,
                URI.create("http://example.org/base/path/"),
                initOp);
    }

    /**
     * Creates a mock status for testing.
     * 
     * @return The created mock status
     */
    protected Object createStatus() {
        return new Object();
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#execute(StatusDependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("operationParameters")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testExecuteStatusDependent_initError(
            final Object params)
    throws Throwable {
        final StatusDependentOperation<Object, Object, Object> operation =
                mock(StatusDependentOperation.class);
        try (final BaseStatedHttpServiceClient client = spy(createTestClient())) {
            final CompletableFuture futureEnsureResult = new CompletableFuture<>();
            willReturn(futureEnsureResult).given(client).ensureInitialized();
            final CompletableFuture<?> futureResult = client.execute(operation, params).toCompletableFuture();
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            then(client).should().ensureInitialized();
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
     * Test for {@link BaseStatedHttpServiceClient#execute(StatusDependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("operationParameters")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testExecuteStatusDependent_operationError(
            final Object params)
    throws Throwable {
        final StatusDependentOperation<Object, Object, Object> operation =
                mock(StatusDependentOperation.class);
        try (final BaseStatedHttpServiceClient client = spy(createTestClient())) {
            final CompletableFuture futureEnsureResult = new CompletableFuture<>();
            final CompletableFuture futureOperationResult = new CompletableFuture<>();
            willReturn(futureEnsureResult).given(client).ensureInitialized();
            given(operation.execute(any(), any(), any())).willReturn(futureOperationResult);
            final CompletableFuture<?> futureResult = client.execute(operation, params).toCompletableFuture();
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            then(client).should().ensureInitialized();
            then(operation).shouldHaveNoInteractions();
            final Object status = createStatus();
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
     * Test for {@link BaseStatedHttpServiceClient#execute(StatusDependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("operationParameters")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testExecuteStatusDependent(
            final Object params)
    throws Throwable {
        final StatusDependentOperation<Object, Object, Object> operation =
                mock(StatusDependentOperation.class);
        try (final BaseStatedHttpServiceClient client = spy(createTestClient())) {
            final CompletableFuture futureEnsureResult = new CompletableFuture<>();
            final CompletableFuture futureOperationResult = new CompletableFuture<>();
            willReturn(futureEnsureResult).given(client).ensureInitialized();
            given(operation.execute(any(), any(), any())).willReturn(futureOperationResult);
            final CompletableFuture<?> futureResult = client.execute(operation, params).toCompletableFuture();
            assertNotNull(futureResult);
            assertFalse(futureResult.isDone());
            then(client).should().ensureInitialized();
            then(operation).shouldHaveNoInteractions();
            final Object status = createStatus();
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
     * Test for {@link BaseStatedHttpServiceClient#setStatus()}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testSetStatus()
    throws Throwable {
        final Object mockStatus = createStatus();
        try (final BaseStatedHttpServiceClient client = createTestClient()) {
            client.setStatus(mockStatus);
            assertSame(mockStatus, client.getStatus());
            then(initOp).shouldHaveNoInteractions();
        }
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#resetStatus()}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testResetStatus()
    throws Throwable {
        final Object oldStatus = createStatus();
        try (final BaseStatedHttpServiceClient client = createTestClient()) {
            client.setStatus(oldStatus);
            client.resetStatus();
            assertNull(client.getStatus());
            then(client.getStatusInitOperation()).shouldHaveNoInteractions();
        }
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#initializeStatus()}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testInitializeStatus()
    throws Throwable {
        try (final BaseStatedHttpServiceClient client = createTestClient()) {
            final CompletableFuture futureInitResult = new CompletableFuture<>();
            given(client.getStatusInitOperation().execute(any(), same(client))).willReturn(futureInitResult);
            final CompletableFuture<?> result = client.initializeStatus().toCompletableFuture();
            assertNotNull(result);
            assertFalse(result.isDone());
            assertNull(client.getStatus());
            final Object mockStatus = createStatus();
            futureInitResult.complete(mockStatus);
            assertSame(mockStatus, result.get());
            assertTrue(result.isDone());
            assertSame(mockStatus, client.getStatus());
            then(client.getStatusInitOperation()).should().execute(any(), same(client));
        }
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#ensureInitialized()}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testEnsureInitialized()
    throws Throwable {
        try (final BaseStatedHttpServiceClient client = createTestClient()) {
            final CompletableFuture futureInitResult = new CompletableFuture<>();
            given(client.getStatusInitOperation().execute(any(), same(client))).willReturn(futureInitResult);
            final CompletableFuture<?> result = client.ensureInitialized().toCompletableFuture();
            assertNotNull(result);
            assertFalse(result.isDone());
            assertNull(client.getStatus());
            final Object mockStatus = createStatus();
            futureInitResult.complete(mockStatus);
            assertSame(mockStatus, result.get());
            assertTrue(result.isDone());
            assertSame(mockStatus, client.getStatus());
            then(client.getStatusInitOperation()).should().execute(any(), same(client));
        }
    }

    /**
     * Test for {@link BaseStatedHttpServiceClient#ensureInitialized()}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void testEnsureInitialized_alreadyInitialized()
    throws Throwable {
        try (final BaseStatedHttpServiceClient client = createTestClient()) {
            final Object status = createStatus();
            client.setStatus(status);
            final CompletableFuture<?> result = client.ensureInitialized().toCompletableFuture();
            assertNotNull(result);
            assertTrue(result.isDone());
            assertSame(status, result.get());
            assertSame(status, client.getStatus());
            then(client.getStatusInitOperation()).should(never()).execute(any(), same(client));
        }
    }
}
