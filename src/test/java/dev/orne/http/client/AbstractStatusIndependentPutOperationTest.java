package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AbstractStatusIndependentPutOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractStatusIndependentPutOperation
 */
@Tag("ut")
public class AbstractStatusIndependentPutOperationTest
extends AbstractStatusIndependentOperationTest {

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractStatusIndependentPutOperation<Object, Object, Object> createOperation() {
        return new TestStatusIndependentOperation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected HttpRequest createMockHttpRequest() {
        return mock(HttpPut.class);
    }

    /**
     * Creates a mock {@code HttpEntity} valid for tested operation.
     * 
     * @return The created mock {@code HttpEntity}
     */
    protected HttpEntity createMockEntity() {
        return mock(HttpEntity.class);
    }

    /**
     * Test for {@link AbstractStatusIndependentPutOperation#createRequest(URI, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateRequest()
    throws Throwable {
        final AbstractStatusIndependentPutOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final URI requestURI = URI.create("http://example.org/some/path");
        final HttpEntity entity = createMockEntity();
        willReturn(entity).given(operation).createEntity(params);
        final HttpPut result = operation.createRequest(requestURI, params);
        assertNotNull(result);
        assertNotNull(result.getURI());
        assertEquals(requestURI, result.getURI());
        assertNotNull(result.getEntity());
        assertSame(entity, result.getEntity());
        then(operation).should(times(1)).createEntity(params);
    }

    /**
     * Test for {@link AbstractStatusIndependentPutOperation#createRequest(URI, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateRequestCreateEntityFail()
    throws Throwable {
        final AbstractStatusIndependentPutOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final URI requestURI = URI.create("http://example.org/some/path");
        final HttpClientException mockException = new HttpClientException();
        willThrow(mockException).given(operation).createEntity(params);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.createRequest(requestURI, params);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).createEntity(params);
    }

    /**
     * Mock implementation of {@code AbstractStatusIndependentPutOperation}
     * for testing.
     */
    private static class TestStatusIndependentOperation
    extends AbstractStatusIndependentPutOperation<Object, Object, Object> {

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        protected URI getRequestURI(
                final Object params)
        throws HttpClientException {
            return null;
        }

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        protected HttpEntity createEntity(
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
