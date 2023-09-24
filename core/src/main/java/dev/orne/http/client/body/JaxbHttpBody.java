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
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.Validate;

import dev.orne.http.ContentType;
import dev.orne.http.MediaTypes;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpRequestBodyGenerationException;
import dev.orne.http.client.HttpResponseBodyParsingException;
import dev.orne.http.client.HttpResponseHandlingException;
import dev.orne.http.client.engine.HttpRequest;
import dev.orne.http.client.engine.HttpResponseBody;

/**
 * API for generation of JAXB based XML HTTP request and response body
 * handlers.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 */
public final class JaxbHttpBody {

    /** The default content type: {@code application/xml;charset=UTF-8}. */
    public static final @NotNull ContentType DEFAULT_OUPUT_CONTENT_TYPE = 
            ContentType.of(MediaTypes.Application.XML, StandardCharsets.UTF_8);

    /** Error message when the default JAXB context creation fails. */
    private static final String JAXB_CREATE_ERR = "Error creating fallback JAXB context.";

    /**
     * Private constructor.
     */
    private JaxbHttpBody() {
        // Utility class
    }

    /**
     * Produce the specified entity as XML HTTP request body
     * with {@code application/xml} as content type
     * using a default JAXB context.
     * 
     * @param entity The HTTP request body entity.
     * @param request The HTTP request.
     * @throws HttpClientException If an error occurs producing
     * or setting the request body.
     */
    public static void produce(
            final @NotNull Object entity,
            final @NotNull HttpRequest request)
    throws HttpClientException {
        Validate.notNull(entity);
        try {
            produce(
                    entity,
                    request,
                    DEFAULT_OUPUT_CONTENT_TYPE,
                    JAXBContext.newInstance(entity.getClass()));
        } catch (final JAXBException e) {
            throw new HttpRequestBodyGenerationException(JAXB_CREATE_ERR, e);
        }
    }

    /**
     * Produce the specified entity as XML HTTP request body
     * with the specified content type
     * using a default JAXB context.
     * 
     * @param entity The HTTP request body entity.
     * @param request The HTTP request.
     * @param contentType The HTTP request body content type.
     * @throws HttpClientException If an error occurs producing
     * or setting the request body.
     */
    public static void produce(
            final @NotNull Object entity,
            final @NotNull HttpRequest request,
            final @NotNull ContentType contentType)
    throws HttpClientException {
        Validate.notNull(entity);
        try {
            produce(
                    entity,
                    request,
                    contentType,
                    JAXBContext.newInstance(entity.getClass()));
        } catch (final JAXBException e) {
            throw new HttpRequestBodyGenerationException(JAXB_CREATE_ERR, e);
        }
    }

    /**
     * Produce the specified entity as XML HTTP request body
     * with the specified content type.
     * 
     * @param entity The HTTP request body entity.
     * @param request The HTTP request.
     * @param contentType The HTTP request body content type.
     * @param context The JAXB context to use.
     * @throws HttpClientException If an error occurs producing
     * or setting the request body.
     */
    public static void produce(
            final @NotNull Object entity,
            final @NotNull HttpRequest request,
            final @NotNull ContentType contentType,
            final @NotNull JAXBContext context)
    throws HttpClientException {
        Validate.notNull(entity);
        Validate.notNull(request);
        Validate.notNull(contentType);
        Validate.notNull(
                contentType.getCharset(),
                "Content type must include a charset parameter.");
        Validate.notNull(context);
        request.setBody(contentType, output -> {
            try {
                final Marshaller marshaller = context.createMarshaller();
                try (final OutputStreamWriter writer = new OutputStreamWriter(
                        output,
                        contentType.getCharset())) {
                    marshaller.setProperty(
                            Marshaller.JAXB_ENCODING,
                            contentType.getCharset().name());
                    marshaller.marshal(entity, writer);
                }
            } catch (final JAXBException e) {
                throw new HttpRequestBodyGenerationException("Error producing HTTP request body", e);
            }
        });
    }

    /**
     * Parses the HTTP response body XML entity
     * with {@code application/xml} as default content type
     * using a default JAXB context.
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
        Validate.notNull(entityType);
        try {
            return parse(
                    body,
                    entityType,
                    XmlHttpResponseBodyParser.DEFAULT_CONTENT_TYPE,
                    JAXBContext.newInstance(entityType));
        } catch (final JAXBException e) {
            throw new HttpResponseBodyParsingException(JAXB_CREATE_ERR, e);
        }
    }

    /**
     * Parses the HTTP response body XML entity
     * with the specified default content type
     * using a default JAXB context.
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
        Validate.notNull(entityType);
        try {
            return parse(
                    body,
                    entityType,
                    defaultContentType,
                    JAXBContext.newInstance(entityType));
        } catch (final JAXBException e) {
            throw new HttpResponseBodyParsingException(JAXB_CREATE_ERR, e);
        }
    }

    /**
     * Parses the HTTP response body XML entity.
     * 
     * @param <E> The HTTP response body entity type.
     * @param body The HTTP response body.
     * @param entityType The HTTP response body entity type.
     * @param defaultContentType The default content type to use if the HTTP
     * response does not specify one.
     * @param context The JAXB context to use.
     * @return The parsed HTTP response body entity.
     * @throws HttpResponseHandlingException If an error occurs parsing the
     * HTTP response body.
     */
    public static <E> E parse(
            final @NotNull HttpResponseBody body,
            final @NotNull Class<? extends E> entityType,
            final @NotNull ContentType defaultContentType,
            final @NotNull JAXBContext context)
    throws HttpResponseHandlingException {
        Validate.notNull(body);
        return body.parse(parser(entityType, defaultContentType, context));
    }

