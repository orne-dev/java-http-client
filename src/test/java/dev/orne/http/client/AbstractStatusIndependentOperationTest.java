package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.net.URI;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
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
public class AbstractStatusIndependentOperationTest
extends AbstractHttpServiceOperationTest {

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractStatusIndependentOperation<Object, Object, Object> createOperation() {
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
     * Test for {@link AbstractStatusIndependentOperation#execute(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testExecute()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final HttpServiceClient client = mock(HttpServiceClient.class);
        final HttpRequest httpRequest = createMockHttpRequest();
        final Object mockResult = new Object();
        willReturn(httpRequest).given(operation).createRequest(params, client);
        willReturn(mockResult).given(operation).executeHttpRequest(params, httpRequest, client);
        final Object result = operation.execute(params, client);
        assertNotNull(result);
        assertSame(mockResult, result);
        then(operation).should(times(1)).createRequest(params, client);
        then(operation).should(times(1)).executeHttpRequest(params, httpRequest, client);
    }

    /**
     * Test for {@link AbstractStatusIndependentOperation#execute(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testExecuteCreateRequestFails()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final HttpServiceClient client = mock(HttpServiceClient.class);
        final HttpClientException mockException = new HttpClientException();
        willThrow(mockException).given(operation).createRequest(params, client);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.execute(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).createRequest(params, client);
        then(operation).should(never()).executeHttpRequest(any(), any(), any());
    }

    /**
     * Test for {@link AbstractStatusIndependentOperation#execute(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testExecuteExecuteRequestFails()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final HttpServiceClient client = mock(HttpServiceClient.class);
        final HttpRequest httpRequest = createMockHttpRequest();
        final HttpClientException mockException = new HttpClientException();
        willReturn(httpRequest).given(operation).createRequest(params, client);
        willThrow(mockException).given(operation).executeHttpRequest(params, httpRequest, client);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.execute(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).createRequest(params, client);
        then(operation).should(times(1)).executeHttpRequest(params, httpRequest, client);
    }

    /**
     * Test for {@link AbstractStatusIndependentOperation#getRequestURI(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testGetRequestURIRelative()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final HttpServiceClient client = mock(HttpServiceClient.class);
        final URI baseURI = URI.create("http://example.org/base/");
        final URI relativeURI = URI.create("relative/path");
        final URI expectedURI = URI.create("http://example.org/base/relative/path");
        willReturn(baseURI).given(client).getBaseURI();
        willReturn(relativeURI).given(operation).getRelativeURI(params);
        final URI result = operation.getRequestURI(params, client);
        assertNotNull(result);
        assertEquals(expectedURI, result);
        then(client).should(times(1)).getBaseURI();
        then(operation).should(times(1)).getRelativeURI(params);
        
    }

    /**
     * Test for {@link AbstractStatusIndependentOperation#getRequestURI(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testGetRequestURIAbsolute()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final HttpServiceClient client = mock(HttpServiceClient.class);
        final URI baseURI = URI.create("http://example.org/base/");
        final URI relativeURI = URI.create("/absolute/path");
        final URI expectedURI = URI.create("http://example.org/absolute/path");
        willReturn(baseURI).given(client).getBaseURI();
        willReturn(relativeURI).given(operation).getRelativeURI(params);
        final URI result = operation.getRequestURI(params, client);
        assertNotNull(result);
        assertEquals(expectedURI, result);
        then(client).should(times(1)).getBaseURI();
        then(operation).should(times(1)).getRelativeURI(params);
        
    }

    /**
     * Test for {@link AbstractStatusIndependentOperation#getRequestURI(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testGetRequestURIRelativeFail()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final HttpServiceClient client = mock(HttpServiceClient.class);
        final URI baseURI = URI.create("http://example.org/base");
        final HttpClientException mockException = new HttpClientException();
        willReturn(baseURI).given(client).getBaseURI();
        willThrow(mockException).given(operation).getRelativeURI(params);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.getRequestURI(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).getRelativeURI(params);
        
    }

    /**
     * Test for {@link AbstractStatusIndependentOperation#createParams(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateParams()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final List<NameValuePair> result = operation.createParams(params);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test for {@link AbstractStatusIndependentOperation#createHeaders(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateHeaders()
    throws Throwable {
        final AbstractStatusIndependentOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final List<Header> result = operation.createHeaders(params);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Mock implementation of {@code AbstractStatusIndependentOperation}
     * for testing.
     */
    private static class TestStatusIndependentOperation
    extends AbstractStatusIndependentOperation<Object, Object, Object> {

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

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        protected HttpRequest createRequest(
                final Object params,
                final HttpServiceClient client)
        throws HttpClientException {
            return null;
        }

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        protected URI getRelativeURI(
                final Object params)
        throws HttpClientException {
            return null;
        }
    }
}
