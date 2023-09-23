package dev.orne.http.client.example.ipify;

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

import javax.validation.constraints.NotNull;

import dev.orne.http.Headers;
import dev.orne.http.Methods;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpResponseHandlingException;
import dev.orne.http.client.body.StringHttpBody;
import dev.orne.http.client.body.StringHttpResponseBodyParser;
import dev.orne.http.client.engine.HttpRequest;
import dev.orne.http.client.engine.HttpResponse;
import dev.orne.http.client.engine.HttpResponseBody;
import dev.orne.http.client.op.AbstractStatusIndependentOperation;

/**
 * Example operation of {@code Ipify} "get public IP address as text" request.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see {@linkplain https://www.ipify.org/}
 */
public class IpifyGetPublicIp
extends AbstractStatusIndependentOperation<Void, String, String> {

    /** The operation's URI, relative to service's base URI. */
    public static final String OP_URI = "";

    /** The shared instance. */
    public static final IpifyGetPublicIp INSTANCE = new IpifyGetPublicIp();

    /**
     * Private constructor.
     * <p>
     * Only one instance is needed, as the class has no internal status and is thread
     * safe.
     */
    private IpifyGetPublicIp() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull URI getRequestURI(Void params) throws HttpClientException {
        return URI.create(OP_URI);
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
            Void params,
            @NotNull HttpRequest request) throws HttpClientException {
        request.addHeader(
                Headers.Request.ACCEPT_ENCODING,
                StringHttpResponseBodyParser.DEFAULT_CONTENT_TYPE.getHeader());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String parseResponse(
            Void params,
            @NotNull HttpResponse response,
            @NotNull HttpResponseBody body)
    throws HttpResponseHandlingException {
        return StringHttpBody.parse(body);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String processResponse(
            Void params,
            String ip,
            @NotNull HttpResponse response)
    throws HttpResponseHandlingException {
        return ip;
    }
}
