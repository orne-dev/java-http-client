package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;

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
public class JacksonJSONResponseHandlerTest
extends AbstractMimeTypeResponseHandlerTest {

    /**
     * Test for {@link JacksonJSONResponseHandler#JacksonJSONResponseHandler(Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testTypeConstructorNullType()
    throws Throwable {
        assertThrows(IllegalArgumentException.class, () -> {
            new JacksonJSONResponseHandler(null);
        });
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#JacksonJSONResponseHandler(Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testTypeConstructor()
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
    public void testMapperConstructorNullType()
    throws Throwable {
        final ObjectMapper mapper = mock(ObjectMapper.class);
        assertThrows(IllegalArgumentException.class, () -> {
            new JacksonJSONResponseHandler(mapper, null);
        });
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#JacksonJSONResponseHandler(ObjectMapper, Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testMapperConstructorNullMapper()
    throws Throwable  {
        assertThrows(IllegalArgumentException.class, () -> {
            new JacksonJSONResponseHandler(null, TestBean.class);
        });
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#JacksonJSONResponseHandler(ObjectMapper, Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testMapperConstructor()
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
            @Nonnull
            final ObjectMapper mapper,
            @Nonnull
            final Class<T> targetType) {
        return new JacksonJSONResponseHandler<>(mapper, targetType);
    }

    /**
     * Test for {@link JacksonJSONResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testHandleEntityNullEntity()
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
    public void testHandleEntity()
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
    public void testHandleEntityGetContentFailIOException()
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
    public void testHandleEntityGetContentFailUnsupportedOperationException()
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
    public void testHandleEntityGetContentTypeFail()
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
    public void testHandleEntityReadValueFail()
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
    public void testHandleEntityUnmarshalNullElement()
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
    public void testcreateReader()
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
    public void testcreateReaderNoCharset()
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
    public void testIsMimeTypeSupportedXml()
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
    public void testIsMimeTypeSupportedOthers()
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
    public void testGetDefaultContentType()
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
