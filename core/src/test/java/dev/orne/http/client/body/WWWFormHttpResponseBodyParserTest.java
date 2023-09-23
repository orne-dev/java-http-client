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

import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import dev.orne.http.ContentType;
import dev.orne.http.MediaTypes;

/**
 * Unit tests for {@code WWWFormHttpResponseBodyParser}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see WWWFormHttpResponseBodyParser
 */
@Tag("ut")
class WWWFormHttpResponseBodyParserTest
extends HttpResponseBodyMediaTypeParserTest {

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull WWWFormHttpResponseBodyParser createParser() {
        return spy(WWWFormHttpResponseBodyParser.class);
    }

    /**
     * Test for {@link WWWFormHttpResponseBodyParser#getDefaultContentType()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetDefaultContentType()
    throws Throwable {
        final WWWFormHttpResponseBodyParser parser = createParser();
        final ContentType result = parser.getDefaultContentType();
        assertNotNull(result);
        assertEquals(MediaTypes.Application.X_WWW_FORM_URLENCODED, result.getMediaType());
        assertEquals(StandardCharsets.UTF_8, result.getCharset());
    }

    /**
     * Test for {@link WWWFormHttpResponseBodyParser#supportsMediaType(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSupportsMediaType()
    throws Throwable {
        final WWWFormHttpResponseBodyParser parser = createParser();
        assertTrue(parser.supportsMediaType(MediaTypes.Application.X_WWW_FORM_URLENCODED));
    }

    /**
     * Test for {@link WWWFormHttpResponseBodyParser#supportsMediaType(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownApplicationMediaTypes")
    void testSupportsApplicationMediaType(
            final String mediaType)
    throws Throwable {
        final boolean expected = MediaTypes.Application.X_WWW_FORM_URLENCODED.equals(mediaType);
        final WWWFormHttpResponseBodyParser parser = createParser();
        assertEquals(expected, parser.supportsMediaType(mediaType));
    }
    private static Stream<Arguments> knownApplicationMediaTypes() {
        return Stream.of(MediaTypes.Application.class.getDeclaredFields())
                .filter(field -> {
                    return String.class.equals(field.getType())
                            && Modifier.isStatic(field.getModifiers())
                            && Modifier.isFinal(field.getModifiers());
                })
                .map(field -> {
                    try {
                        return String.valueOf(field.get(null));
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(value -> Arguments.of(value));
    }

    /**
     * Test for {@link WWWFormHttpResponseBodyParser#supportsMediaType(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownAudioMediaTypes")
    void testSupportsAudioMediaType(
            final String mediaType)
    throws Throwable {
        final WWWFormHttpResponseBodyParser parser = createParser();
        assertFalse(parser.supportsMediaType(mediaType));
    }
    private static Stream<Arguments> knownAudioMediaTypes() {
        return Stream.of(MediaTypes.Audio.class.getDeclaredFields())
                .filter(field -> {
                    return String.class.equals(field.getType())
                            && Modifier.isStatic(field.getModifiers())
                            && Modifier.isFinal(field.getModifiers());
                })
                .map(field -> {
                    try {
                        return String.valueOf(field.get(null));
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(value -> Arguments.of(value));
    }

    /**
     * Test for {@link WWWFormHttpResponseBodyParser#supportsMediaType(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownFontMediaTypes")
    void testSupportsFontMediaType(
            final String mediaType)
    throws Throwable {
        final WWWFormHttpResponseBodyParser parser = createParser();
        assertFalse(parser.supportsMediaType(mediaType));
    }
    private static Stream<Arguments> knownFontMediaTypes() {
        return Stream.of(MediaTypes.Font.class.getDeclaredFields())
                .filter(field -> {
                    return String.class.equals(field.getType())
                            && Modifier.isStatic(field.getModifiers())
                            && Modifier.isFinal(field.getModifiers());
                })
                .map(field -> {
                    try {
                        return String.valueOf(field.get(null));
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(value -> Arguments.of(value));
    }

    /**
     * Test for {@link WWWFormHttpResponseBodyParser#supportsMediaType(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownImageMediaTypes")
    void testSupportsImageMediaType(
            final String mediaType)
    throws Throwable {
        final WWWFormHttpResponseBodyParser parser = createParser();
        assertFalse(parser.supportsMediaType(mediaType));
    }
    private static Stream<Arguments> knownImageMediaTypes() {
        return Stream.of(MediaTypes.Image.class.getDeclaredFields())
                .filter(field -> {
                    return String.class.equals(field.getType())
                            && Modifier.isStatic(field.getModifiers())
                            && Modifier.isFinal(field.getModifiers());
                })
                .map(field -> {
                    try {
                        return String.valueOf(field.get(null));
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(value -> Arguments.of(value));
    }

    /**
     * Test for {@link WWWFormHttpResponseBodyParser#supportsMediaType(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownMultipartMediaTypes")
    void testSupportsMultipartMediaType(
            final String mediaType)
    throws Throwable {
        final WWWFormHttpResponseBodyParser parser = createParser();
        assertFalse(parser.supportsMediaType(mediaType));
    }
    private static Stream<Arguments> knownMultipartMediaTypes() {
        return Stream.of(MediaTypes.Multipart.class.getDeclaredFields())
                .filter(field -> {
                    return String.class.equals(field.getType())
                            && Modifier.isStatic(field.getModifiers())
                            && Modifier.isFinal(field.getModifiers());
                })
                .map(field -> {
                    try {
                        return String.valueOf(field.get(null));
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(value -> Arguments.of(value));
    }

    /**
     * Test for {@link WWWFormHttpResponseBodyParser#supportsMediaType(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownTextMediaTypes")
    void testSupportsTextMediaType(
            final String mediaType)
    throws Throwable {
        final WWWFormHttpResponseBodyParser parser = createParser();
        assertFalse(parser.supportsMediaType(mediaType));
    }
    private static Stream<Arguments> knownTextMediaTypes() {
        return Stream.of(MediaTypes.Text.class.getDeclaredFields())
                .filter(field -> {
                    return String.class.equals(field.getType())
                            && Modifier.isStatic(field.getModifiers())
                            && Modifier.isFinal(field.getModifiers());
                })
                .map(field -> {
                    try {
                        return String.valueOf(field.get(null));
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(value -> Arguments.of(value));
    }

    /**
     * Test for {@link WWWFormHttpResponseBodyParser#supportsMediaType(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownVideoMediaTypes")
    void testSupportsVideoMediaType(
            final String mediaType)
    throws Throwable {
        final WWWFormHttpResponseBodyParser parser = createParser();
        assertFalse(parser.supportsMediaType(mediaType));
    }
    private static Stream<Arguments> knownVideoMediaTypes() {
        return Stream.of(MediaTypes.Video.class.getDeclaredFields())
                .filter(field -> {
                    return String.class.equals(field.getType())
                            && Modifier.isStatic(field.getModifiers())
                            && Modifier.isFinal(field.getModifiers());
                })
                .map(field -> {
                    try {
                        return String.valueOf(field.get(null));
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(value -> Arguments.of(value));
    }
}
