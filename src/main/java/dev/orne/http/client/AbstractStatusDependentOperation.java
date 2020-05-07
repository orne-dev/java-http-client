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
import org.apache.http.NameValuePair;

/**
 * Abstract status dependent operation for {@code StatedHttpServiceClient}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <E> El HTTP response entity type
 * @param <R> El execution result type
 * @param <S> The client status type
 * @since 0.1
 */
public abstract class AbstractStatusDependentOperation<P, E, R, S>
extends AbstractHttpServiceOperation<P, E, R>
implements StatusDependentOperation<P, R, S> {

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public R execute(
            @Nullable
            final P params,
            @Nonnull
            final StatedHttpServiceClient<S> client)
    throws HttpClientException {
        final HttpRequest request = createRequest(params, client);
        return executeHttpRequest(params, request, client);
    }

    /**
     * Creates the HTTP request for the execution.
     * 
     * @param params The operation execution parameters
     * @param client The client to execute the operation for
     * @return The HTTP request to perform
     * @throws HttpClientException If an exception occurs generating the
     * request
     */
    @Nonnull
    protected abstract HttpRequest createRequest(
            @Nullable
            final P params,
            @Nonnull
            final StatedHttpServiceClient<S> client)
    throws HttpClientException;

    /**
     * Returns the URI for this request based on the operation URI.
     * The default implementation returns the operation URI unchanged.
     * If the URI contains template parameters in the form of
     * {@code \u007BvarName\u007D} calls to
     * {@code replacePathVariable(builder, varName, value)}
     * in an overridden version of {@code replacePathVariables()}.
     * 
     * @param params The request parameters
     * @param client The client to execute the operation for
     * @return The request final URI
     * @throws HttpClientException If an error occurs generating the
     * request URI
     */
    @Nonnull
    protected URI getRequestURI(
            @Nullable
            final P params,
            @Nonnull
            final StatedHttpServiceClient<S> client)
    throws HttpClientException {
        return client.getBaseURI().resolve(
                getRelativeURI(params, client.ensureInitialized()));
    }

    /**
     * Returns the relative URI of the operation.
     * 
     * @param params The request parameters
     * @param status The client status
     * @return The relative URI of the operation
     * @throws HttpClientException If an error occurs generating the
     * relative URI
     */
    @Nonnull
    protected abstract URI getRelativeURI(
            @Nullable
            P params,
            @Nonnull
            S status)
    throws HttpClientException;

    /**
     * Creates the request parameters.
     * 
     * @param params The operation execution parameters
     * @param status The client status
     * @return The request parameters
     * @throws HttpClientException If an exception occurs generating the
     * parameters
     */
    @Nonnull
    protected List<NameValuePair> createParams(
            @Nullable
            final P params,
            @Nonnull
            final S status)
    throws HttpClientException {
        return Collections.emptyList();
    }

    /**
     * Creates the request headers.
     * 
     * @param params The operation execution parameters
     * @param status The client status
     * @return The request headers
     * @throws HttpClientException If an exception occurs generating the
     * headers
     */
    @Nonnull
    protected List<Header> createHeaders(
            @Nullable
            final P params,
            @Nonnull
            final S status)
    throws HttpClientException {
        return Collections.emptyList();
    }
}
