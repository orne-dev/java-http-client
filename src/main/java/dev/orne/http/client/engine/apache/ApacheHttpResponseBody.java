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

import javax.validation.constraints.NotNull;

import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

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
    /** The response entity content type. */
    private final ContentType contentType;

    /**
     * Creates a new intance.
     * 
     * @param entity The delegated Apache HTTP client response entity.
     */
    public ApacheHttpResponseBody(
            final @NotNull HttpEntity entity) {
        super();
        this.entity = entity;
        final String typeHeader = entity.getContentType();
        if (typeHeader == null) {
            this.contentType = null;
        } else {
            this.contentType = ContentType.parse(typeHeader);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentType getContentType()
    throws HttpResponseHandlingException {
        return this.contentType;
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
}
