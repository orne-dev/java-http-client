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
import java.io.OutputStream;

import javax.validation.constraints.NotNull;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpClientException;

/**
 * Interface for HTTP requests.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public interface HttpRequest {

    /**
     * Adds the specified header to the HTTP request.
     * 
     * @param header The header name.
     * @param values The header values.
     * @throws HttpClientException If an error occurs setting HTTP header.
     */
    void addHeader(
            @NotNull String header,
            @NotNull String... values)
    throws HttpClientException;

    /**
     * Sets the specified HTTP request body.
     * 
     * @param contentType The body content type.
     * @param body The body content.
     * @throws HttpClientException If an error occurs setting the HTTP
     * request body.
     */
    void setBody(
            @NotNull ContentType contentType,
            @NotNull String body)
    throws HttpClientException;

    /**
     * Sets the specified HTTP request body.
     * 
     * @param contentType The body content type.
     * @param body The body content.
     * @throws HttpClientException If an error occurs setting the HTTP
     * request body.
     */
    void setBody(
            @NotNull ContentType contentType,
            @NotNull byte[] body)
    throws HttpClientException;

    /**
     * Sets the specified HTTP request body with unknown length.
     * 
     * @param contentType The body content type.
     * @param bodyProducer The body content producer.
     * @throws HttpClientException If an error occurs setting the HTTP
     * request body.
     */
    default void setBody(
            @NotNull ContentType contentType,
            @NotNull BodyProducer bodyProducer)
    throws HttpClientException {
        setBody(contentType, -1, bodyProducer);
    }

    /**
     * Sets the specified HTTP request body.
     * 
     * @param contentType The body content type.
     * @param length The body content length.
     *               Use {@code -1} for unknown length.
     * @param bodyProducer The body content producer.
     * @throws HttpClientException If an error occurs setting the HTTP
     * request body.
     */
    void setBody(
            @NotNull ContentType contentType,
            long length,
            @NotNull BodyProducer bodyProducer)
    throws HttpClientException;

    /**
     * Functional interface for HTTP request body producers.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since HttpRequest 1.0
     */
    @FunctionalInterface
    public interface BodyProducer {

        /**
         * Writes the HTTP request body content.
         * 
         * @param output The output stream to write the body to.
         * @throws IOException If an error occurs producing the request body
         */
        void writeBody(
                @NotNull OutputStream output)
        throws IOException;
    }
}
