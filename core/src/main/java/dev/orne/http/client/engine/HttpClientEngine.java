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
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.CompletionStage;

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
     * Returns the HTTP client's cookie store.
     * 
     * @return The HTTP client's cookie store.
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
    @NotNull CompletionStage<Void> executeHttpRequest(
            @NotNull URI uri,
            @NotNull String method,
            @NotNull HttpRequestCustomizer requestCustomizer,
            @NotNull HttpResponseHandler responseHandler)
    throws HttpClientException;

    /**
     * Returns the first HTTP client engine declared through file
     * {@code META-INF/services/dev.orne.http.client.engine.HttpClientEngine}
     * <p>
     * Note that if multiple HTTP client engine implementations are in the
     * class-path the first implementation instantiated by
     * {@code ServiceLoader} will be returned. To choose a specific engine
     * when multiple implementations are available use client's constructor
     * that accepts a engine as argument.
     * 
     * @return The HTTP client engine to use.
     */
    static @NotNull HttpClientEngine fromSpi() {
        final ServiceLoader<HttpClientEngine> loader = ServiceLoader.load(HttpClientEngine.class);
        final Iterator<HttpClientEngine> iterator = loader.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            throw new IllegalStateException("No HTTP service client engine in classpath");
        }
    }
}
