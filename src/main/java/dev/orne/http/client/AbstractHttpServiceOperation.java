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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract operation for {@code HttpServiceClient}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <E> El HTTP response entity type
 * @param <R> El execution result type
 * @since 0.1
 */
public abstract class AbstractHttpServiceOperation<
        P,
        E,
        R> {

    /** The relative URI of the operation. */
    @Nonnull
    private final URI relativeURI;
    /** The response handler. */
    @Nullable
    private ResponseHandler<E> responseHandler;
    /** The logger for this instance's actual class. */
    @Nullable
    private Logger logger;

    /**
     * Creates a new instance.
     * 
     * @param operationURI The relative URI of the operation
     */
    public AbstractHttpServiceOperation(
            @Nonnull
            final URI operationURI) {
        super();
        if (operationURI == null) {
            throw new IllegalArgumentException("Parameter 'operationURI' is required.");
        }
        this.relativeURI = operationURI;
    }

    /**
     * Returns the relative URI of the operation.
     * 
     * @return The relative URI of the operation
     */
    @Nonnull
    public URI getRelativeURI() {
        return this.relativeURI;
    }

    /**
     * Replaces in the URI's request path the variable passed as argument
     * with the value passed as argument. The variable must be in the form
     * {@code \u007BvarName\u007D} to be replaced.
     * 
     * @param builder The URI builder
     * @param varName The variable name
     * @param value The variable value
     */
    protected void replacePathVariable(
            @Nonnull
            final URIBuilder builder,
            @Nonnull
            final String varName,
            @Nonnull
            final String value) {
        final List<String> pathSegments = builder.getPathSegments();
        final List<String> resultPathSegments = new ArrayList<>(
                pathSegments.size());
        for (final String pathSegment : pathSegments) {
            resultPathSegments.add(pathSegment.replaceAll(
                    "\\{" + varName + "\\}",
                    value));
        }
        builder.setPathSegments(resultPathSegments);
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
    @Nullable
    protected R executeHttpRequest(
            @Nullable
            final P params,
            @Nonnull
            final HttpRequest request,
            @Nonnull
            final HttpServiceClient client)
    throws HttpClientException {
        try {
            final CloseableHttpResponse response = client.getClient().execute(
                    client.getHost(), request);
            return processHttpResponse(params, client, request, response);
        } catch (final ClientProtocolException cpe) {
            throw processException(params, client, request, null, cpe);
        } catch (final IOException ioe) {
            throw processException(params, client, request, null, ioe);
        }
    }

    /**
     * @return The response handler for this operation
     */
    @Nonnull
    protected ResponseHandler<E> getResponseHandler() {
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
     */
    @Nonnull
    protected abstract ResponseHandler<E> createResponseHandler();

    /**
     * Extracts the entity from the server's HTTP response.
     * 
     * @param request The original HTTP request
     * @param response The HTTP response
     * @return The response's entity
     * @throws HttpClientException If an error occurs extracting the entity
     * @throws HttpResponseException If the server responded with an error
     * status code
     * @throws HttpAuthenticationRequiredException If the server responded with
     * an {@code 401 Unauthorized} status code
     */
    @Nullable
    protected E extractResponseEntity(
            @Nonnull
            HttpRequest request,
            @Nonnull
            HttpResponse response)
    throws HttpClientException {
        try {
            return getResponseHandler().handleResponse(response);
        } catch (final org.apache.http.client.HttpResponseException hre) {
            if (hre.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                throw new HttpAuthenticationRequiredException("Authentication required",
                        hre, request, response);
            } else {
                throw new HttpResponseException("Rejected HTTP request",
                        hre, request, response);
            }
        } catch (final ClientProtocolException cpe) {
            throw new HttpResponseException("Client protocol exception while connecting to server.",
                    cpe, request, response);
        } catch (final IOException ioe) {
            throw new HttpResponseException("IO error while connecting to server.",
                    ioe, request, response);
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
    @Nullable
    protected R processHttpResponse(
            @Nullable
            P params,
            @Nonnull
            HttpServiceClient client,
            @Nonnull
            HttpRequest request,
            @Nonnull
            CloseableHttpResponse response)
    throws HttpClientException {
        try {
            final E responseEntity = extractResponseEntity(request, response);
            return processHttpResponse(params, client, request, response, responseEntity);
        } catch (final HttpClientException hce) {
            throw processException(params, client, request, response, hce);
        } finally {
            try {
                response.close();
            } catch (final IOException ignored) {
                // Ignore close error
            }
        }
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
    @Nullable
    protected abstract R processHttpResponse(
            @Nullable
            P params,
            @Nonnull
            HttpServiceClient client,
            @Nonnull
            HttpRequest request,
            @Nonnull
            HttpResponse response,
            @Nullable
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
    @Nonnull
    protected HttpClientException processException(
            @Nullable
            P params,
            @Nonnull
            HttpServiceClient client,
            @Nonnull
            HttpRequest request,
            @Nullable
            HttpResponse response,
            @Nonnull
            Exception exception) {
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
     * Returns the logger for this instance's actual class.
     * 
     * @return The logger for this instance's actual class
     */
    @Nonnull
    protected Logger getLogger() {
        synchronized (this) {
            if (this.logger == null) {
                this.logger = LoggerFactory.getLogger(getClass());
            }
            return this.logger;
        }
    }
}
