package dev.orne.http.client.engine.apache;

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
import java.util.LinkedHashMap;

import javax.validation.constraints.NotNull;

import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeaderValueParser;
import org.apache.hc.core5.http.message.ParserCursor;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpResponseHandlingException;
import dev.orne.http.client.engine.HttpResponseBody;

/**
 * Implementation of {@code HttpResponseBody} based on
 * Apache HTTP Client 5.x.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
class ApacheHttpResponseBody
implements HttpResponseBody {

    /** The delegated Apache HTTP client response entity. */
    private final @NotNull HttpEntity entity;

    /**
     * Creates a new intance.
     * 
     * @param entity The delegated Apache HTTP client response entity.
     */
    public ApacheHttpResponseBody(
            final @NotNull HttpEntity entity) {
        super();
        this.entity = entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentType getContentType()
    throws HttpResponseHandlingException {
        final String typeHeader = entity.getContentType();
        final ContentType result;
        if (typeHeader == null) {
            result = null;
        } else {
            result = parseContentType(typeHeader);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getContentLength()
    throws HttpResponseHandlingException {
        return this.entity.getContentLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getContent()
    throws HttpResponseHandlingException {
        try {
            return this.entity.getContent();
        } catch (UnsupportedOperationException | IOException e) {
            throw new HttpResponseHandlingException("Error retrieving HTTP response body content", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void discard()
    throws HttpResponseHandlingException {
        try {
            EntityUtils.consume(this.entity);
        } catch (final IOException e) {
            throw new HttpResponseHandlingException("Error discarding HTTP response body content", e);
        }
    }

    /**
     * Parses the specified content type header and creates a
     * {@code ContentType} instance with the header values.
     * 
     * @param header The content type header value.
     * @return The parsed content type.
     * @throws HttpResponseHandlingException
     */
    public static ContentType parseContentType(
            final String header)
    throws HttpResponseHandlingException {
        if (header == null || header.trim().isEmpty()) {
            return null;
        }
        final ParserCursor cursor = new ParserCursor(0, header.length());
        final HeaderElement[] elements = BasicHeaderValueParser.INSTANCE.parseElements(header, cursor);
        if (elements.length != 1) {
            throw new HttpResponseHandlingException("Received illegal Content-Type header: " + header);
        }
        final HeaderElement element = elements[0];
        if (element.getValue() != null) {
            throw new HttpResponseHandlingException("Received illegal Content-Type header: " + header);
        }
        final String mimeType = element.getName();
        final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        for (NameValuePair parameter : element.getParameters()) {
            parameters.put(parameter.getName(), parameter.getValue());
        }
        return new ContentType(mimeType, parameters);
    }
}
