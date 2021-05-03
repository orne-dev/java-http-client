package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.net.URI;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpDelete;
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
class AbstractStatusIndependentDeleteOperationTest
extends AbstractStatusIndependentOperationTest {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractStatusIndependentDeleteOperation<Object, Object, Object> createOperation() {
        return spy(AbstractStatusIndependentDeleteOperation.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected HttpRequest createMockHttpRequest() {
        return mock(HttpDelete.class);
    }

    /**
     * Test for {@link AbstractStatusIndependentDeleteOperation#createRequest(URI, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateRequest()
    throws Throwable {
        final AbstractStatusIndependentDeleteOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final URI requestURI = URI.create("http://example.org/some/path");
        final HttpDelete result = operation.createRequest(requestURI, params);
        assertNotNull(result);
        assertNotNull(result.getURI());
        assertEquals(requestURI, result.getURI());
    }
}
