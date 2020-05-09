package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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
 * Unit tests for {@code AbstractStatusIndependentDeleteOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractStatusIndependentDeleteOperation
 */
@Tag("ut")
public class AbstractStatusIndependentDeleteOperationTest
extends AbstractStatusIndependentOperationTest {

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractStatusIndependentDeleteOperation<Object, Object, Object> createOperation() {
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
     * Test for {@link AbstractStatusIndependentDeleteOperation#createRequest(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateRequest()
    throws Throwable {
        final AbstractStatusIndependentDeleteOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final HttpServiceClient client = mock(HttpServiceClient.class);
        final URI requestUri = URI.create("http://example.org/some/path");
        final List<NameValuePair> requestParams = Arrays.asList(
                new BasicNameValuePair("someParam", "paramValue"));
        final URI expectedUri = URI.create("http://example.org/some/path?someParam=paramValue");
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"));
        willReturn(requestUri).given(operation).getRequestURI(params, client);
        willReturn(requestParams).given(operation).createParams(params);
        willReturn(requestHeaders).given(operation).createHeaders(params);
        final HttpDelete result = operation.createRequest(params, client);
        assertNotNull(result);
        assertNotNull(result.getURI());
        assertEquals(expectedUri, result.getURI());
        assertNotNull(result.getAllHeaders());
        assertEquals(1, result.getAllHeaders().length);
        assertTrue(result.containsHeader("someHeader"));
        assertEquals("headerValue", result.getFirstHeader("someHeader").getValue());
        then(operation).should(times(1)).getRequestURI(params, client);
        then(operation).should(times(1)).createParams(params);
        then(operation).should(times(1)).createHeaders(params);
    }

    /**
     * Test for {@link AbstractStatusIndependentDeleteOperation#createRequest(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateRequestGetUriFail()
    throws Throwable {
        final AbstractStatusIndependentDeleteOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final HttpServiceClient client = mock(HttpServiceClient.class);
        final List<NameValuePair> requestParams = Arrays.asList(
                new BasicNameValuePair("someParam", "paramValue"));
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"));
        final HttpClientException mockException = new HttpClientException();
        willThrow(mockException).given(operation).getRequestURI(params, client);
        willReturn(requestParams).given(operation).createParams(params);
        willReturn(requestHeaders).given(operation).createHeaders(params);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.createRequest(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).getRequestURI(params, client);
    }

    /**
     * Test for {@link AbstractStatusIndependentDeleteOperation#createRequest(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateRequestGetParamsFail()
    throws Throwable {
        final AbstractStatusIndependentDeleteOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final HttpServiceClient client = mock(HttpServiceClient.class);
        final URI requestUri = URI.create("http://example.org/some/path");
        final List<Header> requestHeaders = Arrays.asList(
                new BasicHeader("someHeader", "headerValue"));
        final HttpClientException mockException = new HttpClientException();
        willReturn(requestUri).given(operation).getRequestURI(params, client);
        willThrow(mockException).given(operation).createParams(params);
        willReturn(requestHeaders).given(operation).createHeaders(params);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.createRequest(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).createParams(params);
    }

    /**
     * Test for {@link AbstractStatusIndependentDeleteOperation#createRequest(Object, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateRequestGetHeadersFail()
    throws Throwable {
        final AbstractStatusIndependentDeleteOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final HttpServiceClient client = mock(HttpServiceClient.class);
        final URI requestUri = URI.create("http://example.org/some/path");
        final List<NameValuePair> requestParams = Arrays.asList(
                new BasicNameValuePair("someParam", "paramValue"));
        final HttpClientException mockException = new HttpClientException();
        willReturn(requestUri).given(operation).getRequestURI(params, client);
        willReturn(requestParams).given(operation).createParams(params);
        willThrow(mockException).given(operation).createHeaders(params);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.createRequest(params, client);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).createHeaders(params);
    }

    /**
     * Mock implementation of {@code AbstractStatusIndependentDeleteOperation}
     * for testing.
     */
    private static class TestStatusIndependentOperation
    extends AbstractStatusIndependentDeleteOperation<Object, Object, Object> {

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
