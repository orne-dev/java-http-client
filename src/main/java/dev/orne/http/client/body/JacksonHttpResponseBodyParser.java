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
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.orne.http.ContentType;
import dev.orne.http.MediaTypes;

/**
 * Jackson based HTTP response JSON body parser. 
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @param <E> The HTTP response body entity type.
 * @since 0.1
 */
public class JacksonHttpResponseBodyParser<E>
implements HttpResponseBodyMediaTypeParser<E> {

    /** The Jackson object mapper to use. */
    private final @NotNull ObjectMapper mapper;
    /** The HTTP body content entity type. */
    private final @NotNull Class<? extends E> entityType;

    /**
     * Creates a new instance with a default Jackson object mapper.
     * 
     * @param entityType The HTTP body content entity type.
     */
    public JacksonHttpResponseBodyParser(
            final @NotNull Class<? extends E> entityType) {
        this(new ObjectMapper(), entityType);
    }

    /**
     * Creates a new instance.
     * 
     * @param mapper The Jackson object mapper to use.
     * @param entityType The HTTP body content entity type.
     */
    public JacksonHttpResponseBodyParser(
            final @NotNull ObjectMapper mapper,
            final @NotNull Class<? extends E> entityType) {
        super();
        this.mapper = Validate.notNull(mapper);
        this.entityType = Validate.notNull(entityType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsMediaType(
            final @NotNull String mediaType) {
        return MediaTypes.Application.JSON.equalsIgnoreCase(mediaType) ||
                mediaType.toLowerCase().endsWith("+json");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E parse(
            final @NotNull ContentType type,
            final @NotNull InputStream content,
            final long length)
    throws HttpResponseBodyParsingException {
        final InputStreamReader reader = new InputStreamReader(
                content,
                ObjectUtils.defaultIfNull(type.getCharset(), StandardCharsets.UTF_8));
        try {
            return this.mapper.readValue(reader, this.entityType);
        } catch (IOException e) {
            throw new HttpResponseBodyParsingException("Error parsing HTTP response body", e);
        }
    }
}
