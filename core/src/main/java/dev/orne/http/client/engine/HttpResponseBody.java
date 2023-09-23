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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpResponseBodyParsingException;
import dev.orne.http.client.HttpResponseHandlingException;
import dev.orne.http.client.body.HttpResponseBodyParser;

/**
 * Interface for HTTP responses' bodies.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public interface HttpResponseBody {

    /**
     * Returns the content type of the response body.
     * 
     * @return The content type of the response body, if declared.
     * @throws HttpResponseHandlingException If an error occurs parsing the
     * response body content type.
     */
    ContentType getContentType()
    throws HttpResponseHandlingException;

    /**
     * Returns the length of the response body content, in bytes.
     * A value of {@code -1} denotes unknown length.
     * 
     * @return The length of the response body content.
     * @throws HttpResponseHandlingException If an error occurs retrieving
     * the response body content length.
     */
    long getContentLength()
    throws HttpResponseHandlingException;

    /**
     * Returns a stream with the content of the response body.
     * <p>
     * The calling method is responsible of closing the stream.
     * 
     * @return The content of the response body, if any.
     * @throws HttpResponseHandlingException If an error occurs retrieving
     * the response body content.
     */
    InputStream getContent()
    throws HttpResponseHandlingException;

    /**
     * Creates a HTTP response body reader.
     * <p>
     * If the content type has no charset uses UTF-8 by default.
     * <p>
     * The calling method is responsible of closing the reader.
     * 
     * @param type The HTTP response body content type.
     * @param is The HTTP response body stream.
     * @return The created reader.
     * @throws HttpResponseHandlingException If an error occurs retrieving
     * the response body content.
     */
    default Reader getContentReader()
    throws HttpResponseHandlingException {
        final ContentType contentType = getContentType();
        final InputStream content = getContent();
        if (content == null) {
            return null;
        }
        if (contentType != null && contentType.getCharset() != null) {
            return new InputStreamReader(content, contentType.getCharset());
        } else {
            return new InputStreamReader(content, StandardCharsets.UTF_8);
        }
    }

    /**
     * Parses the response body content, if any, using the specified parser.
     * 
     * @param <E> The HTTP response entity type.
     * @param parser HTTP response body content parser.
     * @return The HTTP response entity, or {@code null} if no body content is
     * available. 
     * @throws HttpResponseHandlingException If an error occurs handling
     * the response body content.
     */
    default <E> E parse(
            final @NotNull HttpResponseBodyParser<E> parser)
    throws HttpResponseHandlingException {
        try (final InputStream content = getContent()) {
            if (content == null) {
                return null;
            } else {
                return parser.parse(getContentType(), content, getContentLength());
            }
        } catch (final IOException e) {
            throw new HttpResponseBodyParsingException("Error closing HTTP response body stream", e);
        }
    }

    /**
     * Consumes and discards the HTTP response body content.
     * 
     * @throws HttpResponseHandlingException If an error occurs discarding
     * the response body content.
     */
    default void discard()
    throws HttpResponseHandlingException {
        final InputStream content = getContent();
        if (content != null) {
            try {
                content.close();
            } catch (final IOException e) {
                throw new HttpResponseHandlingException(
                        "Error closing response body content stream", e);
            }
        }
    }
}
