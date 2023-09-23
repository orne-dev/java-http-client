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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpResponseBodyParsingException;

/**
 * Interface for parsers of HTTP response body entities.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @param <E> The HTTP response body entity type.
 * @since 0.1
 */
public interface HttpResponseBodyParser<E> {

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
    E parse(
            ContentType type,
            @NotNull InputStream content,
            long length)
    throws HttpResponseBodyParsingException;

    /**
     * Creates a HTTP response body reader.
     * <p>
     * If the content type id {@code null} or has no charset uses {@code UTF-8}
     * by default.
     * <p>
     * The calling method is responsible of closing the reader.
     * 
     * @param type The HTTP response body content type.
     * @param input The HTTP response body stream.
     * @return The created reader.
     */
    default Reader createReader(
            final ContentType contentType,
            final @NotNull InputStream input) {
        Validate.notNull(input);
        if (contentType == null || contentType.getCharset() == null) {
            return new InputStreamReader(input, StandardCharsets.UTF_8);
        } else {
            return new InputStreamReader(input, contentType.getCharset());
        }
    }
}
