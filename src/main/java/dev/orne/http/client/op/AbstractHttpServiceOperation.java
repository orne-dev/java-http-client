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

import javax.validation.constraints.NotNull;

import dev.orne.http.StatusCodes;
import dev.orne.http.client.AuthenticationRequiredException;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpResponseStatusException;
import dev.orne.http.client.HttpServiceClient;
import dev.orne.http.client.engine.HttpResponse;

/**
 * Base abstract class for HTTP client operation with common utility
 * methods for HTTP response handling.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @param <R> The execution result type
 * @since 0.1
 */
public abstract class AbstractHttpServiceOperation<R> {

    /**
     * Returns the operation execution request URI against client's base URI.
     * 
     * @param requestURI The operation execution request URI
     * @param client The client to execute the operation for
     * @return The operation execution request absolute URI
     * @throws HttpClientException If an error occurs generating the
     * execution request URI
     */
    protected @NotNull URI resolveRequestURI(
            final @NotNull URI requestURI,
            final @NotNull HttpServiceClient client)
    throws HttpClientException {
        return client.getBaseURI().resolve(requestURI);
    }

    /**
     * Verifies that the status code is an acceptable one.
     * <p>
     * Default implementation accepts any status code in the success [200, 300)
     * range.
     * <p>
     * Operations of services that return status codes outside the success
     * range that should be processed as an expected (recognized) response must
     * overwrite this method to prevent exception throwing for such status
     * codes.
     * 
     * @param response The HTTP response.
     * @throws HttpResponseStatusException If the status code is not
     * acceptable.
     */
    protected void processResponseStatus(
            final @NotNull HttpResponse response)
    throws HttpResponseStatusException {
        if (!StatusCodes.isSuccess(response.getStatusCode())) {
            throw new HttpResponseStatusException(
                    response.getStatusCode(),
                    response.getStatusReason());
        }
    }

    /**
     * Process the HTTP response status exception.
     * <p>
     * Operations should overwrite this method to process error status codes
     * with special meanings in the service API context, returning an operation
     * result or throwing an specialized exception as appropriate.
     * <p>
     * Note that for status codes with specials meanings which bodies should be
     * processed by the standard means the
     * {@link #processResponseStatus(HttpResponse)} method should
     * be overwritten instead.
     * 
     * @param response The HTTP response.
     * @param exception The HTTP response exception.
     * @return The operation result.
     * @throws HttpResponseStatusException If the rejected HTTP request has no
     * special meaning and cannot be extracted to a valid result.
     * @throws AuthenticationRequiredException If the server responded with
     * an {@code 401 Unauthorized} status code.
     */
    protected R processResponseStatusException(
            final @NotNull HttpResponse response,
            final @NotNull HttpResponseStatusException exception)
    throws HttpClientException {
        if (exception.getStatusCode() == StatusCodes.UNAUTHORIZED) {
            throw new AuthenticationRequiredException(
                    "Authentication required",
                    exception);
        } else {
            throw exception;
        }
    }
}
