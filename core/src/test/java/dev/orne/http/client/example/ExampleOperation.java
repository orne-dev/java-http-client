package dev.orne.http.client.example;

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
import dev.orne.http.MediaTypes;
import dev.orne.http.Methods;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpResponseHandlingException;
import dev.orne.http.client.body.JacksonHttpBody;
import dev.orne.http.client.engine.HttpRequest;
import dev.orne.http.client.engine.HttpResponse;
import dev.orne.http.client.engine.HttpResponseBody;
import dev.orne.http.client.op.AbstractStatusDependentOperation;

public class ExampleOperation
extends AbstractStatusDependentOperation<
        ExampleOperation.Params,
        ExampleOperation.Status,
        ExampleOperation.Response,
        ExampleOperation.Result> {

    @Override
    protected @NotNull URI getRequestURI(
            Params params,
            Status status)
    throws HttpClientException {
        return URI.create("test");
    }

    @Override
    protected @NotNull String getRequestMethod() {
        return Methods.POST;
    }

    @Override
    protected void prepareRequest(
            Params params,
            Status status,
            @NotNull HttpRequest request)
    throws HttpClientException {
        Request requestEntity = new Request();
        request.addHeader(Headers.Request.ACCEPT, MediaTypes.Application.JSON);
        JacksonHttpBody.produce(requestEntity, request);
    }

    @Override
    protected Response parseResponse(
            Params params,
            Status status,
            @NotNull HttpResponse response,
            @NotNull HttpResponseBody body)
    throws HttpResponseHandlingException {
        return JacksonHttpBody.parse(body, Response.class);
    }

    @Override
    protected Result processResponse(
            Params params,
            Status status,
            Response entity,
            @NotNull HttpResponse response) {
        return new Result();
    }

    public static class Params {}
    public static class Status {}
    public static class Request {}
    public static class Response {}
    public static class Result {}
}
