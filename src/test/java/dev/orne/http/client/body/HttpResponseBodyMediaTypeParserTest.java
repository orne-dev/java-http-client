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

import java.io.InputStream;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.http.ContentType;
import dev.orne.http.client.UnsupportedContentTypeException;
import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@code HttpResponseBodyMediaTypeParser}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see HttpResponseBodyMediaTypeParser
 */
@Tag("ut")
class HttpResponseBodyMediaTypeParserTest
extends HttpResponseBodyParserTest {

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull HttpResponseBodyMediaTypeParser<?> createParser() {
        return spy(HttpResponseBodyMediaTypeParser.class);
    }

    /**
     * Test for {@link HttpResponseBodyMediaTypeParser#parse(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final ContentType defaultContentType = Generators.randomValue(ContentType.class);
        final InputStream input = mock(InputStream.class);
        final long contentLength = RandomUtils.nextLong();
        final HttpResponseBodyMediaTypeParser<?> parser = createParser();
        willReturn(defaultContentType).given(parser).getDefaultContentType();
        willReturn(true).given(parser).supportsMediaType(contentType.getMediaType());
        willReturn(null).given(parser).parseSupportedContent(contentType, input, contentLength);
        parser.parse(contentType, input, contentLength);
        then(parser).should().parse(contentType, input, contentLength);
        then(parser).should(atMostOnce()).getDefaultContentType();
        then(parser).should().supportsMediaType(contentType.getMediaType());
        then(parser).should().parseSupportedContent(contentType, input, contentLength);
        then(parser).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link HttpResponseBodyMediaTypeParser#parse(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_NullContent()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final long contentLength = RandomUtils.nextLong();
        final HttpResponseBodyMediaTypeParser<?> parser = createParser();
        assertThrows(NullPointerException.class, () ->
                parser.parse(contentType, null, contentLength));
    }

    /**
     * Test for {@link HttpResponseBodyMediaTypeParser#parse(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_NoContentType()
    throws Throwable {
        final ContentType defaultContentType = Generators.randomValue(ContentType.class);
        final InputStream input = mock(InputStream.class);
        final long contentLength = RandomUtils.nextLong();
        final HttpResponseBodyMediaTypeParser<?> parser = createParser();
        willReturn(defaultContentType).given(parser).getDefaultContentType();
        willReturn(true).given(parser).supportsMediaType(defaultContentType.getMediaType());
        willReturn(null).given(parser).parseSupportedContent(defaultContentType, input, contentLength);
        parser.parse(null, input, contentLength);
        then(parser).should().parse(null, input, contentLength);
        then(parser).should(atMostOnce()).getDefaultContentType();
        then(parser).should().supportsMediaType(defaultContentType.getMediaType());
        then(parser).should().parseSupportedContent(defaultContentType, input, contentLength);
        then(parser).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link HttpResponseBodyMediaTypeParser#parse(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_Unsupported()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final ContentType defaultContentType = Generators.randomValue(ContentType.class);
        final InputStream input = mock(InputStream.class);
        final long contentLength = RandomUtils.nextLong();
        final HttpResponseBodyMediaTypeParser<?> parser = createParser();
        willReturn(defaultContentType).given(parser).getDefaultContentType();
        willReturn(false).given(parser).supportsMediaType(contentType.getMediaType());
        willReturn(null).given(parser).parseSupportedContent(contentType, input, contentLength);
        willReturn(null).given(parser).parseSupportedContent(defaultContentType, input, contentLength);
        assertThrows(UnsupportedContentTypeException.class, () -> {
            parser.parse(contentType, input, contentLength);
        });
        then(parser).should().parse(contentType, input, contentLength);
        then(parser).should(atMostOnce()).getDefaultContentType();
        then(parser).should().supportsMediaType(contentType.getMediaType());
        then(parser).shouldHaveNoMoreInteractions();
    }
}
