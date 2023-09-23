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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpResponseBodyParsingException;
import dev.orne.http.client.engine.HttpRequest;
import dev.orne.http.client.engine.HttpRequest.BodyProducer;
import dev.orne.http.client.engine.HttpResponseBody;
import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@code JacksonHttpBody}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see JacksonHttpBody
 */
@Tag("ut")
class JacksonHttpBodyTest
extends JsonHttpResponseBodyParserTest {

    private @Mock ObjectMapper mapper;
    private @Mock HttpRequest request;
    private @Mock OutputStream output;
    private @Mock HttpResponseBody body;
    private @Mock InputStream input;
    private @Captor ArgumentCaptor<HttpResponseBodyParser<?>> parserCaptor;
    private @Captor ArgumentCaptor<BodyProducer> bodyProducerCaptor;
    private AutoCloseable mocks;
    private ObjectMapper defaultMapperBackup;

    @BeforeEach public void backupDefaultMapper() {
        this.defaultMapperBackup = JacksonHttpBody.getDefaultMapper();
    }

    @BeforeEach public void openMocks() {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach public void releaseMocks() throws Exception {
        this.mocks.close();
    }

    @AfterEach public void restoreDefaultMapper() {
        JacksonHttpBody.setDefaultMapper(this.defaultMapperBackup);
    }

    /**
     * Test for {@link JacksonHttpBody#getDefaultMapper()} and
     * {@link JacksonHttpBody#setDefaultMapper(ObjectMapper)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testDefaultObjectMapper()
    throws Throwable {
        assertNotNull(JacksonHttpBody.getDefaultMapper());
        assertSame(this.defaultMapperBackup, JacksonHttpBody.getDefaultMapper());
        JacksonHttpBody.setDefaultMapper(mapper);
        assertSame(mapper, JacksonHttpBody.getDefaultMapper());
    }

    /**
     * Test for {@link JacksonHttpBody#produce(Object, HttpRequest)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_Request()
    throws Throwable {
        JacksonHttpBody.setDefaultMapper(mapper);
        final TestBean entity = mock(TestBean.class);
        assertThrows(NullPointerException.class, () ->
                JacksonHttpBody.produce(entity, null));
        JacksonHttpBody.produce(entity, request);
        then(request).should().setBody(
                eq(JsonHttpResponseBodyParser.DEFAULT_CONTENT_TYPE),
                bodyProducerCaptor.capture());
        then(request).shouldHaveNoMoreInteractions();
        then(mapper).shouldHaveNoInteractions();
        final BodyProducer producer = bodyProducerCaptor.getValue();
        willAnswer(invocation -> {
            final OutputStreamWriter writer = assertInstanceOf(
                    OutputStreamWriter.class,
                    invocation.getArgument(0));
            final Charset writerCharset = Charset.forName(writer.getEncoding());
            assertEquals(JsonHttpResponseBodyParser.DEFAULT_CONTENT_TYPE.getCharset(), writerCharset);
            return null;
        }).given(mapper).writeValue(any(Writer.class), eq(entity));
        producer.writeBody(output);
        then(mapper).should().writeValue(any(Writer.class), eq(entity));
        then(mapper).shouldHaveNoMoreInteractions();
        assertDoesNotThrow(() ->
                JacksonHttpBody.produce(null, request));
    }

    /**
     * Test for {@link JacksonHttpBody#produce(Object, HttpRequest, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_Content()
    throws Throwable {
        JacksonHttpBody.setDefaultMapper(mapper);
        final TestBean entity = mock(TestBean.class);
        final Charset charset = randomEncodingCharset();
        final ContentType contentType = ContentType.of("test", charset);
        assertThrows(NullPointerException.class, () ->
                JacksonHttpBody.produce(entity, null, contentType));
        assertThrows(NullPointerException.class, () ->
                JacksonHttpBody.produce(entity, request, null));
        final ContentType noCharsetContentType = ContentType.of("test");
        assertThrows(NullPointerException.class, () ->
                JacksonHttpBody.produce(entity, request, noCharsetContentType));
        JacksonHttpBody.produce(entity, request, contentType);
        then(request).should().setBody(eq(contentType), bodyProducerCaptor.capture());
        then(request).shouldHaveNoMoreInteractions();
        then(mapper).shouldHaveNoInteractions();
        final BodyProducer producer = bodyProducerCaptor.getValue();
        willAnswer(invocation -> {
            final OutputStreamWriter writer = assertInstanceOf(
                    OutputStreamWriter.class,
                    invocation.getArgument(0));
            final Charset writerCharset = Charset.forName(writer.getEncoding());
            assertEquals(charset, writerCharset);
            return null;
        }).given(mapper).writeValue(any(Writer.class), eq(entity));
        producer.writeBody(output);
        then(mapper).should().writeValue(any(Writer.class), eq(entity));
        then(mapper).shouldHaveNoMoreInteractions();
        assertDoesNotThrow(() ->
                JacksonHttpBody.produce(null, request, contentType));
    }

    /**
     * Test for {@link JacksonHttpBody#produce(Object, HttpRequest, ContentType, ObjectMapper)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_Mapper()
    throws Throwable {
        final TestBean entity = mock(TestBean.class);
        final Charset charset = randomEncodingCharset();
        final ContentType contentType = ContentType.of("test", charset);
        assertThrows(NullPointerException.class, () ->
                JacksonHttpBody.produce(entity, null, contentType, mapper));
        assertThrows(NullPointerException.class, () ->
                JacksonHttpBody.produce(entity, request, null, mapper));
        final ContentType noCharsetContentType = ContentType.of("test");
        assertThrows(NullPointerException.class, () ->
                JacksonHttpBody.produce(entity, request, noCharsetContentType, mapper));
        assertThrows(NullPointerException.class, () ->
                JacksonHttpBody.produce(entity, request, contentType, null));
        JacksonHttpBody.produce(entity, request, contentType, mapper);
        then(request).should().setBody(eq(contentType), bodyProducerCaptor.capture());
        then(request).shouldHaveNoMoreInteractions();
        then(mapper).shouldHaveNoInteractions();
        final BodyProducer producer = bodyProducerCaptor.getValue();
        willAnswer(invocation -> {
            final OutputStreamWriter writer = assertInstanceOf(
                    OutputStreamWriter.class,
                    invocation.getArgument(0));
            final Charset writerCharset = Charset.forName(writer.getEncoding());
            assertEquals(charset, writerCharset);
            return null;
        }).given(mapper).writeValue(any(Writer.class), eq(entity));
        producer.writeBody(output);
        then(mapper).should().writeValue(any(Writer.class), eq(entity));
        then(mapper).shouldHaveNoMoreInteractions();
        assertDoesNotThrow(() ->
                JacksonHttpBody.produce(null, request, contentType, mapper));
    }

    /**
     * Test for {@link JacksonHttpBody#parse(HttpResponseBody, Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_Simple()
    throws Throwable {
        JacksonHttpBody.setDefaultMapper(mapper);
        final TestBean expected = mock(TestBean.class);
        given(body.parse(any())).willReturn(expected);
        assertThrows(NullPointerException.class, () -> {
            JacksonHttpBody.parse(null, TestBean.class);
        });
        assertThrows(NullPointerException.class, () -> {
            JacksonHttpBody.parse(body, null);
        });
        final TestBean result = JacksonHttpBody.parse(body, TestBean.class);
        assertSame(expected, result);
        then(body).should().parse(parserCaptor.capture());
        then(body).shouldHaveNoMoreInteractions();
        final JacksonHttpBody.JacksonBodyParser<?> parser = assertInstanceOf(
                JacksonHttpBody.JacksonBodyParser.class,
                parserCaptor.getValue());
        assertSame(JsonHttpResponseBodyParser.DEFAULT_CONTENT_TYPE, parser.getDefaultContentType());
        assertSame(TestBean.class, parser.getEntityType());
        assertSame(mapper, parser.getMapper());
    }

    /**
     * Test for {@link JacksonHttpBody#parse(HttpResponseBody, Class, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_DefaultContentType()
    throws Throwable {
        JacksonHttpBody.setDefaultMapper(mapper);
        final Charset charset = Generators.randomValue(Charset.class);
        final ContentType contentType = ContentType.of("test", charset);
        final TestBean expected = mock(TestBean.class);
        given(body.parse(any())).willReturn(expected);
        assertThrows(NullPointerException.class, () -> {
            JacksonHttpBody.parse(null, TestBean.class, contentType);
        });
        assertThrows(NullPointerException.class, () -> {
            JacksonHttpBody.parse(body, null, contentType);
        });
        assertThrows(NullPointerException.class, () -> {
            JacksonHttpBody.parse(body, TestBean.class, null);
        });
        final ContentType noCharsetContentType = ContentType.of("test");
        assertThrows(NullPointerException.class, () -> {
            JacksonHttpBody.parse(body, TestBean.class, noCharsetContentType);
        });
        final TestBean result = JacksonHttpBody.parse(body, TestBean.class, contentType);
        assertSame(expected, result);
        then(body).should().parse(parserCaptor.capture());
        then(body).shouldHaveNoMoreInteractions();
        final JacksonHttpBody.JacksonBodyParser<?> parser = assertInstanceOf(
                JacksonHttpBody.JacksonBodyParser.class,
                parserCaptor.getValue());
        assertSame(contentType, parser.getDefaultContentType());
        assertSame(TestBean.class, parser.getEntityType());
        assertSame(mapper, parser.getMapper());
    }

    /**
     * Test for {@link JacksonHttpBody#parse(HttpResponseBody, Class, ContentType, ObjectMapper)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_Mapper()
    throws Throwable {
        final Charset charset = Generators.randomValue(Charset.class);
        final ContentType contentType = ContentType.of("test", charset);
        final TestBean expected = mock(TestBean.class);
        given(body.parse(any())).willReturn(expected);
        assertThrows(NullPointerException.class, () -> {
            JacksonHttpBody.parse(null, TestBean.class, contentType, mapper);
        });
        assertThrows(NullPointerException.class, () -> {
            JacksonHttpBody.parse(body, null, contentType, mapper);
        });
        assertThrows(NullPointerException.class, () -> {
            JacksonHttpBody.parse(body, TestBean.class, null, mapper);
        });
        final ContentType noCharsetContentType = ContentType.of("test");
        assertThrows(NullPointerException.class, () -> {
            JacksonHttpBody.parse(body, TestBean.class, noCharsetContentType, mapper);
        });
        assertThrows(NullPointerException.class, () -> {
            JacksonHttpBody.parse(body, TestBean.class, contentType, null);
        });
        final TestBean result = JacksonHttpBody.parse(body, TestBean.class, contentType, mapper);
        assertSame(expected, result);
        then(body).should().parse(parserCaptor.capture());
        then(body).shouldHaveNoMoreInteractions();
        final JacksonHttpBody.JacksonBodyParser<?> parser = assertInstanceOf(
                JacksonHttpBody.JacksonBodyParser.class,
                parserCaptor.getValue());
        assertSame(contentType, parser.getDefaultContentType());
        assertSame(TestBean.class, parser.getEntityType());
        assertSame(mapper, parser.getMapper());
    }

    /**
     * Test for {@link JacksonHttpBody#parser(Class)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParser()
    throws Throwable {
        JacksonHttpBody.setDefaultMapper(mapper);
        final @NotNull JsonHttpResponseBodyParser<TestBean> parser =
                JacksonHttpBody.parser(TestBean.class);
        final JacksonHttpBody.JacksonBodyParser<?> tparser = assertInstanceOf(
                JacksonHttpBody.JacksonBodyParser.class,
                parser);
        assertSame(JsonHttpResponseBodyParser.DEFAULT_CONTENT_TYPE, tparser.getDefaultContentType());
        assertSame(TestBean.class, tparser.getEntityType());
        assertSame(mapper, tparser.getMapper());
    }

    /**
     * Test for {@link JacksonHttpBody#parser(Class, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParser_ContentType()
    throws Throwable {
        JacksonHttpBody.setDefaultMapper(mapper);
        final ContentType contentType = ContentType.of(
                "test",
                Generators.randomValue(Charset.class));
        final @NotNull JsonHttpResponseBodyParser<TestBean> parser =
                JacksonHttpBody.parser(
                        TestBean.class,
                        contentType);
        final JacksonHttpBody.JacksonBodyParser<?> tparser = assertInstanceOf(
                JacksonHttpBody.JacksonBodyParser.class,
                parser);
        assertSame(contentType, tparser.getDefaultContentType());
        assertSame(TestBean.class, tparser.getEntityType());
        assertSame(mapper, tparser.getMapper());
    }

    /**
     * Test for {@link JacksonHttpBody#parser(Class, ContentType, ObjectMapper)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParser_Mapper()
    throws Throwable  {
        final ContentType contentType = ContentType.of(
                "test",
                Generators.randomValue(Charset.class));
        final @NotNull JsonHttpResponseBodyParser<TestBean> parser =
                JacksonHttpBody.parser(
                        TestBean.class,
                        contentType,
                        mapper);
        final JacksonHttpBody.JacksonBodyParser<?> tparser = assertInstanceOf(
                JacksonHttpBody.JacksonBodyParser.class,
                parser);
        assertSame(contentType, tparser.getDefaultContentType());
        assertSame(TestBean.class, tparser.getEntityType());
        assertSame(mapper, tparser.getMapper());
    }

    @Override
    protected @NotNull JsonHttpResponseBodyParser<?> createParser() {
        return spy(new JacksonHttpBody.JacksonBodyParser<>(
                TestBean.class,
                JsonHttpResponseBodyParser.DEFAULT_CONTENT_TYPE,
                mapper));
    }

    /**
     * Test for {@link JacksonHttpBody.JacksonBodyParser#JacksonBodyParser(ContentType, Class, JAXBContext)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserConstructor()
    throws Throwable {
        final ContentType contentType = ContentType.of(
                "test",
                Generators.randomValue(Charset.class));
        final ContentType noCharsetContentType = ContentType.of(
                "test");
        assertThrows(NullPointerException.class, () ->
                new JacksonHttpBody.JacksonBodyParser<>(
                        null,
                        XmlHttpResponseBodyParser.DEFAULT_CONTENT_TYPE,
                        mapper));
        assertThrows(NullPointerException.class, () ->
                new JacksonHttpBody.JacksonBodyParser<>(
                        TestBean.class,
                        null,
                        mapper));
        assertThrows(NullPointerException.class, () ->
                new JacksonHttpBody.JacksonBodyParser<>(
                        TestBean.class,
                        noCharsetContentType,
                        mapper));
        assertThrows(NullPointerException.class, () ->
                new JacksonHttpBody.JacksonBodyParser<>(
                        TestBean.class,
                        XmlHttpResponseBodyParser.DEFAULT_CONTENT_TYPE,
                        null));
        final JacksonHttpBody.JacksonBodyParser<TestBean> parser =
                new JacksonHttpBody.JacksonBodyParser<>(
                        TestBean.class,
                        contentType,
                        mapper);
        assertSame(contentType, parser.getDefaultContentType());
        assertSame(TestBean.class, parser.getEntityType());
        assertSame(mapper, parser.getMapper());
    }

    /**
     * Test for {@link JacksonHttpBody.JacksonBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserParse()
    throws Throwable {
        final ContentType defaultContentType = ContentType.of(
                "default",
                Generators.randomValue(Charset.class));
        final ContentType contentType = ContentType.of(
                "test",
                Generators.randomValue(Charset.class));
        final JacksonHttpBody.JacksonBodyParser<TestBean> parser =
                spy(new JacksonHttpBody.JacksonBodyParser<>(
                        TestBean.class,
                        defaultContentType,
                        mapper));
        final TestBean mockResult = mock(TestBean.class);
        given(mapper.readValue(any(Reader.class), eq(TestBean.class))).will(inv -> {
            inv.getArgument(1);
            final InputStreamReader reader = assertInstanceOf(
                    InputStreamReader.class,
                    inv.getArgument(0));
            final Charset readerCharset = Charset.forName(reader.getEncoding());
            assertEquals(contentType.getCharset(), readerCharset);
            return mockResult;
        });
        final TestBean result = parser.parseSupportedContent(contentType, input, -1);
        assertNotNull(result);
        assertSame(mockResult, result);
        then(mapper).should().readValue(any(Reader.class), eq(TestBean.class));
        then(mapper).shouldHaveNoMoreInteractions();
        then(input).should(atMost(1)).close();
        then(input).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link JacksonHttpBody.JacksonBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserParse_NoRequestContentTypeCharset()
    throws Throwable {
        final ContentType defaultContentType = ContentType.of(
                "default",
                Generators.randomValue(Charset.class));
        final ContentType contentType = ContentType.of(
                "test");
        final JacksonHttpBody.JacksonBodyParser<TestBean> parser =
                spy(new JacksonHttpBody.JacksonBodyParser<>(
                        TestBean.class,
                        defaultContentType,
                        mapper));
        final TestBean mockResult = mock(TestBean.class);
        given(mapper.readValue(any(Reader.class), eq(TestBean.class))).will(inv -> {
            inv.getArgument(1);
            final InputStreamReader reader = assertInstanceOf(
                    InputStreamReader.class,
                    inv.getArgument(0));
            final Charset readerCharset = Charset.forName(reader.getEncoding());
            assertEquals(defaultContentType.getCharset(), readerCharset);
            return mockResult;
        });
        final TestBean result = parser.parseSupportedContent(contentType, input, -1);
        assertNotNull(result);
        assertSame(mockResult, result);
        then(mapper).should().readValue(any(Reader.class), eq(TestBean.class));
        then(mapper).shouldHaveNoMoreInteractions();
        then(input).should(atMost(1)).close();
        then(input).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link JacksonHttpBody.JacksonBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserParse_JacksonException()
    throws Throwable {
        final ContentType defaultContentType = ContentType.of(
                "default",
                Generators.randomValue(Charset.class));
        final ContentType contentType = ContentType.of(
                "test",
                Generators.randomValue(Charset.class));
        final JacksonHttpBody.JacksonBodyParser<TestBean> parser =
                spy(new JacksonHttpBody.JacksonBodyParser<>(
                        TestBean.class,
                        defaultContentType,
                        mapper));
        final IOException exception = new IOException();
        given(mapper.readValue(any(Reader.class), eq(TestBean.class))).will(inv -> {
            inv.getArgument(1);
            final InputStreamReader reader = assertInstanceOf(
                    InputStreamReader.class,
                    inv.getArgument(0));
            final Charset readerCharset = Charset.forName(reader.getEncoding());
            assertEquals(contentType.getCharset(), readerCharset);
            throw exception;
        });
        final HttpResponseBodyParsingException result = assertThrows(HttpResponseBodyParsingException.class, () -> {
            parser.parseSupportedContent(contentType, input, -1);
        });
        assertNotNull(result);
        assertSame(exception, result.getCause());
        then(mapper).should().readValue(any(Reader.class), eq(TestBean.class));
        then(mapper).shouldHaveNoMoreInteractions();
        then(input).should(atMost(1)).close();
        then(input).shouldHaveNoMoreInteractions();
    }

    private static Charset randomEncodingCharset() {
        Charset candidate = Generators.randomValue(Charset.class);
        while (!candidate.canEncode()) {
            candidate = Generators.randomValue(Charset.class);
        }
        return candidate;
    }

    /**
     * Mock bean for testing.
     */
    protected static class TestBean {
        // No extra methods
    }
}
