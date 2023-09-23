package dev.orne.http.client.example;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2020 - 2023 Orne Developments
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
import java.net.URL;

import javax.validation.constraints.NotNull;

import dev.orne.http.client.BaseAuthenticableHttpServiceClient;
import dev.orne.http.client.engine.HttpClientEngine;
import dev.orne.http.client.example.ExampleAuthenticationOperation.Credentials;
import dev.orne.http.client.op.AuthenticationOperation;
import dev.orne.http.client.op.StatusInitOperation;

public class ExampleClient
extends BaseAuthenticableHttpServiceClient<
        ExampleStatus,
        ExampleAuthenticationOperation.Credentials> {

    public ExampleClient(
            @NotNull HttpClientEngine engine,
            @NotNull URI baseURI) {
        this(engine, baseURI,
                new ExampleStatusInitOperation(),
                new ExampleAuthenticationOperation());
    }

    public ExampleClient(
            @NotNull HttpClientEngine engine,
            @NotNull URL baseURL)
            throws URISyntaxException {
        this(engine, baseURL,
                new ExampleStatusInitOperation(),
                new ExampleAuthenticationOperation());
    }

    protected ExampleClient(
            @NotNull HttpClientEngine engine,
            @NotNull URI baseURI,
            @NotNull StatusInitOperation<ExampleStatus> statusInitOperation,
            @NotNull AuthenticationOperation<Credentials, ?, ExampleStatus> authenticationOperation) {
        super(engine, baseURI, statusInitOperation, authenticationOperation);
    }

    protected ExampleClient(
            @NotNull HttpClientEngine engine,
            @NotNull URL baseURL,
            @NotNull StatusInitOperation<ExampleStatus> statusInitOperation,
            @NotNull AuthenticationOperation<Credentials, ?, ExampleStatus> authenticationOperation)
            throws URISyntaxException {
        super(engine, baseURL, statusInitOperation, authenticationOperation);
    }
}
