package dev.orne.http.client.engine;

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
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.stream.Stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpResponseHandlingException;
import dev.orne.http.client.body.HttpResponseBodyParser;
import dev.orne.test.rnd.Generators;
import sun.nio.cs.HistoricallyNamedCharset;

/**
 * Unit tests for {@code HttpResponseBody}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see HttpResponseBody
 */
@Tag("ut")
class HttpResponseBodyTest {

    /**
     * Test for {@link HttpResponseBody#getContentReader()}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource
    void testGetContentReader(
            final boolean hasType,
            final boolean hasCharset,
            final boolean hasContent)
    throws Throwable {
        final HttpResponseBody body = mock(HttpResponseBody.class);
        final InputStream content = mock(InputStream.class);
        if (hasContent) {
            given(body.getContent()).willReturn(content);
        }
        final ContentType contentType = mock(ContentType.class);
        final Charset charset = Generators.randomValue(Charset.class);
        if (hasType) {
            given(body.getContentType()).willReturn(contentType);
            if (hasCharset) {
                given(contentType.getCharset()).willReturn(charset);
            }
        }
        willCallRealMethod().given(body).getContentReader();
        final Reader result = body.getContentReader();
        if (hasContent) {
            assertNotNull(result);
            final InputStreamReader reader = assertInstanceOf(InputStreamReader.class, result);
            if (hasCharset) {
                final String encoding = charset instanceof HistoricallyNamedCharset
                        ? ((HistoricallyNamedCharset) charset).historicalName()
                        : charset.name();
                assertEquals(encoding, reader.getEncoding());
            } else {
                assertEquals("UTF8", reader.getEncoding());
            }
        } else {
            assertNull(result);
        }
        then(body).should().getContentReader();
        then(body).should().getContentType();
        then(body).should().getContent();
        then(body).shouldHaveNoMoreInteractions();
        then(content).shouldHaveNoInteractions();
        if (hasType) {
            then(contentType).should(atLeast(0)).getCharset();
            then(contentType).shouldHaveNoMoreInteractions();
        }
    }
    private static Stream<Arguments> testGetContentReader() {
        return Stream.of(
                Arguments.of(true, true, true),
                Arguments.of(true, false, true),
                Arguments.of(true, true, false),
                Arguments.of(true, false, false),
                Arguments.of(false, false, true),
                Arguments.of(false, false, false)
            );
    }

    /**
     * Test for {@link HttpResponseBody#parse(HttpResponseBodyParser)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource
    void testParse(
            final boolean hasType,
            final boolean hasContent,
            final boolean closeError)
    throws Throwable {
        final HttpResponseBody body = mock(HttpResponseBody.class);
        final InputStream content = mock(InputStream.class);
        final IOException closeException = new IOException();
        final long contentLength = Generators.randomValue(long.class);
        if (hasContent) {
            given(body.getContent()).willReturn(content);
            given(body.getContentLength()).willReturn(contentLength);
            if (closeError) {
                willThrow(closeException).given(content).close();
            }
        } else {
            given(body.getContentLength()).willReturn(0L);
        }
        final ContentType contentType = mock(ContentType.class);
        if (hasType) {
            given(body.getContentType()).willReturn(contentType);
        }
        @SuppressWarnings("unchecked")
        final HttpResponseBodyParser<Object> parser = mock(HttpResponseBodyParser.class);
        final Object entity = new Object();
        given(parser.parse(any(), any(), anyLong())).willReturn(entity);
        willCallRealMethod().given(body).parse(any());
        if (hasContent) {
            if (closeError) {
                final HttpResponseHandlingException result = assertThrows(
                        HttpResponseHandlingException.class,
                        () -> body.parse(parser));
                assertSame(closeException, result.getCause());
            } else {
                final Object result = body.parse(parser);
                assertSame(entity, result);
            }
        } else {
            final Object result = body.parse(parser);
            assertNull(result);
        }
        then(body).should().parse(parser);
        then(body).should().getContent();
        if (hasContent) {
            then(body).should().getContentType();
            then(body).should().getContentLength();
            if (hasType) {
                then(parser).should().parse(contentType, content, contentLength);
            } else {
                then(parser).should().parse(null, content, contentLength);
            }
            then(parser).shouldHaveNoMoreInteractions();
            then(content).should().close();
            then(content).shouldHaveNoMoreInteractions();
        } else {
            then(body).should(atMostOnce()).getContentType();
            then(body).should(atMostOnce()).getContentLength();
            then(parser).shouldHaveNoInteractions();
            then(content).shouldHaveNoInteractions();
        }
        then(body).shouldHaveNoMoreInteractions();
        then(contentType).shouldHaveNoInteractions();
    }
    private static Stream<Arguments> testParse() {
        return Stream.of(
                Arguments.of(true, true, false),
                Arguments.of(true, true, true),
                Arguments.of(true, false, false),
                Arguments.of(false, true, false),
                Arguments.of(false, true, true),
                Arguments.of(false, false, false)
            );
    }

    /**
     * Test for {@link HttpResponseBody#discard()}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource
    void testDiscard(
            final boolean hasContent,
            final boolean throwError)
    throws Throwable {
        final InputStream content = mock(InputStream.class);
        final IOException closeException = new IOException();
        if (throwError) {
            willThrow(closeException).given(content).close();
        }
        final HttpResponseBody body = mock(HttpResponseBody.class);
        if (hasContent) {
            given(body.getContent()).willReturn(content);
        }
        willCallRealMethod().given(body).discard();
        if (!hasContent) {
            assertDoesNotThrow(() -> body.discard());
            then(body).should().discard();
            then(body).should().getContent();
            then(body).shouldHaveNoMoreInteractions();
        } else if (throwError) {
            final HttpResponseHandlingException result = assertThrows(
                    HttpResponseHandlingException.class,
                    () -> body.discard());
            assertSame(closeException, result.getCause());
            then(body).should().discard();
            then(body).should().getContent();
            then(body).shouldHaveNoMoreInteractions();
            then(content).should().close();
            then(content).shouldHaveNoMoreInteractions();
        } else {
            assertDoesNotThrow(() -> body.discard());
            then(body).should().discard();
            then(body).should().getContent();
            then(body).shouldHaveNoMoreInteractions();
            then(content).should().close();
            then(content).shouldHaveNoMoreInteractions();
        }
    }
    private static Stream<Arguments> testDiscard() {
        return Stream.of(
                Arguments.of(true, true),
                Arguments.of(true, false),
                Arguments.of(false, true),
                Arguments.of(false, false)
            );
    }
}
