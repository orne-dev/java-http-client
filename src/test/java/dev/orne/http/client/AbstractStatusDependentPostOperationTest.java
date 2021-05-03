package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AbstractStatusDependentPostOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractStatusDependentPostOperation
 */
@Tag("ut")
class AbstractStatusDependentPostOperationTest
extends AbstractStatusDependentOperationTest {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractStatusDependentPostOperation<Object, Object, Object, Object> createOperation() {
        return spy(AbstractStatusDependentPostOperation.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected HttpRequest createMockHttpRequest() {
        return mock(HttpPost.class);
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
     * Test for {@link AbstractStatusDependentPostOperation#createRequest(URI, Object, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateRequest()
    throws Throwable {
        final AbstractStatusDependentPostOperation<Object, Object, Object, Object> operation =
                createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final URI requestURI = URI.create("http://example.org/some/path");
        final HttpEntity entity = createMockEntity();
        willReturn(entity).given(operation).createEntity(params, status);
        final HttpPost result = operation.createRequest(requestURI, params, status);
        assertNotNull(result);
        assertNotNull(result.getURI());
        assertEquals(requestURI, result.getURI());
        assertNotNull(result.getEntity());
        assertSame(entity, result.getEntity());
        then(operation).should(times(1)).createEntity(params, status);
    }

    /**
     * Test for {@link AbstractStatusDependentPostOperation#createRequest(URI, Object, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateRequestCreateEntityFail()
    throws Throwable {
        final AbstractStatusDependentPostOperation<Object, Object, Object, Object> operation =
                createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final URI requestURI = URI.create("http://example.org/some/path");
        final HttpClientException mockException = new HttpClientException();
        willThrow(mockException).given(operation).createEntity(params, status);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.createRequest(requestURI, params, status);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).createEntity(params, status);
    }
}
