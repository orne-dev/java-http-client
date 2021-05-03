package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.net.URI;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AbstractStatusIndependentGetOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractStatusIndependentGetOperation
 */
@Tag("ut")
class AbstractStatusIndependentGetOperationTest
extends AbstractStatusIndependentOperationTest {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractStatusIndependentGetOperation<Object, Object, Object> createOperation() {
        return spy(AbstractStatusIndependentGetOperation.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected HttpRequest createMockHttpRequest() {
        return mock(HttpGet.class);
    }

    /**
     * Test for {@link AbstractStatusIndependentGetOperation#createRequest(URI, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateRequest()
    throws Throwable {
        final AbstractStatusIndependentGetOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final URI requestURI = URI.create("http://example.org/some/path");
        final HttpGet result = operation.createRequest(requestURI, params);
        assertNotNull(result);
        assertNotNull(result.getURI());
        assertEquals(requestURI, result.getURI());
    }
}
