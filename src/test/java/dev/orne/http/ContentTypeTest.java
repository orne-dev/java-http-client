package dev.orne.http;

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

import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@code ContentType}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see ContentType
 */
@Tag("ut")
class ContentTypeTest {

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testOfMediaType()
    throws Throwable {
        assertThrows(NullPointerException.class, () -> ContentType.of(null));
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownApplicationMediaTypes")
    void testOfApplicationMediaType(
            final String mediaType)
    throws Throwable {
        final ContentType result = ContentType.of(mediaType);
        assertEquals(mediaType, result.getMediaType());
        assertNull(result.getCharset());
        assertNull(result.getBoundary());
        assertTrue(result.getParameters().isEmpty());
        assertEquals(mediaType, result.getHeader());
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownAudioMediaTypes")
    void testOfAudioMediaType(
            final String mediaType)
    throws Throwable {
        final ContentType result = ContentType.of(mediaType);
        assertEquals(mediaType, result.getMediaType());
        assertNull(result.getCharset());
        assertNull(result.getBoundary());
        assertTrue(result.getParameters().isEmpty());
        assertEquals(mediaType, result.getHeader());
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownFontMediaTypes")
    void testOfFontMediaType(
            final String mediaType)
    throws Throwable {
        final ContentType result = ContentType.of(mediaType);
        assertEquals(mediaType, result.getMediaType());
        assertNull(result.getCharset());
        assertNull(result.getBoundary());
        assertTrue(result.getParameters().isEmpty());
        assertEquals(mediaType, result.getHeader());
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownImageMediaTypes")
    void testOfImageMediaType(
            final String mediaType)
    throws Throwable {
        final ContentType result = ContentType.of(mediaType);
        assertEquals(mediaType, result.getMediaType());
        assertNull(result.getCharset());
        assertNull(result.getBoundary());
        assertTrue(result.getParameters().isEmpty());
        assertEquals(mediaType, result.getHeader());
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownMultipartMediaTypes")
    void testOfMultipartMediaType(
            final String mediaType)
    throws Throwable {
        assertThrows(IllegalArgumentException.class, () -> ContentType.of(mediaType));
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownTextMediaTypes")
    void testOfTextMediaType(
            final String mediaType)
    throws Throwable {
        assertThrows(IllegalArgumentException.class, () -> ContentType.of(mediaType));
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownVideoMediaTypes")
    void testOfVideoMediaType(
            final String mediaType)
    throws Throwable {
        final ContentType result = ContentType.of(mediaType);
        assertEquals(mediaType, result.getMediaType());
        assertNull(result.getCharset());
        assertNull(result.getBoundary());
        assertTrue(result.getParameters().isEmpty());
        assertEquals(mediaType, result.getHeader());
    }

    /**
     * Test for {@link ContentType#of(String, Charset)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testOfMediaType_Charset()
    throws Throwable {
        final String mediaType = MediaTypes.Text.PLAIN;
        final Charset charset = Generators.randomValue(Charset.class);
        assertThrows(NullPointerException.class, () -> ContentType.of(mediaType, null));
        assertThrows(NullPointerException.class, () -> ContentType.of(null, charset));
        assertThrows(NullPointerException.class, () -> ContentType.of(null, null));
    }

    /**
     * Test for {@link ContentType#of(String, Charset)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownApplicationMediaTypes")
    void testOfApplicationMediaType_Charset(
            final String mediaType)
    throws Throwable {
        final Charset charset = Generators.randomValue(Charset.class);
        final ContentType result = ContentType.of(mediaType, charset);
        assertEquals(mediaType, result.getMediaType());
        assertEquals(charset, result.getCharset());
        assertNull(result.getBoundary());
        assertFalse(result.getParameters().isEmpty());
        assertEquals(1, result.getParameters().size());
        assertEquals(charset.name(), result.getParameter(ContentType.CHARSET_PARAM));
        final String header =
                mediaType + ContentType.PARAMETER_SEPARATOR +
                ContentType.CHARSET_PARAM + ContentType.PARAMETER_VALUE_SEPARATOR + charset.name();
        assertEquals(header, result.getHeader());
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownAudioMediaTypes")
    void testOfAudioMediaType_Charset(
            final String mediaType)
    throws Throwable {
        final Charset charset = Generators.randomValue(Charset.class);
        assertThrows(IllegalArgumentException.class, () -> ContentType.of(mediaType, charset));
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownFontMediaTypes")
    void testOfFontMediaType_Charset(
            final String mediaType)
    throws Throwable {
        final Charset charset = Generators.randomValue(Charset.class);
        assertThrows(IllegalArgumentException.class, () -> ContentType.of(mediaType, charset));
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownImageMediaTypes")
    void testOfImageMediaType_Charset(
            final String mediaType)
    throws Throwable {
        final Charset charset = Generators.randomValue(Charset.class);
        assertThrows(IllegalArgumentException.class, () -> ContentType.of(mediaType, charset));
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownMultipartMediaTypes")
    void testOfMultipartMediaType_Charset(
            final String mediaType)
    throws Throwable {
        final Charset charset = Generators.randomValue(Charset.class);
        assertThrows(IllegalArgumentException.class, () -> ContentType.of(mediaType, charset));
    }

    /**
     * Test for {@link ContentType#of(String, Charset)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownTextMediaTypes")
    void testOfTextMediaType_Charset(
            final String mediaType)
    throws Throwable {
        final Charset charset = Generators.randomValue(Charset.class);
        final ContentType result = ContentType.of(mediaType, charset);
        assertEquals(mediaType, result.getMediaType());
        assertEquals(charset, result.getCharset());
        assertNull(result.getBoundary());
        assertFalse(result.getParameters().isEmpty());
        assertEquals(1, result.getParameters().size());
        assertEquals(charset.name(), result.getParameter(ContentType.CHARSET_PARAM));
        final String header =
                mediaType + ContentType.PARAMETER_SEPARATOR +
                ContentType.CHARSET_PARAM + ContentType.PARAMETER_VALUE_SEPARATOR + charset.name();
        assertEquals(header, result.getHeader());
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownVideoMediaTypes")
    void testOfVideoMediaType_Charset(
            final String mediaType)
    throws Throwable {
        final Charset charset = Generators.randomValue(Charset.class);
        assertThrows(IllegalArgumentException.class, () -> ContentType.of(mediaType, charset));
    }

    /**
     * Test for {@link ContentType#multipart(String, String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testMultipart()
    throws Throwable {
        final String mediaType = MediaTypes.Multipart.FORM_DATA;
        final String boundary = ContentTypeGenerator.randomMultipartBoundary();
        assertThrows(NullPointerException.class, () -> ContentType.multipart(mediaType, null));
        assertThrows(NullPointerException.class, () -> ContentType.multipart(null, boundary));
        assertThrows(NullPointerException.class, () -> ContentType.multipart(null, null));
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownApplicationMediaTypes")
    void testMultipartOfApplicationMediaType(
            final String mediaType)
    throws Throwable {
        final String boundary = ContentTypeGenerator.randomMultipartBoundary();
        assertThrows(IllegalArgumentException.class, () -> ContentType.multipart(mediaType, boundary));
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownAudioMediaTypes")
    void testMultipartOfAudioMediaType(
            final String mediaType)
    throws Throwable {
        final String boundary = ContentTypeGenerator.randomMultipartBoundary();
        assertThrows(IllegalArgumentException.class, () -> ContentType.multipart(mediaType, boundary));
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownFontMediaTypes")
    void testMultipartOfFontMediaType(
            final String mediaType)
    throws Throwable {
        final String boundary = ContentTypeGenerator.randomMultipartBoundary();
        assertThrows(IllegalArgumentException.class, () -> ContentType.multipart(mediaType, boundary));
    }

    /**
     * Test for {@link ContentType#of(String, Charset)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownMultipartMediaTypes")
    void testMultipartOfMultipartMediaType(
            final String mediaType)
    throws Throwable {
        final String boundary = ContentTypeGenerator.randomMultipartBoundary();
        final ContentType result = ContentType.multipart(mediaType, boundary);
        assertEquals(mediaType, result.getMediaType());
        assertNull(result.getCharset());
        assertEquals(boundary, result.getBoundary());
        assertFalse(result.getParameters().isEmpty());
        assertEquals(1, result.getParameters().size());
        assertEquals(boundary, result.getParameter(ContentType.BOUNDARY_PARAM));
        final String header =
                mediaType + ContentType.PARAMETER_SEPARATOR +
                ContentType.BOUNDARY_PARAM + ContentType.PARAMETER_VALUE_SEPARATOR + boundary;
        assertEquals(header, result.getHeader());
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownImageMediaTypes")
    void testMultipartOfImageMediaType(
            final String mediaType)
    throws Throwable {
        final String boundary = ContentTypeGenerator.randomMultipartBoundary();
        assertThrows(IllegalArgumentException.class, () -> ContentType.multipart(mediaType, boundary));
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownTextMediaTypes")
    void testMultipartOfTextMediaType(
            final String mediaType)
    throws Throwable {
        final String boundary = ContentTypeGenerator.randomMultipartBoundary();
        assertThrows(IllegalArgumentException.class, () -> ContentType.multipart(mediaType, boundary));
    }

    /**
     * Test for {@link ContentType#of(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownVideoMediaTypes")
    void testMultipartOfVideoMediaType(
            final String mediaType)
    throws Throwable {
        final String boundary = ContentTypeGenerator.randomMultipartBoundary();
        assertThrows(IllegalArgumentException.class, () -> ContentType.multipart(mediaType, boundary));
    }

    /**
     * Test for {@link ContentType#getHeader()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetHeader()
    throws Throwable {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            boolean withoutParameters = false;
            boolean withParameters = false;
            boolean withCharset = false;
            boolean withBoundary = false;
            while (!withoutParameters ||
                    !withParameters ||
                    !withCharset ||
                    !withBoundary) {
                final ContentType contentType = Generators.randomValue(ContentType.class);
                final StringBuilder expected = new StringBuilder()
                        .append(contentType.getMediaType());
                if (contentType.getParameters().isEmpty()) {
                    withoutParameters = true;
                } else {
                    withParameters = true;
                    withCharset = withCharset || contentType.getCharset() != null;
                    withBoundary = withBoundary || contentType.getBoundary() != null;
                    for (final Map.Entry<String, String> entry : contentType.getParameters().entrySet()) {
                        expected.append(ContentType.PARAMETER_SEPARATOR)
                        .append(entry.getKey())
                        .append(ContentType.PARAMETER_VALUE_SEPARATOR)
                        .append(entry.getValue());
                    }
                }
                assertEquals(expected.toString(), contentType.getHeader());
            }
        });
    }

    /**
     * Test for {@link ContentType#parse(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse()
    throws Throwable {
        assertThrows(NullPointerException.class, () -> ContentType.parse(null));
        assertThrows(IllegalArgumentException.class, () -> ContentType.parse(""));
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            boolean withoutParameters = false;
            boolean withParameters = false;
            boolean withCharset = false;
            boolean withBoundary = false;
            while (!withoutParameters ||
                    !withParameters ||
                    !withCharset ||
                    !withBoundary) {
                final ContentType expected = Generators.randomValue(ContentType.class);
                final String header = expected.getHeader();
                if (expected.getParameters().isEmpty()) {
                    withoutParameters = true;
                } else {
                    withParameters = true;
                    withCharset = withCharset || expected.getCharset() != null;
                    withBoundary = withBoundary || expected.getBoundary() != null;
                }
                final ContentType result = ContentType.parse(header);
                assertEquals(expected, result);
            }
        });
    }

    /**
     * Test for {@link ContentTypeGenerator#defaultValue()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testDefaultGeneration()
    throws Throwable {
        final ContentType contentType = Generators.defaultValue(ContentType.class);
        assertEquals(MediaTypes.Text.PLAIN, contentType.getMediaType());
        assertEquals(StandardCharsets.UTF_8, contentType.getCharset());
        assertEquals(1, contentType.getParameters().size());
    }

    /**
     * Test for {@link ContentTypeGenerator#randomValue()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testRandomGeneration()
    throws Throwable {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            final HashSet<Integer> parameters = new HashSet<>();
            boolean withCharset = false;
            boolean withBoundary = false;
            while (parameters.size() < 4 ||
                    !withCharset ||
                    !withBoundary) {
                final ContentType contentType = Generators.randomValue(ContentType.class);
                parameters.add(contentType.getParameters().size());
                if (!contentType.getParameters().isEmpty()) {
                    withCharset = withCharset || contentType.getCharset() != null;
                    withBoundary = withBoundary || contentType.getBoundary() != null;
                }
            }
        });
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
