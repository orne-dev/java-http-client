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
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHeader;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AbstractStatusIndependentOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractStatusIndependentOperation
 */
@Tag("ut")
class AbstractStatusIndependentOperationTest
extends AbstractHttpServiceOperationTest {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractStatusIndependentOperation<Object, Object, Object> createOperation() {
        return spy(AbstractStatusIndependentOperation.class);
    }

    /**
     * Creates a mock {@code HttpRequest} valid for tested operation.
     * 
     * @return The created mock {@code HttpRequest}
     */
    protected HttpRequest createMockHttpRequest() {
        return mock(HttpRequest.class);
    }

    /**
     * Test for {@link AbstractStatusIndependentOperation#execute(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecute()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final URI operationURI = URI.create("/mockURI");
        final URI requestURI = URI.create("http://example.org/mockURI");
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"),
                new BasicHeader("anotherHeader", "headerValue"));
        final HttpRequest httpRequest = createMockHttpRequest();
        final Object mockResult = new Object();
        willReturn(requestHeaders).given(operation).createHeaders(params);
        willReturn(operationURI).given(operation).getRequestURI(params);
        willReturn(requestURI).given(operation).resolveRequestURI(operationURI, client);
        willReturn(httpRequest).given(operation).createRequest(requestURI, params);
        willReturn(mockResult).given(operation).executeHttpRequest(params, httpRequest, client);
        final Object result = operation.execute(params, client);
        assertNotNull(result);
        assertSame(mockResult, result);
        then(operation).should(times(1)).getRequestURI(params);
        then(operation).should(times(1)).resolveRequestURI(operationURI, client);
        then(operation).should(times(1)).createRequest(requestURI, params);
        then(operation).should(times(1)).createHeaders(params);
        then(httpRequest).should(times(1)).addHeader(requestHeaders.get(0));
        then(httpRequest).should(times(1)).addHeader(requestHeaders.get(1));
        then(operation).should(times(1)).executeHttpRequest(params, httpRequest, client);
    }

    /**
     * Test for {@link AbstractStatusIndependentOperation#execute(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteGetRequestURIFails()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"),
                new BasicHeader("anotherHeader", "headerValue"));
        final HttpClientException mockException = new HttpClientException();
        willReturn(requestHeaders).given(operation).createHeaders(params);
        willThrow(mockException).given(operation).getRequestURI(params);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.execute(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).getRequestURI(params);
        then(operation).should(never()).resolveRequestURI(any(), any());
        then(operation).should(never()).createRequest(any(), any());
        then(operation).should(never()).executeHttpRequest(any(), any(), any());
    }

    /**
     * Test for {@link AbstractStatusIndependentOperation#execute(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteResolveRequestURIFails()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final URI operationURI = URI.create("/mockURI");
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"),
                new BasicHeader("anotherHeader", "headerValue"));
        final HttpClientException mockException = new HttpClientException();
        willReturn(requestHeaders).given(operation).createHeaders(params);
        willReturn(operationURI).given(operation).getRequestURI(params);
        willThrow(mockException).given(operation).resolveRequestURI(operationURI, client);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.execute(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).getRequestURI(params);
        then(operation).should(times(1)).resolveRequestURI(operationURI, client);
        then(operation).should(never()).createRequest(any(), any());
        then(operation).should(never()).executeHttpRequest(any(), any(), any());
    }

    /**
     * Test for {@link AbstractStatusIndependentOperation#execute(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteCreateRequestFails()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final URI operationURI = URI.create("/mockURI");
        final URI requestURI = URI.create("http://example.org/mockURI");
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"),
                new BasicHeader("anotherHeader", "headerValue"));
        final HttpClientException mockException = new HttpClientException();
        willReturn(operationURI).given(operation).getRequestURI(params);
        willReturn(requestURI).given(operation).resolveRequestURI(operationURI, client);
        willReturn(requestHeaders).given(operation).createHeaders(params);
        willThrow(mockException).given(operation).createRequest(requestURI, params);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.execute(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).getRequestURI(params);
        then(operation).should(times(1)).resolveRequestURI(operationURI, client);
        then(operation).should(times(1)).createRequest(requestURI, params);
        then(operation).should(never()).executeHttpRequest(any(), any(), any());
    }

    /**
     * Test for {@link AbstractStatusIndependentOperation#execute(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteCreateHeadersFails()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final URI operationURI = URI.create("/mockURI");
        final URI requestURI = URI.create("http://example.org/mockURI");
        final HttpRequest httpRequest = createMockHttpRequest();
        final HttpClientException mockException = new HttpClientException();
        willReturn(operationURI).given(operation).getRequestURI(params);
        willReturn(requestURI).given(operation).resolveRequestURI(operationURI, client);
        willReturn(httpRequest).given(operation).createRequest(requestURI, params);
        willThrow(mockException).given(operation).createHeaders(params);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.execute(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).createHeaders(params);
        then(operation).should(never()).executeHttpRequest(any(), any(), any());
    }

    /**
     * Test for {@link AbstractStatusIndependentOperation#execute(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testExecuteExecuteRequestFails()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final HttpServiceClient client = createMockClient();
        final URI operationURI = URI.create("/mockURI");
        final URI requestURI = URI.create("http://example.org/mockURI");
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"),
                new BasicHeader("anotherHeader", "headerValue"));
        final HttpRequest httpRequest = createMockHttpRequest();
        final HttpClientException mockException = new HttpClientException();
        willReturn(requestHeaders).given(operation).createHeaders(params);
        willReturn(operationURI).given(operation).getRequestURI(params);
        willReturn(requestURI).given(operation).resolveRequestURI(operationURI, client);
        willReturn(httpRequest).given(operation).createRequest(requestURI, params);
        willThrow(mockException).given(operation).executeHttpRequest(params, httpRequest, client);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.execute(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).getRequestURI(params);
        then(operation).should(times(1)).resolveRequestURI(operationURI, client);
        then(operation).should(times(1)).createRequest(requestURI, params);
        then(operation).should(times(1)).createHeaders(params);
        then(httpRequest).should(times(1)).addHeader(requestHeaders.get(0));
        then(httpRequest).should(times(1)).addHeader(requestHeaders.get(1));
        then(operation).should(times(1)).executeHttpRequest(params, httpRequest, client);
    }

    /**
     * Test for {@link AbstractStatusIndependentOperation#createHeaders(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateHeaders()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final List<Header> result = operation.createHeaders(params);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
