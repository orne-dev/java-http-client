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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpResponseBodyParsingException;
import dev.orne.http.client.HttpResponseHandlingException;
import dev.orne.http.client.engine.HttpRequest;
import dev.orne.http.client.engine.HttpResponseBody;

/**
 * API for generation of Jackson based JSON HTTP request and response body
 * handlers.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 */
public final class JacksonHttpBody {

    /** The default Jackson object mapper to use. */
    private static @NotNull ObjectMapper defaultMapper = new ObjectMapper();

    /**
     * Private constructor.
     */
    private JacksonHttpBody() {
        // Utility class
    }

    /**
     * Returns the default Jackson object mapper to use.
     * 
     * @return The default Jackson object mapper to use.
     */
    public static @NotNull ObjectMapper getDefaultMapper() {
        return JacksonHttpBody.defaultMapper;
    }

    /**
     * Set the default Jackson object mapper to use.
     * 
     * @param mapper The default Jackson object mapper to use.
     */
    public static void setDefaultMapper(
            final @NotNull ObjectMapper mapper) {
        JacksonHttpBody.defaultMapper = Validate.notNull(mapper);
    }

    /**
     * Produce the specified entity as JSON HTTP request body
     * with {@code application/json;charset=UTF-8} as content type
     * using the default Jackson object mapper.
     * 
     * @param entity The HTTP request body entity.
     * @param request The HTTP request.
     * @throws HttpClientException If an error occurs producing
     * or setting the request body.
     */
    public static void produce(
            final Object entity,
            final @NotNull HttpRequest request)
    throws HttpClientException {
        produce(
                entity,
                request,
                JsonHttpResponseBodyParser.DEFAULT_CONTENT_TYPE,
                JacksonHttpBody.defaultMapper);
    }

    /**
     * Produce the specified entity as JSON HTTP request body
     * with the specified content type
     * using the default Jackson object mapper.
     * 
     * @param entity The HTTP request body entity.
     * @param request The HTTP request.
     * @param contentType The HTTP request body content type.
     * @throws HttpClientException If an error occurs producing
     * or setting the request body.
     */
    public static void produce(
            final Object entity,
            final @NotNull HttpRequest request,
            final @NotNull ContentType contentType)
    throws HttpClientException {
        produce(entity, request, contentType, JacksonHttpBody.defaultMapper);
    }

    /**
     * Produce the specified entity as JSON HTTP request body
     * with the specified content type.
     * 
     * @param entity The HTTP request body entity.
     * @param request The HTTP request.
     * @param contentType The HTTP request body content type.
     * @param mapper The Jackson object mapper to use.
     * @throws HttpClientException If an error occurs producing
     * or setting the request body.
     */
    public static void produce(
            final Object entity,
            final @NotNull HttpRequest request,
            final @NotNull ContentType contentType,
            final @NotNull ObjectMapper mapper)
    throws HttpClientException {
        Validate.notNull(request);
        Validate.notNull(contentType);
        Validate.notNull(
                contentType.getCharset(),
                "Content type must include a charset parameter.");
        Validate.notNull(mapper);
        request.setBody(contentType, output -> {
            final OutputStreamWriter writer = new OutputStreamWriter(
                    output,
                    contentType.getCharset());
            mapper.writeValue(writer, entity);
        });
    }

    /**
     * Parses the HTTP response body JSON entity
     * with {@code application/json;charset=UTF-8} as default content type
     * using the default Jackson object mapper.
     * 
     * @param <E> The HTTP response body entity type.
     * @param body The HTTP response body.
     * @param entityType The HTTP response body entity type.
     * @return The parsed HTTP response body entity.
     * @throws HttpResponseHandlingException If an error occurs parsing the
     * HTTP response body.
     */
    public static <E> E parse(
            final @NotNull HttpResponseBody body,
            final @NotNull Class<? extends E> entityType)
    throws HttpResponseHandlingException {
        return parse(
                body,
                entityType,
                JsonHttpResponseBodyParser.DEFAULT_CONTENT_TYPE,
                JacksonHttpBody.defaultMapper);
    }

    /**
     * Parses the HTTP response body JSON entity
     * using the default Jackson object mapper.
     * 
     * @param <E> The HTTP response body entity type.
     * @param body The HTTP response body.
     * @param entityType The HTTP response body entity type.
     * @param defaultContentType The default content type to use if the HTTP
     * response does not specify one.
     * @return The parsed HTTP response body entity.
     * @throws HttpResponseHandlingException If an error occurs parsing the
     * HTTP response body.
     */
    public static <E> E parse(
            final @NotNull HttpResponseBody body,
            final @NotNull Class<? extends E> entityType,
            final @NotNull ContentType defaultContentType)
    throws HttpResponseHandlingException {
        return parse(body, entityType, defaultContentType, JacksonHttpBody.defaultMapper);
    }

