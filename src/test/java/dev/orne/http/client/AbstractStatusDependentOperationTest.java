package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.message.BasicHeader;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AbstractStatusDependentOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractStatusDependentOperation
 */
@Tag("ut")
public class AbstractStatusDependentOperationTest
extends AbstractHttpServiceOperationTest {

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractStatusDependentOperation<Object, Object, Object, Object> createOperation() {
        return new TestStatusIndependentOperation();
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
     * Test for {@link AbstractStatusDependentOperation#execute(Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testExecute()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        @SuppressWarnings("unchecked")
        final StatedHttpServiceClient<Object> client = mock(StatedHttpServiceClient.class);
        final Object status = new Object();
        final URI operationURI = URI.create("/mockURI");
        final URI requestURI = URI.create("http://example.org/mockURI");
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"),
                new BasicHeader("anotherHeader", "headerValue"));
        final HttpRequest httpRequest = createMockHttpRequest();
        final Object mockResult = new Object();
        willReturn(status).given(client).ensureInitialized();
        willReturn(requestHeaders).given(operation).createHeaders(params, status);
        willReturn(operationURI).given(operation).getRequestURI(params, status);
        willReturn(requestURI).given(operation).resolveRequestURI(operationURI, client);
        willReturn(httpRequest).given(operation).createRequest(requestURI, params, status);
        willReturn(mockResult).given(operation).executeHttpRequest(params, httpRequest, client);
        final Object result = operation.execute(params, client);
        assertNotNull(result);
        assertSame(mockResult, result);
        then(client).should(atLeastOnce()).ensureInitialized();
        then(operation).should(times(1)).getRequestURI(params, status);
        then(operation).should(times(1)).resolveRequestURI(operationURI, client);
        then(operation).should(times(1)).createRequest(requestURI, params, status);
        then(operation).should(times(1)).createHeaders(params, status);
        then(httpRequest).should(times(1)).addHeader(requestHeaders.get(0));
        then(httpRequest).should(times(1)).addHeader(requestHeaders.get(1));
        then(operation).should(times(1)).executeHttpRequest(params, httpRequest, client);
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#execute(Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testExecuteGetRequestURIFails()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        @SuppressWarnings("unchecked")
        final StatedHttpServiceClient<Object> client = mock(StatedHttpServiceClient.class);
        final Object status = new Object();
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"),
                new BasicHeader("anotherHeader", "headerValue"));
        final HttpClientException mockException = new HttpClientException();
        willReturn(status).given(client).ensureInitialized();
        willReturn(requestHeaders).given(operation).createHeaders(params, status);
        willThrow(mockException).given(operation).getRequestURI(params, status);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.execute(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).getRequestURI(params, status);
        then(operation).should(never()).resolveRequestURI(any(), any());
        then(operation).should(never()).createRequest(any(), any(), any());
        then(operation).should(never()).executeHttpRequest(any(), any(), any());
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#execute(Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testExecuteResolveRequestURIFails()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        @SuppressWarnings("unchecked")
        final StatedHttpServiceClient<Object> client = mock(StatedHttpServiceClient.class);
        final Object status = new Object();
        final URI operationURI = URI.create("/mockURI");
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"),
                new BasicHeader("anotherHeader", "headerValue"));
        final HttpClientException mockException = new HttpClientException();
        willReturn(status).given(client).ensureInitialized();
        willReturn(requestHeaders).given(operation).createHeaders(params, status);
        willReturn(operationURI).given(operation).getRequestURI(params, status);
        willThrow(mockException).given(operation).resolveRequestURI(operationURI, client);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.execute(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).getRequestURI(params, status);
        then(operation).should(times(1)).resolveRequestURI(operationURI, client);
        then(operation).should(never()).createRequest(any(), any(), any());
        then(operation).should(never()).executeHttpRequest(any(), any(), any());
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#execute(Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testExecuteCreateRequestFails()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        @SuppressWarnings("unchecked")
        final StatedHttpServiceClient<Object> client = mock(StatedHttpServiceClient.class);
        final Object status = new Object();
        final URI operationURI = URI.create("/mockURI");
        final URI requestURI = URI.create("http://example.org/mockURI");
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"),
                new BasicHeader("anotherHeader", "headerValue"));
        final HttpClientException mockException = new HttpClientException();
        willReturn(status).given(client).ensureInitialized();
        willReturn(operationURI).given(operation).getRequestURI(params, status);
        willReturn(requestURI).given(operation).resolveRequestURI(operationURI, client);
        willReturn(requestHeaders).given(operation).createHeaders(params, status);
        willThrow(mockException).given(operation).createRequest(requestURI, params, status);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.execute(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).getRequestURI(params, status);
        then(operation).should(times(1)).resolveRequestURI(operationURI, client);
        then(operation).should(times(1)).createRequest(requestURI, params, status);
        then(operation).should(never()).executeHttpRequest(any(), any(), any());
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#execute(Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testExecuteCreateHeadersFails()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        @SuppressWarnings("unchecked")
        final StatedHttpServiceClient<Object> client = mock(StatedHttpServiceClient.class);
        final Object status = new Object();
        final URI operationURI = URI.create("/mockURI");
        final URI requestURI = URI.create("http://example.org/mockURI");
        final HttpRequest httpRequest = createMockHttpRequest();
        final HttpClientException mockException = new HttpClientException();
        willReturn(status).given(client).ensureInitialized();
        willReturn(operationURI).given(operation).getRequestURI(params, status);
        willReturn(requestURI).given(operation).resolveRequestURI(operationURI, client);
        willReturn(httpRequest).given(operation).createRequest(requestURI, params, status);
        willThrow(mockException).given(operation).createHeaders(params, status);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.execute(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).createHeaders(params, status);
        then(operation).should(never()).executeHttpRequest(any(), any(), any());
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#execute(Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testExecuteExecuteRequestFails()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        @SuppressWarnings("unchecked")
        final StatedHttpServiceClient<Object> client = mock(StatedHttpServiceClient.class);
        final Object status = new Object();
        final URI operationURI = URI.create("/mockURI");
        final URI requestURI = URI.create("http://example.org/mockURI");
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"),
                new BasicHeader("anotherHeader", "headerValue"));
        final HttpRequest httpRequest = createMockHttpRequest();
        final HttpClientException mockException = new HttpClientException();
        willReturn(status).given(client).ensureInitialized();
        willReturn(requestHeaders).given(operation).createHeaders(params, status);
        willReturn(operationURI).given(operation).getRequestURI(params, status);
        willReturn(requestURI).given(operation).resolveRequestURI(operationURI, client);
        willReturn(httpRequest).given(operation).createRequest(requestURI, params, status);
        willThrow(mockException).given(operation).executeHttpRequest(params, httpRequest, client);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.execute(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(client).should(atLeastOnce()).ensureInitialized();
        then(operation).should(times(1)).getRequestURI(params, status);
        then(operation).should(times(1)).resolveRequestURI(operationURI, client);
        then(operation).should(times(1)).createRequest(requestURI, params, status);
        then(operation).should(times(1)).createHeaders(params, status);
        then(httpRequest).should(times(1)).addHeader(requestHeaders.get(0));
        then(httpRequest).should(times(1)).addHeader(requestHeaders.get(1));
        then(operation).should(times(1)).executeHttpRequest(params, httpRequest, client);
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#createHeaders(Object, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateHeaders()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final Object mockStatus = new Object();
        final List<Header> result = operation.createHeaders(params, mockStatus);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Mock implementation of {@code AbstractStatusDependentOperation}
     * for testing.
     */
    private static class TestStatusIndependentOperation
    extends AbstractStatusDependentOperation<Object, Object, Object, Object> {

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        protected URI getRequestURI(
                final Object params,
                final Object status)
        throws HttpClientException {
            return null;
        }

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        protected HttpRequest createRequest(
                final URI requestURI,
                final Object params,
                final Object status)
        throws HttpClientException {
            return null;
        }

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        protected ResponseHandler<Object> createResponseHandler()
        throws HttpClientException {
            return null;
        }

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        protected Object processResponseEntity(
                final Object params,
                final HttpServiceClient client,
                final HttpRequest request,
                final HttpResponse response,
                final Object responseEntity)
        throws HttpClientException {
            return null;
        }
    }
}
