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

import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;

import dev.orne.http.ContentType;
import dev.orne.http.MediaTypes;

/**
 * Interface for parsers of string HTTP response body entities.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @param <E> The HTTP response body entity type.
 * @since 0.1
 */
public interface StringHttpResponseBodyParser
extends HttpResponseBodyMediaTypeParser<String> {

    /** The default content type: {@code text/plain;charset=UTF-8}. */
    public static final @NotNull ContentType DEFAULT_CONTENT_TYPE = 
            ContentType.of(MediaTypes.Text.PLAIN, StandardCharsets.UTF_8);

    /**
     * {@inheritDoc}
     */
    @Override
    default @NotNull ContentType getDefaultContentType() {
        return DEFAULT_CONTENT_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean supportsMediaType(
            final @NotNull String mediaType) {
        return !MediaTypes.isAudio(mediaType) &&
                !MediaTypes.isFont(mediaType) &&
                !MediaTypes.isImage(mediaType) &&
                !MediaTypes.isVideo(mediaType);
    }
}
