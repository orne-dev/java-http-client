package dev.orne.http.client.example.ipapi;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;

import dev.orne.http.ContentType;
import dev.orne.http.MediaTypes;
import dev.orne.http.Methods;
import dev.orne.http.UriBuilder;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpResponseBodyParsingException;
import dev.orne.http.client.body.DelegatedHttpRequestBodyParser;
import dev.orne.http.client.body.HttpResponseBodyParser;
import dev.orne.http.client.body.JacksonHttpBody;
import dev.orne.http.client.body.JaxbHttpBody;
import dev.orne.http.client.engine.HttpRequest;
import dev.orne.http.client.engine.HttpResponse;
import dev.orne.http.client.op.AbstractStatusDependentOperation;
import dev.orne.http.client.op.AuthenticatedOperation;

/**
 * Abstract {@code IpAPI} operation that takes care of common parameters
 * processing and  client.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see {@linkplain https://ipapi.com/}
 */
public abstract class IpapiAbstractOperation<P extends IpapiParams, E, R>
extends AbstractStatusDependentOperation<P, IpapiClientStatus, E, R>
implements AuthenticatedOperation<P, R, IpapiClientStatus> {

    /**
     * Creates a parser that supports both JSON and XML formats form the
     * specified response entity type.
     * 
     * @param <E> The response entity type.
     * @param entityType The response entity type.
     * @return The created response body entity parser.
     * @throws HttpClientException If an error occurs creating the parsers.
     */
    protected static <E> @NotNull HttpResponseBodyParser<E> createParser(
            final @NotNull Class<E> entityType)
    throws HttpClientException {
        return new DelegatedHttpRequestBodyParser<>(
                ContentType.of(MediaTypes.Application.JSON, StandardCharsets.UTF_8),
                JacksonHttpBody.parser(entityType),
                JaxbHttpBody.parser(entityType));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Adds the access key parameter and the provided common parameters.
     * <p>
     * To customize operation parameters override
     * {@code configureOperationUri()} method.
     * 
     * @see #configureOperationUri(IpapiParams, IpapiClientStatus, URIBuilderOld)
     * @see #configureRequestUri(IpapiParams, URIBuilderOld)
     */
    @Override
    protected @NotNull URI getRequestURI(
            final P params,
            final @NotNull IpapiClientStatus status)
    throws HttpClientException {
        final UriBuilder builder = UriBuilder.create();
        configureOperationUri(params, status, builder);
        try {
            return builder.build();
        } catch (URISyntaxException e) {
            throw new HttpClientException(e);
        }
    }

    /**
     * Adds the access key parameter and the provided common parameters.
     * 
     * @param params The request parameters.
     * @param status The client status.
     * @param builder The request URI builder.
     */
    protected void configureOperationUri(
            final P params,
            final @NotNull IpapiClientStatus status,
            final @NotNull UriBuilder builder) {
        builder.addParameter("access_key", status.getAccessKey());
        configureRequestUri(params, builder);
    }

    /**
     * Sets the global request URI parameters based on passed common
     * parameters.
     * 
     * @param params The global IpAPI parameters.
     * @param builder The request URI builder.
     */
    protected void configureRequestUri(
            final IpapiParams params,
            final @NotNull UriBuilder builder) {
        if (params != null) {
            if (params.getFormat() != null) {
                builder.addParameter("output", params.getFormat());
            }
            if (params.getFields() != null && params.getFields().length > 0) {
                builder.addParameter(
                        "fields",
                        String.join(",", params.getFields()));
            }
            if (params.getLanguage() != null) {
                builder.addParameter("language", params.getLanguage());
            }
            if (params.isLookupHostName()) {
                builder.addParameter("hostname", "1");
            }
            if (params.isRetrieveSecurityData()) {
                builder.addParameter("security", "1");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull String getRequestMethod() {
        return Methods.GET;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareRequest(
            final P params,
            final @NotNull IpapiClientStatus status,
            final @NotNull HttpRequest request)
    throws HttpClientException {
        // NOP
    }

    /**
     * Processes the API error returned by the service.
     * 
     * @param error The API error.
     * @param response The HTTP response.
     * @return The exception to throw from the {@code processResponse()}
     * method.
     * @see #processResponse(Object, Object, Object, HttpResponse)
     */
    protected @NotNull HttpClientException processApiError(
            final @NotNull IpapiError error,
            final @NotNull HttpResponse response) {
        final IpapiError.ErrorInfo info = error.getInfo();
        switch (info.getTypeEnum()) {
            case NOT_FOUND:
            case MISSING_ACCESS_KEY:
                return new IpapiClient.MissingAccessKeyException(
                        info.getCause());
            case INVALID_ACCESS_KEY:
                return new IpapiClient.InvalidAccessKeyException(
                        info.getCause());
            default:
                return new HttpResponseBodyParsingException(
                        String.format("Unexpected API error: %s", error));
        }
    }
}
