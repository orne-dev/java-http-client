package dev.orne.http.client;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2020 - 2021 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for {@code AbstractMimeTypeResponseHandler}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractMimeTypeResponseHandler
 */
@Tag("ut")
public class AbstractMimeTypeResponseHandlerTest {

    /**
     * Creates an instance of {@code AbstractMimeTypeResponseHandler}
     * for testing purposes.
     * 
     * @return The created {@code AbstractMimeTypeResponseHandler} instance
     */
    protected AbstractMimeTypeResponseHandler<?> createHandler() {
        return new TestMimeTypeResponseHandler();
    }

    /**
     * Test for {@link AbstractMimeTypeResponseHandler#getSupportedContentType(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testGetContentType()
    throws Throwable {
        final AbstractMimeTypeResponseHandler<?> handler = spy(createHandler());
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final String mockMimeType = "mock-mime-type";
        final ContentType mockContentType = ContentType.create(
                mockMimeType,
                StandardCharsets.UTF_8);
        final Header mockHeader = mock(Header.class);
        willReturn(mockHeader).given(mockEntity).getContentType();
        willReturn(mockContentType.toString()).given(mockHeader).getValue();
        willReturn(true).given(handler).isMimeTypeSupported(mockMimeType);
        final ContentType result = handler.getSupportedContentType(mockEntity);
        assertNotNull(result);
        assertEquals(mockMimeType, result.getMimeType());
        assertEquals(StandardCharsets.UTF_8, result.getCharset());
        then(handler).should(atLeastOnce()).isMimeTypeSupported(mockMimeType);
    }

    /**
     * Test for {@link AbstractMimeTypeResponseHandler#getSupportedContentType(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testGetContentTypeNull()
    throws Throwable {
        final AbstractMimeTypeResponseHandler<?> handler = spy(createHandler());
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final ContentType mockDefaultContentType = ContentType.create(
                "mock-mime-type",
                StandardCharsets.UTF_8);
        willReturn(null).given(mockEntity).getContentType();
        willReturn(mockDefaultContentType).given(handler).getDefaultContentType();
        final ContentType result = handler.getSupportedContentType(mockEntity);
        assertNotNull(result);
        assertSame(mockDefaultContentType, result);
        then(handler).should(atLeastOnce()).getDefaultContentType();
    }

    /**
     * Test for {@link AbstractMimeTypeResponseHandler#getSupportedContentType(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testGetContentTypeUnsupported()
    throws Throwable {
        final AbstractMimeTypeResponseHandler<?> handler = spy(createHandler());
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final String mockMimeType = "mock-mime-type";
        final ContentType mockContentType = ContentType.create(
                mockMimeType,
                StandardCharsets.UTF_8);
        final Header mockHeader = mock(Header.class);
        willReturn(false).given(handler).isMimeTypeSupported(mockMimeType);
        willReturn(mockHeader).given(mockEntity).getContentType();
        willReturn(mockContentType.toString()).given(mockHeader).getValue();
        assertThrows(IOException.class, () -> {
            handler.getSupportedContentType(mockEntity);
        });
        then(handler).should(atLeastOnce()).isMimeTypeSupported(mockMimeType);
    }

    /**
     * Test for {@link AbstractMimeTypeResponseHandler#getSupportedContentType(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testGetContentTypeMalformedHeader()
    throws Throwable {
        final AbstractMimeTypeResponseHandler<?> handler = createHandler();
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final Header mockHeader = mock(Header.class);
        willReturn(mockHeader).given(mockEntity).getContentType();
        willReturn("").given(mockHeader).getValue();
        assertThrows(IOException.class, () -> {
            handler.getSupportedContentType(mockEntity);
        });
    }

    /**
     * Test for {@link AbstractMimeTypeResponseHandler#getSupportedContentType(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testGetContentTypeUnsupportedCharset()
    throws Throwable {
        final AbstractMimeTypeResponseHandler<?> handler = createHandler();
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final Header mockHeader = mock(Header.class);
        final String wrongResult = new StringBuilder()
                .append("mock-mime-type; charset=thisIsNotACharset")
                .toString();
        willReturn(mockHeader).given(mockEntity).getContentType();
        willReturn(wrongResult).given(mockHeader).getValue();
        assertThrows(IOException.class, () -> {
            handler.getSupportedContentType(mockEntity);
        });
    }

    /**
     * Test for {@link AbstractMimeTypeResponseHandler#getLogger()}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testLogger()
    throws Throwable {
        final AbstractMimeTypeResponseHandler<?> handler = createHandler();
        final Logger logger = handler.getLogger();
        assertNotNull(logger);
        assertSame(LoggerFactory.getLogger(handler.getClass()), logger);
    }

    /**
     * Mock implementation of {@code AbstractHttpServiceOperation}
     * for testing.
     */
    private static class TestMimeTypeResponseHandler
    extends AbstractMimeTypeResponseHandler<Object> {

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        protected boolean isMimeTypeSupported(
                final String mimeType) {
            return false;
        }

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        protected ContentType getDefaultContentType() {
            return null;
        }

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        public Object handleEntity(
                final HttpEntity entity)
        throws IOException {
            return null;
        }
    }
}
