/**
 * 
 */
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

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for {@code AbstractHttpServiceOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractHttpServiceOperation
 */
@Tag("ut")
@SuppressWarnings({ "rawtypes", "unchecked" })
class AbstractHttpServiceOperationTest {

    /**
     * Creates the operation to be tested.
     * 
     * @return The operation created
     */
    protected AbstractHttpServiceOperation createOperation() {
        return spy(AbstractHttpServiceOperation.class);
    }

    protected HttpServiceClient createMockClient() {
        return mock(HttpServiceClient.class);
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#resolveRequestURI(URI, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testResolveRequestURI()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final URI requestURI = URI.create("op/path?q=test#frag");
        final URI clientBaseURI = URI.create("http://example.org/base/path/");
        final URI expectedURI = URI.create("http://example.org/base/path/op/path?q=test#frag");
        final HttpServiceClient client = createMockClient();
        given(client.getBaseURI()).willReturn(clientBaseURI);
        final URI result = operation.resolveRequestURI(requestURI, client);
        assertNotNull(result);
        assertEquals(expectedURI, result);
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#resolveRequestURI(URI, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testResolveRequestURIAbsolute()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final URI requestURI = URI.create("/op/path?q=test#frag");
        final URI clientBaseURI = URI.create("http://example.org/base/path");
        final URI expectedURI = URI.create("http://example.org/op/path?q=test#frag");
        final HttpServiceClient client = createMockClient();
        given(client.getBaseURI()).willReturn(clientBaseURI);
        final URI result = operation.resolveRequestURI(requestURI, client);
        assertNotNull(result);
        assertEquals(expectedURI, result);
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#resolveRequestURI(URI, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testResolveRequestURIAbsoluteHost()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final URI requestURI = URI.create("http://other.example.org/op/path?q=test#frag");
        final URI clientBaseURI = URI.create("http://example.org/base/path");
        final URI expectedURI = URI.create("http://other.example.org/op/path?q=test#frag");
        final HttpServiceClient client = createMockClient();
        given(client.getBaseURI()).willReturn(clientBaseURI);
        final URI result = operation.resolveRequestURI(requestURI, client);
        assertNotNull(result);
        assertEquals(expectedURI, result);
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#getHttpContext(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetHttpContext()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final HttpContext result = operation.getHttpContext(params, client);
        assertNull(result);
        then(client).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#executeHttpRequest(Object, HttpRequest, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteHttpRequest()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final HttpServiceClient client = createMockClient();
        final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        final HttpHost httpHost = new HttpHost("example.org");
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final HttpContext httpContext = mock(HttpContext.class);
        final CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        final Object params = new Object();
        final Object expectedResult = new Object();
        willReturn(httpContext).given(operation).getHttpContext(params, client);
        given(client.getHost()).willReturn(httpHost);
        given(client.getClient()).willReturn(httpClient);
        given(httpClient.execute(httpHost, httpRequest, httpContext)).willReturn(httpResponse);
        willReturn(expectedResult).given(operation).processHttpResponse(
                params,
                client,
                httpRequest,
                httpResponse);
        final Object result = operation.executeHttpRequest(params, httpRequest, client);
        assertNotNull(result);
        assertSame(expectedResult, result);
        then(operation).should(times(1)).getHttpContext(params, client);
        then(client).should(atLeastOnce()).getClient();
        then(client).should(atLeastOnce()).getHost();
        then(httpClient).should(times(1)).execute(httpHost, httpRequest, httpContext);
        then(operation).should(times(1)).processHttpResponse(
                params,
                client,
                httpRequest,
                httpResponse);
        then(httpResponse).should(times(1)).close();
        then(httpClient).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#executeHttpRequest(Object, HttpRequest, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteHttpRequestContextCreationError()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final HttpServiceClient client = createMockClient();
        final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        final HttpHost httpHost = new HttpHost("example.org");
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final Object params = new Object();
        final HttpClientException expectedException = new HttpClientException();
        willThrow(expectedException).given(operation).getHttpContext(params, client);
        given(client.getHost()).willReturn(httpHost);
        given(client.getClient()).willReturn(httpClient);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.executeHttpRequest(params, httpRequest, client);
        });
        assertNotNull(result);
        assertSame(expectedException, result);
        then(operation).should(times(1)).executeHttpRequest(params, httpRequest, client);
        then(operation).should(times(1)).getHttpContext(params, client);
        then(httpClient).shouldHaveNoInteractions();
        then(operation).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#executeHttpRequest(Object, HttpRequest, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteHttpRequestIOException()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final HttpServiceClient client = createMockClient();
        final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        final HttpHost httpHost = new HttpHost("example.org");
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final HttpContext httpContext = mock(HttpContext.class);
        final Object params = new Object();
        final IOException httpClientException = new IOException();
        final HttpClientException expectedException = new HttpClientException();
        willReturn(httpContext).given(operation).getHttpContext(params, client);
        given(client.getHost()).willReturn(httpHost);
        given(client.getClient()).willReturn(httpClient);
        willThrow(httpClientException).given(httpClient).execute(httpHost, httpRequest, httpContext);
        willReturn(expectedException).given(operation).processException(
                params,
                client,
                httpRequest,
                null,
                httpClientException);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.executeHttpRequest(params, httpRequest, client);
        });
        assertNotNull(result);
        assertSame(expectedException, result);
        then(operation).should(times(1)).executeHttpRequest(params, httpRequest, client);
        then(operation).should(times(1)).getHttpContext(params, client);
        then(client).should(atLeastOnce()).getClient();
        then(client).should(atLeastOnce()).getHost();
        then(httpClient).should(times(1)).execute(httpHost, httpRequest, httpContext);
        then(operation).should(times(1)).processException(
                params,
                client,
                httpRequest,
                null,
                httpClientException);
        then(operation).shouldHaveNoMoreInteractions();
        then(httpClient).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#getResponseHandler()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetResponseHandler()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final ResponseHandler<?> handler = mock(ResponseHandler.class);
        willReturn(handler).given(operation).createResponseHandler();
        final ResponseHandler<?> result = operation.getResponseHandler();
        assertNotNull(result);
        assertSame(handler, result);
        then(operation).should(times(1)).createResponseHandler();
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#getResponseHandler()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetResponseHandlerCreationFailed()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final HttpClientException expectedException = new HttpClientException();
        willThrow(expectedException).given(operation).createResponseHandler();
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.getResponseHandler();
        });
        assertNotNull(result);
        assertSame(expectedException, result);
        then(operation).should(times(1)).createResponseHandler();
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#getResponseHandler()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetResponseHandlerMultipleTimes()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final ResponseHandler<?> handler = mock(ResponseHandler.class);
        willReturn(handler).given(operation).createResponseHandler();
        final ResponseHandler<?> firstResult = operation.getResponseHandler();
        assertNotNull(firstResult);
        assertSame(handler, firstResult);
        final ResponseHandler<?> secondResult = operation.getResponseHandler();
        assertNotNull(secondResult);
        assertSame(handler, secondResult);
        final ResponseHandler<?> thirdResult = operation.getResponseHandler();
        assertNotNull(thirdResult);
        assertSame(handler, thirdResult);
        final ResponseHandler<?> fourthResult = operation.getResponseHandler();
        assertNotNull(fourthResult);
        assertSame(handler, fourthResult);
        then(operation).should(times(1)).createResponseHandler();
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#extractResponseEntity(Object, HttpServiceClient, HttpRequest, HttpResponse)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExtractResponseEntity()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final ResponseHandler<Object> handler = mock(ResponseHandler.class);
        final Object expectedResult = new Object();
        willReturn(handler).given(operation).getResponseHandler();
        given(handler.handleResponse(httpResponse)).willReturn(expectedResult);
        final Object result = operation.extractResponseEntity(
                params, client, httpRequest, httpResponse);
        assertNotNull(result);
        assertSame(expectedResult, result);
        then(operation).should(atLeastOnce()).getResponseHandler();
        then(handler).should(times(1)).handleResponse(httpResponse);
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#extractResponseEntity(Object, HttpServiceClient, HttpRequest, HttpResponse)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExtractResponseEntityHttpResponseException()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final ResponseHandler<Object> handler = mock(ResponseHandler.class);
        final HttpResponseException handlerException = new HttpResponseException(
                -1, "Mock exception");
        final HttpClientException expectedException = new HttpClientException();
        willReturn(handler).given(operation).getResponseHandler();
        given(handler.handleResponse(httpResponse)).willThrow(handlerException);
        willThrow(expectedException).given(operation).processHttpResponseException(
                params,
                client,
                httpRequest,
                httpResponse,
                handlerException);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.extractResponseEntity(
                    params, client, httpRequest, httpResponse);
        });
        assertNotNull(result);
        assertSame(expectedException, result);
        then(operation).should(atLeastOnce()).getResponseHandler();
        then(handler).should(times(1)).handleResponse(httpResponse);
        then(operation).should(times(1)).processHttpResponseException(
                params,
                client,
                httpRequest,
                httpResponse,
                handlerException);
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#extractResponseEntity(Object, HttpServiceClient, HttpRequest, HttpResponse)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExtractResponseEntityHttpResponseExceptionFallback()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final ResponseHandler<Object> handler = mock(ResponseHandler.class);
        final HttpResponseException handlerException = new HttpResponseException(
                -1, "Mock exception");
        final Object expectedResult = new Object();
        willReturn(handler).given(operation).getResponseHandler();
        given(handler.handleResponse(httpResponse)).willThrow(handlerException);
        willReturn(expectedResult).given(operation).processHttpResponseException(
                params,
                client,
                httpRequest,
                httpResponse,
                handlerException);
        final Object result = operation.extractResponseEntity(
                params, client, httpRequest, httpResponse);
        assertNotNull(result);
        assertSame(expectedResult, result);
        then(operation).should(atLeastOnce()).getResponseHandler();
        then(handler).should(times(1)).handleResponse(httpResponse);
        then(operation).should(times(1)).processHttpResponseException(
                params,
                client,
                httpRequest,
                httpResponse,
                handlerException);
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#extractResponseEntity(Object, HttpServiceClient, HttpRequest, HttpResponse)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExtractResponseEntityIOException()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final ResponseHandler<Object> handler = mock(ResponseHandler.class);
        final IOException handlerException = new IOException();
        final HttpClientException expectedException = new HttpClientException();
        willReturn(handler).given(operation).getResponseHandler();
        given(handler.handleResponse(httpResponse)).willThrow(handlerException);
        willReturn(expectedException).given(operation).processException(
                params,
                client,
                httpRequest,
                httpResponse,
                handlerException);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.extractResponseEntity(
                    params, client, httpRequest, httpResponse);
        });
        assertNotNull(result);
        assertSame(expectedException, result);
        then(operation).should(atLeastOnce()).getResponseHandler();
        then(handler).should(times(1)).handleResponse(httpResponse);
        then(operation).should(times(1)).processException(
                params,
                client,
                httpRequest,
                httpResponse,
                handlerException);
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#processHttpResponse(Object, HttpServiceClient, HttpRequest, HttpResponse)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProcessHttpResponse()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final Object entity = new Object();
        final Object expectedResult = new Object();
        willReturn(entity).given(operation).extractResponseEntity(
                params, client, httpRequest, httpResponse);
        willReturn(expectedResult).given(operation).processResponseEntity(
                params, client, httpRequest, httpResponse, entity);
        final Object result = operation.processHttpResponse(
                params, client, httpRequest, httpResponse);
        assertNotNull(result);
        assertSame(expectedResult, result);
        then(operation).should(times(1)).extractResponseEntity(
                params, client, httpRequest, httpResponse);
        then(operation).should(times(1)).processResponseEntity(
                params, client, httpRequest, httpResponse, entity);
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#processHttpResponse(Object, HttpServiceClient, HttpRequest, HttpResponse)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProcessHttpResponseExtractFail()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final HttpClientException expectedException = new HttpClientException();
        willThrow(expectedException).given(operation).extractResponseEntity(
                params, client, httpRequest, httpResponse);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.processHttpResponse(params, client, httpRequest, httpResponse);
        });
        assertNotNull(result);
        assertSame(expectedException, result);
        then(operation).should(times(1)).extractResponseEntity(
                params, client, httpRequest, httpResponse);
        then(operation).should(times(0)).processResponseEntity(
                any(), any(), any(), any(), any());
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#processHttpResponse(Object, HttpServiceClient, HttpRequest, HttpResponse)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProcessHttpResponseProcessFail()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final Object entity = new Object();
        final HttpClientException expectedException = new HttpClientException();
        willReturn(entity).given(operation).extractResponseEntity(
                params, client, httpRequest, httpResponse);
        willThrow(expectedException).given(operation).processResponseEntity(
                params, client, httpRequest, httpResponse, entity);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.processHttpResponse(params, client, httpRequest, httpResponse);
        });
        assertNotNull(result);
        assertSame(expectedException, result);
        then(operation).should(times(1)).extractResponseEntity(
                params, client, httpRequest, httpResponse);
        then(operation).should(times(1)).processResponseEntity(
                params, client, httpRequest, httpResponse, entity);
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#processException(Object, HttpServiceClient, HttpRequest, HttpResponse, Exception)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProcessException()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final Exception exception = new Exception();
        final HttpClientException result = operation.processException(
                params, client, httpRequest, httpResponse, exception);
        assertNotNull(result);
        assertSame(exception, result.getCause());
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#processException(Object, HttpServiceClient, HttpRequest, HttpResponse, Exception)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProcessExceptionHttpClientException()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final HttpClientException exception = new HttpClientException();
        final HttpClientException result = operation.processException(
                params, client, httpRequest, httpResponse, exception);
        assertNotNull(result);
        assertSame(exception, result);
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#processHttpResponseException(Object, HttpServiceClient, HttpRequest, HttpResponse, HttpResponseException)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProcessHttpResponseException()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final HttpResponseException exception = new HttpResponseException(
                -1, "Mock exception");
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.processHttpResponseException(
                    params, client, httpRequest, httpResponse, exception);
        });
        assertNotNull(result);
        assertSame(exception, result.getCause());
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#processHttpResponseException(Object, HttpServiceClient, HttpRequest, HttpResponse, HttpResponseException)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProcessHttpResponseExceptionUnauthorized()
    throws Throwable {
        final AbstractHttpServiceOperation operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final HttpResponseException exception = new HttpResponseException(
                HttpStatus.SC_UNAUTHORIZED, "Mock exception");
        final AuthenticationRequiredException result = assertThrows(AuthenticationRequiredException.class, () -> {
            operation.processHttpResponseException(
                    params, client, httpRequest, httpResponse, exception);
        });
        assertNotNull(result);
        assertSame(exception, result.getCause());
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#getLogger()}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testLogger()
    throws Throwable {
        final AbstractHttpServiceOperation<?, ?, ?, ?> operation =
                createOperation();
        final Logger logger = operation.getLogger();
        assertNotNull(logger);
        assertSame(LoggerFactory.getLogger(operation.getClass()), logger);
    }
}
