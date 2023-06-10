package dev.orne.http.client.engine;

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
import java.util.concurrent.Future;

import javax.validation.constraints.NotNull;

import dev.orne.http.client.CookieStore;
import dev.orne.http.client.HttpClientException;

/**
 * Interface for abstraction of HTTP client used.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public interface HttpClientEngine {

    /**
     * @return The HTTP client's cookie store
     */
    @NotNull CookieStore getCookieStore();

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
    @NotNull Future<Void> executeHttpRequest(
            @NotNull URI uri,
            @NotNull String method,
            HttpRequestHeadersSupplier headers,
            HttpRequestBodySupplier body,
            @NotNull HttpResponseHandler handler)
    throws HttpClientException;
}
