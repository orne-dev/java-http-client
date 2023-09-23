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
import java.util.stream.Stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@code MediaTypes}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-09
 * @since 0.1
 * @see MediaTypes
 */
@Tag("ut")
class MediaTypesTest {

    /**
     * Test for {@link MediaTypes#isApplication(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownApplicationMediaTypes")
    void testIsApplicationWhenApplicationMediaType(
            final String mediaType)
    throws Throwable {
        assertTrue(MediaTypes.isApplication(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isApplication(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownAudioMediaTypes")
    void testIsApplicationWhenAudioMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isApplication(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isApplication(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownFontMediaTypes")
    void testIsApplicationWhenFontMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isApplication(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isApplication(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownImageMediaTypes")
    void testIsApplicationWhenImageMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isApplication(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isApplication(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownMultipartMediaTypes")
    void testIsApplicationWhenMultipartMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isApplication(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isApplication(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownTextMediaTypes")
    void testIsApplicationWhenTextMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isApplication(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isApplication(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownVideoMediaTypes")
    void testIsApplicationWhenVideoMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isApplication(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isAudio(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownApplicationMediaTypes")
    void testIsAudioWhenApplicationMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isAudio(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isAudio(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownAudioMediaTypes")
    void testIsAudioWhenAudioMediaType(
            final String mediaType)
    throws Throwable {
        assertTrue(MediaTypes.isAudio(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isAudio(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownFontMediaTypes")
    void testIsAudioWhenFontMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isAudio(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isAudio(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownImageMediaTypes")
    void testIsAudioWhenImageMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isAudio(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isAudio(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownMultipartMediaTypes")
    void testIsAudioWhenMultipartMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isAudio(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isAudio(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownTextMediaTypes")
    void testIsAudioWhenTextMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isAudio(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isAudio(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownVideoMediaTypes")
    void testIsAudioWhenVideoMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isAudio(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isFont(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownApplicationMediaTypes")
    void testIsFontWhenApplicationMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isFont(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isFont(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownAudioMediaTypes")
    void testIsFontWhenAudioMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isFont(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isFont(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownFontMediaTypes")
    void testIsFontWhenFontMediaType(
            final String mediaType)
    throws Throwable {
        assertTrue(MediaTypes.isFont(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isFont(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownImageMediaTypes")
    void testIsFontWhenImageMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isFont(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isFont(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownMultipartMediaTypes")
    void testIsFontWhenMultipartMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isFont(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isFont(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownTextMediaTypes")
    void testIsFontWhenTextMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isFont(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isFont(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownVideoMediaTypes")
    void testIsFontWhenVideoMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isFont(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isImage(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownApplicationMediaTypes")
    void testIsImageWhenApplicationMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isImage(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isImage(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownAudioMediaTypes")
    void testIsImageWhenAudioMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isImage(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isImage(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownFontMediaTypes")
    void testIsImageWhenFontMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isImage(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isImage(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownImageMediaTypes")
    void testIsImageWhenImageMediaType(
            final String mediaType)
    throws Throwable {
        assertTrue(MediaTypes.isImage(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isImage(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownMultipartMediaTypes")
    void testIsImageWhenMultipartMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isImage(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isImage(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownTextMediaTypes")
    void testIsImageWhenTextMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isImage(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isImage(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownVideoMediaTypes")
    void testIsImageWhenVideoMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isImage(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isMessage(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownApplicationMediaTypes")
    void testIsMessageWhenApplicationMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isMessage(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isMessage(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownAudioMediaTypes")
    void testIsMessageWhenAudioMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isMessage(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isMessage(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownFontMediaTypes")
    void testIsMessageWhenFontMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isMessage(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isMessage(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownImageMediaTypes")
    void testIsMessageWhenImageMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isMessage(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isMessage(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownMultipartMediaTypes")
    void testIsMessageWhenMultipartMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isMessage(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isMessage(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownTextMediaTypes")
    void testIsMessageWhenTextMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isMessage(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isMessage(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownVideoMediaTypes")
    void testIsMessageWhenVideoMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isMessage(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isModel(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownApplicationMediaTypes")
    void testIsModelWhenApplicationMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isModel(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isModel(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownAudioMediaTypes")
    void testIsModelWhenAudioMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isModel(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isModel(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownFontMediaTypes")
    void testIsModelWhenFontMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isModel(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isModel(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownImageMediaTypes")
    void testIsModelWhenImageMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isModel(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isModel(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownMultipartMediaTypes")
    void testIsModelWhenMultipartMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isModel(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isModel(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownTextMediaTypes")
    void testIsModelWhenTextMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isModel(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isModel(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownVideoMediaTypes")
    void testIsModelWhenVideoMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isModel(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isMultipart(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownApplicationMediaTypes")
    void testIsMultipartWhenApplicationMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isMultipart(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isMultipart(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownAudioMediaTypes")
    void testIsMultipartWhenAudioMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isMultipart(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isMultipart(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownFontMediaTypes")
    void testIsMultipartWhenFontMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isMultipart(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isMultipart(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownImageMediaTypes")
    void testIsMultipartWhenImageMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isMultipart(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isMultipart(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownMultipartMediaTypes")
    void testIsMultipartWhenMultipartMediaType(
            final String mediaType)
    throws Throwable {
        assertTrue(MediaTypes.isMultipart(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isMultipart(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownTextMediaTypes")
    void testIsMultipartWhenTextMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isMultipart(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isMultipart(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownVideoMediaTypes")
    void testIsMultipartWhenVideoMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isMultipart(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isText(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownApplicationMediaTypes")
    void testIsTextWhenApplicationMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isText(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isText(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownAudioMediaTypes")
    void testIsTextWhenAudioMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isText(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isText(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownFontMediaTypes")
    void testIsTextWhenFontMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isText(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isText(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownImageMediaTypes")
    void testIsTextWhenImageMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isText(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isText(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownMultipartMediaTypes")
    void testIsTextWhenMultipartMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isText(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isText(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownTextMediaTypes")
    void testIsTextWhenTextMediaType(
            final String mediaType)
    throws Throwable {
        assertTrue(MediaTypes.isText(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isText(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownVideoMediaTypes")
    void testIsTextWhenVideoMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isText(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isVideo(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownApplicationMediaTypes")
    void testIsVideoWhenApplicationMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isVideo(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isVideo(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownAudioMediaTypes")
    void testIsVideoWhenAudioMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isVideo(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isVideo(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownFontMediaTypes")
    void testIsVideoWhenFontMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isVideo(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isVideo(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownImageMediaTypes")
    void testIsVideoWhenImageMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isVideo(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isVideo(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownMultipartMediaTypes")
    void testIsVideoWhenMultipartMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isVideo(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isVideo(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownTextMediaTypes")
    void testIsVideoWhenTextMediaType(
            final String mediaType)
    throws Throwable {
        assertFalse(MediaTypes.isVideo(mediaType));
    }

    /**
     * Test for {@link MediaTypes#isVideo(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("knownVideoMediaTypes")
    void testIsVideoWhenVideoMediaType(
            final String mediaType)
    throws Throwable {
        assertTrue(MediaTypes.isVideo(mediaType));
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
