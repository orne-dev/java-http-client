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

import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpEntityContainer;

import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.engine.HttpResponse;
import dev.orne.http.client.engine.HttpResponseBody;

/**
 * Implementation of {@code HttpResponse} based on
 * Apache HTTP Client 5.x.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public class ApacheHttpResponse
implements HttpResponse {

    /** The Apache HTTP client response. */
    private final @NotNull org.apache.hc.core5.http.HttpResponse delegate;

    /**
     * Creates a new instance.
     * 
     * @param delegated The delegated Apache HTTP client response.
     */
    public ApacheHttpResponse(
            final @NotNull org.apache.hc.core5.http.HttpResponse delegated) {
        super();
        this.delegate = Validate.notNull(delegated);
    }

    /**
     * Returns the delegated Apache HTTP client response.
     * 
     * @return The Apache HTTP client response.
     */
    protected @NotNull org.apache.hc.core5.http.HttpResponse getDelegate() {
        return this.delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStatusCode() {
        return this.delegate.getCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatusReason() {
        return this.delegate.getReasonPhrase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsHeader(
            final @NotNull String header)
    throws HttpClientException {
        Validate.notNull(header);
        return this.delegate.containsHeader(header);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String[] getHeader(
            final @NotNull String header)
    throws HttpClientException {
        Validate.notNull(header);
        return Stream.of(this.delegate.getHeaders(header))
            .map(Header::getValue)
            .toArray(String[]::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFirstHeaderValue(
            final @NotNull String header)
    throws HttpClientException {
        Validate.notNull(header);
        final Header tmp = this.delegate.getFirstHeader(header);
        if (tmp == null) {
            return null;
        } else {
            return tmp.getValue();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLastHeaderValue(
            final @NotNull String header)
    throws HttpClientException {
        Validate.notNull(header);
        final Header tmp = this.delegate.getLastHeader(header);
        if (tmp == null) {
            return null;
        } else {
            return tmp.getValue();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HttpResponseBody getBody()
    throws HttpClientException {
        HttpResponseBody result = null;
        if (this.delegate instanceof HttpEntityContainer) {
            final HttpEntity entity = ((HttpEntityContainer) this.delegate).getEntity();
            if (entity == null) {
                return null;
            } else {
                return new ApacheHttpResponseBody(entity);
            }
        }
        return result;
    }
}
