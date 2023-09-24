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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpResponseBodyParsingException;
import dev.orne.http.client.HttpResponseHandlingException;
import dev.orne.http.client.engine.HttpRequest;
import dev.orne.http.client.engine.HttpResponseBody;

/**
 * API for generation of URL encoded ({@code application/x-www-form-urlencoded})
 * request and response body handlers.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 */
public final class WWWFormHttpBody {

    /**
     * Private constructor.
     */
    private WWWFormHttpBody() {
        // Utility class
    }

    /**
     * Produce the specified entity HTTP request body
     * with {@code application/x-www-form-urlencoded;charset=UTF-8} as
     * content type.
     * 
     * @param entity The HTTP request body entity.
     * @param request The HTTP request.
     * @throws HttpClientException If an error occurs producing
     * or setting the request body.
     */
    public static void produce(
            final Map<@NotNull String, @NotNull String> entity,
            final @NotNull HttpRequest request)
    throws HttpClientException {
        produce(
                entity,
                request,
                WWWFormHttpResponseBodyParser.DEFAULT_CONTENT_TYPE);
    }

    /**
     * Produce the specified entity as HTTP request body
     * with the specified content type.
     * 
     * @param entity The HTTP request body entity.
     * @param request The HTTP request.
     * @param contentType The HTTP request body content type.
     * @throws HttpClientException If an error occurs producing
     * or setting the request body.
     */
    public static void produce(
            final Map<@NotNull String, @NotNull String> entity,
            final @NotNull HttpRequest request,
            final @NotNull ContentType contentType)
    throws HttpClientException {
        if (entity != null) {
            Validate.notNull(request);
            Validate.notNull(contentType);
            final Charset charset = contentType.getCharset();
            final String urlEncoded = UrlEncodedUtils.format(charset, entity);
            request.setBody(contentType, urlEncoded);
        }
    }

    /**
     * Produce the specified entity HTTP request body
     * with {@code application/x-www-form-urlencoded;charset=UTF-8} as
     * content type.
     * 
     * @param entity The HTTP request body entity.
     * @param request The HTTP request.
     * @throws HttpClientException If an error occurs producing
     * or setting the request body.
     */
    public static void produce(
            final Collection<Pair<@NotNull String, @NotNull String>> entity,
            final @NotNull HttpRequest request)
    throws HttpClientException {
        produce(
                entity,
                request,
                WWWFormHttpResponseBodyParser.DEFAULT_CONTENT_TYPE);
    }

    /**
     * Produce the specified entity as HTTP request body
     * with the specified content type.
     * 
     * @param entity The HTTP request body entity.
     * @param request The HTTP request.
     * @param contentType The HTTP request body content type.
     * @throws HttpClientException If an error occurs producing
     * or setting the request body.
     */
    public static void produce(
            final Collection<Pair<@NotNull String, @NotNull String>> entity,
            final @NotNull HttpRequest request,
            final @NotNull ContentType contentType)
    throws HttpClientException {
        if (entity != null) {
            Validate.notNull(request);
            Validate.notNull(contentType);
            final Charset charset = contentType.getCharset();
            final String urlEncoded = UrlEncodedUtils.format(charset, entity);
            request.setBody(contentType, urlEncoded);
        }
    }

    /**
     * Parses the HTTP response body URL encoded entity
     * with {@code application/x-www-form-urlencoded;charset=UTF-8} as default
     * content type.
     * 
     * @param body The HTTP response body.
     * @return The parsed HTTP response body name-value pairs.
     * @throws HttpResponseHandlingException If an error occurs parsing the
     * HTTP response body.
     */
    public static Collection<@NotNull Pair<@NotNull String, @NotNull String>> parse(
            final @NotNull HttpResponseBody body)
    throws HttpResponseHandlingException {
        return parse(
                body,
                WWWFormHttpResponseBodyParser.DEFAULT_CONTENT_TYPE);
    }

    /**
     * Parses the HTTP response body URL encoded entity.
     * 
     * @param body The HTTP response body.
     * @param defaultContentType The default content type to use if the HTTP
     * response does not specify one.
     * @return The parsed HTTP response body name-value pairs.
     * @throws HttpResponseHandlingException If an error occurs parsing the
     * HTTP response body.
     */
    public static Collection<@NotNull Pair<@NotNull String, @NotNull String>> parse(
            final @NotNull HttpResponseBody body,
            final @NotNull ContentType defaultContentType)
    throws HttpResponseHandlingException {
        Validate.notNull(body);
        return body.parse(parser(defaultContentType));
    }

    /**
     * Creates a new HTTP response body URL encoded entity parser with
     * {@code application/x-www-form-urlencoded;charset=UTF-8} as default
     * content type.
     * 
     * @return The created HTTP response body parser.
     */
    public static @NotNull WWWFormHttpResponseBodyParser parser() {
        return parser(WWWFormHttpResponseBodyParser.DEFAULT_CONTENT_TYPE);
    }

    /**
     * Creates a new HTTP response body URL encoded entity parser with the
     * specified default content type.
     * 
     * @param defaultContentType The default content type to use if the HTTP
     * response does not specify one.
     * @return The created HTTP response body parser.
     */
    public static @NotNull WWWFormHttpResponseBodyParser parser(
            final @NotNull ContentType defaultContentType) {
        return new WWWFormBodyParser(defaultContentType);
    }

    /**
     * Default implementation of {@code WWWFormUrlEncodedHttpResponseBodyParser}.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-07
     * @since 0.1
     */
    public static class WWWFormBodyParser
    extends AbstractHttpResponseBodyMediaTypeParser<Collection<@NotNull Pair<@NotNull String, @NotNull String>>>
    implements WWWFormHttpResponseBodyParser {

        /** The default content type to use. */
        private final @NotNull ContentType defaultContentType;

        /**
         * Creates a new instance.
         * 
         * @param defaultContentType The default content type to use if the HTTP
         * response does not specify one.
         */
        public WWWFormBodyParser(
                final @NotNull ContentType defaultContentType) {
            super();
            this.defaultContentType = Validate.notNull(defaultContentType);
            Validate.notNull(
                    defaultContentType.getCharset(),
                    "Default content type must include a charset parameter.");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull ContentType getDefaultContentType() {
            return this.defaultContentType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Collection<@NotNull Pair<@NotNull String, @NotNull String>> parseSupportedContent(
                final @NotNull ContentType type,
                final @NotNull InputStream content,
                final long length)
        throws HttpResponseBodyParsingException {
            final Charset charset = ObjectUtils.defaultIfNull(
                    type.getCharset(),
                    this.defaultContentType.getCharset());
            try {
                final String encoded = IOUtils.toString(content, charset);
                return UrlEncodedUtils.parse(charset, encoded);
            } catch (IOException e) {
                throw new HttpResponseBodyParsingException("Error parsing HTTP response body", e);
            }
        }
    }
}
