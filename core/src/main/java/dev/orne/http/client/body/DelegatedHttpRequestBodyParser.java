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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpResponseBodyParsingException;
import dev.orne.http.client.UnsupportedContentTypeException;

/**
 * HTTP response body parser that delegates on internal parsers based on the
 * media type of the HTTP response content.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @param <E> The HTTP response body entity type.
 * @since 0.1
 */
public class DelegatedHttpRequestBodyParser<E>
implements HttpResponseBodyMediaTypeParser<E> {

    /** The default content type. */
    private final @NotNull ContentType defaultContentType;
    /** The delegated HTTP response body parsers. */
    private final @NotNull Collection<@NotNull HttpResponseBodyMediaTypeParser<? extends E>> parsers;

    /**
     * Creates a new instance.
     * 
     * @param defaultContentType The default content type to use when response
     * does not include content type header.
     * @param parsers The delegated HTTP response body parsers.
     */
    @SafeVarargs
    public DelegatedHttpRequestBodyParser(
            final @NotNull ContentType defaultContentType,
            final @NotNull HttpResponseBodyMediaTypeParser<? extends E>... parsers) {
        this(defaultContentType, Arrays.asList(parsers));
    }

    /**
     * Creates a new instance.
     * 
     * @param defaultContentType The default content type to use when response
     * does not include content type header.
     * @param parsers The delegated HTTP response body parsers.
     */
    public DelegatedHttpRequestBodyParser(
            final @NotNull ContentType defaultContentType,
            final @NotNull Collection<@NotNull HttpResponseBodyMediaTypeParser<? extends E>> parsers) {
        super();
        this.defaultContentType = Validate.notNull(defaultContentType);
        this.parsers = new ArrayList<>(Validate.notNull(parsers));
        Validate.noNullElements(parsers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentType getDefaultContentType() {
        return this.defaultContentType;
    }

    /**
     * Returns a copy of the delegated HTTP response body parsers.
     * 
     * @return The delegated HTTP response body parsers.
     */
    public @NotNull Collection<HttpResponseBodyMediaTypeParser<? extends E>> getParsers() {
        return Collections.unmodifiableCollection(this.parsers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsMediaType(
            final @NotNull String mediaType) {
        for (final HttpResponseBodyMediaTypeParser<? extends E> parser : this.parsers) {
            if (parser.supportsMediaType(mediaType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the parser to use for the specified content type.
     * 
     * @param type The content type to return the parser for.
     * @return The parser suitable for specified content type.
     * @throws HttpResponseBodyParsingException If an error occurs finding the
     * parser.
     * @throws UnsupportedContentTypeException If no parser is available for
     * the specified content type. 
     */
    protected @NotNull HttpResponseBodyMediaTypeParser<? extends E> getParser(
            final @NotNull ContentType type)
    throws HttpResponseBodyParsingException {
        Validate.notNull(type);
        final String mediaType = type.getMediaType();
        for (final HttpResponseBodyMediaTypeParser<? extends E> parser : this.parsers) {
            if (parser.supportsMediaType(mediaType)) {
                return parser;
            }
        }
        throw new UnsupportedContentTypeException("Unsupported content type: " + type);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws UnsupportedContentTypeException If no parser is available for
     * the specified content type. 
     */
    @Override
    public E parse(
            final ContentType type,
            @NotNull InputStream content,
            long length)
    throws HttpResponseBodyParsingException {
        ContentType effectiveType = ObjectUtils.defaultIfNull(
                type,
                getDefaultContentType());
        return getParser(effectiveType).parse(effectiveType, content, length);
    }
}
