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

import java.net.URI;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.Header;
import org.apache.http.HttpRequest;

/**
 * Abstract status independent operation for {@code HttpServiceClient}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <E> El HTTP response entity type
 * @param <R> El execution result type
 * @since 0.1
 */
public abstract class AbstractStatusIndependentOperation<P, E, R>
extends AbstractHttpServiceOperation<P, E, R, HttpServiceClient>
implements StatusIndependentOperation<P, R> {

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public R execute(
            @Nullable
            final P params,
            @Nonnull
            final HttpServiceClient client)
    throws HttpClientException {
        final URI requestURI = resolveRequestURI(
                getRequestURI(params),
                client);
        final HttpRequest request = createRequest(
                requestURI,
                params);
        final List<Header> requestHeaders = createHeaders(params);
        for (final Header header : requestHeaders) {
            request.addHeader(header);
        }
        return executeHttpRequest(params, request, client);
    }

    /**
     * Creates the HTTP request for the execution.
     * 
     * @param params The operation execution parameters
     * @return The HTTP request to perform
     * @throws HttpClientException If an exception occurs generating the
     * request
     */
    @Nonnull
    protected abstract HttpRequest createRequest(
            @Nonnull
            final URI requestURI,
            @Nullable
            final P params)
    throws HttpClientException;

    /**
     * Returns the operation execution  request URI. The URI should be
     * relative to client's base URI.
     * 
     * @param params The request parameters
     * @return The relative URI of the operation execution request
     * @throws HttpClientException If an error occurs generating the
     * relative URI
     */
    @Nonnull
    protected abstract URI getRequestURI(
            @Nullable
            P params)
    throws HttpClientException;

    /**
     * Creates the request headers.
     * 
     * @param params The operation execution parameters
     * @return The request headers
     * @throws HttpClientException If an exception occurs generating the
     * headers
     */
    @Nonnull
    protected List<Header> createHeaders(
            @Nullable
            final P params)
    throws HttpClientException {
        return Collections.emptyList();
    }
}
