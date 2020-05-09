package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

/**
 * Unit tests for {@code JaxbXMLResponseHandler}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see JaxbXMLResponseHandler
 */
@Tag("ut")
public class XmlResponseHandlerTest
extends AbstractMimeTypeResponseHandlerTest {

    /**
     * Test for {@link JaxbXMLResponseHandler#JaxbXMLResponseHandler(Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testTypeConstructorNullType()
    throws Throwable {
        assertThrows(IllegalArgumentException.class, () -> {
            new JaxbXMLResponseHandler(null);
        });
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#JaxbXMLResponseHandler(Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testTypeConstructor()
    throws Throwable  {
        final JaxbXMLResponseHandler<TestBean> handler =
                new JaxbXMLResponseHandler<>(TestBean.class);
        assertNotNull(handler.getValueType());
        assertSame(TestBean.class, handler.getValueType());
        assertNotNull(handler.getContext());
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#JaxbXMLResponseHandler(JAXBContext, Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testContextConstructorNullType()
    throws Throwable {
        final JAXBContext mockContext = mock(JAXBContext.class);
        assertThrows(IllegalArgumentException.class, () -> {
            new JaxbXMLResponseHandler(mockContext, null);
        });
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#JaxbXMLResponseHandler(JAXBContext, Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testContextConstructorNullContext()
    throws Throwable  {
        assertThrows(IllegalArgumentException.class, () -> {
            new JaxbXMLResponseHandler(null, TestBean.class);
        });
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#JaxbXMLResponseHandler(JAXBContext, Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testContextConstructor()
    throws Throwable  {
        final JAXBContext mockContext = mock(JAXBContext.class);
        final JaxbXMLResponseHandler<TestBean> handler =
                new JaxbXMLResponseHandler<>(mockContext, TestBean.class);
        assertNotNull(handler.getValueType());
        assertSame(TestBean.class, handler.getValueType());
        assertNotNull(handler.getContext());
        assertSame(mockContext, handler.getContext());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractMimeTypeResponseHandler<?> createHandler() {
        return new JaxbXMLResponseHandler<TestBean>(
                mock(JAXBContext.class),
                TestBean.class);
    }

    /**
     * Creates a {@code JaxbXMLResponseHandler} for specified JAXB context and
     * target type.
     * 
     * @param <T> The target type
     * @param mockContext The JAXB context
     * @param targetType The target type
     * @return The created {@code JaxbXMLResponseHandler} instance
     */
    protected <T> JaxbXMLResponseHandler<T> createHandlerForContextAndType(
            @Nonnull
            final JAXBContext mockContext,
            @Nonnull
            final Class<T> targetType) {
        return new JaxbXMLResponseHandler<>(mockContext, targetType);
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testHandleEntityNullEntity()
    throws Throwable {
        final JAXBContext mockContext = mock(JAXBContext.class);
        final JaxbXMLResponseHandler<TestBean> handler =
                createHandlerForContextAndType(mockContext, TestBean.class);
        final Object result = handler.handleEntity(null);
        assertNull(result);
        then(mockContext).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testHandleEntity()
    throws Throwable {
        final JAXBContext mockContext = mock(JAXBContext.class);
        final JaxbXMLResponseHandler<TestBean> handler =
                spy(createHandlerForContextAndType(mockContext, TestBean.class));
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final InputStream mockIS = mock(InputStream.class);
        final ContentType mockContentType = ContentType.create("mock");
        final StreamSource mockSource = mock(StreamSource.class);
        final Unmarshaller mockUnmarshaller = mock(Unmarshaller.class);
        @SuppressWarnings("unchecked")
        final JAXBElement<TestBean> mockJAXBElement = mock(JAXBElement.class);
        final TestBean mockResult = mock(TestBean.class);
        willReturn(mockIS).given(mockEntity).getContent();
        willReturn(mockContentType).given(handler).getSupportedContentType(mockEntity);
        willReturn(mockSource).given(handler).createSource(mockIS, mockContentType);
        willReturn(mockUnmarshaller).given(mockContext).createUnmarshaller();
        willReturn(mockJAXBElement).given(mockUnmarshaller).unmarshal(mockSource, TestBean.class);
        willReturn(mockResult).given(mockJAXBElement).getValue();
        final Object result = handler.handleEntity(mockEntity);
        assertNotNull(result);
        assertSame(mockResult, result);
        then(mockContext).should(times(1)).createUnmarshaller();
        then(mockJAXBElement).should(times(1)).getValue();
        final InOrder inOrder = inOrder(mockEntity, mockUnmarshaller, mockIS);
        inOrder.verify(mockEntity, times(1)).getContent();
        inOrder.verify(mockUnmarshaller, times(1)).unmarshal(mockSource, TestBean.class);
        inOrder.verify(mockIS, times(1)).close();
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testHandleEntityGetContentFailIOException()
    throws Throwable {
        final JAXBContext mockContext = mock(JAXBContext.class);
        final JaxbXMLResponseHandler<TestBean> handler =
                spy(createHandlerForContextAndType(mockContext, TestBean.class));
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final ContentType mockContentType = ContentType.create("mock");
        final Unmarshaller mockUnmarshaller = mock(Unmarshaller.class);
        final IOException mockResult = new IOException();
        willThrow(mockResult).given(mockEntity).getContent();
        willReturn(mockContentType).given(handler).getSupportedContentType(mockEntity);
        willReturn(mockUnmarshaller).given(mockContext).createUnmarshaller();
        final IOException result = assertThrows(IOException.class, () -> {
            handler.handleEntity(mockEntity);
        });
        assertNotNull(result);
        assertSame(mockResult, result);
        then(mockEntity).should(times(1)).getContent();
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testHandleEntityGetContentFailUnsupportedOperationException()
    throws Throwable {
        final JAXBContext mockContext = mock(JAXBContext.class);
        final JaxbXMLResponseHandler<TestBean> handler =
                spy(createHandlerForContextAndType(mockContext, TestBean.class));
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final ContentType mockContentType = ContentType.create("mock");
        final Unmarshaller mockUnmarshaller = mock(Unmarshaller.class);
        final UnsupportedOperationException mockResult = new UnsupportedOperationException();
        willThrow(mockResult).given(mockEntity).getContent();
        willReturn(mockContentType).given(handler).getSupportedContentType(mockEntity);
        willReturn(mockUnmarshaller).given(mockContext).createUnmarshaller();
        final IOException result = assertThrows(IOException.class, () -> {
            handler.handleEntity(mockEntity);
        });
        assertNotNull(result);
        assertSame(mockResult, result.getCause());
        then(mockEntity).should(times(1)).getContent();
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testHandleEntityGetContentTypeFail()
    throws Throwable {
        final JAXBContext mockContext = mock(JAXBContext.class);
        final JaxbXMLResponseHandler<TestBean> handler =
                spy(createHandlerForContextAndType(mockContext, TestBean.class));
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final InputStream mockIS = mock(InputStream.class);
        final Unmarshaller mockUnmarshaller = mock(Unmarshaller.class);
        final IOException mockResult = new IOException();
        willReturn(mockIS).given(mockEntity).getContent();
        willThrow(mockResult).given(handler).getSupportedContentType(mockEntity);
        willReturn(mockUnmarshaller).given(mockContext).createUnmarshaller();
        final IOException result = assertThrows(IOException.class, () -> {
            handler.handleEntity(mockEntity);
        });
        assertNotNull(result);
        assertSame(mockResult, result);
        then(handler).should(times(1)).getSupportedContentType(mockEntity);
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testHandleEntityCreateUnmarshallerFail()
    throws Throwable {
        final JAXBContext mockContext = mock(JAXBContext.class);
        final JaxbXMLResponseHandler<TestBean> handler =
                spy(createHandlerForContextAndType(mockContext, TestBean.class));
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final InputStream mockIS = mock(InputStream.class);
        final ContentType mockContentType = ContentType.create("mock");
        final StreamSource mockSource = mock(StreamSource.class);
        final JAXBException mockResult = new JAXBException("mock");
        willReturn(mockIS).given(mockEntity).getContent();
        willReturn(mockContentType).given(handler).getSupportedContentType(mockEntity);
        willReturn(mockSource).given(handler).createSource(mockIS, mockContentType);
        willThrow(mockResult).given(mockContext).createUnmarshaller();
        final IOException result = assertThrows(IOException.class, () -> {
            handler.handleEntity(mockEntity);
        });
        assertNotNull(result);
        assertSame(mockResult, result.getCause());
        then(mockContext).should(times(1)).createUnmarshaller();
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testHandleEntityUnmarshalFail()
    throws Throwable {
        final JAXBContext mockContext = mock(JAXBContext.class);
        final JaxbXMLResponseHandler<TestBean> handler =
                spy(createHandlerForContextAndType(mockContext, TestBean.class));
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final InputStream mockIS = mock(InputStream.class);
        final ContentType mockContentType = ContentType.create("mock");
        final StreamSource mockSource = mock(StreamSource.class);
        final Unmarshaller mockUnmarshaller = mock(Unmarshaller.class);
        final JAXBException mockResult = new JAXBException("mock");
        willReturn(mockIS).given(mockEntity).getContent();
        willReturn(mockContentType).given(handler).getSupportedContentType(mockEntity);
        willReturn(mockSource).given(handler).createSource(mockIS, mockContentType);
        willReturn(mockUnmarshaller).given(mockContext).createUnmarshaller();
        willThrow(mockResult).given(mockUnmarshaller).unmarshal(mockSource, TestBean.class);
        final IOException result = assertThrows(IOException.class, () -> {
            handler.handleEntity(mockEntity);
        });
        assertNotNull(result);
        assertSame(mockResult, result.getCause());
        then(mockContext).should(times(1)).createUnmarshaller();
        final InOrder inOrder = inOrder(mockEntity, mockUnmarshaller, mockIS);
        inOrder.verify(mockEntity, times(1)).getContent();
        inOrder.verify(mockUnmarshaller, times(1)).unmarshal(
                any(StreamSource.class), same(TestBean.class));
        inOrder.verify(mockIS, times(1)).close();
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testHandleEntityUnmarshalNullElement()
    throws Throwable {
        final JAXBContext mockContext = mock(JAXBContext.class);
        final JaxbXMLResponseHandler<TestBean> handler =
                spy(createHandlerForContextAndType(mockContext, TestBean.class));
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final InputStream mockIS = mock(InputStream.class);
        final ContentType mockContentType = ContentType.create("mock");
        final StreamSource mockSource = mock(StreamSource.class);
        final Unmarshaller mockUnmarshaller = mock(Unmarshaller.class);
        willReturn(mockIS).given(mockEntity).getContent();
        willReturn(mockContentType).given(handler).getSupportedContentType(mockEntity);
        willReturn(mockSource).given(handler).createSource(mockIS, mockContentType);
        willReturn(mockUnmarshaller).given(mockContext).createUnmarshaller();
        willReturn(null).given(mockUnmarshaller).unmarshal(mockSource, TestBean.class);
        final Object result = handler.handleEntity(mockEntity);
        assertNull(result);
        then(mockContext).should(times(1)).createUnmarshaller();
        final InOrder inOrder = inOrder(mockEntity, mockUnmarshaller, mockIS);
        inOrder.verify(mockEntity, times(1)).getContent();
        inOrder.verify(mockUnmarshaller, times(1)).unmarshal(mockSource, TestBean.class);
        inOrder.verify(mockIS, times(1)).close();
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#createSource(InputStream, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateSource()
    throws Throwable {
        final JAXBContext mockContext = mock(JAXBContext.class);
        final JaxbXMLResponseHandler<TestBean> handler =
                createHandlerForContextAndType(mockContext, TestBean.class);
        final InputStream mockEntityConent = mock(InputStream.class);
        final ContentType mockContentType = ContentType.create(
                ContentType.APPLICATION_XML.getMimeType(),
                StandardCharsets.UTF_8);
        final StreamSource result = handler.createSource(
                mockEntityConent, mockContentType);
        assertNotNull(result);
        assertNull(result.getInputStream());
        assertNotNull(result.getReader());
        assertTrue(result.getReader() instanceof InputStreamReader);
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#createSource(InputStream, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateSourceNoCharset()
    throws Throwable {
        final JAXBContext mockContext = mock(JAXBContext.class);
        final JaxbXMLResponseHandler<TestBean> handler =
                createHandlerForContextAndType(mockContext, TestBean.class);
        final InputStream mockEntityConent = mock(InputStream.class);
        final ContentType mockContentType = ContentType.create(
                ContentType.APPLICATION_XML.getMimeType());
        final StreamSource result = handler.createSource(
                mockEntityConent, mockContentType);
        assertNotNull(result);
        assertNotNull(result.getInputStream());
        assertSame(mockEntityConent, result.getInputStream());
        assertNull(result.getReader());
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#isMimeTypeSupported(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testIsMimeTypeSupportedXml()
    throws Throwable {
        final JAXBContext mockContext = mock(JAXBContext.class);
        final JaxbXMLResponseHandler<TestBean> handler =
                createHandlerForContextAndType(mockContext, TestBean.class);
        assertTrue(handler.isMimeTypeSupported(
                ContentType.APPLICATION_XML.getMimeType()));
        assertTrue(handler.isMimeTypeSupported(
                ContentType.TEXT_XML.getMimeType()));
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#isMimeTypeSupported(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testIsMimeTypeSupportedOthers()
    throws Throwable {
        final JAXBContext mockContext = mock(JAXBContext.class);
        final JaxbXMLResponseHandler<TestBean> handler =
                createHandlerForContextAndType(mockContext, TestBean.class);
        assertFalse(handler.isMimeTypeSupported(
                ContentType.APPLICATION_JSON.getMimeType()));
        assertFalse(handler.isMimeTypeSupported(
                ContentType.TEXT_HTML.getMimeType()));
        assertFalse(handler.isMimeTypeSupported(
                ContentType.TEXT_PLAIN.getMimeType()));
        assertFalse(handler.isMimeTypeSupported("mock-mime-type"));
    }

    /**
     * Test for {@link JaxbXMLResponseHandler#getDefaultContentType()}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testGetDefaultContentType()
    throws Throwable {
        final JAXBContext mockContext = mock(JAXBContext.class);
        final JaxbXMLResponseHandler<TestBean> handler =
                createHandlerForContextAndType(mockContext, TestBean.class);
        final ContentType result = handler.getDefaultContentType();
        assertNotNull(result);
        assertEquals(ContentType.APPLICATION_XML.getMimeType(), result.getMimeType());
        assertNull(result.getCharset());
    }

    /**
     * Mock bean for testing.
     */
    protected static class TestBean {
        // No extra methods
    }
}
