package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AbstractStatusDependentDeleteOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractStatusDependentDeleteOperation
 */
@Tag("ut")
public class AbstractStatusDependentDeleteOperationTest
extends AbstractStatusDependentOperationTest {

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractStatusDependentDeleteOperation<Object, Object, Object, Object> createOperation() {
        return new TestStatusIndependentOperation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected HttpRequest createMockHttpRequest() {
        return mock(HttpDelete.class);
    }

    /**
     * Test for {@link AbstractStatusDependentDeleteOperation#createRequest(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateRequest()
    throws Throwable {
        final AbstractStatusDependentDeleteOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        @SuppressWarnings("unchecked")
        final StatedHttpServiceClient<Object> client = mock(StatedHttpServiceClient.class);
        final Object mockStatus = new Object();
        final URI requestUri = URI.create("http://example.org/some/path");
        final List<NameValuePair> requestParams = Arrays.asList(
                new BasicNameValuePair("someParam", "paramValue"));
        final URI expectedUri = URI.create("http://example.org/some/path?someParam=paramValue");
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"));
        willReturn(mockStatus).given(client).ensureInitialized();
        willReturn(requestUri).given(operation).getRequestURI(params, client);
        willReturn(requestParams).given(operation).createParams(params, mockStatus);
        willReturn(requestHeaders).given(operation).createHeaders(params, mockStatus);
        final HttpDelete result = operation.createRequest(params, client);
        assertNotNull(result);
        assertNotNull(result.getURI());
        assertEquals(expectedUri, result.getURI());
        assertNotNull(result.getAllHeaders());
        assertEquals(1, result.getAllHeaders().length);
        assertTrue(result.containsHeader("someHeader"));
        assertEquals("headerValue", result.getFirstHeader("someHeader").getValue());
        then(operation).should(times(1)).getRequestURI(params, client);
        then(operation).should(times(1)).createParams(params, mockStatus);
        then(operation).should(times(1)).createHeaders(params, mockStatus);
    }

    /**
     * Test for {@link AbstractStatusDependentDeleteOperation#createRequest(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateRequestGetUriFail()
    throws Throwable {
        final AbstractStatusDependentDeleteOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        @SuppressWarnings("unchecked")
        final StatedHttpServiceClient<Object> client = mock(StatedHttpServiceClient.class);
        final Object mockStatus = new Object();
        final List<NameValuePair> requestParams = Arrays.asList(
                new BasicNameValuePair("someParam", "paramValue"));
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"));
        final HttpClientException mockException = new HttpClientException();
        willReturn(mockStatus).given(client).ensureInitialized();
        willThrow(mockException).given(operation).getRequestURI(params, client);
        willReturn(requestParams).given(operation).createParams(params, mockStatus);
        willReturn(requestHeaders).given(operation).createHeaders(params, mockStatus);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.createRequest(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).getRequestURI(params, client);
    }

    /**
     * Test for {@link AbstractStatusDependentDeleteOperation#createRequest(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateRequestGetParamsFail()
    throws Throwable {
        final AbstractStatusDependentDeleteOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        @SuppressWarnings("unchecked")
        final StatedHttpServiceClient<Object> client = mock(StatedHttpServiceClient.class);
        final Object mockStatus = new Object();
        final URI requestUri = URI.create("http://example.org/some/path");
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"));
        final HttpClientException mockException = new HttpClientException();
        willReturn(mockStatus).given(client).ensureInitialized();
        willReturn(requestUri).given(operation).getRequestURI(params, client);
        willThrow(mockException).given(operation).createParams(params, mockStatus);
        willReturn(requestHeaders).given(operation).createHeaders(params, mockStatus);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.createRequest(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).createParams(params, mockStatus);
    }

    /**
     * Test for {@link AbstractStatusDependentDeleteOperation#createRequest(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateRequestGetHeadersFail()
    throws Throwable {
        final AbstractStatusDependentDeleteOperation<Object, Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        @SuppressWarnings("unchecked")
        final StatedHttpServiceClient<Object> client = mock(StatedHttpServiceClient.class);
        final Object mockStatus = new Object();
        final URI requestUri = URI.create("http://example.org/some/path");
        final List<NameValuePair> requestParams = Arrays.asList(
                new BasicNameValuePair("someParam", "paramValue"));
        final HttpClientException mockException = new HttpClientException();
        willReturn(mockStatus).given(client).ensureInitialized();
        willReturn(requestUri).given(operation).getRequestURI(params, client);
        willReturn(requestParams).given(operation).createParams(params, mockStatus);
        willThrow(mockException).given(operation).createHeaders(params, mockStatus);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.createRequest(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).createHeaders(params, mockStatus);
    }

    /**
     * Mock implementation of {@code AbstractStatusDependentDeleteOperation}
     * for testing.
     */
    private static class TestStatusIndependentOperation
    extends AbstractStatusDependentDeleteOperation<Object, Object, Object, Object> {

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
