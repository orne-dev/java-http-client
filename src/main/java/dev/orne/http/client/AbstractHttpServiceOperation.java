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
import java.net.URI;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract operation for {@code HttpServiceClient}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> The execution parameters type
 * @param <E> The HTTP response entity type
 * @param <R> The execution result type
 * @param <C> The client type
 * @since 0.1
 */
public abstract class AbstractHttpServiceOperation<
        P,
        E,
        R,
        C extends HttpServiceClient> {

    /** The response handler. */
    private ResponseHandler<E> responseHandler;
    /** The logger for this instance's actual class. */
    private Logger logger;

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
            final @NotNull C client)
    throws HttpClientException {
        return client.getBaseURI().resolve(requestURI);
    }

    /**
     * Executes the operation's HTTP request.
     * 
     * @param params The operation execution parameters
     * @param request The HTTP request
     * @param client The HTTP service client to use on execution
     * @return The operation execution result
     * @throws HttpClientException If an exception occurs executing the
     * request
     */
    protected R executeHttpRequest(
            final P params,
            final @NotNull HttpRequest request,
            final @NotNull C client)
    throws HttpClientException {
        try (final CloseableHttpResponse response = client.getClient().execute(
                client.getHost(),
                request,
                getHttpContext(params, client))) {
            return processHttpResponse(params, client, request, response);
        } catch (final IOException ioe) {
            throw processException(params, client, request, null, ioe);
        }
    }

    /**
     * Returns the HTTP context to use in the request for the specified
     * parameters and client. Default implementation returns {@code null}.
     * 
     * @param params The operation execution parameters
     * @param client The HTTP service client to use on execution
     * @return The HTTP context, or {@code null} to use the default one
     * @throws HttpClientException If an exception occurs creating the
     * HTTP context
     */
    protected HttpContext getHttpContext(
            final P params,
            final @NotNull C client)
    throws HttpClientException {
        return null;
    }

    /**
     * Returns the response handler for this operation. Multiple
     * calls to this method should return the same response handler
     * instance.
     * 
     * @return The response handler for this operation
     * @throws HttpClientException If an exception occurs creating the
     * response handler
     */
    protected @NotNull ResponseHandler<E> getResponseHandler()
    throws HttpClientException {
        synchronized (this) {
            if (this.responseHandler == null) {
                this.responseHandler = createResponseHandler();
            }
            return this.responseHandler;
        }
    }

    /**
     * Creates a new instance of response handler for this operation.
     * 
     * @return The response handler for this operation
     * @throws HttpClientException If an exception occurs creating the
     * response handler
     */
    protected abstract @NotNull ResponseHandler<E> createResponseHandler()
    throws HttpClientException;

    /**
     * Extracts the entity from the server's HTTP response.
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @return The response's entity
     * @throws HttpClientException If an error occurs extracting the entity
     */
    protected E extractResponseEntity(
            final P params,
            final @NotNull C client,
            final @NotNull HttpRequest request,
            final @NotNull HttpResponse response)
    throws HttpClientException {
        try {
            return getResponseHandler().handleResponse(response);
        } catch (final HttpResponseException hre) {
            return processHttpResponseException(params, client, request, response, hre);
        } catch (final IOException ioe) {
            throw processException(params, client, request, response, ioe);
        }
    }

    /**
     * Process the server response.
     * 
     * @param params The operation execution parameters
     * @param client The client the operation executed for
     * @param request The original HTTP request
     * @param response The server HTTP response
     * @return The operation execution result
     * @throws HttpClientException If an exception occurs processing the
     * response
     */
    protected R processHttpResponse(
            final P params,
            final @NotNull C client,
            final @NotNull HttpRequest request,
            final @NotNull HttpResponse response)
    throws HttpClientException {
        final E responseEntity = extractResponseEntity(params, client, request, response);
        return processResponseEntity(params, client, request, response, responseEntity);
    }

    /**
     * Process the server response.
     * 
     * @param params The operation execution parameters
     * @param client The client the operation executed for
     * @param request The original HTTP request
     * @param response The server HTTP response
     * @param responseEntity The server response's entity
     * @return The operation execution result
     * @throws HttpClientException If an exception occurs processing the
     * response
     */
    protected abstract R processResponseEntity(
            P params,
            @NotNull C client,
            @NotNull HttpRequest request,
            @NotNull HttpResponse response,
            E responseEntity)
    throws HttpClientException;

    /**
     * Process the exception occurred during the request execution.
     * 
     * @param params The operation execution parameters
     * @param client The client the operation tried to execute for
     * @param request The original HTTP request
     * @param response The server HTTP response, if any
     * @param exception The exception occurred during the request execution
     * @return The exception to be thrown
     */
    protected @NotNull HttpClientException processException(
            final P params,
            final @NotNull C client,
            final @NotNull HttpRequest request,
            final HttpResponse response,
            final @NotNull Exception exception) {
        final HttpClientException result;
        if (exception instanceof HttpClientException) {
            result = (HttpClientException) exception;
        } else {
            result = new HttpClientException(
                    "Unexpected exception performing the request",
                    exception);
        }
        return result;
    }

    /**
     * Process the HTTP response exception relative to a rejected request.
     * 
     * @param params The operation execution parameters
     * @param client The client the operation tried to execute for
     * @param request The HTTP request
     * @param response The HTTP response
     * @param exception The HTTP response exception
     * @return The fallback entity
     * @throws HttpClientException If the rejected HTTP request has no
     * special meaning and cannot be extracted to a valid entity
     * @throws AuthenticationRequiredException If the server responded with
     * an {@code 401 Unauthorized} status code
     */
    protected E processHttpResponseException(
            final P params,
            final @NotNull C client,
            final @NotNull HttpRequest request,
            final @NotNull HttpResponse response,
            final @NotNull HttpResponseException exception)
    throws HttpClientException {
        if (exception.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
            throw new AuthenticationRequiredException(
                    "Authentication required",
                    exception);
        } else {
            throw new HttpClientException(
                    "Rejected HTTP request",
                    exception);
        }
    }

    /**
     * Returns the logger for this instance's actual class.
     * 
     * @return The logger for this instance's actual class
     */
    protected @NotNull Logger getLogger() {
        synchronized (this) {
            if (this.logger == null) {
                this.logger = LoggerFactory.getLogger(getClass());
            }
            return this.logger;
        }
    }
}
