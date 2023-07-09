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

import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import dev.orne.test.rnd.AbstractTypedGenerator;
import dev.orne.test.rnd.Generators;

/**
 * Generator of random {@code ContentType} instances.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 */
public class ContentTypeGenerator
extends AbstractTypedGenerator<ContentType> {

    /** The known media types. */
    private static final @NotNull List<String> knownMediaTypes =
            new ArrayList<>();
    /** Probability of extra parameters in application media types. */
    private static final float EXTRA_PARAMS_P = 0.2f;

    static {
        knownMediaTypes.addAll(scanStringConstants(MediaTypes.Application.class));
        knownMediaTypes.addAll(scanStringConstants(MediaTypes.Audio.class));
        knownMediaTypes.addAll(scanStringConstants(MediaTypes.Font.class));
        knownMediaTypes.addAll(scanStringConstants(MediaTypes.Image.class));
        knownMediaTypes.addAll(scanStringConstants(MediaTypes.Multipart.class));
        knownMediaTypes.addAll(scanStringConstants(MediaTypes.Text.class));
        knownMediaTypes.addAll(scanStringConstants(MediaTypes.Video.class));
    }

    /**
     * Scans the {@code static final String} constants of the specified class.
     * 
     * @param type The class to scan for {@code String} constants.
     * @return The {@code String} constants found.
     */
    private static @NotNull List<String> scanStringConstants(
            final @NotNull Class<?> type) {
        return Stream.of(type.getDeclaredFields())
                .filter(field -> String.class.equals(field.getType())
                            && Modifier.isStatic(field.getModifiers())
                            && Modifier.isFinal(field.getModifiers()))
                .map(field -> {
                    try {
                        return String.valueOf(field.get(null));
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ContentType defaultValue() {
        return ContentType.of(MediaTypes.Text.PLAIN, StandardCharsets.UTF_8);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ContentType randomValue() {
        final String mediaType = randomMediaType();
        final ContentType result;
        if (MediaTypes.isAudio(mediaType) ||
                MediaTypes.isFont(mediaType) ||
                MediaTypes.isImage(mediaType) ||
                MediaTypes.isVideo(mediaType)) {
            result = ContentType.of(mediaType);
        } else if (MediaTypes.isMultipart(mediaType)) {
            final String boundary = randomMultipartBoundary();
            result = ContentType.multipart(mediaType, boundary);
        } else if (MediaTypes.isText(mediaType)) {
            final Charset charset = Generators.randomValue(Charset.class);
            result = ContentType.of(mediaType, charset);
        } else {
            if (RandomUtils.nextBoolean()) {
                final Charset charset = Generators.randomValue(Charset.class);
                result = ContentType.of(mediaType, charset);
            } else {
                result = ContentType.of(mediaType);
            }
        }
        if (RandomUtils.nextFloat(0, 1) < EXTRA_PARAMS_P) {
            final int extraParams = RandomUtils.nextInt(1, 3);
            for (int i = 0; i < extraParams; i++) {
                final String name = RandomStringUtils.randomAlphanumeric(10);
                final String value = RandomStringUtils.randomAlphanumeric(10);
                result.setParameter(name, value);
            }
        }
        return result;
    }

    /**
     * Generates a random known media type.
     * 
     * @return The media type.
     */
    public static final @NotNull String randomMediaType() {
        return knownMediaTypes.get(
                RandomUtils.nextInt(0, knownMediaTypes.size()));
    }

    /**
     * Generates a random mutipart media type boundary parameter value.
     * 
     * @return The mutipart media type boundary.
     */
    public static final @NotNull String randomMultipartBoundary() {
        return RandomStringUtils.randomAlphanumeric(20);
    }

    /**
     * Generates a random parameter name.
     * 
     * @return The parameter name.
     */
    public static final @NotNull String randomParameterName() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    /**
     * Generates a random parameter value.
     * 
     * @return The parameter value.
     */
    public static final @NotNull String randomParameterValue() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
