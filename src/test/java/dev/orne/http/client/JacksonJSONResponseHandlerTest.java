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
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit tests for {@code JacksonJSONResponseHandler}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see JacksonJSONResponseHandler
 */
@Tag("ut")
class JacksonJSONResponseHandlerTest
extends AbstractMimeTypeResponseHandlerTest {

    /**
     * Test for {@link JacksonJSONResponseHandler#JacksonJSONResponseHandler(Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testTypeConstructorNullType()
    throws Throwable {
        assertThrows(NullPointerException.class, () -> {
            new JacksonJSONResponseHandler(null);
        });
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#JacksonJSONResponseHandler(Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testTypeConstructor()
    throws Throwable  {
        final JacksonJSONResponseHandler<TestBean> handler =
                new JacksonJSONResponseHandler<>(TestBean.class);
        assertNotNull(handler.getValueType());
        assertSame(TestBean.class, handler.getValueType());
        assertNotNull(handler.getMapper());
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#JacksonJSONResponseHandler(ObjectMapper, Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testMapperConstructorNullType()
    throws Throwable {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        assertThrows(NullPointerException.class, () -> {
            new JacksonJSONResponseHandler(mapper, null);
        });
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#JacksonJSONResponseHandler(ObjectMapper, Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testMapperConstructorNullMapper()
    throws Throwable  {
        assertThrows(NullPointerException.class, () -> {
            new JacksonJSONResponseHandler(null, TestBean.class);
        });
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#JacksonJSONResponseHandler(ObjectMapper, Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testMapperConstructor()
    throws Throwable  {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        final JacksonJSONResponseHandler<TestBean> handler =
                new JacksonJSONResponseHandler<>(mapper, TestBean.class);
        assertNotNull(handler.getValueType());
        assertSame(TestBean.class, handler.getValueType());
        assertNotNull(handler.getMapper());
        assertSame(mapper, handler.getMapper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractMimeTypeResponseHandler<?> createHandler() {
        return new JacksonJSONResponseHandler<TestBean>(
                mock(ObjectMapper.class),
                TestBean.class);
    }

    /**
     * Creates a {@code JacksonJSONResponseHandler} for specified JAXB context and
     * target type.
     * 
     * @param <T> The target type
     * @param mapper The Jackson object mapper
     * @param targetType The target type
     * @return The created {@code JacksonJSONResponseHandler} instance
     */
    protected <T> JacksonJSONResponseHandler<T> createHandlerForMapperAndType(
            final @NotNull ObjectMapper mapper,
            final @NotNull Class<T> targetType) {
        return new JacksonJSONResponseHandler<>(mapper, targetType);
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testHandleEntityNullEntity()
    throws Throwable {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        final JacksonJSONResponseHandler<TestBean> handler =
                createHandlerForMapperAndType(mapper, TestBean.class);
        final Object result = handler.handleEntity(null);
        assertNull(result);
        then(mapper).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testHandleEntity()
    throws Throwable {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        final JacksonJSONResponseHandler<TestBean> handler =
                spy(createHandlerForMapperAndType(mapper, TestBean.class));
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final InputStream mockIS = mock(InputStream.class);
        final ContentType mockContentType = ContentType.create("mock");
        final Reader mockReader = mock(Reader.class);
        final TestBean mockResult = mock(TestBean.class);
        willReturn(mockIS).given(mockEntity).getContent();
        willReturn(mockContentType).given(handler).getSupportedContentType(mockEntity);
        willReturn(mockReader).given(handler).createReader(mockIS, mockContentType);
        willReturn(mockResult).given(mapper).readValue(mockReader, TestBean.class);
        final Object result = handler.handleEntity(mockEntity);
        assertNotNull(result);
        assertSame(mockResult, result);
        final InOrder inOrder = inOrder(mockEntity, mapper, mockIS);
        inOrder.verify(mockEntity, times(1)).getContent();
        inOrder.verify(mapper, times(1)).readValue(
                any(Reader.class), same(TestBean.class));
        inOrder.verify(mockIS, times(1)).close();
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testHandleEntityGetContentFailIOException()
    throws Throwable {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        final JacksonJSONResponseHandler<TestBean> handler =
                spy(createHandlerForMapperAndType(mapper, TestBean.class));
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final ContentType mockContentType = ContentType.create("mock");
        final IOException mockResult = new IOException();
        willThrow(mockResult).given(mockEntity).getContent();
        willReturn(mockContentType).given(handler).getSupportedContentType(mockEntity);
        final IOException result = assertThrows(IOException.class, () -> {
            handler.handleEntity(mockEntity);
        });
        assertNotNull(result);
        assertSame(mockResult, result);
        then(mockEntity).should(times(1)).getContent();
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testHandleEntityGetContentFailUnsupportedOperationException()
    throws Throwable {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        final JacksonJSONResponseHandler<TestBean> handler =
                spy(createHandlerForMapperAndType(mapper, TestBean.class));
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final ContentType mockContentType = ContentType.create("mock");
        final UnsupportedOperationException mockResult = new UnsupportedOperationException();
        willThrow(mockResult).given(mockEntity).getContent();
        willReturn(mockContentType).given(handler).getSupportedContentType(mockEntity);
        final IOException result = assertThrows(IOException.class, () -> {
            handler.handleEntity(mockEntity);
        });
        assertNotNull(result);
        assertSame(mockResult, result.getCause());
        then(mockEntity).should(times(1)).getContent();
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testHandleEntityGetContentTypeFail()
    throws Throwable {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        final JacksonJSONResponseHandler<TestBean> handler =
                spy(createHandlerForMapperAndType(mapper, TestBean.class));
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final InputStream mockIS = mock(InputStream.class);
        final IOException mockResult = new IOException();
        willReturn(mockIS).given(mockEntity).getContent();
        willThrow(mockResult).given(handler).getSupportedContentType(mockEntity);
        final IOException result = assertThrows(IOException.class, () -> {
            handler.handleEntity(mockEntity);
        });
        assertNotNull(result);
        assertSame(mockResult, result);
        then(handler).should(times(1)).getSupportedContentType(mockEntity);
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testHandleEntityReadValueFail()
    throws Throwable {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        final JacksonJSONResponseHandler<TestBean> handler =
                spy(createHandlerForMapperAndType(mapper, TestBean.class));
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final InputStream mockIS = mock(InputStream.class);
        final ContentType mockContentType = ContentType.create("mock");
        final Reader mockReader = mock(Reader.class);
        final IOException mockResult = new IOException("mock");
        willReturn(mockIS).given(mockEntity).getContent();
        willReturn(mockContentType).given(handler).getSupportedContentType(mockEntity);
        willReturn(mockReader).given(handler).createReader(mockIS, mockContentType);
        willThrow(mockResult).given(mapper).readValue(mockReader, TestBean.class);
        final IOException result = assertThrows(IOException.class, () -> {
            handler.handleEntity(mockEntity);
        });
        assertNotNull(result);
        assertSame(mockResult, result);
        final InOrder inOrder = inOrder(mockEntity, mapper, mockIS);
        inOrder.verify(mockEntity, times(1)).getContent();
        inOrder.verify(mapper, times(1)).readValue(
                any(Reader.class), same(TestBean.class));
        inOrder.verify(mockIS, times(1)).close();
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testHandleEntityUnmarshalNullElement()
    throws Throwable {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        final JacksonJSONResponseHandler<TestBean> handler =
                spy(createHandlerForMapperAndType(mapper, TestBean.class));
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final InputStream mockIS = mock(InputStream.class);
        final ContentType mockContentType = ContentType.create("mock");
        final Reader mockReader = mock(Reader.class);
        willReturn(mockIS).given(mockEntity).getContent();
        willReturn(mockContentType).given(handler).getSupportedContentType(mockEntity);
        willReturn(mockReader).given(handler).createReader(mockIS, mockContentType);
        willReturn(null).given(mapper).readValue(mockReader, TestBean.class);
        final Object result = handler.handleEntity(mockEntity);
        assertNull(result);
        final InOrder inOrder = inOrder(mockEntity, mapper, mockIS);
        inOrder.verify(mockEntity, times(1)).getContent();
        inOrder.verify(mapper, times(1)).readValue(
                any(Reader.class), same(TestBean.class));
        inOrder.verify(mockIS, times(1)).close();
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#createReader(InputStream, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testcreateReader()
    throws Throwable {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        final JacksonJSONResponseHandler<TestBean> handler =
                createHandlerForMapperAndType(mapper, TestBean.class);
        final InputStream mockEntityConent = mock(InputStream.class);
        final ContentType mockContentType = ContentType.create(
                ContentType.APPLICATION_JSON.getMimeType(),
                StandardCharsets.US_ASCII);
        final Reader result = handler.createReader(
                mockEntityConent, mockContentType);
        assertNotNull(result);
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#createReader(InputStream, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testcreateReaderNoCharset()
    throws Throwable {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        final JacksonJSONResponseHandler<TestBean> handler =
                createHandlerForMapperAndType(mapper, TestBean.class);
        final InputStream mockEntityConent = mock(InputStream.class);
        final ContentType mockContentType = ContentType.create(
                ContentType.APPLICATION_JSON.getMimeType());
        final Reader result = handler.createReader(
                mockEntityConent, mockContentType);
        assertNotNull(result);
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#isMimeTypeSupported(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testIsMimeTypeSupportedXml()
    throws Throwable {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        final JacksonJSONResponseHandler<TestBean> handler =
                createHandlerForMapperAndType(mapper, TestBean.class);
        assertTrue(handler.isMimeTypeSupported(
                ContentType.APPLICATION_JSON.getMimeType()));
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#isMimeTypeSupported(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testIsMimeTypeSupportedOthers()
    throws Throwable {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        final JacksonJSONResponseHandler<TestBean> handler =
                createHandlerForMapperAndType(mapper, TestBean.class);
        assertFalse(handler.isMimeTypeSupported(
                ContentType.APPLICATION_XML.getMimeType()));
        assertFalse(handler.isMimeTypeSupported(
                ContentType.TEXT_XML.getMimeType()));
        assertFalse(handler.isMimeTypeSupported(
                ContentType.TEXT_HTML.getMimeType()));
        assertFalse(handler.isMimeTypeSupported(
                ContentType.TEXT_PLAIN.getMimeType()));
        assertFalse(handler.isMimeTypeSupported("mock-mime-type"));
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#getDefaultContentType()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetDefaultContentType()
    throws Throwable {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        final JacksonJSONResponseHandler<TestBean> handler =
                createHandlerForMapperAndType(mapper, TestBean.class);
        final ContentType result = handler.getDefaultContentType();
        assertNotNull(result);
        assertEquals(ContentType.APPLICATION_JSON.getMimeType(), result.getMimeType());
        assertNull(result.getCharset());
    }

    /**
     * Mock bean for testing.
     */
    protected static class TestBean {
        // No extra methods
    }
}
