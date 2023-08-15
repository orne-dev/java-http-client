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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpResponseBodyParsingException;
import dev.orne.http.client.engine.HttpRequest;
import dev.orne.http.client.engine.HttpRequest.BodyProducer;
import dev.orne.http.client.engine.HttpResponseBody;

/**
 * Unit tests for {@code StringHttpBody}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see StringHttpBody
 */
@Tag("ut")
class StringHttpBodyTest
extends StringHttpResponseBodyParserTest {

    private @Mock HttpRequest request;
    private @Mock OutputStream output;
    private @Mock HttpResponseBody body;
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
     * Test for {@link StringHttpBody#produce(Object, HttpRequest)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_Request()
    throws Throwable {
        final String entity = RandomStringUtils.random(20);
        assertThrows(NullPointerException.class, () ->
                StringHttpBody.produce(entity, null));
        StringHttpBody.produce(entity, request);
        then(request).should().setBody(
                StringHttpResponseBodyParser.DEFAULT_CONTENT_TYPE,
                entity);
        then(request).shouldHaveNoMoreInteractions();
        assertDoesNotThrow(() ->
                StringHttpBody.produce(null, request));
    }

    /**
     * Test for {@link StringHttpBody#produce(Object, HttpRequest, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_Content()
    throws Throwable {
        final Charset charset = UrlEncodedUtilsTest.randomEncodingCharset();
        final ContentType contentType = ContentType.of("test", charset);
        final String entity = RandomStringUtils.random(20);
        assertThrows(NullPointerException.class, () ->
                StringHttpBody.produce(entity, null, contentType));
        assertThrows(NullPointerException.class, () ->
                StringHttpBody.produce(entity, request, null));
        final ContentType noCharsetContentType = ContentType.of("test");
        assertThrows(NullPointerException.class, () ->
                StringHttpBody.produce(entity, request, noCharsetContentType));
        StringHttpBody.produce(entity, request, contentType);
        then(request).should().setBody(contentType, entity);
        then(request).shouldHaveNoMoreInteractions();
        assertDoesNotThrow(() ->
                StringHttpBody.produce(null, request, contentType));
    }

    /**
     * Test for {@link StringHttpBody#parse(HttpResponseBody)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_Simple()
    throws Throwable {
        final String expected = RandomStringUtils.random(20);
        given(body.parse(any())).willReturn(expected);
        assertThrows(NullPointerException.class, () -> {
            StringHttpBody.parse(null);
        });
        final String result = StringHttpBody.parse(body);
        assertSame(expected, result);
        then(body).should().parse(parserCaptor.capture());
        then(body).shouldHaveNoMoreInteractions();
        final StringHttpBody.StringBodyParser parser = assertInstanceOf(
                StringHttpBody.StringBodyParser.class,
                parserCaptor.getValue());
        assertSame(StringHttpResponseBodyParser.DEFAULT_CONTENT_TYPE, parser.getDefaultContentType());
    }

    /**
     * Test for {@link StringHttpBody#parse(HttpResponseBody, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_DefaultContentType()
    throws Throwable {
        final Charset charset = UrlEncodedUtilsTest.randomEncodingCharset();
        final ContentType contentType = ContentType.of("test", charset);
        final String expected = RandomStringUtils.random(20);
        given(body.parse(any())).willReturn(expected);
        assertThrows(NullPointerException.class, () -> {
            StringHttpBody.parse(null, contentType);
        });
        assertThrows(NullPointerException.class, () -> {
            StringHttpBody.parse(body, null);
        });
        final ContentType noCharsetContentType = ContentType.of("test");
        assertThrows(NullPointerException.class, () -> {
            StringHttpBody.parse(body, noCharsetContentType);
        });
        final String result = StringHttpBody.parse(body, contentType);
        assertSame(expected, result);
        then(body).should().parse(parserCaptor.capture());
        then(body).shouldHaveNoMoreInteractions();
        final StringHttpBody.StringBodyParser parser = assertInstanceOf(
                StringHttpBody.StringBodyParser.class,
                parserCaptor.getValue());
        assertSame(contentType, parser.getDefaultContentType());
    }

    /**
     * Test for {@link StringHttpBody#parser()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParser()
    throws Throwable {
        final @NotNull StringHttpResponseBodyParser parser =
                StringHttpBody.parser();
        final StringHttpBody.StringBodyParser tparser = assertInstanceOf(
                StringHttpBody.StringBodyParser.class,
                parser);
        assertSame(StringHttpResponseBodyParser.DEFAULT_CONTENT_TYPE, tparser.getDefaultContentType());
    }

    /**
     * Test for {@link StringHttpBody#parser(ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParser_ContentType()
    throws Throwable {
        final ContentType contentType = ContentType.of(
                "test",
                UrlEncodedUtilsTest.randomEncodingCharset());
        final @NotNull StringHttpResponseBodyParser parser =
                StringHttpBody.parser(
                        contentType);
        final StringHttpBody.StringBodyParser tparser = assertInstanceOf(
                StringHttpBody.StringBodyParser.class,
                parser);
        assertSame(contentType, tparser.getDefaultContentType());
    }

    @Override
    protected @NotNull StringHttpResponseBodyParser createParser() {
        return spy(new StringHttpBody.StringBodyParser(
                StringHttpResponseBodyParser.DEFAULT_CONTENT_TYPE));
    }

    /**
     * Test for {@link StringHttpBody.StringBodyParser#StringBodyParser(ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserConstructor()
    throws Throwable {
        final ContentType contentType = ContentType.of(
                "test",
                UrlEncodedUtilsTest.randomEncodingCharset());
        final ContentType noCharsetContentType = ContentType.of(
                "test");
        assertThrows(NullPointerException.class, () ->
                new StringHttpBody.StringBodyParser(
                        null));
        assertThrows(NullPointerException.class, () ->
                new StringHttpBody.StringBodyParser(
                        noCharsetContentType));
        final StringHttpBody.StringBodyParser parser =
                new StringHttpBody.StringBodyParser(
                        contentType);
        assertSame(contentType, parser.getDefaultContentType());
    }

    /**
     * Test for {@link StringHttpBody.StringBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserParse()
    throws Throwable {
        final Charset charset = StandardCharsets.UTF_8;
        final ContentType defaultContentType = ContentType.of(
                "default",
                UrlEncodedUtilsTest.randomEncodingCharset());
        final ContentType contentType = ContentType.of(
                "test",
                charset);
        final StringHttpBody.StringBodyParser parser =
                new StringHttpBody.StringBodyParser(
                        defaultContentType);
        final String expected = RandomStringUtils.random(20);
        final byte[] bytes = expected.getBytes(charset);
        try (final ByteArrayInputStream input = new ByteArrayInputStream(bytes)) {
            final String result = parser.parseSupportedContent(contentType, input, bytes.length);
            assertNotNull(result);
            assertEquals(expected, result);
        }
    }

    /**
     * Test for {@link StringHttpBody.StringBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserParse_UnknownLength()
    throws Throwable {
        final Charset charset = StandardCharsets.UTF_8;
        final ContentType defaultContentType = ContentType.of(
                "default",
                UrlEncodedUtilsTest.randomEncodingCharset());
        final ContentType contentType = ContentType.of(
                "test",
                charset);
        final StringHttpBody.StringBodyParser parser =
                new StringHttpBody.StringBodyParser(
                        defaultContentType);
        final String expected = RandomStringUtils.random(20);
        final byte[] bytes = expected.getBytes(charset);
        try (final ByteArrayInputStream input = new ByteArrayInputStream(bytes)) {
            final String result = parser.parseSupportedContent(contentType, input, -1);
            assertNotNull(result);
            assertEquals(expected, result);
        }
    }

    /**
     * Test for {@link StringHttpBody.StringBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserParse_NoRequestContentTypeCharset()
    throws Throwable {
        final Charset charset = StandardCharsets.UTF_8;
        final ContentType defaultContentType = ContentType.of(
                "default",
                charset);
        final ContentType contentType = ContentType.of(
                "test");
        final StringHttpBody.StringBodyParser parser =
                new StringHttpBody.StringBodyParser(
                        defaultContentType);
        final String expected = RandomStringUtils.random(20);
        final byte[] bytes = expected.getBytes(charset);
        try (final ByteArrayInputStream input = new ByteArrayInputStream(bytes)) {
            final String result = parser.parseSupportedContent(contentType, input, -1);
            assertNotNull(result);
            assertEquals(expected, result);
        }
    }

    /**
     * Test for {@link StringHttpBody.StringBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserParse_IOException()
    throws Throwable {
        final Charset charset = StandardCharsets.UTF_8;
        final ContentType defaultContentType = ContentType.of(
                "default",
                UrlEncodedUtilsTest.randomEncodingCharset());
        final ContentType contentType = ContentType.of(
                "test",
                charset);
        final StringHttpBody.StringBodyParser parser =
                spy(new StringHttpBody.StringBodyParser(
                        defaultContentType));
        final InputStream input = mock(InputStream.class);
        final IOException exception = new IOException();
        given(input.read(any())).willThrow(exception);
        assertThrows(HttpResponseBodyParsingException.class, () -> {
            parser.parseSupportedContent(contentType, input, -1);
        });
    }
}
