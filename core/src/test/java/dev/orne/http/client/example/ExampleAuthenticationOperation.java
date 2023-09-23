package dev.orne.http.client.example;

import java.io.Serializable;

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
import dev.orne.http.client.op.AuthenticationOperation;

public class ExampleAuthenticationOperation
extends AbstractStatusDependentOperation<
        ExampleAuthenticationOperation.Credentials,
        ExampleStatus,
        ExampleAuthenticationOperation.Response,
        Void>
implements AuthenticationOperation<
        ExampleAuthenticationOperation.Credentials,
        Void,
        ExampleStatus>{

    @Override
    protected @NotNull URI getRequestURI(
            Credentials params,
            ExampleStatus status)
    throws HttpClientException {
        return URI.create("test");
    }

    @Override
    protected @NotNull String getRequestMethod() {
        return Methods.POST;
    }

    @Override
    protected void prepareRequest(
            Credentials params,
            ExampleStatus status,
            @NotNull HttpRequest request)
    throws HttpClientException {
        Request requestEntity = new Request();
        requestEntity.setUser(params.getUsername());
        requestEntity.setPassword(params.getPassword());
        request.addHeader(Headers.Request.ACCEPT, MediaTypes.Application.JSON);
        JacksonHttpBody.produce(requestEntity, request);
    }

    @Override
    protected Response parseResponse(
            Credentials params,
            ExampleStatus status,
            @NotNull HttpResponse response,
            @NotNull HttpResponseBody body)
    throws HttpResponseHandlingException {
        return JacksonHttpBody.parse(body, Response.class);
    }

    @Override
    protected Void processResponse(
            Credentials params,
            ExampleStatus status,
            Response entity,
            @NotNull HttpResponse response) {
        status.setAuthenticationToken(entity.getToken());
        return null;
    }

    public static class Credentials
    implements Serializable {
        private static final long serialVersionUID = 1L;
        private String username;
        private String password;
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }
    public static class Request {
        private String user;
        private String password;
        public String getUser() {
            return user;
        }
        public void setUser(String user) {
            this.user = user;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }
    public static class Response {
        private String token;
        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
    }
}
