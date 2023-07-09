package dev.orne.http.client.body;

import java.io.InputStream;

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

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.ObjectUtils;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpResponseBodyParsingException;
import dev.orne.http.client.UnsupportedContentTypeException;

/**
 * Interface for parsers of HTTP response body entities that can parse
 * only certain media types.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @param <E> The HTTP response body entity type.
 * @since 0.1
 */
public interface HttpResponseBodyMediaTypeParser<E>
extends HttpResponseBodyParser<E> {

    /**
     * Returns the default content type to use when HTTP response does not
     * include a content type header.
     * 
     * @return The default content type.
     */
    @NotNull ContentType getDefaultContentType();

    /**
     * Returns {@code true} if the specified media type is supported by this
     * parser.
     * 
     * @param mediaType The media type to support.
     * @return If the specified media type is supported.
     */
    boolean supportsMediaType(
            @NotNull String mediaType);

    /**
     * {@inheritDoc}
     */
    @Override
    default E parse(
            ContentType type,
            final @NotNull InputStream content,
            final long length)
    throws HttpResponseBodyParsingException {
        type = ObjectUtils.defaultIfNull(type, getDefaultContentType());
        if (!supportsMediaType(type.getMediaType())) {
            throw new UnsupportedContentTypeException(
                    "Unsupported content type on HTTP response body: " + type);
        }
        return parseSupportedContent(type, content, length);
    }

    /**
     * Parses the HTTP response body entity.
     * 
     * @param type The HTTP response body content type.
     * @param content The HTTP response body content.
     * @param length HTTP response body content length.
     * @return The parsed HTTP response body entity.
     * @throws HttpResponseBodyParsingException If an error occurs parsing the
     * HTTP response body.
     */
    E parseSupportedContent(
            @NotNull ContentType type,
            @NotNull InputStream content,
            long length)
    throws HttpResponseBodyParsingException;
}
