package dev.orne.http.client.engine.apache;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2023 Orne Developments
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

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.io.CloseMode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.http.Methods;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.engine.HttpRequest;
import dev.orne.http.client.engine.HttpRequestCustomizer;
import dev.orne.http.client.engine.HttpResponse;
import dev.orne.http.client.engine.HttpResponseHandler;
import dev.orne.test.rnd.generators.URIGenerator;

/**
 * Unit tests for {@code ApacheHttpClientEngine}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see ApacheHttpClientEngine
 */
@Tag("ut")
class ApacheHttpClientEngineTest {

    private @Mock CookieStore cookieStore;
    private @Mock CloseableHttpClient client;
    private @Mock ExecutorService executor;
    private @Captor ArgumentCaptor<HttpRequest> requestCaptor;
    private @Captor ArgumentCaptor<Runnable> runnableCaptor;
    private @Captor ArgumentCaptor<HttpClientResponseHandler<?>> engineHandlerCaptor;
    private @Captor ArgumentCaptor<HttpResponse> responseCaptor;
    private AutoCloseable mocks;

    @BeforeEach public void openMocks() {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach public void releaseMocks() throws Exception {
        this.mocks.close();
    }

    /**
     * Test for {@link ApacheHttpClientEngine#ApacheHttpClientEngine()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructor()
    throws Throwable {
        try (final ApacheHttpClientEngine engine = new ApacheHttpClientEngine()) {
            assertNotNull(engine.getCookieStore());
            assertNotNull(engine.getCookieStore().getDelegate());
            assertNotNull(engine.getClient());
            assertNotNull(engine.getExecutor());
        }
    }

    /**
     * Test for {@link ApacheHttpClientEngine#ApacheHttpClientEngine()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testComponentsConstructor()
    throws Throwable {
        try (final ApacheHttpClientEngine engine = new ApacheHttpClientEngine(
                cookieStore,
                client,
                executor)) {
            assertNotNull(engine.getCookieStore());
            assertSame(cookieStore, engine.getCookieStore().getDelegate());
            assertSame(client, engine.getClient());
            assertSame(executor, engine.getExecutor());
            then(cookieStore).shouldHaveNoInteractions();
            then(client).shouldHaveNoInteractions();
            then(executor).shouldHaveNoInteractions();
        }
    }

    /**
     * Test for {@link ApacheHttpClientEngine#configureRequestConfig(RequestConfig.Builder)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConfigureRequestConfig()
    throws Throwable {
        final RequestConfig.Builder builder = mock(RequestConfig.Builder.class);
        try (final ApacheHttpClientEngine engine = new ApacheHttpClientEngine(
                cookieStore,
                client,
                executor)) {
            engine.configureRequestConfig(builder);
            then(builder).shouldHaveNoInteractions();
        }
    }

    /**
     * Test for {@link ApacheHttpClientEngine#createRequest(String, URI)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownHttpMethods")
    void testCreateRequest(
            final String method)
    throws Throwable {
        final URI uri = URI.create(URIGenerator.randomAbsolutePath());
        try (final ApacheHttpClientEngine engine = new ApacheHttpClientEngine(
                cookieStore,
                client,
                executor)) {
            final ClassicHttpRequest result = engine.createRequest(method, uri);
            assertEquals(uri, result.getUri());
            assertEquals(method, result.getMethod());
            assertThrows(HttpClientException.class, () -> engine.createRequest("NOT_AN_HTTP_METHOD", uri));
        }
    }

    /**
     * Test for {@link ApacheHttpClientEngine#getHttpContext()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetHttpContext()
    throws Throwable {
        try (final ApacheHttpClientEngine engine = new ApacheHttpClientEngine(
                cookieStore,
                client,
                executor)) {
            assertNull(engine.getHttpContext());
        }
    }

    /**
     * Test for {@link ApacheHttpClientEngine#executeHttpRequest(URI, String, HttpRequestCustomizer, HttpResponseHandler)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownHttpMethods")
    void testExecuteHttpRequest(
            final String method)
    throws Throwable {
        final URI uri = new URI(
                URIGenerator.randomScheme(),
                URIGenerator.randomHostName(),
                URIGenerator.randomAbsolutePath(),
                null);
        final HttpHost host = new HttpHost(uri.getScheme(), uri.getHost(), uri.getPort());
        final URI path = URI.create(uri.getPath());
        final HttpRequestCustomizer requestCustomizer = mock(HttpRequestCustomizer.class);
        final HttpResponseHandler responseHandler = mock(HttpResponseHandler.class);
        final ClassicHttpRequest apacheRequest = mock(ClassicHttpRequest.class);
        final ClassicHttpResponse apacheResponse = mock(ClassicHttpResponse.class);
        final HttpContext context = mock(HttpContext.class);
        try (final ApacheHttpClientEngine engine = spy(new ApacheHttpClientEngine(
                cookieStore,
                client,
                executor))) {
            willReturn(apacheRequest).given(engine).createRequest(method, path);
            willReturn(context).given(engine).getHttpContext();
            final CompletableFuture<Void> result = engine.executeHttpRequest(
                    uri,
                    method,
                    requestCustomizer,
                    responseHandler).toCompletableFuture();
            then(requestCustomizer).should().customizeRequest(requestCaptor.capture());
            then(requestCustomizer).shouldHaveNoMoreInteractions();
            then(responseHandler).shouldHaveNoInteractions();
            then(executor).should().execute(runnableCaptor.capture());
            then(executor).shouldHaveNoMoreInteractions();
            then(client).shouldHaveNoInteractions();
            assertFalse(result.isDone());
            final ApacheHttpRequest request = assertInstanceOf(
                    ApacheHttpRequest.class,
                    requestCaptor.getValue());
            assertSame(apacheRequest, request.getDelegate());
            final Runnable delayedTask = runnableCaptor.getValue();
            delayedTask.run();
            then(client).should().execute(
                    eq(host),
                    eq(apacheRequest),
                    eq(context),
                    engineHandlerCaptor.capture());
            then(client).shouldHaveNoMoreInteractions();
            then(responseHandler).shouldHaveNoInteractions();
            assertFalse(result.isDone());
            final HttpClientResponseHandler<?> clientResponseHandler = engineHandlerCaptor.getValue();
            clientResponseHandler.handleResponse(apacheResponse);
            then(responseHandler).should().handle(responseCaptor.capture());
            then(responseHandler).shouldHaveNoMoreInteractions();
            final ApacheHttpResponse response = assertInstanceOf(
                    ApacheHttpResponse.class,
                    responseCaptor.getValue());
            assertSame(apacheResponse, response.getDelegate());
            assertTrue(result.isDone());
            assertDoesNotThrow(() -> result.get());
        }
    }

    /**
     * Test for {@link ApacheHttpClientEngine#executeHttpRequest(URI, String, HttpRequestCustomizer, HttpResponseHandler)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownHttpMethods")
    void testExecuteHttpRequest_IOException(
            final String method)
    throws Throwable {
        final URI uri = new URI(
                URIGenerator.randomScheme(),
                URIGenerator.randomHostName(),
                URIGenerator.randomAbsolutePath(),
                null);
        final HttpHost host = new HttpHost(uri.getScheme(), uri.getHost(), uri.getPort());
        final URI path = URI.create(uri.getPath());
        final HttpRequestCustomizer requestCustomizer = mock(HttpRequestCustomizer.class);
        final HttpResponseHandler responseHandler = mock(HttpResponseHandler.class);
        final ClassicHttpRequest apacheRequest = mock(ClassicHttpRequest.class);
        final IOException clientException = new IOException();
        final HttpContext context = mock(HttpContext.class);
        try (final ApacheHttpClientEngine engine = spy(new ApacheHttpClientEngine(
                cookieStore,
                client,
                executor))) {
            willReturn(apacheRequest).given(engine).createRequest(method, path);
            willReturn(context).given(engine).getHttpContext();
            final CompletableFuture<Void> result = engine.executeHttpRequest(
                    uri,
                    method,
                    requestCustomizer,
                    responseHandler).toCompletableFuture();
            then(requestCustomizer).should().customizeRequest(requestCaptor.capture());
            then(requestCustomizer).shouldHaveNoMoreInteractions();
            then(responseHandler).shouldHaveNoInteractions();
            then(executor).should().execute(runnableCaptor.capture());
            then(executor).shouldHaveNoMoreInteractions();
            then(client).shouldHaveNoInteractions();
            assertFalse(result.isDone());
            final ApacheHttpRequest request = assertInstanceOf(
                    ApacheHttpRequest.class,
                    requestCaptor.getValue());
            assertSame(apacheRequest, request.getDelegate());
            given(client.execute(eq(host), eq(apacheRequest), eq(context), any())).willThrow(clientException);
            final Runnable delayedTask = runnableCaptor.getValue();
            delayedTask.run();
            then(client).should().execute(
                    eq(host),
                    eq(apacheRequest),
                    eq(context),
                    engineHandlerCaptor.capture());
            then(client).shouldHaveNoMoreInteractions();
            then(responseHandler).shouldHaveNoInteractions();
            assertTrue(result.isDone());
            final ExecutionException futureErr = assertThrows(ExecutionException.class, () -> result.get());
            assertSame(clientException, futureErr.getCause());
        }
    }

    /**
     * Test for {@link ApacheHttpClientEngine#close()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testClose()
    throws Throwable {
        try (final ApacheHttpClientEngine engine = new ApacheHttpClientEngine(
                cookieStore,
                client,
                executor)) {
            then(cookieStore).shouldHaveNoInteractions();
            then(client).shouldHaveNoInteractions();
            then(executor).shouldHaveNoInteractions();
        }
        
        then(cookieStore).shouldHaveNoInteractions();
        then(client).should().close(CloseMode.GRACEFUL);
        then(client).shouldHaveNoMoreInteractions();
        then(executor).shouldHaveNoInteractions();
    }

    private static Stream<Arguments> knownHttpMethods() {
        return Stream.of(Methods.class.getDeclaredFields())
                .filter(field -> {
                    return String.class.equals(field.getType())
                            && Modifier.isStatic(field.getModifiers())
                            && Modifier.isFinal(field.getModifiers());
                })
                .map(field -> {
                    try {
                        return String.valueOf(field.get(null));
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(method -> !Methods.CONNECT.equals(method))
                .map(value -> Arguments.of(value));
    }
}
