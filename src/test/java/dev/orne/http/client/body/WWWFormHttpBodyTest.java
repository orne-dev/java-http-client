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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.tuple.Pair;
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
 * Unit tests for {@code WWWFormHttpBody}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see WWWFormHttpBody
 */
@Tag("ut")
class WWWFormHttpBodyTest
extends WWWFormHttpResponseBodyParserTest {

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
     * Test for {@link WWWFormHttpBody#produce(Collection, HttpRequest)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_Request()
    throws Throwable {
        final List<Pair<String, String>> entity = UrlEncodedUtilsTest.generatePairs();
        final String encoded = UrlEncodedUtils.format(
                WWWFormHttpResponseBodyParser.DEFAULT_CONTENT_TYPE.getCharset(),
                entity);
        assertThrows(NullPointerException.class, () ->
                WWWFormHttpBody.produce(entity, null));
        WWWFormHttpBody.produce(entity, request);
        then(request).should().setBody(
                WWWFormHttpResponseBodyParser.DEFAULT_CONTENT_TYPE,
                encoded);
        then(request).shouldHaveNoMoreInteractions();
        assertDoesNotThrow(() ->
                WWWFormHttpBody.produce((Collection<Pair<String, String>>) null, request));
    }

    /**
     * Test for {@link WWWFormHttpBody#produce(Map, HttpRequest)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_Map_Request()
    throws Throwable {
        final List<Pair<String, String>> pairs = UrlEncodedUtilsTest.generatePairs();
        final String encoded = UrlEncodedUtils.format(
                WWWFormHttpResponseBodyParser.DEFAULT_CONTENT_TYPE.getCharset(),
                pairs);
        final Map<String, String> entity = UrlEncodedUtilsTest.generatePairsMap(pairs);
        assertThrows(NullPointerException.class, () ->
                WWWFormHttpBody.produce(entity, null));
        WWWFormHttpBody.produce(entity, request);
        then(request).should().setBody(
                WWWFormHttpResponseBodyParser.DEFAULT_CONTENT_TYPE,
                encoded);
        then(request).shouldHaveNoMoreInteractions();
        assertDoesNotThrow(() ->
                WWWFormHttpBody.produce((Map<String, String>) null, request));
    }

    /**
     * Test for {@link WWWFormHttpBody#produce(Collection, HttpRequest, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_Content()
    throws Throwable {
        final Charset charset = UrlEncodedUtilsTest.randomEncodingCharset();
        final ContentType contentType = ContentType.of("test", charset);
        final List<Pair<String, String>> entity = UrlEncodedUtilsTest.generatePairs();
        final String encoded = UrlEncodedUtils.format(charset, entity);
        assertThrows(NullPointerException.class, () ->
                WWWFormHttpBody.produce(entity, null, contentType));
        assertThrows(NullPointerException.class, () ->
                WWWFormHttpBody.produce(entity, request, null));
        final ContentType noCharsetContentType = ContentType.of("test");
        assertThrows(NullPointerException.class, () ->
                WWWFormHttpBody.produce(entity, request, noCharsetContentType));
        WWWFormHttpBody.produce(entity, request, contentType);
        then(request).should().setBody(contentType, encoded);
        then(request).shouldHaveNoMoreInteractions();
        assertDoesNotThrow(() ->
                WWWFormHttpBody.produce((Collection<Pair<String, String>>) null, request, contentType));
    }

    /**
     * Test for {@link WWWFormHttpBody#produce(Map, HttpRequest, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testProduce_Map_Content()
    throws Throwable {
        final Charset charset = UrlEncodedUtilsTest.randomEncodingCharset();
        final ContentType contentType = ContentType.of("test", charset);
        final List<Pair<String, String>> pairs = UrlEncodedUtilsTest.generatePairs();
        final String encoded = UrlEncodedUtils.format(charset, pairs);
        final Map<String, String> entity = UrlEncodedUtilsTest.generatePairsMap(pairs);
        assertThrows(NullPointerException.class, () ->
                WWWFormHttpBody.produce(entity, null, contentType));
        assertThrows(NullPointerException.class, () ->
                WWWFormHttpBody.produce(entity, request, null));
        final ContentType noCharsetContentType = ContentType.of("test");
        assertThrows(NullPointerException.class, () ->
                WWWFormHttpBody.produce(entity, request, noCharsetContentType));
        WWWFormHttpBody.produce(entity, request, contentType);
        then(request).should().setBody(contentType, encoded);
        then(request).shouldHaveNoMoreInteractions();
        assertDoesNotThrow(() ->
                WWWFormHttpBody.produce((Map<String, String>) null, request, contentType));
    }

    /**
     * Test for {@link WWWFormHttpBody#parse(HttpResponseBody)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_Simple()
    throws Throwable {
        final Collection<Pair<String, String>> expected = UrlEncodedUtilsTest.generatePairs();
        given(body.parse(any())).willReturn(expected);
        assertThrows(NullPointerException.class, () -> {
            WWWFormHttpBody.parse(null);
        });
        final Collection<Pair<String, String>> result = WWWFormHttpBody.parse(body);
        assertSame(expected, result);
        then(body).should().parse(parserCaptor.capture());
        then(body).shouldHaveNoMoreInteractions();
        final WWWFormHttpBody.WWWFormBodyParser parser = assertInstanceOf(
                WWWFormHttpBody.WWWFormBodyParser.class,
                parserCaptor.getValue());
        assertSame(WWWFormHttpResponseBodyParser.DEFAULT_CONTENT_TYPE, parser.getDefaultContentType());
    }

    /**
     * Test for {@link WWWFormHttpBody#parse(HttpResponseBody, ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_DefaultContentType()
    throws Throwable {
        final Charset charset = UrlEncodedUtilsTest.randomEncodingCharset();
        final ContentType contentType = ContentType.of("test", charset);
        final Collection<Pair<String, String>> expected = UrlEncodedUtilsTest.generatePairs();
        given(body.parse(any())).willReturn(expected);
        assertThrows(NullPointerException.class, () -> {
            WWWFormHttpBody.parse(null, contentType);
        });
        assertThrows(NullPointerException.class, () -> {
            WWWFormHttpBody.parse(body, null);
        });
        final ContentType noCharsetContentType = ContentType.of("test");
        assertThrows(NullPointerException.class, () -> {
            WWWFormHttpBody.parse(body, noCharsetContentType);
        });
        final Collection<Pair<String, String>> result = WWWFormHttpBody.parse(body, contentType);
        assertSame(expected, result);
        then(body).should().parse(parserCaptor.capture());
        then(body).shouldHaveNoMoreInteractions();
        final WWWFormHttpBody.WWWFormBodyParser parser = assertInstanceOf(
                WWWFormHttpBody.WWWFormBodyParser.class,
                parserCaptor.getValue());
        assertSame(contentType, parser.getDefaultContentType());
    }

    /**
     * Test for {@link WWWFormHttpBody#parser()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParser()
    throws Throwable {
        final @NotNull WWWFormHttpResponseBodyParser parser =
                WWWFormHttpBody.parser();
        final WWWFormHttpBody.WWWFormBodyParser tparser = assertInstanceOf(
                WWWFormHttpBody.WWWFormBodyParser.class,
                parser);
        assertSame(WWWFormHttpResponseBodyParser.DEFAULT_CONTENT_TYPE, tparser.getDefaultContentType());
    }

    /**
     * Test for {@link WWWFormHttpBody#parser(ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParser_ContentType()
    throws Throwable {
        final ContentType contentType = ContentType.of(
                "test",
                UrlEncodedUtilsTest.randomEncodingCharset());
        final @NotNull WWWFormHttpResponseBodyParser parser =
                WWWFormHttpBody.parser(
                        contentType);
        final WWWFormHttpBody.WWWFormBodyParser tparser = assertInstanceOf(
                WWWFormHttpBody.WWWFormBodyParser.class,
                parser);
        assertSame(contentType, tparser.getDefaultContentType());
    }

    @Override
    protected @NotNull WWWFormHttpResponseBodyParser createParser() {
        return spy(new WWWFormHttpBody.WWWFormBodyParser(
                WWWFormHttpResponseBodyParser.DEFAULT_CONTENT_TYPE));
    }

    /**
     * Test for {@link WWWFormHttpBody.WWWFormBodyParser#WWWFormBodyParser(ContentType)}.
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
                new WWWFormHttpBody.WWWFormBodyParser(
                        null));
        assertThrows(NullPointerException.class, () ->
                new WWWFormHttpBody.WWWFormBodyParser(
                        noCharsetContentType));
        final WWWFormHttpBody.WWWFormBodyParser parser =
                new WWWFormHttpBody.WWWFormBodyParser(
                        contentType);
        assertSame(contentType, parser.getDefaultContentType());
    }

    /**
     * Test for {@link WWWFormHttpBody.WWWFormBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
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
        final WWWFormHttpBody.WWWFormBodyParser parser =
                new WWWFormHttpBody.WWWFormBodyParser(
                        defaultContentType);
        final List<Pair<String, String>> expected = UrlEncodedUtilsTest.generatePairs();
        final String encodedValue = UrlEncodedUtils.format(charset, expected);
        final byte[] bytes = encodedValue.getBytes(charset);
        try (final ByteArrayInputStream input = new ByteArrayInputStream(bytes)) {
            final Collection<Pair<String, String>> result = parser.parseSupportedContent(contentType, input, bytes.length);
            assertNotNull(result);
            assertEquals(expected, new ArrayList<>(result));
        }
    }

    /**
     * Test for {@link WWWFormHttpBody.WWWFormBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
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
        final WWWFormHttpBody.WWWFormBodyParser parser =
                new WWWFormHttpBody.WWWFormBodyParser(
                        defaultContentType);
        final List<Pair<String, String>> expected = UrlEncodedUtilsTest.generatePairs();
        final String encodedValue = UrlEncodedUtils.format(charset, expected);
        try (final ByteArrayInputStream input = new ByteArrayInputStream(encodedValue.getBytes(charset))) {
            final Collection<Pair<String, String>> result = parser.parseSupportedContent(contentType, input, -1);
            assertNotNull(result);
            assertEquals(expected, new ArrayList<>(result));
        }
    }

    /**
     * Test for {@link WWWFormHttpBody.WWWFormBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
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
        final WWWFormHttpBody.WWWFormBodyParser parser =
                new WWWFormHttpBody.WWWFormBodyParser(
                        defaultContentType);
        final List<Pair<String, String>> expected = UrlEncodedUtilsTest.generatePairs();
        final String encodedValue = UrlEncodedUtils.format(charset, expected);
        try (final ByteArrayInputStream input = new ByteArrayInputStream(encodedValue.getBytes(charset))) {
            final Collection<Pair<String, String>> result = parser.parseSupportedContent(contentType, input, -1);
            assertNotNull(result);
            assertEquals(expected, new ArrayList<>(result));
        }
    }

    /**
     * Test for {@link WWWFormHttpBody.WWWFormBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
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
        final WWWFormHttpBody.WWWFormBodyParser parser =
                spy(new WWWFormHttpBody.WWWFormBodyParser(
                        defaultContentType));
        final InputStream input = mock(InputStream.class);
        final IOException exception = new IOException();
        given(input.read(any())).willThrow(exception);
        assertThrows(HttpResponseBodyParsingException.class, () -> {
            parser.parseSupportedContent(contentType, input, -1);
        });
    }

    /**
     * Test for {@link WWWFormHttpBody.WWWFormBodyParser#parseSupportedContent(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParserParse_ParseException()
    throws Throwable {
        final Charset charset = StandardCharsets.UTF_8;
        final ContentType defaultContentType = ContentType.of(
                "default",
                UrlEncodedUtilsTest.randomEncodingCharset());
        final ContentType contentType = ContentType.of(
                "test",
                charset);
        final WWWFormHttpBody.WWWFormBodyParser parser =
                spy(new WWWFormHttpBody.WWWFormBodyParser(
                        defaultContentType));
        final String encodedValue = "some=invalid&value";
        try (final ByteArrayInputStream input = new ByteArrayInputStream(encodedValue.getBytes(charset))) {
            assertThrows(HttpResponseBodyParsingException.class, () -> {
                parser.parseSupportedContent(contentType, input, -1);
            });
        }
    }
}
