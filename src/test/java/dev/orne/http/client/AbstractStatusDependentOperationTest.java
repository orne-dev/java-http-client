package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

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
     * Test for {@link AbstractStatusDependentOperation#getRequestURI(Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testGetRequestURIRelative()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        @SuppressWarnings("unchecked")
        final StatedHttpServiceClient<Object> client = mock(StatedHttpServiceClient.class);
        final URI baseURI = URI.create("http://example.org/base/");
        final URI relativeURI = URI.create("relative/path");
        final URI expectedURI = URI.create("http://example.org/base/relative/path");
        final Object mockStatus = new Object();
        willReturn(baseURI).given(client).getBaseURI();
        willReturn(mockStatus).given(client).ensureInitialized();
        willReturn(relativeURI).given(operation).getRelativeURI(params, mockStatus);
        final URI result = operation.getRequestURI(params, client);
        assertNotNull(result);
        assertEquals(expectedURI, result);
        then(client).should(times(1)).getBaseURI();
        then(client).should(times(1)).ensureInitialized();
        then(operation).should(times(1)).getRelativeURI(params, mockStatus);
        
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#getRequestURI(Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testGetRequestURIAbsolute()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        @SuppressWarnings("unchecked")
        final StatedHttpServiceClient<Object> client = mock(StatedHttpServiceClient.class);
        final URI baseURI = URI.create("http://example.org/base/");
        final URI relativeURI = URI.create("/absolute/path");
        final URI expectedURI = URI.create("http://example.org/absolute/path");
        final Object mockStatus = new Object();
        willReturn(baseURI).given(client).getBaseURI();
        willReturn(mockStatus).given(client).ensureInitialized();
        willReturn(relativeURI).given(operation).getRelativeURI(params, mockStatus);
        final URI result = operation.getRequestURI(params, client);
        assertNotNull(result);
        assertEquals(expectedURI, result);
        then(client).should(times(1)).getBaseURI();
        then(client).should(times(1)).ensureInitialized();
        then(operation).should(times(1)).getRelativeURI(params, mockStatus);
        
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#getRequestURI(Object, StatedHttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testGetRequestURIRelativeFail()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        @SuppressWarnings("unchecked")
        final StatedHttpServiceClient<Object> client = mock(StatedHttpServiceClient.class);
        final URI baseURI = URI.create("http://example.org/base");
        final HttpClientException mockException = new HttpClientException();
        final Object mockStatus = new Object();
        willReturn(baseURI).given(client).getBaseURI();
        willReturn(mockStatus).given(client).ensureInitialized();
        willThrow(mockException).given(operation).getRelativeURI(params, mockStatus);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.getRequestURI(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(client).should(times(1)).ensureInitialized();
        then(operation).should(times(1)).getRelativeURI(params, mockStatus);
        
    }

    /**
     * Test for {@link AbstractStatusDependentOperation#createParams(Object, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateParams()
    throws Throwable {
        final AbstractStatusDependentOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final Object mockStatus = new Object();
        final List<NameValuePair> result = operation.createParams(params, mockStatus);
        assertNotNull(result);
        assertTrue(result.isEmpty());
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
        protected URI getRelativeURI(
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
                final Object params,
                final StatedHttpServiceClient<Object> client)
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
