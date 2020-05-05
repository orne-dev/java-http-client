package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code VoidResponseHandler}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see VoidResponseHandler
 */
@Tag("ut")
public class VoidResponseHandlerTest {

    /**
     * Test for {@link VoidResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testHandleEntityNullEntity()
    throws IOException {
        final VoidResponseHandler handler = new VoidResponseHandler();
        final Object result = handler.handleEntity(null);
        assertNull(result);
    }

    /**
     * Test for {@link VoidResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testHandleEntity()
    throws IOException {
        final VoidResponseHandler handler = new VoidResponseHandler();
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final Object result = handler.handleEntity(mockEntity);
        assertNull(result);
        then(mockEntity).shouldHaveNoInteractions();
    }
}
