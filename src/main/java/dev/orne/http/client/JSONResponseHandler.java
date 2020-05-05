package dev.orne.http.client;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2020 Orne Developments
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

import org.apache.http.HttpEntity;
import org.apache.http.impl.client.AbstractResponseHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON response handler based on FasterXML's Jackson databind.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <E> The HTTP response entity type
 * @since 0.1
 */
public class JSONResponseHandler<E>
extends AbstractResponseHandler<E> {

    /** The JSON object mapper to use. */
    private final ObjectMapper mapper;
    /** The result value type. */
    private final Class<? extends E> valueType;

    /**
     * Creates a new instance.
     * 
     * @param valueType The result value type
     */
    public JSONResponseHandler(
            final Class<? extends E> valueType) {
        super();
        this.mapper = new ObjectMapper();
        this.valueType = valueType;
    }

    /**
     * Creates a new instance.
     * 
     * @param mapper The JSON object mapper to use
     * @param valueType The result value type
     */
    public JSONResponseHandler(
            final ObjectMapper mapper,
            final Class<? extends E> valueType) {
        super();
        this.mapper = mapper;
        this.valueType = valueType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E handleEntity(final HttpEntity entity)
    throws IOException {
        E result = null;
        final InputStream entityIS = entity.getContent();
        try {
            result = this.mapper.readValue(entityIS, valueType);
        } finally {
            entityIS.close();
        }
        return result;
    }
}
