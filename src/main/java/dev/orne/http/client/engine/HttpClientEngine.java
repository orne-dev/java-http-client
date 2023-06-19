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

import java.io.Closeable;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

import javax.validation.constraints.NotNull;

import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.cookie.CookieStore;

/**
 * Interface for abstraction of HTTP client used.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public interface HttpClientEngine
extends Closeable {

    /**
     * @return The HTTP client's cookie store
     */
    @NotNull CookieStore getCookieStore();

    /**
     * Executes the operation's HTTP request.
     * 
     * @param uri The HTTP request target absolute URI.
     * @param method The HTTP request method.
     * @param requestCustomizer The HTTP request customizer.
     * @param responseHandler The HTTP response handler.
     * @return The future HTTP request result.
     * @throws HttpClientException If an exception occurs executing the
     * request
     */
    @NotNull CompletableFuture<Void> executeHttpRequest(
            @NotNull URI uri,
            @NotNull String method,
            @NotNull HttpRequestCustomizer requestCustomizer,
            @NotNull HttpResponseHandler responseHandler)
    throws HttpClientException;
}
