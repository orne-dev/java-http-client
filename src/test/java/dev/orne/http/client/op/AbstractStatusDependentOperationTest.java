package dev.orne.http.client.op;

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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpResponseHandlingException;
import dev.orne.http.client.HttpResponseStatusException;
import dev.orne.http.client.StatedHttpServiceClient;
import dev.orne.http.client.engine.HttpClientEngine;
import dev.orne.http.client.engine.HttpRequest;
import dev.orne.http.client.engine.HttpRequestCustomizer;
import dev.orne.http.client.engine.HttpResponse;
import dev.orne.http.client.engine.HttpResponseBody;

/**
 * Unit tests for {@code AbstractStatusDependentOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractStatusDependentOperation
 */
@Tag("ut")
class AbstractStatusDependentOperationTest
extends AbstractHttpServiceOperationTest {

    private @Captor ArgumentCaptor<HttpRequestCustomizer> requestCustomizerCaptor;
    private AutoCloseable mocks;

    @BeforeEach public void openMocks() {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach public void releaseMocks() throws Exception {
        this.mocks.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractStatusDependentOperation<Object, Object, Object, Object> createOperation() {
        return spy(AbstractStatusDependentOperation.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected StatedHttpServiceClient<Object> createMockClient() {
        return mock(StatedHttpServiceClient.class);
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#createResponseHandler(Object, Object)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testHandlerWrapper(
            final boolean handleResponseThrows)
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final HttpResponse response = mock(HttpResponse.class);
        final OperationResponseHandler<Object> handler = operation.createResponseHandler(params, status);
        if (handleResponseThrows) {
            final HttpClientException exception = new HttpClientException();
            willThrow(exception).given(operation).handleResponse(params, status, response);
            handler.handle(response);
            then(operation).should().handleResponse(params, status, response);
            final HttpClientException result = assertThrows(HttpClientException.class, () -> handler.getResult());
            assertSame(exception, result);
        } else {
            final Object opResult = new Object();
            willReturn(opResult).given(operation).handleResponse(params, status, response);
            handler.handle(response);
            then(operation).should().handleResponse(params, status, response);
            final Object result = handler.getResult();
            assertSame(opResult, result);
        }
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#handleResponse(Object, Object, HttpResponse)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testHandleResponse(
            final boolean responseBody)
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final HttpResponse response = mock(HttpResponse.class);
        willDoNothing().given(operation).processResponseStatus(response);
        final HttpResponseBody body;
        final Object entity;
        if (responseBody) {
            body = mock(HttpResponseBody.class);
            entity = new Object();
            given(operation.parseResponse(params, status, response, body)).willReturn(entity);
        } else {
            body = null;
            entity = null;
        }
        given(response.getBody()).willReturn(body);
        final Object opResult = new Object();
        given(operation.processResponse(params, status, entity, response)).willReturn(opResult);
        final Object result = operation.handleResponse(params, status, response);
        assertSame(opResult, result);
        then(operation).should().processResponseStatus(response);
        if (responseBody) {
            then(operation).should().parseResponse(params, status, response, body);
        } else {
            then(operation).should(never()).parseResponse(any(), any(), any(), any());
        }
        then(operation).should().processResponse(params, status, entity, response);
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#handleResponse(Object, Object, HttpResponse)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @CsvSource({"false,false", "false,true", "true,false", "true,true"})
    void testHandleResponse_unexpectedStatusCode(
            final boolean responseBody,
            final boolean exceptionResult)
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final HttpResponse response = mock(HttpResponse.class);
        final HttpResponseStatusException statusException = new HttpResponseStatusException(500, "Mock reason");
        willThrow(statusException).given(operation).processResponseStatus(response);
        final HttpResponseBody body;
        if (responseBody) {
            body = mock(HttpResponseBody.class);
        } else {
            body = null;
        }
        given(response.getBody()).willReturn(body);
        if (exceptionResult) {
            final HttpClientException convertedException = new HttpClientException();
            willThrow(convertedException).given(operation).processResponseStatusException(response, statusException);
            final HttpClientException result = assertThrows(
                    HttpClientException.class,
                    () -> operation.handleResponse(params, status, response));
            assertSame(convertedException, result);
        } else {
            final Object opResult = new Object();
            willReturn(opResult).given(operation).processResponseStatusException(response, statusException);
            final Object result = operation.handleResponse(params, status, response);
            assertSame(opResult, result);
        }
        then(operation).should().processResponseStatus(response);
        then(operation).should().processResponseStatusException(response, statusException);
        then(operation).should(never()).parseResponse(any(), any(), any(), any());
        then(operation).should(never()).processResponse(any(), any(), any(), any());
        then(response).should().getBody();
        if (responseBody) {
            then(body).should().discard();
        }
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#handleResponse(Object, Object, HttpResponse)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testHandleResponse_parseError()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final HttpResponse response = mock(HttpResponse.class);
        willDoNothing().given(operation).processResponseStatus(response);
        final HttpResponseBody body = mock(HttpResponseBody.class);
        given(response.getBody()).willReturn(body);
        final HttpResponseHandlingException exception = new HttpResponseHandlingException();
        given(operation.parseResponse(params, status, response, body)).willThrow(exception);
        final HttpClientException result = assertThrows(
                HttpClientException.class,
                () -> operation.handleResponse(params, status, response));
        assertSame(exception, result);
        then(operation).should().processResponseStatus(response);
        then(operation).should().parseResponse(params, status, response, body);
        then(operation).should(never()).processResponse(any(), any(), any(), any());
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#handleResponse(Object, Object, HttpResponse)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testHandleResponse_processError(
            final boolean responseBody)
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final HttpResponse response = mock(HttpResponse.class);
        willDoNothing().given(operation).processResponseStatus(response);
        final HttpResponseBody body;
        final Object entity;
        if (responseBody) {
            body = mock(HttpResponseBody.class);
            entity = new Object();
            given(operation.parseResponse(params, status, response, body)).willReturn(entity);
        } else {
            body = null;
            entity = null;
        }
        given(response.getBody()).willReturn(body);
        final HttpResponseHandlingException exception = new HttpResponseHandlingException();
        given(operation.processResponse(params, status, entity, response)).willThrow(exception);
        final HttpClientException result = assertThrows(
                HttpClientException.class,
                () -> operation.handleResponse(params, status, response));
        assertSame(exception, result);
        then(operation).should().processResponseStatus(response);
        if (responseBody) {
            then(operation).should().parseResponse(params, status, response, body);
        } else {
            then(operation).should(never()).parseResponse(any(), any(), any(), any());
        }
        then(operation).should().processResponse(params, status, entity, response);
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#execute(Object, Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecute()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final StatedHttpServiceClient<Object> client = createMockClient();
        final HttpClientEngine engine = mock(HttpClientEngine.class);
        given(client.getEngine()).willReturn(engine);
        final HttpRequest request = mock(HttpRequest.class);
        final URI operationURI = URI.create("/mockURI");
        given(operation.getRequestURI(params, status)).willReturn(operationURI);
        final URI requestURI = URI.create("http://example.org/mockURI");
        willReturn(requestURI).given(operation).resolveRequestURI(operationURI, client);
        final String operationMethod = "Mock method";
        given(operation.getRequestMethod()).willReturn(operationMethod);
        @SuppressWarnings("unchecked")
        final OperationResponseHandler<Object> handler = mock(OperationResponseHandler.class);
        given(operation.createResponseHandler(params, status)).willReturn(handler);
        final Object opResult = new Object();
        given(handler.getResult()).willReturn(opResult);
        final CompletableFuture<Void> engineFuture = new CompletableFuture<>();
        given(engine.executeHttpRequest(eq(requestURI), eq(operationMethod), any(), any())).willReturn(engineFuture);
        
        final CompletableFuture<Object> futureResult = operation.execute(params, status, client);
        assertNotNull(futureResult);
        then(operation).should().execute(params, status, client);
        then(operation).should().getRequestURI(params, status);
        then(operation).should().resolveRequestURI(operationURI, client);
        then(operation).should().getRequestMethod();
        then(operation).should().createResponseHandler(params, status);
        then(operation).shouldHaveNoMoreInteractions();
        then(engine).should().executeHttpRequest(
                eq(requestURI),
                eq(operationMethod),
                requestCustomizerCaptor.capture(),
                eq(handler));
        then(engine).shouldHaveNoMoreInteractions();
        final HttpRequestCustomizer customizer = requestCustomizerCaptor.getValue();
        customizer.customizeRequest(request);
        then(operation).should().prepareRequest(params, status, request);
        then(operation).shouldHaveNoMoreInteractions();
        then(handler).shouldHaveNoInteractions();
        engineFuture.complete(null);
        final Object result = futureResult.get();
        assertSame(opResult, result);
        then(handler).should().getResult();
        then(handler).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#execute(Object, Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecute_operationUriError()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final StatedHttpServiceClient<Object> client = createMockClient();
        final HttpClientEngine engine = mock(HttpClientEngine.class);
        given(client.getEngine()).willReturn(engine);
        final HttpClientException exception = new HttpClientException();
        given(operation.getRequestURI(params, status)).willThrow(exception);
        final String operationMethod = "Mock method";
        given(operation.getRequestMethod()).willReturn(operationMethod);
        @SuppressWarnings("unchecked")
        final OperationResponseHandler<Object> handler = mock(OperationResponseHandler.class);
        given(operation.createResponseHandler(params, status)).willReturn(handler);
        
        final CompletableFuture<Object> futureResult = operation.execute(params, status, client);
        assertNotNull(futureResult);
        final ExecutionException result = assertThrows(ExecutionException.class, futureResult::get);
        assertSame(exception, HttpClientException.unwrapFutureException(result));
        then(operation).should().execute(params, status, client);
        then(operation).should().getRequestURI(params, status);
        then(operation).should(never()).resolveRequestURI(any(), any());
        then(operation).should(atMostOnce()).getRequestMethod();
        then(operation).should(atMostOnce()).createResponseHandler(params, status);
        then(operation).shouldHaveNoMoreInteractions();
        then(engine).shouldHaveNoInteractions();
        then(handler).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#execute(Object, Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecute_resolveRequestError()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final StatedHttpServiceClient<Object> client = createMockClient();
        final HttpClientEngine engine = mock(HttpClientEngine.class);
        given(client.getEngine()).willReturn(engine);
        final URI operationURI = URI.create("/mockURI");
        given(operation.getRequestURI(params, status)).willReturn(operationURI);
        final HttpClientException exception = new HttpClientException();
        willThrow(exception).given(operation).resolveRequestURI(operationURI, client);
        final String operationMethod = "Mock method";
        given(operation.getRequestMethod()).willReturn(operationMethod);
        @SuppressWarnings("unchecked")
        final OperationResponseHandler<Object> handler = mock(OperationResponseHandler.class);
        given(operation.createResponseHandler(params, status)).willReturn(handler);
        
        final CompletableFuture<Object> futureResult = operation.execute(params, status, client);
        assertNotNull(futureResult);
        final ExecutionException result = assertThrows(ExecutionException.class, futureResult::get);
        assertSame(exception, HttpClientException.unwrapFutureException(result));
        then(operation).should().execute(params, status, client);
        then(operation).should().getRequestURI(params, status);
        then(operation).should().resolveRequestURI(operationURI, client);
        then(operation).should(atMostOnce()).getRequestMethod();
        then(operation).should(atMostOnce()).createResponseHandler(params, status);
        then(operation).shouldHaveNoMoreInteractions();
        then(engine).shouldHaveNoInteractions();
        then(handler).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#execute(Object, Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecute_createHandlerError()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final StatedHttpServiceClient<Object> client = createMockClient();
        final HttpClientEngine engine = mock(HttpClientEngine.class);
        given(client.getEngine()).willReturn(engine);
        final URI operationURI = URI.create("/mockURI");
        given(operation.getRequestURI(params, status)).willReturn(operationURI);
        final URI requestURI = URI.create("http://example.org/mockURI");
        willReturn(requestURI).given(operation).resolveRequestURI(operationURI, client);
        final String operationMethod = "Mock method";
        given(operation.getRequestMethod()).willReturn(operationMethod);
        final HttpClientException exception = new HttpClientException();
        given(operation.createResponseHandler(params, status)).willThrow(exception);
        final CompletableFuture<Void> engineFuture = new CompletableFuture<>();
        given(engine.executeHttpRequest(eq(requestURI), eq(operationMethod), any(), any())).willReturn(engineFuture);
        
        final CompletableFuture<Object> futureResult = operation.execute(params, status, client);
        assertNotNull(futureResult);
        final ExecutionException result = assertThrows(ExecutionException.class, futureResult::get);
        assertSame(exception, HttpClientException.unwrapFutureException(result));
        then(operation).should().execute(params, status, client);
        then(operation).should(atMostOnce()).getRequestURI(params, status);
        then(operation).should(atMostOnce()).resolveRequestURI(operationURI, client);
        then(operation).should(atMostOnce()).getRequestMethod();
        then(operation).should().createResponseHandler(params, status);
        then(operation).shouldHaveNoMoreInteractions();
        then(engine).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#execute(Object, Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecute_engineError()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final StatedHttpServiceClient<Object> client = createMockClient();
        final HttpClientEngine engine = mock(HttpClientEngine.class);
        given(client.getEngine()).willReturn(engine);
        final URI operationURI = URI.create("/mockURI");
        given(operation.getRequestURI(params, status)).willReturn(operationURI);
        final URI requestURI = URI.create("http://example.org/mockURI");
        willReturn(requestURI).given(operation).resolveRequestURI(operationURI, client);
        final String operationMethod = "Mock method";
        given(operation.getRequestMethod()).willReturn(operationMethod);
        @SuppressWarnings("unchecked")
        final OperationResponseHandler<Object> handler = mock(OperationResponseHandler.class);
        given(operation.createResponseHandler(params, status)).willReturn(handler);
        final HttpClientException exception = new HttpClientException();
        given(engine.executeHttpRequest(eq(requestURI), eq(operationMethod), any(), any())).willThrow(exception);
        
        final CompletableFuture<Object> futureResult = operation.execute(params, status, client);
        assertNotNull(futureResult);
        final ExecutionException result = assertThrows(ExecutionException.class, futureResult::get);
        assertSame(exception, HttpClientException.unwrapFutureException(result));
        then(operation).should().execute(params, status, client);
        then(operation).should().getRequestURI(params, status);
        then(operation).should().resolveRequestURI(operationURI, client);
        then(operation).should().getRequestMethod();
        then(operation).should().createResponseHandler(params, status);
        then(operation).shouldHaveNoMoreInteractions();
        then(engine).should().executeHttpRequest(
                eq(requestURI),
                eq(operationMethod),
                requestCustomizerCaptor.capture(),
                eq(handler));
        then(engine).shouldHaveNoMoreInteractions();
        then(handler).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#execute(Object, Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecute_engineFutureError()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final StatedHttpServiceClient<Object> client = createMockClient();
        final HttpClientEngine engine = mock(HttpClientEngine.class);
        given(client.getEngine()).willReturn(engine);
        final HttpRequest request = mock(HttpRequest.class);
        final URI operationURI = URI.create("/mockURI");
        given(operation.getRequestURI(params, status)).willReturn(operationURI);
        final URI requestURI = URI.create("http://example.org/mockURI");
        willReturn(requestURI).given(operation).resolveRequestURI(operationURI, client);
        final String operationMethod = "Mock method";
        given(operation.getRequestMethod()).willReturn(operationMethod);
        @SuppressWarnings("unchecked")
        final OperationResponseHandler<Object> handler = mock(OperationResponseHandler.class);
        given(operation.createResponseHandler(params, status)).willReturn(handler);
        final CompletableFuture<Void> engineFuture = new CompletableFuture<>();
        given(engine.executeHttpRequest(eq(requestURI), eq(operationMethod), any(), any())).willReturn(engineFuture);
        
        final CompletableFuture<Object> futureResult = operation.execute(params, status, client);
        assertNotNull(futureResult);
        then(operation).should().execute(params, status, client);
        then(operation).should().getRequestURI(params, status);
        then(operation).should().resolveRequestURI(operationURI, client);
        then(operation).should().getRequestMethod();
        then(operation).should().createResponseHandler(params, status);
        then(operation).shouldHaveNoMoreInteractions();
        then(engine).should().executeHttpRequest(
                eq(requestURI),
                eq(operationMethod),
                requestCustomizerCaptor.capture(),
                eq(handler));
        then(engine).shouldHaveNoMoreInteractions();
        final HttpRequestCustomizer customizer = requestCustomizerCaptor.getValue();
        customizer.customizeRequest(request);
        then(operation).should().execute(params, status, client);
        then(operation).should().prepareRequest(params, status, request);
        then(operation).shouldHaveNoMoreInteractions();
        then(handler).shouldHaveNoInteractions();
        final HttpClientException exception = new HttpClientException();
        engineFuture.completeExceptionally(exception);
        final ExecutionException result = assertThrows(ExecutionException.class, futureResult::get);
        assertSame(exception, HttpClientException.unwrapFutureException(result));
        then(handler).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#execute(Object, Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecute_prepareRequestError()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final StatedHttpServiceClient<Object> client = createMockClient();
        final HttpClientEngine engine = mock(HttpClientEngine.class);
        given(client.getEngine()).willReturn(engine);
        final HttpRequest request = mock(HttpRequest.class);
        final URI operationURI = URI.create("/mockURI");
        given(operation.getRequestURI(params, status)).willReturn(operationURI);
        final URI requestURI = URI.create("http://example.org/mockURI");
        willReturn(requestURI).given(operation).resolveRequestURI(operationURI, client);
        final String operationMethod = "Mock method";
        given(operation.getRequestMethod()).willReturn(operationMethod);
        @SuppressWarnings("unchecked")
        final OperationResponseHandler<Object> handler = mock(OperationResponseHandler.class);
        given(operation.createResponseHandler(params, status)).willReturn(handler);
        final HttpClientException exception = new HttpClientException();
        willThrow(exception).given(operation).prepareRequest(params, status, request);
        final CompletableFuture<Void> engineFuture = new CompletableFuture<>();
        given(engine.executeHttpRequest(eq(requestURI), eq(operationMethod), any(), any())).willReturn(engineFuture);
        
        final CompletableFuture<Object> futureResult = operation.execute(params, status, client);
        assertNotNull(futureResult);
        then(operation).should().execute(params, status, client);
        then(operation).should().getRequestURI(params, status);
        then(operation).should().resolveRequestURI(operationURI, client);
        then(operation).should().getRequestMethod();
        then(operation).should().createResponseHandler(params, status);
        then(operation).shouldHaveNoMoreInteractions();
        then(engine).should().executeHttpRequest(
                eq(requestURI),
                eq(operationMethod),
                requestCustomizerCaptor.capture(),
                eq(handler));
        then(engine).shouldHaveNoMoreInteractions();
        final HttpRequestCustomizer customizer = requestCustomizerCaptor.getValue();
        final HttpClientException customizerError = assertThrows(
                HttpClientException.class,
                () -> customizer.customizeRequest(request));
        then(operation).should().execute(params, status, client);
        then(operation).should().prepareRequest(params, status, request);
        then(operation).shouldHaveNoMoreInteractions();
        then(handler).shouldHaveNoInteractions();
        engineFuture.completeExceptionally(customizerError);
        final ExecutionException result = assertThrows(ExecutionException.class, futureResult::get);
        assertSame(exception, HttpClientException.unwrapFutureException(result));
        then(handler).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#execute(Object, Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecute_responseHandlingException()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final StatedHttpServiceClient<Object> client = createMockClient();
        final HttpClientEngine engine = mock(HttpClientEngine.class);
        given(client.getEngine()).willReturn(engine);
        final HttpRequest request = mock(HttpRequest.class);
        final URI operationURI = URI.create("/mockURI");
        given(operation.getRequestURI(params, status)).willReturn(operationURI);
        final URI requestURI = URI.create("http://example.org/mockURI");
        willReturn(requestURI).given(operation).resolveRequestURI(operationURI, client);
        final String operationMethod = "Mock method";
        given(operation.getRequestMethod()).willReturn(operationMethod);
        @SuppressWarnings("unchecked")
        final OperationResponseHandler<Object> handler = mock(OperationResponseHandler.class);
        given(operation.createResponseHandler(params, status)).willReturn(handler);
        final HttpClientException exception = new HttpClientException();
        given(handler.getResult()).willThrow(exception);
        final CompletableFuture<Void> engineFuture = new CompletableFuture<>();
        given(engine.executeHttpRequest(eq(requestURI), eq(operationMethod), any(), any())).willReturn(engineFuture);
        
        final CompletableFuture<Object> futureResult = operation.execute(params, status, client);
        assertNotNull(futureResult);
        then(operation).should().execute(params, status, client);
        then(operation).should().getRequestURI(params, status);
        then(operation).should().resolveRequestURI(operationURI, client);
        then(operation).should().getRequestMethod();
        then(operation).should().createResponseHandler(params, status);
        then(operation).shouldHaveNoMoreInteractions();
        then(engine).should().executeHttpRequest(
                eq(requestURI),
                eq(operationMethod),
                requestCustomizerCaptor.capture(),
                eq(handler));
        then(engine).shouldHaveNoMoreInteractions();
        final HttpRequestCustomizer customizer = requestCustomizerCaptor.getValue();
        customizer.customizeRequest(request);
        then(operation).should().execute(params, status, client);
        then(operation).should().prepareRequest(params, status, request);
        then(operation).shouldHaveNoMoreInteractions();
        then(handler).shouldHaveNoInteractions();
        engineFuture.complete(null);
        final ExecutionException result = assertThrows(ExecutionException.class, futureResult::get);
        assertSame(exception, HttpClientException.unwrapFutureException(result));
        then(handler).should().getResult();
        then(handler).shouldHaveNoMoreInteractions();
    }
}
