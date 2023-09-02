package dev.orne.http.client.op;

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
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletionException;

import javax.validation.constraints.NotNull;

import dev.orne.http.client.FutureUtils;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpResponseHandlingException;
import dev.orne.http.client.HttpResponseStatusException;
import dev.orne.http.client.StatedHttpServiceClient;
import dev.orne.http.client.engine.HttpResponse;
import dev.orne.http.client.engine.HttpResponseBody;
import dev.orne.http.client.engine.HttpRequest;

/**
 * Abstract status dependent operation for {@code StatedHttpServiceClient}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @param <P> The execution parameters type
 * @param <S> The client status type
 * @param <E> The HTTP response entity type
 * @param <R> The execution result type
 * @since 0.1
 */
public abstract class AbstractStatusDependentOperation<P, S, E, R>
extends AbstractHttpServiceOperation<R>
implements StatusDependentOperation<P, R, S> {

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CompletionStage<R> execute(
            final P params,
            final @NotNull S status,
            final @NotNull StatedHttpServiceClient<? extends S> client) {
        try {
            final URI requestURI = resolveRequestURI(
                    getRequestURI(params, status),
                    client);
            final OperationResponseHandler<R> handler =
                    createResponseHandler(params, status);
            return client.getEngine().executeHttpRequest(
                    requestURI,
                    getRequestMethod(),
                    request -> prepareRequest(params, status, request),
                    handler)
            .thenApply(nop -> {
                try {
                    return handler.getResult();
                } catch (HttpClientException e) {
                    throw new CompletionException(e);
                }
            });
        } catch (final HttpClientException e) {
            return FutureUtils.completableFailure(e);
        }
    }

    /**
     * Returns the operation HTTP request URI.
     * The URI can be relative to client's base URI.
     * 
     * @param params The request parameters
     * @param status The client status.
     * @return The URI of the operation HTTP request
     * @throws HttpClientException If an error occurs generating the
     * relative URI
     */
    protected abstract @NotNull URI getRequestURI(
            P params,
            @NotNull S status)
    throws HttpClientException;

    /**
     * Returns the HTTP method used on this operation.
     * 
     * @return The HTTP method to use.
     */
    protected abstract @NotNull String getRequestMethod();

    /**
     * Prepares the HTTP request.
     * 
     * @param params The operation execution parameters.
     * @param status The client status.
     * @param request The HTTP request.
     * @throws HttpClientException If an exception occurs preparing the request.
     */
    protected abstract void prepareRequest(
            final P params,
            final @NotNull S status,
            final @NotNull HttpRequest request)
    throws HttpClientException;

    /**
     * Returns the response handler for this operation.
     * 
     * @param params The operation execution parameters.
     * @param status The current client status.
     * @return The response handler for this operation.
     * @throws HttpClientException If an exception occurs creating the
     * response handler.
     */
    protected @NotNull OperationResponseHandler<R> createResponseHandler(
            final P params,
            final @NotNull S status)
    throws HttpClientException {
        return new HandlerWrapper(params, status);
    }

    /**
     * Handles the HTTP response and return the result of the operation.
     * 
     * @param params The operation execution parameters.
     * @param status The current client status.
     * @param response The HTTP response.
     * @return The operation's result.
     * @throws HttpClientException If an error occurs during result generation.
     */
    protected R handleResponse(
            P params,
            @NotNull S status,
            @NotNull HttpResponse response)
    throws HttpClientException {
        try {
            processResponseStatus(response);
            final HttpResponseBody body = response.getBody();
            final E entity;
            if (body == null) {
                entity = null;
            } else {
                entity = parseResponse(params, status, response, body);
            }
            return processResponse(params, status, entity, response);
        } catch (HttpResponseStatusException e) {
            final HttpResponseBody body = response.getBody();
            if (body != null) {
                body.discard();
            }
            return processResponseStatusException(response, e);
        }
    }

    /**
     * Parses the HTTP response entity.
     * 
     * @param params The operation execution parameters.
     * @param status The current client status.
     * @param response The HTTP response.
     * @param body The HTTP response body.
     * @return The HTTP response entity.
     * @throws HttpResponseHandlingException If an error occurs parsing the entity.
     */
    protected abstract E parseResponse(
            P params,
            @NotNull S status,
            @NotNull HttpResponse response,
            @NotNull HttpResponseBody body)
    throws HttpResponseHandlingException;

    /**
     * Processes the HTTP response and generates the result.
     * 
     * @param params The operation execution parameters.
     * @param status The current client status.
     * @param entity The HTTP response entity.
     * @param response The HTTP response body.
     * @return The operation execution result.
     * @throws HttpClientException If an error occurs processing the
     * response.
     */
    protected abstract R processResponse(
            P params,
            @NotNull S status,
            E entity,
            @NotNull HttpResponse response)
    throws HttpClientException;

    /**
     * Internal implementation of {@code OperationResponseHandler} for
     * status dependent operations.
     * <p>
     * Delegates on operations {@code handleResponse()} method.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since AbstractStatusDependentOperation 1.0
     * @see AbstractStatusDependentOperation#handleResponse(Object, Object, HttpResponse)
     */
    protected class HandlerWrapper
    implements OperationResponseHandler<R> {

        /** The operation execution parameters. */
        private final P params;
        /** The current client status. */
        private final @NotNull S status;
        /** The operations result. */
        private R result;
        /** The error occurred during response handling. */
        private HttpClientException error;

        /**
         * Creates a new instance.
         * 
         * @param params The operation execution parameters.
         * @param status The current client status.
         */
        public HandlerWrapper(
                final P params,
                final @NotNull S status) {
            super();
            this.params = params;
            this.status = status;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void handle(
                final @NotNull HttpResponse response) {
            try {
                result = handleResponse(this.params, this.status, response);
            } catch (final HttpClientException e) {
                error = e;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public R getResult()
        throws HttpClientException {
            if (this.error != null) {
                throw this.error;
            }
            return this.result;
        }
    }
}