    /**
     * Creates a new HTTP response body XML entity parser for the specified
     * entity type
     * with {@code application/xml} as default content type
     * using a default JAXB context.
     * 
     * @param <E> The HTTP response body entity type.
     * @param entityType The HTTP response body entity type.
     * @return The created HTTP response body parser.
     * @throws HttpClientException If an error occurs creating the parser.
     */
    public static <E> @NotNull XmlHttpResponseBodyParser<E> parser(
            final @NotNull Class<? extends E> entityType)
    throws HttpClientException {
        Validate.notNull(entityType);
        try {
            return parser(
                    entityType,
                    XmlHttpResponseBodyParser.DEFAULT_CONTENT_TYPE,
                    JAXBContext.newInstance(entityType));
        } catch (final JAXBException e) {
            throw new HttpClientException(JAXB_CREATE_ERR, e);
        }
    }

    /**
     * Creates a new HTTP response body XML entity parser for the specified
     * entity type
     * with the specified default content type
     * using a default JAXB context.
     * 
     * @param <E> The HTTP response body entity type.
     * @param entityType The HTTP response body entity type.
     * @param defaultContentType The default content type to use if the HTTP
     * response does not specify one.
     * @return The created HTTP response body parser.
     * @throws HttpClientException If an error occurs creating the parser.
     */
    public static <E> @NotNull XmlHttpResponseBodyParser<E> parser(
            final @NotNull Class<? extends E> entityType,
            final @NotNull ContentType defaultContentType)
    throws HttpClientException {
        Validate.notNull(entityType);
        try {
            return parser(
                    entityType,
                    defaultContentType,
                    JAXBContext.newInstance(entityType));
        } catch (final JAXBException e) {
            throw new HttpClientException(JAXB_CREATE_ERR, e);
        }
    }

    /**
     * Creates a new HTTP response body XML entity parser for the specified
     * entity type.
     * 
     * @param <E> The HTTP response body entity type.
     * @param entityType The HTTP response body entity type.
     * @param defaultContentType The default content type to use if the HTTP
     * response does not specify one.
     * @param context The JAXB context to use.
     * @return The created HTTP response body parser.
     */
    public static <E> @NotNull XmlHttpResponseBodyParser<E> parser(
            final @NotNull Class<? extends E> entityType,
            final @NotNull ContentType defaultContentType,
            final @NotNull JAXBContext context) {
        return new JaxbBodyParser<>(entityType, defaultContentType, context);
    }

    /**
     * JAXB based implementation of {@code XmlHttpResponseBodyParser}.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-07
     * @param <E> The HTTP response body entity type.
     * @since 0.1
     */
    public static class JaxbBodyParser<E>
    extends AbstractHttpResponseBodyMediaTypeParser<E>
    implements XmlHttpResponseBodyParser<E> {

        /** The HTTP response body entity type. */
        private final @NotNull Class<? extends E> entityType;
        /** The default content type to use. */
        private final @NotNull ContentType defaultContentType;
        /** The JAXB context to use. */
        private final @NotNull JAXBContext context;

        /**
         * Creates a new instance.
         * 
         * @param entityType The HTTP response body entity type.
         * @param defaultContentType The default content type to use if the
         * HTTP response does not specify one.
         * @param context The JAXB context to use.
         */
        public JaxbBodyParser(
                final @NotNull Class<? extends E> entityType,
                final @NotNull ContentType defaultContentType,
                final @NotNull JAXBContext context) {
            super();
            this.entityType = Validate.notNull(entityType);
            this.defaultContentType = Validate.notNull(defaultContentType);
            this.context = Validate.notNull(context);
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
         * Returns the JAXB context to use.
         * 
         * @return The JAXB context to use.
         */
        protected @NotNull JAXBContext getContext() {
            return this.context;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected E parseSupportedContent(
                final @NotNull ContentType type,
                final @NotNull InputStream content,
                final long length)
        throws HttpResponseBodyParsingException {
            E result = null;
            try {
                final Unmarshaller unmarshaller = this.context.createUnmarshaller();
                final JAXBElement<? extends E> element = unmarshaller.unmarshal(
                        createSource(content, type),
                        this.entityType);
                if (element != null) {
                    result = element.getValue();
                }
            } catch (final JAXBException e) {
                throw new HttpResponseBodyParsingException("Error parsing HTTP response body", e);
            }
            return result;
        }

        /**
         * Creates a {@code StreamSource} for the HTTP response body
         * {@code InputStream}.
         * 
         * @param content The entity's content {@code InputStream}
         * @param type The entity's content type
         * @return The source to use for reading the entity's content
         */
        protected @NotNull StreamSource createSource(
                final @NotNull InputStream content,
                final @NotNull ContentType type) {
            final StreamSource source;
            if (type.getCharset() == null) {
                source = new StreamSource(content);
            } else {
                source = new StreamSource(new InputStreamReader(
                        content, type.getCharset()));
            }
            return source;
        }
    }
}
