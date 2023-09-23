package dev.orne.http.client.body;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2023 Orne Developments
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpRequestBodyGenerationException;
import dev.orne.http.client.HttpResponseBodyParsingException;
import dev.orne.http.client.engine.HttpRequest;
import dev.orne.http.client.engine.HttpResponseBody;
import dev.orne.http.client.engine.HttpRequest.BodyProducer;
import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@code JaxbHttpBody}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see JaxbHttpBody
 */
@Tag("ut")
class JaxbHttpBodyTest
extends XmlHttpResponseBodyParserTest {

    private static final String TEST_NS = "testns";
    private static final String TEST_ROOT = "testelem";

    private @Mock JAXBContext context;
    private @Mock HttpRequest request;
    private @Mock OutputStream output;
    private @Mock HttpResponseBody body;
    private @Mock InputStream input;
    private @Captor ArgumentCaptor<HttpResponseBodyParser<?>> parserCaptor;
    private @Captor ArgumentCaptor<BodyProducer> bodyProducerCaptor;
    private AutoCloseable mocks;

    @BeforeEach public void openMocks() {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach public void releaseMocks() throws Exception {
        this.mocks.close();
    }

    /**
     * Test for {@link JaxbHttpBody#produce(Object, HttpRequest)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_Request()
    throws Throwable {
        final TestBean entity = new TestBean();
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(null, request));
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(entity, null));
        JaxbHttpBody.produce(entity, request);
        then(request).should().setBody(
                eq(JaxbHttpBody.DEFAULT_OUPUT_CONTENT_TYPE),
                bodyProducerCaptor.capture());
        then(request).shouldHaveNoMoreInteractions();
        final BodyProducer producer = bodyProducerCaptor.getValue();
        try (final ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
            producer.writeBody(bytes);
            final byte[] result = bytes.toByteArray();
            assertTrue(result.length > 0);
            final String xml = new String(result, JaxbHttpBody.DEFAULT_OUPUT_CONTENT_TYPE.getCharset());
            assertNotNull(xml);
        }
    }

    /**
     * Test for {@link JaxbHttpBody#produce(Object, HttpRequest)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_Request_CreateContextError()
    throws Throwable {
        final BadTestBean entity = new BadTestBean();
        final HttpRequestBodyGenerationException result = assertThrows(HttpRequestBodyGenerationException.class, () -> {
            JaxbHttpBody.produce(entity, request);
        });
        assertInstanceOf(JAXBException.class, result.getCause());
        then(request).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link JaxbHttpBody#produce(Object, HttpRequest, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_ContentType()
    throws Throwable {
        final TestBean entity = new TestBean();
        final Charset charset = randomEncodingCharset();
        final ContentType contentType = ContentType.of("test", charset);
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(null, request, contentType));
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(entity, null, contentType));
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(entity, request, null));
        final ContentType noCharsetContentType = ContentType.of("test");
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(entity, request, noCharsetContentType));
        JaxbHttpBody.produce(entity, request, contentType);
        then(request).should().setBody(eq(contentType), bodyProducerCaptor.capture());
        then(request).shouldHaveNoMoreInteractions();
        final BodyProducer producer = bodyProducerCaptor.getValue();
        try (final ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
            producer.writeBody(bytes);
            final byte[] result = bytes.toByteArray();
            assertTrue(result.length > 0);
            final String xml = new String(result, charset);
            assertNotNull(xml);
        }
    }

    /**
     * Test for {@link JaxbHttpBody#produce(Object, HttpRequest, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_ContentType_CreateContextError()
    throws Throwable {
        final BadTestBean entity = new BadTestBean();
        final Charset charset = randomEncodingCharset();
        final ContentType contentType = ContentType.of("test", charset);
        final HttpRequestBodyGenerationException result = assertThrows(HttpRequestBodyGenerationException.class, () -> {
            JaxbHttpBody.produce(entity, request, contentType);
        });
        assertInstanceOf(JAXBException.class, result.getCause());
        then(request).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link JaxbHttpBody#produce(Object, HttpRequest, ContentType, JAXBContext)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_Context()
    throws Throwable {
        final TestBean entity = mock(TestBean.class);
        final Charset charset = randomEncodingCharset();
        final ContentType contentType = ContentType.of("test", charset);
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(null, request, contentType, context));
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(entity, null, contentType, context));
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(entity, request, null, context));
        final ContentType noCharsetContentType = ContentType.of("test");
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(entity, request, noCharsetContentType, context));
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(entity, request, contentType, null));
        final Marshaller marshaller = mock(Marshaller.class);
        given(context.createMarshaller()).willReturn(marshaller);
        JaxbHttpBody.produce(entity, request, contentType, context);
        then(request).should().setBody(eq(contentType), bodyProducerCaptor.capture());
        then(request).shouldHaveNoMoreInteractions();
        then(context).shouldHaveNoInteractions();
        final BodyProducer producer = bodyProducerCaptor.getValue();
        willAnswer(invocation -> {
            final OutputStreamWriter writer = assertInstanceOf(
                    OutputStreamWriter.class,
                    invocation.getArgument(1));
            final Charset writerCharset = Charset.forName(writer.getEncoding());
            assertEquals(charset, writerCharset);
            return null;
        }).given(marshaller).marshal(eq(entity), any(Writer.class));
        producer.writeBody(output);
        then(context).should().createMarshaller();
        then(context).shouldHaveNoMoreInteractions();
        then(marshaller).should().setProperty(
                Marshaller.JAXB_ENCODING,
                charset.name());
        then(marshaller).should().marshal(eq(entity), any(Writer.class));
        then(marshaller).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link JaxbHttpBody#produce(Object, HttpRequest, ContentType, JAXBContext)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_Context_MarshallError()
    throws Throwable {
        final TestBean entity = mock(TestBean.class);
        final Charset charset = randomEncodingCharset();
        final ContentType contentType = ContentType.of("test", charset);
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(null, request, contentType, context));
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(entity, null, contentType, context));
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(entity, request, null, context));
        final ContentType noCharsetContentType = ContentType.of("test");
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(entity, request, noCharsetContentType, context));
        assertThrows(NullPointerException.class, () ->
                JaxbHttpBody.produce(entity, request, contentType, null));
        final Marshaller marshaller = mock(Marshaller.class);
        given(context.createMarshaller()).willReturn(marshaller);
        JaxbHttpBody.produce(entity, request, contentType, context);
        then(request).should().setBody(eq(contentType), bodyProducerCaptor.capture());
        then(request).shouldHaveNoMoreInteractions();
        then(context).shouldHaveNoInteractions();
        final BodyProducer producer = bodyProducerCaptor.getValue();
        willAnswer(invocation -> {
            final OutputStreamWriter writer = assertInstanceOf(
                    OutputStreamWriter.class,
                    invocation.getArgument(1));
            final Charset writerCharset = Charset.forName(writer.getEncoding());
            assertEquals(charset, writerCharset);
            return null;
        }).given(marshaller).marshal(eq(entity), any(Writer.class));
        final JAXBException exception = new JAXBException("test");
        willThrow(exception).given(marshaller).marshal(eq(entity), any(Writer.class));
        final HttpRequestBodyGenerationException result = assertThrows(HttpRequestBodyGenerationException.class, () -> {
            producer.writeBody(output);
        });
        assertSame(exception, result.getCause());
        then(context).should().createMarshaller();
        then(context).shouldHaveNoMoreInteractions();
        then(marshaller).should().setProperty(
                Marshaller.JAXB_ENCODING,
                charset.name());
        then(marshaller).should().marshal(eq(entity), any(Writer.class));
        then(marshaller).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link JaxbHttpBody#parse(HttpResponseBody, Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_Simple()
    throws Throwable {
        final TestBean expected = mock(TestBean.class);
        given(body.parse(any())).willReturn(expected);
        assertThrows(NullPointerException.class, () -> {
            JaxbHttpBody.parse(null, TestBean.class);
        });
        assertThrows(NullPointerException.class, () -> {
            JaxbHttpBody.parse(body, null);
        });
        final TestBean result = JaxbHttpBody.parse(body, TestBean.class);
        assertSame(expected, result);
        then(body).should().parse(parserCaptor.capture());
        then(body).shouldHaveNoMoreInteractions();
        final JaxbHttpBody.JaxbBodyParser<?> parser = assertInstanceOf(
                JaxbHttpBody.JaxbBodyParser.class,
                parserCaptor.getValue());
        assertSame(XmlHttpResponseBodyParser.DEFAULT_CONTENT_TYPE, parser.getDefaultContentType());
        assertSame(TestBean.class, parser.getEntityType());
        final JAXBContext createdContext = parser.getContext();
        assertNotNull(createdContext);
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><testelem xmlns=\"testns\"/>";
        try (final StringReader reader = new StringReader(xml)) {
            final Object contextResult = createdContext.createUnmarshaller().unmarshal(reader);
            assertNotNull(contextResult);
            assertInstanceOf(TestBean.class, contextResult);
        }
    }

    /**
     * Test for {@link JaxbHttpBody#parse(HttpResponseBody, Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_Simple_CreateContextError()
    throws Throwable {
        final HttpResponseBodyParsingException result = assertThrows(HttpResponseBodyParsingException.class, () -> {
            JaxbHttpBody.parse(body, BadTestBean.class);
        });
        assertInstanceOf(JAXBException.class, result.getCause());
        then(body).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link JaxbHttpBody#parse(HttpResponseBody, Class, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_ContentType()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final TestBean expected = mock(TestBean.class);
        given(body.parse(any())).willReturn(expected);
        assertThrows(NullPointerException.class, () -> {
            JaxbHttpBody.parse(null, TestBean.class, contentType);
        });
        assertThrows(NullPointerException.class, () -> {
            JaxbHttpBody.parse(body, null, contentType);
        });
        assertThrows(NullPointerException.class, () -> {
            JaxbHttpBody.parse(body, TestBean.class, null);
        });
        final TestBean result = JaxbHttpBody.parse(body, TestBean.class, contentType);
        assertSame(expected, result);
        then(body).should().parse(parserCaptor.capture());
        then(body).shouldHaveNoMoreInteractions();
        final JaxbHttpBody.JaxbBodyParser<?> parser = assertInstanceOf(
                JaxbHttpBody.JaxbBodyParser.class,
                parserCaptor.getValue());
        assertSame(contentType, parser.getDefaultContentType());
        assertSame(TestBean.class, parser.getEntityType());
        final JAXBContext createdContext = parser.getContext();
        assertNotNull(createdContext);
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><testelem xmlns=\"testns\"/>";
        try (final StringReader reader = new StringReader(xml)) {
            final Object contextResult = createdContext.createUnmarshaller().unmarshal(reader);
            assertNotNull(contextResult);
            assertInstanceOf(TestBean.class, contextResult);
        }
    }

    /**
     * Test for {@link JaxbHttpBody#parse(HttpResponseBody, Class, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_ContentType_CreateContextError()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final HttpResponseBodyParsingException result = assertThrows(HttpResponseBodyParsingException.class, () -> {
            JaxbHttpBody.parse(body, BadTestBean.class, contentType);
        });
        assertInstanceOf(JAXBException.class, result.getCause());
        then(body).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link JaxbHttpBody#parse(HttpResponseBody, Class, ContentType, JAXBContext)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_Context()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final TestBean expected = mock(TestBean.class);
        given(body.parse(any())).willReturn(expected);
        assertThrows(NullPointerException.class, () -> {
            JaxbHttpBody.parse(null, TestBean.class, contentType, context);
        });
        assertThrows(NullPointerException.class, () -> {
            JaxbHttpBody.parse(body, null, contentType, context);
        });
        assertThrows(NullPointerException.class, () -> {
            JaxbHttpBody.parse(body, TestBean.class, null, context);
        });
        assertThrows(NullPointerException.class, () -> {
            JaxbHttpBody.parse(body, TestBean.class, contentType, null);
        });
        final TestBean result = JaxbHttpBody.parse(body, TestBean.class, contentType, context);
        assertSame(expected, result);
        then(body).should().parse(parserCaptor.capture());
        then(body).shouldHaveNoMoreInteractions();
        final JaxbHttpBody.JaxbBodyParser<?> parser = assertInstanceOf(
                JaxbHttpBody.JaxbBodyParser.class,
                parserCaptor.getValue());
        assertSame(contentType, parser.getDefaultContentType());
        assertSame(TestBean.class, parser.getEntityType());
        assertSame(context, parser.getContext());
    }

    /**
     * Test for {@link JaxbHttpBody#parser(Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParser()
    throws Throwable {
        final @NotNull XmlHttpResponseBodyParser<TestBean> parser =
                JaxbHttpBody.parser(TestBean.class);
        final JaxbHttpBody.JaxbBodyParser<?> tparser = assertInstanceOf(
                JaxbHttpBody.JaxbBodyParser.class,
                parser);
        assertSame(XmlHttpResponseBodyParser.DEFAULT_CONTENT_TYPE, tparser.getDefaultContentType());
        assertSame(TestBean.class, tparser.getEntityType());
        assertNotNull(tparser.getContext());
    }

    /**
     * Test for {@link JaxbHttpBody#parser(Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParser_CreateContextError()
    throws Throwable {
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            JaxbHttpBody.parser(BadTestBean.class);
        });
        assertInstanceOf(JAXBException.class, result.getCause());
        then(body).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link JaxbHttpBody#parser(Class, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParser_ContentType()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final @NotNull XmlHttpResponseBodyParser<TestBean> parser =
                JaxbHttpBody.parser(
                        TestBean.class,
                        contentType);
        final JaxbHttpBody.JaxbBodyParser<?> tparser = assertInstanceOf(
                JaxbHttpBody.JaxbBodyParser.class,
                parser);
        assertSame(contentType, tparser.getDefaultContentType());
        assertSame(TestBean.class, tparser.getEntityType());
        assertNotNull(tparser.getContext());
    }

    /**
     * Test for {@link JaxbHttpBody#parser(Class, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParser_ContentType_CreateContextError()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            JaxbHttpBody.parser(BadTestBean.class, contentType);
        });
        assertInstanceOf(JAXBException.class, result.getCause());
        then(body).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link JaxbHttpBody#parser(Class, ContentType, JAXBContext)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParser_Context()
    throws Throwable  {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final @NotNull XmlHttpResponseBodyParser<TestBean> parser =
                JaxbHttpBody.parser(
                        TestBean.class,
                        contentType,
                        context);
        final JaxbHttpBody.JaxbBodyParser<?> tparser = assertInstanceOf(
                JaxbHttpBody.JaxbBodyParser.class,
                parser);
        assertSame(contentType, tparser.getDefaultContentType());
        assertSame(TestBean.class, tparser.getEntityType());
        assertSame(context, tparser.getContext());
    }

    @Override
    protected @NotNull XmlHttpResponseBodyParser<?> createParser() {
        return spy(new JaxbHttpBody.JaxbBodyParser<>(
                TestBean.class,
                XmlHttpResponseBodyParser.DEFAULT_CONTENT_TYPE,
                context));
    }

    /**
     * Test for {@link JaxbHttpBody.JaxbBodyParser#JaxbBodyParser(ContentType, Class, JAXBContext)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserConstructor()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        assertThrows(NullPointerException.class, () ->
                new JaxbHttpBody.JaxbBodyParser<>(
                        TestBean.class,
                        null,
                        context));
        assertThrows(NullPointerException.class, () ->
                new JaxbHttpBody.JaxbBodyParser<>(
                        null,
                        XmlHttpResponseBodyParser.DEFAULT_CONTENT_TYPE,
                        context));
        assertThrows(NullPointerException.class, () ->
                new JaxbHttpBody.JaxbBodyParser<>(
                        TestBean.class,
                        XmlHttpResponseBodyParser.DEFAULT_CONTENT_TYPE,
                        null));
        final JaxbHttpBody.JaxbBodyParser<TestBean> parser =
                new JaxbHttpBody.JaxbBodyParser<>(
                        TestBean.class,
                        contentType,
                        context);
        assertSame(contentType, parser.getDefaultContentType());
        assertSame(TestBean.class, parser.getEntityType());
        assertSame(context, parser.getContext());
    }

    /**
     * Test for {@link JaxbHttpBody.JaxbBodyParser#createSource(InputStream, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserCreateSource()
    throws Throwable {
        final Charset charset = Generators.randomValue(Charset.class);
        final ContentType contentType = ContentType.of("test", charset);
        final JaxbHttpBody.JaxbBodyParser<TestBean> parser =
                new JaxbHttpBody.JaxbBodyParser<>(
                        TestBean.class,
                        contentType,
                        context);
        final StreamSource result = parser.createSource(input, contentType);
        assertNotNull(result);
        assertNull(result.getInputStream());
        assertNotNull(result.getReader());
        final InputStreamReader reader = assertInstanceOf(InputStreamReader.class, result.getReader());
        final Charset readerCharset = Charset.forName(reader.getEncoding());
        assertEquals(charset, readerCharset);
    }

    /**
     * Test for {@link JaxbHttpBody.JaxbBodyParser#createSource(InputStream, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserCreateSourceNoCharset()
    throws Throwable {
        final ContentType contentType = ContentType.of("test");
        final JaxbHttpBody.JaxbBodyParser<TestBean> parser =
                new JaxbHttpBody.JaxbBodyParser<>(
                        TestBean.class,
                        contentType,
                        context);
        final StreamSource result = parser.createSource(input, contentType);
        assertNotNull(result);
        assertNotNull(result.getInputStream());
        assertSame(input, result.getInputStream());
        assertNull(result.getReader());
    }

    /**
     * Test for {@link JaxbHttpBody.JaxbBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserParse()
    throws Throwable {
        final ContentType defaultContentType = Generators.randomValue(ContentType.class);
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final JaxbHttpBody.JaxbBodyParser<TestBean> parser =
                spy(new JaxbHttpBody.JaxbBodyParser<>(
                        TestBean.class,
                        defaultContentType,
                        context));
        final StreamSource source = mock(StreamSource.class);
        willReturn(source).given(parser).createSource(input, contentType);
        final Unmarshaller unmarshaller = mock(Unmarshaller.class);
        willReturn(unmarshaller).given(context).createUnmarshaller();
        @SuppressWarnings("unchecked")
        final JAXBElement<TestBean> jaxbElement = mock(JAXBElement.class);
        willReturn(jaxbElement).given(unmarshaller).unmarshal(source, TestBean.class);
        final TestBean mockResult = mock(TestBean.class);
        willReturn(mockResult).given(jaxbElement).getValue();
        final TestBean result = parser.parseSupportedContent(contentType, input, -1);
        assertNotNull(result);
        assertSame(mockResult, result);
        then(context).should().createUnmarshaller();
        then(context).shouldHaveNoMoreInteractions();
        then(parser).should().createSource(input, contentType);
        then(unmarshaller).should().unmarshal(source, TestBean.class);
        then(unmarshaller).shouldHaveNoMoreInteractions();
        then(jaxbElement).should().getValue();
        then(jaxbElement).shouldHaveNoMoreInteractions();
        then(input).shouldHaveNoInteractions();
        then(source).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link JaxbHttpBody.JaxbBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserParse_CreateUnmarshallerFail()
    throws Throwable {
        final ContentType defaultContentType = Generators.randomValue(ContentType.class);
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final JaxbHttpBody.JaxbBodyParser<TestBean> parser =
                spy(new JaxbHttpBody.JaxbBodyParser<>(
                        TestBean.class,
                        defaultContentType,
                        context));
        final StreamSource source = mock(StreamSource.class);
        willReturn(source).given(parser).createSource(input, contentType);
        final JAXBException exception = new JAXBException("mock");
        willThrow(exception).given(context).createUnmarshaller();
        final HttpResponseBodyParsingException result = assertThrows(HttpResponseBodyParsingException.class, () -> {
            parser.parseSupportedContent(contentType, input, -1);
        });
        assertNotNull(result);
        assertSame(exception, result.getCause());
        then(context).should().createUnmarshaller();
        then(context).shouldHaveNoMoreInteractions();
        then(parser).should(atMostOnce()).createSource(input, contentType);
        then(input).shouldHaveNoInteractions();
        then(source).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link JaxbHttpBody.JaxbBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserParse_UnmarshalError()
    throws Throwable {
        final ContentType defaultContentType = Generators.randomValue(ContentType.class);
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final JaxbHttpBody.JaxbBodyParser<TestBean> parser =
                spy(new JaxbHttpBody.JaxbBodyParser<>(
                        TestBean.class,
                        defaultContentType,
                        context));
        final StreamSource source = mock(StreamSource.class);
        willReturn(source).given(parser).createSource(input, contentType);
        final Unmarshaller unmarshaller = mock(Unmarshaller.class);
        willReturn(unmarshaller).given(context).createUnmarshaller();
        final JAXBException exception = new JAXBException("mock");
        willThrow(exception).given(unmarshaller).unmarshal(source, TestBean.class);
        final HttpResponseBodyParsingException result = assertThrows(HttpResponseBodyParsingException.class, () -> {
            parser.parseSupportedContent(contentType, input, -1);
        });
        assertNotNull(result);
        assertSame(exception, result.getCause());
        then(context).should().createUnmarshaller();
        then(context).shouldHaveNoMoreInteractions();
        then(parser).should().createSource(input, contentType);
        then(unmarshaller).should().unmarshal(source, TestBean.class);
        then(unmarshaller).shouldHaveNoMoreInteractions();
        then(input).shouldHaveNoInteractions();
        then(source).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link JaxbHttpBody.JaxbBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserParse_NullElement()
    throws Throwable {
        final ContentType defaultContentType = Generators.randomValue(ContentType.class);
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final JaxbHttpBody.JaxbBodyParser<TestBean> parser =
                spy(new JaxbHttpBody.JaxbBodyParser<>(
                        TestBean.class,
                        defaultContentType,
                        context));
        final StreamSource source = mock(StreamSource.class);
        willReturn(source).given(parser).createSource(input, contentType);
        final Unmarshaller unmarshaller = mock(Unmarshaller.class);
        willReturn(unmarshaller).given(context).createUnmarshaller();
        willReturn(null).given(unmarshaller).unmarshal(source, TestBean.class);
        final TestBean result = parser.parseSupportedContent(contentType, input, -1);
        assertNull(result);
        then(context).should().createUnmarshaller();
        then(context).shouldHaveNoMoreInteractions();
        then(parser).should().createSource(input, contentType);
        then(unmarshaller).should().unmarshal(source, TestBean.class);
        then(unmarshaller).shouldHaveNoMoreInteractions();
        then(input).shouldHaveNoInteractions();
        then(source).shouldHaveNoInteractions();
    }

    /**
     * Mock bean for testing.
     */
    @XmlRootElement(namespace = TEST_NS, name = TEST_ROOT)
    protected static class TestBean {
        // No extra methods
    }

    private static Charset randomEncodingCharset() {
        Charset candidate = Generators.randomValue(Charset.class);
        while (!candidate.canEncode()) {
            candidate = Generators.randomValue(Charset.class);
        }
        return candidate;
    }

    /**
     * Mock bean type for JAXB context creation exception testing.
     */
    @XmlRootElement(namespace = TEST_NS, name = TEST_ROOT)
    protected static class BadTestBean {
        @XmlElement(name="propA")
        private String propA;
        @XmlElement(name="propA")
        public String getPropA() {
            return propA;
        }
    }
}
