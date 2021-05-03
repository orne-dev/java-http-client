package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.net.URI;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AbstractStatusDependentGetOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractStatusDependentGetOperation
 */
@Tag("ut")
class AbstractStatusDependentGetOperationTest
extends AbstractStatusDependentOperationTest {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractStatusDependentGetOperation<Object, Object, Object, Object> createOperation() {
        return spy(AbstractStatusDependentGetOperation.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected HttpRequest createMockHttpRequest() {
        return mock(HttpGet.class);
    }

    /**
     * Test for {@link AbstractStatusDependentGetOperation#createRequest(URI, Object, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateRequest()
    throws Throwable {
        final AbstractStatusDependentGetOperation<Object, Object, Object, Object> operation =
                createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final URI requestURI = URI.create("http://example.org/some/path");
        final HttpGet result = operation.createRequest(requestURI, params, status);
        assertNotNull(result);
        assertNotNull(result.getURI());
        assertSame(requestURI, result.getURI());
    }
}
