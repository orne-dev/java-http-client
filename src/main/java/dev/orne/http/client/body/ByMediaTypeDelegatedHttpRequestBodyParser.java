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

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;

import dev.orne.http.ContentType;

/**
 * HTTP response body parser that delegates on an internal parser based on the
 * media type of the HTTP response content. 
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @param <E> The HTTP response body entity type.
 * @since 0.1
 */
public class ByMediaTypeDelegatedHttpRequestBodyParser<E>
implements HttpResponseBodyMediaTypeParser<E> {

    /** The delegated HTTP response body parsers. */
    private @NotNull Collection<@NotNull HttpResponseBodyMediaTypeParser<E>> parsers;

    /**
     * Creates a new instance.
     * 
     * @param parsers The delegated HTTP response body parsers.
     */
    @SafeVarargs
    public ByMediaTypeDelegatedHttpRequestBodyParser(
            final @NotNull HttpResponseBodyMediaTypeParser<E>... parsers) {
        this(Arrays.asList(parsers));
    }

    /**
     * Creates a new instance.
     * 
     * @param parsers The delegated HTTP response body parsers.
     */
    public ByMediaTypeDelegatedHttpRequestBodyParser(
            final @NotNull Collection<@NotNull HttpResponseBodyMediaTypeParser<E>> parsers) {
        super();
        Validate.notNull(parsers);
        Validate.noNullElements(parsers);
        this.parsers = new ArrayList<>(parsers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsMediaType(
            final @NotNull String mediaType) {
        for (final HttpResponseBodyMediaTypeParser<E> parser : this.parsers) {
            if (parser.supportsMediaType(mediaType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws UnsupportedContentTypeException If no parser is available for
     * the specified content type. 
     */
    @Override
    public E parse(
            final @NotNull ContentType type,
            final @NotNull InputStream content,
            final long length)
    throws HttpResponseBodyParsingException {
        final String mediaType = type.getMediaType();
        for (final HttpResponseBodyMediaTypeParser<E> parser : this.parsers) {
            if (parser.supportsMediaType(mediaType)) {
                return parser.parse(type, content, length);
            }
        }
        throw new UnsupportedContentTypeException("Unsupported content type: " + type);
    }
}
