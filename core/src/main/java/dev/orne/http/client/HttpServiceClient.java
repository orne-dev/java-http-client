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

import java.io.Closeable;
import java.net.URI;
import java.util.concurrent.CompletionStage;

import javax.validation.constraints.NotNull;

import dev.orne.http.client.cookie.CookieStore;
import dev.orne.http.client.engine.HttpClientEngine;
import dev.orne.http.client.op.StatusIndependentOperation;

/**
 * HTTP service client interface.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public interface HttpServiceClient
extends Closeable {

    /**
     * Returns the HTTP service's base URI.
     * 
     * @return The HTTP service's base URI
     */
    @NotNull URI getBaseURI();

    /**
     * Returns the HTTP client's cookie store.
     * 
     * @return The HTTP client's cookie store
     */
    @NotNull CookieStore getCookieStore();

    /**
     * Returns the HTTP client engine.
     * 
     * @return The HTTP client engine.
     */
    @NotNull HttpClientEngine getEngine();

    /**
     * Executes the specified status unaware operation for this HTTP service.
     * 
     * @param <P> The operation's parameter type
     * @param <R> The operation execution's result type
     * @param operation The operation to execute
     * @param params The operation parameter
     * @return The operation execution's result
     */
    <P, R> @NotNull CompletionStage<R> execute(
            @NotNull StatusIndependentOperation<P, R> operation,
            P params);
}