    /**
     * Parses the HTTP response body JSON entity.
     * 
     * @param <E> The HTTP response body entity type.
     * @param body The HTTP response body.
     * @param entityType The HTTP response body entity type.
     * @param defaultContentType The default content type to use if the HTTP
     * response does not specify one.
     * @param mapper The Jackson object mapper to use.
     * @return The parsed HTTP response body entity.
     * @throws HttpResponseHandlingException If an error occurs parsing the
     * HTTP response body.
     */
    public static <E> E parse(
            final @NotNull HttpResponseBody body,
            final @NotNull Class<? extends E> entityType,
            final @NotNull ContentType defaultContentType,
            final @NotNull ObjectMapper mapper)
    throws HttpResponseHandlingException {
        Validate.notNull(body);
        return body.parse(parser(entityType, defaultContentType, mapper));
    }

    /**
     * Creates a new HTTP response body JSON entity parser for the specified
     * entity type
     * with {@code application/json;charset=UTF-8} as default content type
     * using the default Jackson object mapper.
     * 
     * @param <E> The HTTP response body entity type.
     * @param entityType The HTTP response body entity type.
     * @param defaultContentType The default content type to use if the HTTP
     * response does not specify one.
     * @return The created HTTP response body parser.
     */
    public static <E> @NotNull JsonHttpResponseBodyParser<E> parser(
            final @NotNull Class<? extends E> entityType) {
        return parser(
                entityType,
                JsonHttpResponseBodyParser.DEFAULT_CONTENT_TYPE,
                JacksonHttpBody.defaultMapper);
    }

    /**
     * Creates a new HTTP response body JSON entity parser for the specified
     * entity type
     * with the specified default content type
     * using the default Jackson object mapper.
     * 
     * @param <E> The HTTP response body entity type.
     * @param entityType The HTTP response body entity type.
     * @param defaultContentType The default content type to use if the HTTP
     * response does not specify one.
     * @return The created HTTP response body parser.
     */
    public static <E> @NotNull JsonHttpResponseBodyParser<E> parser(
            final @NotNull Class<? extends E> entityType,
            final @NotNull ContentType defaultContentType) {
        return parser(entityType, defaultContentType, JacksonHttpBody.defaultMapper);
    }

    /**
     * Creates a new HTTP response body JSON entity parser for the specified
     * entity type.
     * 
     * @param <E> The HTTP response body entity type.
     * @param entityType The HTTP response body entity type.
     * @param defaultContentType The default content type to use if the HTTP
     * response does not specify one.
     * @param mapper The Jackson object mapper to use.
     * @return The created HTTP response body parser.
     */
    public static <E> @NotNull JsonHttpResponseBodyParser<E> parser(
            final @NotNull Class<? extends E> entityType,
            final @NotNull ContentType defaultContentType,
            final @NotNull ObjectMapper mapper) {
        return new JacksonBodyParser<>(entityType, defaultContentType, mapper);
    }

    /**
     * Jackson based implementation of {@code JsonHttpResponseBodyParser}.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-07
     * @param <E> The HTTP response body entity type.
     * @since 0.1
     */
    public static class JacksonBodyParser<E>
    implements JsonHttpResponseBodyParser<E> {

        /** The HTTP response body entity type. */
        private final @NotNull Class<? extends E> entityType;
        /** The default content type to use. */
        private final @NotNull ContentType defaultContentType;
        /** The Jackson object mapper to use. */
        private final @NotNull ObjectMapper mapper;

        /**
         * Creates a new instance.
         * 
         * @param entityType The HTTP response body entity type.
         * @param defaultContentType The default content type to use if the HTTP
         * response does not specify one.
         * @param mapper The Jackson object mapper to use.
         */
        public JacksonBodyParser(
                final @NotNull Class<? extends E> entityType,
                final @NotNull ContentType defaultContentType,
                final @NotNull ObjectMapper mapper) {
            super();
            this.entityType = Validate.notNull(entityType);
            this.defaultContentType = Validate.notNull(defaultContentType);
            Validate.notNull(
                    defaultContentType.getCharset(),
                    "Default content type must include a charset parameter.");
            this.mapper = Validate.notNull(mapper);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull ContentType getDefaultContentType() {
            return this.defaultContentType;
        }

        /**
         * Returns the HTTP response body entity type.
         * 
         * @return The HTTP response body entity type.
         */
        protected @NotNull Class<? extends E> getEntityType() {
            return this.entityType;
        }

        /**
         * Returns the Jackson object mapper to use.
         * 
         * @return The Jackson object mapper to use.
         */
        protected @NotNull ObjectMapper getMapper() {
            return this.mapper;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public E parseSupportedContent(
                final @NotNull ContentType type,
                final @NotNull InputStream content,
                final long length)
        throws HttpResponseBodyParsingException {
            try (final InputStreamReader reader = new InputStreamReader(
                    content,
                    ObjectUtils.defaultIfNull(
                        type.getCharset(),
                        this.defaultContentType.getCharset()))) {
                return this.mapper.readValue(reader, this.entityType);
            } catch (IOException e) {
                throw new HttpResponseBodyParsingException("Error parsing HTTP response body", e);
            }
        }
    }
}
