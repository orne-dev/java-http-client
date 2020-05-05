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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

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
extends AbstractHttpServiceOperation<P, E, R>
implements StatusIndependentOperation<P, R> {

    /**
     * Creates a new instance.
     * 
     * @param operationURI The relative URI of the operation
     */
    public AbstractStatusIndependentOperation(
            @Nonnull
            final URI operationURI) {
        super(operationURI);
    }

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
            final HttpServiceClient client)
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
            final HttpServiceClient client)
    throws HttpClientException {
        final URI absoluteURI = client.getBaseURI().resolve(getRelativeURI());
        final URIBuilder builder = new URIBuilder(absoluteURI);
        replacePathVariables(builder, params);
        try {
            return builder.build();
        } catch (final URISyntaxException use) {
            throw new HttpClientException(use);
        }
    }

    /**
     * <p>Replaces the variables of the operation URI with the values for this
     * request, based in the request parameters. Operations with variables in
     * their URI must override this method to replace variables with the
     * appropriate values with calls to {@code replacePathVariable()}.</p>
     * 
     * <p>For example:</P>
     * <pre>
     * {@literal @}Override
     * protected void replacePathVariables(
     *         final URIBuilder builder,
     *         final Params params)
     * throws HttpClientException {
     *     replacePathVariable(builder, "myVar", params.getMyVarValue());
     * }
     * </pre>
     * 
     * @param builder The URI builder
     * @param params The request params
     * @throws HttpClientException If an error occurs obtaining the variable
     * values
     */
    protected void replacePathVariables(
            @Nonnull
            final URIBuilder builder,
            @Nullable
            final P params)
    throws HttpClientException {
        // Do nothing by default. Override if needed
    }

    /**
     * Creates the request parameters.
     * 
     * @param params The operation execution parameters
     * @return The request parameters
     * @throws HttpClientException If an exception occurs generating the
     * parameters
     */
    @Nonnull
    protected List<NameValuePair> createParams(
            @Nullable
            final P params)
    throws HttpClientException {
        return new ArrayList<>();
    }

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
        return new ArrayList<>();
    }
}
