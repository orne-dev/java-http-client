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
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.http.ContentType;
import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@code HttpResponseBodyParser}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see HttpResponseBodyParser
 */
@Tag("ut")
class HttpResponseBodyParserTest {

    /**
     * Creates a new parser instance.
     * 
     * @return The parser instance.
     */
    protected @NotNull HttpResponseBodyParser<?> createParser() {
        return spy(HttpResponseBodyParser.class);
    }

    /**
     * Test for {@link HttpResponseBodyParser#createReader(ContentType, InputStream)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateReader()
    throws Throwable {
        final Charset charset = Generators.randomValue(Charset.class);
        final InputStream input = mock(InputStream.class);
        final HttpResponseBodyParser<?> parser = createParser();
        final ContentType contentType = ContentType.of("test", charset);
        final ContentType noCharsetContentType = ContentType.of("test");
        assertThrows(NullPointerException.class, () ->
                parser.createReader(contentType, null));
        try (final InputStreamReader result = assertInstanceOf(
                InputStreamReader.class,
                parser.createReader(null, input))) {
            final Charset writerCharset = Charset.forName(result.getEncoding());
            assertEquals(StandardCharsets.UTF_8, writerCharset);
        }
        try (final InputStreamReader result = assertInstanceOf(
                InputStreamReader.class,
                parser.createReader(noCharsetContentType, input))) {
            final Charset writerCharset = Charset.forName(result.getEncoding());
            assertEquals(StandardCharsets.UTF_8, writerCharset);
        }
        try (final InputStreamReader result = assertInstanceOf(
                InputStreamReader.class,
                parser.createReader(contentType, input))) {
            final Charset writerCharset = Charset.forName(result.getEncoding());
            assertEquals(charset, writerCharset);
        }
    }
}
