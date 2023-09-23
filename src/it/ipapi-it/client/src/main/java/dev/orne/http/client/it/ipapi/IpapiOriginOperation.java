package dev.orne.http.client.it.ipapi;

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

import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;

import dev.orne.http.ContentType;
import dev.orne.http.MediaTypes;
import dev.orne.http.UriBuilder;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpResponseHandlingException;
import dev.orne.http.client.body.DelegatedHttpRequestBodyParser;
import dev.orne.http.client.body.HttpResponseBodyParser;
import dev.orne.http.client.body.JacksonHttpBody;
import dev.orne.http.client.body.JaxbHttpBody;
import dev.orne.http.client.engine.HttpResponse;
import dev.orne.http.client.engine.HttpResponseBody;
import dev.orne.http.client.op.AuthenticatedOperation;

/**
 * Operation for {@code IpAPI} origin IP information retrieval.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see {@linkplain https://ipapi.com/documentation#origin_lookup}
 */
public class IpapiOriginOperation
extends IpapiAbstractOperation<IpapiParams, IpapiStandardOperation.Response, IpapiResult>
implements AuthenticatedOperation<IpapiParams, IpapiResult, IpapiClientStatus> {

    /** The operation URI, relative to API base URI. */
    public static final String OPERATION_URI = "check";

    /** The response body entity parser. */
    private final @NotNull HttpResponseBodyParser<IpapiStandardOperation.Response> PARSER;

    /**
     * Creates a new instance.
     * <p>
     * This class has no state, so multiple instances have no sense.
     */
    public IpapiOriginOperation() {
        super();
        try {
            PARSER = new DelegatedHttpRequestBodyParser<>(
                    ContentType.of(MediaTypes.Application.JSON, StandardCharsets.UTF_8),
                    JacksonHttpBody.parser(IpapiStandardOperation.Response.class),
                    JaxbHttpBody.parser(IpapiStandardOperation.ErrorResponse.class));
        } catch (HttpClientException e) {
            throw new IllegalStateException("Error creating operation parsers", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureOperationUri(
            final IpapiParams params,
            final @NotNull IpapiClientStatus status,
            final @NotNull UriBuilder builder) {
        builder.setPath(OPERATION_URI);
        super.configureOperationUri(params, status, builder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected IpapiStandardOperation.Response parseResponse(
            final IpapiParams params,
            final @NotNull IpapiClientStatus status,
            final @NotNull HttpResponse response,
            final @NotNull HttpResponseBody body)
    throws HttpResponseHandlingException {
        return body.parse(PARSER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected IpapiResult processResponse(
            final IpapiParams params,
            final @NotNull IpapiClientStatus status,
            final IpapiStandardOperation.Response entity,
            final @NotNull HttpResponse response)
    throws HttpClientException {
        if (entity instanceof IpapiError) {
            throw processApiError((IpapiError) entity, response);
        }
        return (IpapiResult) entity;
    }
}
